package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.StorageDomainVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItStorageDomainService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StorageDomainServiceImpl implements ItStorageDomainService {
    @Autowired
    private AdminConnectionService adminConnectionService;


    @Override
    public List<StorageDomainVo> getList() {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<StorageDomainVo> sdVoList = new ArrayList<>();
        StorageDomainVo sdVo = null;

        List<StorageDomain> storageDomainList =
                ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().send()).storageDomains();

        for(StorageDomain storageDomain : storageDomainList){
            sdVo = new StorageDomainVo();

            sdVo.setId(storageDomain.id());
            sdVo.setName(storageDomain.name());
            sdVo.setDomainType(storageDomain.type().value() + (storageDomain.master() ? "(master)" : ""));
            sdVo.setStorageType(storageDomain.storage().type().value());
            sdVo.setDomainFormat(storageDomain.storageFormat().value());

            sdVo.setAvailableSize(storageDomain.availablePresent() ? storageDomain.available() : null); // 여유공간
            sdVo.setUsedSize(storageDomain.usedPresent() ? storageDomain.used() : null); // 사용된 공간
            sdVo.setDiskSize( (storageDomain.availablePresent() && storageDomain.usedPresent()) ? storageDomain.available().add(storageDomain.used()) : null);
            sdVo.setDescription(storageDomain.description());

            if(storageDomain.statusPresent()){
                sdVo.setStatus(storageDomain.status().value());
            }else if(storageDomain.externalStatusPresent()){
                sdVo.setStatus(storageDomain.externalStatus().value());
            }

            sdVoList.add(sdVo);
        }
        return sdVoList;
    }

    @Override
    public StorageDomainVo getDomain(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        StorageDomainVo sdVo = new StorageDomainVo();

        StorageDomain storageDomain =
                ((StorageDomainService.GetResponse)systemService.storageDomainsService().storageDomainService(id).get().send()).storageDomain();

        sdVo.setId(storageDomain.id());
        sdVo.setName(storageDomain.name());

        sdVo.setAvailableSize(storageDomain.available());
        sdVo.setUsedSize(storageDomain.used());
        sdVo.setCommitedSize(storageDomain.committed());
        sdVo.setDiskSize(storageDomain.availablePresent() ? storageDomain.available().add(storageDomain.used()) : null);

        sdVo.setStorageAddress(storageDomain.storagePresent() ? storageDomain.storage().address() : null);
        sdVo.setStoragePath(storageDomain.storage().path());
        sdVo.setNfsVersion(storageDomain.storage().nfsVersionPresent() ? storageDomain.storage().nfsVersion().value() : null);
        sdVo.setWarning(storageDomain.warningLowSpaceIndicatorAsInteger());
        sdVo.setBlockSize(storageDomain.criticalSpaceActionBlockerAsInteger());

        return sdVo;
    }

    @Override
    public List<DataCenterVo> getDatacenter(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<DataCenterVo> dcVoList = new ArrayList<>();
        DataCenterVo dcVo = null;

        List<StorageDomain> storageDomainList =
                ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().send()).storageDomains();

        for(StorageDomain storageDomain : storageDomainList){
            if(storageDomain.dataCentersPresent()){
                for(int i=0; i<storageDomain.dataCenters().size(); i++) {
                    DataCenter dc = ((DataCenterService.GetResponse) systemService.dataCentersService().dataCenterService(storageDomain.dataCenters().get(i).id()).get().send()).dataCenter();

                    dcVo = new DataCenterVo();

                    dcVo.setId(dc.id());
                    dcVo.setName(dc.name());
                    dcVo.setStatus(dc.status().value());
                    // 데이터 센터 내의 도메인 상태

                    dcVoList.add(dcVo);
                }
            }
        }
        return dcVoList;
    }

    @Override
    public List<VmVo> getVm(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();


        return null;
    }

    @Override
    public List<TemplateVo> getTemplate(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();


        return null;
    }

    @Override
    public List<DiskVo> getDisk(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<DiskVo> dVoList = new ArrayList<>();


        return null;
    }


    @Override
    public List<PermissionVo> getPermission(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList =
                ((AssignedPermissionsService.ListResponse)systemService.storageDomainsService().storageDomainService(id).permissionsService().list().send()).permissions();

        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            if(permission.groupPresent() && !permission.userPresent()){
                Group group = ((GroupService.GetResponse)systemService.groupsService().groupService(permission.group().id()).get().send()).get();
                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                // 생성일의 경우 db에서 가져와야함?

                Role role = ((RoleService.GetResponse)systemService.rolesService().roleService(permission.role().id()).get().send()).role();
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            if(permission.userPresent() && !permission.groupPresent()){
                User user = ((UserService.GetResponse)systemService.usersService().userService(permission.user().id()).get().send()).user();
                pVo.setUser(user.name());
                pVo.setNameSpace(user.namespace());

                Role role = ((RoleService.GetResponse)systemService.rolesService().roleService(permission.role().id()).get().send()).role();
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    @Override
    public List<EventVo> getEvent(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;

        // 2024. 1. 4. PM 04:01:21
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<Event> eventList =
                ((EventsService.ListResponse)systemService.eventsService().list().send()).events();

        for(Event event : eventList){
            if(event.storageDomainPresent() && event.storageDomain().id().equals(id)){
                eVo = new EventVo();

                eVo.setSeverity(event.severity().value());
                eVo.setTime(sdf.format(event.time()));
                eVo.setMessage(event.description());
                eVo.setRelationId(event.correlationIdPresent() ? event.correlationId() : "");
                eVo.setSource(event.origin());

                eVoList.add(eVo);
            }
        }
        return eVoList;
    }

}
