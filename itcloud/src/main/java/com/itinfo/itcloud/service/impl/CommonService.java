package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.PermissionVo;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonService {

    /**
     * Byte -> MB
     *
     * @param memory BigInteger 값을 받는다
     * @return MB로 변환된 값
     */
    public double getMB(BigInteger memory){
        BigInteger convertMb = BigInteger.valueOf(1024).pow(2); // 제곱
        return memory.divide(convertMb).doubleValue();
    }

    
    /**
     * Statistic의 memory, swap memory, speed(rx,tx)
     * vms/{id}/nic/{id}/statistics
     * @param statisticList 보통 vm
     * @param query statistic.name이 들어감
     * @return BigInteger 형식의 값
     */
    public BigInteger getSpeed(List<Statistic> statisticList, String query){
        return statisticList.stream()
                .filter(statistic -> statistic.name().equals(query) && statistic.valuesPresent()) // valuepresent가 애매함
                .map(statistic -> statistic.values().get(0).datum().toBigInteger())
                .findAny()
                .orElse(BigInteger.ZERO);
        // value가 없는 경우도 있음
//        <statistic href="/ovirt-engine/api/vms/e929923d-8710-47ef-bfbd-e281434eb8ee/nics/98273d10-a01f-44b4-809a-c27b9a3504f3/statistics/50b8f057-7795-30bd-825a-1acadd37a9d7" id="50b8f057-7795-30bd-825a-1acadd37a9d7">
//            <name>errors.total.rx</name>
//            <values/>
//        </statistic>
    }
    

    /**
     * Statistic의 hugepage
     * hosts/{hostId}/statistic
     * @param statisticList 보통 vm
     * @param query statistic.name이 들어감
     * @return int형식
     */
    public int getPage(List<Statistic> statisticList, String query) {
        return statisticList.stream()
                .filter(statistic -> statistic.name().equals(query))
                .map(statistic -> statistic.values().get(0).datum().intValue())
                .findAny()
                .orElse(0);
    }


    /**
     * Vm 업타임 구하기
     * 이건 매개변수로 statisticList 안줘도 되는게 vm에서만 사용
     * @param system 
     * @param vmId 
     * @return 일, 시간, 분 형식
     */
    public String getVmUptime(SystemService system, String vmId){
        List<Statistic> statisticList = system.vmsService().vmService(vmId).statisticsService().list().send().statistics();

        long time = statisticList.stream()
                .filter(statistic -> statistic.name().equals("elapsed.time"))
                .mapToLong(statistic -> statistic.values().get(0).datum().longValue())
                .findFirst()
                .orElse(0);

        long days = time / (60 * 60 * 24);
        long hours = (time % (60 * 60 * 24)) / (60 * 60);
        long minutes = ((time % (60 * 60 * 24)) % (60 * 60)) / 60;

        if (days > 0) {
            return days + "일";
        }
        else if (hours > 0) {
            return hours + "시간";
        }else if(minutes > 0){
            return minutes + "분";
        }else{
            return null;
        }
    }


    /**
     * Vm ip 알아내기
     * vms/{id}/nic/{id}/statistic
     * @param system
     * @param vmId  system.vmsService().vmService(vmId).get().send().vm()
     * @param version ipv4, ipv6
     * @return
     */
    public String getVmIp(SystemService system, String vmId, String version){
        Vm vm = system.vmsService().vmService(vmId).get().follow("nics").send().vm();
        return vm.nics().stream()
                .flatMap(nic -> {
                    List<ReportedDevice> reportedDeviceList = system.vmsService().vmService(vmId).nicsService().nicService(nic.id()).reportedDevicesService().list().send().reportedDevice().stream()
                            .filter(r -> !vm.status().value().equals("down"))
                            .collect(Collectors.toList());
                    return reportedDeviceList.stream();
                })
                .findFirst()
                .map(r -> {
                    if ("v4".equals(version)) {
                        return r.ips().get(0).address();
                    } else {
                        return r.ips().get(1).address();
                    }
                })
                .orElse(null);
    }



    // 권한
    public List<PermissionVo> getPermission(SystemService system, List<Permission> permissionList) {
        return permissionList.stream()
                .map(permission -> {
                    Role role = system.rolesService().roleService(permission.role().id()).get().send().role();

                    if(permission.groupPresent() && !permission.userPresent()){
                        Group group = system.groupsService().groupService(permission.group().id()).get().send().get();
                        return PermissionVo.builder()
                                .permissionId(permission.id())
                                .user(group.name())
                                .nameSpace(group.namespace())
                                .role(role.name())
                                .build();
                    }

                    if(!permission.groupPresent() && permission.userPresent()){
                        User user = system.usersService().userService(permission.user().id()).get().send().user();
                        return PermissionVo.builder()
                                .user(user.name())
                                .provider(user.domainPresent() ? user.domain().name() : null)
                                .nameSpace(user.namespace())
                                .role(role.name())
                                .build();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }


    // 이름 중복 검사
    // true=이름중복없음
    public boolean isNameDuplicate(SystemService system, String type, String name, String id) {
        if ("datacenter".equals(type)) {
            return system.dataCentersService().list().send().dataCenters().stream()
                    .filter(dataCenter -> id == null || !dataCenter.id().equals(id))
                    .anyMatch(dataCenter -> dataCenter.name().equals(name));
        } else if ("host".equals(type)) {
            return system.hostsService().list().send().hosts().stream()
                    .filter(host -> id == null || !host.id().equals(id))
                    .anyMatch(host -> host.name().equals(name));
        } else {
            return system.vmsService().list().send().vms().stream()
                    .filter(vm -> id == null || !vm.id().equals(id))
                    .anyMatch(vm -> vm.name().equals(name));
        }
    }






//    public List<VmVo> setVmList(List<Vm> vmList, String clusterId){
//        return vmList.stream()
//                .filter(vm -> vm.cluster().id().equals(clusterId))
//                .map(vm ->
//                        VmVo.builder()
//                                .id(vm.id())
//                                .name(vm.name())
//                                .build()
//                )
//                .collect(Collectors.toList());
//    }
//    public List<HostVo> setHostList(List<Host> hostList, String clusterId){
//        return hostList.stream()
//                .filter(host -> host.cluster().id().equals(clusterId))
//                .map(host ->
//                        HostVo.builder()
//                                .id(host.id())
//                                .name(host.name())
//                                .build()
//                )
//                .collect(Collectors.toList());
//    }

}
