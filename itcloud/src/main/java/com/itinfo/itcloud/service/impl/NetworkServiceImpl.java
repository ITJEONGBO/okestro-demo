package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItNetworkService;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NetworkServiceImpl implements ItNetworkService {

    @Autowired
    private AdminConnectionService adminConnectionService;

    public NetworkServiceImpl(){}

    public SystemService getSystemSerivice(){
        Connection connection = this.adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();
        return systemService;
    }

    /*@Override
    public List<NetworkVO> getNetworkList() {
        return null;
    }*/

    /*public List<NetworkVO> getNetworkList() {
        Connection connection = this.adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();
        NetworksService networkService = systemService.networksService();

        List<Vm> vms = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();
        DataCenter dataCenter = (DataCenter)((DataCentersService.ListResponse)systemService.dataCentersService().list().send()).dataCenters().get(0);
        ClustersService clustersService = systemService.clustersService();

        List<Cluster> clusters = ((ClustersService.ListResponse)clustersService.list().send()).clusters();
        List<Network> networkList = ((NetworksService.ListResponse)networkService.list().send()).networks();
        List<OpenStackNetworkProvider> openStackNetworkProviders = ((OpenstackNetworkProvidersService.ListResponse)systemService.openstackNetworkProvidersService().list().send()).providers();
        List<Qos> qoss = ((QossService.ListResponse)systemService.dataCentersService().dataCenterService(dataCenter.id()).qossService().list().send()).qoss();

        List<NetworkVO> list = new ArrayList();

        networkList.forEach((network) -> {
            NetworkVO networkVO = new NetworkVO();
            networkVO.setComment(network.comment());
            networkVO.setNetworkId(network.id());
            networkVO.setNetworkName(network.name());
            networkVO.setDescription(network.description());
            if (network.mtuPresent()) {
                networkVO.setMtu(String.valueOf(network.mtuAsInteger()));
            }

            if (network.vlanPresent()) {
                networkVO.setVlanTag(String.valueOf(network.vlan().id().intValue()));
            }

            if (network.qosPresent()) {
                qoss.forEach((qos) -> {
                    if (qos.id().equalsIgnoreCase(network.qos().id())) {
                        networkVO.setQos(qos.name());
                        networkVO.setQosId(network.qos().id());
                    }

                });
            }

            if (network.externalProviderPresent()) {
                openStackNetworkProviders.forEach((networkProvider) -> {
                    if (networkProvider.id().equalsIgnoreCase(network.externalProvider().id())) {
                        networkVO.setProvider(networkProvider.name());
                    }

                });
            }

            if (network.usagesPresent()) {
                List<NetworkUsage> usages = network.usages();
                Iterator var9 = usages.iterator();

                while(var9.hasNext()) {
                    NetworkUsage usage = (NetworkUsage)var9.next();
                    if (usage.name().equalsIgnoreCase("VM")) {
                        networkVO.setUsage(usage.name());
                    }
                }
            }

            ((NetworkLabelsService.ListResponse)networkService.networkService(network.id()).networkLabelsService().list().send()).labels().forEach((label) -> {
                networkVO.setLabel(label.id());
            });
            clusters.forEach((cluster) -> {
                ClusterService clusterServices = clustersService.clusterService(cluster.id());
                ((ClusterNetworksService.ListResponse)clusterServices.networksService().list().send()).networks().forEach((clusterNetwork) -> {
                    if (clusterNetwork.id().equalsIgnoreCase(network.id())) {
                        networkVO.setCluster(cluster.id());
                    }

                });
            });
            list.add(networkVO);
        });
        return list;
    }*/

}
