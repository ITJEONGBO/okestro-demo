package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.create.DomainSetVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.*;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItStorageService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.*;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
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
    // de에 있는 디스크로 바꿔야하긴한데
    @Override
    public List<DiskVo> getDiskList(String dcId) {
        SystemService system = admin.getConnection().systemService();
        List<StorageDomain> sdList = system.dataCentersService().dataCenterService(dcId).storageDomainsService().list().send().storageDomains();

        List<Vm> vmList = system.vmsService().list().send().vms();

        // TODO
        return sdList.stream()
                .flatMap(storageDomain -> {
                    List<Disk> diskList = system.dataCentersService().dataCenterService(dcId).storageDomainsService().storageDomainService(storageDomain.id()).disksService().list().send().disks();
                    return diskList.stream()
                            .map(disk -> {
                                Disk disk1 = system.disksService().diskService(disk.id()).get().send().disk();

//                                vmList.stream()
//                                        .flatMap(vm -> {
//                                            List<DiskAttachment> daList = system.vmsService().vmService(vm.id()).diskAttachmentsService().list().send().attachments();
//
//                                            return daList.stream()
//                                                    .map(diskAttachment -> {
//                                                        if(diskAttachment.vm().id().equals(vm.id())){
//                                                            return system.vmsService().vmService(vm.id()).get().send().vm().name();
//                                                        }
//                                                    })
//                                                    .findAny();
//
//                                        })
                                // 그러니까 vm의 diskattachment에 disk id가 같은지 비교
                                // 근데 전체 vms를 다 뒤져야함 => 복잡

                            return DiskVo.builder()
                                        .id(disk.id())
                                        .name(disk.name())
                                        .alias(disk.alias())
                                        .description(disk.description())
                                        .shareable(disk.shareable())
                                        .status(disk.status())
                                        .storageType(disk.storageType())
                                        .virtualSize(disk.provisionedSize())
//                                        .connection()
                                        .domainVoList(
                                            disk1.storageDomains().stream()
                                                    .map(storageDomain1 ->
                                                            DomainVo.builder()
                                                                    .id(storageDomain.id())
                                                                    .name(storageDomain.name())
                                                                    .build()
                                                    )
                                                    .collect(Collectors.toList())
                                        )
                                        .build();
                            });
                })
                .collect(Collectors.toList());
    }

    // 디스크 - 새로만들기 - 이미지(데이터센터, 스토리지 도메인) 목록 보여지게
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


    @Override
    public LunCreateVo setDiskLun(String dcId) {
        SystemService system = admin.getConnection().systemService();
        DataCenter dataCenter = system.dataCentersService().dataCenterService(dcId).get().follow("clusters").send().dataCenter();
        List<Host> hostList = system.hostsService().list().send().hosts();
        hostList.stream()
                .filter(Host::clusterPresent)
                .map(host ->
                        HostVo.builder()
                                .id(host.id())
                                .name(host.name())
                        .build()
                )
                .collect(Collectors.toList());

        return null;
    }

    // 스토리지 > 디스크 > 새로만들기 - 직접 LUN
    // 오케는 lun생성 없음(코드는 있음)
    @Override
    public CommonVo<Boolean> addDiskLun(LunCreateVo lun) {
        SystemService system = admin.getConnection().systemService();
        DisksService disksService = system.disksService();
//        Host host = system.hostsService().hostService(lunVo.getHostId()).get().send().host();

        // host 사용 -> 호스트는 상태 확인(예: LUN 표시) 및 LUN에 대한 기본 정보(예: 크기 및 일련 번호) 검색에 사용
        // host 사용X ->  데이터베이스 전용 작업. 스토리지에 액세스되지 않습니다.

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

//            Disk disk = disksService.add().disk(diskBuilder).send().disk();
            Disk disk = disksService.addLun().disk(diskBuilder).send().disk();

            do{
                log.info("ok");
            }while (disk.status().equals(DiskStatus.OK));

            log.info("성공: 새 가상 디스크 (lun) 생성");
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
                .domainVoList(
                    storageDomainList.stream()
                            .filter(storageDomain -> !storageDomain.id().equals(disk.storageDomains().get(0).id()))
                            .map(storageDomain ->
                                DomainVo.builder()
                                        .id(storageDomain.id())
                                        .name(storageDomain.name())
                                        .build()
                            )
                            .collect(Collectors.toList())
                )
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
                .domainVoList(
                        storageDomainList.stream()
                                .map(storageDomain ->
                                        DomainVo.builder()
                                                .id(storageDomain.id())
                                                .name(storageDomain.name())
                                                .build()
                                )
                                .collect(Collectors.toList())
                )
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


    // 디스크 상태 체크
    private void checkStatus(DisksService disksService, Disk disk, String expectStatus) throws InterruptedException {
        while (true) {
            DiskStatus status = disksService.diskService(disk.id()).get().send().disk().status();

            if (status == DiskStatus.valueOf(expectStatus)) {
                log.info("디스크 {} {} 완료", disk.name(), expectStatus);
                break;
            } else {
                log.debug("디스크 상태: " + status + ", {} 상태를 기다리는 중...", expectStatus);
                Thread.sleep(5000);  // 5초 대기 후 상태 재확인
            }
        }
    }




    // 파일 선택시 파일에 있는 포맷, 컨텐츠(파일 확장자로 칭하는건지), 크기 출력
    //           파일 크기가 자동으로 디스크 옵션에 추가
    //          파일 명칭이 파일의 이름으로 지정됨 (+설명)
    // provisioned_size, alias, description, wipe_after_delete, shareable, backup and disk_profile.
    public CommonVo<Boolean> uploadDisk(MultipartFile file, ImageCreateVo image) throws IOException {
        SystemService system = admin.getConnection().systemService();
        DisksService disksService = system.disksService();
        ImageTransfersService imageTransService = system.imageTransfersService(); // 이미지 추가를 위한 서비스

        try {
            long fileSize = file.getSize();
            int provisionedSize = (int) Math.ceil(file.getSize() / (Double) Math.pow(1024, 3));

            log.info("파일: {}, byte: {} -> {}, 타입: {}", file.getOriginalFilename(), fileSize, Math.ceil(fileSize / (Double) Math.pow(1024, 3)), file.getContentType());

            Disk disk = disksService.add()
                    .disk(createDisk(image, provisionedSize))
                    .send().disk();

            checkStatus(disksService, disk, "OK");

            ImageTransfer imageTransfer = imageTransService.add()
                    .imageTransfer(
                            new ImageTransferBuilder()
                                    .disk(disk)
                                    .direction(ImageTransferDirection.UPLOAD)
                    )
                    .send().imageTransfer();

            System.out.println("imageTransfer.transferUrl(): " + imageTransfer.transferUrl());

            // 이미지 업로드 가능한지 체크
            do {
                log.debug(String.valueOf(imageTransfer.phase()));
                Thread.sleep(1000L);
            } while (imageTransfer.phase() == ImageTransferPhase.INITIALIZING);

            if (imageTransfer.transferUrl() != null) {
                URL url = new URL(imageTransfer.transferUrl());

                // SSL 설정
//                trust(url, provisionedSize);
                disableSSLVerification();

                // 자바에서 HTTP 요청을 보낼 때 기본적으로 제한된 헤더를 사용자 코드에서 설정할 수 있도록 허용하는 설정
                System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

                // 연결
//                HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
//                httpsConn.setRequestProperty("Content-Length", String.valueOf(file.getSize()));
//                httpsConn.setRequestMethod("PUT");
//                httpsConn.setFixedLengthStreamingMode(file.getSize());
//                httpsConn.setDoOutput(true);
//                httpsConn.connect();
//
//                try (InputStream input = file.getInputStream();
//                     OutputStream output = httpsConn.getOutputStream()) {
//
//                    byte[] buf = new byte[131072];  // 128kb
//                    int bytes;
//                    while ((bytes = input.read(buf)) != -1) {
//                        output.write(buf, 0, bytes);
//                        output.flush();
//                    }
//
//                    if (httpsConn.getResponseCode() == 200) {
//                        System.out.println("finish");
//                    }
//                }

                HttpURLConnection httpsConn = (HttpURLConnection) url.openConnection();
                httpsConn.setDoOutput(true);
                httpsConn.setRequestMethod("POST");
                httpsConn.setRequestProperty("Content-Type", "application/octet-stream");

                try (OutputStream os = httpsConn.getOutputStream()) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    try (InputStream is = file.getInputStream()) {
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }
                }

                int responseCode = httpsConn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("Server returned non-OK status: " + responseCode);
                }

                imageTransService.imageTransferService(imageTransfer.id()).finalize_().send();
                httpsConn.disconnect();
            }
            return CommonVo.successResponse();
        } catch (Exception var28) {
            var28.printStackTrace();
            return CommonVo.failResponse(var28.getMessage());
        }
    }

    private Disk createDisk(ImageCreateVo image, long provisionedSize) throws Exception {
        return new DiskBuilder()
                .provisionedSize(provisionedSize)
                .alias(image.getAlias())
                .description(image.getDescription())
                .storageDomains(new StorageDomain[]{new StorageDomainBuilder().id(image.getDomainId()).build()})
                .wipeAfterDelete(image.isWipeAfterDelete())
                .shareable(image.isShare())
                .backup(image.isBackup() ? DiskBackup.INCREMENTAL : DiskBackup.NONE)
                .format(image.isShare() ? DiskFormat.RAW : DiskFormat.COW)
                .diskProfile(new DiskProfileBuilder().id(image.getProfileId()).build())
                .build();
    }

    public static void disableSSLVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
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
        FileInputStream fis = new FileInputStream("D:/keystore/rootca.crt");
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


    // 클라이언트가 SSL/TLS 연결을 설정할 때 서버의 인증서를 검증하는 과정에 대한 코드?
    private void setConnection(URL url, int bytes) throws Exception {
        String result = null;

        try {
            HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
            SSLContext ssl = SSLContext.getInstance("TLS");

            ssl.init(null,
                    new TrustManager[]{new X509TrustManager() {
                        // 신뢰할 수 있는 기관의 목록 반환
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        // 클라이언트 인증서 검증
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        // 서버 인증서 검증
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }
                    }},
                    new SecureRandom()
            );

            HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory());

            // 자바에서 HTTPS 연결을 설정할 때 서버의 호스트 이름을 검증하는 데 사용되는 인터페이스
            // Client가 SSL/TLS 연결을 설정할 때 서버 인증서를 검증하는 과정, 서버의 호스트 이름이 인증서에 명시된 호스트 이름과 일치하는지 확인
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

