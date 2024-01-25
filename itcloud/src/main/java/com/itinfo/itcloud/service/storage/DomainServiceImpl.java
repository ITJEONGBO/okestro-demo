package com.itinfo.itcloud.service.storage;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVmVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.ovirt.OvirtService;
import com.itinfo.itcloud.service.ItDomainService;
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
public class DomainServiceImpl implements ItDomainService {
    @Autowired private AdminConnectionService admin;
    @Autowired private OvirtService ovirt;

    @Override
    public String getName(String id){
        SystemService systemService = admin.getConnection().systemService();

        return ((StorageDomainService.GetResponse)systemService.storageDomainsService().storageDomainService(id).get().send()).storageDomain().name();
    }

    @Override
    public List<DomainVo> getList() {
        SystemService systemService = admin.getConnection().systemService();

        List<DomainVo> sdVoList = new ArrayList<>();
        DomainVo sdVo = null;

        List<StorageDomain> storageDomainList =
                ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().send()).storageDomains();

        for(StorageDomain storageDomain : storageDomainList){
            sdVo = new DomainVo();

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
    public DomainVo getDomain(String id) {
        SystemService systemService = admin.getConnection().systemService();

        DomainVo sdVo = new DomainVo();

        StorageDomain storageDomain =
                ((StorageDomainService.GetResponse)systemService.storageDomainsService().storageDomainService(id).get().send()).storageDomain();

        sdVo.setId(storageDomain.id());
        sdVo.setName(storageDomain.name());

        sdVo.setAvailableSize(storageDomain.available());
        sdVo.setUsedSize(storageDomain.used());
        sdVo.setCommitedSize(storageDomain.committed());
        sdVo.setDiskSize(storageDomain.availablePresent() ? storageDomain.available().add(storageDomain.used()) : null);
//        sdVo.setOverCommit( storageDomain.committedPresent() ? sdVo.getCommitedSize().divide(sdVo.getAvailableSize()) : null);

        sdVo.setStorageAddress(storageDomain.storagePresent() ? storageDomain.storage().address() : null);
        sdVo.setStoragePath(storageDomain.storage().path());
        sdVo.setNfsVersion(storageDomain.storage().nfsVersionPresent() ? storageDomain.storage().nfsVersion().value() : null);
        sdVo.setWarning(storageDomain.warningLowSpaceIndicatorAsInteger());
        sdVo.setBlockSize(storageDomain.criticalSpaceActionBlockerAsInteger());

        return sdVo;
    }

    @Override
    public List<DataCenterVo> getDatacenter(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<DataCenterVo> dcVoList = new ArrayList<>();
        DataCenterVo dcVo = null;

        List<StorageDomain> storageDomainList =
                ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().send()).storageDomains();

        for(StorageDomain storageDomain : storageDomainList){
            if(storageDomain.dataCentersPresent()){
                for(int i=0; i<storageDomain.dataCenters().size(); i++) {
                    DataCenter dc =
                            ((DataCenterService.GetResponse) systemService.dataCentersService().dataCenterService(storageDomain.dataCenters().get(i).id()).get().send()).dataCenter();
                    dcVo = new DataCenterVo();

                    dcVo.setId(dc.id());
                    dcVo.setName(dc.name());
                    dcVo.setStatus(dc.status().value());

                    dcVoList.add(dcVo);
                }
            }
        }
        return dcVoList;
    }

