package com.itinfo.itcloud.service.computing;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClusterServiceImpl implements ItClusterService {

    @Autowired private AdminConnectionService admin;
    @Autowired private OvirtService ovirt;

    @Override
    public String getName(String id){
        return ovirt.getName("cluster", id);
    }

    // 컴퓨팅-클러스터 목록
    // 상태, 이름, 코멘트, 호환버전, 설명, 클러스터cpu유형, 호스트수, 가상머신수, (업그레이드 상태)
    @Override
    public List<ClusterVo> getList(){
        long start = System.currentTimeMillis();
        SystemService systemService = admin.getConnection().systemService();

        List<ClusterVo> cVoList = new ArrayList<>();
        ClusterVo cVo = null;

        List<Cluster> clusterList = systemService.clustersService().list().send().clusters();
        for(Cluster cluster : clusterList){
            cVo = ClusterVo.builder()
                    .id(cluster.id())
                    .name(cluster.name())
                    .comment(cluster.comment())
                    .version(cluster.version().major() + "." + cluster.version().minor())
                    .description(cluster.description())
                    .cpuType(cluster.cpuPresent() ? cluster.cpu().type() : null)
                    .build();

//            clusterVO.setStatus(cluster.);        // 업그레이드 상태
            getHostCnt(systemService, cVo);
            getVmCnt(systemService, cVo);

            cVoList.add(cVo);
        }

        long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        System.out.println("cluster getList 수행시간(ms): " + (end-start));

        return cVoList;
    }

//    public List<ClusterVo> getList() {
//        long start = System.currentTimeMillis();
//        SystemService systemService = admin.getConnection().systemService();
//
//        return systemService.clustersService().list().send().clusters().parallelStream()
//                .map(cluster -> {
//                    ClusterVo cVo = ClusterVo.builder()
//                            .id(cluster.id())
//                            .name(cluster.name())
//                            .comment(cluster.comment())
//                            .version(cluster.version().major() + "." + cluster.version().minor())
//                            .description(cluster.description())
//                            .cpuType(cluster.cpuPresent() ? cluster.cpu().type() : null)
//                            .build();
//
//                    getHostCnt(systemService, cVo);
//                    getVmCnt(systemService, cVo);
//
//                    long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
//                    System.out.println("cluster getList 수행시간(ms): " + (end-start));
//
//                    return cVo;
//                })
//                .collect(Collectors.toList());
//
//    }

    @Override
    public ClusterVo getInfo(String id){
        SystemService systemService = admin.getConnection().systemService();
        Cluster cluster = systemService.clustersService().clusterService(id).get().send().cluster();

        ClusterVo cVo = ClusterVo.builder()
                .id(id)
                .name(cluster.name())
                .description(cluster.description())
                .version(cluster.version().major() + "." + cluster.version().minor())
                .datacenterId(cluster.dataCenterPresent() ? cluster.dataCenter().id() : "")
                .datacenterName(cluster.dataCenterPresent() ? ovirt.getName("datacenter", cluster.dataCenter().id()) : "")
                .cpuType(cluster.cpuPresent() ? cluster.cpu().type() : null)
                .chipsetFirmwareType(cluster.biosTypePresent() ? cluster.biosType().value() : null)
                .threadsAsCore(cluster.threadsAsCores())
                .memoryOverCommit(cluster.memoryPolicy().overCommit().percentAsInteger())
                .restoration(cluster.errorHandling().onError().value())
                .build();

//        if(cluster.errorHandling().onError().value().equals("do_not_migrate")){
//            cVo.setRestoration("아니요");
//        }else if(cluster.errorHandling().onError().value().equals("migrate_highly_available")){
//            cVo.setRestoration("높은 우선 순위만");
//        }else{
//            cVo.setRestoration("예");
//        }

        getVmCnt(systemService, cVo);
        return cVo;
    }


    @Override
    public List<NetworkVo> getNetwork(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<NetworkVo> nwVoList = new ArrayList<>();
        NetworkVo nwVo = null;

        List<Network> networkList = systemService.clustersService().clusterService(id).networksService().list().send().networks();
        if (networkList != null) {
            for (Network network : networkList) {
                nwVo = NetworkVo.builder()
                        .id(network.id())
                        .name(network.name())
                        .status(network.status().value())
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
                        .build();

                nwVoList.add(nwVo);
            }
        }
        return nwVoList;
    }

    @Override
    public List<HostVo> getHost(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<HostVo> hVoList = new ArrayList<>();
        HostVo hostVo = null;

        List<Host> hostList = systemService.hostsService().list().send().hosts();
        for (Host host : hostList) {
            if (host.cluster().id().equals(id)) {
                hostVo = HostVo.builder()
                        .id(host.id())
                        .name(host.name())
                        .status(host.status().value())
                        .address(host.address())
                        .vmUpCnt(getHostVmsCnt(systemService, host.id()))
                        .build();

                hVoList.add(hostVo);
            }
        }
        return hVoList;
    }


    @Override
    public List<VmVo> getVm(String id) {
        SystemService systemService = admin.getConnection().systemService();
        Date now = new Date(System.currentTimeMillis());

        List<VmVo> vmVoList = new ArrayList<>();
        VmVo vmVo = null;

        List<Vm> vmList = systemService.vmsService().list().send().vms();
        for (Vm vm : vmList) {
            if(vm.cluster().id().equals(id)) {
                vmVo = new VmVo();

                vmVo.setId(vm.id());
                vmVo.setName(vm.name());
                vmVo.setStatus(vm.status().value());        // vmstatus 많음

                List<Statistic> statisticList = systemService.vmsService().vmService(vm.id()).statisticsService().list().send().statistics();
                for(Statistic statistic : statisticList) {
                    long hour = 0;

                    if (statistic.name().equals("elapsed.time")) {
                        hour = statistic.values().get(0).datum().longValue() / (60*60);      //시간

                        if(hour > 24){
                            vmVo.setUpTime(hour/24 + "일");
                        }else if( hour > 1 && hour < 24){
                            vmVo.setUpTime(hour + "시간");
                        }else {
                            vmVo.setUpTime( (statistic.values().get(0).datum().longValue() / 60) + "분");
                        }
                    }
                }

                // ip 주소
                List<Nic> nicList = ovirt.cNicList(vm.id());
                for (Nic nic : nicList) {
                    List<ReportedDevice> reportedDeviceList = systemService.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).reportedDevicesService().list().send().reportedDevice();
                    for (ReportedDevice r : reportedDeviceList) {
                        vmVo.setIpv4(!vm.status().value().equals("down") ? r.ips().get(0).address() : null);
                        vmVo.setIpv6(!vm.status().value().equals("down") ? r.ips().get(1).address() : null);
                    }
                }
                vmVoList.add(vmVo);
            }
        }
        return vmVoList;
    }

    @Override
    public List<AffinityGroupVo> getAffinitygroup(String id){
        SystemService systemService = admin.getConnection().systemService();

        List<AffinityGroupVo> agVoList = new ArrayList<>();
        AffinityGroupVo agVo = null;

        List<AffinityGroup> affinityGroupList = systemService.clustersService().clusterService(id).affinityGroupsService().list().send().groups();
        for(AffinityGroup ag : affinityGroupList){
            agVo = new AffinityGroupVo();

            agVo.setName(ag.name());
            agVo.setDescription(ag.description());
            agVo.setStatus(ag.broken());
            agVo.setPriority(ag.priority().intValue());      //  우선순위


            // vmsRule
            // 가상머신 측 극성 (비활성화) = enabled, positive
            // 가상머신 측 극성 (양극, 음극)
            agVo.setPositive(ag.positivePresent() && ag.positive());

            agVo.setVmEnabled(ag.vmsRule().enabled());
            agVo.setVmPositive(ag.vmsRule().positive());
            agVo.setVmEnforcing(ag.vmsRule().enforcing());

            // hostRule
            // 호스트 측 극성 = enabled, positive
            // 호스트 측 극성 (양극, 음극)
            agVo.setHostEnabled(ag.hostsRule().enabled());
            agVo.setHostPositive(ag.hostsRule().positive());
            agVo.setHostEnforcing(ag.hostsRule().enforcing());


            // 가상머신 멤버 (수정 필요)
            List<Vm> vmList = systemService.clustersService().clusterService(id).affinityGroupsService().groupService(ag.id()).vmsService().list().send().vms();
            List<String> vmNames = new ArrayList<>();
            for (Vm vm : vmList){
                vmNames.add(vm.name());
            }
            agVo.setVmList(vmNames);

            // 호스트 멤버 (수정 필요)
            List<Host> hostList = systemService.clustersService().clusterService(id).affinityGroupsService().groupService(ag.id()).hostsService().list().send().hosts();
            List<String> hostNames = new ArrayList<>();
            for(Host host : hostList){
                hostNames.add(host.name());
            }
            agVo.setHostList(hostNames);


            // 가상머신 레이블
            List<AffinityLabel> vmLabel = systemService.clustersService().clusterService(id).affinityGroupsService().groupService(ag.id()).vmLabelsService().list().send().labels();
            List<String> vms = new ArrayList<>();
            for(AffinityLabel affinityLabel : vmLabel) {
                if(affinityLabel != null){
                    vms.add(affinityLabel.name());
                }
            }
            agVo.setVmLabels(vms);

                // 호스트 레이블
            List<AffinityLabel> hostLabel = systemService.clustersService().clusterService(id).affinityGroupsService().groupService(ag.id()).hostLabelsService().list().send().labels();
            List<String> hosts = new ArrayList<>();
            for(AffinityLabel affinityLabel : hostLabel){
                if(affinityLabel != null){
                    hosts.add(affinityLabel.name());
                }
            }
            agVo.setHostLabels(hosts);


            agVoList.add(agVo);
        }
        return agVoList;
    }


    //수정
    @Override
    public List<AffinityLabelVo> getAffinitylabel() {
        SystemService systemService = admin.getConnection().systemService();

        List<AffinityLabelVo> alVoList = new ArrayList<>();
        AffinityLabelVo alVo = null;

        List<AffinityLabel> affinityLabelList = systemService.affinityLabelsService().list().send().labels();
        for(AffinityLabel affinityLabel : affinityLabelList){
            alVo = new AffinityLabelVo();
            alVo.setId(affinityLabel.id());
            alVo.setName(affinityLabel.name());

            // 가상머신 멤버
            List<Vm> vmList = systemService.affinityLabelsService().labelService(affinityLabel.id()).vmsService().list().send().vms();
            List<String> vms = new ArrayList<>();
            for(Vm vm : vmList){
                vms.add(ovirt.getName("vm", vm.id()));
            }
            alVo.setVms(vms);

            List<Host> hostList = systemService.affinityLabelsService().labelService(affinityLabel.id()).hostsService().list().send().hosts();
            List<String> hosts = new ArrayList<>();
            for(Host host : hostList){
                hosts.add(ovirt.getName("host", host.id()));
            }
            alVo.setHosts(hosts);

            alVoList.add(alVo);
        }
        return alVoList;
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
        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList = systemService.clustersService().clusterService(id).permissionsService().list().send().permissions();
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
    public List<EventVo> getEvent(String id) {
        SystemService systemService = admin.getConnection().systemService();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<Event> eventList = systemService.eventsService().list().send().events();
        List<EventVo> eVoList = new ArrayList<>();

        EventVo eVo = null;
        String name = getName(id);

        for(Event event : eventList){
            if(event.clusterPresent() && event.cluster().name().equals(name)){
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


    // ----------------------------------------------------------------------------------------

    // cluster-add.jsp에서 datacenter 선택하기 위해 사용되는 dc list
    @Override
    public List<DataCenterVo> getDcList(){
        SystemService systemService = admin.getConnection().systemService();
        List<DataCenterVo> dcVoList = new ArrayList<>();
        DataCenterVo dcVo = null;

        List<DataCenter> dataCenterList = systemService.dataCentersService().list().send().dataCenters();
        for(DataCenter dataCenter : dataCenterList){
            dcVo = DataCenterVo.builder()
                    .id(dataCenter.id())
                    .name(dataCenter.name())
                    .storageType(dataCenter.local())
                    .networkList( getNetworkList(systemService, dataCenter.id()))
                    .build();

            dcVoList.add(dcVo);
            System.out.println(dcVo.toString());
        }
        return dcVoList;
    }


    public List<NetworkVo> getNetworkList(SystemService sys, String dcId){
        List<NetworkVo> nwVoList = new ArrayList<>();
        NetworkVo nwVo = null;

        List<Network> networkList = sys.dataCentersService().dataCenterService(dcId).networksService().list().send().networks();
        for(Network network : networkList){
            nwVo = NetworkVo.builder()
                    .id(network.id())
                    .name(network.name())
                    .datacenterId(dcId)
                    .build();

            nwVoList.add(nwVo);
        }
        return nwVoList;
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
            boolean display = network.display();
            networkId = display ? network.id() : null;
            networkName = display ? network.name() : null;
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
//                .virtService(cluster.virtService())
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
    public boolean addCluster(ClusterCreateVo cVo) {
        // required: name , cpu.type, data_center   (Identify the datacenter with either id or name)
        SystemService systemService = admin.getConnection().systemService();

        ClustersService clustersService = systemService.clustersService();
        List<Cluster> clusterList = systemService.clustersService().list().send().clusters();
        DataCenter dataCenter = systemService.dataCentersService().dataCenterService(cVo.getDatacenterId()).get().send().dataCenter();
        Network network = systemService.networksService().networkService(cVo.getNetworkId()).get().send().network();
//        OpenStackNetworkProvider networkProvider = systemService.openstackNetworkProvidersService().providerService(cVo.getNetworkProvider()).get().send().provider();

        String[] ver = cVo.getVersion().split("\\.");      // 버전값 분리

        try{
            log.info("addCluster: " + dataCenter.name());

            Cluster cluster = new ClusterBuilder()
                    .dataCenter(dataCenter) // 필수
                    .name(cVo.getName())    // 필수
                    .cpu( new CpuBuilder().architecture(cVo.getCpuArc()).type(cVo.getCpuType()) )   // 필수
                    .description(cVo.getDescription())
                    .comment(cVo.getComment())
                    .managementNetwork(network)
                    .biosType(cVo.getBiosType())
                    .fipsMode(cVo.getFipsMode())
                    .version(new VersionBuilder()
                                .major(Integer.parseInt(ver[0]))
                                .minor(Integer.parseInt(ver[1]))
                                .build())  // 호환 버전
                    .switchType(cVo.getSwitchType())
                    .firewallType(cVo.getFirewallType())
                    .logMaxMemoryUsedThreshold(cVo.getLogMaxMemory())
//                    .externalNetworkProviders( new ExternalProvider[]{networkProvider} )
                    .virtService(cVo.isVirtService())
                    .glusterService(cVo.isGlusterService())
//                     추가 난수 생성기 소스
                    .errorHandling( new ErrorHandlingBuilder().onError(cVo.getRecoveryPolicy()) )   // 복구정책
                    .migration(new MigrationOptionsBuilder()
                            // 마이그레이션 정책
                            .bandwidth(new MigrationBandwidthBuilder().assignmentMethod(cVo.getBandwidth()))    // 대역폭
                            .encrypted(cVo.getEncrypted())      // 암호화
                    )
                    .build();

            clustersService.add().cluster(cluster).send();

            System.out.println("-- add Cluster: " + cVo.toString());
            return clustersService.list().send().clusters().size() == (clusterList.size()+1);
        }catch (Exception e){
            log.error("error: ", e);
            return false;
        }

    }

    @Override
    public void editCluster(ClusterCreateVo cVo) {
        SystemService systemService = admin.getConnection().systemService();
        ClusterService clusterService = systemService.clustersService().clusterService(cVo.getId());

        DataCenter dataCenter = systemService.dataCentersService().dataCenterService(cVo.getDatacenterId()).get().send().dataCenter();
        Network network = systemService.networksService().networkService(cVo.getNetworkId()).get().send().network();

        String[] ver = cVo.getVersion().split("\\.");      // 버전값 분리
        System.out.println(cVo.getVersion());

        try{
            log.info("editCluster: " + dataCenter.name());

            Cluster cluster = new ClusterBuilder()
                    .dataCenter(dataCenter) // 필수
                    .name(cVo.getName())    // 필수
                    .cpu( new CpuBuilder().architecture(cVo.getCpuArc()).type(cVo.getCpuType()) )   // 필수
                    .description(cVo.getDescription())
                    .comment(cVo.getComment())
                    .managementNetwork(network)
                    .biosType(cVo.getBiosType())
                    .fipsMode(cVo.getFipsMode())
                    .version(new VersionBuilder().major(Integer.parseInt(ver[0])).minor(7).build())  // 호환 버전
                    .switchType(cVo.getSwitchType())
                    .firewallType(cVo.getFirewallType())
                    .logMaxMemoryUsedThreshold(cVo.getLogMaxMemory())
                    .virtService(cVo.isVirtService())
                    .glusterService(cVo.isGlusterService())
//                     추가 난수 생성기 소스
                    .errorHandling( new ErrorHandlingBuilder().onError(cVo.getRecoveryPolicy()) )
                    .migration(new MigrationOptionsBuilder()
                            // 마이그레이션 정책
                            .bandwidth(new MigrationBandwidthBuilder().assignmentMethod(cVo.getBandwidth()))    // 대역폭
                            .encrypted(cVo.getEncrypted())      // 암호화
                    )
                    .build();

            log.info("---" + cVo.toString());
            clusterService.update().cluster(cluster).send();

        }catch (Exception e){
            log.error("error: ", e);
        }
    }

    @Override
    public boolean deleteCluster(String id) {
        SystemService systemService = admin.getConnection().systemService();
        ClustersService clustersService = systemService.clustersService();
        List<Cluster> cList = clustersService.list().send().clusters();

        try {
            ClusterService clusterService = systemService.clustersService().clusterService(id);
            clusterService.remove().send();

            log.info("delete cluster");
            return clustersService.list().send().clusters().size() == ( cList.size()-1 );
        }catch (Exception e){
            log.error("error ", e);
            return false;
        }
    }


    public int getHostVmsCnt(SystemService systemService, String hostId){
        List<Vm> vmList = systemService.vmsService().list().send().vms();

        int vmsUpCnt = 0;

        for (Vm vm : vmList) {
//            System.out.println("hostVO.getHostId(): " + hostId);
//            // vm down 상태일때는 placement만 뜨는가
//            if(!vm.placementPolicy().hosts().isEmpty() && vm.placementPolicy().hosts().get(0).id().equals(hostId)){
//                vmsCnt++;
//                System.out.println("vmsCnt: " + vmsCnt);
//            }

            // vm up 상태
            if (vm.host() != null && vm.host().id().equals(hostId)) {
                if (vm.status().value().equals("up")) {
                    vmsUpCnt++;
                }
            }

        }
        return vmsUpCnt;
    }


    public void getHostCnt(SystemService systemService, ClusterVo clusterVo) {
        List<Host> hostList = systemService.hostsService().list().send().hosts();

        int hostCnt = 0;
        int hostUpCnt = 0;
        
        for (Host host : hostList) {
            if (host.cluster().id().equals(clusterVo.getId())) {
                hostUpCnt = (int) hostList.stream()
                        .filter(hostcnt -> hostcnt.status().value().equals("up"))
                        .count();
            }else{
                hostCnt =0;
                break;
            }
            hostCnt++;
        }
        clusterVo.hostCnt(hostCnt);
        clusterVo.hostUpCnt(hostUpCnt);
        clusterVo.hostDownCnt(hostCnt - hostUpCnt);
    }


    public void getVmCnt(SystemService systemService, ClusterVo clusterVo){
        List<Vm> vmList = systemService.vmsService().list().send().vms();

        int vmCnt = 0;
        int vmUpCnt = 0;

        for (Vm vm : vmList) {
            if (vm.cluster().id().equals(clusterVo.getId())) {
                vmUpCnt = (int) vmList.stream()
                        .filter(vmcnt -> vmcnt.status().value().equals("up"))
                        .count();
            }else{
                vmCnt = 0;
                break;
            }
            vmCnt++;
        }
        clusterVo.vmCnt(vmCnt);
        clusterVo.vmUpCnt(vmUpCnt);
        clusterVo.vmDownCnt(vmCnt - vmUpCnt);
    }



}
