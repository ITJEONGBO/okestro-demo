package com.itinfo.itcloud.service.storage;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.UsageVo;
import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.model.storage.ImageCreateVo;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItStorageServiceTest {
    @Autowired
    ItStorageService storageService;

    String dcId = "ae1d4138-f642-11ee-9c1b-00163e4b3128";


    @Test
    void totalStorage(){
        UsageVo result = storageService.totalStorage();
        System.out.println(result);
    }

    @Test
    @DisplayName("디스크 목록 출력")
    void getDiskList() {
        List<DiskVo> result = storageService.getDiskList(dcId);

        result.stream()
                .map(DiskVo::getAlias)
                .forEach(System.out::println);

        assertThat(storageService.getDiskList(dcId).size()).isEqualTo(38);
    }

    @Test
    @DisplayName("디스크 데이터센터 리스트")
    void setDcList(){
        List<IdentifiedVo> result = storageService.setDatacenterList();

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("디스크 스토리지 도메인 리스트")
    void setDomainList(){
        List<IdentifiedVo> result = storageService.setDomainList(dcId, null);

        result.forEach(System.out::println);
    }


    @Test
    @DisplayName("디스크 디스크 프로파일 리스트")
    void setDiskProfile(){
        String domainId = "12d17014-a612-4b6e-a512-6ec4e1aadba6";
//        String domainId = "43a786b3-e37e-4545-ba1f-5e3ae0ca6f0f";
        List<IdentifiedVo> result = storageService.setDiskProfile(domainId);

        result.forEach(System.out::println);
    }

    
    

    @Test
    @DisplayName("디스크 이미지 생성")
    void addDiskImage() {
        ImageCreateVo image =
                ImageCreateVo.builder()
                        .alias("S1")
                        .size(2)
                        .description("test")
                        .domainId("12d17014-a612-4b6e-a512-6ec4e1aadba6")
                        .profileId("23ab66ac-26c3-4b21-ba78-691ec2a004df")
                        .sparse(true)
                        .wipeAfterDelete(false)
                        .share(false)
                        .backup(true)
                        .build();

        CommonVo<Boolean> result = storageService.addDiskImage(image);
        assertThat(result.getHead().getCode()).isEqualTo(201);
//        assertThat("Sdd").isEqualTo(image.getAlias());
    }


    @Test
    @DisplayName("디스크 이미지 수정창")
    void setDiskImage(){
        String diskId = "d1947b58-84df-4ff3-802a-b4a6497853c6";
        ImageCreateVo result = storageService.setDiskImage(diskId);

        System.out.println(result);
    }


    @Test
    @DisplayName("디스크 이미지 수정")
    void editDiskImage() {
        String diskId = "d1947b58-84df-4ff3-802a-b4a6497853c6";

        ImageCreateVo image =
                ImageCreateVo.builder()
                        .id(diskId)
                        .alias("Sdd1")
                        .size(3)
                        .appendSize(0)
                        .description("tes2t")
                        .sparse(false)
                        .wipeAfterDelete(false)
                        .share(false)
                        .backup(true)
                        .build();

        CommonVo<Boolean> result = storageService.editDiskImage(image);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }


    @Test
    @DisplayName("디스크 이미지 삭제")
    void deleteDisk() {
        String diskId = "b0ded29b-04fc-451b-a7a4-bfd436f47890";
        CommonVo<Boolean> result = storageService.deleteDisk(diskId);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("디스크 이동창")
    void setDiskMoveDomainList() {
        String diskId = "f89493dd-51f8-44bd-9bfb-4687f43c822c";
        List<IdentifiedVo> result = storageService.setDomainList(dcId, diskId);

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("디스크 이동")
    void moveDisk() {
        String diskId = "f89493dd-51f8-44bd-9bfb-4687f43c822c";
        String domainId = "43a786b3-e37e-4545-ba1f-5e3ae0ca6f0f";
        CommonVo<Boolean> result = storageService.moveDisk(diskId, domainId);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }


    @Test
    @DisplayName("디스크 복사")
    void copyDisk() {
        String diskId = "f89493dd-51f8-44bd-9bfb-4687f43c822c";
        String domainId = "12d17014-a612-4b6e-a512-6ec4e1aadba6";
        DiskVo diskVo =
            DiskVo.builder()
                    .id(diskId)
                    .alias("abcdTest2")
                    .domainVo(DomainVo.builder().id(domainId).build())
                .build();

        CommonVo<Boolean> result = storageService.copyDisk(diskVo);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }


    @Test
    @DisplayName("디스크 이미지 업로드")
    void uploadDisk() throws IOException {
        // test환경에서는 실패할 경우 있음
        String path = "C:/Users/deh22/Documents/Rocky-8.4-x86_64-minimal.iso";

        File file = new File(path);
        FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = fileItem.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

        ImageCreateVo iVo =
                ImageCreateVo.builder()
                        .alias("absc")
                        .description("test")
                        .domainId("12d17014-a612-4b6e-a512-6ec4e1aadba6")
                        .profileId("23ab66ac-26c3-4b21-ba78-691ec2a004df")
                        .sparse(false)
                        .wipeAfterDelete(false)
                        .share(false)
                        .backup(false)
                        .build();

        CommonVo<Boolean> result = storageService.uploadDisk(multipartFile, iVo);

    }


    @Test
    @DisplayName("스토리지 도메인 목록")
    void getDomainList() {
        List<DomainVo> result = storageService.getDomainList(dcId);
        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("호스트 목록")
    void setHostList() {
        List<IdentifiedVo> result = storageService.setHostList(dcId);

        result.forEach(System.out::println);
    }

    @Test
    void addDomain() {
    }

    @Test
    void deleteDomain() {
    }


    @Test
    @DisplayName("데이터센터 - 논리네트워크 목록")
    void getNetworkVoList() {
        List<NetworkVo> result = storageService.getNetworkVoList(dcId);

        result.forEach(System.out::println);
//        assertThat(8).isEqualTo(storageService.getNetworkVoList(dcId).size());
    }

    @Test
    @DisplayName("데이터센터 - 클러스터 목록")
    void getClusterVoList() {
        List<ClusterVo> result = storageService.getClusterVoList(dcId);

        result.forEach(System.out::println);
//        assertThat(1).isEqualTo(storageService.getClusterVoList(dcId).size());
    }

    @Test
    void getPermission() {
    }

    @Test
    void getEvent() {
    }


}