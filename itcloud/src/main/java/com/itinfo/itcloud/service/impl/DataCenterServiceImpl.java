package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.ClusterVO;
import com.itinfo.itcloud.model.computing.DataCenterVO;
import com.itinfo.itcloud.model.network.NetworkVO;
import com.itinfo.itcloud.model.storage.DomainVO;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItDataCenterService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.Cluster;
import org.ovirt.engine.sdk4.types.DataCenter;
import org.ovirt.engine.sdk4.types.Network;
import org.ovirt.engine.sdk4.types.StorageDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DataCenterServiceImpl implements ItDataCenterService {

    @Autowired
    private AdminConnectionService adminConnectionService;

    public DataCenterServiceImpl() { }

    public List<DataCenterVO> getDatacenters(){
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<DataCenterVO> dataCenterVOList = new ArrayList<>();
        DataCenterVO dataCenterVO = null;

        List<DataCenter> dataCenterList =
                ((DataCentersService.ListResponse)systemService.dataCentersService().list().send()).dataCenters();

        for(DataCenter dataCenter : dataCenterList){
            dataCenterVO = new DataCenterVO();

            dataCenterVO.setId(dataCenter.id());
            dataCenterVO.setName(dataCenter.name());
            dataCenterVO.setComment(dataCenter.comment());
            dataCenterVO.setStorageType(dataCenter.local());
            dataCenterVO.setStatus(dataCenter.status().value());
            dataCenterVO.setVersion(dataCenter.version().major() + "." + dataCenter.version().minor());
            dataCenterVO.setDescription(dataCenter.description());

            dataCenterVOList.add(dataCenterVO);
        }

        return dataCenterVOList;
    }

    @Override
    public DataCenterVO getStorage(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        DataCenterVO dataCenterVO = new DataCenterVO();
        dataCenterVO.setId(id);

        List<DomainVO> domainVOList = new ArrayList<>();
        DomainVO domainVO = null;

        List<StorageDomain> storageDomainList =
                ((AttachedStorageDomainsService.ListResponse)systemService.dataCentersService().dataCenterService(id).storageDomainsService().list().send()).storageDomains();

        for(StorageDomain storageDomain : storageDomainList) {
            domainVO = new DomainVO();

            domainVO.setId(storageDomain.id());
            domainVO.setName(storageDomain.name());
            domainVO.setDomainType(storageDomain.type().value() + (storageDomain.master() ? "(마스터)":""));
            domainVO.setStatus(storageDomain.status().value());
            domainVO.setAvailableDiskSize(storageDomain.available()); // 여유공간
            domainVO.setUsedDiskSize(storageDomain.used()); // 사용된 공간
            domainVO.setDiskSize(storageDomain.available().add(storageDomain.used()));
            domainVO.setDescription(storageDomain.description());

            domainVOList.add(domainVO);
        }
        dataCenterVO.setDomainVOList(domainVOList);

        return dataCenterVO;
    }



    @Override
    public DataCenterVO getNetwork(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        DataCenterVO dataCenterVO = new DataCenterVO();
        dataCenterVO.setId(id);
        List<NetworkVO> networkVOList = new ArrayList<>();
        NetworkVO networkVO = null;

        List<Network> networkList =
                ((DataCenterNetworksService.ListResponse)systemService.dataCentersService().dataCenterService(id).networksService().list().send()).networks();

        for(Network network : networkList){
            networkVO = new NetworkVO();

            networkVO.setId(network.id());
            networkVO.setName(network.name());
            networkVO.setDescription(network.description());

            networkVOList.add(networkVO);
        }
        dataCenterVO.setNetworkVOList(networkVOList);

        return dataCenterVO;
    }

    @Override
    public DataCenterVO getCluster(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        DataCenterVO dataCenterVO = new DataCenterVO();
        dataCenterVO.setId(id);

        List<ClusterVO> clusterVOList = new ArrayList<>();
        ClusterVO clusterVO = null;

        List<Cluster> clusterList =
                ((ClustersService.ListResponse) systemService.dataCentersService().dataCenterService(id).clustersService().list().send()).clusters();

        for(Cluster cluster : clusterList){
            clusterVO = new ClusterVO();

            clusterVO.setId(cluster.id());
            clusterVO.setName(cluster.name());
            clusterVO.setVersion(cluster.version().major() + "." + cluster.version().minor());
            clusterVO.setDescription(cluster.description());

            clusterVOList.add(clusterVO);
        }
        dataCenterVO.setClusterVOList(clusterVOList);

        return dataCenterVO;
    }


}
