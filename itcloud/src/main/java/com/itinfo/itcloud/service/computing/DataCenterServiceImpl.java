package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

        List<DataCenter> dataCenterList = systemService.dataCentersService().list().send().dataCenters();

        return dataCenterList.stream()
                .map(dataCenter -> DataCenterVo.builder()
                        .id(dataCenter.id())
                        .name(dataCenter.name())
                        .description(dataCenter.description())
                        .storageType(dataCenter.local())
                        .status(TypeExtKt.findDCStatus(dataCenter.status()))
                        .quotaMode(TypeExtKt.findQuota(dataCenter.quotaMode()))
                        .version(dataCenter.version().major() + "." + dataCenter.version().minor())
                        .comment(dataCenter.comment())
                        .build()
                )
                .collect(Collectors.toList());
    }



    // 데이터센터 - 스토리지
//    @Override
//    public List<DomainVo> getStorage(String id) {
//        SystemService systemService = admin.getConnection().systemService();
//
//        List<DomainVo> sdVoList = new ArrayList<>();
//        DomainVo sdVo = null;
//
//        List<StorageDomain> storageDomainList = ((AttachedStorageDomainsService.ListResponse) systemService.dataCentersService().dataCenterService(id).storageDomainsService().list().send()).storageDomains();
//        for(StorageDomain storageDomain : storageDomainList) {
//            sdVo = new DomainVo();
//
//            sdVo.setId(storageDomain.id());
//            sdVo.setName(storageDomain.name());
//            sdVo.setDomainType( storageDomain.type().value() + (storageDomain.master() ? "(마스터)" : "") );
//            sdVo.setStatus(storageDomain.status().value());     // storageDomainStatus 10개
//            sdVo.setAvailableSize(storageDomain.available());   // 여유공간
//            sdVo.setUsedSize(storageDomain.used());             // 사용된 공간
//            sdVo.setDiskSize(storageDomain.available().add(storageDomain.used()));
//            sdVo.setDescription(storageDomain.description());
//
//            sdVoList.add(sdVo);
//        }
//
//        return sdVoList;
//    }

    // 데이터센터 - 네트워크
//    @Override
//    public List<NetworkVo> getNetwork(String id) {
//        SystemService systemService = admin.getConnection().systemService();
//
//        List<NetworkVo> nwVoList = new ArrayList<>();
//        NetworkVo nwVo = null;
//
//        List<Network> networkList = ((DataCenterNetworksService.ListResponse) systemService.dataCentersService().dataCenterService(id).networksService().list().send()).networks();
//        for(Network network : networkList){
//            nwVo = new NetworkVo();
//
//            nwVo.setId(network.id());
//            nwVo.setName(network.name());
//            nwVo.setDescription(network.description());
//
//            nwVoList.add(nwVo);
//        }
//        return nwVoList;
//    }


    // 데이터센터 - 클러스터
//    @Override
//    public List<ClusterVo> getCluster(String id) {
//        SystemService systemService = admin.getConnection().systemService();
//
//        List<ClusterVo> cVoList = new ArrayList<>();
//        ClusterVo cVo = null;
//
//        List<Cluster> clusterList = ((ClustersService.ListResponse) systemService.dataCentersService().dataCenterService(id).clustersService().list().send()).clusters();
//        for(Cluster cluster : clusterList){
//            cVo = new ClusterVo();
//
//            cVo.setId(cluster.id());
//            cVo.setName(cluster.name());
//            cVo.setVersion(cluster.version().major() + "." + cluster.version().minor());
//            cVo.setDescription(cluster.description());
//
//            cVoList.add(cVo);
//        }
//        return cVoList;
//    }

    // 데이터센터 - 권한
