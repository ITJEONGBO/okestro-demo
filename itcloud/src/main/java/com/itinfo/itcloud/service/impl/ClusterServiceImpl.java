package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItClusterService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ClusterServiceImpl implements ItClusterService {

    @Autowired private AdminConnectionService adminConnectionService;


    // 컴퓨팅-클러스터 목록
    // 상태, 이름, 코멘트, 호환버전, 설명, 클러스터cpu유형, 호스트수, 가상머신수, (업그레이드 상태)
    @Override
    public List<ClusterVo> getList(){
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<ClusterVo> cVoList = new ArrayList<>();
        ClusterVo cVo = null;

        List<Cluster> clusterList =
                ((ClustersService.ListResponse) systemService.clustersService().list().send()).clusters();

        for(Cluster cluster : clusterList){
            cVo = new ClusterVo();

            cVo.setId(cluster.id());
            cVo.setName(cluster.name());
            cVo.setComment(cluster.comment());
            cVo.setVersion(cluster.version().major() + "." + cluster.version().minor());
            cVo.setDescription(cluster.description());

            if(cluster.cpu() != null) {
                cVo.setCpuType(cluster.cpu().type());
            }
//            clusterVO.setStatus(cluster.);        // 업그레이드 상태

            getHostCnt(systemService, cVo);
            getVmCnt(systemService, cVo);

            cVoList.add(cVo);
        }
        return cVoList;
    }

    @Override
    public ClusterVo getInfo(String id){
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        Cluster cluster = ((ClusterService.GetResponse) systemService.clustersService().clusterService(id).get().send()).cluster();
        ClusterVo cVo = new ClusterVo();
        cVo.setId(id);

        cVo.setName(cluster.name());
        cVo.setDescription(cluster.description());
        cVo.setVersion(cluster.version().major() + "." + cluster.version().minor());

        // 데이터센터 이름
        if(cluster.dataCenter() != null){
            DataCenter dataCenter =
                    ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send()).dataCenter();
            cVo.setDatacenterId(dataCenter.id());
            cVo.setDatacenterName(dataCenter.name());
        }

        if (cluster.cpu() != null) {
            cVo.setCpuType(cluster.cpu().type());
        }

        if(cluster.biosType() != null){
            cVo.setChipsetFirmwareType(cluster.biosType().value());
        }

        cVo.setThreadsAsCore(cluster.threadsAsCores());
        cVo.setMemoryOverCommit(cluster.memoryPolicy().overCommit().percentAsInteger());
//            clusterVo.setRestoration(cluster.)    // 복구정책
        getVmCnt(systemService, cVo);
        return cVo;
    }


    @Override
    public List<NetworkVo> getNetwork(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<NetworkVo> nwVoList = new ArrayList<>();
        NetworkVo nwVo = null;

        List<Network> networkList =
                ((ClusterNetworksService.ListResponse) systemService.clustersService().clusterService(id).networksService().list().send()).networks();

        if (networkList != null) {
            for (Network network : networkList) {
                nwVo = new NetworkVo();
                nwVo.setId(network.id());
                nwVo.setName(network.name());
                nwVo.setStatus(network.status().value());
                nwVo.setDescription(network.description());
                System.out.println("클러스터 이름: " + ((ClusterService.GetResponse)systemService.clustersService().clusterService(id).get().send()).cluster().name() );
                nwVo.setDatacenterName( ((ClusterService.GetResponse)systemService.clustersService().clusterService(id).get().send()).cluster().name() );

                // usages
                nwVo.setVm(network.usages().contains(NetworkUsage.VM));
                nwVo.setDisplay(network.usages().contains(NetworkUsage.DISPLAY));
                nwVo.setMigration(network.usages().contains(NetworkUsage.MIGRATION));
                nwVo.setManagement(network.usages().contains(NetworkUsage.MANAGEMENT));
                nwVo.setDefaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE));
                nwVo.setGluster(network.usages().contains(NetworkUsage.GLUSTER));

                nwVoList.add(nwVo);
            }
        }
        return nwVoList;
    }

    @Override
    public List<HostVo> getHost(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<HostVo> hVoList = new ArrayList<>();
        HostVo hVo = null;

        List<Host> hostList =
                ((HostsService.ListResponse) systemService.hostsService().list().send()).hosts();

        for (Host host : hostList) {
            if (host.cluster().id().equals(id)) {
                hVo = new HostVo();

                hVo.setId(host.id());
                hVo.setName(host.name());
                hVo.setStatus(host.status().value());
                hVo.setAddress(host.address());
                hVo.setStatus(host.status().value());
                hVo.setVmUpCnt(getHostVmsCnt(systemService, host.id()));

                hVoList.add(hVo);
            }
        }
        return hVoList;
    }


    @Override
    public List<VmVo> getVm(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<VmVo> vmVoList = new ArrayList<>();
        VmVo vmVo = null;

        List<Vm> vmList =
                ((VmsService.ListResponse) systemService.vmsService().list().send()).vms();

        for (Vm vm : vmList) {
            if(vm.cluster().id().equals(id)) {
                vmVo = new VmVo();

                vmVo.setId(vm.id());
                vmVo.setName(vm.name());
                vmVo.setStatus(vm.status().value());        // vmstatus 많음

                // startTime

                if(vm.startTimePresent()) {
                    vmVo.setStartTime(vm.startTime());
                }
                if(vm.creationTimePresent()){
                    vmVo.setCreationTime(vm.creationTime());
                }

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
            }
        }
        return vmVoList;
    }

    @Override
    public List<AffinityGroupVo> getAffinitygroups(String id){
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<AffinityGroupVo> agVoList = new ArrayList<>();
        AffinityGroupVo agVo = null;

        List<AffinityGroup> affinityGroupList =
                ((AffinityGroupsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().list().send()).groups();

        for(AffinityGroup a : affinityGroupList){
            agVo = new AffinityGroupVo();
            agVo.setName(a.name());
            agVo.setDescription(a.description());
//            agVo.setStatus(a.); //상태 수정 필요
            agVo.setPriority(a.priority().intValue());

            agVo.setVmEnabled(a.vmsRule().enabled());
            agVo.setVmPositive(a.vmsRule().positive());
            agVo.setVmEnforcing(a.vmsRule().enforcing());

            // 수정 필요
//            if(a.positive() != null){
//                agVo.setVmPositive(a.positive());    // 애매 a.positive()와 a.vmsRule().positive()
//            }
//            agVo.setVmEnforcing(a.enforcing());  // 얘도 마찬가지

            agVo.setHostEnabled(a.hostsRule().enabled());
            agVo.setHostPositive(a.hostsRule().positive());
            agVo.setHostEnforcing(a.hostsRule().enforcing());

            // 수정 필요
            if(a.hostLabels().isEmpty()){
                agVo.setHostLabels("레이블 없음");
            }
            if(a.vmLabels().isEmpty()){
                agVo.setVmLabels("레이블 없음");
            }

            // 가상머신 멤버 (수정 필요)
            List<Vm> vmList =
                    ((AffinityGroupVmsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().groupService(a.id()).vmsService().list().send()).vms();
            String vmNames = "";

            for(Vm vm : vmList){
                vmNames += vm.name() + " ";
            }
            agVo.setVmList(vmNames);

            // 호스트 멤버 (수정 필요)
            List<Host> hostList =
                    ((AffinityGroupHostsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().groupService(a.id()).hostsService().list().send()).hosts();
            String hostNames = "";

            for(Host host : hostList){
                hostNames += host.name() + " ";
            }
            agVo.setHostList(hostNames);

            agVoList.add(agVo);
        }
        return agVoList;
    }


    @Override
    public List<AffinityLabelVo> getAffinitylabels(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<AffinityLabelVo> alVoList = new ArrayList<>();
        AffinityLabelVo alVo = null;

        List<AffinityLabel> affinityGroupList =
                ((AffinityLabelsService.ListResponse)systemService.clustersService().clusterService(id).get().send()).labels();

        for(AffinityLabel affinityLabel : affinityGroupList){
            if(affinityLabel.hasImplicitAffinityGroup()){
                alVo = new AffinityLabelVo();
                alVo.setName(affinityLabel.name());
//                alVo.setHostsLabel();
            }
            alVoList.add(alVo);
        }
        return alVoList;
    }


    public List<CpuProfileVo> getCpuProfile(String id){
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<CpuProfileVo> cpVoList = new ArrayList<>();
        CpuProfileVo cpVo = null;

        List<CpuProfile> cpuProfileList =
                ((AssignedCpuProfilesService.ListResponse)systemService.clustersService().clusterService(id).cpuProfilesService().list().send()).profiles();

        for(CpuProfile cpuProfile : cpuProfileList){
            cpVo = new CpuProfileVo();
            cpVo.setName(cpuProfile.name());
            cpVo.setDescription(cpuProfile.description());

            // 수정 필요
//            cpuProfileVO.setQosName(cpuProfile.qos() !=null ? cpuProfile.qos().name() : "");

            cpVoList.add(cpVo);
        }
        return cpVoList;
    }

//    public ClusterVO getPermission(String id){
//        return null;
//    }
//
//    public ClusterVO getEvent(String id){
//        return null;
//    }




    public int getHostVmsCnt(SystemService systemService, String hostId){
        List<Vm> vmList =
                ((VmsService.ListResponse) systemService.vmsService().list().send()).vms();

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
        List<Host> hostList =
                ((HostsService.ListResponse) systemService.hostsService().list().send()).hosts();

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
        List<Vm> vmList =
                ((VmsService.ListResponse) systemService.vmsService().list().send()).vms();

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