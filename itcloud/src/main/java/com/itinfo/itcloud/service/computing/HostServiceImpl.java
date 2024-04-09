package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.HostCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItHostService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class HostServiceImpl implements ItHostService {

    @Autowired private AdminConnectionService admin;

    // 호스트 목록
    @Override
    public List<HostVo> getList() {
        SystemService system = admin.getConnection().systemService();

        // allContent를 포함해야 hosted Engine의 정보가 나온다
        List<Host> hostList = system.hostsService().list().allContent(true).send().hosts();

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
        List<Host> hostList = system.hostsService().list().send().hosts();
        Cluster cluster = system.clustersService().clusterService(hostCreateVo.getClusterId()).get().send().cluster();

        try {
            HostBuilder hostBuilder = new HostBuilder();
            // 고려해야하는 것, ssh port번호, 전원관리 활성 여부(펜스 에이전트가 추가되는지가 달림)
            // sshport가 22면 .ssh() 설정하지 않아도 알아서 지정됨, sshport 변경을 ovirt에서 해보신적은 없어서 우선 보류

            hostBuilder
                    .name(hostCreateVo.getName())
                    .comment(hostCreateVo.getComment())
                    .address(hostCreateVo.getHostIp())          // 호스트이름/IP
                    .rootPassword(hostCreateVo.getSshPw())   // 암호
                    .spm(new SpmBuilder().priority(hostCreateVo.getSpm()))
                    .hostedEngine(new HostedEngineBuilder().active(hostCreateVo.isHostEngine()))
                    .cluster(cluster);

            if(hostCreateVo.getSshPort() != 22){
                hostBuilder.ssh(new SshBuilder().port(hostCreateVo.getSshPort()));  // 새로 지정할 포트번호
            }
            log.info("---- " + hostCreateVo.toString());

            // 호스트 엔진 배치작업 선택 (없음/배포)  -> 호스트 생성
            if(hostCreateVo.isHostEngine()){
                hostsService.add().deployHostedEngine(true).host(hostBuilder).send().host();
                log.info("hostEngine 추가");
            }else {
                hostsService.add().deployHostedEngine(false).host(hostBuilder).send().host();  // false 생략가능
                log.info("! hostEngine 추가");
            }

            Host host = hostBuilder.build();

            while (host.status().value().equals(HostStatus.UP)) {
                log.info("호스트 추가 완료(" + hostCreateVo.getName() + ")");
            }

//            return hostsService.list().send().hosts().size() == ( hostList.size()+1 );
            return CommonVo.successResponse();
        } catch (Exception e) {
            log.error("error: ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 호스트 편집창
    @Override
    public HostCreateVo getHostCreate(String id) {
        SystemService system = admin.getConnection().systemService();
        Host host = system.hostsService().hostService(id).get().send().host();
        Cluster cluster = system.clustersService().clusterService(host.cluster().id()).get().send().cluster();

        log.info("getHostsCreate");

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
        Cluster cluster = system.clustersService().clusterService(hostCreateVo.getClusterId()).get().send().cluster();

        try {
            Host host = new HostBuilder()
                    .id(id)
                    .name(hostCreateVo.getName())
                    .comment(hostCreateVo.getComment())
                    .address(hostCreateVo.getHostIp())
                    .rootPassword(hostCreateVo.getSshPw())
                    .spm(new SpmBuilder().priority(hostCreateVo.getSpm()))
                    .hostedEngine(new HostedEngineBuilder().active(hostCreateVo.isHostEngine()))
                    .cluster(cluster)
                    .build();

            hostService.update().host(host).send().host();

            log.info("host 수정" + host.toString());
            return CommonVo.successResponse();
        } catch (Exception e) {
            log.error("error ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 호스트 삭제
    @Override
    public CommonVo<Boolean> deleteHost(String id) {
        SystemService system = admin.getConnection().systemService();

        List<Host> hList = system.hostsService().list().send().hosts();
        HostsService hostsService = system.hostsService();
        HostService hostService = system.hostsService().hostService(id);

        Host host = hostService.get().send().host();
        String name = hostService.get().send().host().name();

        try {
            if(host.status().equals(HostStatus.MAINTENANCE)) {
                hostService.remove().send();
                log.info("delete host: {}", name);
//                return hostsService.list().send().hosts().size() == (hList.size() - 1);
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

    
    // 유지보수
    @Override
    public void deActive(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);

        try {
            Host host = hostService.get().send().host();
            System.out.println(host.id() + ", " + host.name() + ", "+  host.status().value());

            if(host.status() != HostStatus.MAINTENANCE){
                hostService.deactivate().send();
                log.info(host.status().value());
            }

            log.info("유지보수 정지");
        }catch (Exception e){
            log.error("error ", e);
        }
    }

    // 활성
    @Override
    public void active(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);

        try {
            Host host = hostService.get().send().host();

            if(host.status() != HostStatus.UP){
                hostService.activate().send();
                log.info(host.status().value());
            }

            log.info("활성");
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
            Host host = hostService.get().send().host();
            hostService.refresh().send();
            log.info("refresh " + host.status().value());
        }catch (Exception e){
            log.error("error ", e);
        }
    }


    // ssh 관리
    // 재시작
    @Override
    public void reStart(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);

        // restart 하기 전, maintenance 인지 확인
        /*
        ssh 관리
            oVirt의 엔진 SDK를 통해 SSH를 통해 호스트를 재부팅하는 기능은 기본적으로 제공되지 않습니다.
            oVirt 엔진 SDK는 호스트를 관리하기 위한 API를 제공하지만, SSH를 통한 호스트 재부팅과 같은 기능은 포함되어 있지 않습니다.
        */

        try {
            hostService.fence().fenceType(FenceType.RESTART.value()).send().powerManagement();

            log.info("restart");
        }catch (Exception e){
            log.error("error: ", e);
        }
    }

    // 시작
    @Override
    public void start(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);

        try {
            Host host = hostService.get().send().host();

            log.info("start");
        }catch (Exception e){
            log.error("error: ", e);
        }
    }

    // 중지
    @Override
    public void stop(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);

        try {
            Host host = hostService.get().send().host();

            log.info("stop");
        }catch (Exception e){
            log.error("error: ", e);
        }
    }



    @Override
    public HostVo getInfo(String id) {
        SystemService system = admin.getConnection().systemService();

        Host host = system.hostsService().hostService(id).get().allContent(true).send().host();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        // 온라인 논리 CPU 코어수 - HostCpuUnit 이 없음 인식안됨
        // https://192.168.0.70/ovirt-engine/api/hosts/3bbd27b9-13d8-4fff-ad29-c0350994ca88/cpuunits,numanodes
        List<Statistic> statisticList = system.hostsService().hostService(id).statisticsService().list().send().statistics();

        long bootTime = statisticList.stream()
                                .filter(statistic -> statistic.name().equals("boot.time"))
                                .map(statistic -> statistic.values().get(0).datum().longValue() * 1000)
                                .findAny()
                                .orElse(0L);

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
                .memory(getStatistic(statisticList, "memory.total"))
                .memoryFree(getStatistic(statisticList, "memory.free"))
                .memoryUsed(getStatistic(statisticList, "memory.used"))
                .memoryShared(getStatistic(statisticList, "memory.shared")) // 문제잇음
                .swapTotal(getStatistic(statisticList, "swap.total"))
                .swapFree(getStatistic(statisticList, "swap.free"))
                .swapUsed(getStatistic(statisticList, "swap.used"))
                .hugePage2048Total(getPage(statisticList, "hugepages.2048.total"))
                .hugePage2048Free(getPage(statisticList, "hugepages.2048.free"))
                .hugePage1048576Total(getPage(statisticList, "hugepages.1048576.total"))
                .hugePage1048576Free(getPage(statisticList, "hugepages.1048576.free"))
                .bootingTime(sdf.format(new Date(bootTime)))
                .hostedEngine(host.hostedEnginePresent() && host.hostedEngine().active())       // Hosted Engine HA
                .hostedActive(host.hostedEnginePresent() ? host.hostedEngine().active() : null)
                .hostedScore(host.hostedEnginePresent() ? host.hostedEngine().scoreAsInteger() : 0)
                .ksm(host.ksmPresent() && host.ksm().enabled())         // 메모리 페이지 공유  비활성
                .pageSize(host.transparentHugePages().enabled())    // 자동으로 페이지를 크게 (확실하지 않음. 매우)
                .seLinux(host.seLinux().mode().value())     // selinux모드: disabled, enforcing, permissive
                // 클러스터 호환버전
                .hostHwVo(getHardWare(system, id))
                .hostSwVo(getSoftWare(system, id))
            .build();
    }


    // 호스트 가상머신 목록
    // TODO: 고쳐야됨
    @Override
    public List<VmVo> getVm(String id) {
        SystemService system = admin.getConnection().systemService();

        List<Vm> vmList = system.vmsService().list().send().vms();

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
                                .upTime(getUptime(system, vm.id()))
                                .ipv4(getIp(system, vm.id(), "v4"))
                                .ipv6(getIp(system, vm.id(), "v6"))
                                .build()
                )
                .collect(Collectors.toList());
    }


    @Override
    public List<NicVo> getNic(String id) {
        SystemService system = admin.getConnection().systemService();

        List<HostNic> hostNicList = system.hostsService().hostService(id).nicsService().list().send().nics();
        return hostNicList.stream()
                .map(hostNic -> {
                    NicVo nVo = NicVo.builder()
                            .status(hostNic.status())
                            .name(hostNic.name())
                            .networkName(system.networksService().networkService(hostNic.network().id()).get().send().network().name())
                            .macAddress(hostNic.mac().address())
                            .ipv4(hostNic.ip().address())
                            .ipv6(hostNic.ipv6().addressPresent() ? hostNic.ipv6().address() : null)
                            .speed(String.valueOf(hostNic.speed().divide(BigInteger.valueOf(1024 * 1024))))
                            .build();

                    DecimalFormat df = new DecimalFormat("###,###");
                    List<Statistic> statisticList =
                            system.hostsService().hostService(id).nicsService().nicService(hostNic.id()).statisticsService().list().send().statistics();

                    for (Statistic statistic : statisticList) {
                        String st = "";

                        if (statistic.name().equals("data.current.rx.bps")) {
                            st = df.format((statistic.values().get(0).datum()).divide(BigDecimal.valueOf(1024 * 1024)));
                            nVo.setRxSpeed(st);
                        }
                        if (statistic.name().equals("data.current.tx.bps")) {
                            st = df.format((statistic.values().get(0).datum()).divide(BigDecimal.valueOf(1024 * 1024)));
                            nVo.setTxSpeed(st);
                        }
                        if (statistic.name().equals("data.total.rx")) {
                            st = df.format(statistic.values().get(0).datum());
                            nVo.setRxTotalSpeed(st);
                        }
                        if (statistic.name().equals("data.total.tx")) {
                            st = df.format(statistic.values().get(0).datum());
                            nVo.setTxTotalSpeed(st);
                        }
                        if (statistic.name().equals("errors.total.rx")) {
                            st = df.format(statistic.values().get(0).datum());
                            nVo.setStop(st);
                        }
                    }

                    return nVo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HostDeviceVo> getHostDevice(String id) {
        SystemService system = admin.getConnection().systemService();
        List<HostDevice> hostDeviceList = system.hostsService().hostService(id).devicesService().list().send().devices();

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
        return pVoList;
    }



    // 호스트 선호도 레이블 목록
    @Override
    public List<AffinityLabelVo> getAffinitylabels(String id) {
        SystemService system = admin.getConnection().systemService();

        List<AffinityLabel> affinityLabelList = system.hostsService().hostService(id).affinityLabelsService().list().follow("hosts,vms").send().label();

        log.info("호스트 {} 선호도 레이블", system.hostsService().hostService(id).get().send().host().name());
        return affinityLabelList.stream()
                .map(al ->
                        AffinityLabelVo.builder()
                            .id(al.id())
                            .name(al.name())
                            .hosts(getHostLabelMember(system, al.id()))
                            .vms(getVmLabelMember(system, al.id()))
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public AffinityHostVm setAffinityDefaultInfo(String id, String type) {
        SystemService system = admin.getConnection().systemService();

        List<AffinityLabel> affinityLabelList = system.affinityLabelsService().list().send().labels();
        List<AffinityGroup> affinityGroupList = system.clustersService().clusterService(id).affinityGroupsService().list().send().groups();

        // host vm lavel 메소드 분리?
        if(type.equals("label")){
            return AffinityHostVm.builder()
                    .clusterId(id)
                    .hostList(getHostVoList(system, id))
                    .vmList(getVmVoList(system, id))
                    .build();
        }else{ //group
            // TODO 레이블 추가해야함
            return AffinityHostVm.builder()
                    .clusterId(id)
                    .hostList(getHostVoList(system, id))
                    .vmList(getVmVoList(system, id))
                    .build();
        }
    }


    @Override
    public CommonVo<Boolean> addAffinitylabel(AffinityLabelCreateVo alVo) {
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
                log.info("성공: 호스트 {} 선호도레이블", alVo.getName());
                return CommonVo.successResponse();
            }else {
                log.error("실패: 호스트 선호도레이블 이름 중복");
                return CommonVo.failResponse("이름 중복");
            }
        } catch (Exception e) {
            log.error("실패: 호스트 선호도 레이블");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public CommonVo<Boolean> editAffinitylabel(AffinityLabelCreateVo alVo) {
        return null;
    }

    @Override
    public CommonVo<Boolean> deleteAffinitylabel(String id) {
        return null;
    }

    @Override
    public List<EventVo> getEvent(String id) {
        SystemService system = admin.getConnection().systemService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");
        List<Event> eventList = system.eventsService().list().search("host.name=" + system.hostsService().hostService(id).get().send().host().name()).send().events();

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




    private HostHwVo getHardWare(SystemService system, String id){
        Host host = system.hostsService().hostService(id).get().send().host();

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
    private HostSwVo getSoftWare(SystemService system, String id){
        Host host = system.hostsService().hostService(id).get().send().host();

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


    // static의 메모리와 swap을 알아낸다
    private BigInteger getStatistic(List<Statistic> statisticList, String q){
        return statisticList.stream()
                .filter(statistic -> statistic.name().equals(q))
                .map(statistic -> statistic.values().get(0).datum().toBigInteger())
                .findAny()
                .orElse(BigInteger.ZERO);
    }

    // hugepage
    private int getPage(List<Statistic> statisticList, String q) {
        return statisticList.stream()
                .filter(statistic -> statistic.name().equals(q))
                .map(statistic -> statistic.values().get(0).datum().intValue())
                .findAny()
                .orElse(0);
    }



    // vm uptime에서 사용
    private String getUptime(SystemService system, String id){
        List<Statistic> statisticList = system.vmsService().vmService(id).statisticsService().list().send().statistics();

        long hour = statisticList.stream()
                .filter(statistic -> statistic.name().equals("elapsed.time"))
                .mapToLong(statistic -> statistic.values().get(0).datum().longValue() / (60 * 60))
                .findFirst()
                .orElse(0);

        String upTime;
        if (hour > 24) {
            upTime = hour / 24 + "일";
        } else if (hour > 1 && hour < 24) {
            upTime = hour + "시간";
        } else if (hour == 0) {
            upTime = null;
        } else {
            upTime = (hour / 60) + "분";
        }

        return upTime;
    }
    // vm ip 주소
    private String getIp(SystemService system, String id, String version){
        List<Nic> nicList = system.vmsService().vmService(id).nicsService().list().send().nics();
        Vm vm = system.vmsService().vmService(id).get().send().vm();

        String ip = null;

        for (Nic nic : nicList){
            List<ReportedDevice> reportedDeviceList = system.vmsService().vmService(id).nicsService().nicService(nic.id()).reportedDevicesService().list().send().reportedDevice();

            if("v4".equals(version)) {
                ip = reportedDeviceList.stream()
                        .filter(r -> !vm.status().value().equals("down"))
                        .map(r -> r.ips().get(0).address())
                        .findFirst()
                        .orElse(null);
            }else {
                ip = reportedDeviceList.stream()
                        .filter(r -> !vm.status().value().equals("down"))
                        .map(r -> r.ips().get(1).address())
                        .findFirst()
                        .orElse(null);
            }
        }
        return ip;
    }


    private List<HostVo> getHostVoList(SystemService system, String id){
        List<Host> hostList = system.hostsService().list().send().hosts();

        return hostList.stream()
                .filter(host -> host.cluster().id().equals(id))
                .map(host ->
                        HostVo.builder()
                                .id(host.id())
                                .name(host.name())
                                .build()
                )
                .collect(Collectors.toList());
    }

    private List<VmVo> getVmVoList(SystemService system, String id){
        List<Vm> vmList = system.vmsService().list().send().vms();

        return vmList.stream()
                .filter(vm -> vm.cluster().id().equals(id))
                .map(vm ->
                        VmVo.builder()
                                .id(vm.id())
                                .name(vm.name())
                                .build()
                )
                .collect(Collectors.toList());
    }
    // 선호도 레이블에 있는 호스트 출력
    private List<HostVo> getHostLabelMember(SystemService system, String alid){
        List<Host> hostList = system.affinityLabelsService().labelService(alid).hostsService().list().send().hosts();

        List<String> idList = hostList.stream()
                .map(Host::id)
                .collect(Collectors.toList());

        return idList.stream()
                .map(hostId -> {
                    Host host = system.hostsService().hostService(hostId).get().send().host();
                    return HostVo.builder()
                            .id(host.id())
                            .name(host.name())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 선호도 레이블 - vm
    private List<VmVo> getVmLabelMember(SystemService system, String alid){
        List<Vm> vmList = system.affinityLabelsService().labelService(alid).vmsService().list().send().vms();

        // id만 출력
        List<String> idList = vmList.stream()
                .map(Vm::id)
                .collect(Collectors.toList());

        return idList.stream()
                .map(vmId -> {
                    Vm vm = system.vmsService().vmService(vmId).get().send().vm();
                    return VmVo.builder()
                            .id(vm.id())
                            .name(vm.name())
                            .build();
                })
                .collect(Collectors.toList());
    }





}
