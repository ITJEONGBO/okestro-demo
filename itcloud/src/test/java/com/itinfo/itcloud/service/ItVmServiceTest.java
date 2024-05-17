package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.*;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
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
    String defaultId = "c9c1c52d-d2a4-4f2a-93fe-30200f1e0bff";

    @Test
    @DisplayName("가상머신 리스트")
    void getList() {
        List<VmVo> result = vmService.getList();

        assertThat(9).isEqualTo(result.size());
        result.stream().map(VmVo::getUpTime).forEach(System.out::println);
    }


    @Test
    @DisplayName("가상머신 생성 창")
    void setVmSet() {
        List<VmSetVo> result = vmService.setVmSet();

        assertThat(2).isEqualTo(result.size());
        assertThat(14).isEqualTo(result.get(0).getVnicList().size());
        assertThat(6).isEqualTo(result.get(1).getVnicList().size());

        System.out.println(result.toString());
    }

    @Test
    @DisplayName("가상머신 생성창-리소스-cpuProfile")
    void setCpuProfile() {
        List<IdentifiedVo> result = vmService.getCpuProfileList("9c7452ea-a5f3-11ee-93d2-00163e39cb43");

        assertThat(result.size()).isEqualTo(2);
        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("가상머신 생성")
    void addVm() {
        String randomName = RandomStringUtils.randomAlphabetic(2);
        String randomDiskName = RandomStringUtils.randomAlphabetic(3);

        VmCreateVo vm =
            VmCreateVo.builder()
                    .clusterId("9c7452ea-a5f3-11ee-93d2-00163e39cb43")
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
//                    .vDiskList()

                    // 네트워크
                    .vnicList(
                            Arrays.asList(
                                    IdentifiedVo.builder()
                                            .id("0000000a-000a-000a-000a-000000000398")
                                            .build()
                            )
                    )
                    // 디스크
                    .vDiskList(
                            Arrays.asList(
                                    VDiskVo.builder()
                                            .vDiskImageVo(
                                                    VDiskImageVo.builder()
                                                            .size(2)
                                                            .alias(randomDiskName)
                                                            .description("test")
                                                            .storageDomainId("06faa572-f1ac-4874-adcc-9d26bb74a54d")
                                                            .allocationPolicy(true) // 할당정책: 씬
                                                            .diskProfile("73247789-5b48-4684-bbd9-60f244de73d9")
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
                                                            .size(2)
                                                            .alias(randomDiskName+"2")
                                                            .description("testsf")
                                                            .storageDomainId("e6611ac1-35b0-42b9-b339-681a6d6cb538")
                                                            .allocationPolicy(true) // 할당정책: 씬
                                                            .diskProfile("b5cbcbc2-43c0-45d4-8016-0c524dc7ccd4")
                                                            .wipeAfterDelete(false)
                                                            .shareable(false)
                                                            .backup(true) // 증분백업 기본값 t
                                                            // 취소 활성화

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
                                    .cloudInit(true)   // 일단 안됨
                                    .hostName(randomName) // 기본값은 해당 vm의 이름
                                    .timeStandard("Asia/Seoul")
                                    .script("")
                                    .build()
                    )
                    .vmHostVo(
                            VmHostVo.builder()
//                                    .clusterHost(true)  // 클러스터 내 호스트
                                    .clusterHost(false)  // 특정 호스트
                                    .selectHostId(Arrays.asList("1c8ed321-28e5-4f83-9e34-e13f9125f253", "f08baae8-2137-490c-bec2-fd00f67a37b9"))
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
                                    .cpuProfileId("25e675a8-0690-4dee-908e-1b3c3bd120fc")
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
        VmCreateVo result = vmService.setEditVm(id);

        System.out.println(result);
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
        // 삭제방지 모드 해제 란을 생성해야할듯
        String id = "ce196547-342c-4622-b099-2677e2b80597";
        CommonVo<Boolean> result = vmService.deleteVm(id);

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
//        String id = "6b2cf6fb-bc4f-444d-9a19-7b3766cf1dd9"; // 1
        String id = "e929923d-8710-47ef-bfbd-e281434eb8ee"; // 2
        List<SnapshotDiskVo> result = vmService.setSnapshot(id);

        assertThat(result.size()).isEqualTo(2);
        result.stream().map(SnapshotDiskVo::getAlias).forEach(System.out::println);
        result.stream().map(SnapshotDiskVo::getDaId).forEach(System.out::println);
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
    @DisplayName("가상머신 생성/ 새 네트워크 인터페이스 프로파일 목록 출력")
    void setVnic() {
        String clusterId = "9c7452ea-a5f3-11ee-93d2-00163e39cb43";
        List<VnicProfileVo> vnic = vmService.setVnic(clusterId);

        System.out.println(vnic.size());
        vnic.forEach(System.out::println);
    }


    @Test
    void setDisk() {
        List<DiskVo> con = vmService.setDiskConn();
        System.out.println(con);
    }

    @Test
    void setDomains(){
        String clusterId = "9c7452ea-a5f3-11ee-93d2-00163e39cb43";
        List<DomainVo> set = vmService.setDiskAttach(clusterId);
        System.out.println(set);
    }


    @Test
    @DisplayName("가상머신 일반")
    void getInfo() {
        String id = "6b2cf6fb-bc4f-444d-9a19-7b3766cf1dd9";
        VmVo result = vmService.getInfo(id);

        assertThat(true).isEqualTo(result.getName().equals("on20-ap03"));
        System.out.println(result);
    }

    @Test
    @DisplayName("가상머신 네트워크 인터페이스")
    void getNic() {
        List<NicVo> result = vmService.getNic(defaultId);

        assertThat("192.168.0.80").isEqualTo(result.get(0).getIpv4());
        assertThat(true).isEqualTo(result.stream().anyMatch(nicVo -> nicVo.getName().equals("vnet0")));

    }

    @Test
    @DisplayName("가상머신 디스크")
    void getDisk() {
        List<VmDiskVo> result = vmService.getDisk(defaultId);

        assertThat(true).isEqualTo(result.stream().anyMatch(vmDiskVo -> vmDiskVo.getName().equals("he_virtio_disk")));
        System.out.println(result.get(0).toString());
    }

    @Test
    @DisplayName("가상머신 스냅샷")
    void getSnapshot() {
        String id = "eec63849-5026-482c-8f05-1d8e419ef548";
        List<SnapshotVo> result = vmService.getSnapshot(id);

//        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("가상머신 애플리케이션")
    void getApplication() {
        List<ApplicationVo> result = vmService.getApplication(defaultId);

        assertThat(2).isEqualTo(result.size());
        System.out.println(result.get(0).toString());
    }

    @Test
    @DisplayName("가상머신 선호도 그룹")
    void getAffinitygroup() {
        List<AffinityGroupVo> result = vmService.getAffinitygroup(defaultId);

        assertThat(3).isEqualTo(result.size());
//        result.stream().map(AffinityGroupVo::getName).forEach(System.out::println);
    }

    @Test
    @DisplayName("가상머신 선호도 레이블")
    void getAffinitylabel() {
        String id = "6b2cf6fb-bc4f-444d-9a19-7b3766cf1dd9";

        List<AffinityLabelVo> result = vmService.getAffinitylabel(defaultId);

//        assertThat(0).isEqualTo(result.size());
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