//    @Override
//    public List<PermissionVo> getPermission(String id) {
//        SystemService systemService = admin.getConnection().systemService();
//
//        List<PermissionVo> pVoList = new ArrayList<>();
//        PermissionVo pVo = null;
//
//        List<Permission> permissionList =
//                ((AssignedPermissionsService.ListResponse) systemService.dataCentersService().dataCenterService(id).permissionsService().list().send()).permissions();
//        for(Permission permission : permissionList){
//            pVo = new PermissionVo();
//            pVo.setPermissionId(permission.id());
//
//            // 그룹이 있고, 유저가 없을때
//            if(permission.groupPresent() && !permission.userPresent()){
//                Group group = ((GroupService.GetResponse) systemService.groupsService().groupService(permission.group().id()).get().send()).get();
//                Role role = ((RoleService.GetResponse) systemService.rolesService().roleService(permission.role().id()).get().send()).role();
//
//                pVo.setUser(group.name());
//                pVo.setNameSpace(group.namespace());
//                pVo.setRole(role.name());
//
//                pVoList.add(pVo);       // 그룹에 추가
//            }
//
//            // 그룹이 없고, 유저가 있을때
//            if(!permission.groupPresent() && permission.userPresent()){
//                User user = ((UserService.GetResponse) systemService.usersService().userService(permission.user().id()).get().send()).user();
//                Role role = ((RoleService.GetResponse) systemService.rolesService().roleService(permission.role().id()).get().send()).role();
//
//                pVo.setUser(user.name());
//                pVo.setProvider(user.domainPresent() ? user.domain().name() : null);
//                pVo.setNameSpace(user.namespace());
//                pVo.setRole(role.name());
//
//                pVoList.add(pVo);
//            }
//        }
//        return pVoList;
//    }

    // 데이터센터 - 이벤트
    @Override
    public List<EventVo> getEvent(String id) {
        SystemService systemService = admin.getConnection().systemService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<Event> eventList = systemService.eventsService().list().send().events();
        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;
        String name = getName(id);

        // <data_center href="" id=""> <name> </data_center>
        // <data_center> <name> </data_center>
        for(Event event : eventList){
            if( event.dataCenterPresent() /*&& event.dataCenter().name().equals(name)*/ ){
                eVo = EventVo.builder()
                        .datacenterName(name)
                        .severity(TypeExtKt.findLogSeverity(event.severity()))   //상태
                        .time(sdf.format(event.time()))
                        .message(event.description())
                        .relationId(event.correlationIdPresent() ? event.correlationId() : null)
                        .source(event.origin())
                        .build();

                eVoList.add(eVo);
            }
        }
        return eVoList;
    }


    //---------------------------------------------------------------------------------------------------

    // 데이터센터 - edit 시 필요한 값
    @Override
    public DataCenterCreateVo getDatacenter(String id){
        SystemService systemService = admin.getConnection().systemService();

        DataCenter dataCenter = systemService.dataCentersService().dataCenterService(id).get().send().dataCenter();
        return DataCenterCreateVo.builder()
                .id(id)
                .name(dataCenter.name())
                .description(dataCenter.description())
                .storageType(dataCenter.local())
                .quotaMode(dataCenter.quotaMode())
                .version(dataCenter.version().major() + "." + dataCenter.version().minor())
                .comment(dataCenter.comment())
                .build();
    }


    // 데이터센터 생성
    @Override
    public CommonVo<Boolean> addDatacenter(DataCenterCreateVo dcVo) {
        SystemService systemService = admin.getConnection().systemService();

        DataCentersService datacentersService = systemService.dataCentersService();     // datacenters 서비스 불러오기
        DataCenter dataCenter = null;

        // 같은 이름이 있는지 확인하는 코드
        boolean dcName = datacentersService.list().search("name="+dcVo.getName()).send().dataCenters().isEmpty();
        String[] ver = dcVo.getVersion().split("\\.");      // 버전값 분리

        try {
            // 데이터센터 중복 이름은 생성 불가
            if(dcName) {
                dataCenter = new DataCenterBuilder()
                        .name(dcVo.getName())       // 이름
                        .description(dcVo.getDescription())     // 설명
                        .local(dcVo.isStorageType())    // 스토리지 유형
                        .version(new VersionBuilder()
                                .major(Integer.parseInt(ver[0]))
                                .minor(Integer.parseInt(ver[1]))
                                .build())  // 호환 버전
                        .quotaMode(dcVo.getQuotaMode())      // 쿼터 모드
                        .comment(dcVo.getComment())     // 코멘트
                        .build();

                datacentersService.add().dataCenter(dataCenter).send();     // 데이터센터 만든거 추가
                log.info("------addDatacenter " + dcVo.toString());
                return CommonVo.successResponse();
            }else {
                log.error("데이터센터 이름 중복 에러");
                return CommonVo.failResponse("데이터센터 이름 중복 에러");
            }
        }catch (Exception e){
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 데이터센터 수정
    @Override
    public CommonVo<Boolean> editDatacenter(DataCenterCreateVo dcVo) {
        SystemService systemService = admin.getConnection().systemService();

        DataCentersService datacentersService = systemService.dataCentersService();
        DataCenterService dataCenterService = systemService.dataCentersService().dataCenterService(dcVo.getId());
        DataCenter dataCenter = null;

        boolean dcName = datacentersService.list().search("name="+dcVo.getName()).send().dataCenters().isEmpty();
        String[] ver = dcVo.getVersion().split("\\.");      // 버전값 분리

        try {
            if (dcName) {
                dataCenter = new DataCenterBuilder()
                        .name(dcVo.getName())       // 이름
                        .description(dcVo.getDescription())     // 설명
                        .local(dcVo.isStorageType())    // 스토리지 유형
                        .version(new VersionBuilder()
                                .major(Integer.parseInt(ver[0]))
                                .minor(Integer.parseInt(ver[1]))
                                .build())  // 호환 버전
                        .quotaMode(dcVo.getQuotaMode())      // 쿼터 모드
                        .comment(dcVo.getComment())     // 코멘트
                        .build();

                dataCenterService.update().dataCenter(dataCenter).send();   // 데이터센터 수정
                log.info("------edit datacenter "+dcVo.toString());
                return CommonVo.successResponse();
            }else {
                log.error("데이터센터 이름 중복 에러");
                return CommonVo.failResponse("데이터센터 이름 중복 에러");
            }
        }catch (Exception e){
            log.error("error: "+ e);
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 데이터센터 삭제
    // 문제있음, 데이터센터 삭제를 두번 수행
    // 데이터센터가 있는지 확인하고 삭제하고 종료
    @Override
    public CommonVo<Boolean> deleteDatacenter(String id) {
        SystemService systemService = admin.getConnection().systemService();

        DataCenterService dataCenterService = systemService.dataCentersService().dataCenterService(id);
        DataCenter dataCenter = dataCenterService.get().send().dataCenter();

        try {
            if(dataCenter.idPresent()) {
                dataCenterService.remove().force(true).send();
                log.info("datacenter {} 삭제", dataCenter.name());
                return CommonVo.successResponse();
            }else {
                log.error("datacenter {} 삭제 실패", dataCenter.name());
                return CommonVo.failResponse("삭제할 Datacenter가 없습니다.");
            }
        }catch (Exception e){
            log.error("datacenter error");
            return CommonVo.failResponse(e.getMessage());
        }
    }


}
