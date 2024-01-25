package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.ovirt.OvirtService;
import com.itinfo.itcloud.service.ItDataCenterService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DataCenterServiceImpl implements ItDataCenterService {
    @Autowired private AdminConnectionService admin;
    @Autowired private OvirtService ovirt;

    @Override
    public String getName(String id){
        return ovirt.getName("datacenter", id);
    }

    // 데이터센터 리스트 불러오기
    @Override
    public List<DataCenterVo> getList(){
        SystemService systemService = admin.getConnection().systemService();

        long start = System.currentTimeMillis();

        List<DataCenterVo> dcVoList = new ArrayList<>();
        DataCenterVo dcVo = null;

        List<DataCenter> dataCenterList = ((DataCentersService.ListResponse)systemService.dataCentersService().list().send()).dataCenters();
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
        long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        System.out.println("datacenter getList 수행시간(ms): " + (end-start));

        return dcVoList;
    }

    // 데이터센터 - 스토리지
    @Override
    public List<DomainVo> getStorage(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<DomainVo> sdVoList = new ArrayList<>();
        DomainVo sdVo = null;

        List<StorageDomain> storageDomainList = ((AttachedStorageDomainsService.ListResponse) systemService.dataCentersService().dataCenterService(id).storageDomainsService().list().send()).storageDomains();
        for(StorageDomain storageDomain : storageDomainList) {
            sdVo = new DomainVo();

            sdVo.setId(storageDomain.id());
            sdVo.setName(storageDomain.name());
            sdVo.setDomainType( storageDomain.type().value() + (storageDomain.master() ? "(마스터)" : "") );
            sdVo.setStatus(storageDomain.status().value());     // storageDomainStatus 10개
            sdVo.setAvailableSize(storageDomain.available());   // 여유공간
            sdVo.setUsedSize(storageDomain.used());             // 사용된 공간
            sdVo.setDiskSize(storageDomain.available().add(storageDomain.used()));
            sdVo.setDescription(storageDomain.description());

            sdVoList.add(sdVo);
        }

        return sdVoList;
    }


    // 데이터센터 - 네트워크
    @Override
    public List<NetworkVo> getNetwork(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<NetworkVo> nwVoList = new ArrayList<>();
        NetworkVo nwVo = null;

        List<Network> networkList = ((DataCenterNetworksService.ListResponse) systemService.dataCentersService().dataCenterService(id).networksService().list().send()).networks();
        for(Network network : networkList){
            nwVo = new NetworkVo();

            nwVo.setId(network.id());
            nwVo.setName(network.name());
            nwVo.setDescription(network.description());

            nwVoList.add(nwVo);
        }
        return nwVoList;
    }


    // 데이터센터 - 클러스터
    @Override
    public List<ClusterVo> getCluster(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<ClusterVo> cVoList = new ArrayList<>();
        ClusterVo cVo = null;

        List<Cluster> clusterList = ((ClustersService.ListResponse) systemService.dataCentersService().dataCenterService(id).clustersService().list().send()).clusters();
        for(Cluster cluster : clusterList){
            cVo = new ClusterVo();

            cVo.setId(cluster.id());
            cVo.setName(cluster.name());
            cVo.setVersion(cluster.version().major() + "." + cluster.version().minor());
            cVo.setDescription(cluster.description());

            cVoList.add(cVo);
        }
        return cVoList;
    }

    // 데이터센터 - 권한
    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList =
                ((AssignedPermissionsService.ListResponse) systemService.dataCentersService().dataCenterService(id).permissionsService().list().send()).permissions();
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

    // 데이터센터 - 이벤트
    @Override
    public List<EventVo> getEvent(String id) {
        SystemService systemService = admin.getConnection().systemService();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;

        List<Event> eventList = ((EventsService.ListResponse)systemService.eventsService().list().send()).events();
        DataCenter dc = ovirt.dataCenter(id);

        for(Event event : eventList){
            // <data_center href="" id=""> <name> </data_center>
            // <data_center> <name> </data_center>
            if( event.dataCenterPresent() && event.dataCenter().name().equals(dc.name()) ){
                eVo = new EventVo();
                eVo.setDatacenterName(dc.name());

                // 상태[LogSeverity] : alert, error, normal, warning
                eVo.setSeverity(event.severity().value());
                eVo.setTime(sdf.format(event.time()));
                eVo.setMessage(event.description());
                eVo.setRelationId(event.correlationIdPresent() ? event.correlationId() : null);
                eVo.setSource(event.origin());

                eVoList.add(eVo);
            }
        }
        return eVoList;
    }


}
