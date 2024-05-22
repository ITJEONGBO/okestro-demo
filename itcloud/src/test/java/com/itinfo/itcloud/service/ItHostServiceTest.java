package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.NicVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.create.HostCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItHostServiceTest {
    @Autowired ItHostService hostService;

    String defaultId = "f08baae8-2137-490c-bec2-fd00f67a37b9";

    @Test
    @DisplayName("호스트 목록")
    void getList() {
        List<HostVo> hostList = hostService.getList();

        assertThat(hostList.size()).isEqualTo(2);
        hostList.forEach(System.out::println);
    }

    @Test
    @DisplayName("호스트 생성 클러스터 리스트 출력")
    void setHostDefaultInfo() {
        List<ClusterVo> result = hostService.setHostDefaultInfo();

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("호스트 생성")
    void addHost() {
        HostCreateVo create =
                HostCreateVo.builder()
                        .name("host01.ititinfo.com")
                        .comment("192.168.0.81")
                        .hostIp("host01.ititinfo.com")
                        .sshPw("adminRoot!@#")
                        .spm(1)
                        .sshPort(22)
//                        .hostEngine(false)
                        .clusterId("9c7452ea-a5f3-11ee-93d2-00163e39cb43")
                        .build();

        CommonVo<Boolean> result = hostService.addHost(create);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("호스트 수정창")
    void setHost() {
        HostCreateVo result = hostService.setHost(defaultId);
        
        System.out.println(result);
    }

    @Test
    void editHost() {
    }

    @Test
    @DisplayName("호스트 삭제")
    void deleteHost() {
        String id = "1c8ed321-28e5-4f83-9e34-e13f9125f253";
        CommonVo<Boolean> result = hostService.deleteHost(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("호스트 유지보수")
    void deActive() {
        String id = "1c8ed321-28e5-4f83-9e34-e13f9125f253";
        hostService.deActive(id);

    }

    @Test
    void active() {
    }

    @Test
    void refresh() {
    }

    @Test
    void reStart() {
    }

    @Test
    void stop() {
        String id = "507b4118-346a-47be-bd3a-a15abc36d26a";

        CommonVo<Boolean> result = hostService.stop(id);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    void getInfo() {
    }

    @Test
    @DisplayName("호스트 가상머신 목록")
    void getVm() {
        String id = "507b4118-346a-47be-bd3a-a15abc36d26a";
        List<VmVo> vmVoList = hostService.getVm(id);

        vmVoList.forEach(System.out::println);
    }

    @Test
    @DisplayName("호스트 가상머신 실행")
    void startVm() {
        String vmId = "eec63849-5026-482c-8f05-1d8e419ef548";
        CommonVo<Boolean> result = hostService.startVm(vmId);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("호스트 가상머신 일시정지")
    void pauseVm() {
        String vmId = "eec63849-5026-482c-8f05-1d8e419ef548";
        CommonVo<Boolean> result = hostService.pauseVm(vmId);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("호스트 가상머신 종료")
    void shutdownVm() {
        String vmId = "eec63849-5026-482c-8f05-1d8e419ef548";
        CommonVo<Boolean> result = hostService.stopVm(vmId);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("호스트 가상머신 전원끔")
    void stopVm() {
        String vmId = "eec63849-5026-482c-8f05-1d8e419ef548";
        CommonVo<Boolean> result = hostService.shutdownVm(vmId);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("호스트 가상머신 마이그레이션")
    void migrationVm() {

    }

    @Test
    @DisplayName("호스트 가상머신 마이그레이션 취소")
    void migrationCancelVm() {

    }




    @Test
    void getNic() {
        String hostId = "1c8ed321-28e5-4f83-9e34-e13f9125f253";
        List<NicVo> nicList = hostService.getNic(hostId);

        nicList.forEach(System.out::println);
        assertThat(nicList.size()).isEqualTo(2);
    }

    @Test
    void getHostDevice() {
    }

    @Test
    void getPermission() {
    }

    @Test
    void setAffinityDefaultInfo() {
    }

    @Test
    void getAffinitylabels() {
    }

    @Test
    void addAffinitylabel() {
    }

    @Test
    void getAffinityLabel() {
    }

    @Test
    void editAffinitylabel() {
    }

    @Test
    void deleteAffinitylabel() {
    }

    @Test
    void getEvent() {
    }
}