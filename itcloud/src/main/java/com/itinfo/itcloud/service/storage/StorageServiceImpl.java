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
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.internal.containers.ImageContainer;
import org.ovirt.engine.sdk4.internal.containers.ImageTransferContainer;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
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

    // region: disk

    // de에 있는 디스크로 바꿔야하긴한데
    @Override
    public List<DiskVo> getDiskList(String dcId) {
        SystemService system = admin.getConnection().systemService();

        List<StorageDomain> sdList = system.storageDomainsService().list().send().storageDomains();
//        List<Disk> diskList = system.storageDomainsService().storageDomainService(sdId).disksService().list().send().disks();

        List<Disk> diskList = system.disksService().list().send().disks();

//        System.out.println(BigInteger.valueOf(2).multiply(BigInteger.valueOf(1024).pow(3)));

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

    // 디스크 - 새로만들기 - 이미지(데이터센터, 스토리지 도메인) 목록 보여지게
    @Override
    public DiskDcVo setDiskImage(String dcId) {
        SystemService system = admin.getConnection().systemService();

        DataCenter dataCenter = system.dataCentersService().dataCenterService(dcId).get().follow("clusters").send().dataCenter();
        List<StorageDomain> sdList = system.dataCentersService().dataCenterService(dcId).storageDomainsService().list().send().storageDomains();
        List<Host> hostList = system.hostsService().list().send().hosts();

        List<String> clusterId = dataCenter.clusters().stream().map(Identified::id).collect(Collectors.toList());
        clusterId.forEach(System.out::println);

        return DiskDcVo.builder()
                .dcId(dcId)
                .dcName(dataCenter.name())
                .domainVoList(
                    sdList.stream()
                        .map(storageDomain -> {
                            List<DiskProfile> dpList = system.storageDomainsService().storageDomainService(storageDomain.id()).diskProfilesService().list().send().profiles();

                            return DomainVo.builder()
                                    .id(storageDomain.id())
                                    .name(storageDomain.name())
                                    .diskSize(storageDomain.available().add(storageDomain.used()))
                                    .availableSize(storageDomain.available())
                                    .profileVoList(
                                        dpList.stream()
                                            .map(diskProfile ->
                                                    DiskProfileVo.builder()
                                                            .id(diskProfile.id())
                                                            .name(diskProfile.name())
                                                            .build()
                                            )
                                            .collect(Collectors.toList())
                                    )
                                    .build();
                        })
                        .collect(Collectors.toList())
                )
//                .hostVoList(
//                    hostList.stream()
//                        .filter(host -> host.clusterPresent() && host.cluster().id().equals())
//                        .map(host ->
//                            HostVo.builder()
//                                .id(host.id())
//                                .name(host.name())
//                            .build()
//                        )
//                        .collect(Collectors.toList())
//                )
                .build();
    }

    // 스토리지 > 디스크 > 새로만들기 - 이미지
    @Override
    public CommonVo<Boolean> addDiskImage(ImageCreateVo image) {
        // storage_domain, provisioned_size and format
        SystemService system = admin.getConnection().systemService();

        DisksService disksService = system.disksService();

        try{
            DiskBuilder diskBuilder = new DiskBuilder();
            diskBuilder
					.provisionedSize(image.getSize().multiply(BigInteger.valueOf(1024).pow(3))) // 값 받은 것을 byte로 변환하여 준다
                    .name(image.getName())
                    .description(image.getDescription())
					.storageDomains(new StorageDomain[]{ new StorageDomainBuilder().id(image.getDomainId()).build()})
					.wipeAfterDelete(image.isWipeAfterDelete())
                    .shareable(image.isShare())     // shareable
                    .backup(image.getBackup())
                    .format(image.isShare() ? DiskFormat.RAW : DiskFormat.COW)
                    .diskProfile(new DiskProfileBuilder().id(image.getProfileId()).build())
            .build();

            Disk disk = disksService.add().disk(diskBuilder).send().disk();

            do{
                log.info("ok");
            }while (disk.status().equals(DiskStatus.OK));

            log.info("성공: 디스크 이미지 {} 생성", image.getName());
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("실패: 새 가상 디스크 (이미지) 생성");
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public CommonVo<Boolean> editDiskImage(ImageCreateVo image) {
        SystemService system = admin.getConnection().systemService();

        DiskService diskService = system.disksService().diskService(image.getId());

        try{
            DiskBuilder diskBuilder = new DiskBuilder();
            diskBuilder
                    .id(image.getId())
                    .provisionedSize( (image.getSize().add(image.getAppendSize())).multiply(BigInteger.valueOf(1024).pow(3)) ) //확장 +
                    .name(image.getName())
                    .description(image.getDescription())
                    .storageDomains(new StorageDomain[]{ new StorageDomainBuilder().id(image.getDomainId()).build()})
                    .wipeAfterDelete(image.isWipeAfterDelete())
                    .shareable(image.isShare())
                    .format(image.isShare() ? DiskFormat.RAW : DiskFormat.COW)
                    .diskProfile(new DiskProfileBuilder().id(image.getProfileId()).build())
            .build();

            diskService.update().disk(diskBuilder).send().disk();

            log.info("성공: 디스크 이미지 {} 수정", image.getName());
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("실패: 새 가상 디스크 (이미지) 수정");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }


    @Override
    public LunCreateVo setDiskLun(String dcId) {
        SystemService system = admin.getConnection().systemService();

        return null;
    }

    // 스토리지 > 디스크 > 새로만들기 - 직접 LUN
    @Override
    public CommonVo<Boolean> addDiskLun(LunCreateVo lun) {
        SystemService system = admin.getConnection().systemService();

        DisksService disksService = system.disksService();
//        Host host = system.hostsService().hostService(lunVo.getHostId()).get().send().host();
        // host 사용하지 않고 직접 lun디스크 생성시 호스트 요소 제거

        try{
            DiskBuilder diskBuilder = new DiskBuilder();
            diskBuilder
                    .alias(lun.getAlias())
                    .description(lun.getDescription())
                    .lunStorage(
                        new HostStorageBuilder()
                            .host(new HostBuilder().id(lun.getHostId()).build())
                            .type(lun.getStorageType())
                            .logicalUnits(
                                new LogicalUnitBuilder()
                                    .address(lun.getAddress())
                                    .port(lun.getPort())
                                    .target(lun.getTarget())
                                .build()
                            )
                        .build()
                    )
            .build();

            Disk disk = disksService.add().disk(diskBuilder).send().disk();

            do{
                log.info("ok");
            }while (disk.status().equals(DiskStatus.OK));

            log.info("성공: 새 가상 디스크 (lun) {} 생성", lun.getAlias());
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("실패: 새 가상 디스크 (lun) 생성");
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public CommonVo<Boolean> editDiskLun(LunCreateVo lunCreateVo) {
        SystemService system = admin.getConnection().systemService();

        return null;
    }



    @Override
    public CommonVo<Boolean> deleteDisk(String diskId) {
        SystemService system = admin.getConnection().systemService();

        DiskService diskService = system.disksService().diskService(diskId);

        try{
            diskService.remove().send();

            log.info("성공: 디스크 {} 삭제", diskId);
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("실패: 새 가상 디스크 (이미지) 수정");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 디스크 이동
    @Override
    public CommonVo<Boolean> moveDisk(DiskVo disk) {
        SystemService system = admin.getConnection().systemService();

        DiskService diskService = system.disksService().diskService(disk.getId());
        StorageDomain sd = system.storageDomainsService().storageDomainService(disk.getStorageDomainId()).get().send().storageDomain();

        try {
            diskService.move().storageDomain(sd).send();

            log.info("성공: 디스크 이동");
            return CommonVo.successResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("실패: 디스크 이동");
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public CommonVo<Boolean> copyDisk(DiskVo disk) {
        // diskId, storageDomainId, disk-name
        SystemService system = admin.getConnection().systemService();

        DiskService diskService = system.disksService().diskService(disk.getId());
        StorageDomain sd = system.storageDomainsService().storageDomainService(disk.getStorageDomainId()).get().send().storageDomain();

        try {
            diskService.copy().disk((new DiskBuilder()).name(disk.getName()).build()).storageDomain(sd).send();

            log.info("성공: 디스크 복사");
            return CommonVo.successResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("실패: 디스크 복사");
            return CommonVo.failResponse(e.getMessage());
        }
    }



    // 파일 선택시 파일에 있는 포맷, 컨텐츠(파일 확장자로 칭하는건지), 크기 출력
    //           파일 크기가 자동으로 디스크 옵션에 추가
    //          파일 명칭이 파일의 이름으로 지정됨 (+설명)
    // 연결테스트
    // provisioned_size, alias, description, wipe_after_delete, shareable, backup and disk_profile.
    @Override
    public CommonVo<Boolean> uploadDisk(MultipartFile file, ImageCreateVo image) throws IOException {
        SystemService system = admin.getConnection().systemService();

        DisksService disksService = system.disksService();
        ImageTransfersService imageTransService = system.imageTransfersService(); // 이미지 추가를 위한 서비스

        InputStream input = null;
        OutputStream output = null;

        System.out.println("파일: " + file.getSize() +", "+ file.getContentType() +", "+ file.getOriginalFilename());
        System.out.println(Math.ceil(file.getSize() / (Double)Math.pow(1024, 3)));
//        BigInteger.valueOf(file.getSize()).divide(BigInteger.valueOf(1024).pow(3))

        try{
            input = file.getInputStream();

            // 디스크 기본 정보 입력
            DiskBuilder diskBuilder = new DiskBuilder();
            diskBuilder
                    .provisionedSize((int) Math.ceil(file.getSize() / (Double)Math.pow(1024, 3))) // 값 받은 것을 byte로 변환하여 준다
                    .name(image.getName())
                    .description(image.getDescription())
                    .storageDomains(new StorageDomain[]{ new StorageDomainBuilder().id(image.getDomainId()).build()})
                    .wipeAfterDelete(image.isWipeAfterDelete())
                    .shareable(image.isShare())     // shareable
                    .backup(image.getBackup())
                    .format(image.isShare() ? DiskFormat.RAW : DiskFormat.COW)
                    .diskProfile(new DiskProfileBuilder().id(image.getProfileId()).build())
//                    .lunStorage(new HostStorageBuilder().id(image.getHostId()).build())
                    .build();

            Disk disk = disksService.add().disk(diskBuilder).send().disk();
            System.out.println(disk.name() +": " + disk.provisionedSize()); // try1 : 1


            ImageContainer imageContainer = new ImageContainer();
            imageContainer.id(disk.id());

            ImageTransferContainer imageTransferContainer = new ImageTransferContainer();
            imageTransferContainer.image(imageContainer);
            System.out.println("imageContainer.id(): " + imageContainer.id());

            ImageTransfer imageTransfer = imageTransService.add().imageTransfer(new ImageTransferBuilder().disk(disk).build()).send().imageTransfer();
            System.out.println("imageTransfer.transferUrl(): " + imageTransfer.transferUrl());

            do {
                Thread.sleep(1000L);
            } while(imageTransferContainer.phase() == ImageTransferPhase.INITIALIZING);

            // transferUrl(): 사용자가 직접 입력하거나 출력할 수 있는 데몬 서버의 URL
            if (imageTransfer.transferUrl() != null){
                ImageTransferService transferService = system.imageTransfersService().imageTransferService(imageTransfer.id());

                // imageTransfer에서 처리하는 url을 얻어옴
                URL url = new URL(imageTransfer.transferUrl());

                // 연결
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestProperty("PUT", url.getPath());
                http.setRequestProperty("Content-Length", String.valueOf(file.getSize()));
                http.setRequestMethod("PUT");
                http.setFixedLengthStreamingMode(file.getSize());
                http.setDoOutput(true);
                http.connect();

                // 파일값을 받아옴(읽기)
                input = file.getInputStream();
                // 파일값으로 http로 보냄(쓰기)
                output = http.getOutputStream();

                // 바이트수 만큼 읽어오기
                byte[] buf = new byte[131072];  // 128kb

                int bytes;
                while ( (bytes = input.read(buf)) != -1) {
                    output.write(buf, 0, bytes);
                    output.flush(); // 값 내보내기
                }

                if (http.getResponseCode() == 200) {
                    System.out.println("finish");
                }

                input.close();
                output.close();

                transferService.finalize_().send();
                http.disconnect();
            }
            return CommonVo.successResponse();
        } catch (Exception var28) {
            var28.printStackTrace();
            return CommonVo.failResponse(var28.getMessage());
        }
    }

    @Override
    public CommonVo<Boolean> downloadDisk() {
        return null;
    }

    // endregion


    // region: domain

    @Override
    public List<DomainVo> getDomainList() {
        SystemService system = admin.getConnection().systemService();

//        List<DataCenter> dataCenterList = system.dataCentersService().list().send().dataCenters();
//
//        for(String id : dataCenterList.stream().map(Identified::id).collect(Collectors.toList())){
//            List<StorageDomain> storageDomainList = system.dataCentersService().dataCenterService(id).storageDomainsService().list().send().storageDomains();
//
//
//            System.out.println(id);
//        }

        List<StorageDomain> storageDomainList = system.storageDomainsService().list().send().storageDomains();


        return storageDomainList.stream()
                .map(storageDomain ->
                    DomainVo.builder()
                        .status(storageDomain.status())
                        .id(storageDomain.id())
                        .name(storageDomain.name())
                        .comment(storageDomain.comment())
                        .domainType(storageDomain.type())
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







    // endregion

    // region: volume

    // 데이터가 많이 없음 생성요청
    @Override
    public List<VolumeVo> getVolumeVoList(String dcId) {
        return null;
    }

    // endregion


    // region: storage

    // dc에 잇는 스토리지 도메인
    @Override
    public List<DomainVo> getStorageList(String dcId) {
        SystemService system = admin.getConnection().systemService();

        List<StorageDomain> storageDomainList = system.dataCentersService().dataCenterService(dcId).storageDomainsService().list().send().storageDomains();

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

    // endregion


    // region : network
    @Override
    public List<NetworkVo> getNetworkVoList(String dcId) {
        SystemService system = admin.getConnection().systemService();

        List<Network> networkList = system.dataCentersService().dataCenterService(dcId).networksService().list().send().networks();

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

    // endregion


    // region: cluster
    @Override
    public List<ClusterVo> getClusterVoList(String dcId) {
        SystemService system = admin.getConnection().systemService();

        List<Cluster> clusterList = system.dataCentersService().dataCenterService(dcId).clustersService().list().send().clusters();

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

    // endregion


    // region: permisson

    //데이터센터 - 권한
    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService system = admin.getConnection().systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList = system.dataCentersService().dataCenterService(id).permissionsService().list().send().permissions();


        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            // 그룹이 있고, 유저가 없을때
            if(permission.groupPresent() && !permission.userPresent()){
                Group group = system.groupsService().groupService(permission.group().id()).get().send().get();
                Role role = system.rolesService().roleService(permission.role().id()).get().send().role();

                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            // 그룹이 없고, 유저가 있을때
            if(!permission.groupPresent() && permission.userPresent()){
                User user = system.usersService().userService(permission.user().id()).get().send().user();
                Role role = system.rolesService().roleService(permission.role().id()).get().send().role();

                pVo.setUser(user.name());
                pVo.setProvider(user.domainPresent() ? user.domain().name() : null);
                pVo.setNameSpace(user.namespace());
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }

    // endregion


    // region : event

    @Override
    public List<EventVo> getEvent(String id) {
        SystemService system = admin.getConnection().systemService();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");
        List<Event> eventList = system.eventsService().list().send().events();

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

    // endregion

}
