package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.ovirt.ConnectionService;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
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

        List<DataCenter> dataCenterList = ((DataCentersService.ListResponse)systemService.dataCentersService().list().send()).dataCenters();
        List<Cluster> clusterList = ((ClustersService.ListResponse) systemService.clustersService().list().send()).clusters();
        List<Host> hostList = ((HostsService.ListResponse)systemService.hostsService().list().send()).hosts();
        List<Vm> vmList = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();
        List<Template> templateList = ((TemplatesService.ListResponse)systemService.templatesService().list().send()).templates();

        List<VnicProfile> vnicProfileList = ((VnicProfilesService.ListResponse)systemService.vnicProfilesService().list().send()).profiles();
        List<Network> networkList = ((NetworksService.ListResponse)systemService.networksService().list().send()).networks();

        List<StorageDomain> storageDomainList = ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().send()).storageDomains();
        List<Disk> diskList = ((DisksService.ListResponse)systemService.disksService().list().send()).disks();


        List<DataCenterVo> dcVoList = new ArrayList<>();
        for(DataCenter dataCenter : dataCenterList){
            DataCenterVo dcVo = new DataCenterVo();
            dcVo.setId(dataCenter.id());
            dcVo.setName(dataCenter.name());
            dcVoList.add(dcVo);
        }
        m.setDatacenter(dcVoList);


        List<ClusterVo> cVoList = new ArrayList<>();
        for(Cluster cluster : clusterList) {
            ClusterVo cVo = new ClusterVo();
            cVo.setId(cluster.id());
            cVo.setName(cluster.name());
            cVo.setDatacenterId(((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send()).dataCenter().id());
            cVoList.add(cVo);
        }
        m.setCluster(cVoList);

        List<HostVo> hVoList = new ArrayList<>();
        for(Host host : hostList) {
            HostVo hVo = new HostVo();
            hVo.setId(host.id());
            hVo.setName(host.name());
            hVo.setClusterId(host.clusterPresent() ? host.cluster().id() : null);
            hVoList.add(hVo);
        }
        m.setHost(hVoList);

        List<VmVo> vmVoList = new ArrayList<>();
        for(Vm vm : vmList) {
            VmVo vmVo = new VmVo();
            vmVo.setId(vm.id());
            vmVo.setName(vm.name());
            vmVo.setHostId(vm.hostPresent() ? vm.host().id() : null);
            vmVo.setStatus(vm.status().value());
            vmVoList.add(vmVo);
        }
        m.setVm(vmVoList);

        List<TemplateVo> tVoList = new ArrayList<>();
        for(Template template : templateList) {
            TemplateVo tVo = new TemplateVo();
            tVo.setId(template.id());
            tVo.setName(template.name());
            tVoList.add(tVo);
        }
        m.setTm(tVoList);

        List<VnicProfileVo> vpVoList = new ArrayList<>();
        for(VnicProfile vnicProfile : vnicProfileList) {
            VnicProfileVo vpVo = new VnicProfileVo();
            vpVo.setId(vnicProfile.id());
            vpVo.setName(vnicProfile.name());
            vpVoList.add(vpVo);
        }
        m.setVnic(vpVoList);

        List<NetworkVo> nwVoList = new ArrayList<>();
        for(Network network : networkList) {
            NetworkVo nwVo = new NetworkVo();
            nwVo.setId(network.id());
            nwVo.setName(network.name());
            nwVoList.add(nwVo);
        }
        m.setNetwork(nwVoList);

        return m;
    }
}
