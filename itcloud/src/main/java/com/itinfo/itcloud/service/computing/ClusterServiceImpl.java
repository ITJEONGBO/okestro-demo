package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkUsageVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItClusterService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClusterServiceImpl implements ItClusterService {

    @Autowired private AdminConnectionService admin;

    // 클러스터 목록
    // 상태, 이름, 코멘트, 호환버전, 설명, 클러스터cpu유형, 호스트수, 가상머신수, (업그레이드 상태)
    @Override
    public List<ClusterVo> getList(){
        SystemService system = admin.getConnection().systemService();

        List<Cluster> clusterList = system.clustersService().list().send().clusters();
        List<Host> hostList = system.hostsService().list().send().hosts();
        List<Vm> vmList = system.vmsService().list().send().vms();

        return clusterList.stream()
                .map(cluster ->
                    ClusterVo.builder()
                        .id(cluster.id())
                        .name(cluster.name())
                        .comment(cluster.comment())
                        .version(cluster.version().major() + "." + cluster.version().minor())
                        .description(cluster.description())
                        .cpuType(cluster.cpuPresent() ? cluster.cpu().type() : null)
                        .hostCnt(getHostCnt(hostList, cluster.id(), ""))
//                        .hostUpCnt(getHostCnt(hostList, cluster.id(), "up"))
//                        .hostDownCnt(getHostCnt(hostList, cluster.id(), "down"))
                        .vmCnt(getVmCnt(vmList, cluster.id(), ""))
//                        .vmUpCnt(getVmCnt(vmList, cluster.id(), "up"))
//                        .vmDownCnt(getVmCnt(vmList, cluster.id(), "down"))
                    .build()
                )
                .collect(Collectors.toList());
    }

    // 클러스터 생성 위해 필요한 데이터센터 리스트
    // 모든 데이터센터 리스트를 보여준다
    @Override
    public List<DataCenterVo> setClusterDefaultInfo(){
        SystemService system = admin.getConnection().systemService();

        List<DataCenter> dataCenterList = system.dataCentersService().list().send().dataCenters();

        return dataCenterList.stream()
                .map(dataCenter ->
                    DataCenterVo.builder()
                        .id(dataCenter.id())
                        .name(dataCenter.name())
                        .storageType(dataCenter.local())
                        .networkList(
                            system.dataCentersService().dataCenterService(dataCenter.id()).networksService().list().send().networks().stream()
                                .map(network ->
                                    NetworkVo.builder()
                                        .id(network.id())
                                        .name(network.name())
                                    .build()
                                )
                                .collect(Collectors.toList())
                        )
                    .build()
                )
                .collect(Collectors.toList());
    }


    // 클러스터 생성
    // required: name , cpu.type, data_center   (Identify the datacenter with either id or name)
    @Override
    public CommonVo<Boolean> addCluster(ClusterCreateVo cVo) {
        SystemService system = admin.getConnection().systemService();

        ClustersService clustersService = system.clustersService();
        ExternalProvider openStackNetworkProvider = system.openstackNetworkProvidersService().list().send().providers().get(0);

        boolean duplicateName = clustersService.list().search("name=" + cVo.getName()).send().clusters().isEmpty();
        String[] ver = cVo.getVersion().split("\\.", 2);      // 버전값 분리

        try{
            if(duplicateName) {
                ClusterBuilder clusterBuilder = new ClusterBuilder();
                clusterBuilder
                        .dataCenter(new DataCenterBuilder().id(cVo.getDatacenterId()).build()) // 필수
                        .name(cVo.getName())    // 필수
                        .cpu(new CpuBuilder().architecture(cVo.getCpuArc()).type(cVo.getCpuType()).build())   // 필수
                        .description(cVo.getDescription())
                        .comment(cVo.getComment())
                        .managementNetwork(new NetworkBuilder().id(cVo.getNetworkId()).build())
                        .biosType(cVo.getBiosType())
                        .fipsMode(cVo.getFipsMode())
                        .version(new VersionBuilder().major(Integer.parseInt(ver[0])).minor(Integer.parseInt(ver[1])).build())
                        .switchType(cVo.getSwitchType())
                        .firewallType(cVo.getFirewallType())
                        .logMaxMemoryUsedThreshold(cVo.getLogMaxMemory())
                        .logMaxMemoryUsedThresholdType(cVo.getLogMaxType())
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

                log.info("성공: 클러스터 {} 생성", cVo.getName());
                return CommonVo.successResponse();
            }else {
                log.error("실패: 클러스터 생성 이름 중복 에러");
                return CommonVo.failResponse("클러스터 이름 중복 에러");
            }
        }catch (Exception e){
            log.error("실패: 클러스터 생성 실패", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 편집하려는 클러스터의 정보
    @Override
    public ClusterCreateVo setEditCluster(String id){
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

        log.info("클러스터 {} 편집 창", cluster.name());

        return ClusterCreateVo.builder()
                .id(id)
                .name(cluster.name())
                .description(cluster.description())
                .comment(cluster.comment())
                .datacenterId(dataCenter.id())
                .datacenterName(dataCenter.name())
                .cpuType(cluster.cpuPresent() ? cluster.cpu().type() : null)
                .cpuArc(cluster.cpuPresent() ? cluster.cpu().architecture() : null)
                .biosType(cluster.biosType())
                .fipsMode(cluster.fipsMode())
                .version(cluster.version().major() + "." + cluster.version().minor())
                .switchType(cluster.switchType())
                .firewallType(cluster.firewallType())
                .logMaxMemory(cluster.logMaxMemoryUsedThresholdAsInteger())
                .logMaxType(cluster.logMaxMemoryUsedThresholdType())
                .virtService(cluster.virtService())
//                .glusterService(cluster.glusterService())
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

        ClustersService clustersService = system.clustersService();
        ClusterService clusterService = system.clustersService().clusterService(id);
//        Network network = system.networksService().networkService(cVo.getNetworkId()).get().send().network();
        OpenStackNetworkProvider openStackNetworkProvider = system.openstackNetworkProvidersService().list().send().providers().get(0);

        boolean cName = clustersService.list().search("name="+cVo.getName()).send().clusters().isEmpty();
        String[] ver = cVo.getVersion().split("\\.");      // 버전값 분리

        try{
            if(cName) {
                ClusterBuilder clusterBuilder = new ClusterBuilder();
                clusterBuilder
                        .dataCenter(new DataCenterBuilder().id(cVo.getDatacenterId()).build()) // 필수
                        .name(cVo.getName())    // 필수
                        .cpu(new CpuBuilder().architecture(cVo.getCpuArc()).type(cVo.getCpuType()))   // 필수
                        .description(cVo.getDescription())
                        .comment(cVo.getComment())
                        .managementNetwork(new NetworkBuilder().id(cVo.getNetworkId()).build())
                        .biosType(cVo.getBiosType())
                        .fipsMode(cVo.getFipsMode())
                        .version(new VersionBuilder().major(Integer.parseInt(ver[0])).minor(Integer.parseInt(ver[1])).build())  // 호환 버전
//                    .switchType(cVo.getSwitchType())      // 선택불가
                        .firewallType(cVo.getFirewallType())
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

                log.info("성공: 클러스터 {} 편집", cVo.getName());
                return CommonVo.successResponse();
            }else {
                log.error("실패: 클러스터 {} 편집 이름 중복 에러", cVo.getName());
                return CommonVo.failResponse("클러스터 이름 중복 에러");
            }
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

            log.info("성공: 클러스터 삭제");
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("실패: 클러스터 삭제 ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }



    // 일반
    @Override
    public ClusterVo getInfo(String id){
        SystemService system = admin.getConnection().systemService();

        Cluster cluster = system.clustersService().clusterService(id).get().send().cluster();
        List<Vm> vmList = system.vmsService().list().send().vms();
        String dcName = system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name();

        log.info("클러스터 {} 출력", cluster.name());
        return ClusterVo.builder()
                    .id(id)
                    .name(cluster.name())
                    .description(cluster.description())
                    .datacenterName(cluster.dataCenterPresent() ? dcName : null)
                    .version(cluster.version().major() + "." + cluster.version().minor())
                    .gluster(cluster.glusterService())
                    .virt(cluster.virtService())
                    .cpuType(cluster.cpuPresent() ? cluster.cpu().type() : null)
                    .chipsetFirmwareType(cluster.biosTypePresent() ? TypeExtKt.findBios(cluster.biosType()) : "자동 감지")
                    .threadsAsCore(cluster.threadsAsCores())
                    .memoryOverCommit(cluster.memoryPolicy().overCommit().percentAsInteger())
                    .restoration(TypeExtKt.findMigrateErr(cluster.errorHandling().onError()))
                    .vmCnt(getVmCnt(vmList, id, ""))
                .build();
    }

    // 네트워크 목록
    @Override
    public List<NetworkVo> getNetwork(String id) {
        SystemService system = admin.getConnection().systemService();

        List<Network> networkList = system.clustersService().clusterService(id).networksService().list().send().networks();

        log.info("클러스터 {} 네트워크 목록", system.clustersService().clusterService(id).get().send().cluster().name());
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

    // 호스트 목록
    @Override
    public List<HostVo> getHost(String id) {
        SystemService system = admin.getConnection().systemService();

        List<Host> hostList = system.hostsService().list().send().hosts();
        List<Vm> vmList = system.vmsService().list().send().vms();

        log.info("클러스터 {} 호스트 목록", system.clustersService().clusterService(id).get().send().cluster().name());
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

        log.info("클러스터 {} 가상머신 목록", system.clustersService().clusterService(id).get().send().cluster().name());
        return vmList.stream()
                .filter(vm -> vm.cluster().id().equals(id))
                .map(vm ->
                    VmVo.builder()
                        .status(vm.status().value())
                        .id(vm.id())
                        .name(vm.name())
                        .upTime(getUptime(system, vm.id()))
                        .ipv4(getIp(system, vm.id(), "v4"))
                        .ipv6(getIp(system, vm.id(), "v6"))
                    .build()
                )
                .collect(Collectors.toList());
    }


    // 선호도 그룹 목록 - 클러스터 id를 받아와서 처리
    @Override
    public List<AffinityGroupVo> getAffinitygroup(String id){
        SystemService system = admin.getConnection().systemService();

        List<AffinityGroup> affinityGroupList = system.clustersService().clusterService(id).affinityGroupsService().list().send().groups();

        log.info("클러스터 {} 선호도그룹 목록", system.clustersService().clusterService(id).get().send().cluster().name());
        return affinityGroupList.stream()
                .map(ag ->
                    AffinityGroupVo.builder()
                        .id(ag.id())
                        .name(ag.name())
                        .description(ag.description())
                        .status(ag.broken())
                        .priority(ag.priority().intValue())  // 우선순위
                        .positive(ag.positivePresent() && ag.positive())
                        .vmEnabled(ag.vmsRule().enabled())
                        .vmPositive(ag.vmsRule().positive())
                        .vmEnforcing(ag.vmsRule().enforcing())
                        .hostEnabled(ag.hostsRule().enabled())
                        .hostPositive(ag.hostsRule().positive())
                        .hostEnforcing(ag.hostsRule().enforcing())
                        .hostMembers(ag.hostsPresent() ? getHostList(system, id, ag.id()) : null)
                        .vmMembers(ag.vmsPresent() ? getVmList(system, id, ag.id()) : null)
                        .hostLabels(ag.hostLabelsPresent() ? getLabelName(system, ag.hostLabels().get(0).id()) : null)
                        .vmLabels(ag.vmLabelsPresent() ? getLabelName(system, ag.vmLabels().get(0).id()) : null)
                    .build())
                .collect(Collectors.toList());
    }

    // 선호도 그룹 생성 위한 기본 설정
    @Override
    public ClusterAffGroupHostVm setAffinitygroupDefaultInfo(String clusterId) {
        SystemService system = admin.getConnection().systemService();
        List<AffinityLabel> affinityLabelList = system.affinityLabelsService().list().send().labels();

        return ClusterAffGroupHostVm.builder()
                .clusterId(clusterId)
                .hostList(
                    system.hostsService().list().send().hosts().stream()
                        .filter(host -> host.cluster().id().equals(clusterId))
                        .map(host ->
                                HostVo.builder()
                                    .id(host.id())
                                    .name(host.name())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .vmList(
                    system.vmsService().list().send().vms().stream()
                        .filter(vm -> vm.cluster().id().equals(clusterId))
                        .map(vm ->
                                VmVo.builder()
                                    .id(vm.id())
                                    .name(vm.name())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }

    // 선호도 그룹 추가 - 클러스터 아이디를 받아와서 처리
    @Override
    public CommonVo<Boolean> addAffinitygroup(String id, AffinityGroupCreateVo agVo) {
        SystemService system = admin.getConnection().systemService();

        AffinityGroupsService agServices = system.clustersService().clusterService(id).affinityGroupsService();
        List<AffinityGroup> agList = system.clustersService().clusterService(id).affinityGroupsService().list().send().groups();

        // 선호도 그룹 이름 중복검사
        boolean duplicateName = agList.stream().noneMatch(ag -> ag.name().equals(agVo.getName()));

        try {
            if(duplicateName) {
                AffinityGroup ag = new AffinityGroupBuilder()
                        .name(agVo.getName())
                        .description(agVo.getDescription())
                        .cluster(new ClusterBuilder().id(id).build())
                        .priority(agVo.getPriority())
                        .vmsRule(new AffinityRuleBuilder()
                                .enabled(agVo.isVmEnabled())    // 비활성화
                                .positive(agVo.isVmPositive())  // 양극 음극
                                .enforcing(agVo.isVmEnforcing()) // 강제 적용
                        )
                        .hostsRule(new AffinityRuleBuilder()
                                .enabled(agVo.isHostEnabled())
                                .positive(agVo.isHostPositive())
                                .enforcing(agVo.isHostEnforcing())
                        )
                        .hostLabels(agVo.getHostLabels().stream()
                                .map(al -> new AffinityLabelBuilder().id(al.getId()).build())
                                .collect(Collectors.toList())
                        )
                        .vmLabels(agVo.getVmLabels().stream()
                                .map(al -> new AffinityLabelBuilder().id(al.getId()).build())
                                .collect(Collectors.toList())
                        )
                        .hosts(agVo.getHostList().stream()
                                .map(host -> new HostBuilder().id(host.getId()).build())
                                .collect(Collectors.toList())
                        )
                        .vms(agVo.getVmList().stream()
                                .map(vm -> new VmBuilder().id(vm.getId()).build())
                                .collect(Collectors.toList())
                        )
                    .build();

                agServices.add().group(ag).send().group();

                log.info("성공: 클러스터 선호도그룹 {} 생성 ", agVo.getName());
                return CommonVo.successResponse();
            }else {
                log.error("실패: 클러스터 선호도그룹 이름 중복");
                return CommonVo.failResponse("error");
            }
        } catch (Exception e) {
            log.error("실패: 클러스터 선호도그룹 생성");
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public AffinityGroupCreateVo setEditAffinitygroup(String clusterId, String agId) {
        SystemService system = admin.getConnection().systemService();

        AffinityGroup affinityGroup = system.clustersService().clusterService(clusterId).affinityGroupsService().groupService(agId).get().follow("vmlabels,hostlabels,vms,hosts").send().group();

        return AffinityGroupCreateVo.builder()
                .clusterId(clusterId)
                .id(agId)
                .name(affinityGroup.name())
                .description(affinityGroup.description())
                .priority(affinityGroup.priority().intValue())
                .vmEnabled(affinityGroup.vmsRule().enabled())// 비활성화
                .vmPositive(affinityGroup.vmsRule().positive()) // 양극 음극
                .vmEnforcing(affinityGroup.vmsRule().enforcing()) // 강제 적용
                .hostEnabled(affinityGroup.hostsRule().enabled())
                .hostPositive(affinityGroup.hostsRule().positive())
                .hostEnforcing(affinityGroup.hostsRule().enforcing())
                .hostLabels(
                        affinityGroup.hostLabels().stream()
                            .map(affinityLabel ->
                                    AffinityLabelVo.builder()
                                        .id(affinityLabel.id())
                                        .name(affinityLabel.name())
                                    .build()
                            )
                            .collect(Collectors.toList())
                )
                .vmLabels(
                        affinityGroup.hostLabels().stream()
                            .map(affinityLabel ->
                                    AffinityLabelVo.builder()
                                        .id(affinityLabel.id())
                                        .name(affinityLabel.name())
                                    .build()
                            )
                            .collect(Collectors.toList())
                )
                .hostList(
                        affinityGroup.hosts().stream()
                            .map(host ->
                                    HostVo.builder()
                                        .id(host.id())
                                        .name(host.name())
                                    .build()
                            )
                            .collect(Collectors.toList())
                )
                .vmList(
                        affinityGroup.vms().stream()
                            .map(vm ->
                                    VmVo.builder()
                                        .id(vm.id())
                                        .name(vm.name())
                                    .build()
                            )
                            .collect(Collectors.toList())
                )
                .build();
    }

    // 선호도 그룹 편집
    @Override
    public CommonVo<Boolean> editAffinitygroup(String id, String agId, AffinityGroupCreateVo agVo) {
        SystemService system = admin.getConnection().systemService();

        AffinityGroupService agService = system.clustersService().clusterService(id).affinityGroupsService().groupService(agId);
        List<AffinityGroup> agList = system.clustersService().clusterService(id).affinityGroupsService().list().send().groups();

//        boolean duplicateName = agList.stream().noneMatch(ag -> ag.name().equals(agVo.getName()));

        try {
            AffinityGroupBuilder agBuilder = new AffinityGroupBuilder();
            agBuilder
                    .id(agId)
                    .name(agVo.getName())
                    .description(agVo.getDescription())
                    .cluster(new ClusterBuilder().id(id).build())
                    .priority(agVo.getPriority())
                    .vmsRule(
                        new AffinityRuleBuilder()
                            .enabled(agVo.isVmEnabled())    // 비활성화
                            .positive(agVo.isVmPositive())  // 양극 음극
                            .enforcing(agVo.isVmEnforcing()) // 강제 적용
                    )
                    .hostsRule(
                        new AffinityRuleBuilder()
                            .enabled(agVo.isHostEnabled())
                            .positive(agVo.isHostPositive())
                            .enforcing(agVo.isHostEnforcing())
                    )
                    .hostLabels(agVo.getHostLabels().stream()
                            .map(al -> new AffinityLabelBuilder().id(al.getId()).build())
                            .collect(Collectors.toList())
                    )
                    .vmLabels(agVo.getVmLabels().stream()
                            .map(al -> new AffinityLabelBuilder().id(al.getId()).build())
                            .collect(Collectors.toList())
                    )
                    .hosts(agVo.getHostList().stream()
                            .map(host -> new HostBuilder().id(host.getId()).build())
                            .collect(Collectors.toList())
                    )
                    .vms(agVo.getVmList().stream()
                            .map(vm -> new VmBuilder().id(vm.getId()).build())
                            .collect(Collectors.toList())
                    )
                .build();

            agService.update().send().group();

            log.info("성공: 클러스터 선호도그룹 {} 편집", agVo.getName());
            return CommonVo.successResponse();
        } catch (Exception e) {
            log.error("실패: 클러스터 선호도그룹 {} 편집", agVo.getName());
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 선호도 그룹 삭제 - clusterId와 agid를 가져와서 삭제
    @Override
    public CommonVo<Boolean> deleteAffinitygroup(String id, String agId) {
        SystemService system = admin.getConnection().systemService();

        AffinityGroupService agService = system.clustersService().clusterService(id).affinityGroupsService().groupService(agId);

        try {
            agService.remove().send();

            log.info("성공: 클러스터 선호도그룹 삭제");
            return CommonVo.successResponse();
        } catch (Exception e) {
            log.error("실패: 클러스터 선호도그룹 삭제");
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 선호도 레이블 목록 출력
    @Override
    public List<AffinityLabelVo> getAffinitylabelList(String id) {
        SystemService system = admin.getConnection().systemService();

        List<AffinityLabel> affinityLabelList = system.affinityLabelsService().list().send().labels();

        log.info("클러스터 {} 선호도 레이블", system.clustersService().clusterService(id).get().send().cluster().name());
        return affinityLabelList.stream()
                .map(al ->
                    AffinityLabelVo.builder()
                        .id(al.id())
                        .name(al.name())
                        .hosts(getHostLabelMember(system, al.id()))
                        .vms(getVmLabelMember(system, al.id()))
                    .build()
                )
                .collect(Collectors.toList());
    }

    // 레이블 생성시 필요 - 해당 cluster 가지고 있는 host
    @Override
    public List<HostVo> getHostMember(String clusterId){
        SystemService system = admin.getConnection().systemService();

        List<Host> hostList = system.hostsService().list().send().hosts();

        log.info("클러스터 선호도 레이블 생성시 필요한 호스트 리스트");
        return hostList.stream()
                .filter(host -> host.clusterPresent() && host.cluster().id().equals(clusterId))
                .map(host ->
                    HostVo.builder()
                        .id(host.id())
                        .name(host.name())
                    .build())
                .collect(Collectors.toList());
    }

    // 레이블 생성시 필요 - 해당 cluster 가지고 있는 vm
    @Override
    public List<VmVo> getVmMember(String clusterId){
        SystemService system = admin.getConnection().systemService();

        List<Vm> vmList = system.vmsService().list().send().vms();

        log.info("클러스터 선호도레이블 생성시 필요한 가상머신 리스트");
        return vmList.stream()
                .filter(vm -> vm.clusterPresent() && vm.cluster().id().equals(clusterId))
                .map(vm ->
                    VmVo.builder()
                        .id(vm.id())
                        .name(vm.name())
                    .build())
                .collect(Collectors.toList());
    }

    // 선호도 레이블 생성 시 출력창
    @Override
    public AffinityLabelCreateVo getAffinityLabel(String id){   // id는 alid
        SystemService system = admin.getConnection().systemService();

        AffinityLabel al = system.affinityLabelsService().labelService(id).get().follow("hosts,vms").send().label();

        log.info("성공: 클러스터 {} 선호도 레이블", system.clustersService().clusterService(id).get().send().cluster().name());
        return AffinityLabelCreateVo.builder()
                .id(id)
                .name(al.name())
                .hostList(al.hostsPresent() ? getHostLabelMember(system, id) : null )
                .vmList(al.vmsPresent() ? getVmLabelMember(system, id) : null)
                .build();
    }

    // 선호도 레이블 생성
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
                        .hosts(alVo.getHostList().stream()
                                .map(host -> new HostBuilder().id(host.getId()).build())
                                .collect(Collectors.toList())
                        )
                        .vms(alVo.getVmList().stream()
                                .map(vm -> new VmBuilder().id(vm.getId()).build())
                                .collect(Collectors.toList())
                        )
                        .build();

                alServices.add().label(alBuilder).send().label();
                log.info("성공: 클러스터 {} 선호도레이블", alVo.getName());
                return CommonVo.successResponse();
            }else {
                log.error("실패: 클러스터 선호도레이블 이름 중복");
                return CommonVo.failResponse("이름 중복");
            }

        } catch (Exception e) {
            log.error("실패: 클러스터 선호도 레이블");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 이게 레이블에서 항목삭제 할때 필요할 것 같음
    public void deleteHostMember(String id){

    }


    // 선호도 레이블 - 편집
    // 이름만 바뀌는거 같음, 호스트하고 vm은 걍 삭제하는 방식으로
    @Override
    public CommonVo<Boolean> editAffinitylabel(AffinityLabelCreateVo alVo) {
        SystemService system = admin.getConnection().systemService();

        AffinityLabelService alService = system.affinityLabelsService().labelService(alVo.getId());
        List<AffinityLabel> alList = system.affinityLabelsService().list().send().labels();
//        AffinityLabelHostsService alHostsService = system.affinityLabelsService().labelService(alVo.getId()).hostsService();
//        AffinityLabelVmsService alVmsService = system.affinityLabelsService().labelService(alVo.getId()).vmsService();

        // 중복이름
        boolean duplicateName = alList.stream().noneMatch(al -> al.name().equals(alVo.getName()));

        try {
            AffinityLabelBuilder alBuilder = new AffinityLabelBuilder();
            alBuilder
                    .id(alVo.getId())
                    .name(alVo.getName())
                    .hosts(alVo.getHostList().stream()
                            .map(host -> new HostBuilder().id(host.getId()).build())
                            .collect(Collectors.toList())
                    )
                    .vms(alVo.getVmList().stream()
                            .map(vm -> new VmBuilder().id(vm.getId()).build())
                            .collect(Collectors.toList())
                    )
                    .build();

//            alVo.getVmList().stream().distinct().forEach(System.out::println);

            alService.update().label(alBuilder).send().label();
            log.info("성공: 클러스터 {} 선호도레이블 편집", alVo.getName());
            return CommonVo.successResponse();
        } catch (Exception e) {
            log.error("실패: 클러스터 {} 선호도레이블 편집", alVo.getName());
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 선호도 레이블 - 삭제하려면 해당 레이블에 있는 가상머신&호스트 멤버 전부 내리고 해야함
    @Override
    public CommonVo<Boolean> deleteAffinitylabel(String id) {
        SystemService system = admin.getConnection().systemService();

        AffinityLabelService alService = system.affinityLabelsService().labelService(id);
        AffinityLabel affinityLabel = system.affinityLabelsService().labelService(id).get().follow("hosts,vms").send().label();

        try {
            if(!affinityLabel.hostsPresent() && !affinityLabel.vmsPresent()) {
                alService.remove().send();

                log.info("성공: 클러스터 선호도레이블 삭제");
                return CommonVo.successResponse();
            } else {
                log.error("가상머신 혹은 호스트를 삭제하세요");
                return CommonVo.failResponse("error");
            }
        } catch (Exception e) {
            log.error("실패: 클러스터 선호도레이블 삭제");
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 클러스터 권한 출력
    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService system = admin.getConnection().systemService();

        List<Permission> permissionList = system.clustersService().clusterService(id).permissionsService().list().send().permissions();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            // 그룹이 있고, 유저가 없을때
            if(permission.groupPresent() && !permission.userPresent()){
                Group group = system.groupsService().groupService(permission.group().id()).get().send().get();
                Role role = system.rolesService().roleService(permission.role().id()).get().send().role();

                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            // 그룹이 없고, 유저가 있을때
            if(!permission.groupPresent() && permission.userPresent()){
                User user = system.usersService().userService(permission.user().id()).get().send().user();
                Role role = system.rolesService().roleService(permission.role().id()).get().send().role();

                pVo.setUser(user.name());
                pVo.setProvider(user.domainPresent() ? user.domain().name() : null);
                pVo.setNameSpace(user.namespace());
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    // 클러스터 이벤트 출력
    @Override
    public List<EventVo> getEvent(String id) {
        SystemService system = admin.getConnection().systemService();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");
        List<Event> eventList = system.eventsService().list().send().events();

        log.info("클러스터 이벤트 출력");
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




    // vm 가동시간
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
        } else if(hour == 0){
            upTime = null;
        }else {
            upTime = (hour / 60) + "분";
        }

        return upTime;
    }

    // vm ip 주소
    private String getIp(SystemService system, String id, String version){
        Vm vm = system.vmsService().vmService(id).get().send().vm();

        return system.vmsService().vmService(id).nicsService().list().send().nics().stream()
                .flatMap(nic -> {
                    List<ReportedDevice> reportedDeviceList = system.vmsService().vmService(id).nicsService().nicService(nic.id()).reportedDevicesService().list().send().reportedDevice().stream()
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
//        List<Nic> nicList = system.vmsService().vmService(id).nicsService().list().send().nics();
//        Vm vm = system.vmsService().vmService(id).get().send().vm();
//
//        String ip = null;
//
//        for (Nic nic : nicList){
//            List<ReportedDevice> reportedDeviceList = system.vmsService().vmService(id).nicsService().nicService(nic.id()).reportedDevicesService().list().send().reportedDevice();
//
//            if("v4".equals(version)) {
//                ip = reportedDeviceList.stream()
//                        .filter(r -> !vm.status().value().equals("down"))
//                        .map(r -> r.ips().get(0).address())
//                        .findFirst()
//                        .orElse(null);
//            }else {
//                ip = reportedDeviceList.stream()
//                        .filter(r -> !vm.status().value().equals("down"))
//                        .map(r -> r.ips().get(1).address())
//                        .findFirst()
//                        .orElse(null);
//            }
//        }
//        return ip;
    }



    // 선호도  - 레이블 아이디와 이름 얻기
    private List<AffinityLabelVo> getLabelName(SystemService system,String alId){
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

    // 선호도  - 호스트 리스트 출력
    private List<HostVo> getHostList(SystemService system, String clusterId, String agId){
        List<Host> hostList = system.clustersService().clusterService(clusterId).affinityGroupsService().groupService(agId).hostsService().list().send().hosts();

        return hostList.stream()
                .map(host ->
                    HostVo.builder()
                        .id(host.id())
                        .name(host.name())
                    .build())
                .collect(Collectors.toList());
    }

    // 선호도 그룹 목록 - 가상머신 리스트 출력
    private List<VmVo> getVmList(SystemService system, String clusterId, String agId){
        List<Vm> vmList = system.clustersService().clusterService(clusterId).affinityGroupsService().groupService(agId).vmsService().list().send().vms();

        return vmList.stream()
                .map(vm ->
                    VmVo.builder()
                        .id(vm.id())
                        .name(vm.name())
                    .build())
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

        idList.stream().forEach(System.out::println);

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

    // 클러스터 목록
    // 호스트 개수 파악
    private int getHostCnt(List<Host> hostList, String id, String ele){
        if("up".equals(ele)){
            return (int) hostList.stream()
                    .filter(host -> host.cluster().id().equals(id) && host.status().value().equals("up"))
                    .count();
        }else if("down".equals(ele)){
            return (int) hostList.stream()
                    .filter(host -> host.cluster().id().equals(id) && !host.status().value().equals("up"))
                    .count();
        }else {
            return (int) hostList.stream()
                    .filter(host -> host.cluster().id().equals(id))
                    .count();
        }
    }

    // 클러스터 목록
    // 가상머신 개수 파악
    private int getVmCnt(List<Vm> vmList, String id, String ele){
        if("up".equals(ele)) {
            return (int) vmList.stream()
                    .filter(vm -> vm.cluster().id().equals(id) && vm.status().value().equals("up"))
                    .count();
        }else if("down".equals(ele)) {
            return (int) vmList.stream()
                    .filter(vm -> vm.cluster().id().equals(id) && !vm.status().value().equals("up"))
                    .count();
        }else{
            return (int) vmList.stream()
                    .filter(vm -> vm.cluster().id().equals(id))
                    .count();
        }
    }


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
