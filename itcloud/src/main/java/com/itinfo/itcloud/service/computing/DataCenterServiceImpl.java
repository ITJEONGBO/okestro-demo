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
import org.ovirt.engine.sdk4.builders.DataCenterBuilder;
import org.ovirt.engine.sdk4.builders.VersionBuilder;
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

    // 데이터센터 - edit 시 필요한 값
    @Override
    public DataCenterVo getDatacenter(String id){
        SystemService systemService = admin.getConnection().systemService();

        DataCenter dataCenter = ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(id).get().send()).dataCenter();
        DataCenterVo dcVo = new DataCenterVo();

        dcVo.setId(id);
        dcVo.setName(dataCenter.name());
        dcVo.setDescription(dataCenter.description());
        dcVo.setStorageType(dataCenter.local());
        dcVo.setQuotaMode(dataCenter.quotaMode());
        dcVo.setVersion(dataCenter.version().major() + "." + dataCenter.version().minor());
        dcVo.setComment(dataCenter.comment());

        System.out.println(dcVo);
        return dcVo;
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
        String name = ovirt.getName("datacenter",id);

        for(Event event : eventList){
            // <data_center href="" id=""> <name> </data_center>
            // <data_center> <name> </data_center>
            if( event.dataCenterPresent() && event.dataCenter().name().equals(name) ){
                eVo = new EventVo();
                eVo.setDatacenterName(name);

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



    //---------------------------------------------------------------------------------------------------



    @Override
    public boolean addDatacenter(DataCenterVo dcVo) {
        SystemService systemService = admin.getConnection().systemService();
        DataCentersService datacentersService = systemService.dataCentersService();     // datacenters 서비스 불러오기
        List<DataCenter> dcList = datacentersService.list().send().dataCenters();

        log.info("addDatacenter Service try");
        String[] ver = dcVo.getVersion().split("\\.");      // 버전값 분리

        try {
            // 데이터센터 중복 이름은 생성 불가 (try-catch ? boolean)
            // DataCenter 생성
            DataCenter dataCenter = new DataCenterBuilder()
                    .name(dcVo.getName())       // 이름
                    .description(dcVo.getDescription())     // 설명
                    .local(dcVo.isStorageType())    // 스토리지 유형
                    .version(new VersionBuilder().major(Integer.parseInt(ver[0])).minor(Integer.parseInt(ver[1])).build())  // 호환 버전
                    .quotaMode(dcVo.getQuotaMode())      // 쿼터 모드
                    .comment(dcVo.getComment())     // 코멘트
                    .build();

            datacentersService.add().dataCenter(dataCenter).send();     // 데이터센터 만든거 추가

            log.info("------"+dcVo.toString());
            return datacentersService.list().send().dataCenters().size() == (dcList.size() + 1);    // 기존 데이터센터 개수와 추가된 데이터센터 개수를 비교
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void editDatacenter(DataCenterVo dcVo) {
        SystemService systemService = admin.getConnection().systemService();
        DataCenterService dataCenterService = systemService.dataCentersService().dataCenterService(dcVo.getId());

        log.info("editDatacenter Service try");
        String[] ver = dcVo.getVersion().split("\\.");      // 버전값 분리

        try {
            System.out.println("id: "+dcVo.getId());
            // DataCenter 생성
            DataCenter dataCenter = new DataCenterBuilder()
//                    .id(dcVo.getId())
                    .name(dcVo.getName())       // 이름
                    .description(dcVo.getDescription())     // 설명
                    .local(dcVo.isStorageType())    // 스토리지 유형
                    .version(new VersionBuilder().major(Integer.parseInt(ver[0])).minor(Integer.parseInt(ver[1])).build())  // 호환 버전
                    .quotaMode(dcVo.getQuotaMode())      // 쿼터 모드
                    .comment(dcVo.getComment())     // 코멘트
                    .build();

            // 데이터센터 수정
            dataCenterService.update().dataCenter(dataCenter).send();

            log.info("------"+dcVo.toString());
        }catch (Exception e){
            log.error("error: "+ e);
        }
    }

    @Override
    public boolean deleteDatacenter(String id) {
        SystemService systemService = admin.getConnection().systemService();
        DataCentersService datacentersService = systemService.dataCentersService();     // datacenters 서비스 불러오기
        List<DataCenter> dcList = datacentersService.list().send().dataCenters();

        try {
            System.out.println("id: "+id);
            DataCenterService dataCenterService = systemService.dataCentersService().dataCenterService(id);
            log.info("deleteDatacenter Service try");

            dataCenterService.remove().force(true).send();
            System.out.println(datacentersService.list().send().dataCenters().size() + "==" + (dcList.size() - 1));

            return true;
        }catch (Exception e){
            log.error("error ", e);
            return false;
        }
    }
}
