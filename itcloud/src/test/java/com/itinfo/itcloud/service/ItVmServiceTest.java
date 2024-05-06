package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.VmCreateVo;
import com.itinfo.itcloud.model.create.VmHostVo;
import com.itinfo.itcloud.model.create.VmSystemVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.storage.VmDiskVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ovirt.engine.sdk4.types.BiosType;
import org.ovirt.engine.sdk4.types.VmType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

        assertThat(10).isEqualTo(result.size());
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
    @DisplayName("가상머신 생성")
    void addVm() {
        VmCreateVo vm =
            VmCreateVo.builder()
                    .clusterId("9c7452ea-a5f3-11ee-93d2-00163e39cb43")
                    .templateId("00000000-0000-0000-0000-000000000000")
                    .os("rhel_8x64")
                    .chipsetType(String.valueOf(BiosType.Q35_OVMF))
                    .option(String.valueOf(VmType.SERVER))

                    .name("f")
                    .description("기본생성, 메모리")
                    .comment("dSA")
                    .stateless(false)
                    .startPaused(false)
                    .deleteProtected(false)
//                    .vDiskList()
//                    .vnicList()
                    .vmSystemVo(
                            VmSystemVo.builder()
                                    .instanceType("large") //tiny 안됨
                                    .memorySize(2048)
                                    .memoryMax(2048)
                                    .memoryActual(2048)
                                    .vCpuSocket(1)
                                    .vCpuSocketCore(2)
                                    .vCpuCoreThread(1)
                                    .build()
                    )
//                    .vmInitVo()
                    .vmHostVo(
                            VmHostVo.builder()
                                    .build()
                    )
                .build();

        CommonVo<Boolean> result = vmService.addVm(vm);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("가상머신 편집 창")
    void setEditVm() {
        String id = "701c0c48-51c0-4bd1-930a-81184e0062d7";
        VmCreateVo result = vmService.setEditVm(id);
        System.out.println(result);
    }

    @Test
    @DisplayName("가상머신 편집")
    void editVm() {
        String id = "";
        VmCreateVo vm = VmCreateVo.builder().build();
        CommonVo<Boolean> result = vmService.editVm(id, vm);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("가상머신 삭제")
    void deleteVm() {
        String id = "9391b3a0-2adf-4a4c-8d5c-7b4a1352bfc9";
        CommonVo<Boolean> result = vmService.deleteVm(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }





    @Test
    @DisplayName("가상머신 일반")
    void getInfo() {
        VmVo result = vmService.getInfo(defaultId);

        assertThat(true).isEqualTo(result.getName().equals("HostedEngine"));
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