package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.ovirt.OvirtService;
import com.itinfo.itcloud.service.ItHostService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class HostServiceImpl implements ItHostService {

    @Autowired private AdminConnectionService admin;
    @Autowired private OvirtService ovirt;

    @Override
    public String getName(String id){
        return ovirt.getName("host", id);
    }

    @Override
    public List<HostVo> getList() {
        SystemService systemService = admin.getConnection().systemService();

        List<HostVo> hostVoList = new ArrayList<>();
        HostVo hostVo = null;

        List<Host> hostList = ((HostsService.ListResponse)systemService.hostsService().list().send()).hosts();
        for(Host host : hostList){
            Cluster cluster = systemService.clustersService().clusterService(host.cluster().id()).get().send().cluster();
            DataCenter dataCenter = systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter();
            List<Vm> vmList = systemService.vmsService().list().send().vms();

            int vmsCnt = 0;
            for(Vm vm : vmList){
                if(vm.host() != null && vm.host().id().equals(host.id())){
                    vmsCnt++;
                }
            }

            hostVo = HostVo.builder()
                    .id(host.id())
                    .name(host.name())
                    .comment(host.comment())
                    .status(host.status().value())
                    .address(host.address())
                    .clusterId(host.cluster().id())
                    .clusterName(cluster.name())
                    .datacenterId(cluster.dataCenter().id())
                    .datacenterName(dataCenter.name())
                    .spmStatus(host.spm().status().value())
                    .vmCnt(vmsCnt)
                    .build();

            hostVoList.add(hostVo);
        }
        return hostVoList;
    }


    @Override
    public HostVo getInfo(String id) {
        SystemService systemService = admin.getConnection().systemService();

        Host host = ((HostService.GetResponse)systemService.hostsService().hostService(id).get().send()).host();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<HostCpuUnit> hcuList = ((HostCpuUnitsService.ListResponse)systemService.hostsService().hostService(id).cpuUnitsService().list().send()).cpuUnits();

        int cpu = host.cpu().topology().coresAsInteger()
                * host.cpu().topology().socketsAsInteger()
                * host.cpu().topology().threadsAsInteger();

        // 온라인 논리 CPU 코어수
        // HostCpuUnit 이 없음 인식안됨
        // https://192.168.0.70/ovirt-engine/api/hosts/3bbd27b9-13d8-4fff-ad29-c0350994ca88/cpuunits
        // https://192.168.0.70/ovirt-engine/api/hosts/3bbd27b9-13d8-4fff-ad29-c0350994ca88/numanodes
        List<Integer> online = new ArrayList<>();
        for(HostCpuUnit hcu : hcuList) {
            online.add(hcu.cpuIdAsInteger());
        }

        BigInteger memory = BigInteger.ZERO, memoryUsed = BigInteger.ZERO, memoryFree = BigInteger.ZERO;
        BigInteger swapTotal = BigInteger.ZERO, swapUsed = BigInteger.ZERO, swapFree = BigInteger.ZERO;
        String bootingTime = "";
        int hugePage2048Free = 0, hugePage2048Total = 0, hugePage1048576Free = 0, hugePage1048576Total = 0;

        List<Statistic> statisticList = systemService.hostsService().hostService(id).statisticsService().list().send().statistics();
        for(Statistic statistic : statisticList){
            // 물리메모리
            if(statistic.name().equals("memory.total")){
                memory = statistic.values().get(0).datum().toBigInteger();
            }
            if(statistic.name().equals("memory.used")){
                memoryUsed = statistic.values().get(0).datum().toBigInteger();
            }
            if(statistic.name().equals("memory.free")){
                memoryFree = statistic.values().get(0).datum().toBigInteger();
            }

            // 공유메모리 / keep
//            if(statistic.name().equals("memory.shared")){
//                memoryShared = statistic.values().get(0).datum().toBigInteger();
//            }

            // swap 크기
            if(statistic.name().equals("swap.total")){
                swapTotal = statistic.values().get(0).datum().toBigInteger();
            }
            if(statistic.name().equals("swap.used")){
                swapUsed = statistic.values().get(0).datum().toBigInteger();
            }
            if(statistic.name().equals("swap.free")){
                swapFree = statistic.values().get(0).datum().toBigInteger();
            }

            // Huge pages(size:free/total)
            if(statistic.name().equals("hugepages.2048.free")){
                hugePage2048Free = statistic.values().get(0).datum().intValue();
            }
            if(statistic.name().equals("hugepages.2048.total")){
                hugePage2048Total = statistic.values().get(0).datum().intValue();
            }

            if(statistic.name().equals("hugepages.1048576.free")){
                hugePage1048576Free = statistic.values().get(0).datum().intValue();
            }
            if(statistic.name().equals("hugepages.1048576.total")){
                hugePage1048576Total = statistic.values().get(0).datum().intValue();
            }

            // 부팅시간
            if(statistic.name().equals("boot.time")){
                long b = statistic.values().get(0).datum().longValue() * 1000;
                Date now = new Date(b);
                bootingTime = sdf.format(now);
            }
        }

        // 가상머신 수
        List<Vm> vmList = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();
        int vmsUpCnt = 0;
        for(Vm vm : vmList){
            if(vm.host()!= null && vm.host().id().equals(host.id()) && vm.status().value().equals("up")){
                vmsUpCnt++;
            }
        }

        return HostVo.builder()
                .id(id)
                .name(host.name())
                .address(host.address())        //호스트 ip
                .spmPriority(host.spm().priorityAsInteger())    // spm 우선순위
                .cpuCnt(cpu)
                .cpuOnline(online)
                .vmUpCnt(vmsUpCnt)
                .iscsi(host.iscsiPresent() ? host.iscsi().initiator() : null)   // iscsi 게시자 이름
                .kdump(host.kdumpStatus().value())      // kdump intergration status
                .devicePassThrough(host.devicePassthrough().enabled())  // 장치통과
                .memoryMax(host.maxSchedulingMemory())    // 최대 여유 메모리.
                .memory(memory)
                .memoryFree(memoryFree)
                .memoryUsed(memoryUsed)
                .swapTotal(swapTotal)
                .swapFree(swapFree)
                .swapUsed(swapUsed)
                .hugePage2048Total(hugePage2048Total)
                .hugePage2048Free(hugePage2048Free)
                .hugePage1048576Total(hugePage1048576Total)
                .hugePage1048576Free(hugePage1048576Free)
                .bootingTime(bootingTime)
                // Hosted Engine HA
                // 메모리 페이지 공유  비활성
                .pageSize(host.transparentHugePages().enabled())    // 자동으로 페이지를 크게 (확실하지 않음. 매우)
                .seLinux(host.seLinux().mode().value())     // selinux모드: disabled, enforcing, permissive
                // 클러스터 호환버전
                .hostHwVo(getHardWare(systemService, id))
                .hostSwVo(getSoftWare(systemService, id))
                .build();
    }

    @Override
    public List<VmVo> getVm(String id) {
        SystemService systemService = admin.getConnection().systemService();

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

                List<Statistic> statisticList =
                        ((StatisticsService.ListResponse)systemService.vmsService().vmService(vm.id()).statisticsService().list().send()).statistics();

                for(Statistic statistic : statisticList) {
                    long hour = 0;
                    if (statistic.name().equals("elapsed.time")) {
                        hour = statistic.values().get(0).datum().longValue() / (60*60);      //시간
                        System.out.println(vm.id() + " " +hour);

                        if(hour > 24){
                            vmVo.setUpTime(hour/24 + "일");
                        }else if( hour > 1 && hour < 24){
                            vmVo.setUpTime(hour + "시간");
                        }else {
                            vmVo.setUpTime( (statistic.values().get(0).datum().longValue() / 60) + "분");
                        }
                    }
                }

//                vmVo.setStartTime(vm.startTimePresent() ? vm.startTime() : null);


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
            } else if(vm.placementPolicy().hostsPresent() && vm.placementPolicy().hosts().get(0).id().equals(id)){
                // vm이 down 상태일 경우
                vmVo = new VmVo();
                vmVo.setId(vm.id());
                vmVo.setName(vm.name());
                vmVo.setStatus(vm.status().value());
                vmVo.setClusterName( ((ClusterService.GetResponse)systemService.clustersService().clusterService(vm.cluster().id()).get().send()).cluster().name() );

                vmVoList.add(vmVo);
            }
        }
        return vmVoList;
    }


    @Override
    public List<NicVo> getNic(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<NicVo> nVoList = new ArrayList<>();
        NicVo nVo = null;
        List<HostNic> hostNicList =
                ((HostNicsService.ListResponse)systemService.hostsService().hostService(id).nicsService().list().send()).nics();

        for(HostNic hostNic : hostNicList){
            nVo = new NicVo();

            nVo.setStatus(hostNic.status().value());
            nVo.setName(hostNic.name());
            nVo.setNetworkName( ((NetworkService.GetResponse)systemService.networksService().networkService(hostNic.network().id()).get().send()).network().name() );

            nVo.setMacAddress(hostNic.mac().address());// 논리 네트워크
            nVo.setIpv4(hostNic.ip().address());
            nVo.setIpv6(hostNic.ipv6().addressPresent() ? hostNic.ipv6().address() : null);

            DecimalFormat df = new DecimalFormat("###,###");
            List<Statistic> statisticList =
                    ((StatisticsService.ListResponse)systemService.hostsService().hostService(id).nicsService().nicService(hostNic.id()).statisticsService().list().send()).statistics();

            for(Statistic statistic : statisticList){
                String st = "";

                if(statistic.name().equals("data.current.rx.bps")){
                    st = df.format( (statistic.values().get(0).datum()).divide(BigDecimal.valueOf(1024*1024)) );
                    nVo.setRxSpeed( st );
                }
                if(statistic.name().equals("data.current.tx.bps")){
                    st = df.format( (statistic.values().get(0).datum()).divide(BigDecimal.valueOf(1024*1024)) );
                    nVo.setTxSpeed( st );
                }
                if(statistic.name().equals("data.total.rx")){
                    st = df.format(statistic.values().get(0).datum());
                    nVo.setRxTotalSpeed( st );
                }
                if(statistic.name().equals("data.total.tx")){
                    st = df.format(statistic.values().get(0).datum());
                    nVo.setTxTotalSpeed( st );
                }

                if(statistic.name().equals("errors.total.rx")){
                    st = df.format(statistic.values().get(0).datum());
                    nVo.setStop( st );
                }
            }
            // 값 표현 오류
            nVo.setSpeed(String.valueOf(hostNic.speed()));

            nVoList.add(nVo);
        }
        return nVoList;
    }

    @Override
    public List<HostDeviceVo> getHostDevice(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<HostDeviceVo> hostDeviceVoList = new ArrayList<>();
        HostDeviceVo hostDeviceVo = null;

        List<HostDevice> hostDeviceList =
                ((HostDevicesService.ListResponse)systemService.hostsService().hostService(id).devicesService().list().send()).devices();

        for(HostDevice hostDevice : hostDeviceList){
            hostDeviceVo = new HostDeviceVo();
            hostDeviceVo.setName(hostDevice.name());
            hostDeviceVo.setCapability(hostDevice.capability());
            hostDeviceVo.setDriver(hostDevice.driverPresent() ? hostDevice.driver() : null);

            if(hostDevice.vendorPresent()) {
                hostDeviceVo.setVendorName(hostDevice.vendor().name() + " (" +hostDevice.vendor().id() + ")" );
            }

            if(hostDevice.productPresent()){
                hostDeviceVo.setProductName(hostDevice.product().name() + " (" + hostDevice.product().id() + ")");
            }

            hostDeviceVoList.add(hostDeviceVo);
        }
        return hostDeviceVoList;
    }

    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService systemService = admin.getConnection().systemService();

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
                pVo.setProvider(user.domainPresent() ? user.domain().name() : null);

                Role role = ((RoleService.GetResponse)systemService.rolesService().roleService(permission.role().id()).get().send()).role();
                pVo.setRole(role.name());


                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    @Override
    public List<AffinityLabelVo> getAffinitylabels(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<AffinityLabel> affinityLabelList =
                ((AssignedAffinityLabelsService.ListResponse)systemService.hostsService().hostService(id).affinityLabelsService().list().send()).label();
        List<AffinityLabelVo> alVoList = new ArrayList<>();
        AffinityLabelVo alVo = null;


        for(AffinityLabel a : affinityLabelList) {
            alVo = new AffinityLabelVo();

            alVo.setId(a.id());
            alVo.setName(a.name());


            alVoList.add(alVo);
        }
        return alVoList;
    }

    @Override
    public List<EventVo> getEvent(String id) {
        SystemService systemService = admin.getConnection().systemService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");      // 2024. 1. 4. PM 04:01:21

        List<Event> eventList = ((EventsService.ListResponse)systemService.eventsService().list().send()).events();
        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;

        String name = getName(id);

        for(Event event : eventList){
            if(event.hostPresent() && event.host().name().equals(name)){
                eVo = EventVo.builder()
                        .severity(event.severity().value())     // 상태[LogSeverity] : alert, error, normal, warning
                        .time(sdf.format(event.time()))
                        .message(event.description())
                        .relationId(event.correlationIdPresent() ? event.correlationId() : null)
                        .source(event.origin())
                        .build();

                eVoList.add(eVo);
            }
        }
        return eVoList;
    }


    public HostHwVo getHardWare(SystemService systemService, String id){
        Host host = systemService.hostsService().hostService(id).get().send().host();

        return HostHwVo.builder()
                .family(host.hardwareInformation().family())           // 생산자
                .manufacturer(host.hardwareInformation().manufacturer())     // 제품군
                .productName(host.hardwareInformation().productName())      // 제품 이름
                .hwVersion(host.hardwareInformation().version())        // 버전
                .cpuName(host.cpu().name())          // cpu 모델
                .cpuType(host.cpu().type())          // cpu 유형
                .uuid(host.hardwareInformation().uuid())             // uuid
                .serialNum(host.hardwareInformation().serialNumber())        // 일련번호
                .cpuSocket(host.cpu().topology().socketsAsInteger())        // cpu 소켓
                .coreThread(host.cpu().topology().threadsAsInteger())       // 코어당 cpu 스레드
                .coreSocket(host.cpu().topology().coresAsInteger())       // 소켓당 cpu 코어
                .build();
    }


    // 구하는 방법이 db밖에는 없는건지 확인필요
    public HostSwVo getSoftWare(SystemService systemService, String id){
        Host host = systemService.hostsService().hostService(id).get().send().host();

        return HostSwVo.builder()
                .osVersion(host.os().type() + " " + host.os().version().fullVersion())    // os 버전
//                .osInfo(host.os())       // os 정보
                .kernalVersion(host.os().reportedKernelCmdline())// 커널 버전 db 수정해야함
                // kvm 버전 db
                .libvirtVersion(host.libvirtVersion().fullVersion())// LIBVIRT 버전
                .vdsmVersion(host.version().fullVersion())// VDSM 버전 db
                // SPICE 버전
                // GlusterFS 버전
                // CEPH 버전
                // Open vSwitch 버전
                // Nmstate 버전
                .build();
    }

}
