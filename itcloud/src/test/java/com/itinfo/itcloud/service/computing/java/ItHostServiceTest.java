/*
package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.network.NicVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.response.Res;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItHostServiceTest {
    @Autowired ItHostService hostService;

    private final String host1 = "971160c2-307d-463b-8f52-459561aa6996";    // 70 host01
    private final String host2 = "92b81da8-b160-4abf-859f-8005d07944eb";  // host2

    @Test
    @DisplayName("호스트 목록")
    void getList() {
        List<HostVo> hostList = hostService.findAll();
        assertThat(hostList.size()).isEqualTo(2);
        hostList.forEach(System.out::println);
    }

    @Test
    @DisplayName("호스트 생성 클러스터 목록 출력")
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
                        .sshPort(22 */
/*1237*//*
)
                        .sshPw("adminRoot!@#")
//                        .powerManagementActive(false) // 전원관리 활성화
                        .spm(1)
                        .hostEngine(false)
                        .build();

        Res<Boolean> result = hostService.add(create);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("호스트 편집창")
    void setHost() {
        HostCreateVo result = hostService.setHost(host1);
        
        System.out.println(result);
    }

    @Test
    @DisplayName("호스트 편집")
    void editHost() {
        HostCreateVo create =
                HostCreateVo.builder()
                        .id(host1)
                        .name("host01.ititinfo.local")
                        .comment("192.168.0.71 test")
                        .powerManagementActive(false) // 전원관리 활성화
                        .spm(5)
                        .hostEngine(false)
                        .build();

        Res<Boolean> result = hostService.update(create);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("호스트 삭제")
    void deleteHost() {
        List<String> ids = Arrays.asList("76a92f14-d3d1-4add-be6f-c7f42214cfef");
        Res<Boolean> result = hostService.remove(ids);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("호스트 유지보수")
    void deactiveHost() {
        Res<Boolean> result = hostService.deactiveHost(host1);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("호스트 활성")
    void activeHost() {
        Res<Boolean> result = hostService.activeHost(host1);
        assertThat(result.getHead().getCode()).isEqualTo(200);
//        assertThat(result.getHead().getCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("호스트 새로고침")
    void refreshHost() {
        Res<Boolean> result = hostService.refreshHost(host1);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("호스트 ssh-재시작")
    void reStartHost() throws UnknownHostException {
        String id = "a16955bd-ff57-4e6e-add5-c7d46d5315e9";

        Res<Boolean> result = hostService.reStartHost(id);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }


    @Test
    @DisplayName("호스트 정보")
    void getHost() {
        HostVo result = hostService.getHost(host1);

        System.out.println(result);
    }

    @Test
    @DisplayName("호스트 가상머신 목록")
    void getVm() {
        List<VmVo> vmVoList = hostService.findAllVmsFromHost(host1);

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
        List<NicVo> nicList = hostService.getNicsByHost(host2);

        nicList.forEach(System.out::println);
//        assertThat(nicList.size()).isEqualTo(2);
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
    @DisplayName("호스트 선호도 레이블 편집")
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
}*/
