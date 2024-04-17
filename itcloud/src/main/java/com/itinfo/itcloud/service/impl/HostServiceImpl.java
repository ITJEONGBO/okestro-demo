package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.HostCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItHostService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.AffinityLabelsService;
import org.ovirt.engine.sdk4.services.HostService;
import org.ovirt.engine.sdk4.services.HostsService;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HostServiceImpl implements ItHostService {

    @Autowired private AdminConnectionService admin;
    private final CommonService commonService = new CommonService();

    // 호스트 목록
    @Override
    public List<HostVo> getList() {
        SystemService system = admin.getConnection().systemService();

        // allContent를 포함해야 hosted Engine의 정보가 나온다
        List<Host> hostList = system.hostsService().list().allContent(true).send().hosts();

        log.info("Host 목록");
        return hostList.stream()
                .map(host -> {
                    Cluster cluster = system.clustersService().clusterService(host.cluster().id()).get().send().cluster();
                    DataCenter dataCenter = system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter();

                    return HostVo.builder()
                                .id(host.id())
                                .name(host.name())
                                .comment(host.comment())
                                .status(host.status().value())
                                .address(host.address())
                                .clusterId(host.cluster().id())
                                .clusterName(cluster.name())
                                .datacenterId(cluster.dataCenter().id())
                                .datacenterName(dataCenter.name())
                                .hostedEngine(host.hostedEnginePresent() ? host.hostedEngine().active() : null)
                                .vmCnt(
                                        (int) system.vmsService().list().send().vms().stream()
                                            .filter(vm -> vm.host() != null && vm.host().id().equals(host.id()))
                                            .count()
                                )
                            .build();
                })
                .collect(Collectors.toList());
    }


    // 호스트 생성 클러스터 리스트 출력
    @Override
    public List<ClusterVo> setHostDefaultInfo() {
        SystemService system = admin.getConnection().systemService();
        List<Cluster> clusterList = system.clustersService().list().send().clusters();

        log.info("Host 생성창");
        return clusterList.stream()
                .map(
                    cluster ->
                        ClusterVo.builder()
                            .id(cluster.id())
                            .name(cluster.name())
                            .datacenterName(system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name())
                        .build()
                )
                .collect(Collectors.toList());
    }

    // 호스트 생성
    @Override
    public CommonVo<Boolean> addHost(HostCreateVo hostCreateVo) {
        // The name, address, and root_password properties are required.
        SystemService system = admin.getConnection().systemService();

        HostsService hostsService = system.hostsService();
        Cluster cluster = system.clustersService().clusterService(hostCreateVo.getClusterId()).get().send().cluster();
//        List<Host> hostList = system.hostsService().list().send().hosts();

        try {
            // 고려해야하는 것, ssh port번호, 전원관리 활성 여부(펜스 에이전트가 추가되는지가 달림)
            // sshport가 22면 .ssh() 설정하지 않아도 알아서 지정됨, sshport 변경을 ovirt에서 해보신적은 없어서 우선 보류

            // 비밀번호 잘못되면 보여줄 코드?
            HostBuilder hostBuilder = new HostBuilder();
            hostBuilder
                    .name(hostCreateVo.getName())
                    .comment(hostCreateVo.getComment())
                    .address(hostCreateVo.getHostIp())          // 호스트이름/IP
                    .rootPassword(hostCreateVo.getSshPw())   // 암호
                    .spm(new SpmBuilder().priority(hostCreateVo.getSpm()))
                    .ssh(new SshBuilder().port(hostCreateVo.getSshPort()))  // 기본값이 22
//                    .hostedEngine(new HostedEngineBuilder().active(hostCreateVo.isHostEngine()))
                    .cluster(cluster)
                    .build();

            // 호스트 엔진 배치작업 선택 (없음/배포)  -> 호스트 생성
            Host host = hostsService.add().deployHostedEngine(hostCreateVo.isHostEngine()).host(hostBuilder).send().host();

            do{
                log.info("Host 생성 (" + hostCreateVo.getName() + ")");
            } while (host.status().equals(HostStatus.UP));
            
//            return hostsService.list().send().hosts().size() == ( hostList.size()+1 );
            return CommonVo.successResponse();
        } catch (Exception e) {
            log.error("error: ", e);
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 호스트 편집창
    @Override
    public HostCreateVo getHostCreate(String id) {
        SystemService system = admin.getConnection().systemService();
        Host host = system.hostsService().hostService(id).get().send().host();
        Cluster cluster = system.clustersService().clusterService(host.cluster().id()).get().send().cluster();

        log.info("Host 편집창");

        return HostCreateVo.builder()
                .clusterId(host.cluster().id())
                .datacenterName(system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name())
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

    // 호스트 편집
    @Override
    public CommonVo<Boolean> editHost(String id, HostCreateVo hostCreateVo) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);

        try {
            Host host = new HostBuilder()
                    .id(id)
                    .name(hostCreateVo.getName())
                    .comment(hostCreateVo.getComment())
                    .spm(new SpmBuilder().priority(hostCreateVo.getSpm()))
                    .build();

            hostService.update().host(host).send().host();

            log.info("Host 편집");
            return CommonVo.successResponse();
        } catch (Exception e) {
            log.error("error ", e);
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 호스트 삭제
    @Override
    public CommonVo<Boolean> deleteHost(String id) {
        SystemService system = admin.getConnection().systemService();

//        List<Host> hList = system.hostsService().list().send().hosts();
//        HostsService hostsService = system.hostsService();
//                return hostsService.list().send().hosts().size() == (hList.size() - 1);
        HostService hostService = system.hostsService().hostService(id);
        Host host = hostService.get().send().host();
        String name = hostService.get().send().host().name();

        try {
            if(host.status().equals(HostStatus.MAINTENANCE)) {
                hostService.remove().send();
                log.info("delete host: {}", name);
                return CommonVo.successResponse();
            }else{
                log.error("유지 보수 후 삭제 해야함");
                return CommonVo.failResponse("fail");
            }
        }catch (Exception e){
            log.error("error ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }


    
    // 유지보수
    @Override
    public void deActive(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);
        Host host = hostService.get().send().host();

        try {
            if(host.status() != HostStatus.MAINTENANCE){
                hostService.deactivate().send();
                log.info("Host 유지보수");
            }
        }catch (Exception e){
            log.error("error ", e);
        }
    }

    // 활성
    @Override
    public void active(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);
        Host host = hostService.get().send().host();

        try {
            if(host.status() != HostStatus.UP){
                hostService.activate().send();
                log.info("Host 활성");
            }
        }catch (Exception e){
            log.error("error ", e);
        }
    }

    // 새로고침
    @Override
    public void refresh(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);

        try{
            hostService.refresh().send();
            log.info("Host 새로고침");
        }catch (Exception e){
            log.error("error ", e);
        }
    }


    // ssh 관리
    // 재시작
    // TODO
    /*
        ssh 관리
        oVirt의 엔진 SDK를 통해 SSH를 통해 호스트를 재부팅하는 기능은 기본적으로 제공되지 않습니다.
        oVirt 엔진 SDK는 호스트를 관리하기 위한 API를 제공하지만, SSH를 통한 호스트 재부팅과 같은 기능은 포함되어 있지 않습니다.
    */
    @Override
    public void reStart(String id) {
        SystemService system = admin.getConnection().systemService();
        HostsService hostsService = system.hostsService();  // reboot
        HostService hostService = system.hostsService().hostService(id);

        // restart 하기 전, maintenance 인지 확인
        try {
//            hostService.fence().fenceType(FenceType.RESTART.value()).send().powerManagement();

            log.info("Host 재시작");
        }catch (Exception e){
            log.error("error: ", e);
        }
    }


    // 중지
    // TODO
    @Override
    public void stop(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);

        try {
//            hostService.get().send().host();

            log.info("Host 증지");
        }catch (Exception e){
            log.error("error: ", e);
        }
    }


    // 일반
    @Override
    public HostVo getInfo(String id) {
        SystemService system = admin.getConnection().systemService();

        Host host = system.hostsService().hostService(id).get().allContent(true).send().host();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        // 온라인 논리 CPU 코어수 - HostCpuUnit 이 없음 인식안됨
        // https://192.168.0.70/ovirt-engine/api/hosts/3bbd27b9-13d8-4fff-ad29-c0350994ca88/cpuunits,numanodes
        List<Statistic> statisticList = system.hostsService().hostService(id).statisticsService().list().send().statistics();

        long bootTime = system.hostsService().hostService(id).statisticsService().list().send().statistics().stream()
                                .filter(statistic -> statistic.name().equals("boot.time"))
                                .map(statistic -> statistic.values().get(0).datum().longValue() * 1000)
                                .findAny()
                                .orElse(0L);

        log.info("Host 일반");
        return HostVo.builder()
                .id(id)
                .name(host.name())
                .address(host.address())        //호스트 ip
                .spmPriority(host.spm().priorityAsInteger())    // spm 우선순위
                // cpu 있으면 출력으로 바꿔야됨
                .cpuCnt(
                        host.cpu().topology().coresAsInteger()
                        * host.cpu().topology().socketsAsInteger()
                        * host.cpu().topology().threadsAsInteger()
                )
                .cpuOnline(
                        system.hostsService().hostService(id).cpuUnitsService().list().send().cpuUnits().stream()
                                .map(HostCpuUnit::cpuIdAsInteger)
                                .collect(Collectors.toList())
                )
                .vmUpCnt(
                        (int) system.vmsService().list().send().vms().stream()
                                .filter(vm -> vm.host()!= null && vm.host().id().equals(host.id()) && vm.status().value().equals("up"))
                                .count()
                )
                .iscsi(host.iscsiPresent() ? host.iscsi().initiator() : null)   // iscsi 게시자 이름
                .kdump(host.kdumpStatus().value())      // kdump intergration status
                .devicePassThrough(host.devicePassthrough().enabled())  // 장치통과
                .memoryMax(host.maxSchedulingMemory())    // 최대 여유 메모리.
                .memory(commonService.getSpeed(statisticList, "memory.total"))
                .memoryFree(commonService.getSpeed(statisticList, "memory.free"))
                .memoryUsed(commonService.getSpeed(statisticList, "memory.used"))
                .memoryShared(commonService.getSpeed(statisticList, "memory.shared")) // 문제잇음
                .swapTotal(commonService.getSpeed(statisticList, "swap.total"))
                .swapFree(commonService.getSpeed(statisticList, "swap.free"))
                .swapUsed(commonService.getSpeed(statisticList, "swap.used"))
                .hugePage2048Total(commonService.getPage(statisticList, "hugepages.2048.total"))
                .hugePage2048Free(commonService.getPage(statisticList, "hugepages.2048.free"))
                .hugePage1048576Total(commonService.getPage(statisticList, "hugepages.1048576.total"))
                .hugePage1048576Free(commonService.getPage(statisticList, "hugepages.1048576.free"))
                .bootingTime(sdf.format(new Date(bootTime)))
                .hostedEngine(host.hostedEnginePresent() && host.hostedEngine().active())       // Hosted Engine HA
                .hostedActive(host.hostedEnginePresent() ? host.hostedEngine().active() : null)
                .hostedScore(host.hostedEnginePresent() ? host.hostedEngine().scoreAsInteger() : 0)
                .ksm(host.ksmPresent() && host.ksm().enabled())         // 메모리 페이지 공유  비활성
                .pageSize(host.transparentHugePages().enabled())    // 자동으로 페이지를 크게 (확실하지 않음. 매우)
                .seLinux(host.seLinux().mode().value())     // selinux모드: disabled, enforcing, permissive
                // 클러스터 호환버전
                .hostHwVo(getHardWare(system, host))
                .hostSwVo(getSoftWare(system, host))
            .build();
    }


    // TODO: 고쳐야됨
    // 가상머신 목록
    @Override
    public List<VmVo> getVm(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Vm> vmList = system.vmsService().list().send().vms();

        log.info("Host 가상머신");
        return vmList.stream()
                .filter(vm -> (vm.hostPresent() && vm.host().id().equals(id)) || (vm.placementPolicy().hostsPresent() && vm.placementPolicy().hosts().get(0).id().equals(id)))
                .map(vm ->
                        VmVo.builder()
                            .hostName(system.hostsService().hostService(id).get().send().host().name())
                            .id(vm.id())
                            .name(vm.name())
                            .clusterName(system.clustersService().clusterService(vm.cluster().id()).get().send().cluster().name())
                            .status(vm.statusPresent() ? vm.status().value() : "")
                            .fqdn(vm.fqdn())
                            .upTime(commonService.getVmUptime(system, vm.id()))
                            .ipv4(commonService.getVmIp(system, vm.id(), "v4"))
                            .ipv6(commonService.getVmIp(system, vm.id(), "v6"))
                        .build()
                )
                .collect(Collectors.toList());
    }


    @Override
    public List<NicVo> getNic(String id) {
        SystemService system = admin.getConnection().systemService();
        List<HostNic> hostNicList = system.hostsService().hostService(id).nicsService().list().send().nics();
        DecimalFormat df = new DecimalFormat("###,###");

        log.info("Host 네트워크 인터페이스");
        return hostNicList.stream()
                .map(hostNic -> {
                    List<Statistic> statisticList = system.hostsService().hostService(id).nicsService().nicService(hostNic.id()).statisticsService().list().send().statistics();

                    return NicVo.builder()
                            .status(hostNic.status())
                            .name(hostNic.name())
                            .networkName(system.networksService().networkService(hostNic.network().id()).get().send().network().name())
                            .macAddress(hostNic.mac().address())
                            .ipv4(hostNic.ip().address())
                            .ipv6(hostNic.ipv6().addressPresent() ? hostNic.ipv6().address() : null)
                            .speed(hostNic.speed().divide(BigInteger.valueOf(1024 * 1024)))
                            .rxSpeed(commonService.getSpeed(statisticList, "data.current.rx.bps").divide(BigInteger.valueOf(1024 * 1024)))
                            .txSpeed(commonService.getSpeed(statisticList, "data.current.tx.bps").divide(BigInteger.valueOf(1024 * 1024)))
                            .rxTotalSpeed(commonService.getSpeed(statisticList, "data.total.rx"))
                            .txTotalSpeed(commonService.getSpeed(statisticList, "data.total.tx"))
                            .stop(commonService.getSpeed(statisticList, "errors.total.rx").divide(BigInteger.valueOf(1024 * 1024)))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HostDeviceVo> getHostDevice(String id) {
        SystemService system = admin.getConnection().systemService();
        List<HostDevice> hostDeviceList = system.hostsService().hostService(id).devicesService().list().send().devices();

        log.info("Host 호스트 장치");
        return hostDeviceList.stream()
                .map(
                    hostDevice ->
                        HostDeviceVo.builder()
                            .name(hostDevice.name())
                            .capability(hostDevice.capability())
                            .driver(hostDevice.driverPresent() ? hostDevice.driver() : null)
                            .vendorName(hostDevice.vendorPresent() ? hostDevice.vendor().name() + " (" +hostDevice.vendor().id() + ")" : "")
                            .productName(hostDevice.productPresent() ? hostDevice.product().name() + " (" + hostDevice.product().id() + ")" : "")
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService system = admin.getConnection().systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList = system.hostsService().hostService(id).permissionsService().list().send().permissions();

        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            if(permission.groupPresent() && !permission.userPresent()){
                Group group = system.groupsService().groupService(permission.group().id()).get().send().get();
                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                // 생성일의 경우 db에서 가져와야함

                Role role = system.rolesService().roleService(permission.role().id()).get().send().role();
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            if(permission.userPresent() && !permission.groupPresent()){
                User user = system.usersService().userService(permission.user().id()).get().send().user();
                pVo.setUser(user.name());
                pVo.setNameSpace(user.namespace());
                pVo.setProvider(user.domainPresent() ? user.domain().name() : null);

                Role role = system.rolesService().roleService(permission.role().id()).get().send().role();
                pVo.setRole(role.name());


                pVoList.add(pVo);
            }
        }
        log.info("Host 권한");
        return pVoList;
    }



    // 호스트 선호도 레이블 목록
    @Override
    public List<AffinityLabelVo> getAffinitylabels(String id) {
        SystemService system = admin.getConnection().systemService();
        List<AffinityLabel> affinityLabelList = system.hostsService().hostService(id).affinityLabelsService().list().follow("hosts,vms").send().label();

        log.info("Host 선호도 레이블");
        return affinityLabelList.stream()
                .map(al ->
                        AffinityLabelVo.builder()
                            .id(al.id())
                            .name(al.name())
                            .hosts(commonService.getHostLabelMember(system, al.id()))
                            .vms(commonService.getVmLabelMember(system, al.id()))
                        .build())
                .collect(Collectors.toList());
    }

    // 선호도 레이블 생성 창
    @Override
    public AffinityHostVm setAffinityDefaultInfo(String id, String type) {
        SystemService system = admin.getConnection().systemService();
        List<Host> hostList = system.hostsService().list().send().hosts();
        List<Vm> vmList = system.vmsService().list().send().vms();
        String clusterId = system.hostsService().hostService(id).get().send().host().cluster().id();

        log.info("Host 선호도 레이블 생성 창");
        return AffinityHostVm.builder()
                .clusterId(id)
                .hostList(commonService.setHostList(hostList, clusterId))
                .vmList(commonService.setVmList(vmList, clusterId))
                .build();
    }


    @Override
    public CommonVo<Boolean> addAffinitylabel(String id, AffinityLabelCreateVo alVo) {
        SystemService system = admin.getConnection().systemService();
        AffinityLabelsService alServices = system.affinityLabelsService();
        List<AffinityLabel> alList = system.affinityLabelsService().list().send().labels();

        // 중복이름
        boolean duplicateName = alList.stream().noneMatch(al -> al.name().equals(alVo.getName()));

        try {
            if(duplicateName) {
                AffinityLabelBuilder alBuilder = new AffinityLabelBuilder();
                alBuilder
                        .name(alVo.getName())
                        .hosts(
                            alVo.getHostList().stream()
                                .map(host -> new HostBuilder().id(host.getId()).build())
                                .collect(Collectors.toList())
                        )
                        .vms(
                            alVo.getVmList().stream()
                                .map(vm -> new VmBuilder().id(vm.getId()).build())
                                .collect(Collectors.toList())
                        )
                        .build();

                alServices.add().label(alBuilder).send().label();
                log.info("Host 선호도 레이블 생성");
                return CommonVo.successResponse();
            }else {
                log.error("실패: Host 선호도레이블 이름 중복");
                return CommonVo.failResponse("이름 중복");
            }
        } catch (Exception e) {
            log.error("실패: Host 선호도 레이블");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public AffinityLabelCreateVo getAffinityLabel(String alId) {
        SystemService system = admin.getConnection().systemService();
        AffinityLabel al = system.affinityLabelsService().labelService(alId).get().follow("hosts,vms").send().label();

        log.info("Host 선호도 레이블 편집창");
        return AffinityLabelCreateVo.builder()
                .id(alId)
                .name(al.name())
                .hostList(al.hostsPresent() ? commonService.getHostLabelMember(system, alId) : null )
                .vmList(al.vmsPresent() ? commonService.getVmLabelMember(system, alId) : null)
                .build();
    }

    @Override
    public CommonVo<Boolean> editAffinitylabel(String id, String alId, AffinityLabelCreateVo alVo) {

        return null;
    }

    @Override
    public CommonVo<Boolean> deleteAffinitylabel(String id, String alId) {

        return null;
    }

    @Override
    public List<EventVo> getEvent(String id) {
        SystemService system = admin.getConnection().systemService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");
        List<Event> eventList = system.eventsService().list().search("host.name=" + system.hostsService().hostService(id).get().send().host().name()).send().events();

        log.info("Host 이벤트");
        return eventList.stream()
                .map(
                    event ->
                        EventVo.builder()
                            .severity(event.severity().value())     // 상태[LogSeverity] : alert, error, normal, warning
                            .time(sdf.format(event.time()))
                            .message(event.description())
                            .relationId(event.correlationIdPresent() ? event.correlationId() : null)
                            .source(event.origin())
                        .build()
                )
                .collect(Collectors.toList());
    }




    //-------------------------------------------------------------------------------------------


    private HostHwVo getHardWare(SystemService system, Host host){
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
    private HostSwVo getSoftWare(SystemService system, Host host){
        return HostSwVo.builder()
                .osVersion(host.os().type() + " " + host.os().version().fullVersion())    // os 버전
//                .osInfo()       // os 정보
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





    // region: 안쓸거 같음




//    @Override
//    public boolean rebootHost(String hostId) {
//        log.debug("rebootHost ... hostId: {}", hostId);
//        SystemService system = admin.getConnection().systemService();
//        HostService hostService = system.hostsService().hostService(hostId);
//        /*
//        Host hostFound = hostService.get().send().host();
//        if (hostFound == null) {
//            log.error("rebootHost FAILED ...");
//            return false;
//        }
//        log.debug("hostFound: {}", hostFound);
//        log.debug("hostFound.status(): {}", hostFound.status().value());
//        */
//        try {
//            Host hostAfter = hostService.updateUsingSsh().async(true).host(
//                    new HostBuilder()
//                            .id(hostId)
//                            // TODO: .status 메소드로 상태변경방법 조사 필요
//                            .status(HostStatus.REBOOT)
//                            .build()
//            ).send().host();
//            log.info("hostAfter: {}", hostAfter.status().value());
//            log.info("hostAfter.status(): {}", hostAfter.status().value());
//            return true;
//        } catch (Exception e) {
//            log.error("error {}", e.getLocalizedMessage());
//            return false;
//        }
//    }





    // 시작
//    @Override
//    public void start(String id) {
//        SystemService system = admin.getConnection().systemService();
//        HostService hostService = system.hostsService().hostService(id);
//
//        try {
//            Host host = hostService.get().send().host();
//
//            log.info("start");
//        }catch (Exception e){
//            log.error("error: ", e);
//        }
//    }

    // endregion


}
