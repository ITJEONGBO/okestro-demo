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
        SystemService systemService = admin.getConnection().systemService();

        List<ClusterVo> cVoList = new ArrayList<>();
        ClusterVo cVo = null;

        List<Cluster> clusterList = ((ClustersService.ListResponse)systemService.clustersService().list().send()).clusters();
        for(Cluster cluster : clusterList){
            cVo = new ClusterVo();

            cVo.setId(cluster.id());
            cVo.setName(cluster.name());
            cVo.setComment(cluster.comment());
            cVo.setVersion(cluster.version().major() + "." + cluster.version().minor());
            cVo.setDescription(cluster.description());
            cVo.setCpuType(cluster.cpuPresent() ? cluster.cpu().type() : null);
//            clusterVO.setStatus(cluster.);        // 업그레이드 상태

            getHostCnt(systemService, cVo);
            getVmCnt(systemService, cVo);

            cVoList.add(cVo);
        }

        return cVoList;
    }

    @Override
    public ClusterVo getInfo(String id){
        SystemService systemService = admin.getConnection().systemService();

        Cluster cluster = ((ClusterService.GetResponse) systemService.clustersService().clusterService(id).get().send()).cluster();
        ClusterVo cVo = new ClusterVo();
        cVo.setId(id);

        cVo.setName(cluster.name());
        cVo.setDescription(cluster.description());
        cVo.setVersion(cluster.version().major() + "." + cluster.version().minor());

        cVo.setDatacenterId(cluster.dataCenter().id());
        cVo.setDatacenterName(ovirt.getName("datacenter", cluster.dataCenter().id()) );
        cVo.setCpuType(cluster.cpuPresent() ? cluster.cpu().type() : null);
        cVo.setChipsetFirmwareType(cluster.biosTypePresent() ? cluster.biosType().value() : null);

        cVo.setThreadsAsCore(cluster.threadsAsCores());
        cVo.setMemoryOverCommit(cluster.memoryPolicy().overCommit().percentAsInteger());

        if(cluster.errorHandling().onError().value().equals("do_not_migrate")){
            cVo.setRestoration("아니요");
        }else if(cluster.errorHandling().onError().value().equals("migrate_highly_available")){
            cVo.setRestoration("높은 우선 순위만");
        }else{
            cVo.setRestoration("예");
        }

        getVmCnt(systemService, cVo);
        return cVo;
    }


    @Override
    public List<NetworkVo> getNetwork(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<NetworkVo> nwVoList = new ArrayList<>();
        NetworkVo nwVo = null;

        List<Network> networkList = ((ClusterNetworksService.ListResponse) systemService.clustersService().clusterService(id).networksService().list().send()).networks();
        if (networkList != null) {
            for (Network network : networkList) {
                nwVo = new NetworkVo();
                nwVo.setId(network.id());
                nwVo.setName(network.name());
                nwVo.setStatus(network.status().value());
                nwVo.setDescription(network.description());

                // usages
                NetworkUsageVo nuVo = new NetworkUsageVo();
                nuVo.setVm(network.usages().contains(NetworkUsage.VM));
                nuVo.setDisplay(network.usages().contains(NetworkUsage.DISPLAY));
                nuVo.setMigration(network.usages().contains(NetworkUsage.MIGRATION));
                nuVo.setManagement(network.usages().contains(NetworkUsage.MANAGEMENT));
                nuVo.setDefaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE));
                nuVo.setGluster(network.usages().contains(NetworkUsage.GLUSTER));

                nwVo.setNetworkUsageVo(nuVo);

                nwVoList.add(nwVo);
            }
        }
        return nwVoList;
    }

    @Override
    public List<HostVo> getHost(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<HostVo> hVoList = new ArrayList<>();
        HostVo hVo = null;

        List<Host> hostList = ((HostsService.ListResponse)systemService.hostsService().list().send()).hosts();
        for (Host host : hostList) {
            if (host.cluster().id().equals(id)) {
                hVo = new HostVo();

                hVo.setId(host.id());
                hVo.setName(host.name());
                hVo.setStatus(host.status().value());
                hVo.setAddress(host.address());
                hVo.setStatus(host.status().value());
                hVo.setVmUpCnt(getHostVmsCnt(host.id()));

                hVoList.add(hVo);
            }
        }
        return hVoList;
    }


    @Override
    public List<VmVo> getVm(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<VmVo> vmVoList = new ArrayList<>();
        VmVo vmVo = null;
        Date now = new Date(System.currentTimeMillis());

        List<Vm> vmList = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();
        for (Vm vm : vmList) {
            if(vm.cluster().id().equals(id)) {
                vmVo = new VmVo();

                vmVo.setId(vm.id());
                vmVo.setName(vm.name());
                vmVo.setStatus(vm.status().value());        // vmstatus 많음

                List<Statistic> statisticList = ((StatisticsService.ListResponse) systemService.vmsService().vmService(vm.id()).statisticsService().list().send()).statistics();
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
                    List<ReportedDevice> reportedDeviceList = ((VmReportedDevicesService.ListResponse) systemService.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).reportedDevicesService().list().send()).reportedDevice();
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

        List<AffinityGroup> affinityGroupList = ((AffinityGroupsService.ListResponse) systemService.clustersService().clusterService(id).affinityGroupsService().list().send()).groups();
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
            List<Vm> vmList =
                    ((AffinityGroupVmsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().groupService(ag.id()).vmsService().list().send()).vms();
            List<String> vmNames = new ArrayList<>();
            for (Vm vm : vmList){
                vmNames.add(vm.name());
            }
            agVo.setVmList(vmNames);

            // 호스트 멤버 (수정 필요)
            List<Host> hostList =
                    ((AffinityGroupHostsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().groupService(ag.id()).hostsService().list().send()).hosts();
            List<String> hostNames = new ArrayList<>();
            for(Host host : hostList){
                hostNames.add(host.name());
            }
            agVo.setHostList(hostNames);


            // 가상머신 레이블
            List<AffinityLabel> vmLabel = ((AffinityGroupVmLabelsService.ListResponse) systemService.clustersService().clusterService(id).affinityGroupsService().groupService(ag.id()).vmLabelsService().list().send()).labels();
            List<String> vms = new ArrayList<>();
            for(AffinityLabel affinityLabel : vmLabel) {
                if(affinityLabel != null){
                    vms.add(affinityLabel.name());
                }
            }
            agVo.setVmLabels(vms);

                // 호스트 레이블
            List<AffinityLabel> hostLabel = ((AffinityGroupHostLabelsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().groupService(ag.id()).hostLabelsService().list().send()).labels();
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

        List<AffinityLabel> affinityLabelList = ((AffinityLabelsService.ListResponse) systemService.affinityLabelsService().list().send()).labels();
        for(AffinityLabel affinityLabel : affinityLabelList){
            alVo = new AffinityLabelVo();
            alVo.setId(affinityLabel.id());
            alVo.setName(affinityLabel.name());

            // 가상머신 멤버
            List<Vm> vmList = ((AffinityLabelVmsService.ListResponse) systemService.affinityLabelsService().labelService(affinityLabel.id()).vmsService().list().send()).vms();
            List<String> vms = new ArrayList<>();
            for(Vm vm : vmList){
                vms.add(ovirt.getName("vm", vm.id()));
            }
            alVo.setVms(vms);

            List<Host> hostList = ((AffinityLabelHostsService.ListResponse)systemService.affinityLabelsService().labelService(affinityLabel.id()).hostsService().list().send()).hosts();
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

        List<Permission> permissionList = ((AssignedPermissionsService.ListResponse) systemService.clustersService().clusterService(id).permissionsService().list().send()).permissions();
        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            // 그룹이 있고, 유저가 없을때
            if(permission.groupPresent() && !permission.userPresent()){
                Group group = ((GroupService.GetResponse) systemService.groupsService().groupService(permission.group().id()).get().send()).get();
                Role role = ((RoleService.GetResponse) systemService.rolesService().roleService(permission.role().id()).get().send()).role();

                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            // 그룹이 없고, 유저가 있을때
            if(!permission.groupPresent() && permission.userPresent()){
                User user = ((UserService.GetResponse) systemService.usersService().userService(permission.user().id()).get().send()).user();
                Role role = ((RoleService.GetResponse) systemService.rolesService().roleService(permission.role().id()).get().send()).role();

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

        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;
        String name = ovirt.getName("cluster", id);

        List<Event> eventList = ((EventsService.ListResponse)systemService.eventsService().list().send()).events();
        for(Event event : eventList){
            eVo = new EventVo();

            if(event.clusterPresent() && event.cluster().name().equals(name)){
                eVo.setSeverity(event.severity().value());
                eVo.setTime(sdf.format(event.time()));
                eVo.setMessage(event.description());
                eVo.setRelationId(event.correlationIdPresent() ? event.correlationId() : "");
                eVo.setSource(event.origin());

                eVoList.add(eVo);
            }
        }
        return eVoList;
    }


    // ----------------------------------------------------------------------------------------

    @Override
    public List<DataCenterVo> getDcList(){
        SystemService systemService = admin.getConnection().systemService();
        List<DataCenterVo> dcVoList = new ArrayList<>();
        DataCenterVo dcVo = null;

        List<DataCenter> dataCenterList = ((DataCentersService.ListResponse)systemService.dataCentersService().list().send()).dataCenters();
        for(DataCenter dataCenter : dataCenterList){
            dcVo = new DataCenterVo();

            dcVo.setId(dataCenter.id());
            dcVo.setName(dataCenter.name());

            dcVo.setNetworkList( getNetworkList(systemService, dataCenter.id()) );
            dcVoList.add(dcVo);
        }

        System.out.println( "서비스 데이터센터 이름: "+dcVo.getName() );
        System.out.println( "서비스 데이터센터 네트워므 리스트: " + dcVo.getNetworkList());

        return dcVoList;
    }


    public List<NetworkVo> getNetworkList(SystemService sys, String dcId){
        List<Network> networkList =
                ((DataCenterNetworksService.ListResponse) sys.dataCentersService().dataCenterService(dcId).networksService().list().send()).networks();

        List<NetworkVo> nwVoList = new ArrayList<>();
        NetworkVo nwVo = null;

        for(Network network : networkList){
            nwVo = new NetworkVo();

            nwVo.setId(network.id());
            nwVo.setName(network.name());
            nwVo.setDatacenterId(dcId);

            nwVoList.add(nwVo);
        }
        return nwVoList;
    }


    // edit 창
    @Override
    public ClusterCreateVo getClusterCreate(String id){
        SystemService systemService = admin.getConnection().systemService();

        Cluster cluster = ((ClusterService.GetResponse)systemService.clustersService().clusterService(id).get().send()).cluster();

        DataCenter dataCenter =
                ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send()).dataCenter();
        List<Network> networkList =
                ((ClusterNetworksService.ListResponse)systemService.clustersService().clusterService(id).networksService().list().send()).networks();

        ClusterCreateVo ccVo = new ClusterCreateVo();

        ccVo.setId(id);
        ccVo.setName(cluster.name());
        ccVo.setDescription(cluster.description());
        ccVo.setComment(cluster.comment());
        ccVo.setDatacenterId(dataCenter.id());
        ccVo.setDatacenterName(dataCenter.name());
        ccVo.setCpuType(cluster.cpuPresent() ? cluster.cpu().type() : null);
        ccVo.setCpuArc(cluster.cpuPresent() ? cluster.cpu().architecture() : null);
        ccVo.setBiosType(cluster.biosType());
        ccVo.setFipsMode(cluster.fipsMode());
        ccVo.setVersion(cluster.version().major() + "." + cluster.version().minor());
        ccVo.setSwitchType(cluster.switchType());
        ccVo.setFirewallType(cluster.firewallType());
        ccVo.setLogMaxMemory(cluster.logMaxMemoryUsedThresholdAsInteger());
        ccVo.setVirtService(cluster.virtService());
        ccVo.setGlusterService(cluster.glusterService());


        // display 여부인가?
        for(Network network : networkList){
            ccVo.setNetworkId(network.display() ? network.id() : null );
            ccVo.setNetworkName(network.display() ? network.name() : null);
        }
        System.out.println(ccVo.toString());
        return ccVo;
    }



    // 클러스터 생성
    @Override
    public boolean addCluster(ClusterCreateVo cVo) {
        // required: name , cpu.type, data_center   (Identify the datacenter with either id or name)
        SystemService systemService = admin.getConnection().systemService();
        ClustersService clustersService = systemService.clustersService();
        List<Cluster> clusterList = systemService.clustersService().list().send().clusters();
        DataCenter dataCenter =
                ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(cVo.getDatacenterId()).get().send()).dataCenter();
        Network network =
                ((NetworkService.GetResponse)systemService.networksService().networkService(cVo.getNetworkId()).get().send()).network();

        String[] ver = cVo.getVersion().split("\\.");      // 버전값 분리

        try{
            System.out.println(dataCenter.name());
            log.info("addCluster");

            Cluster cluster = new ClusterBuilder()
                    .dataCenter(dataCenter) // 필수
                    .name(cVo.getName())    // 필수
                    .cpu( new CpuBuilder().architecture(cVo.getCpuArc()).type(cVo.getCpuType()) )   // 필수
                    .description(cVo.getDescription())
                    .comment(cVo.getComment())
                    .managementNetwork(network)
                    .biosType(cVo.getBiosType())
                    .fipsMode(cVo.getFipsMode())
                    .version(new VersionBuilder().major(Integer.parseInt(ver[0])).minor(Integer.parseInt(ver[1])).build())  // 호환 버전
                    .switchType(cVo.getSwitchType())
                    .firewallType(cVo.getFirewallType())
                    .logMaxMemoryUsedThreshold(cVo.getLogMaxMemory())
                    .virtService(cVo.isVirtService())
                    .glusterService(cVo.isGlusterService())
//                     추가 난수 생성기 소스
//                    .migration(new MigrationOptionsBuilder()
//                            // 마이그레이션 정책
//                            .bandwidth(new MigrationBandwidthBuilder().assignmentMethod(cVo.getBandwidth()))    // 대역폭
//                            .encrypted(cVo.getEncrypted())      // 암호화
//                    )
                    .build();

            clustersService.add().cluster(cluster).send();

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

        DataCenter dataCenter =
                ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(cVo.getDatacenterId()).get().send()).dataCenter();
        Network network =
                ((NetworkService.GetResponse)systemService.networksService().networkService(cVo.getNetworkId()).get().send()).network();

        String[] ver = cVo.getVersion().split("\\.");      // 버전값 분리
        System.out.println(cVo.getVersion());


        try{
            System.out.println(dataCenter.name());
            log.info("editCluster");

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
//                    .migration(new MigrationOptionsBuilder()
//                            // 마이그레이션 정책
//                            .bandwidth(new MigrationBandwidthBuilder().assignmentMethod(cVo.getBandwidth()))    // 대역폭
//                            .encrypted(cVo.getEncrypted())      // 암호화
//                    )
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

            return clustersService.list().send().clusters().size() == ( cList.size()-1 );
        }catch (Exception e){
            log.error("error ", e);
            return false;
        }
    }





    public int getHostVmsCnt(String hostId){
        List<Vm> vmList = ovirt.vmList();

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
        List<Host> hostList = ((HostsService.ListResponse)systemService.hostsService().list().send()).hosts();

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
        clusterVo.setHostCnt(hostCnt);
        clusterVo.setHostUpCnt(hostUpCnt);
        clusterVo.setHostDownCnt(hostCnt - hostUpCnt);
    }


    public void getVmCnt(SystemService systemService, ClusterVo clusterVo){
        List<Vm> vmList = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();

        int vmsCnt = 0;
        int vmsUpCnt = 0;

        for (Vm vm : vmList) {
            if (vm.cluster().id().equals(clusterVo.getId())) {
                vmsUpCnt = (int) vmList.stream()
                        .filter(vmcnt -> vmcnt.status().value().equals("up"))
                        .count();
            }else{
                vmsCnt = 0;
                break;
            }
            vmsCnt++;
        }
        clusterVo.setVmCnt(vmsCnt);
        clusterVo.setVmUpCnt(vmsUpCnt);
        clusterVo.setVmDownCnt(vmsCnt - vmsUpCnt);
    }



}
