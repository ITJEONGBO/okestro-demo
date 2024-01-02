package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItNetworkService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.Cluster;
import org.ovirt.engine.sdk4.types.Network;
import org.ovirt.engine.sdk4.types.NetworkUsage;
import org.ovirt.engine.sdk4.types.VnicProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NetworkServiceImpl implements ItNetworkService {

    @Autowired
    private AdminConnectionService adminConnectionService;

    @Override
    public List<NetworkVo> getList() {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<NetworkVo> nwVoList = new ArrayList<>();
        NetworkVo nwVo = null;

        List<Network> networkList =
                ((NetworksService.ListResponse)systemService.networksService().list().send()).networks();

        for(Network network : networkList){
            nwVo = new NetworkVo();

            nwVo.setId(network.id());
            nwVo.setName(network.name());
            nwVo.setDescription(network.description());
            nwVo.setComment(network.comment());
            nwVo.setMtu(network.mtuAsInteger());
            nwVo.setVdsmName(network.vdsmName());
            nwVo.setDatacenterId(network.dataCenter().id());
            nwVo.setDatacenterName( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(network.dataCenter().id()).get().send()).dataCenter().name() );

            nwVo.setVm(network.usages().contains(NetworkUsage.VM));
            nwVo.setDisplay(network.usages().contains(NetworkUsage.DISPLAY));
            nwVo.setMigration(network.usages().contains(NetworkUsage.MIGRATION));
            nwVo.setManagement(network.usages().contains(NetworkUsage.MANAGEMENT));
            nwVo.setDefaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE));
            nwVo.setGluster(network.usages().contains(NetworkUsage.GLUSTER));

            nwVoList.add(nwVo);
        }
        return nwVoList;
    }

    @Override
    public NetworkVo getNetwork(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        NetworkVo nwVo = new NetworkVo();

        Network network =
                ((NetworkService.GetResponse)systemService.networksService().networkService(id).get().send()).network();

        nwVo.setId(network.id());
        nwVo.setName(network.name());
        nwVo.setDescription(network.description());
        nwVo.setVdsmName(network.vdsmName());

        // vlan 여부 확인 후 체크
        if(network.vlanPresent()){
            nwVo.setVlan(network.vlan().id());
        }else{
            nwVo.setVlan(null);
        }

        nwVo.setMtu(network.mtuAsInteger());

        return nwVo;
    }

    @Override
    public List<VnicProfileVo> getVnic(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<VnicProfileVo> vpVoList = new ArrayList<>();
        VnicProfileVo vpVo = null;

        List<VnicProfile> vnicProfileList =
                ((AssignedVnicProfilesService.ListResponse)systemService.networksService().networkService(id).vnicProfilesService().list().send()).profiles();

        for(VnicProfile vnicProfile : vnicProfileList){
            if(id.equals(vnicProfile.network().id())){
                Network network =
                        ((NetworkService.GetResponse)systemService.networksService().networkService(id).get().send()).network();

                vpVo = new VnicProfileVo();

                vpVo.setId(vnicProfile.id());
                vpVo.setName(vnicProfile.name());
                vpVo.setDescription(vnicProfile.description());
                vpVo.setNetworkName( ((NetworkService.GetResponse)systemService.networksService().networkService(id).get().send()).network().name() );
                vpVo.setDatacenterName( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(network.dataCenter().id()).get().send()).dataCenter().name() );
                vpVo.setPassThrough(vnicProfile.passThrough().mode().value());
                vpVo.setPortMirroring(vnicProfile.portMirroring());
                vpVo.setNetworkFilterName( ((NetworkFilterService.GetResponse)systemService.networkFiltersService().networkFilterService(vnicProfile.networkFilter().id()).get().send()).networkFilter().name() );

                vpVoList.add(vpVo);
            }
        }
        return vpVoList;
    }

    @Override
    public List<ClusterVo> getCluster(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<ClusterVo> cVoList = new ArrayList<>();
        ClusterVo cVo = null;

        // 클러스터리스트 출력
        List<Cluster> clusterList =
                ((ClustersService.ListResponse)systemService.clustersService().list().send()).clusters();

        for(Cluster cluster : clusterList){
            // 클러스터 아이디를 넣고 네트워크 검색
            List<Network> networkList =
                    ((ClusterNetworksService.ListResponse)systemService.clustersService().clusterService(cluster.id()).networksService().list().send()).networks();

            for(Network network : networkList){
                if(cluster.id().equals(network.cluster().id())){
                    cVo = new ClusterVo();
                    cVo.setName(cluster.name());
                    cVo.setDescription(cluster.description());

                    cVoList.add(cVo);
                }
            }
        }
        return cVoList;
    }

    @Override
    public List<HostVo> getHost(String id) {
        return null;
    }

    @Override
    public List<VmVo> getVm(String id) {
        return null;
    }

    @Override
    public List<TemplateVo> getTemplate(String id) {
        return null;
    }


}
