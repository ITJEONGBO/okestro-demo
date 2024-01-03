package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.StorageDomainVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItDataCenterService;
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
public class DataCenterServiceImpl implements ItDataCenterService {

    @Autowired
    private AdminConnectionService adminConnectionService;


    @Override
    public List<DataCenterVo> getDatacenters(){
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<DataCenterVo> dcVoList = new ArrayList<>();
        DataCenterVo dcVo = null;

        List<DataCenter> dataCenterList =
                ((DataCentersService.ListResponse)systemService.dataCentersService().list().send()).dataCenters();

        for(DataCenter dataCenter : dataCenterList){
            dcVo = new DataCenterVo();

            dcVo.setId(dataCenter.id());
            dcVo.setName(dataCenter.name());
            dcVo.setComment(dataCenter.comment());
            dcVo.setStorageType(dataCenter.local());
            dcVo.setStatus(dataCenter.status().value());
            dcVo.setVersion(dataCenter.version().major() + "." + dataCenter.version().minor());
            dcVo.setDescription(dataCenter.description());

            dcVoList.add(dcVo);
        }
        return dcVoList;
    }

    @Override
    public List<StorageDomainVo> getStorage(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<StorageDomainVo> sdVoList = new ArrayList<>();
        StorageDomainVo sdVo = null;

        List<StorageDomain> storageDomainList =
                ((AttachedStorageDomainsService.ListResponse)systemService.dataCentersService().dataCenterService(id).storageDomainsService().list().send()).storageDomains();

        for(StorageDomain storageDomain : storageDomainList) {
            sdVo = new StorageDomainVo();

            sdVo.setId(storageDomain.id());
            sdVo.setName(storageDomain.name());
            sdVo.setDomainType(storageDomain.type().value() + (storageDomain.master() ? "(master)" : ""));
            sdVo.setStatus(storageDomain.status().value());     // storageDomainStatus 10개
            sdVo.setAvailableSize(storageDomain.available()); // 여유공간
            sdVo.setUsedSize(storageDomain.used()); // 사용된 공간
            sdVo.setDiskSize(storageDomain.available().add(storageDomain.used()));
            sdVo.setDescription(storageDomain.description());
            sdVo.setDatacenterName( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(id).get().send()).dataCenter().name() );

            System.out.println(sdVo.getDatacenterName());
            System.out.println(sdVo.getDatacenterName().getClass().getName());
            sdVoList.add(sdVo);
        }
        return sdVoList;
    }


    @Override
    public List<NetworkVo> getNetwork(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<NetworkVo> nwVoList = new ArrayList<>();
        NetworkVo nwVo = null;

        List<Network> networkList =
                ((DataCenterNetworksService.ListResponse)systemService.dataCentersService().dataCenterService(id).networksService().list().send()).networks();

        for(Network network : networkList){
            nwVo = new NetworkVo();

            nwVo.setId(network.id());
            nwVo.setName(network.name());
            nwVo.setDescription(network.description());
            nwVo.setDatacenterName( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(id).get().send()).dataCenter().name() );

            nwVoList.add(nwVo);
        }
        return nwVoList;
    }


    @Override
    public List<ClusterVo> getCluster(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<ClusterVo> cVoList = new ArrayList<>();
        ClusterVo cVo = null;

        List<Cluster> clusterList =
                ((ClustersService.ListResponse) systemService.dataCentersService().dataCenterService(id).clustersService().list().send()).clusters();

        for(Cluster cluster : clusterList){
            cVo = new ClusterVo();

            cVo.setId(cluster.id());
            cVo.setName(cluster.name());
            cVo.setVersion(cluster.version().major() + "." + cluster.version().minor());
            cVo.setDescription(cluster.description());
            cVo.setDatacenterName( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(id).get().send()).dataCenter().name()  );

            cVoList.add(cVo);
        }
        return cVoList;
    }

    @Override
    public List<PermissionVo> getPermission(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList =
                ((AssignedPermissionsService.ListResponse)systemService.dataCentersService().dataCenterService(id).permissionsService().list().send()).permissions();

        for(Permission permission : permissionList){
            System.out.println(permission.id());
            Group group =
                    ((GroupService.GetResponse)systemService.groupsService().groupService(permission.group().id()).get().send()).get();

            System.out.println("groupName: "+group.name());

            pVo = new PermissionVo();

            pVo.setPermissionId(permission.id());
            pVo.setGroupId(permission.group().id());

            pVo.setRoleId(permission.role().id());


            pVoList.add(pVo);
        }

        return pVoList;
    }


}
