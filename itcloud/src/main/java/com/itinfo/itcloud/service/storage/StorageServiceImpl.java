package com.itinfo.itcloud.service.storage;

import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.*;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItStorageService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StorageServiceImpl implements ItStorageService {
    @Autowired private AdminConnectionService admin;

    @Override
    public String getName(String id){
        return admin.getConnection().systemService().storageDomainsService().storageDomainService(id).get().send().storageDomain().name();
    }

    // de에 있는 디스크로 바꿔야하긴한데
    @Override
    public List<DiskVo> getDiskVoList(String dcId) {
        SystemService systemService = admin.getConnection().systemService();

        List<StorageDomain> sdList = systemService.storageDomainsService().list().send().storageDomains();
//        List<Disk> diskList = systemService.storageDomainsService().storageDomainService(sdId).disksService().list().send().disks();

        List<Disk> diskList = systemService.disksService().list().send().disks();

        return diskList.stream()
                .map(disk ->
                        DiskVo.builder()
                                .id(disk.id())
                                .name(disk.name())
                                .alias(disk.alias())
                                .description(disk.description())
    //                           .connection()
                                .storageDomainName(getName(disk.storageDomains().get(0).id()))
                                .shareable(disk.shareable())
                                .status(disk.status())
                                .storageType(disk.storageType())
                                .virtualSize(disk.provisionedSize())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public ImageVo setAddDisk() {
        return null;
    }

    @Override
    public CommonVo<Boolean> addDiskImage(ImageVo imageVo) {
        SystemService systemService = admin.getConnection().systemService();

        DisksService disksService = systemService.disksService();

        //


        return null;
    }

    @Override
    public CommonVo<Boolean> addDiskLun(LunVo lunVo) {
        SystemService systemService = admin.getConnection().systemService();
        return null;
    }

    @Override
    public CommonVo<Boolean> addDiskBlock(BlockVo blockVo) {
        SystemService systemService = admin.getConnection().systemService();
        return null;
    }

    @Override
    public List<DomainVo> getDomainList() {
        SystemService systemService = admin.getConnection().systemService();

        List<StorageDomain> storageDomainList = systemService.storageDomainsService().list().send().storageDomains();

        return storageDomainList.stream()
                .map(storageDomain ->
                        DomainVo.builder()
                                .status(storageDomain.status())
                                .id(storageDomain.id())
                                .name(storageDomain.name())
                                .comment(storageDomain.comment())
                                .domainType(storageDomain.type())   //storageDomain.type().value() + (storageDomain.master() ? "(마스터)" : "")
                                .domainTypeMaster(storageDomain.master())
                                .storageType(storageDomain.storage().type())
                                .format(storageDomain.storageFormat())
                                // 데이터 센터간 상태
                                .diskSize(storageDomain.usedPresent() ? storageDomain.used().add(storageDomain.available()) : null)  // 전체공간
                                .availableSize(storageDomain.available())
                                // 확보된 여유공간
                                .description(storageDomain.description())
                            .build()
                )
                .collect(Collectors.toList());
    }

    // 데이터가 많이 없음 생성요청
    @Override
    public List<VolumeVo> getVolumeVoList(String dcId) {
        return null;
    }

    // dc에 잇는 스토리지 도메인
    @Override
    public List<DomainVo> getStorageList(String dcId) {
        SystemService systemService = admin.getConnection().systemService();

        List<StorageDomain> storageDomainList = systemService.dataCentersService().dataCenterService(dcId).storageDomainsService().list().send().storageDomains();

        return storageDomainList.stream()
                .map(storageDomain ->
                        DomainVo.builder()
                                .id(storageDomain.id())
                                .name(storageDomain.name())
                                .description(storageDomain.description())
                                .domainType(storageDomain.type())   //storageDomain.type().value() + (storageDomain.master() ? "(마스터)" : "")
                                .status(storageDomain.status())
                                .availableSize(storageDomain.available())
                                .usedSize(storageDomain.used())
                                .diskSize(storageDomain.used().add(storageDomain.available()))
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<NetworkVo> getNetworkVoList(String dcId) {
        SystemService systemService = admin.getConnection().systemService();

        List<Network> networkList = systemService.dataCentersService().dataCenterService(dcId).networksService().list().send().networks();

        return networkList.stream()
                .map(network ->
                    NetworkVo.builder()
                            .id(network.id())
                            .name(network.name())
                            .description(network.description())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<ClusterVo> getClusterVoList(String dcId) {
        SystemService systemService = admin.getConnection().systemService();

        List<Cluster> clusterList = systemService.dataCentersService().dataCenterService(dcId).clustersService().list().send().clusters();

        return clusterList.stream()
                .map(cluster ->
                        ClusterVo.builder()
                                .id(cluster.id())
                                .name(cluster.name())
                                .description(cluster.description())
                                .version(cluster.version().fullVersion())
                            .build()
                )
                .collect(Collectors.toList());
    }


    //데이터센터 - 권한
    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList = systemService.dataCentersService().dataCenterService(id).permissionsService().list().send().permissions();


        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            // 그룹이 있고, 유저가 없을때
            if(permission.groupPresent() && !permission.userPresent()){
                Group group = systemService.groupsService().groupService(permission.group().id()).get().send().get();
                Role role = systemService.rolesService().roleService(permission.role().id()).get().send().role();

                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            // 그룹이 없고, 유저가 있을때
            if(!permission.groupPresent() && permission.userPresent()){
                User user = systemService.usersService().userService(permission.user().id()).get().send().user();
                Role role = systemService.rolesService().roleService(permission.role().id()).get().send().role();

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
        List<Event> eventList = systemService.eventsService().list().send().events();

        log.info("데이터센터 {} 이벤트 출력", getName(id));
        return eventList.stream()
                .filter(Event::dataCenterPresent)
                .map(event ->
                        EventVo.builder()
                                .datacenterName(getName(id))
                                .severity(TypeExtKt.findLogSeverity(event.severity()))   //상태
                                .time(sdf.format(event.time()))
                                .message(event.description())
                                .relationId(event.correlationIdPresent() ? event.correlationId() : null)
                                .source(event.origin())
                                .build()
                )
                .collect(Collectors.toList());
    }

}