    @Override
    public List<DomainVmVo> getVm(String id) {
        SystemService systemService = admin.getConnection().systemService();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");
        List<DomainVmVo> domainVmVoList = new ArrayList<>();
        DomainVmVo domainVmVo= null;
        int diskCnt = 0;

        List<Vm> vmList =
                ((StorageDomainVmsService.ListResponse)systemService.storageDomainsService().storageDomainService(id).vmsService().list().send()).vm();
        for(Vm vm : vmList){
            if(vm.storageDomainPresent() && vm.storageDomain().id().equals(id)) {
                domainVmVo = new DomainVmVo();

                domainVmVo.setVmName(vm.name());
                domainVmVo.setTemplateName(ovirt.getName("template", vm.template().id()));
                domainVmVo.setVmDate(sdf.format(vm.creationTime()));


                List<DiskAttachment> vmdiskList =
                        ((DiskAttachmentsService.ListResponse)systemService.vmsService().vmService(vm.id()).diskAttachmentsService().list().send()).attachments();
                domainVmVo.setDiskCnt(vmdiskList.size());

                for(DiskAttachment diskAttachment : vmdiskList){
                    Disk disk =
                            ((DiskService.GetResponse)systemService.disksService().diskService(diskAttachment.disk().id()).get().send()).disk();
                    domainVmVo.setDiskName(disk.name());
                    domainVmVo.setVirtualSize(disk.provisionedSize());
                    domainVmVo.setActualSize(disk.actualSize());
//                    domainVmVo.setSnapName(disk.snapshot().id());     // keep
                }
                domainVmVoList.add(domainVmVo);
            }
        }

        return domainVmVoList;
    }

    // 미완
    @Override
    public List<TemplateVo> getTemplate(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<TemplateVo> templateVoList = new ArrayList<>();
        TemplateVo tVo = null;

        List<Template> templateList =
                ((StorageDomainTemplatesService.ListResponse)systemService.storageDomainsService().storageDomainService(id).templatesService().list().send()).templates();
        for(Template template : templateList){
            tVo = new TemplateVo();
            tVo.setName(template.name());

            templateVoList.add(tVo);
        }
        return templateVoList;
    }

    // 미완
    @Override
    public List<DiskVo> getDisk(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<DiskVo> dVoList = new ArrayList<>();
        DiskVo diskVo = null;

        List<Disk> diskList =
                ((StorageDomainDisksService.ListResponse)systemService.storageDomainsService().storageDomainService(id).disksService().list().send()).disks();
        System.out.println(diskList.size());
        for(Disk disk : diskList){
            diskVo = new DiskVo();

            diskVo.setId(disk.id());
            diskVo.setAlias(disk.alias());
            diskVo.setVirtualSize(disk.provisionedSize());
            diskVo.setActualSize(disk.actualSize());
            // 부팅가능
            // 공유가능
            // 할당정책
            // 스토리지 도메인
            diskVo.setStorageDomainName(disk.storageDomainPresent() ? ovirt.getName("domain", disk.storageDomain().id()) : null);
            // 생성 일자
            // 최근 업데이트
            // 가상머신

            diskVo.setStatus(disk.status().value());
            diskVo.setStorageType(disk.storageType().value());
            diskVo.setDescription(disk.description());

            dVoList.add(diskVo);
        }

        return dVoList;
    }

    @Override
    public List<SnapshotVo> getSnapshot(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<SnapshotVo> snapshotVoList = new ArrayList<>();
        SnapshotVo sVo = null;

        List<DiskSnapshot> snapshotList =
                ((DiskSnapshotsService.ListResponse)systemService.storageDomainsService().storageDomainService(id).diskSnapshotsService().list().send()).snapshots();
        for(DiskSnapshot diskSnapshot : snapshotList){
            sVo =  new SnapshotVo();
            sVo.setName(diskSnapshot.name());

            snapshotVoList.add(sVo);
        }
        return snapshotVoList;
    }

    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;
        List<Permission> permissionList =
                ((AssignedPermissionsService.ListResponse)systemService.storageDomainsService().storageDomainService(id).permissionsService().list().send()).permissions();

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


    @Override
    public List<EventVo> getEvent(String id) {
        SystemService systemService = admin.getConnection().systemService();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;

        List<Event> eventList = ((EventsService.ListResponse) systemService.eventsService().list().send()).events();
        for (Event event : eventList) {
            eVo = new EventVo();

            if (event.storageDomainPresent() && event.storageDomain().id().equals(id)) {
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
