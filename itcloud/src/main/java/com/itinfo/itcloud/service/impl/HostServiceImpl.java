package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItHostService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class HostServiceImpl implements ItHostService {

    @Autowired
    private AdminConnectionService adminConnectionService;


    @Override
    public List<HostVo> getList() {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<HostVo> hostVoList = new ArrayList<>();
        HostVo hostVo = null;

        List<Host> hostList = ((HostsService.ListResponse)systemService.hostsService().list().send()).hosts();

        for(Host host : hostList){
            hostVo = new HostVo();

            hostVo.setId(host.id());
            hostVo.setName(host.name());
            hostVo.setComment(host.comment());
            hostVo.setAddress(host.address());
            hostVo.setStatus(host.status().value());

            Cluster cluster =
                    ((ClusterService.GetResponse)systemService.clustersService().clusterService(host.cluster().id()).get().send()).cluster();
            DataCenter dataCenter =
                    ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send()).dataCenter();
            hostVo.setClusterId(host.cluster().id());
            hostVo.setClusterName(cluster.name());
            hostVo.setDatacenterId(cluster.dataCenter().id());
            hostVo.setDatacenterName(dataCenter.name());

            List<Vm> vmList =
                    ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();
            int vmsCnt = 0;
            for(Vm vm : vmList){
                if(vm.host() != null && vm.host().id().equals(host.id())){
                    vmsCnt++;
                }
            }
            hostVo.setVmCnt(vmsCnt);
            hostVo.setSpmStatus(host.spm().status().value());
            hostVoList.add(hostVo);
        }
        return hostVoList;
    }


    @Override
    public HostVo getInfo(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        HostVo hostVo = new HostVo();
        hostVo.setId(id);

        Host host = ((HostService.GetResponse)systemService.hostsService().hostService(id).get().send()).host();

        hostVo.setName(host.name());
        hostVo.setAddress(host.address()); //호스트 ip
        hostVo.setSpmPriority(host.spm().priorityAsInteger());  // spm 우선순위

        int cpu = host.cpu().topology().coresAsInteger()
                * host.cpu().topology().socketsAsInteger()
                * host.cpu().topology().threadsAsInteger();
        hostVo.setCpuCnt(cpu); // 논리 cpu 코어수

        if(host.iscsiPresent()){    // host.iscsi() != null
            hostVo.setIscsi(host.iscsi().initiator());  // iscsi 게시자 이름
        }

        hostVo.setKdump(host.kdumpStatus().value());  // kdump intergration status
        hostVo.setDevicePassThrough(host.devicePassthrough().enabled());    // 장치통과
        hostVo.setMemoryMax(host.maxSchedulingMemory());      // 최대 여유 메모리

        //메모리 페이지 공유  비활성


        // 자동으로 페이지를 크게 (확실하지 않음. 매우)
//        hostVo.setPageSize(host.transparentHugePages().enabled());

        // selinux모드
        hostVo.setSeLinux(host.seLinux().mode().value());

        // 클러스터 호환버전

        // 온라인 논리 cpu 코어 수 ?
        // https://192.168.0.70/ovirt-engine/api/hosts/3bbd27b9-13d8-4fff-ad29-c0350994ca88/numanodes


        List<Statistic> statisticList =
                ((StatisticsService.ListResponse)systemService.hostsService().hostService(id).statisticsService().list().send()).statistics();

        for(Statistic statistic : statisticList){
            // 물리메모리
            if(statistic.name().equals("memory.total")){
                hostVo.setMemory(statistic.values().get(0).datum().toBigInteger());
            }
            if(statistic.name().equals("memory.used")){
                hostVo.setMemoryUsed(statistic.values().get(0).datum().toBigInteger());
            }
            if(statistic.name().equals("memory.free")){
                hostVo.setMemoryFree(statistic.values().get(0).datum().toBigInteger());
            }
            // 공유메모리?
//            if(statistic.name().equals("memory.shared")){
//                hostVo.setMemoryShared(statistic.values().get(0).datum().toBigInteger());
//            }

            // swap 크기
            if(statistic.name().equals("swap.total")){
                hostVo.setSwapTotal(statistic.values().get(0).datum().toBigInteger());
            }
            if(statistic.name().equals("swap.used")){
                hostVo.setSwapUsed(statistic.values().get(0).datum().toBigInteger());
            }
            if(statistic.name().equals("swap.free")){
                hostVo.setSwapFree(statistic.values().get(0).datum().toBigInteger());
            }

            // 부팅시간
            if(statistic.name().equals("boot.time")){
                long b = statistic.values().get(0).datum().longValue() *1000;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. aaa HH:mm:ss");
                Date now = new Date(b);
                hostVo.setBootingTime(sdf.format(now));
            }


            // Huge pages(size:free/total)
//            if(statistic.name().equals("hugepages.2048.free")){
//                a = statistic.values().get(0).datum().intValue();
//            }
//            if(statistic.name().equals("hugepages.2048.total")){
//                aa =  statistic.values().get(0).datum().intValue();
//            }
//
//            if(statistic.name().equals("hugepages.1048576.free")){
//                b = statistic.values().get(0).datum().intValue();
//            }
//            if(statistic.name().equals("hugepages.1048576.total")){
//                bb = statistic.values().get(0).datum().intValue();
//            }
        }

        // 코드 수정 필요
//        hostVo.setHugePagesType("2048: " + a + "/" + aa);
//        hostVo.setHugePagesType2("1048576: " + b + "/" + b);


        // 가상머신 수
        List<Vm> vmList =
                ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();

        int vmsUpCnt = 0;
        for(Vm vm : vmList){
            if(vm.host()!= null && vm.host().id().equals(host.id()) && vm.status().value().equals("up")){
                vmsUpCnt++;
            }
        }

        hostVo.setVmUpCnt(vmsUpCnt);
        getHardWare(systemService, hostVo, id);
        getSoftWare(systemService, hostVo, id);

        return hostVo;
    }

    public void getHardWare(SystemService systemService, HostVo hostVo, String id){
        Host host =
                ((HostService.GetResponse)systemService.hostsService().hostService(id).get().send()).host();

        HostHwVo hostHwVo = new HostHwVo();

        hostHwVo.setFamily(host.hardwareInformation().family());            // 생산자
        hostHwVo.setManufacturer(host.hardwareInformation().manufacturer());    // 제품군
        hostHwVo.setProductName(host.hardwareInformation().productName());      // 제품 이름
        hostHwVo.setHwVersion(host.hardwareInformation().version());         // 버전
        hostHwVo.setCpuName(host.cpu().name());     // cpu 모델
        hostHwVo.setCpuType(host.cpu().type());      // cpu 유형
        hostHwVo.setUuid(host.hardwareInformation().uuid());       // uuid
        hostHwVo.setSerialNum(host.hardwareInformation().serialNumber());   // 일련번호

        hostHwVo.setCpuSocket(host.cpu().topology().socketsAsInteger());      // cpu 소켓
        hostHwVo.setCoreThread(host.cpu().topology().threadsAsInteger());     // 코어당 cpu 스레드
        hostHwVo.setCoreSocket(host.cpu().topology().coresAsInteger());     // 소켓당 cpu 코어

        hostVo.setHostHwVo(hostHwVo);
    }


    // 구하는 방법이 db밖에는 없는건지 확인필요
    public void getSoftWare(SystemService systemService, HostVo hostVo, String id){
        Host host =
                ((HostService.GetResponse)systemService.hostsService().hostService(id).get().send()).host();

        HostSwVo hostSwVo = new HostSwVo();

        hostSwVo.setOsVersion(host.os().type() + " " + host.os().version().fullVersion());  // os 버전
//        hostSwVo.setOsInfo(host.os());   // os 정보
        hostSwVo.setKernalVersion(host.os().reportedKernelCmdline());   // 커널 버전 db 수정해야함
        // kvm 버전 db
        hostSwVo.setLibvirtVersion(host.libvirtVersion().fullVersion());    // LIBVIRT 버전
        hostSwVo.setVdsmVersion(host.version().fullVersion());  // VDSM 버전 db
        // SPICE 버전
        // GlusterFS 버전
        // CEPH 버전
        // Open vSwitch 버전
        // Nmstate 버전

        hostVo.setHostSwVo(hostSwVo);
    }


    @Override
    public List<VmVo> getVm(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<VmVo> vmVoList = new ArrayList<>();
        VmVo vmVo = null;
        Date now = new Date(System.currentTimeMillis());

        List<Vm> vmList =
                ((VmsService.ListResponse) systemService.vmsService().list().send()).vms();

        for (Vm vm : vmList) {
            if (vm.hostPresent() && vm.host().id().equals(id)) {
                vmVo = new VmVo();

                vmVo.setHostName( ((HostService.GetResponse)systemService.hostsService().hostService(vm.host().id()).get().send()).host().name() );
                vmVo.setId(vm.id());
                vmVo.setName(vm.name());
                vmVo.setClusterName( ((ClusterService.GetResponse)systemService.clustersService().clusterService(vm.cluster().id()).get().send()).cluster().name() );
                vmVo.setStatus(vm.status().value());
                vmVo.setFqdn(vm.fqdn());

//                vmVo.setStartTime(vm.startTimePresent() ? vm.startTime() : null);

                // uptime 계산
                if(vm.status().value().equals("up") && vm.startTimePresent()) {
                    vmVo.setUpTime( (now.getTime() - vm.startTime().getTime()) / (1000*60*60*24) );
                }
                else if(vm.status().value().equals("up") && !vm.startTimePresent() && vm.creationTimePresent()) {
                    vmVo.setUpTime( (now.getTime() - vm.creationTime().getTime()) / (1000*60*60*24) );
                }

                if(!vm.status().value().equals("down")){
                    // ipv4 부분. vms-nic-reporteddevice
                    List<Nic> nicList =
                            ((VmNicsService.ListResponse) systemService.vmsService().vmService(vm.id()).nicsService().list().send()).nics();

                    for (Nic nic : nicList){
                        List<ReportedDevice> reportedDeviceList
                                = ((VmReportedDevicesService.ListResponse)systemService.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).reportedDevicesService().list().send()).reportedDevice();
                        for (ReportedDevice r : reportedDeviceList){
                            vmVo.setIpv4(r.ips().get(0).address());
                            vmVo.setIpv6(r.ips().get(1).address());
                        }
                    }
                }else{
                    vmVo.setIpv4("");
                    vmVo.setIpv6("");
                }
                vmVoList.add(vmVo);
            } else if(vm.placementPolicyPresent() && vm.placementPolicy().hosts().get(0).id().equals(id)){
                vmVo = new VmVo();
                vmVo.setId(vm.id());
                vmVo.setName(vm.name());
                vmVo.setStatus(vm.status().value());

                vmVoList.add(vmVo);
            }
        }
        return vmVoList;
    }

    // 이해안감, nic의 개수가 무조건 한개인지
    @Override
    public List<NicVo> getNic(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();


        List<NicVo> nicVoList = new ArrayList<>();
        NicVo nicVo = null;
        List<HostNic> hostNicList =
                ((HostNicsService.ListResponse)systemService.hostsService().hostService(id).nicsService().list().send()).nics();

        for(HostNic hostNic : hostNicList){
            nicVo = new NicVo();

            nicVo.setId(hostNic.id());
            nicVo.setName(hostNic.name());
            nicVo.setMacAddress(hostNic.mac().address());
            nicVo.setSpeed(hostNic.speed());

            nicVoList.add(nicVo);
        }
        return nicVoList;
    }

    @Override
    public List<HostDeviceVo> getHostDevice(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<HostDeviceVo> hostDeviceVoList = new ArrayList<>();
        HostDeviceVo hostDeviceVo = null;

        List<HostDevice> hostDeviceList =
                ((HostDevicesService.ListResponse)systemService.hostsService().hostService(id).devicesService().list().send()).devices();

        for(HostDevice hostDevice : hostDeviceList){
            hostDeviceVo = new HostDeviceVo();
            hostDeviceVo.setName(hostDevice.name());
            hostDeviceVo.setCapability(hostDevice.capability());

            if(hostDevice.vendor() != null){
                hostDeviceVo.setVendor(hostDevice.vendor().name());
            }
            if(hostDevice.product() != null){
                hostDeviceVo.setProduct(hostDevice.product().name());
            }
            if(hostDevice.driver() != null){
                hostDeviceVo.setDriver(hostDevice.driver());
            }
            hostDeviceVoList.add(hostDeviceVo);
        }
        return hostDeviceVoList;
    }

    @Override
    public List<PermissionVo> getPermission(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList =
                ((AssignedPermissionsService.ListResponse)systemService.hostsService().hostService(id).permissionsService().list().send()).permissions();

        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            if(permission.groupPresent() && !permission.userPresent()){
                Group group = ((GroupService.GetResponse)systemService.groupsService().groupService(permission.group().id()).get().send()).get();
                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                // 생성일의 경우 db에서 가져와야함

                Role role = ((RoleService.GetResponse)systemService.rolesService().roleService(permission.role().id()).get().send()).role();
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            if(permission.userPresent() && !permission.groupPresent()){
                User user = ((UserService.GetResponse)systemService.usersService().userService(permission.user().id()).get().send()).user();
                pVo.setUser(user.name());
                pVo.setNameSpace(user.namespace());

                Role role = ((RoleService.GetResponse)systemService.rolesService().roleService(permission.role().id()).get().send()).role();
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    @Override
    public List<AffinityLabelVo> getAffinitylabels(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<AffinityLabelVo> alVoList = new ArrayList<>();
        AffinityLabelVo alVo = null;

        List<AffinityLabel> affinityLabelList =
                ((AssignedAffinityLabelsService.ListResponse)systemService.hostsService().hostService(id).affinityLabelsService().list().send()).label();

        for(AffinityLabel a : affinityLabelList) {
            alVo = new AffinityLabelVo();

            alVo.setId(a.id());

            alVoList.add(alVo);
        }
        return alVoList;
    }

    @Override
    public List<EventVo> getEvent(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;

        // 2024. 1. 4. PM 04:01:21
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<Event> eventList =
                ((EventsService.ListResponse)systemService.eventsService().list().send()).events();

        for(Event event : eventList){
            // index 446에서 문제가 있아

            if(event.hostPresent() && event.host().id() == null){
                continue;
            } else if(event.hostPresent() && event.host().id().equals(id)){
                eVo = new EventVo();

                eVo.setSeverity(event.severity().value());
                eVo.setTime(sdf.format(event.time()));
                eVo.setMessage(event.description());
                eVo.setRelationId(event.correlationIdPresent() ? event.correlationId() : null);
                eVo.setSource(event.origin());

                eVoList.add(eVo);
            }
        }
        return eVoList;
    }


}
