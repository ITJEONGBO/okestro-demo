package com.itinfo.itcloud.service.computing;

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

import java.net.UnknownHostException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItHostServiceTest {
    @Autowired ItHostService hostService;

    private final String defaultId = "a16955bd-ff57-4e6e-add5-c7d46d5315e9";    // 70 host01


    @Test
    @DisplayName("호스트 목록")
    void getList() {
        List<HostVo> hostList = hostService.getList();

        assertThat(hostList.size()).isEqualTo(2);
        hostList.forEach(System.out::println);
    }

    @Test
    @DisplayName("호스트 생성 클러스터 리스트 출력")
    void setClusterList() {
        List<ClusterVo> result = hostService.setClusterList();

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("호스트 생성")
    void addHost() {
        HostCreateVo create =
                HostCreateVo.builder()
                        .clusterId("ae1ea51e-f642-11ee-bcc4-00163e4b3128")
                        .name("host01.ititinfo.local")
                        .comment("192.168.0.71")
                        .hostIp("host01.ititinfo.local")
                        .sshPort(22 /*1237*/)
                        .sshPw("adminRoot!@#")
//                        .powerManagementActive(false) // 전원관리 활성화
                        .spm(1)
                        .hostEngine(false)
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
    @DisplayName("호스트 수정")
    void editHost() {
        HostCreateVo create =
                HostCreateVo.builder()
                        .id(defaultId)
                        .name("host01.ititinfo.local")
                        .comment("192.168.0.71 test")
                        .powerManagementActive(false) // 전원관리 활성화
                        .spm(5)
                        .hostEngine(false)
                        .build();

        CommonVo<Boolean> result = hostService.editHost(create);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("호스트 삭제")
    void deleteHost() {
        String id = "704178f0-efa5-41d2-b14a-d8486dc3a252";
        CommonVo<Boolean> result = hostService.deleteHost(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("호스트 유지보수")
    void deactiveHost() {
//        String id = "6a8e5257-0b2f-4b3c-b720-1d5eee1cbbfc";

        CommonVo<Boolean> result = hostService.deactiveHost(defaultId);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("호스트 활성")
    void activeHost() {
        String id = "6a8e5257-0b2f-4b3c-b720-1d5eee1cbbfc";

        CommonVo<Boolean> result = hostService.activeHost(defaultId);
        assertThat(result.getHead().getCode()).isEqualTo(200);
//        assertThat(result.getHead().getCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("호스트 새로고침")
    void refreshHost() {
        CommonVo<Boolean> result = hostService.refreshHost(defaultId);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("호스트 ssh-재시작")
    void reStartHost() throws UnknownHostException {
        String id = "a16955bd-ff57-4e6e-add5-c7d46d5315e9";

        CommonVo<Boolean> result = hostService.reStartHost(id);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }


    @Test
    @DisplayName("호스트 정보")
    void getHost() {
        HostVo result = hostService.getHost(defaultId);

        System.out.println(result);
    }

    @Test
    @DisplayName("호스트 가상머신 목록")
    void getVm() {
        String id = "92b81da8-b160-4abf-859f-8005d07944eb";
        List<VmVo> vmVoList = hostService.getVm(id);

        vmVoList.forEach(System.out::println);
    }

//    @Test
//    @DisplayName("호스트 가상머신 실행")
//    void startVm() {
//        String vmId = "266b7ca4-354b-4016-adbe-7324c932c8ca";
//        CommonVo<Boolean> result = vmService.startVm(vmId);
//
//        assertThat(result.getHead().getCode()).isEqualTo(200);
//    }
//
//    @Test
//    @DisplayName("호스트 가상머신 일시정지")
//    void pauseVm() {
//        String vmId = "266b7ca4-354b-4016-adbe-7324c932c8ca";
//        CommonVo<Boolean> result = vmService.pauseVm(vmId);
//
//        assertThat(result.getHead().getCode()).isEqualTo(200);
//    }
//
//    @Test
//    @DisplayName("호스트 가상머신 종료")
//    void shutdownVm() {
//        String vmId = "eec63849-5026-482c-8f05-1d8e419ef548";
//        CommonVo<Boolean> result = vmService.stopVm(vmId);
//
//        assertThat(result.getHead().getCode()).isEqualTo(200);
//    }
//
//    @Test
//    @DisplayName("호스트 가상머신 전원끔")
//    void stopVm() {
//        String vmId = "eec63849-5026-482c-8f05-1d8e419ef548";
//        CommonVo<Boolean> result = vmService.shutdownVm(vmId);
//
//        assertThat(result.getHead().getCode()).isEqualTo(200);
//    }
//
//    @Test
//    @DisplayName("호스트 가상머신 마이그레이션")
//    void migrationVm() {
//
//    }
//
//    @Test
//    @DisplayName("호스트 가상머신 마이그레이션 취소")
//    void migrationCancelVm() {
//
//    }




    @Test
    void getNic() {
        String hostId = "1c8ed321-28e5-4f83-9e34-e13f9125f253";
        List<NicVo> nicList = hostService.getNic(hostId);

        nicList.forEach(System.out::println);
        assertThat(nicList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("호스트 장치")
    void getHostDevice() {
    }

    @Test
    @DisplayName("호스트 권한")
    void getPermission() {
    }

    @Test
    @DisplayName("호스트 ")
    void setAffinityDefaultInfo() {
    }

    @Test
    @DisplayName("호스트 선호도 레이블")
    void getAffinitylabels() {
    }

    @Test
    @DisplayName("호스트 선호도 레이블 생성")
    void addAffinitylabel() {
    }

    @Test
    @DisplayName("호스트 선호도 레이블 받아오기")
    void getAffinityLabel() {
    }

    @Test
    @DisplayName("호스트 선호도 레이블 수정")
    void editAffinitylabel() {
    }

    @Test
    @DisplayName("호스트 선호도 레이블 삭제")
    void deleteAffinitylabel() {
    }

    @Test
    @DisplayName("호스트 이벤트")
    void getEvent() {
    }
}