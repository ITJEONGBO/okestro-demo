package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.HostCreateVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.ovirt.OvirtService;
import com.itinfo.itcloud.service.ItHostService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.AgentBuilder;
import org.ovirt.engine.sdk4.builders.HostBuilder;
import org.ovirt.engine.sdk4.builders.SshBuilder;
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

        List<Host> hostList = systemService.hostsService().list().send().hosts();
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

        Host host = systemService.hostsService().hostService(id).get().send().host();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<HostCpuUnit> hcuList = systemService.hostsService().hostService(id).cpuUnitsService().list().send().cpuUnits();

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
        List<Vm> vmList = systemService.vmsService().list().send().vms();
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

        List<Vm> vmList = systemService.vmsService().list().send().vms();

        for (Vm vm : vmList) {
            if (vm.hostPresent() && vm.host().id().equals(id)) {
                vmVo = new VmVo();

                vmVo.setHostName( systemService.hostsService().hostService(vm.host().id()).get().send().host().name() );
                vmVo.setId(vm.id());
                vmVo.setName(vm.name());
                vmVo.setClusterName( systemService.clustersService().clusterService(vm.cluster().id()).get().send().cluster().name() );
                vmVo.setStatus(vm.status().value());
                vmVo.setFqdn(vm.fqdn());

                List<Statistic> statisticList = systemService.vmsService().vmService(vm.id()).statisticsService().list().send().statistics();

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
                    List<Nic> nicList = systemService.vmsService().vmService(vm.id()).nicsService().list().send().nics();

                    for (Nic nic : nicList){
                        List<ReportedDevice> reportedDeviceList
                                = systemService.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).reportedDevicesService().list().send().reportedDevice();
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
                vmVo.setClusterName( systemService.clustersService().clusterService(vm.cluster().id()).get().send().cluster().name() );

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
        List<HostNic> hostNicList = systemService.hostsService().hostService(id).nicsService().list().send().nics();

        for(HostNic hostNic : hostNicList){
            nVo = new NicVo();

            nVo.setStatus(hostNic.status().value());
            nVo.setName(hostNic.name());
            nVo.setNetworkName( systemService.networksService().networkService(hostNic.network().id()).get().send().network().name() );

            nVo.setMacAddress(hostNic.mac().address());// 논리 네트워크
            nVo.setIpv4(hostNic.ip().address());
            nVo.setIpv6(hostNic.ipv6().addressPresent() ? hostNic.ipv6().address() : null);

            DecimalFormat df = new DecimalFormat("###,###");
            List<Statistic> statisticList =
                    systemService.hostsService().hostService(id).nicsService().nicService(hostNic.id()).statisticsService().list().send().statistics();

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

        List<HostDevice> hostDeviceList = systemService.hostsService().hostService(id).devicesService().list().send().devices();

        for(HostDevice hostDevice : hostDeviceList){
            hostDeviceVo = HostDeviceVo.builder()
                    .name(hostDevice.name())
                    .capability(hostDevice.capability())
                    .driver(hostDevice.driverPresent() ? hostDevice.driver() : null)
                    .vendorName(hostDevice.vendorPresent() ? hostDevice.vendor().name() + " (" +hostDevice.vendor().id() + ")" : "")
                    .productName(hostDevice.productPresent() ? hostDevice.product().name() + " (" + hostDevice.product().id() + ")" : "")
                    .build();

            hostDeviceVoList.add(hostDeviceVo);
        }
        return hostDeviceVoList;
    }

    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList = systemService.hostsService().hostService(id).permissionsService().list().send().permissions();

        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            if(permission.groupPresent() && !permission.userPresent()){
                Group group = systemService.groupsService().groupService(permission.group().id()).get().send().get();
                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                // 생성일의 경우 db에서 가져와야함

                Role role = systemService.rolesService().roleService(permission.role().id()).get().send().role();
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            if(permission.userPresent() && !permission.groupPresent()){
                User user = systemService.usersService().userService(permission.user().id()).get().send().user();
                pVo.setUser(user.name());
                pVo.setNameSpace(user.namespace());
                pVo.setProvider(user.domainPresent() ? user.domain().name() : null);

                Role role = systemService.rolesService().roleService(permission.role().id()).get().send().role();
                pVo.setRole(role.name());


                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    @Override
    public List<AffinityLabelVo> getAffinitylabels(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<AffinityLabel> affinityLabelList = systemService.hostsService().hostService(id).affinityLabelsService().list().send().label();
        List<AffinityLabelVo> alVoList = new ArrayList<>();
        AffinityLabelVo alVo = null;

        for(AffinityLabel affinityLabel : affinityLabelList) {
            alVo = AffinityLabelVo.builder()
                    .id(affinityLabel.id())
                    .name(affinityLabel.name())
                    .build();

            alVoList.add(alVo);
        }
        return alVoList;
    }

    @Override
    public List<EventVo> getEvent(String id) {
        SystemService systemService = admin.getConnection().systemService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");      // 2024. 1. 4. PM 04:01:21

        String name = getName(id);
        List<Event> eventList =  systemService.eventsService().list().search("host.name=" + name).send().events();
        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;

        for(Event event : eventList){
            // 이벤트중에 뭐 안뜨는게 있엇음
            eVo = EventVo.builder()
                    .severity(event.severity().value())     // 상태[LogSeverity] : alert, error, normal, warning
                    .time(sdf.format(event.time()))
                    .message(event.description())
                    .relationId(event.correlationIdPresent() ? event.correlationId() : null)
                    .source(event.origin())
                    .build();
            System.out.println(eVo.toString());

            eVoList.add(eVo);
        }
        return eVoList;
    }



    // edit
    @Override
    public HostCreateVo getHostCreate(String id) {
        SystemService systemService = admin.getConnection().systemService();

        Host host = systemService.hostsService().hostService(id).get().send().host();
//        Cluster cluster = systemService.clustersService().clusterService(host.cluster().id()).get().send().cluster();

        log.info("getHostsCreate");

        return HostCreateVo.builder()
                .clusterId(host.cluster().id())
                .id(id)
                .name(host.name())
                .comment(host.comment())
                .hostIp(host.address())
                .sshPort(host.portAsInteger())
                .sshPw(host.rootPassword())
                .spm(host.spm().priorityAsInteger())
                .hostEngine(host.hostedEnginePresent())
                .build();
    }

    @Override
    public boolean addHost(HostCreateVo hostCreateVo) {
        // The name, address, and root_password properties are required.
        SystemService systemService = admin.getConnection().systemService();

        HostsService hostsService = systemService.hostsService();
        List<Host> hostList = systemService.hostsService().list().send().hosts();
        Cluster cluster = systemService.clustersService().clusterService(hostCreateVo.getClusterId()).get().send().cluster();

        try {
            // 고려해야하는 것, ssh port번호, 전원관리 활성 여부(펜스 에이전트가 추가되는지가 달림)
            // sshport가 22면 .ssh() 설정하지 않아도 알아서 지정됨
            Host host = null;
            if (hostCreateVo.getSshPort() == 22) {
                host = new HostBuilder()
                            .name(hostCreateVo.getName())
                            .comment(hostCreateVo.getComment())
                            .address(hostCreateVo.getHostIp())          // 호스트이름/IP
                            .rootPassword(hostCreateVo.getSshPw())   // 암호
                            .cluster(cluster)
                            .build();
            }else {
                host = new HostBuilder()
                            .name(hostCreateVo.getName())
                            .comment(hostCreateVo.getComment())
                            .address(hostCreateVo.getHostIp())          // 호스트이름/IP
                            .ssh(new SshBuilder().port(hostCreateVo.getSshPort()))  // 새로 지정할 포트번호
                            .rootPassword(hostCreateVo.getSshPw())   // 암호
                            .cluster(cluster)
                            .build();
            }

            log.info("----", hostCreateVo.toString());
            // 호스트 엔진 배치작업 선택 (없음/배포)
            // 호스트 생성
            if(hostCreateVo.isHostEngine()){
                hostsService.add().deployHostedEngine(true).host(host).send().host();
            }else {
                hostsService.add().deployHostedEngine(false).host(host).send().host();  // false 생략가능
            }

            if (host.status() == HostStatus.UP) {
                log.info("호스트 추가 완료(" + hostCreateVo.getName() + ")");
            }


            return hostsService.list().send().hosts().size() == ( hostList.size()+1 );
        } catch (Exception e) {
            log.info("error: ", e);
            return false;
        }
    }

    @Override
    public void editHost(HostCreateVo hostCreateVo) {
        SystemService systemService = admin.getConnection().systemService();

        HostService hostService = systemService.hostsService().hostService(hostCreateVo.getId());

//        HostCreateVo srcHostCreateVo = this.retrieveCreateHostInfo(hostCreateVo.getId());
//
//        HostBuilder hostBuilder = new HostBuilder();
//        if (hostCreateVo.getName() != null && !"".equals(hostCreateVo.getName()) && !srcHostCreateVo.getName().equals(hostCreateVo.getName())) {
//            hostBuilder.name(hostCreateVo.getName());
//        }
//
//        if (hostCreateVo.getComment() != null && !"".equals(hostCreateVo.getComment())) {
//            hostBuilder.comment(hostCreateVo.getComment());
//        } else {
//            hostBuilder.comment("");
//        }
//
//        if (hostCreateVo.getSsh() != null && srcHostCreateVo.getSsh().getPort() != hostCreateVo.getSsh().getPort()) {
//            hostBuilder.port(54321).ssh((new SshBuilder()).port(hostCreateVo.getSsh().getPort()));
//        }
//
//        if (!srcHostCreateVo.getClusterId().equals(hostCreateVo.getClusterId())) {
//            Cluster cluster = ((ClusterService.GetResponse)systemService.clustersService().clusterService(hostCreateVo.getClusterId()).get().send()).cluster();
//            hostBuilder.cluster((new ClusterBuilder()).name(cluster.name()));
//        }
//
//        if (hostCreateVo.getFenceAgent() != null) {
//            if ((srcHostCreateVo.isPowerManagementEnabled() || !hostCreateVo.isPowerManagementEnabled() || hostCreateVo.getFenceAgent().getAddress() == null) && !"".equals(hostCreateVo.getFenceAgent().getAddress())) {
//                if (srcHostCreateVo.isPowerManagementEnabled() && !hostCreateVo.isPowerManagementEnabled()) {
//                    hostBuilder.powerManagement((new PowerManagementBuilder()).enabled(false).kdumpDetection(true));
//                }
//            } else {
//                FenceAgentsService fenceAgentsService = hostService.fenceAgentsService();
//                AgentBuilder agentBuilder = new AgentBuilder();
//                agentBuilder.address(hostCreateVo.getFenceAgent().getAddress()).username(hostCreateVo.getFenceAgent().getUsername()).password(hostCreateVo.getFenceAgent().getPassword()).type(hostCreateVo.getFenceAgent().getType()).order(1).encryptOptions(false);
//                Agent agent = ((FenceAgentsService.AddResponse)fenceAgentsService.add().agent(agentBuilder).send()).agent();
//                hostBuilder.powerManagement((new PowerManagementBuilder()).enabled(true).kdumpDetection(true).agents(new Agent[]{agent}));
//            }
//        }
//
//        try {
//            ((HostService.UpdateResponse)hostService.update().host(hostBuilder).send()).host();
//
//            message.setText("호스트 수정 완료(" + hostCreateVo.getName() + ")");
//        } catch (Exception var11) {
//            var11.printStackTrace();
//            message.setText("호스트 수정 실패(" + hostCreateVo.getName() + ")");
//        }
    }

    @Override
    public boolean deleteHost(String id) {
        SystemService systemService = admin.getConnection().systemService();

        HostsService hostsService = systemService.hostsService();
        List<Host> hList = systemService.hostsService().list().send().hosts();
        HostService hostService = systemService.hostsService().hostService(id);
        String name = hostService.get().send().host().name();

        try {
            hostService.remove().send();

            log.info("delete host: {}", name);
            return hostsService.list().send().hosts().size() == ( hList.size() -1 );
        }catch (Exception e){
            // 호스트는 유지보수로 변경하고 해야한다
            log.error("error ", e);
            return false;
        }
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
