package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.ovirt.ConnectionService;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItMenuService {
    @Autowired
    private ConnectionService ovirtConnection;


    public MenuVo getMenu() {
        Connection connection = ovirtConnection.getConnection();
        SystemService systemService = connection.systemService();

        MenuVo m = new MenuVo();

        List<DataCenter> dataCenterList = systemService.dataCentersService().list().send().dataCenters();
        List<Cluster> clusterList = systemService.clustersService().list().send().clusters();
        List<Host> hostList = systemService.hostsService().list().send().hosts();
        List<Vm> vmList = systemService.vmsService().list().send().vms();
        List<Template> templateList = systemService.templatesService().list().send().templates();

        List<VnicProfile> vnicProfileList = systemService.vnicProfilesService().list().send().profiles();
        List<Network> networkList = systemService.networksService().list().send().networks();

        List<StorageDomain> storageDomainList = systemService.storageDomainsService().list().send().storageDomains();
        List<Disk> diskList = systemService.disksService().list().send().disks();


        // region: computing
        List<DataCenterVo> dcVoList = new ArrayList<>();
        for(DataCenter dataCenter : dataCenterList){
            DataCenterVo dcVo = DataCenterVo.builder()
                    .id(dataCenter.id())
                    .name(dataCenter.name())
                    .build();
            dcVoList.add(dcVo);
        }
        m.setDatacenter(dcVoList);


        List<ClusterVo> cVoList = new ArrayList<>();
        for(Cluster cluster : clusterList) {
            // ¿ 데이터센터가 삭제 된 후 연결되어 있는 클러스터는 어떻게 처리를 해야하는가
            if(cluster.dataCenterPresent()) {
                ClusterVo cVo = ClusterVo.builder()
                        .id(cluster.id())
                        .name(cluster.name())
                        .datacenterId(cluster.dataCenter().id())
                        .build();
                cVoList.add(cVo);
            }else {
                ClusterVo cVo = ClusterVo.builder()
                        .id(cluster.id())
                        .name(cluster.name())
                        .datacenterId("")
                        .build();
                cVoList.add(cVo);
            }
        }
        m.setCluster(cVoList);

        List<HostVo> hVoList = new ArrayList<>();
        for(Host host : hostList) {
            HostVo hVo = HostVo.builder()
                    .id(host.id())
                    .name(host.name())
                    .clusterId(host.clusterPresent() ? host.cluster().id() : null)
                    .build();
            hVoList.add(hVo);
        }
        m.setHost(hVoList);

        List<VmVo> vmVoList = new ArrayList<>();
        String hostId = null;
        for(Vm vm : vmList) {
            if(vm.hostPresent()){
                hostId = systemService.hostsService().hostService(vm.host().id()).get().send().host().id();
            }else if(!vm.hostPresent() && vm.placementPolicy().hostsPresent()){
                hostId = systemService.hostsService().hostService(vm.placementPolicy().hosts().get(0).id()).get().send().host().id();
            }

            VmVo vmVo = VmVo.builder()
                    .status(vm.status().value())
                    .id(vm.id())
                    .name(vm.name())
                    .hostId(hostId)
                    .build();

            vmVoList.add(vmVo);
        }
        m.setVm(vmVoList);


        // endregion


        //region: network
//        List<VnicProfileVo> vpVoList = new ArrayList<>();
//        for(VnicProfile vnicProfile : vnicProfileList) {
//            VnicProfileVo vpVo = new VnicProfileVo();
//            vpVo.setId(vnicProfile.id());
//            vpVo.setName(vnicProfile.name());
//            vpVoList.add(vpVo);
//        }
//        m.setVnic(vpVoList);

        List<NetworkVo> nwVoList = new ArrayList<>();
        for(Network network : networkList) {
            NetworkVo nwVo = NetworkVo.builder()
                    .id(network.id())
                    .name(network.name())
                    .datacenterId(network.dataCenter().id())
                    .build();
            nwVoList.add(nwVo);
        }
        m.setNetwork(nwVoList);
        //endregion


        //region: storage
//        List<StorageDomainVo> sdVoList = new ArrayList<>();
//        for(StorageDomain storageDomain : storageDomainList) {
//            StorageDomainVo sdVo = new StorageDomainVo();
//            sdVo.setId(storageDomain.id());
//            sdVo.setName(storageDomain.name());
//
//            if(storageDomain.dataCentersPresent()) {
//                for (int i = 0; i < storageDomain.dataCenters().size(); i++) {
//                    DataCenter dc = ((DataCenterService.GetResponse) systemService.dataCentersService().dataCenterService(storageDomain.dataCenters().get(i).id()).get().send()).dataCenter();
//                    sdVo.setDatacenterId(dc.id());
//                    sdVo.setDatacenterName(dc.name());
//                }
//            }
//            sdVoList.add(sdVo);
//        }
//        m.setDomain(sdVoList);

        // 볼륨 미완

        List<DiskVo> dVoList = new ArrayList<>();
        for(Disk disk : diskList) {
            DiskVo dVo = new DiskVo();
            dVo.setId(disk.id());
            dVo.setName(disk.name());
            dVoList.add(dVo);
        }
        m.setDisk(dVoList);
        //endregion



        //region: setting
        //endregion



        return m;

    }

}
