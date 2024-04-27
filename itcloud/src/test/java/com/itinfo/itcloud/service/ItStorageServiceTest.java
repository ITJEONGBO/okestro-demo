package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.storage.ImageCreateVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItStorageServiceTest {
    @Autowired ItStorageService storageService;

    String dcId = "9c72ff12-a5f3-11ee-941d-00163e39cb43";

    @Test
    @DisplayName("디스크 목록 출력")
    @Order(1)
    void getDiskList() {
        assertThat(22).isEqualTo(storageService.getDiskList(dcId).size());
    }

    @Test
    void setDiskImage() {

        storageService.setDiskImage(dcId);
    }

    @Test
    @DisplayName("디스크 이미지 생성")
    void addDiskImage() {
        ImageCreateVo image =
                ImageCreateVo.builder()
                        .name("Sd")
                        .size(2)
                        .description("test")
                        .domainId("e6611ac1-35b0-42b9-b339-681a6d6cb538")
                        .profileId("b5cbcbc2-43c0-45d4-8016-0c524dc7ccd4")
                        .sparse(false)
                        .wipeAfterDelete(false)
                        .share(false)
                        .backup(true)
                        .build();

        CommonVo<Boolean> result = storageService.addDiskImage(image);
        assertThat(result.getHead().getCode()).isEqualTo(201);

        assertThat("Sd").isEqualTo(image.getName());
        assertThat("test").isEqualTo(image.getDescription());
        assertThat("e6611ac1-35b0-42b9-b339-681a6d6cb538").isEqualTo(image.getDomainId());
    }

    @Test
    @DisplayName("디스크 이미지 수정")
    void editDiskImage() {
        String id = "90fc30fb-7f77-48b4-9897-df7baf1dd6fa";

        ImageCreateVo image =
                ImageCreateVo.builder()
                        .id(id)
                        .name("Sd")
                        .size(2)
                        .description("test")
                        .domainId("e6611ac1-35b0-42b9-b339-681a6d6cb538")
                        .profileId("b5cbcbc2-43c0-45d4-8016-0c524dc7ccd4")
                        .sparse(false)
                        .wipeAfterDelete(false)
                        .share(false)
                        .backup(true)
                        .build();

        CommonVo<Boolean> result = storageService.editDiskImage(image);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    void setDiskLun() {
    }

    @Test
    void addDiskLun() {
    }

    @Test
    void editDiskLun() {
    }

    @Test
    @DisplayName("디스크 이미지 삭제")
    void deleteDisk() {
        String diskId = "865386fc-350b-4d90-8b64-60c27b45068a";

        CommonVo<Boolean> result = storageService.deleteDisk(diskId);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("디스크 이동창")
    void setDiskMove() {
    }

    @Test
    @DisplayName("디스크 이동")
    void moveDisk() {
    }

    @Test
    void setDiskCopy() {
    }

    @Test
    void copyDisk() {
    }

    @Test
    void uploadDisk() {

    }

    @Test
    void cancelUpload() {
    }

    @Test
    void pauseUpload() {
    }

    @Test
    void resumeUpload() {
    }

    @Test
    void downloadDisk() {
    }

    @Test
    void getDomainList() {
    }

    @Test
    void setDomain() {
    }

    @Test
    void addDomain() {
    }

    @Test
    void deleteDomain() {
    }

    @Test
    void getVolumeVoList() {
    }

    @Test
    @DisplayName("데이터센터 - 스토리지 목록")
    void getStorageList() {
        assertThat(2).isEqualTo(storageService.getStorageList(dcId).size());
    }

    @Test
    @DisplayName("데이터센터 - 논리네트워크 목록")
    void getNetworkVoList() {
        assertThat(8).isEqualTo(storageService.getNetworkVoList(dcId).size());
    }

    @Test
    @DisplayName("데이터센터 - 클러스터 목록")
    void getClusterVoList() {
        assertThat(1).isEqualTo(storageService.getClusterVoList(dcId).size());
    }

    @Test
    void getPermission() {
    }

    @Test
    void getEvent() {
    }


}