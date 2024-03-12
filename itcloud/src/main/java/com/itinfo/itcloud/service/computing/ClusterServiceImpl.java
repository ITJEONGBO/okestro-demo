package com.itinfo.itcloud.service.computing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.network.NetworkUsageVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.ovirt.OvirtService;
import com.itinfo.itcloud.service.ItClusterService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.Package;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClusterServiceImpl implements ItClusterService {

    @Autowired private AdminConnectionService admin;
    @Autowired private OvirtService ovirt;


    @Override
    public String getName(String id){
        return admin.getConnection().systemService().clustersService().clusterService(id).get().send().cluster().name();
    }

    // 컴퓨팅-클러스터 목록
    // 상태, 이름, 코멘트, 호환버전, 설명, 클러스터cpu유형, 호스트수, 가상머신수, (업그레이드 상태)
    @Override
    public List<ClusterVo> getList(){
        SystemService systemService = admin.getConnection().systemService();

        List<Cluster> clusterList = systemService.clustersService().list().send().clusters();
        List<Host> hostList = systemService.hostsService().list().send().hosts();
        List<Vm> vmList = systemService.vmsService().list().send().vms();

        return clusterList.stream()
                .map(cluster ->
                    ClusterVo.builder()
                        .id(cluster.id())
                        .name(cluster.name())
                        .comment(cluster.comment())
                        .version(cluster.version().major()+"."+cluster.version().minor())
                        .description(cluster.description())
                        .cpuType(cluster.cpuPresent() ? cluster.cpu().type() : null)
                        .hostCnt(getHostCnt(hostList, cluster.id()))
                        .hostUpCnt(getHostUpCnt(hostList, cluster.id()))
                        .hostDownCnt(getHostDownCnt(hostList, cluster.id()))
                        .vmCnt(getVmCnt(vmList, cluster.id()))
                        .vmUpCnt(getVmUpCnt(vmList, cluster.id()))
                        .vmDownCnt(getVmDownCnt(vmList, cluster.id()))
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public ClusterVo getInfo(String id){
        SystemService systemService = admin.getConnection().systemService();

        Cluster cluster = systemService.clustersService().clusterService(id).get().send().cluster();
        List<Vm> vmList = systemService.vmsService().list().send().vms();
        String dcName = systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name();

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
                .vmCnt(getVmCnt(vmList, id))
                .build();
    }

    @Override
    public List<NetworkVo> getNetwork(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<Network> networkList = systemService.clustersService().clusterService(id).networksService().list().send().networks();

        return networkList.stream()
                .filter(network -> !networkList.isEmpty())
                .map(network -> NetworkVo.builder()
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
                                        .build())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<HostVo> getHost(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<Host> hostList = systemService.hostsService().list().send().hosts();
        List<Vm> vmList = systemService.vmsService().list().send().vms();

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


    @Override
    public List<VmVo> getVm(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<Vm> vmList = systemService.vmsService().list().send().vms();

        return vmList.stream()
                .filter(vm -> vm.cluster().id().equals(id))
                .map(vm ->
                    VmVo.builder()
                        .status(vm.status().value())
                        .id(vm.id())
                        .name(vm.name())
                        .upTime(getStatistic(systemService, vm.id()))
                        .ipv4(getIp(systemService, vm.id(), "v4"))
                        .ipv6(getIp(systemService, vm.id(), "v6"))
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<AffinityGroupVo> getAffinitygroup(String id){
        SystemService systemService = admin.getConnection().systemService();

        List<AffinityGroup> affinityGroupList = systemService.clustersService().clusterService(id).affinityGroupsService().list().send().groups();

        return affinityGroupList.stream()
                .map(ag ->
                    AffinityGroupVo.builder()
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
                        .hostList(getMember(systemService, id, ag.id(), "host"))
                        .vmList(getMember(systemService, id, ag.id(), "vm"))
                        .hostLabels(getLabel(systemService, id, ag.id(), "host"))
                        .vmLabels(getLabel(systemService, id, ag.id(), "vm"))
                        .build())
                .collect(Collectors.toList());
    }

    //수정
    @Override
    //TODO: 해야됨
    public List<AffinityLabelVo> getAffinitylabel(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<AffinityLabel> affinityLabelList = systemService.affinityLabelsService().list().send().labels();

        return affinityLabelList.stream()
                .map(al -> AffinityLabelVo.builder()
                        .id(al.id())
                        .name(al.name())
                        .hosts(getLabel(systemService, id, al.id(), "host"))
                        .vms(getLabel(systemService, id, al.id(), "vm"))
                        .build()
                )
                .collect(Collectors.toList());

    }

//    필요없을 거 같음
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

    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<Permission> permissionList = systemService.clustersService().clusterService(id).permissionsService().list().send().permissions();

//        return permissionList.stream()
//                .map(p -> PermissionVo.builder()
//                        .permissionId(p.id())
//                        .user()
//                        .build()
//                )
//                .collect(Collectors.toList());
//

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            // 그룹이 있고, 유저가 없을때
            if(permission.groupPresent() && !permission.userPresent()){
                Group group = systemService.groupsService().groupService(permission.group().id()).get().send().get();
                Role role = systemService.rolesService().roleService(permission.role().id()).get().send().role();

                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            // 그룹이 없고, 유저가 있을때
            if(!permission.groupPresent() && permission.userPresent()){
                User user = systemService.usersService().userService(permission.user().id()).get().send().user();
                Role role = systemService.rolesService().roleService(permission.role().id()).get().send().role();

                pVo.setUser(user.name());
                pVo.setProvider(user.domainPresent() ? user.domain().name() : null);
                pVo.setNameSpace(user.namespace());
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    @Override
    // TODO 이벤트 검색 자체 오류
    public List<EventVo> getEvent(String id) {
        SystemService systemService = admin.getConnection().systemService();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");
        List<Event> eventList = systemService.eventsService().list().max(10).send().events();

        return eventList.stream()
                .filter(Event::clusterPresent /*&& event.cluster().name().equals(getName(id))*/)
                .map(event -> EventVo.builder()
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

    // cluster-add.jsp에서 datacenter 선택하기 위해 사용되는 dc list
    @Override
    public List<DataCenterVo> getDcList(){
        SystemService systemService = admin.getConnection().systemService();

        List<DataCenter> dataCenterList = systemService.dataCentersService().list().send().dataCenters();

        return dataCenterList.stream()
                .map(dataCenter -> DataCenterVo.builder()
                        .id(dataCenter.id())
                        .name(dataCenter.name())
                        .storageType(dataCenter.local())
                        .networkList( getNetworkList(systemService, dataCenter.id()))
                        .build()
                )
                .collect(Collectors.toList());
    }


    public List<NetworkVo> getNetworkList(SystemService sys, String dcId){
        List<Network> networkList = sys.dataCentersService().dataCenterService(dcId).networksService().list().send().networks();

        return networkList.stream()
                .map(network -> NetworkVo.builder()
                        .id(network.id())
                        .name(network.name())
                        .datacenterId(dcId)
                        .build())
                .collect(Collectors.toList());
    }


    // edit 창
    @Override
    public ClusterCreateVo getClusterCreate(String id){
        SystemService systemService = admin.getConnection().systemService();

        Cluster cluster = systemService.clustersService().clusterService(id).get().send().cluster();
        DataCenter dataCenter = systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter();
        List<Network> networkList = systemService.clustersService().clusterService(id).networksService().list().send().networks();

        String networkId = "";
        String networkName = "";

        for(Network network : networkList){
            if(network.display()){
                networkId = network.id();
                networkName = network.name();
            }
        }

        log.info("getClusterCreate");

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


    // 클러스터 생성
    @Override
    public CommonVo<Boolean> addCluster(ClusterCreateVo cVo) {
        // required: name , cpu.type, data_center   (Identify the datacenter with either id or name)
        SystemService systemService = admin.getConnection().systemService();

        ClustersService clustersService = systemService.clustersService();
        DataCenter dataCenter = systemService.dataCentersService().dataCenterService(cVo.getDatacenterId()).get().send().dataCenter();
        Network network = systemService.networksService().networkService(cVo.getNetworkId()).get().send().network();
        ExternalProvider openStackNetworkProvider = systemService.openstackNetworkProvidersService().list().send().providers().get(0);

        boolean cName = clustersService.list().search("name="+cVo.getName()).send().clusters().isEmpty();
        String[] ver = cVo.getVersion().split("\\.");      // 버전값 분리

        try{
            if(cName) {
                ClusterBuilder clusterBuilder = new ClusterBuilder();
                clusterBuilder
                        .dataCenter(dataCenter) // 필수
                        .name(cVo.getName())    // 필수
                        .cpu(new CpuBuilder().architecture(cVo.getCpuArc()).type(cVo.getCpuType()))   // 필수
                        .description(cVo.getDescription())
                        .comment(cVo.getComment())
                        .managementNetwork(network)
                        .biosType(cVo.getBiosType())
                        .fipsMode(cVo.getFipsMode())
                        .version(new VersionBuilder().major(Integer.parseInt(ver[0])).minor(Integer.parseInt(ver[1])).build())
                        .switchType(cVo.getSwitchType())
                        .firewallType(cVo.getFirewallType())
                        .logMaxMemoryUsedThreshold(cVo.getLogMaxMemory())
                        .logMaxMemoryUsedThresholdType(cVo.getLogMaxType())
                        .virtService(cVo.isVirtService())
                        .errorHandling(new ErrorHandlingBuilder().onError(cVo.getRecoveryPolicy()))
                        .migration(new MigrationOptionsBuilder()
                                        // TODO: 마이그레이션 정책 관련 설정 값 조회 기능 존재여부 확인필요
//                            .policy(new MigrationPolicyBuilder()
//                                    .build())
                                        .bandwidth(new MigrationBandwidthBuilder().assignmentMethod(cVo.getBandwidth()))
                                        .encrypted(cVo.getEncrypted())
                        )
                        .fencingPolicy(new FencingPolicyBuilder()
                                .skipIfConnectivityBroken(new SkipIfConnectivityBrokenBuilder().enabled(true))
                                .skipIfSdActive(new SkipIfSdActiveBuilder().enabled(true)));

                if (cVo.getNetworkProvider().equals(true)) {
                    clusterBuilder.externalNetworkProviders(openStackNetworkProvider);
                }

                Cluster cluster = clusterBuilder.build();

                clustersService.add().cluster(cluster).send();

                return CommonVo.successResponse();
            }else {
                log.error("클러스터 이름 중복 에러");
                return CommonVo.failResponse("클러스터 이름 중복 에러");
            }
        }catch (Exception e){
            log.error("addClsuter error");
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public CommonVo<Boolean> editCluster(ClusterCreateVo cVo) {
        SystemService systemService = admin.getConnection().systemService();

        ClustersService clustersService = systemService.clustersService();
        ClusterService clusterService = systemService.clustersService().clusterService(cVo.getId());
        DataCenter dataCenter = systemService.dataCentersService().dataCenterService(cVo.getDatacenterId()).get().send().dataCenter();
        Network network = systemService.networksService().networkService(cVo.getNetworkId()).get().send().network();
        OpenStackNetworkProvider openStackNetworkProvider = systemService.openstackNetworkProvidersService().list().send().providers().get(0);

        boolean cName = clustersService.list().search("name="+cVo.getName()).send().clusters().isEmpty();
        String[] ver = cVo.getVersion().split("\\.");      // 버전값 분리

        try{
            if(cName) {
                ClusterBuilder clusterBuilder = new ClusterBuilder();
                clusterBuilder
                        .dataCenter(dataCenter) // 필수
                        .name(cVo.getName())    // 필수
                        .cpu(new CpuBuilder().architecture(cVo.getCpuArc()).type(cVo.getCpuType()))   // 필수
                        .description(cVo.getDescription())
                        .comment(cVo.getComment())
                        .managementNetwork(network)
                        .biosType(cVo.getBiosType())
                        .fipsMode(cVo.getFipsMode())
                        .version(new VersionBuilder()
                                .major(Integer.parseInt(ver[0]))
                                .minor(Integer.parseInt(ver[1]))
                                .build())  // 호환 버전
//                    .switchType(cVo.getSwitchType())      // 선택불가
                        .firewallType(cVo.getFirewallType())
                        .logMaxMemoryUsedThreshold(cVo.getLogMaxMemory())
                        .logMaxMemoryUsedThresholdType(LogMaxMemoryUsedThresholdType.PERCENTAGE)
                        .virtService(cVo.isVirtService())
//                    .glusterService(cVo.isGlusterService())
                        .errorHandling(new ErrorHandlingBuilder().onError(cVo.getRecoveryPolicy()))   // 복구정책
                        .migration(new MigrationOptionsBuilder()
                                // 마이그레이션 정책
                                .bandwidth(new MigrationBandwidthBuilder().assignmentMethod(cVo.getBandwidth()))    // 대역폭
                                .encrypted(cVo.getEncrypted())      // 암호화
                        );
                if (cVo.getNetworkProvider().equals(true)) {
                    clusterBuilder.externalNetworkProviders(openStackNetworkProvider);
                }

                Cluster cluster = clusterBuilder.build();

                log.info("editCluster: " + cluster.name());
                clusterService.update().cluster(cluster).send();

                return CommonVo.successResponse();
            }else {
                log.error("클러스터 이름 중복 에러");
                return CommonVo.failResponse("클러스터 이름 중복 에러");
            }
        }catch (Exception e){
            log.error("error: ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public CommonVo<Boolean> deleteCluster(String id) {
        SystemService systemService = admin.getConnection().systemService();

        ClusterService clusterService = systemService.clustersService().clusterService(id);
        String name = clusterService.get().send().cluster().name();

        try {
            clusterService.remove().send();
            log.info("delete cluster: {}", name);
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("error ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }







    public String getStatistic(SystemService systemService, String id){
        List<Statistic> statisticList = systemService.vmsService().vmService(id).statisticsService().list().send().statistics();

        String upTime = null;
        for(Statistic statistic : statisticList) {
            long hour = 0;
            if (statistic.name().equals("elapsed.time")) {
                hour = statistic.values().get(0).datum().longValue() / (60*60);      // 시간

                if(hour > 24){ upTime = hour/24 + "일";
                }else if( hour > 1 && hour < 24){ upTime = hour + "시간";
                }else { upTime = (statistic.values().get(0).datum().longValue() / 60) + "분";}
            }
        }

//        return statisticList.stream()
//                .filter(statistic -> statistic.name().equals("elapsed.time"))
//                .map(statistic -> statistic.values().get(0).datum().longValue() / (60*60))
//                .collect();
        return upTime;
    }

    public String getIp(SystemService systemService, String id, String version){
        List<Nic> nicList = systemService.vmsService().vmService(id).nicsService().list().send().nics();
        Vm vm = systemService.vmsService().vmService(id).get().send().vm();

        String ipv4 = null;
        String ipv6 = null;
        for (Nic nic : nicList){
            List<ReportedDevice> reportedDeviceList = systemService.vmsService().vmService(id).nicsService().nicService(nic.id()).reportedDevicesService().list().send().reportedDevice();
            for (ReportedDevice r : reportedDeviceList){
                ipv4 = !vm.status().value().equals("down") ? r.ips().get(0).address() : null;
                ipv6 = !vm.status().value().equals("down") ? r.ips().get(1).address() : null;
            }
        }

        if(version.equals("v4")){ return ipv4; }
        else { return ipv6; }
    }




    // 선호도 그룹 멤버
    public List<String> getMember(SystemService systemService, String id, String agId, String ele){
        List<Vm> vmList = systemService.clustersService().clusterService(id).affinityGroupsService().groupService(agId).vmsService().list().send().vms();
        List<Host> hostList = systemService.clustersService().clusterService(id).affinityGroupsService().groupService(agId).hostsService().list().send().hosts();

        if(ele.equals("vm")){
            return vmList.stream()
                    .map(Identified::name)
                    .collect(Collectors.toList());
        }else{
            return hostList.stream()
                    .map(Identified::name)
                    .collect(Collectors.toList());
        }
    }

    // 선호도 그룹 레이블
    public List<String> getLabel(SystemService systemService, String id, String alId, String ele){
        List<AffinityLabel> vmLabel = systemService.clustersService().clusterService(id).affinityGroupsService().groupService(alId).vmLabelsService().list().send().labels();
        List<AffinityLabel> hostLabel = systemService.clustersService().clusterService(id).affinityGroupsService().groupService(alId).hostLabelsService().list().send().labels();

        if(ele.equals("vm")) {
            return vmLabel.stream()
                    .filter(Objects::nonNull)
                    .map(Identified::name)
                    .collect(Collectors.toList());
        } else if (ele.equals("host")) {
            return hostLabel.stream()
                    .filter(Objects::nonNull)
                    .map(Identified::name)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }




    public int getHostCnt(List<Host> hostList, String id){
        return (int) hostList.stream()
                .filter(host -> host.cluster().id().equals(id))
                .count();
    }

    public int getHostUpCnt(List<Host> hostList, String id){
        return (int) hostList.stream()
                .filter(host -> host.cluster().id().equals(id) && host.status().value().equals("up"))
                .count();
    }

    public int getHostDownCnt(List<Host> hostList, String id){
        return (int) hostList.stream()
                .filter(host -> host.cluster().id().equals(id) && !host.status().value().equals("up"))
                .count();
    }

    public int getVmCnt(List<Vm> vmList, String id){
        return (int) vmList.stream()
                .filter(vm -> vm.cluster().id().equals(id))
                .count();
    }
    public int getVmUpCnt(List<Vm> vmList, String id){
        return (int) vmList.stream()
                .filter(vm -> vm.cluster().id().equals(id) && vm.status().value().equals("up"))
                .count();
    }
    public int getVmDownCnt(List<Vm> vmList, String id){
        return (int) vmList.stream()
                .filter(vm -> vm.cluster().id().equals(id) && vm.status().value().equals("up"))
                .count();
    }


}
