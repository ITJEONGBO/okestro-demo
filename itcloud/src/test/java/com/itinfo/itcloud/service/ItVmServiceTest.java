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
    void setVmSet() {
        List<VmSetVo> result = vmService.setVmSet();

        assertThat(2).isEqualTo(result.size());
        assertThat(14).isEqualTo(result.get(0).getVnicList().size());
        assertThat(6).isEqualTo(result.get(1).getVnicList().size());

        System.out.println(result.toString());
    }

    @Test
    void addVm() {
        VmCreateVo vm =
            VmCreateVo.builder()
                    .clusterId("5a25c18a-b584-4a1a-bcf8-9f5368ee5e41")
                    .templateId("00000000-0000-0000-0000-000000000000")
                    .os("rhel_8x64")
                    .chipsetType(String.valueOf(BiosType.Q35_SEA_BIOS))
                    .option(String.valueOf(VmType.SERVER))
                    .vmSystemVo(
                            VmSystemVo.builder()
                                    .memorySize(1024)
                                    .memoryMax(2048)
                                    .memoryActual(1024)
                                    .build()
                    )
                    .vmHostVo(
                            VmHostVo.builder()
                                    .hostId("1c8ed321-28e5-4f83-9e34-e13f9125f253")
                                    .build()
                    )
                    .name("testV")
                    .description("test")
                .build();

        CommonVo<Boolean> result = vmService.addVm(vm);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    void getVmCreate() {
        VmCreateVo result = vmService.getVmCreate(defaultId);
        System.out.println(result);
    }

    @Test
    void editVm() {
    }

    @Test
    void deleteVm() {
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