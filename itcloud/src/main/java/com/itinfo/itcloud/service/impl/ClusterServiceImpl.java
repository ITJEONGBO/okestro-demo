package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkClusterVo;
import com.itinfo.itcloud.model.network.NetworkUsageVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItAffinityService;
import com.itinfo.itcloud.service.ItClusterService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClusterServiceImpl implements ItClusterService {
    @Autowired private AdminConnectionService admin;
    @Autowired private CommonService commonService;
    @Autowired private ItAffinityService affinityService;

    // 클러스터 목록
    // 상태, 이름, 코멘트, 호환버전, 설명, 클러스터cpu유형, 호스트수, 가상머신수, (업그레이드 상태)
    @Override
    public List<ClusterVo> getList(){
        SystemService system = admin.getConnection().systemService();
        List<Cluster> clusterList = system.clustersService().list().send().clusters();
        List<Host> hostList = system.hostsService().list().send().hosts();
        List<Vm> vmList = system.vmsService().list().send().vms();

        log.info("Cluster 목록");
        return clusterList.stream()
                .map(cluster ->
                    ClusterVo.builder()
                        .id(cluster.id())
                        .name(cluster.name())
                        .comment(cluster.comment())
                        .version(cluster.version().major() + "." + cluster.version().minor())
                        .description(cluster.description())
                        .cpuType(cluster.cpuPresent() ? cluster.cpu().type() : null)
                        .hostCnt(commonService.getClusterHostCnt(hostList, cluster.id(), ""))
                        .vmCnt(commonService.getClusterVmCnt(vmList, cluster.id(), ""))
                    .build()
                )
                .collect(Collectors.toList());
    }

    // 클러스터 생성 위해 필요한 데이터센터 리스트
    @Override
    public List<DataCenterVo> setDatacenterList(){
        SystemService system = admin.getConnection().systemService();
        List<DataCenter> dataCenterList = system.dataCentersService().list().send().dataCenters();

        log.info("Cluster 생성창");
        return dataCenterList.stream()
                .map(dataCenter ->
                        DataCenterVo.builder()
                            .id(dataCenter.id())
                            .name(dataCenter.name())
                            .storageType(dataCenter.local())
                            .build()
                )
                .collect(Collectors.toList());
    }

    // 클러스터 생성 위해 필요한 네트워크 리스트 (데이터센터 아이디에 의존)
    @Override
    public List<NetworkVo> setNetworkList(String dcId) {
        SystemService system = admin.getConnection().systemService();
        List<Network> networkList = system.dataCentersService().dataCenterService(dcId).networksService().list().send().networks();

        return networkList.stream()
                .filter(network -> !network.externalProviderPresent())
                .map(network ->
                        NetworkVo.builder()
                            .id(network.id())
                            .name(network.name())
                            .build()
                )
                .collect(Collectors.toList());
    }

    // 클러스터 생성
    // required: name, cpu.type, data_center (Identify the datacenter with either id or name)
    @Override
    public CommonVo<Boolean> addCluster(ClusterCreateVo cVo) {
        SystemService system = admin.getConnection().systemService();
        ClustersService clustersService = system.clustersService();
        ExternalProvider openStackNetworkProvider = system.openstackNetworkProvidersService().list().send().providers().get(0);
        String[] ver = cVo.getVersion().split("\\.", 2);      // 버전값 분리

        try{
            if (commonService.isNameDuplicate(system, "cluster", cVo.getName(), null)) {
                log.error("클러스터 이름 중복");
                return CommonVo.failResponse("클러스터 이름 중복");
            }

            ClusterBuilder clusterBuilder = new ClusterBuilder();
            clusterBuilder
                    .dataCenter(new DataCenterBuilder().id(cVo.getDatacenterId()).build()) // 필수
                    .name(cVo.getName())    // 필수
                    .cpu(new CpuBuilder().architecture(Architecture.valueOf(cVo.getCpuArc())).type(cVo.getCpuType()).build())   // 필수
                    .description(cVo.getDescription())
                    .comment(cVo.getComment())
                    .managementNetwork(new NetworkBuilder().id(cVo.getNetworkId()).build())
                    .biosType(BiosType.valueOf(cVo.getBiosType()))
                    .fipsMode(FipsMode.valueOf(cVo.getFipsMode()))
                    .version(new VersionBuilder().major(Integer.parseInt(ver[0])).minor(Integer.parseInt(ver[1])).build())
                    .switchType(SwitchType.valueOf(cVo.getSwitchType()))
                    .firewallType(FirewallType.valueOf(cVo.getFirewallType()))
                    .logMaxMemoryUsedThreshold(cVo.getLogMaxMemory())
                    .logMaxMemoryUsedThresholdType(LogMaxMemoryUsedThresholdType.valueOf(cVo.getLogMaxType()))
                    .virtService(cVo.isVirtService())
                    .glusterService(cVo.isGlusterService())
                    .errorHandling(new ErrorHandlingBuilder().onError(cVo.getRecoveryPolicy()))
                    // TODO: 마이그레이션 정책 관련 설정 값 조회 기능 존재여부 확인필요
                    .migration(
                        new MigrationOptionsBuilder()
                            .bandwidth(new MigrationBandwidthBuilder().assignmentMethod(cVo.getBandwidth()))
                            .encrypted(cVo.getEncrypted())
                    )
                    .fencingPolicy(
                        new FencingPolicyBuilder()
                            .skipIfConnectivityBroken(new SkipIfConnectivityBrokenBuilder().enabled(true))
                            .skipIfSdActive(new SkipIfSdActiveBuilder().enabled(true))
                    );

            if (cVo.isNetworkProvider()) {
                clusterBuilder.externalNetworkProviders(openStackNetworkProvider);
            }

            clustersService.add().cluster(clusterBuilder.build()).send();

            log.info("성공: 클러스터 생성");
            return CommonVo.createResponse();
        }catch (Exception e){
            log.error("실패: 클러스터 생성 실패", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 클러스터 편집 창
    @Override
    public ClusterCreateVo setCluster(String id){
        SystemService system = admin.getConnection().systemService();
        Cluster cluster = system.clustersService().clusterService(id).get().send().cluster();
        DataCenter dataCenter = system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter();
        List<Network> networkList = system.clustersService().clusterService(id).networksService().list().send().networks();

        String networkId = networkList.stream()
                .filter(Network::display)
                .map(Identified::id)
                .findFirst()
                .orElse("");

        String networkName = networkList.stream()
                .filter(Network::display)
                .map(Identified::name)
                .findFirst()
                .orElse("");

        log.info("Cluster 편집 창");
        return ClusterCreateVo.builder()
                .id(id)
                .name(cluster.name())
                .description(cluster.description())
                .comment(cluster.comment())
                .datacenterId(dataCenter.id())
                .datacenterName(dataCenter.name())
                .cpuType(cluster.cpuPresent() ? cluster.cpu().type() : null)
                .cpuArc(cluster.cpuPresent() ? String.valueOf(cluster.cpu().architecture()) : null)
                .biosType(String.valueOf(cluster.biosType()))
                .fipsMode(String.valueOf(cluster.fipsMode()))
                .version(cluster.version().major() + "." + cluster.version().minor())
                .switchType(String.valueOf(cluster.switchType()))
                .firewallType(String.valueOf(cluster.firewallType()))
                .logMaxMemory(cluster.logMaxMemoryUsedThresholdAsInteger())
                .logMaxType(String.valueOf(cluster.logMaxMemoryUsedThresholdType()))
                .virtService(cluster.virtService())
                .glusterService(cluster.glusterService())
                .networkId(networkId)
                .networkName(networkName)
                // migration
                .bandwidth(cluster.migration().bandwidth().assignmentMethod())
                .recoveryPolicy(cluster.errorHandling().onError())
                .build();
    }

    
    // 클러스터 편집
    @Override
    public CommonVo<Boolean> editCluster(String id, ClusterCreateVo cVo) {
        SystemService system = admin.getConnection().systemService();
        ClusterService clusterService = system.clustersService().clusterService(id);
        OpenStackNetworkProvider openStackNetworkProvider = system.openstackNetworkProvidersService().list().send().providers().get(0);

        boolean nameDuplicate = system.clustersService().list().send().clusters().stream()
                .filter(cluster -> !cluster.id().equals(id))
                .anyMatch(cluster -> cluster.name().equals(cVo.getName()));

        String[] ver = cVo.getVersion().split("\\.");      // 버전값 분리

        try{
            if (nameDuplicate) {
                log.error("클러스터 이름 중복");
                return CommonVo.failResponse("클러스터 이름 중복");
            }

            ClusterBuilder clusterBuilder = new ClusterBuilder();
            clusterBuilder
                    .id(id)
                    .dataCenter(new DataCenterBuilder().id(cVo.getDatacenterId()).build()) // 필수
                    .name(cVo.getName())    // 필수
                    .cpu(new CpuBuilder().architecture(Architecture.valueOf(cVo.getCpuArc())).type(cVo.getCpuType()))   // 필수
                    .description(cVo.getDescription())
                    .comment(cVo.getComment())
                    .managementNetwork(new NetworkBuilder().id(cVo.getNetworkId()).build())
                    .biosType(BiosType.valueOf(cVo.getBiosType()))
                    .fipsMode(FipsMode.valueOf(cVo.getFipsMode()))
                    .version(new VersionBuilder().major(Integer.parseInt(ver[0])).minor(Integer.parseInt(ver[1])).build())  // 호환 버전
//                    .switchType(cVo.getSwitchType())      // 선택불가
                    .firewallType(FirewallType.valueOf(cVo.getFirewallType()))
                    .logMaxMemoryUsedThreshold(cVo.getLogMaxMemory())
                    .logMaxMemoryUsedThresholdType(LogMaxMemoryUsedThresholdType.PERCENTAGE)
                    .virtService(cVo.isVirtService())
                    .glusterService(cVo.isGlusterService())
                    .errorHandling(new ErrorHandlingBuilder().onError(cVo.getRecoveryPolicy()))   // 복구정책
                    // 마이그레이션 정책
                    .migration(new MigrationOptionsBuilder()
                            .bandwidth(new MigrationBandwidthBuilder().assignmentMethod(cVo.getBandwidth()))    // 대역폭
                            .encrypted(cVo.getEncrypted())      // 암호화
                    );

            if (cVo.isNetworkProvider()) {
                clusterBuilder.externalNetworkProviders(openStackNetworkProvider);
            }

            clusterService.update().cluster(clusterBuilder.build()).send();

            log.info("성공: 클러스터 편집");
            return CommonVo.createResponse();
        }catch (Exception e){
            log.error("실패: 클러스터 편집", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 클러스터 삭제 (클러스터 아이디 입력시)
    @Override
    public CommonVo<Boolean> deleteCluster(String id) {
        SystemService system = admin.getConnection().systemService();
        ClusterService clusterService = system.clustersService().clusterService(id);

        try {
            clusterService.remove().send();

            log.info("Cluster 삭제");
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("실패: Cluster 삭제");
            return CommonVo.failResponse(e.getMessage());
        }
    }



    // 일반
    @Override
    public ClusterVo getInfo(String id){
        SystemService system = admin.getConnection().systemService();
        Cluster cluster = system.clustersService().clusterService(id).get().send().cluster();
        List<Vm> vmList = system.vmsService().list().send().vms();

        log.info("Cluster 일반");
        return ClusterVo.builder()
                    .id(id)
                    .name(cluster.name())
                    .description(cluster.description())
                    .datacenterName(cluster.dataCenterPresent() ? system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name() : null)
                    .version(cluster.version().major() + "." + cluster.version().minor())
                    .gluster(cluster.glusterService())
                    .virt(cluster.virtService())
                    .cpuType(cluster.cpuPresent() ? cluster.cpu().type() : null)
                    .chipsetFirmwareType(cluster.biosTypePresent() ? TypeExtKt.findBios(cluster.biosType()) : "자동 감지")
                    .threadsAsCore(cluster.threadsAsCores())
                    .memoryOverCommit(cluster.memoryPolicy().overCommit().percentAsInteger())
                    .restoration(TypeExtKt.findMigrateErr(cluster.errorHandling().onError()))
                    .vmCnt(commonService.getClusterVmCnt(vmList, id, ""))
                .build();
    }

    // 네트워크 목록
    @Override
    public List<NetworkVo> getNetwork(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Network> networkList = system.clustersService().clusterService(id).networksService().list().send().networks();

        log.info("Cluster 네트워크");
        return networkList.stream()
                .filter(network -> !networkList.isEmpty())
                .map(network ->
                    NetworkVo.builder()
                        .id(network.id())
                        .name(network.name())
                        .status(TypeExtKt.findNetworkStatus(network.status()))
                        .description(network.description())
                        .networkUsageVo(
                            NetworkUsageVo.builder()
                                .vm(network.usages().contains(NetworkUsage.VM))
                                .display(network.usages().contains(NetworkUsage.DISPLAY))
                                .migration(network.usages().contains(NetworkUsage.MIGRATION))
                                .management(network.usages().contains(NetworkUsage.MANAGEMENT))
                                .defaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE))
                                .gluster(network.usages().contains(NetworkUsage.GLUSTER))
                            .build()
                        )
                    .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public CommonVo<Boolean> addNetwork(String id, NetworkCreateVo ncVo) {
        SystemService system = admin.getConnection().systemService();
        NetworksService networksService = system.networksService();
        OpenStackNetworkProvider openStackNetworkProvider = system.openstackNetworkProvidersService().list().send().providers().get(0);
        String dcId = system.clustersService().clusterService(id).get().send().cluster().dataCenter().id();

        // TODO
        //  외부 공급자 설정할 때 물리적 네트워크에 연결하는 거 구현해야함, & 외부 공급자 설정 시 클러스터에서 모두필요 항목은 사라져야됨 (프론트)
        try {
            Network network =
                    networksService.add()
                            .network(
                                    new NetworkBuilder()
                                            .dataCenter(new DataCenterBuilder().id(dcId).build())
                                            .name(ncVo.getName())
                                            .description(ncVo.getDescription())
                                            .comment(ncVo.getComment())
                                            .vlan(ncVo.getVlan() != null ? new VlanBuilder().id(ncVo.getVlan()) : null)
                                            .usages(ncVo.getUsageVm() ? NetworkUsage.VM : NetworkUsage.DEFAULT_ROUTE)
                                            .portIsolation(ncVo.getPortIsolation())
                                            .mtu(ncVo.getMtu())
                                            .stp(ncVo.getStp()) // ?
                                            .externalProvider(ncVo.getExternalProvider() ?  openStackNetworkProvider : null)
                            )
                            .send().network();

            // TODO: vnic 기본생성 가능. 기본생성명 수정시 기본생성과 수정명 2개가 생김
//            ncVo.getVnics().forEach(vnicProfileVo -> {
//                AssignedVnicProfilesService aVnicsService = system.networksService().networkService(network.id()).vnicProfilesService();
//                aVnicsService.add().profile(new VnicProfileBuilder().name(vnicProfileVo.getName()).build()).send().profile();
//            });

            // 클러스터 모두연결이 선택되어야지만 모두 필요가 선택됨
            ncVo.getClusterVoList().stream()
                    .filter(NetworkClusterVo::isConnected) // 연결된 경우만 필터링
                    .forEach(networkClusterVo -> {
                        ClusterNetworksService clusterNetworksService = system.clustersService().clusterService(networkClusterVo.getId()).networksService();
                        clusterNetworksService.add().network(
                                new NetworkBuilder()
                                        .id(network.id())
                                        .required(networkClusterVo.isRequired())
                        ).send().network();
                    });

            // 외부 공급자 처리시 레이블 생성 안됨
            if (ncVo.getLabel() != null && !ncVo.getLabel().isEmpty()) {
                NetworkLabelsService nlsService = system.networksService().networkService(network.id()).networkLabelsService();
                nlsService.add().label(new NetworkLabelBuilder().id(ncVo.getLabel())).send();
            }

            log.info("network {} 추가 성공", network.name());
            return CommonVo.createResponse();
        }catch (Exception e){
            e.printStackTrace();
            log.error("error, ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 네트워크 관리 창
    @Override
    public List<NetworkClusterVo> setManageNetwork(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Network> networkList = system.clustersService().clusterService(id).networksService().list().send().networks();

        return networkList.stream()
                .map(network ->
                    NetworkClusterVo.builder()
                            .id(network.id())
                            .name(network.name())
                            .connected(network.clusterPresent()) // 할당: 클러스터에 떠있어야만 이게 보임, 할당을 해제하면 cluster에서 삭제
                            .required(network.required())
                            .networkUsageVo(
                                    NetworkUsageVo.builder()
                                            .vm(network.usages().stream().anyMatch(networkUsage -> networkUsage.value().equals("vm")))
                                            .display(network.usages().stream().anyMatch(networkUsage -> networkUsage.value().equals("display")))
                                            .migration(network.usages().stream().anyMatch(networkUsage -> networkUsage.value().equals("migration")))
                                            .management(network.usages().stream().anyMatch(networkUsage -> networkUsage.value().equals("management")))
                                            .defaultRoute(network.usages().stream().anyMatch(networkUsage -> networkUsage.value().equals("default_route")))
                                            .gluster(network.usages().stream().anyMatch(networkUsage -> networkUsage.value().equals("gluster")))
                                            .build()
                            )
                            .build()
                )
                .collect(Collectors.toList());
    }

    // TODO:HELP  관리기능 애매
    @Override
    public CommonVo<Boolean> manageNetwork(String id, List<NetworkClusterVo> ncVoList) {
        SystemService system = admin.getConnection().systemService();
        List<Network> networkList = system.clustersService().clusterService(id).networksService().list().send().networks();


        return null;
    }

    // 호스트 목록
    @Override
    public List<HostVo> getHost(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Host> hostList = system.hostsService().list().send().hosts();
        List<Vm> vmList = system.vmsService().list().send().vms();

        log.info("Cluster 호스트");
        return hostList.stream()
                .filter(host -> host.cluster().id().equals(id))
                .map(host ->
                    HostVo.builder()
                        .id(host.id())
                        .name(host.name())
                        .status(host.status().value())
                        .address(host.address())
                        .vmUpCnt(
                            (int) vmList.stream()
                                    .filter(vm -> vm.hostPresent() && vm.host().id().equals(host.id()) && vm.status().value().equals("up"))
                                    .count()
                        )
                    .build()
                )
                .collect(Collectors.toList());
    }

    // 가상머신 목록
    @Override
    public List<VmVo> getVm(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Vm> vmList = system.vmsService().list().send().vms();

        log.info("Cluster 가상머신");
        return vmList.stream()
                .filter(vm -> vm.cluster().id().equals(id))
                .map(vm ->
                    VmVo.builder()
                            .status(vm.status().value())
                            .id(vm.id())
                            .name(vm.name())
                            .upTime(commonService.getVmUptime(system, vm.id()))
                            // 왕관여부
                            .ipv4(commonService.getVmIp(system, vm.id(), "v4"))
                            .ipv6(commonService.getVmIp(system, vm.id(), "v6"))
                            .build()
                )
                .collect(Collectors.toList());
    }



    // 선호도 그룹 목록
    // 선호도 그룹 생성창
    // 선호도 그룹 생성
    // 선호도 그룹 편집 창
    // 선호도 그룹 편집
    // 선호도 그룹 삭제



    // 선호도 레이블 목록 출력
    @Override
    public List<AffinityLabelVo> getAffinitylabelList(String id) {  // 호스트 id
        SystemService system = admin.getConnection().systemService();
        List<AffinityLabel> affinityLabelList = system.affinityLabelsService().list().send().labels();

        log.info("Cluster 선호도 레이블");
        return affinityLabelList.stream()
                .map(al -> AffinityLabelVo.builder()
                            .id(al.id())
                            .name(al.name())
//                            .hosts(affinityService.getHostLabelMember(system, al.id()))
//                            .vms(affinityService.getVmLabelMember(system, al.id()))
                            .build()
                )
                .collect(Collectors.toList());
    }


    // 선호도 레이블 생성
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

                log.info("Cluster 선호도레이블 생성");
                return CommonVo.createResponse();
            }else {
                log.error("실패: Cluster 선호도레이블 이름 중복");
                return CommonVo.failResponse("이름 중복");
            }
        } catch (Exception e) {
            log.error("실패: Cluster 선호도 레이블");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 선호도 레이블 편집 시 출력창
    @Override
    public AffinityLabelCreateVo getAffinityLabel(String id, String alId){   // id는 alid
        SystemService system = admin.getConnection().systemService();
        AffinityLabel al = system.affinityLabelsService().labelService(alId).get().follow("hosts,vms").send().label();

        log.info("Cluster 선호도 레이블 편집창");
        return AffinityLabelCreateVo.builder()
                .id(id)
                .name(al.name())
//                .hostList(al.hostsPresent() ? affinityService.getHostLabelMember(system, alId) : null )
//                .vmList(al.vmsPresent() ? affinityService.getVmLabelMember(system, alId) : null)
                .build();
    }

    // 선호도 레이블 - 편집
    // 이름만 바뀌는거 같음, 호스트하고 vm은 걍 삭제하는 방식으로
    @Override
    public CommonVo<Boolean> editAffinitylabel(String id, String alId, AffinityLabelCreateVo alVo) {
        SystemService system = admin.getConnection().systemService();
        AffinityLabelService alService = system.affinityLabelsService().labelService(alVo.getId());
        List<AffinityLabel> alList = system.affinityLabelsService().list().send().labels();

        // 중복이름
        boolean duplicateName = alList.stream().noneMatch(al -> al.name().equals(alVo.getName()));

        try {
            AffinityLabelBuilder alBuilder = new AffinityLabelBuilder();
            alBuilder
                    .id(alVo.getId())
                    .name(alVo.getName())
                    .hosts(
                        alVo.getHostList().stream()
                            .map(host ->
                                    new HostBuilder()
                                            .id(host.getId())
                                            .build()
                            )
                            .collect(Collectors.toList())
                    )
                    .vms(
                        alVo.getVmList().stream()
                            .map(vm ->
                                    new VmBuilder()
                                            .id(vm.getId())
                                            .build()
                            )
                            .collect(Collectors.toList())
                    )
                    .build();

//            alVo.getVmList().stream().distinct().forEach(System.out::println);

            alService.update().label(alBuilder).send().label();
            log.info("Cluster 선호도레이블 편집");
            return CommonVo.createResponse();
        } catch (Exception e) {
            log.error("실패: Cluster 선호도레이블 편집");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 선호도 레이블 - 삭제하려면 해당 레이블에 있는 가상머신&호스트 멤버 전부 내리고 해야함
    @Override
    public CommonVo<Boolean> deleteAffinitylabel(String id, String alId) {
        SystemService system = admin.getConnection().systemService();
        AffinityLabelService alService = system.affinityLabelsService().labelService(alId);
        AffinityLabel affinityLabel = system.affinityLabelsService().labelService(alId).get().follow("hosts,vms").send().label();

        try {
            if(!affinityLabel.hostsPresent() && !affinityLabel.vmsPresent()) {
                alService.remove().send();

                log.info("Cluster 선호도레이블 삭제");
                return CommonVo.successResponse();
            } else {
                log.error("가상머신 혹은 호스트를 삭제하세요");
                return CommonVo.failResponse("error");
            }
        } catch (Exception e) {
            log.error("실패: Cluster 선호도레이블 삭제");
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 클러스터 권한 출력
    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Permission> permissionList = system.clustersService().clusterService(id).permissionsService().list().send().permissions();

        return commonService.getPermission(system, permissionList);
    }


    // 클러스터 이벤트 출력
    @Override
    public List<EventVo> getEvent(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Event> eventList = system.eventsService().list().send().events();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        log.info("Cluster 이벤트");
        return eventList.stream()
                .filter(event -> event.clusterPresent() && event.cluster().idPresent() && event.cluster().id().equals(id))
                .map(event ->
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




    // ----------------------------------------------------------------------------------------



    // region :필요없을 거 같음

//    public List<CpuProfileVo> getCpuProfile(String id){
//        Connection connection = adminConnectionService.getConnection();
//        SystemService systemService = connection.systemService();
//
//        List<CpuProfileVo> cpVoList = new ArrayList<>();
//        CpuProfileVo cpVo = null;
//
//        List<CpuProfile> cpuProfileList =
//                ((AssignedCpuProfilesService.ListResponse)systemService.clustersService().clusterService(id).cpuProfilesService().list().send()).profiles();
//
//        for(CpuProfile cpuProfile : cpuProfileList){
//            cpVo = new CpuProfileVo();
//            cpVo.setName(cpuProfile.name());
//            cpVo.setDescription(cpuProfile.description());
//            cpVoList.add(cpVo);
//        }
//        return cpVoList;
//    }

    // endregion

}
