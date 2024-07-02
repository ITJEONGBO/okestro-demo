package com.itinfo.itcloud.service.computing;

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
    @Autowired
    ItVmService vmService;
    @Autowired
    ItAffinityService affinityService;

    String clusterId = "ae1ea51e-f642-11ee-bcc4-00163e4b3128";
    String defaultId = "74bbfae5-ada6-491e-9d3d-51ac8b50471e"; // HostedEngine

    @Test
    @DisplayName("가상머신 리스트")
    void getList() {
        List<VmVo> result = vmService.getList();

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(5);
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
        assertThat(result.size()).isEqualTo(3);
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


    //-----------------------생성------------------------------------

    @Test
    @DisplayName("가상머신 생성 - 기본")
    void addVm1() {
        String randomName = RandomStringUtils.randomAlphabetic(2);

        VmCreateVo vm =
            VmCreateVo.builder()
                .clusterId(clusterId)
                .templateId("00000000-0000-0000-0000-000000000000")
                .os("other_linux")
                .chipsetType("Q35_SEA_BIOS")  // String.valueOf(BiosType.Q35_OVMF)
                .option("SERVER")  // String.valueOf(VmType.SERVER)
                .name(randomName)
                .description("")
                .comment("")
                .stateless(false)
                .startPaused(false)
                .deleteProtected(false)
                .vnicList(
                    Arrays.asList(VnicProfileVo.builder().id("0000000a-000a-000a-000a-000000000398").build())
                )
                .vDiskList(
                    Arrays.asList(
                        VDiskVo.builder().vDiskImageVo(
                            VDiskImageVo.builder()
                                .size(200)
                                .alias(randomName + 1)
                                .description("")
                                .interfaces(DiskInterface.valueOf("VIRTIO_SCSI")) // 인터페이스
                                .storageDomainId("12d17014-a612-4b6e-a512-6ec4e1aadba6") // hosted_storage
                                .allocationPolicy(true) // 할당정책: 씬
                                .diskProfile("23ab66ac-26c3-4b21-ba78-691ec2a004df")

                                .wipeAfterDelete(false)
                                .bootable(true) // 기본값:t
                                .shareable(false)
                                .readOnly(false) // 읽기전용
                                // 취소 활성화
                                .backup(true) // 증분백업 기본값 t
                                .build()
                        ).build()
                    )
                )
                .vmSystemVo(
                    VmSystemVo.builder()
                        .instanceType("") //tiny 안됨 ( small, medium, xlarge)
                        .memorySize(2048)
                        .memoryMax(8192)
                        .memoryActual(2048)
                        .vCpuSocket(2)
                        .vCpuSocketCore(1)
                        .vCpuCoreThread(1)
                        .timeOffset("Asia/Seoul")  // Asia/Seoul , Etc/GMT
                        .build()
                )
                .vmInitVo(
                    VmInitVo.builder()
                        .cloudInit(false)   // 일단 안됨
                        .build()
                )
                .vmHostVo(
                    VmHostVo.builder()
                        .clusterHost(false)  // 특정 호스트
                        .hostId(
                                Arrays.asList(IdentifiedVo.builder().id("a16955bd-ff57-4e6e-add5-c7d46d5315e9").build())
                        )
                        .migrationEncrypt(InheritableBoolean.INHERIT)
                        .migrationMode("MIGRATABLE")  // 마이그레이션
                        .build()
                )
                .vmHaVo(
                    VmHaVo.builder()
                        .ha(false) // 기본 false
                        .priority(1)  // 우선순위: 기본 1(낮음)
                        .build()
                )
                .vmResourceVo(
                    VmResourceVo.builder()
                        .cpuProfileId("58ca604e-01a7-003f-01de-000000000250") // 클러스터 밑에 있는 cpu profile
                        .cpuShare(0) // 비활성화됨 0
                        .cpuPinningPolicy("NONE")
                        .memoryBalloon(true)    // 메모리 balloon 활성화
                        .multiQue(true) // 멀티 큐 사용
                        .virtSCSIEnable(true)  // virtIO-SCSI 활성화
                        .build()
                )
                .vmBootVo(
                    VmBootVo.builder()
                        .firstDevice("HD")
                        .connId("3516d9c2-12c1-4c38-97a2-0e43180220f6")
                        .build()
                )
                .build();

        CommonVo<Boolean> result = vmService.addVm(vm);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }








    @Test
    @DisplayName("가상머신 생성 - 네트워크")
    void addVm2() {
        String randomName = RandomStringUtils.randomAlphabetic(2);
        String randomDiskName = RandomStringUtils.randomAlphabetic(3);

        List<VDiskVo> diskList = Arrays.asList(
                VDiskVo.builder().vDiskImageVo(
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
                ).build()
//                ,
//                // 연결
//                VDiskVo.builder().vDiskImageVo(VDiskImageVo.builder()
//                                        .diskId("ab422078-c7b0-49e4-90cc-b8b1f1befa99")
//                                        .interfaces(DiskInterface.valueOf("VIRTIO_SCSI"))
//                                        .bootable(false) // 기본값:t
//                                        .readOnly(false)
//                                        .build()
//                ).build()
        );

//        List<VnicProfileVo> vnicList = Arrays.asList(
//                VnicProfileVo.builder().id("0000000a-000a-000a-000a-000000000398").build(),
//                VnicProfileVo.builder().id("7f429cf2-e7ec-497c-8c48-6ba0975f6383").build(),
//                VnicProfileVo.builder().id("41a5558d-3fbd-4348-ab9b-f66de38fe720").build()
//        );

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
                        .vnicList(null/*vnicList*/)     // 네트워크
                        .vDiskList(diskList)    // 디스크
                        .vmSystemVo(
                                VmSystemVo.builder()
                                        .instanceType("") //tiny 안됨 ( small, medium, xlarge)
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
                                        .cloudInit(false)   // 일단 안됨
                                        .hostName(randomName) // 기본값은 해당 vm의 이름
                                        .timeStandard("Asia/Seoul")
                                        .script("")
                                        .build()
                        )
                        .vmHostVo(
                                VmHostVo.builder()
//                                        .clusterHost(true)  // 클러스터 내 호스트
                                    .clusterHost(false)  // 특정 호스트
                                    .hostId(
                                        Arrays.asList(
                                            IdentifiedVo.builder().id("a16955bd-ff57-4e6e-add5-c7d46d5315e9").build(),
                                            IdentifiedVo.builder().id("92b81da8-b160-4abf-859f-8005d07944eb").build())
                                    )
                                    .migrationEncrypt(InheritableBoolean.FALSE)
                                    .migrationMode("PINNED")  // 마이그레이션 안함
                                    .build()
                        )
                        .vmHaVo(
                                VmHaVo.builder()
//                                        .ha(false) // 기본 false
                                        .ha(true) // 기본 false
                                    .vmStorageDomainId("12d17014-a612-4b6e-a512-6ec4e1aadba6") // 스토리지 도메인
                                        // 재개동작?
                                        .priority(50)  // 우선순위: 기본 1(낮음)
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
                                        .firstDevice("HD")
//                                        .secDevice("CDROM")
                                        .connId("3516d9c2-12c1-4c38-97a2-0e43180220f6")
                                        .build()
                        )
                        .build();

        CommonVo<Boolean> result = vmService.addVm(vm);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }
























    @Test
    @DisplayName("가상머신 편집 창")
    void setVm() {
        String id = "3f5fc65c-8eba-4b9f-a321-ecfc9a58483c";
        VmCreateVo result = vmService.setVm(id);

        System.out.println(result);
        assertThat(result.getName()).isEqualTo("sT");
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

        CommonVo<Boolean> result = vmService.editVm(vm);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("가상머신 삭제 - 삭제방지 모드 X")
    void deleteVm() {
        String id = "d1d753d1-6ceb-43c7-b260-242671cbe9e4";
        boolean disk = true; //t: 디스크지움, f:디스크남김
        CommonVo<Boolean> result = vmService.deleteVm(id, disk);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("가상머신 삭제 - 삭제방지 모드 O")
    void deleteVm2() {
        String id = "293642d3-349f-4550-bced-01bdcc9baf64";
        boolean disk = true; //t: 디스크지움, f:디스크남김
        CommonVo<Boolean> result = vmService.deleteVm(id, disk);

        assertThat(result.getHead().getCode()).isEqualTo(404); // 삭제방지모드
    }





    @Test
    @DisplayName("가상머신 실행")
    void startVm() {
        String id = "89455fd7-770a-427a-962d-ee5782db5615";
        CommonVo<Boolean> result = vmService.startVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("가상머신 일시정지")
    void pauseVm() {
        String id = "89455fd7-770a-427a-962d-ee5782db5615";
        CommonVo<Boolean> result = vmService.pauseVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }
    

    @Test
    @DisplayName("가상머신 전원끔(강제종료)")
    void stopVm() {
        String id = "89455fd7-770a-427a-962d-ee5782db5615";
        CommonVo<Boolean> result = vmService.powerOffVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }
    

    @Test
    @DisplayName("가상머신 종료")
    void shutdownVm() {
        String id = "89455fd7-770a-427a-962d-ee5782db5615";
        CommonVo<Boolean> result = vmService.shutDownVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }
    
    @Test
    @DisplayName("가상머신 재부팅")
    void rebootVm() {
        String id = "89455fd7-770a-427a-962d-ee5782db5615";
        CommonVo<Boolean> result = vmService.rebootVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    
    @Test
    @DisplayName("가상머신 재설정")
    void resetVm() {
        String id = "89455fd7-770a-427a-962d-ee5782db5615";
        CommonVo<Boolean> result = vmService.resetVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }





    @Test
    @DisplayName("가상머신 마이그레이션할 목록 - 클러스터 내 호스트")
    void migrateHostList() {
        String id = "7a2e21ea-303f-444d-b3ae-964fc04c3919";
        List<IdentifiedVo> result = vmService.migrateHostList(id);

        result.forEach(System.out::println);
    }


    @Test
    @DisplayName("가상머신 마이그레이션")
    void migrateVm() {
        String id = "d1d753d1-6ceb-43c7-b260-242671cbe9e4";
        String hostId = "a16955bd-ff57-4e6e-add5-c7d46d5315e9";
        CommonVo<Boolean> result = vmService.migrateVm(id, hostId);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("가상머신 ova 내보내기")
    void exportVm() {
        String id = "7a2e21ea-303f-444d-b3ae-964fc04c3919";
        VmExportVo exportVo =
                VmExportVo.builder()
                        .vmId(id)
                        .hostName("host01.ititinfo.local")
                        .directory("/root")
                        .fileName("testOva.ova")
                        .build();

        CommonVo<Boolean> result = vmService.exportOvaVm(exportVo);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }



    @Test
    @DisplayName("가상머신 일반")
    void getInfo() {
        String id = "89455fd7-770a-427a-962d-ee5782db5615";
        VmVo result = vmService.getInfo(defaultId);

//        assertThat("HostedEngine").isEqualTo(result.getName());
        System.out.println(result);
    }



    @Test
    @DisplayName("가상머신 네트워크 인터페이스")
    void getNic() {
        String id = "266b7ca4-354b-4016-adbe-7324c932c8ca";
        List<NicVo> result = vmService.getNic(defaultId);

        result.forEach(System.out::println);
//        assertThat(true).isEqualTo(result.stream().anyMatch(nicVo -> nicVo.getName().equals("vnet0")));
    }

    @Test
    @DisplayName("가상머신 네트워크 인터페이스 생성")
    void addNic() {
        String id = "89455fd7-770a-427a-962d-ee5782db5615";
        String profileId = "0000000a-000a-000a-000a-000000000398";

        NicVo nicVo =
            NicVo.builder()
                .name("sfa4")
                .vnicProfileVo(VnicProfileVo.builder().id(profileId).build())
                .interfaces("VIRTIO")
                .linkStatus(true)
                .plugged(true)
                .macAddress("00:14:4a:23:67:56")
                .nfVoList(
                    Arrays.asList(
                        NetworkFilterParameterVo.builder().name("s").value("20").build(),
                        NetworkFilterParameterVo.builder().name("s2").value("21").build()
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
//        String id = "3f342124-8aa9-473b-b96a-043e985b5742";
        String id = "7a2e21ea-303f-444d-b3ae-964fc04c3919";
        List<SnapshotVo> result = vmService.getSnapshot(id);

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("가상머신 스냅샷 창")
    void setSnapshotVm() {
        String id = "3f342124-8aa9-473b-b96a-043e985b5742";
        List<SnapshotDiskVo> result = vmService.setSnapshot(id);

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("가상머신 스냅샷 생성")
    void snapshotVm() {
        String id = "3f342124-8aa9-473b-b96a-043e985b5742";

        SnapshotVo snapshotVo =
                SnapshotVo.builder()
                        .vmId(id)
                        .description("asdf")
                        .persistMemory(true)    // 메모리 저장 X
                        .sDiskList(
                            Arrays.asList(
                                SnapshotDiskVo.builder().daId("24a47eb3-2a22-417e-aa5c-4bacbe3bfd53").build()
//                                SnapshotDiskVo.builder().daId("0450dd6b-1325-4672-8323-9b6fb1802226").build()
                            )
                        )
                        .build();
        System.out.println(snapshotVo);

        CommonVo<Boolean> result = vmService.addSnapshot(snapshotVo);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("가상머신 스냅샷 삭제")
    void deleteSnapshot() {
        String id = "3f342124-8aa9-473b-b96a-043e985b5742";
        String snapId = "74e56ce0-407d-4059-90a1-f5c76f0b7be8";

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
        String id = "7a2e21ea-303f-444d-b3ae-964fc04c3919"; // centos
        List<IdentifiedVo> result = vmService.getApplication(id);

        result.forEach(System.out::println);
//        assertThat(2).isEqualTo(result.size());
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


    @Test
    @DisplayName("가상머신 콘솔")
    void getConsole(){

    }

}