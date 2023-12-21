package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.NetworkVO;
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

    @Autowired
    private AdminConnectionService adminConnectionService;

    public ClusterServiceImpl() {
    }


    // json 보여줄 용도 / 전체 클러스터 출력용
    @Override
    public List<ClusterVO> getClusters() {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<Cluster> clusterList =
                ((ClustersService.ListResponse) systemService.clustersService().list().send()).clusters();

        List<ClusterVO> clusterVOList = new ArrayList<>();
        ClusterVO clusterVO = null;

        for (Cluster cluster : clusterList) {
            clusterVO = new ClusterVO();

            clusterVO.setId(cluster.id());
            clusterVO.setName(cluster.name());
            clusterVO.setComment(cluster.comment());
            clusterVO.setDescription(cluster.description());
            clusterVO.setVersion(cluster.version().major() + "." + cluster.version().minor());

            // 데이터센터 이름
            if(cluster.dataCenter() != null){
                DataCenter dataCenter =
                        ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send()).dataCenter();
                clusterVO.setDataCenter(dataCenter.name());
            }


            if (cluster.cpu() != null) {
                clusterVO.setCpuType(cluster.cpu().type());
            }

            clusterVO.setThreadsAsCPU(cluster.threadsAsCores());
            clusterVO.setMemoryOverCommit(cluster.memoryPolicy().overCommit().percentAsInteger());

            // 복구정책
//            clusterVO.setRestoration(cluster.)
//            clusterVO.setChipsetFirmwareType(cluster.biosType().value()); //없으면 문제됨

            getHostCnt(systemService, clusterVO);
            getVmCnt(systemService, clusterVO);

            clusterVOList.add(clusterVO);
        }

        return clusterVOList;
    }



    /*
     * 여기서 시작쓰
     */


    // 컴퓨팅-클러스터 목록
    // 상태, 이름, 코멘트, 호환버전, 설명, 클러스터cpu유형, 호스트수, 가상머신수, (업그레이드 상태)
    public List<ClusterVO> getList(){
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<ClusterVO> clusterVOList = new ArrayList<>();
        ClusterVO clusterVO = null;

        List<Cluster> clusterList =
                ((ClustersService.ListResponse) systemService.clustersService().list().send()).clusters();

        for(Cluster cluster : clusterList){
            clusterVO = new ClusterVO();

            clusterVO.setId(cluster.id());
            clusterVO.setName(cluster.name());
            clusterVO.setComment(cluster.comment());
            clusterVO.setVersion(cluster.version().major() + "." + cluster.version().minor());
            clusterVO.setDescription(cluster.description());

            if(cluster.cpu() != null) {
                clusterVO.setCpuType(cluster.cpu().type());
            }
//            clusterVO.setStatus(cluster.);        // 업그레이드 상태

            getHostCnt(systemService, clusterVO);
            getVmCnt(systemService, clusterVO);

            clusterVOList.add(clusterVO);
        }
        return clusterVOList;
    }



    @Override
    public ClusterVO getInfo(String id){
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        Cluster cluster = ((ClusterService.GetResponse) systemService.clustersService().clusterService(id).get().send()).cluster();
        ClusterVO clusterVO = new ClusterVO();
        clusterVO.setId(id);

        if (cluster.id().equals(id)) {
            clusterVO.setName(cluster.name());
            clusterVO.setDescription(cluster.description());
            clusterVO.setVersion(cluster.version().major() + "." + cluster.version().minor());

            // 데이터센터 이름
            if(cluster.dataCenter() != null){
                DataCenter dataCenter =
                        ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send()).dataCenter();
                clusterVO.setDataCenter(dataCenter.name());
            }

            if (cluster.cpu() != null) {
                clusterVO.setCpuType(cluster.cpu().type());
            }

            if(cluster.biosType() != null){
                clusterVO.setChipsetFirmwareType(cluster.biosType().value());
            }

            clusterVO.setThreadsAsCPU(cluster.threadsAsCores());
            clusterVO.setMemoryOverCommit(cluster.memoryPolicy().overCommit().percentAsInteger());
//            clusterVO.setRestoration(cluster.)    // 복구정책

            getVmCnt(systemService, clusterVO);
        }
        return clusterVO;
    }

    @Override
    public ClusterVO getNetwork(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        ClusterVO clusterVO = new ClusterVO();
        clusterVO.setId(id);

        List<NetworkVO> clusterNetworkVOList = new ArrayList<>();
        NetworkVO networkVO = null;

        List<Network> networkList =
                ((ClusterNetworksService.ListResponse) systemService.clustersService().clusterService(id).networksService().list().send()).networks();

        if (networkList != null) {
            for (Network network : networkList) {
                networkVO = new NetworkVO();

                networkVO.setId(network.id());
                networkVO.setName(network.name());
                networkVO.setStatus(network.status().value());
                networkVO.setDescription(network.description());

                // 역할 일단 대충나오게만 하기 이후 변경 필요
//                System.out.println("------"+network.usages().stream().count());
                for(int i=0; i < (int) network.usages().stream().count(); i++){
                    if(network.usages().get(i).value().equals("vm")) {
                        networkVO.setVm(true);
                        break;
                    }
                }
                for(int i=0; i < (int) network.usages().stream().count(); i++){
                    if(network.usages().get(i).value().equals("display")) {
                        networkVO.setDisplay(true);
                        break;
                    }
                }
                for(int i=0; i < (int) network.usages().stream().count(); i++){
                    if(network.usages().get(i).value().equals("migration")) {
                        networkVO.setMigration(true);
                        break;
                    }
                }
                for(int i=0; i < (int) network.usages().stream().count(); i++){
                    if(network.usages().get(i).value().equals("management")) {
                        networkVO.setManagement(true);
                        break;
                    }
                }
                for(int i=0; i < (int) network.usages().stream().count(); i++){
                    if(network.usages().get(i).value().equals("default_route")) {
                        networkVO.setDefaultRoute(true);
                        break;
                    }
                }
                for(int i=0; i < (int) network.usages().stream().count(); i++){
                    if(network.usages().get(i).value().equals("gluster")) {
                        networkVO.setGluster(true);
                        break;
                    }
                }
//                networkVO.setVm(network.usages().get(i).value().equals("vm"));
//                networkVO.setDisplay(network.usages().get(i).value().equals("display"));
//                networkVO.setMigration(network.usages().get(i).value().equals("migration"));
//                networkVO.setManagement(network.usages().get(i).value().equals("management"));
//                networkVO.setDefaultRoute(network.usages().get(i).value().equals("default_route"));
//                networkVO.setGluster(network.usages().get(i).value().equals("gluster"));

                clusterNetworkVOList.add(networkVO);
            }
        }
        clusterVO.setNetworkVOList(clusterNetworkVOList);

        return clusterVO;
    }

    @Override
    public ClusterVO getHost(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        ClusterVO clusterVO = new ClusterVO();
        clusterVO.setId(id);

        List<HostVO> hostVOList = new ArrayList<>();
        HostVO hostVO = null;

        List<Host> hostList =
                ((HostsService.ListResponse) systemService.hostsService().list().send()).hosts();

        for (Host host : hostList) {
            if (host.cluster().id().equals(id)) {
                hostVO = new HostVO();

                hostVO.setId(host.id());
                hostVO.setName(host.name());
                hostVO.setAddress(host.address());
                hostVO.setStatus(host.status().value());
                hostVO.setVmUpCnt(getHostVmsCnt(systemService, host.id()));
                hostVOList.add(hostVO);
            }
        }
        clusterVO.setHostVOList(hostVOList);

        return clusterVO;
    }


    @Override
    public ClusterVO getVm(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        ClusterVO clusterVO = new ClusterVO();
        clusterVO.setId(id);

        List<VmVO> vmVOList = new ArrayList<>();
        VmVO vmVO = null;

        List<Vm> vmList =
                ((VmsService.ListResponse) systemService.vmsService().list().send()).vms();

        for (Vm vm : vmList) {
            if (vm.cluster().id().equals(id)) {
                vmVO = new VmVO();

                vmVO.setId(vm.id());
                vmVO.setName(vm.name());
                vmVO.setStatus(vm.status().value());

                // uptime
                if(vm.startTime() != null) {
                    vmVO.setStartTime(vm.startTime().toString());
                }

                if(!vm.status().value().equals("down")){
                    // ipv4 부분. vms-nic-reporteddevice
                    List<Nic> nicList =
                            ((VmNicsService.ListResponse) systemService.vmsService().vmService(vm.id()).nicsService().list().send()).nics();

                    for (Nic nic : nicList){
                        List<ReportedDevice> reportedDeviceList
                                = ((VmReportedDevicesService.ListResponse)systemService.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).reportedDevicesService().list().send()).reportedDevice();
                        for (ReportedDevice r : reportedDeviceList){
                            vmVO.setIpv4(r.ips().get(0).address());
                            vmVO.setIpv6(r.ips().get(1).address());
                        }
                    }
                }else{
                    vmVO.setIpv4("");
                    vmVO.setIpv6("");
                }
                vmVOList.add(vmVO);
            }
        }

        clusterVO.setVmVOList(vmVOList);
        return clusterVO;
    }

    public ClusterVO getAffinitygroups(String id){
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        ClusterVO clusterVO = new ClusterVO();
        clusterVO.setId(id);

        List<AffinityGroupVO> affinityGroupVOList = new ArrayList<>();
        AffinityGroupVO affinityGroupVO = null;

        List<AffinityGroup> affinityGroupList =
                ((AffinityGroupsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().list().send()).groups();

        for(AffinityGroup a : affinityGroupList){
            affinityGroupVO = new AffinityGroupVO();
            affinityGroupVO.setName(a.name());
            affinityGroupVO.setDescription(a.description());
//            affinityGroupVO.setStatus(a.); //상태 수정 필요
            affinityGroupVO.setPriority(a.priority().intValue());

            affinityGroupVO.setVmEnabled(a.vmsRule().enabled());
            affinityGroupVO.setVmPositive(a.vmsRule().positive());
            affinityGroupVO.setVmEnforcing(a.vmsRule().enforcing());

            // 수정 필요
//            if(a.positive() != null){
//                affinityGroupVO.setVmPositive(a.positive());    // 애매 a.positive()와 a.vmsRule().positive()
//            }
//            affinityGroupVO.setVmEnforcing(a.enforcing());  // 얘도 마찬가지

            affinityGroupVO.setHostEnabled(a.hostsRule().enabled());
            affinityGroupVO.setHostPositive(a.hostsRule().positive());
            affinityGroupVO.setHostEnforcing(a.hostsRule().enforcing());

            // 수정 필요
            if(a.hostLabels().isEmpty()){
                affinityGroupVO.setHostLabels("레이블 없음");
            }
            if(a.vmLabels().isEmpty()){
                affinityGroupVO.setVmLabels("레이블 없음");
            }

            // 가상머신 멤버 (수정 필요)
            List<Vm> vmList =
                    ((AffinityGroupVmsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().groupService(a.id()).vmsService().list().send()).vms();
            String vmNames = "";

            for(Vm vm : vmList){
                vmNames += vm.name() + " ";
            }
            affinityGroupVO.setVmList(vmNames);

            // 호스트 멤버 (수정 필요)
            List<Host> hostList =
                    ((AffinityGroupHostsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().groupService(a.id()).hostsService().list().send()).hosts();
            String hostNames = "";

            for(Host host : hostList){
                hostNames += host.name() + " ";
            }
            affinityGroupVO.setHostList(hostNames);

            affinityGroupVOList.add(affinityGroupVO);
        }
        clusterVO.setAffinityGroupVOList(affinityGroupVOList);
        return clusterVO;
    }

    // 선호도 레이블 문제가 있음요


    public ClusterVO getCpuProfile(String id){
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        ClusterVO clusterVO = new ClusterVO();
        clusterVO.setId(id);

        List<CpuProfileVO> cpuProfileVOList = new ArrayList<>();
        CpuProfileVO cpuProfileVO = null;

        List<CpuProfile> cpuProfileList =
                ((AssignedCpuProfilesService.ListResponse)systemService.clustersService().clusterService(id).cpuProfilesService().list().send()).profiles();

        for(CpuProfile cpuProfile : cpuProfileList){
            cpuProfileVO = new CpuProfileVO();

            cpuProfileVO.setName(cpuProfile.name());
            cpuProfileVO.setDescription(cpuProfile.description());

            // 수정 필요
//            cpuProfileVO.setQosName(cpuProfile.qos() !=null ? cpuProfile.qos().name() : "");

            cpuProfileVOList.add(cpuProfileVO);
        }
        clusterVO.setCpuProfileVOList(cpuProfileVOList);
        return clusterVO;
    }

    public ClusterVO getPermission(String id){
        return null;
    }

    public ClusterVO getEvent(String id){
        return null;
    }



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


    public void getHostCnt(SystemService systemService, ClusterVO clusterVO) {
        List<Host> hostList =
                ((HostsService.ListResponse) systemService.hostsService().list().send()).hosts();

        int hostCnt = 0;
        int hostUpCnt = 0;

        for (Host host : hostList) {
            if (host.cluster().id().equals(clusterVO.getId())) {
                hostUpCnt = (int) hostList.stream()
                        .filter(hostcnt -> hostcnt.status().value().equals("up"))
                        .count();
            }else{
                hostCnt =0;
                break;
            }
            hostCnt++;
        }
        clusterVO.setHostCnt(hostCnt);
        clusterVO.setHostUpCnt(hostUpCnt);
        clusterVO.setHostDownCnt(hostCnt - hostUpCnt);
    }


    public void getVmCnt(SystemService systemService, ClusterVO clusterVO){
        List<Vm> vmList =
                ((VmsService.ListResponse) systemService.vmsService().list().send()).vms();

        int vmsCnt = 0;
        int vmsUpCnt = 0;

        for (Vm vm : vmList) {
            if (vm.cluster().id().equals(clusterVO.getId())) {

                vmsUpCnt = (int) vmList.stream()
                        .filter(vmcnt -> vmcnt.status().value().equals("up"))
                        .count();
            }else{
                vmsCnt = 0;
                break;
            }
            vmsCnt++;
        }
        clusterVO.setVmCnt(vmsCnt);
        clusterVO.setVmUpCnt(vmsUpCnt);
        clusterVO.setVmDownCnt(vmsCnt - vmsUpCnt);
    }

}