//            HttpsURLConnection.setDefaultHostnameVerifier(
//                    new HostnameVerifier() {
//                        public boolean verify(String hostName, SSLSession session) {
//                            // 반환 값: 호스트 이름 검증이 성공하면 true, 실패하면 false.
//                            // return "example.com".equalsIgnoreCase(hostname);
//                            return true;  // 모든 호스트 이름을 검증 없이 허용
//                        }
//                    }
//            );

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public CommonVo<Boolean> cancelUpload(String diskId) {
        SystemService system = admin.getConnection().systemService();

        ImageTransferService imageTranService = system.imageTransfersService().imageTransferService(diskId); // 이미지 추가를 위한 서비스
        imageTranService.cancel().send();

        return CommonVo.successResponse();
    }

    @Override
    public CommonVo<Boolean> pauseUpload(String diskId) {
        SystemService system = admin.getConnection().systemService();

        ImageTransferService imageTranService = system.imageTransfersService().imageTransferService(diskId); // 이미지 추가를 위한 서비스
        imageTranService.pause().send();

        return CommonVo.successResponse();
    }

    @Override
    public CommonVo<Boolean> resumeUpload(String diskId) {
        SystemService system = admin.getConnection().systemService();

        ImageTransferService imageTranService = system.imageTransfersService().imageTransferService(diskId); // 이미지 추가를 위한 서비스
        imageTranService.resume().send();

        return CommonVo.successResponse();
    }

    @Override
    public CommonVo<Boolean> downloadDisk() {
        return null;
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
    @Override
    public List<VolumeVo> getVolumeVoList(String dcId) {
        SystemService system = admin.getConnection().systemService();



        return null;
    }



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

