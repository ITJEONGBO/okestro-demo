package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.create.DomainSetVo;
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

import javax.net.ssl.*;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StorageServiceImpl implements ItStorageService {
    @Autowired private AdminConnectionService admin;
    @Autowired private CommonService commonService;


    // region: disk
    @Override
    public List<DiskVo> getDiskList(String dcId) {
        SystemService system = admin.getConnection().systemService();
        AttachedStorageDomainsService attSdsService = system.dataCentersService().dataCenterService(dcId).storageDomainsService();

        // 연결대상때문에 필요한듯 == vm의 diskattachment에 disk id가 같은지 비교. 근데 전체 vms를 다 뒤져야함 => 복잡
        VmsService vmsService = system.vmsService();

        return attSdsService.list().send().storageDomains().stream()
                .flatMap(sd ->
                    attSdsService.storageDomainService(sd.id()).disksService().list().send().disks().stream()
                        .map(disk -> {
                            boolean isAttached = vmsService.list().send().vms().stream()
                                    .anyMatch(vm -> vmsService.vmService(vm.id()).diskAttachmentsService().list().send().attachments().stream()
                                                .anyMatch(diskAttachment -> diskAttachment.id().equals(disk.id())));

                            return DiskVo.builder()
                                    .id(disk.id())
                                    .name(disk.name())
                                    .alias(disk.alias())
                                    .description(disk.description())
                                    .shareable(disk.shareable())
                                    .status(disk.status())
                                    .storageType(disk.storageType().value())
                                    .virtualSize(disk.provisionedSize())
//                                    .connection(String.valueOf(isAttached)) // TODO:HELP 연결대상 쉽지않음
                                    .domainVo(DomainVo.builder().id(sd.id()).name(sd.name()).build())
                                    .build();
                        })
                )
                .collect(Collectors.toList());
    }


    // 이미지 생성 위한 데이터센터 up 상태것만
    public List<IdentifiedVo> setDcList(){
        SystemService system = admin.getConnection().systemService();
        return system.dataCentersService().list().send().dataCenters().stream()
                .filter(dc -> dc.status() == DataCenterStatus.UP)
                .map(dc -> IdentifiedVo.builder().id(dc.id()).name(dc.name()).build())
                .collect(Collectors.toList());
    }

    // 디스크 - 새로만들기 - 이미지(데이터센터, 스토리지 도메인) 목록 보여지게
    // TODO:HELP
    @Override
    public DiskDcVo setDiskImage(String dcId) {
        SystemService system = admin.getConnection().systemService();
        DataCenter dataCenter = system.dataCentersService().dataCenterService(dcId).get().follow("clusters").send().dataCenter();
        List<StorageDomain> sdList = system.dataCentersService().dataCenterService(dcId).storageDomainsService().list().send().storageDomains();

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
                .build();
    }

    // 스토리지 > 디스크 > 새로만들기 - 이미지
    // ovirt에서 dc정보는 스토리지 도메인을 파악하기 위해있음
    // storage_domain, provisioned_size and format
    @Override
    public CommonVo<Boolean> addDiskImage(ImageCreateVo image) {
        SystemService system = admin.getConnection().systemService();
        DisksService disksService = system.disksService();

        try{
            DiskBuilder diskBuilder = new DiskBuilder();
            diskBuilder
					.provisionedSize(BigInteger.valueOf(image.getSize()).multiply(BigInteger.valueOf(1024).pow(3))) // 값 받은 것을 byte로 변환하여 준다
                    .name(image.getAlias())
                    .description(image.getDescription())
					.storageDomains(new StorageDomain[]{ new StorageDomainBuilder().id(image.getDomainId()).build()})
                    .sparse(image.isSparse()) // 할당정책: 씬 true
                    .diskProfile(new DiskProfileBuilder().id(image.getProfileId()).build()) // 없어도 상관없음
					.wipeAfterDelete(image.isWipeAfterDelete()) // 삭제후 초기화
                    .shareable(image.isShare())     // 공유 가능 (공유가능 o 이라면 증분백업 안됨 FRONT에서 막기?)
                    .backup(image.isBackup() ? DiskBackup.INCREMENTAL : DiskBackup.NONE)    // 증분 백업 사용(기본이 true)
                    .format(image.isBackup() ? DiskFormat.COW : DiskFormat.RAW) // 백업 안하면 RAW
            .build();

            Disk disk = disksService.add().disk(diskBuilder).send().disk();

            do{
                log.info("not ok");
            }while (disk.status() == DiskStatus.OK);

            log.info("성공: 디스크 이미지 {} 생성", image.getAlias());
            return CommonVo.createResponse();
        }catch (Exception e){
            log.error("실패: 새 가상 디스크 (이미지) 생성");
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 디스트 수정
    // 수정시 나오는 화면은 다 같은거 같아서
    // 디스크 프로파일은 막아야할 거 같음
    @Override
    public CommonVo<Boolean> editDiskImage(ImageCreateVo image) {
        SystemService system = admin.getConnection().systemService();
        DiskService diskService = system.disksService().diskService(image.getId());

        try{
            DiskBuilder diskBuilder = new DiskBuilder();
            diskBuilder
                    .id(image.getId())
                    .provisionedSize( (BigInteger.valueOf(image.getSize()).add(image.getAppendSize())).multiply(BigInteger.valueOf(1024).pow(3)) ) // 값 받은 것을 byte로 변환하여 준다
                    .name(image.getAlias())
                    .description(image.getDescription())
                    .wipeAfterDelete(image.isWipeAfterDelete()) // 삭제후 초기화
                    .shareable(image.isShare())     // 공유 가능
                    // 증분 백업 사용(기본이 true)
                    // 백업 수정시 false면 front에서 막아야됨
                    .backup(image.isBackup() ? DiskBackup.INCREMENTAL : DiskBackup.NONE)    
                    .format(image.isBackup() ? DiskFormat.COW : DiskFormat.RAW) // 백업 안하면 RAW
            .build();

            diskService.update().disk(diskBuilder).send().disk();

            log.info("성공: 디스크 이미지 {} 수정", image.getAlias());
            return CommonVo.createResponse();
//            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("실패: 새 가상 디스크 (이미지) 수정");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }


//    @Override
//    public LunCreateVo setDiskLun(String dcId) {
//        SystemService system = admin.getConnection().systemService();
//        DataCenter dataCenter = system.dataCentersService().dataCenterService(dcId).get().follow("clusters").send().dataCenter();
//        List<Host> hostList = system.hostsService().list().send().hosts();
//        hostList.stream()
//                .filter(Host::clusterPresent)
//                .map(host ->
//                        HostVo.builder()
//                                .id(host.id())
//                                .name(host.name())
//                        .build()
//                )
//                .collect(Collectors.toList());
//
//        return null;
//    }
//
//    // 스토리지 > 디스크 > 새로만들기 - 직접 LUN
//    // 오케는 lun생성 없음(코드는 있음)
//    @Override
//    public CommonVo<Boolean> addDiskLun(LunCreateVo lun) {
//        SystemService system = admin.getConnection().systemService();
//        DisksService disksService = system.disksService();
////        Host host = system.hostsService().hostService(lunVo.getHostId()).get().send().host();
//
//        // host 사용 -> 호스트는 상태 확인(예: LUN 표시) 및 LUN에 대한 기본 정보(예: 크기 및 일련 번호) 검색에 사용
//        // host 사용X ->  데이터베이스 전용 작업. 스토리지에 액세스되지 않습니다.
//
//        try{
//            DiskBuilder diskBuilder = new DiskBuilder();
//            diskBuilder
//                    .alias(lun.getAlias())
//                    .description(lun.getDescription())
//                    .lunStorage(
//                        new HostStorageBuilder()
//                            .host(new HostBuilder().id(lun.getHostId()).build())
//                            .type(lun.getStorageType())
//                            .logicalUnits(
//                                new LogicalUnitBuilder()
//                                    .address(lun.getAddress())
//                                    .port(lun.getPort())
//                                    .target(lun.getTarget())
//                                .build()
//                            )
//                        .build()
//                    )
//            .build();
//
////            Disk disk = disksService.add().disk(diskBuilder).send().disk();
//            Disk disk = disksService.addLun().disk(diskBuilder).send().disk();
//
//            do{
//                log.info("ok");
//            }while (disk.status().equals(DiskStatus.OK));
//
//            log.info("성공: 새 가상 디스크 (lun) 생성");
//            return CommonVo.successResponse();
//        }catch (Exception e){
//            log.error("실패: 새 가상 디스크 (lun) 생성");
//            return CommonVo.failResponse(e.getMessage());
//        }
//    }
//
//    @Override
//    public CommonVo<Boolean> editDiskLun(LunCreateVo lunCreateVo) {
//        SystemService system = admin.getConnection().systemService();
//
//        return null;
//    }




    @Override
    public CommonVo<Boolean> deleteDisk(String diskId) {
        SystemService system = admin.getConnection().systemService();
        DiskService diskService = system.disksService().diskService(diskId);
        Disk disk = system.disksService().diskService(diskId).get().send().disk();

        try{
            // 가상머신이 연결되어잇는지, down 상태인지
            diskService.remove().send();

            do{
                log.info("디스크 완전 삭제");
            }while (!disk.idPresent());

            log.info("성공: 디스크 삭제");
            return CommonVo.successResponse();
        }catch (Exception e){
            e.printStackTrace();
            log.error("실패: 가상 디스크 삭제");
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 디스크 이동 창
    // profile은 필요없음
    @Override
    public DiskVo setDiskMove(String dcId, String id) {
        SystemService system = admin.getConnection().systemService();
        List<StorageDomain> storageDomainList = system.dataCentersService().dataCenterService(dcId).storageDomainsService().list().send().storageDomains();
        Disk disk = system.disksService().diskService(id).get().send().disk();

        return DiskVo.builder()
                .id(disk.id())
                .alias(disk.alias())
                .virtualSize(disk.provisionedSize().divide(BigInteger.valueOf(1024).pow(3)))
//                .domainVoList(
//                    storageDomainList.stream()
//                            .filter(storageDomain -> !storageDomain.id().equals(disk.storageDomains().get(0).id()))
//                            .map(storageDomain ->
//                                DomainVo.builder()
//                                        .id(storageDomain.id())
//                                        .name(storageDomain.name())
//                                        .build()
//                            )
//                            .collect(Collectors.toList())
//                )
//                .dpVoList(
//                    storageDomainList.stream()
//                            .filter(storageDomain -> !storageDomain.id().equals(disk.storageDomains().get(0).id()))
//                            .flatMap(storageDomain -> {
//                                List<DiskProfile> diskProfileList = system.storageDomainsService().storageDomainService(storageDomain.id()).diskProfilesService().list().send().profiles();
//                                return diskProfileList.stream()
//                                        .map(diskProfile ->
//                                                DiskProfileVo.builder()
//                                                        .id(diskProfile.id())
//                                                        .name(diskProfile.name())
//                                                        .build()
//                                        );
//                            })
//                            .collect(Collectors.toList())
//                )
                .build();
    }

    // 디스크 이동
    @Override
    public CommonVo<Boolean> moveDisk(String id, DiskMoveCopyVo diskMoveCopyVo) {
        SystemService system = admin.getConnection().systemService();
        DiskService diskService = system.disksService().diskService(id);
        StorageDomain sd = system.storageDomainsService().storageDomainService(diskMoveCopyVo.getDomainId()).get().send().storageDomain();

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
    public DiskVo setDiskCopy(String dcId, String id) {
        SystemService system = admin.getConnection().systemService();
        Disk disk = system.disksService().diskService(id).get().send().disk();
        List<StorageDomain> storageDomainList = system.dataCentersService().dataCenterService(dcId).storageDomainsService().list().send().storageDomains();

        return DiskVo.builder()
                .id(disk.id())
                .alias(disk.alias())
                .virtualSize(disk.provisionedSize().divide(BigInteger.valueOf(1024).pow(3)))
                .domainVo(
                        storageDomainList.stream()
                                .filter(storageDomain -> storageDomain.id().equals(disk.storageDomains().get(0).id()))
                                .map(storageDomain ->
                                        DomainVo.builder()
                                                .id(storageDomain.id())
                                                .name(storageDomain.name())
                                                .build()
                                )
                                .findAny()
                                .orElse(null)
                )
//                .domainVoList(
//                        storageDomainList.stream()
//                                .map(storageDomain ->
//                                        DomainVo.builder()
//                                                .id(storageDomain.id())
//                                                .name(storageDomain.name())
//                                                .build()
//                                )
//                                .collect(Collectors.toList())
//                )
                .dpVoList(
                        storageDomainList.stream()
                                .filter(storageDomain -> !storageDomain.id().equals(disk.storageDomains().get(0).id()))
                                .flatMap(storageDomain -> {
                                    List<DiskProfile> diskProfileList = system.storageDomainsService().storageDomainService(storageDomain.id()).diskProfilesService().list().send().profiles();
                                    return diskProfileList.stream()
                                            .map(diskProfile ->
                                                    DiskProfileVo.builder()
                                                            .id(diskProfile.id())
                                                            .name(diskProfile.name())
                                                            .build()
                                            );
                                })
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public CommonVo<Boolean> copyDisk(String id, DiskMoveCopyVo diskMoveCopyVo) {
        SystemService system = admin.getConnection().systemService();
        DiskService diskService = system.disksService().diskService(id);
        StorageDomain sd = system.storageDomainsService().storageDomainService(diskMoveCopyVo.getDomainId()).get().send().storageDomain();

        try {
            DiskBuilder diskBuilder = new DiskBuilder();
            diskBuilder.alias(diskMoveCopyVo.getAlias()).build();

            diskService.copy().disk(diskBuilder).storageDomain(sd).send();

            log.info("성공: 디스크 복사");
            return CommonVo.successResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("실패: 디스크 복사");
            return CommonVo.failResponse(e.getMessage());
        }
    }









    // (화면표시) 파일 선택시 파일에 있는 포맷, 컨텐츠(파일 확장자로 칭하는건지), 크기 출력
    //           파일 크기가 자동으로 디스크 옵션에 추가, 파일 명칭이 파일의 이름으로 지정됨 (+설명)
    // required: provisioned_size, alias, description, wipe_after_delete, shareable, backup, disk_profile.
    @Override
    public CommonVo<Boolean> uploadDisk(MultipartFile file, ImageCreateVo image) throws IOException {
        SystemService system = admin.getConnection().systemService();
        DisksService disksService = system.disksService();
        ImageTransfersService imageTransfersService = system.imageTransfersService(); // 이미지 추가를 위한 서비스

        try {
            if(file == null || file.isEmpty()){
                return CommonVo.failResponse("파일이 없습니다.");
            }
            log.info("파일: {}, size: {}, 타입: {}", file.getOriginalFilename(), file.getSize(), file.getContentType());

            // 우선 입력된 디스크 정보를 바탕으로 디스크 추가
            Disk disk = disksService.add().disk(createDisk(image, file.getSize())).send().disk();
            DiskService diskService = disksService.diskService(disk.id());

            // 디스크 ok 상태여야 이미지 업로드 가능
            if(checkDiskStatus(diskService, DiskStatus.OK, 1000, 30000)){
                log.info("디스크 생성 성공");
            }else {
                log.error("디스크 대기시간 초과");
                return CommonVo.failResponse("디스크 대기시간 초과");
            }

            // 이미지를 저장하거나 관리하는 컨테이너,.이미지의 생성, 삭제, 수정 등의 작업을 지원
            ImageContainer imageContainer = new ImageContainer();
            imageContainer.id(disk.id());

            // 이미지 전송 작업(업로드나 다운로드)을 관리하는 컨테이너. 전송 상태, 진행률, 오류 처리 등의 기능을 포함.
            ImageTransferContainer imageTransferContainer = new ImageTransferContainer();
            imageTransferContainer.direction(ImageTransferDirection.UPLOAD);
            imageTransferContainer.image(imageContainer);

            // 이미지 전송
            ImageTransfer imageTransfer = imageTransfersService.add().imageTransfer(imageTransferContainer).send().imageTransfer();
            while (imageTransfer.phase() == ImageTransferPhase.INITIALIZING) {
                log.debug("이미지 업로드 가능상태 확인 {}", imageTransfer.phase());
                Thread.sleep(1000);
            }

            ImageTransferService imageTransferService = imageTransfersService.imageTransferService(imageTransfer.id());

            String transferUrl = imageTransfer.transferUrl();
            if(transferUrl == null || transferUrl.isEmpty()){
                log.debug("transferUrl 없음");
                return CommonVo.failResponse("transferUrl 가 없음");
            }else {
                log.debug("imageTransfer.transferUrl(): {}", transferUrl);
                imageSend(imageTransferService, file);
            }
            return CommonVo.successResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }


    /**
     * 디스크 생성
     * @param image
     * @param fileSize
     * @return
     */
    private Disk createDisk(ImageCreateVo image, long fileSize){
        return new DiskBuilder()
                .provisionedSize(fileSize)
                .alias(image.getAlias())
                .description(image.getDescription())
                .storageDomains(new StorageDomain[]{new StorageDomainBuilder().id(image.getDomainId()).build()})
                .diskProfile(new DiskProfileBuilder().id(image.getProfileId()).build())
                .wipeAfterDelete(image.isWipeAfterDelete())
                .shareable(image.isShare())
                .backup(DiskBackup.NONE) // 증분백업 되지 않음
                .format(DiskFormat.RAW) // 이미지 업로드는 raw형식만 가능
                .contentType(DiskContentType.ISO) // iso 업로드
                .build();
    }


    /**
     * 디스크 이미지 전송
     * @param imageTransferService
     * @param file
     */
    private CommonVo<Boolean> imageSend(ImageTransferService imageTransferService, MultipartFile file) {
        HttpsURLConnection httpsConn = null;

        try {
            disableSSLVerification();
            log.debug("imageSend");

            // 자바에서 HTTP 요청을 보낼 때 기본적으로 제한된 헤더를 사용자 코드에서 설정할 수 있도록 허용하는 설정
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

            URL url = new URL(imageTransferService.get().send().imageTransfer().transferUrl());
            httpsConn = (HttpsURLConnection) url.openConnection();
            httpsConn.setRequestMethod("PUT");
            httpsConn.setRequestProperty("Content-Length", String.valueOf(file.getSize()));
            httpsConn.setFixedLengthStreamingMode(file.getSize()); // 메모리 사용 최적화
            httpsConn.setDoOutput(true); // 서버에 데이터를 보낼수 있게 설정
            httpsConn.connect();

            // 버퍼 크기 설정 (128KB)
            int bufferSize = 131072;
            try (
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(file.getInputStream(), bufferSize);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(httpsConn.getOutputStream(), bufferSize);
            ) {
                byte[] buffer = new byte[bufferSize];
                int bytesRead;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, bytesRead);
                }
                bufferedOutputStream.flush();

                // image 전송 완료
                imageTransferService.finalize_().send();

                httpsConn.disconnect();

                ImageTransfer imageTransfer = imageTransferService.get().send().imageTransfer();
                log.debug("phase() : {}", imageTransfer.phase());

                return CommonVo.successResponse();
            } catch (IOException e) {
                e.printStackTrace();
                return CommonVo.failResponse("disk image 업로드 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonVo.failResponse("");
        } finally {
            if (httpsConn != null) {
                httpsConn.disconnect();
            }
        }
    }


    /**
     * SSL 인증서를 검증하지 않도록 설정하는 메서드
     */
    private void disableSSLVerification() {
        log.debug("disableSSLVerification");
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 인증서를 keystore에 저장하는 과정
    public void trust(URL url, int bytes) throws Exception {
        FileInputStream fis = new FileInputStream("D:/key/server.crt");
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate caCert = (X509Certificate) cf.generateCertificate(fis);

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("caCert", caCert);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(null, tmf.getTrustManagers(), new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory());

        // 모든 호스트 이름을 검증 없이 허용 (보안에 좋지 않음)
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
        httpsConn.setDoInput(true);
        httpsConn.setDoOutput(true);
        httpsConn.setRequestMethod("PUT");
        httpsConn.setConnectTimeout(2000);

        try (OutputStream os = httpsConn.getOutputStream()) {
            os.write(bytes);
            os.flush();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpsConn.getInputStream(), StandardCharsets.UTF_8))) {
            reader.lines().collect(Collectors.joining());
        }
    }


//    @Override
//    public CommonVo<Boolean> cancelUpload(String diskId) {
//        SystemService system = admin.getConnection().systemService();
//
//        imageTranService.cancel().send();
//
//        return CommonVo.successResponse();
//    }
//
//    @Override
//    public CommonVo<Boolean> pauseUpload(String diskId) {
//        SystemService system = admin.getConnection().systemService();
//
//        imageTranService.pause().send();
//
//        return CommonVo.successResponse();
//    }
//
//    @Override
//    public CommonVo<Boolean> resumeUpload(String diskId) {
//        SystemService system = admin.getConnection().systemService();
//
//        imageTranService.resume().send();
//
//        return CommonVo.successResponse();
//    }
//
//    @Override
//    public CommonVo<Boolean> downloadDisk() {
//        SystemService system = admin.getConnection().systemService();
//
//
//        return CommonVo.successResponse();
//    }


    /**
     * 디스크 상태 확인
     * @param diskService 해당 디스크 서비스
     * @param expectStatus 예상하는 디스크 상태
     * @param interval 대기 시간
     * @param timeout 총 대기시간
     * @return
     * @throws InterruptedException
     */
    private boolean checkDiskStatus(DiskService diskService, DiskStatus expectStatus, long interval, long timeout) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while (true) {
            Disk currentVm = diskService.get().send().disk();
            DiskStatus status = currentVm.status();

            if (status == expectStatus) {
                return true;
            } else if (System.currentTimeMillis() - startTime > timeout) {
                return false;
            }

            log.info("디스크 상태: {}", status);
            Thread.sleep(interval);
        }
    }


    // endregion


    // region: domain

    @Override
    public List<DomainVo> getDomainList(String dcId) {
        SystemService system = admin.getConnection().systemService();
        List<StorageDomain> storageDomainList = system.storageDomainsService().list().send().storageDomains();

        return storageDomainList.stream()
                .filter(storageDomain -> !storageDomain.dataCentersPresent() || storageDomain.dataCenters().get(0).id().equals(dcId))
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
                            .diskSize(storageDomain.usedPresent() ? storageDomain.used().add(storageDomain.available()) : null)
                            .availableSize(storageDomain.available())
                            .description(storageDomain.description())
                            .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<DomainSetVo> setDomain() {
        SystemService system = admin.getConnection().systemService();
        List<DataCenter> dataCenterList = system.dataCentersService().list().send().dataCenters();

        return null;
    }

    // requires: name, type, host, and storage attributes. Identify the host attribute with the id or name attributes.
    // To add a new storage domain with specified name, type, storage.type, storage.address, and storage.path,
    // and using a host with an id 123, send a request like this
    @Override
    public CommonVo<Boolean> addDomain(DomainCreateVo dcVo) {
        SystemService system = admin.getConnection().systemService();
        StorageDomainsService storageDomainsService = system.storageDomainsService();
        DataCenterService dataCenterService = system.dataCentersService().dataCenterService(dcVo.getDatacenterId());

        try{
            StorageDomainBuilder storageDomainBuilder = new StorageDomainBuilder();

            // 스토리지 유형이 iscsi 일 경우
            if(dcVo.getStorageType().equals(StorageType.ISCSI)){
                storageDomainBuilder
                    .dataCenter(new DataCenterBuilder().id(dcVo.getDatacenterId()).build())
                    .name(dcVo.getName())
                    .type(dcVo.getDomainType()) // domaintype
                    .storage(
                        new HostStorageBuilder()
                            .type(StorageType.ISCSI)
                            .logicalUnits(new LogicalUnitBuilder().id(dcVo.getLogicalUnitId()).build()) // TODO
                        .build()
                    )
                    .host(new HostBuilder().name(dcVo.getHostName()).build())
//                        .discardAfterDelete()
//                        .supportsDiscard()
//                        .backup()
//                        .wipeAfterDelete()
                .build();
            }else { // 그외 다른 유형
                // 경로  예: myserver.mydomain.com:/my/local/path
                // paths[0] = address, paths[1] = path
                String[] paths = dcVo.getPath().split(":", 2);
                if(paths.length != 2){
                    return CommonVo.failResponse("콜론 error");
                }

                storageDomainBuilder
                    .name(dcVo.getName())
                    .type(dcVo.getDomainType())
                    .storage(
                        new HostStorageBuilder()
                            .type(dcVo.getStorageType())
                            .address(paths[0].trim())
                            .path(paths[1].trim())
                        .build()
                    )
                    .host(new HostBuilder().name(dcVo.getHostName()).build())
                .build();
            }

            StorageDomain storageDomain = storageDomainsService.add().storageDomain(storageDomainBuilder).send().storageDomain();
            StorageDomainService storageDomainService = storageDomainsService.storageDomainService(storageDomain.id());

            do {
                Thread.sleep(2000L);
                storageDomain = storageDomainService.get().send().storageDomain();
            } while(storageDomain.status() != StorageDomainStatus.UNATTACHED);

            AttachedStorageDomainsService asdsService = dataCenterService.storageDomainsService();
            AttachedStorageDomainService asdService = asdsService.storageDomainService(storageDomain.id());
            try {
                asdsService.add().storageDomain(new StorageDomainBuilder().id(storageDomain.id())).send();
//                        .dataCenter(new DataCenterBuilder().id(dcVo.getDatacenterId()).build())
            } catch (Exception var18) {
                var18.printStackTrace();
            }

            do {
                Thread.sleep(2000L);

                storageDomain = asdService.get().send().storageDomain();
            } while(storageDomain.status() != StorageDomainStatus.ACTIVE);

            return CommonVo.successResponse();
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public CommonVo<Boolean> deleteDomain(String domainId) {
        SystemService system = admin.getConnection().systemService();
        StorageDomainService storageDomainService = system.storageDomainsService().storageDomainService(domainId);
        StorageDomain storageDomain = storageDomainService.get().send().storageDomain();
        AttachedStorageDomainService asdService = system.dataCentersService().dataCenterService(storageDomain.dataCenters().get(0).id()).storageDomainsService().storageDomainService(domainId);

        try{
            asdService.remove().async(true).send();

            do{
                System.out.println("떨어짐");
            }while (storageDomain.status() != StorageDomainStatus.UNATTACHED);

            storageDomainService.remove().destroy(true).send();

            return CommonVo.successResponse();
        }catch (Exception e){
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // endregion



    // 데이터가 많이 없음 생성요청
//    @Override
//    public List<VolumeVo> getVolumeVoList(String dcId) {
//        SystemService system = admin.getConnection().systemService();
//
//        return null;
//    }



    // dc에 잇는 스토리지 도메인
    @Override
    public List<DomainVo> getStorageList(String dcId) {
        SystemService system = admin.getConnection().systemService();
        return system.dataCentersService().dataCenterService(dcId).storageDomainsService().list().send().storageDomains().stream()
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
        SystemService system = admin.getConnection().systemService();
        return system.dataCentersService().dataCenterService(dcId).networksService().list().send().networks().stream()
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
        SystemService system = admin.getConnection().systemService();
        return system.dataCentersService().dataCenterService(dcId).clustersService().list().send().clusters().stream()
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
        SystemService system = admin.getConnection().systemService();
        List<Permission> permissionList = system.storageDomainsService().storageDomainService(id).permissionsService().list().send().permissions();

        return commonService.getPermission(system, permissionList);
    }

    @Override
    public List<EventVo> getEvent(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Event> eventList = system.eventsService().list().send().events();
        String dcName = system.dataCentersService().dataCenterService(id).get().send().dataCenter().name();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        log.info("데이터센터 {} 이벤트 출력", dcName);
        return eventList.stream()
                .filter(event -> event.dataCenterPresent()
                        && (event.dataCenter().idPresent() && event.dataCenter().id().equals(id) || (event.dataCenter().namePresent() && event.dataCenter().name().equals(dcName)) )
                )
                .map(event ->
                        EventVo.builder()
                                .datacenterName(dcName)
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

