package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.NetworkUsageVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.ovirt.OvirtService;
import com.itinfo.itcloud.service.ItClusterService;
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
public class ClusterServiceImpl implements ItClusterService {

    @Autowired private AdminConnectionService adminConnectionService;
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

//        Connection connection = adminConnectionService.getConnection();
//        SystemService systemService = connection.systemService();

        List<ClusterVo> cVoList = new ArrayList<>();
        ClusterVo cVo = null;

        List<Cluster> clusterList = ovirt.clusterList();
//        List<Cluster> clusterList = ((ClustersService.ListResponse)systemService.clustersService().list().send()).clusters();

        for(Cluster cluster : clusterList){
            cVo = new ClusterVo();

            cVo.setId(cluster.id());
            cVo.setName(cluster.name());
            cVo.setComment(cluster.comment());
            cVo.setVersion(cluster.version().major() + "." + cluster.version().minor());
            cVo.setDescription(cluster.description());
            cVo.setCpuType(cluster.cpuPresent() ? cluster.cpu().type() : null);
//            clusterVO.setStatus(cluster.);        // 업그레이드 상태

//            getHostCnt(systemService, cVo);
//            getVmCnt(systemService, cVo);

            getHostCnt(cVo);
            getVmCnt(cVo);

            cVoList.add(cVo);
        }

        long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        System.out.println("수행시간(ms): " + (end-start));

        return cVoList;
    }

    @Override
    public ClusterVo getInfo(String id){
        Cluster cluster = ovirt.cluster(id);
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

//        getVmCnt(cVo);
        return cVo;
    }


    @Override
    public List<NetworkVo> getNetwork(String id) {
        List<NetworkVo> nwVoList = new ArrayList<>();
        NetworkVo nwVo = null;

        List<Network> networkList = ovirt.cNetworkList(id);
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
        List<HostVo> hVoList = new ArrayList<>();
        HostVo hVo = null;

        List<Host> hostList = ovirt.hostList();
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
        List<VmVo> vmVoList = new ArrayList<>();
        VmVo vmVo = null;
        Date now = new Date(System.currentTimeMillis());

        List<Vm> vmList = ovirt.vmList();
        for (Vm vm : vmList) {
            if(vm.cluster().id().equals(id)) {
                vmVo = new VmVo();

                vmVo.setId(vm.id());
                vmVo.setName(vm.name());
                vmVo.setStatus(vm.status().value());        // vmstatus 많음

                List<Statistic> statisticList = ovirt.vmStatisticList(vm.id());
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
                    List<ReportedDevice> reportedDeviceList = ovirt.cReportedDeviceList(vm.id(), nic.id());
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
    public List<AffinityGroupVo> getAffinitygroups(String id){
        List<AffinityGroupVo> agVoList = new ArrayList<>();
        AffinityGroupVo agVo = null;

        List<AffinityGroup> affinityGroupList = ovirt.cAffinityGroupList(id);
        for(AffinityGroup a : affinityGroupList){
            agVo = new AffinityGroupVo();

            agVo.setName(a.name());
            agVo.setDescription(a.description());
//            agVo.setStatus(a.broken());         // a.broken(boolean)으로 선호도 그룹 상태를 파악

            agVo.setPriority(a.priority().intValue());      //  우선순위

            // vmsRule
            agVo.setVmEnabled(a.vmsRule().enabled());

            if(a.positivePresent() && a.positive()){
                agVo.setVmPositive(a.vmsRule().positive());
                agVo.setPositive(a.positive());
            }

            if(a.enforcingPresent() && a.enforcing()){
                agVo.setVmEnforcing(a.vmsRule().enforcing());
                agVo.setEnforcing(a.enforcing());
            }

            // hostRule
            agVo.setHostEnabled(a.hostsRule().enabled());
            agVo.setHostPositive(a.hostsRule().positive());
            agVo.setHostEnforcing(a.hostsRule().enforcing());


            // 가상머신에 멤버 레이블
            // 호스트에 멤버 레이블
            // 이건 나중에
            // 객체로 할지 그냥 이름만 보여주고 말지를 정해야한다.
//            if(a.hostLabelsPresent()){
//                for(AffinityLabel affinityLabel : a.hostLabels()){
//                    agVo.setHostLabels(((AffinityLabelService.GetResponse)systemService.affinityLabelsService().labelService(affinityLabel.id()).get().send()).label().name());
//                    System.out.println("aff : "+affinityLabel.id());
//                    System.out.println("aff : "+ ((AffinityLabelService.GetResponse)systemService.affinityLabelsService().labelService(affinityLabel.id()).get().send()).label().name());
//                }
//            }
//
//            // 가상머신 멤버 (수정 필요)
//            List<Vm> vmList =
//                    ((AffinityGroupVmsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().groupService(a.id()).vmsService().list().send()).vms();
//            String vmNames = "";
//
//            for(Vm vm : vmList){
//                vmNames += vm.name() + " ";
//            }
//            agVo.setVmList(vmNames);
//
//            // 호스트 멤버 (수정 필요)
//            List<Host> hostList =
//                    ((AffinityGroupHostsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().groupService(a.id()).hostsService().list().send()).hosts();
//            String hostNames = "";
//
//            for(Host host : hostList){
//                hostNames += host.name() + " ";
//            }
//            agVo.setHostList(hostNames);

            agVoList.add(agVo);
        }
        return agVoList;
    }


    //수정
    @Override
    public List<AffinityLabelVo> getAffinitylabels(String id) {
        List<AffinityLabelVo> alVoList = new ArrayList<>();
        AffinityLabelVo alVo = null;

        List<AffinityLabel> affinityLabelList = ovirt.cAffinityLabelList();
        for(AffinityLabel affinityLabel : affinityLabelList){
            alVo = new AffinityLabelVo();
            alVo.setName(affinityLabel.name());

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
        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList = ovirt.cPermissionList(id);
        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            if(permission.groupPresent() && !permission.userPresent()){
                Group group = ovirt.group(permission.group().id());
                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                // 생성일의 경우 db에서 가져와야함

                Role role = ovirt.role(permission.role().id());
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            if(permission.userPresent() && !permission.groupPresent()){
                User user = ovirt.user(permission.user().id());
                pVo.setUser(user.name());
                pVo.setNameSpace(user.namespace());

                Role role = ovirt.role(permission.role().id());
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    @Override
    public List<EventVo> getEvent(String id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;

        List<Event> eventList = ovirt.eventList();
        Cluster c = ovirt.cluster(id);

        for(Event event : eventList){
            eVo = new EventVo();

            if(event.clusterPresent() && event.cluster().name().equals(c.name())){
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


    public void getHostCnt(ClusterVo clusterVo) {
        List<Host> hostList = ovirt.hostList();
//        List<Host> hostList = ((HostsService.ListResponse)systemService.hostsService().list().send()).hosts();

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


    public void getVmCnt(ClusterVo clusterVo){
        List<Vm> vmList = ovirt.vmList();
//        List<Vm> vmList = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();

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
