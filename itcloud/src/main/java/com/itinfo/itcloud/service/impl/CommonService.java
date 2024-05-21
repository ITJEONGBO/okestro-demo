package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.VmVo;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonService {

    
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


    
    /**
     * Affinity
     * 해당 클러스터에 있는 모든 호스트 출력
     * setAffinityDefaultInfo
     * @param hostList
     * @param clusterId 클러스터 아이디 비교
     * @return 호스트 리스트
     */
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

    public List<IdentifiedVo> setHostList(List<Host> hostList, String clusterId){
        return hostList.stream()
                .filter(host -> host.cluster().id().equals(clusterId))
                .map(host ->
                        IdentifiedVo.builder()
                                .id(host.id())
                                .name(host.name())
                                .build()
                )
                .collect(Collectors.toList());
    }


    /**
     * Affinity
     * 해당 클러스터에 있는 모든 가상머신 출력
     * @param vmList
     * @param clusterId
     * @return 가상머신 리스트
     */
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

    public List<IdentifiedVo> setVmList(List<Vm> vmList, String clusterId){
        return vmList.stream()
                .filter(vm -> vm.cluster().id().equals(clusterId))
                .map(vm ->
                        IdentifiedVo.builder()
                                .id(vm.id())
                                .name(vm.name())
                                .build()
                )
                .collect(Collectors.toList());
    }

    public List<IdentifiedVo> setLabel(SystemService system) {
        List<AffinityLabel> alList = system.affinityLabelsService().list().send().labels();

        return alList.stream()
                .map(affinityLabel ->
                        IdentifiedVo.builder()
                            .id(affinityLabel.id())
                            .name(affinityLabel.name())
                            .build()
                )
                .collect(Collectors.toList());
    }





    /**
     * AffinityLabel
     * 선호도  - 레이블 아이디와 이름 얻기
     * @param system
     * @param alId
     * @return 선호도 레이블 id, 이름
     */
    public List<AffinityLabelVo> getLabelName(SystemService system, String alId){
        List<AffinityLabel> alList = system.affinityLabelsService().list().send().labels();

        return alList.stream()
                .filter(al -> al.id().equals(alId))
                .map(al ->
                        AffinityLabelVo.builder()
                                .id(al.id())
                                .name(al.name())
                                .build())
                .collect(Collectors.toList());
    }
    

    /**
     * 선호도 그룹이 가지고 있는 호스트 리스트 출력
     * @param system
     * @param clusterId
     * @param agId
     * @return 클러스터 밑의 호스트 리스트
     */
    public List<HostVo> getAgHostList(SystemService system, String clusterId, String agId){
        List<Host> hostList = system.clustersService().clusterService(clusterId).affinityGroupsService().groupService(agId).hostsService().list().send().hosts();
        return hostList.stream()
                .map(host ->
                        HostVo.builder()
                                .id(host.id())
                                .name(host.name())
                                .build())
                .collect(Collectors.toList());
    }


    /**
     * 선호도 그룹이 가지고 있는 가상머신 리스트 출력
     * @param system
     * @param clusterId
     * @param agId
     * @return 클러스터 밑의 가상머신 리스트
     */
    public List<VmVo> getAgVmList(SystemService system, String clusterId, String agId){
        List<Vm> vmList = system.clustersService().clusterService(clusterId).affinityGroupsService().groupService(agId).vmsService().list().send().vms();

        return vmList.stream()
                .map(vm ->
                        VmVo.builder()
                                .id(vm.id())
                                .name(vm.name())
                                .build())
                .collect(Collectors.toList());
    }



    /**
     * 선호도 레이블에 있는 호스트 출력
     * List를 매개변수로 주지못한 이유: 이름출력이 안됨
     * @param system
     * @param alid 선호도 레이블 아이디
     * @return 선호도 레이블이 가지고 있는 host 리스트
     */
    public List<HostVo> getHostLabelMember(SystemService system, String alid){
        List<Host> hostList = system.affinityLabelsService().labelService(alid).hostsService().list().send().hosts();
        
        return hostList.stream()
                .map(host -> {
                    Host h = system.hostsService().hostService(host.id()).get().send().host();
                    return HostVo.builder()
                            .id(host.id())
                            .name(h.name())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 선호도 레이블에 있는 가상머신 출력
     * List를 매개변수로 주지못한 이유: 이름출력이 안됨
     * @param system
     * @param alid 선호도 레이블 아이디
     * @return 선호도 레이블이 가지고 있는 가상머신 리스트
     */
    public List<VmVo> getVmLabelMember(SystemService system, String alid){
        List<Vm> vmList = system.affinityLabelsService().labelService(alid).vmsService().list().send().vms();
        return vmList.stream()
                .map(vm -> {
                    Vm v = system.vmsService().vmService(vm.id()).get().send().vm();
                    return VmVo.builder()
                            .id(vm.id())
                            .name(v.name())
                            .build();
                })
                .collect(Collectors.toList());
    }


    /**
     * 클러스터 내에 있는 호스트 개수 파악
     * @param hostList
     * @param clusterId host의 clusterId와 비교
     * @param ele up, down, "" 값
     * @return 호스트 개수
     */
    public int getHostCnt(List<Host> hostList, String clusterId, String ele){
        if("up".equals(ele)){
            return (int) hostList.stream()
                    .filter(host -> host.cluster().id().equals(clusterId) && host.status().value().equals("up"))
                    .count();
        }else if("down".equals(ele)){
            return (int) hostList.stream()
                    .filter(host -> host.cluster().id().equals(clusterId) && !host.status().value().equals("up"))
                    .count();
        }else {
            return (int) hostList.stream()
                    .filter(host -> host.cluster().id().equals(clusterId))
                    .count();
        }
    }


    /**
     * 클러스터 내에 있는 가상머신 개수 파악
     * @param vmList
     * @param clusterId vm의 clusterId와 비교
     * @param ele up, down, "" 값
     * @return 가상머신 개수
     */
    public int getVmCnt(List<Vm> vmList, String clusterId, String ele){
        if("up".equals(ele)) {
            return (int) vmList.stream()
                    .filter(vm -> vm.cluster().id().equals(clusterId) && vm.status().value().equals("up"))
                    .count();
        }else if("down".equals(ele)) {
            return (int) vmList.stream()
                    .filter(vm -> vm.cluster().id().equals(clusterId) && !vm.status().value().equals("up"))
                    .count();
        }else{
            return (int) vmList.stream()
                    .filter(vm -> vm.cluster().id().equals(clusterId))
                    .count();
        }
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




}
