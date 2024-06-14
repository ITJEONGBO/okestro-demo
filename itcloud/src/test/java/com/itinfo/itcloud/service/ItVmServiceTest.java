package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.*;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkFilterParameterVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.model.storage.DiskProfileVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.model.storage.VmDiskVo;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ovirt.engine.sdk4.types.DiskInterface;
import org.ovirt.engine.sdk4.types.InheritableBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItVmServiceTest {
    @Autowired ItVmService vmService;
    @Autowired ItAffinityService affinityService;

    String clusterId = "ae1ea51e-f642-11ee-bcc4-00163e4b3128";
    String defaultId = "74bbfae5-ada6-491e-9d3d-51ac8b50471e"; // HostedEngine

    @Test
    @DisplayName("가상머신 리스트")
    void getList() {
        List<VmVo> result = vmService.getList();

        assertThat(result.size()).isEqualTo(2);

        result.forEach(System.out::println);
    }


    @Test
    @DisplayName("생성 - 클러스터 목록")
    void setClusterList() {
        List<ClusterVo> result = vmService.setClusterList();

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(2);
    }



    @Test
    @DisplayName("생성 - 템플릿 목록")
    void setTemplateList() {
        List<TemplateVo> result = vmService.setTemplateList();

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("생성 - 디스크(연결) 목록")
    void setDiskList() {
        List<DiskVo> result = vmService.setDiskList();

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("생성 - 디스크(생성) 목록")
    void setDiskAttach() {
        List<DomainVo> result = vmService.setDiskAttach(clusterId);

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("생성 - vnic 목록")
    void setVnic() {
        List<VnicProfileVo> result = vmService.setVnic(clusterId);

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("생성 - 호스트 목록")    
    void setHostList() {
        List<IdentifiedVo> result = vmService.setHostList(clusterId);

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("생성 - 스토리지도메인 목록")
    void setStorageList() {
        List<IdentifiedVo> result = vmService.setStorageList();

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("생성 - cpu profile 목록")
    void setCpuProfileList() {
        List<IdentifiedVo> result = vmService.setCpuProfileList(clusterId);

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("생성 - iso 목록")
    void getIsoImage() {
        List<IdentifiedVo> result = vmService.setIsoImage();

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("생성 - 선호도 그룹 목록")
    void setAgList() {
        List<IdentifiedVo> result = vmService.setAgList(clusterId);

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("생성 - 선호도 레이블 목록")
    void setAlList() {
        List<IdentifiedVo> result = vmService.setAlList();

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(1);
    }







    @Test
    @DisplayName("가상머신 생성")
    void addVm() {
        String randomName = RandomStringUtils.randomAlphabetic(2);
        String randomDiskName = RandomStringUtils.randomAlphabetic(3);

        VmCreateVo vm =
            VmCreateVo.builder()
                    .clusterId(clusterId)
                    .templateId("00000000-0000-0000-0000-000000000000")
                    .os("rhel_8x64")
                    .chipsetType("Q35_OVMF")  // String.valueOf(BiosType.Q35_OVMF)
                    .option("SERVER")  // String.valueOf(VmType.SERVER)
                    .name(randomName)
                    .description("")
                    .comment("cc")
                    .stateless(false)
                    .startPaused(false)
                    .deleteProtected(false)
                    // 네트워크
                    .vnicList(
                            Arrays.asList(
                                    IdentifiedVo.builder()
                                            .id("0000000a-000a-000a-000a-000000000398")
                                            .build()
                            )
                    )
                    // 디스크
                    // 생성
                    .vDiskList(
                            Arrays.asList(
                                    VDiskVo.builder()
                                            .vDiskImageVo(
                                                    VDiskImageVo.builder()
                                                            .size(2)
                                                            .alias(randomDiskName)
                                                            .description("test")
                                                            .storageDomainId("12d17014-a612-4b6e-a512-6ec4e1aadba6") // hosted_storage
                                                            .allocationPolicy(true) // 할당정책: 씬
                                                            .diskProfile("23ab66ac-26c3-4b21-ba78-691ec2a004df")
                                                            .wipeAfterDelete(false)
                                                            .shareable(false)
                                                            .backup(true) // 증분백업 기본값 t
                                                            // 취소 활성화
                                                            .interfaces(DiskInterface.valueOf("VIRTIO_SCSI"))
                                                            .bootable(true) // 기본값:t
                                                            .readOnly(false)
                                                            .build()
                                            )
                                            .build()
                                    ,
                                    VDiskVo.builder()
                                            .vDiskImageVo(
                                                    VDiskImageVo.builder()
                                                            .diskId("ab422078-c7b0-49e4-90cc-b8b1f1befa99")
                                                            .interfaces(DiskInterface.valueOf("VIRTIO_SCSI"))
                                                            .bootable(false) // 기본값:t
                                                            .readOnly(false)
                                                            .build()
                                            )
                                            .build()
                            )
                    )
                    .vmSystemVo(
                            VmSystemVo.builder()
                                    .instanceType("small") //tiny 안됨 ( small, medium, xlarge)
                                    .memorySize(1024)
                                    .memoryMax(4096)
                                    .memoryActual(1024)
                                    .vCpuSocket(1)
                                    .vCpuSocketCore(1)
                                    .vCpuCoreThread(1)
                                    .timeOffset("Asia/Seoul")  // Asia/Seoul , Etc/GMT
                                    .build()
                    )
                    .vmInitVo(
                            VmInitVo.builder()
                                    .cloudInit(true)   // 일단 안됨
                                    .hostName(randomName) // 기본값은 해당 vm의 이름
                                    .timeStandard("Asia/Seoul")
                                    .script("")
                                    .build()
                    )
                    .vmHostVo(
                            VmHostVo.builder()
                                    .clusterHost(true)  // 클러스터 내 호스트
//                                    .clusterHost(false)  // 특정 호스트
//                                    .selectHostId(Arrays.asList("1c8ed321-28e5-4f83-9e34-e13f9125f253", "f08baae8-2137-490c-bec2-fd00f67a37b9"))
                                    .migrationEncrypt(InheritableBoolean.FALSE)
                                    .migrationMode("PINNED")  // 마이그레이션 안함
                                    .build()
                    )
                    .vmHaVo(
                            VmHaVo.builder()
                                    .ha(false) // 기본 false
//                                    .vmStorageDomainId("06faa572-f1ac-4874-adcc-9d26bb74a54d") // 스토리지 도메인
                                    // 재개동작?
                                    .priority(1)  // 우선순위: 기본 1(낮음)
                                    .build()
                    )
                    .vmResourceVo(
                            VmResourceVo.builder()
                                    .cpuProfileId("58ca604e-01a7-003f-01de-000000000250")
                                    .cpuShare(512)
                                    .cpuPinningPolicy("DEDICATED")
                                    .memoryBalloon(true)    // 시스템에서
                                    .multiQue(true)
//                                    .virtSCSIEnable(true)
                                    .build()
                    )
                    .vmBootVo(
                            VmBootVo.builder()
                                    .deviceList(Arrays.asList("HD", "CDROM"))
                                    .connId("a97b75d7-15fe-4168-ba5a-c52434936c70")
                                    .build()
                    )
                .build();

        CommonVo<Boolean> result = vmService.addVm(vm);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }





    @Test
    @DisplayName("가상머신 편집 창")
    void setEditVm() {
        String id = "364dfe92-fb24-4e97-8a7b-1e9de59f6be6";
        VmCreateVo result = vmService.setEditVm(defaultId);

        System.out.println(result);
        assertThat(result.getName()).isEqualTo("HostedEngine");
    }

    @Test
    @DisplayName("가상머신 편집")
    void editVm() {
        String id = "364dfe92-fb24-4e97-8a7b-1e9de59f6be6";

        VmCreateVo vm =
                VmCreateVo.builder()
                        .id(id)
                        .os("rhel_3")
                        .chipsetType("i440fx_sea_bios")
                        .build();

        CommonVo<Boolean> result = vmService.editVm(id, vm);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("가상머신 삭제")
    void deleteVm() {
        String id = "97f550a7-99d4-443a-9d1d-2571ede6efdc";
        boolean disk = false; //t: 디스크지움, f:디스크남김

        CommonVo<Boolean> result = vmService.deleteVm(id, disk);
        System.out.println("가상머신 삭제 disk: " + disk);

//        assertThat(result.getHead().getCode()).isEqualTo(404); // 삭제방지모드
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }



    @Test
    @DisplayName("가상머신 실행")
    void startVm() {
        String id = "6b2cf6fb-bc4f-444d-9a19-7b3766cf1dd9";
        CommonVo<Boolean> result = vmService.startVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
//        assertThat(result.getHead().getCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("가상머신 일시정지")
    void pauseVm() {
        String id = "6b2cf6fb-bc4f-444d-9a19-7b3766cf1dd9";
        CommonVo<Boolean> result = vmService.pauseVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
//        assertThat(result.getHead().getCode()).isEqualTo(404);
    }
    

    @Test
    @DisplayName("가상머신 전원끔")
    void stopVm() {
        String id = "";
        CommonVo<Boolean> result = vmService.stopVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
//        assertThat(result.getHead().getCode()).isEqualTo(404);
    }
    

    @Test
    @DisplayName("가상머신 종료")
    void shutdownVm() {
        String id = "6b2cf6fb-bc4f-444d-9a19-7b3766cf1dd9";
        CommonVo<Boolean> result = vmService.shutdownVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
//        assertThat(result.getHead().getCode()).isEqualTo(404);
    }
    
    @Test
    @DisplayName("가상머신 재부팅")
    void rebootVm() {
        String id = "6b2cf6fb-bc4f-444d-9a19-7b3766cf1dd9";
        CommonVo<Boolean> result = vmService.rebootVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
//        assertThat(result.getHead().getCode()).isEqualTo(404);
    }

    
    @Test
    @DisplayName("가상머신 재설정")
    void resetVm() {
        String id = "6b2cf6fb-bc4f-444d-9a19-7b3766cf1dd9";
        CommonVo<Boolean> result = vmService.resetVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
//        assertThat(result.getHead().getCode()).isEqualTo(404);
    }



    @Test
    @DisplayName("가상머신 스냅샷 창")
    void setSnapshotVm() {
//        String id = "21a4369f-c828-47d7-afcb-1248f7c2a787"; // 디스크없음
        String id = "e929923d-8710-47ef-bfbd-e281434eb8ee"; // 2

        List<SnapshotDiskVo> result = vmService.setSnapshot(defaultId);

        assertThat(result.size()).isEqualTo(1);
        result.forEach(System.out::println);
    }




    @Test
    @DisplayName("가상머신 스냅샷 생성")
    void snapshotVm() {
        String id = "6b2cf6fb-bc4f-444d-9a19-7b3766cf1dd9";

        List<SnapshotDiskVo> sList = new ArrayList<>();
        SnapshotDiskVo s =
                SnapshotDiskVo.builder()
                    .alias("")
                    .daId("e6bff67c-dc9e-4d3b-9e5e-79fcd5cac6dd")
                    .build();

        sList.add(s);

        SnapshotVo snapshotVo =
                SnapshotVo.builder()
                        .vmId(id)
                        .sDiskList(sList)
                        .build();
        CommonVo<Boolean> result = vmService.addSnapshot(snapshotVo);

//        assertThat(result.getHead().getCode()).isEqualTo(201);
    }






    @Test
    @DisplayName("가상머신 일반")
    void getInfo() {
        String id = "6b2cf6fb-bc4f-444d-9a19-7b3766cf1dd9";
        VmVo result = vmService.getInfo(defaultId);

        assertThat("HostedEngine").isEqualTo(result.getName());
        System.out.println(result);
    }

    @Test
    @DisplayName("가상머신 네트워크 인터페이스")
    void getNic() {
        String id = "266b7ca4-354b-4016-adbe-7324c932c8ca";
        List<NicVo> result = vmService.getNic(id);

//        assertThat(true).isEqualTo(result.stream().anyMatch(nicVo -> nicVo.getName().equals("vnet0")));
        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("가상머신 네트워크 인터페이스 생성")
    void addNic() {
        String id = "e929923d-8710-47ef-bfbd-e281434eb8ee";

        NicVo nicVo =
                NicVo.builder()
                        .name("sf4")
                        .vnicProfileVo(
                                VnicProfileVo.builder()
                                        .id("0000000a-000a-000a-000a-000000000398")
                                        .build()
                        )
                        .interfaces("VIRTIO")
                        .linkStatus(true)
                        .plugged(true)
//                        .macAddress("00:14:4a:23:67:56")
                        .nfVoList(
                                Arrays.asList(
                                        NetworkFilterParameterVo.builder()
                                                .name("s")
                                                .value("20")
                                                .build()
                                        ,
                                        NetworkFilterParameterVo.builder()
                                                .name("s2")
                                                .value("21")
                                                .build()
                                )
                        )
                        .build();

        CommonVo<Boolean> addNic = vmService.addNic(id, nicVo);

        assertThat(addNic.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("가상머신 nic 수정창")
    void setEditNic() {
        String id = "e929923d-8710-47ef-bfbd-e281434eb8ee";
        String nicId = "53658aa8-5d6d-4875-822c-c7ec61eb9ba5";
        NicVo nicVo = vmService.setEditNic(id,nicId);

        System.out.println(nicVo);
    }

    @Test
    @DisplayName("가상머신 nic 삭제")
    void deleteNic() {
        String id = "e929923d-8710-47ef-bfbd-e281434eb8ee";
        String nicId = "5699ab9d-7a49-49d1-9657-abbb561855e6";

        CommonVo<Boolean> delete = vmService.deleteNic(id, nicId);
    }

    @Test
    @DisplayName("가상머신 디스크")
    void getDisk() {
        String id = "e929923d-8710-47ef-bfbd-e281434eb8ee";
        List<VmDiskVo> result = vmService.getDisk(id);

//        assertThat(true).isEqualTo(result.stream().anyMatch(vmDiskVo -> vmDiskVo.getName().equals("he_virtio_disk")));
        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("가상머신 디스크-이미지 생성")
    void addDisk() {
        String randomName = RandomStringUtils.randomAlphabetic(2);
        String id = "d9e6d0f3-8c0c-4054-8726-761180d817cd";
//        String id = "e929923d-8710-47ef-bfbd-e281434eb8ee";

        VDiskImageVo image =
                VDiskImageVo.builder()
                        .size(2)
                        .alias(randomName)
                        .description("test")
                        .storageDomainId("06faa572-f1ac-4874-adcc-9d26bb74a54d")
                        .allocationPolicy(true) // 할당정책: 씬
                        .diskProfile("73247789-5b48-4684-bbd9-60f244de73d9")
                        .wipeAfterDelete(false)
                        .shareable(false)
                        .backup(true) // 증분백업 기본값 t
                        .active(true)

                        .interfaces(DiskInterface.valueOf("VIRTIO_SCSI"))
                        .bootable(false) // 기본값:t, 부팅되는 디스크는 한개만 존재할 수 있음
                        .readOnly(false)
                        .build();

        CommonVo<Boolean> result = vmService.addDiskImage(id, image);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("가상머신 디스크 삭제")
    void deleteDisk() {
        String id = "d9e6d0f3-8c0c-4054-8726-761180d817cd";
        String daId = "ca331d1a-915a-4713-bc72-16992bde558d";
        boolean type = true;   // true = 완전삭제
        
        CommonVo<Boolean> result = vmService.deleteDisk(id, daId, type);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("가상머신 디스크 활성화")
    void activeDisk(){
        String id = "d9e6d0f3-8c0c-4054-8726-761180d817cd";
        String daId = "9efac83d-9d6c-49e7-85f5-3d5120ae0cdf";

        vmService.activeDisk(id, daId);
    }

    @Test
    @DisplayName("가상머신 디스크 비활성화")
    void deactiveDisk(){
        String id = "d9e6d0f3-8c0c-4054-8726-761180d817cd";
        String daId = "9efac83d-9d6c-49e7-85f5-3d5120ae0cdf";

        vmService.deactivateDisk(id, daId);
    }

    @Test
    @DisplayName("가상머신 디스크 이동창")
    void setMoveDisk(){
        String id = "e929923d-8710-47ef-bfbd-e281434eb8ee";
        String daId = "d8ec6bd4-0306-4d4f-9ac6-f076e7a5c286";

        DiskVo result = vmService.setDiskMove(id, daId);

        System.out.println(result);
    }


    @Test
    @DisplayName("가상머신 디스크 이동창")
    void moveDisk() {
        String id = "e929923d-8710-47ef-bfbd-e281434eb8ee";
        String daId = "e708cb9c-287f-41c3-94e2-3e8102386313";
        DiskVo disk =
                DiskVo.builder()
                        .domainVo(
                                DomainVo.builder()
                                        .id("e6611ac1-35b0-42b9-b339-681a6d6cb538")
                                .build())
                        .profileVo(
                                DiskProfileVo.builder()
                                        .id("b5cbcbc2-43c0-45d4-8016-0c524dc7ccd4")
                                        .build()
                        )
                        .build();

        CommonVo<Boolean> result = vmService.moveDisk(id, daId, disk);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }





    @Test
    @DisplayName("가상머신 스냅샷")
    void getSnapshot() {
        String id = "e929923d-8710-47ef-bfbd-e281434eb8ee";  //b
        List<SnapshotVo> result = vmService.getSnapshot(id);

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("가상머신 스냅샷 삭제")
    void deleteSnapshot() {
        String id = "cf4efa5e-c753-4c0b-aebf-ae16fbeb6fc4";
        String snapId = "baf91adf-8794-4ee9-b623-d467c07a0f08";

        CommonVo<Boolean> result = vmService.deleteSnapshot(id, snapId);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }


    @Test
    @DisplayName("가상머신 선호도 그룹 목록")
    void getAgList() {
        String vmId = "c9c1c52d-d2a4-4f2a-93fe-30200f1e0bff";

        List<AffinityGroupVo> result = affinityService.getAffinitygroup(vmId, "vm");
        result.forEach(System.out::println);
    }


    @Test
    @DisplayName("가상머신 선호도 그룹 생성")
    void addAffinitygroup() {
        List<IdentifiedVo> hostList = new ArrayList<>();
        hostList.add(IdentifiedVo.builder().id("6a8e5257-0b2f-4b3c-b720-1d5eee1cbbfc").build());

        List<IdentifiedVo> vmList = new ArrayList<>();
        vmList.add(IdentifiedVo.builder().id(defaultId).build());

        AffinityGroupCreateVo ag =
                AffinityGroupCreateVo.builder()
                        .name("sga")
                        .description("asktestDescriptinn")
                        .priority(5)
                        .clusterId(defaultId)
                        .vmEnabled(false)
                        .vmEnforcing(false)
                        .vmPositive(true)
                        .hostEnabled(false)
                        .hostEnforcing(false)
                        .hostPositive(false)
                        .hostList(hostList)
                        .vmList(vmList)
                        .build();

        CommonVo<Boolean> result = affinityService.addAffinitygroup(defaultId, "vm", ag);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("가상머신 선호도 그룹 편집 창")
    void setEditAffinitygroup() {
        String agId = "92ad2ff1-2c7d-475d-80e6-8174e187cafe";
        AffinityGroupCreateVo group = affinityService.setEditAffinitygroup(defaultId, "vm", agId);

        System.out.println(group.toString());
    }


    @Test
    @DisplayName("가상머신 애플리케이션")
    void getApplication() {
        List<ApplicationVo> result = vmService.getApplication(defaultId);

        assertThat(2).isEqualTo(result.size());
        System.out.println(result.get(0).toString());
    }


    @Test
    @DisplayName("가상머신 게스트 정보")
    void getGuestInfo() {
        GuestInfoVo result = vmService.getGuestInfo(defaultId);
        System.out.println(result.toString());
    }

    @Test
    @DisplayName("가상머신 권한")
    void getPermission() {
        List<PermissionVo> result = vmService.getPermission(defaultId);

        assertThat(3).isEqualTo(result.size());
        result.stream().map(PermissionVo::getUser).forEach(System.out::println);
    }

    @Test
    @DisplayName("가상머신 이벤트")
    void getEvent() {
        List<EventVo> result = vmService.getEvent(defaultId);

        assertThat(3).isEqualTo(result.size());
    }

}