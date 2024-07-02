package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkClusterVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ovirt.engine.sdk4.types.InheritableBoolean;
import org.ovirt.engine.sdk4.types.MigrateOnError;
import org.ovirt.engine.sdk4.types.MigrationBandwidthAssignmentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItClusterServiceTest {
    @Autowired
    ItClusterService clusterService;

    String dcId = "9c72ff12-a5f3-11ee-941d-00163e39cb43";
    String defaultId = "9c7452ea-a5f3-11ee-93d2-00163e39cb43";
    String id = "99ce9472-cabc-4338-80f7-9fd3d9367027";

    @Test
    @DisplayName("클러스터 리스트 출력")
    void getList() {
        List<ClusterVo> result = clusterService.getList();

        result.forEach(System.out::println);
        assertThat(true).isEqualTo(result.stream().anyMatch(clusterVo -> clusterVo.getName().equals("Default")));
    }

    @Test
    @DisplayName("클러스터 생성 창 - dc 출력")
    void setDatacenterList() {
        List<DataCenterVo> result = clusterService.setDatacenterList();

        result.forEach(System.out::println);

        assertThat(3).isEqualTo(result.size());
        assertThat(true).isEqualTo(result.stream().anyMatch(dataCenterVo -> dataCenterVo.getName().equals("Default")));
    }

    @Test
    @DisplayName("클러스터 생성 창 - 네트워크 출력")
    void setNetworkList() {
//        String dcId = "";
        List<NetworkVo> result = clusterService.setNetworkList(dcId);

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("클러스터 생성 - 외부 공급자 X")
    void addCluster() {
        String randomName = RandomStringUtils.randomAlphabetic(2);

        ClusterCreateVo c =
                ClusterCreateVo.builder()
                        .datacenterId(dcId)
                        .name(randomName)
                        .cpuArc("X86_64")
                        .cpuType("Intel Nehalem Family")
//                        .description("")
//                        .comment("")
                        .networkId("00000000-0000-0000-0000-000000000009")
                        .biosType("Q35_SEA_BIOS")
                        .fipsMode("ENABLED")
                        .version("4.7")
                        .switchType("LEGACY")
                        .firewallType("FIREWALLD")
                        .logMaxMemory(95)
                        .logMaxType("PERCENTAGE")
                        .virtService(true)
                        .glusterService(false)
                        .recoveryPolicy(MigrateOnError.MIGRATE)
                        .bandwidth(MigrationBandwidthAssignmentMethod.AUTO)
                        .encrypted(InheritableBoolean.INHERIT)
                        .networkProvider(false)
                        .build();

        CommonVo<Boolean> result = clusterService.addCluster(c);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }


    @Test
    @DisplayName("클러스터 생성 - 외부 공급자 O")
    void addCluster2() {
        String randomName = RandomStringUtils.randomAlphabetic(2);

        ClusterCreateVo c =
                ClusterCreateVo.builder()
                        .datacenterId(dcId)
                        .name(randomName)
                        .cpuArc("X86_64")
                        .cpuType("Intel Nehalem Family")
//                        .description("")
//                        .comment("")
                        .networkId("00000000-0000-0000-0000-000000000009")
                        .biosType("CLUSTER_DEFAULT")
                        .fipsMode("ENABLED")
                        .version("4.7")
                        .switchType("LEGACY")
                        .firewallType("FIREWALLD")
                        .logMaxMemory(1024)
                        .logMaxType("ABSOLUTE_VALUE_IN_MB") // ABSOLUTE_VALUE_IN_MB, PERCENTAGE
                        .virtService(true)
                        .glusterService(false)
                        .recoveryPolicy(MigrateOnError.MIGRATE)
                        .bandwidth(MigrationBandwidthAssignmentMethod.AUTO)
                        .encrypted(InheritableBoolean.INHERIT)
                        .networkProvider(true)
                        .build();

        CommonVo<Boolean> result = clusterService.addCluster(c);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }


    @Test
    @DisplayName("클러스터 생성 - 외부 공급자 X, 중복이름")
    void addCluster3() {

        ClusterCreateVo c =
                ClusterCreateVo.builder()
                        .datacenterId(dcId)
                        .name("clustertest")
                        .cpuArc("X86_64")
                        .cpuType("Intel Nehalem Family")
//                        .description("")
//                        .comment("")
                        .networkId("00000000-0000-0000-0000-000000000009")
                        .biosType("CLUSTER_DEFAULT")
                        .fipsMode("ENABLED")
                        .version("4.7")
                        .switchType("LEGACY")
                        .firewallType("FIREWALLD")
                        .logMaxMemory(95)
                        .logMaxType("PERCENTAGE")
                        .virtService(true)
                        .glusterService(false)
                        .recoveryPolicy(MigrateOnError.MIGRATE)
                        .bandwidth(MigrationBandwidthAssignmentMethod.AUTO)
                        .encrypted(InheritableBoolean.INHERIT)
//                        .networkProvider(true)
                        .networkProvider(false)
                        .build();

        CommonVo<Boolean> result = clusterService.addCluster(c);

        assertThat(result.getHead().getCode()).isEqualTo(404);
    }


    @Test
    @DisplayName("클러스터 수정 창")
    void setCluster() {
        String id = "729bd062-f5b6-44f1-a3c2-64d81e2dbf1b";
        ClusterCreateVo c = clusterService.setCluster(id);

        System.out.println(c);
//        assertThat("MD").isEqualTo(c.getName());
    }

    @Test
    @DisplayName("클러스터 수정")
    void editCluster() {
        String randomName = RandomStringUtils.randomAlphabetic(2);
        String id = "4318aae2-29bd-4062-a2ba-75f814ca847c";

        ClusterCreateVo c =
                ClusterCreateVo.builder()
                        .id(id)
//                        .datacenterId(dcId)
                        .name(randomName)
                        .cpuArc("X86_64")
                        .cpuType("Intel Nehalem Family")
                        .description("asdf")
                        .networkId("00000000-0000-0000-0000-000000000009")
                        .biosType("CLUSTER_DEFAULT")
                        .fipsMode("ENABLED")
                        .version("4.7")
                        .firewallType("FIREWALLD")
                        .logMaxMemory(95)
                        .logMaxType("PERCENTAGE")
                        .virtService(true)
                        .glusterService(false)
                        .recoveryPolicy(MigrateOnError.MIGRATE)
                        .bandwidth(MigrationBandwidthAssignmentMethod.AUTO)
                        .encrypted(InheritableBoolean.INHERIT)
                        .networkProvider(false)
                        .build();

        CommonVo<Boolean> result = clusterService.editCluster(c);

        assertThat(result.getHead().getCode()).isEqualTo(201);
        assertThat("asdf").isEqualTo(c.getDescription());
        assertThat("Intel Nehalem Family").isEqualTo(c.getCpuType());
    }

    @Test
    @DisplayName("클러스터 수정 - 중복이름 ")
    void editCluster2() {
        String id = "4318aae2-29bd-4062-a2ba-75f814ca847c";

        ClusterCreateVo c =
                ClusterCreateVo.builder()
                        .id(id)
                        .name("Ni")
                        .cpuArc("X86_64")
                        .cpuType("Intel Nehalem Family")
                        .description("asdf")
                        .networkId("00000000-0000-0000-0000-000000000009")
                        .biosType("Q35_SEA_BIOS")
                        .fipsMode("ENABLED")
                        .version("4.7")
                        .firewallType("FIREWALLD")
                        .logMaxMemory(95)
                        .logMaxType("PERCENTAGE")
                        .virtService(true)
                        .glusterService(false)
                        .recoveryPolicy(MigrateOnError.MIGRATE)
                        .bandwidth(MigrationBandwidthAssignmentMethod.AUTO)
                        .encrypted(InheritableBoolean.INHERIT)
                        .networkProvider(false)
                        .build();

        CommonVo<Boolean> result = clusterService.editCluster(c);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("클러스터 삭제")
    void deleteCluster() {
        String did = "eafa0922-b6c8-428c-bc8d-25b14be10888";

        CommonVo<Boolean> result = clusterService.deleteCluster(did);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("클러스터 정보")
    void getInfo() {
        ClusterVo c = clusterService.getInfo(defaultId);

        System.out.println(c);
        assertThat("Default").isEqualTo(c.getName());
        assertThat(8).isEqualTo(c.getVmCnt());
    }

    @Test
    @DisplayName("클러스터 네트워크 목록")
    void getNetwork() {
        List<NetworkVo> result = clusterService.getNetwork(defaultId);

        result.forEach(System.out::println);
        assertThat(5).isEqualTo(result.size());
    }

    @Test
    @DisplayName("클러스터 네트워크 생성")
    void addNetwork(){
        String id = "729bd062-f5b6-44f1-a3c2-64d81e2dbf1b";

        List<VnicProfileVo> vnicProfileVoList = new ArrayList<>();
        vnicProfileVoList.add(VnicProfileVo.builder().name("aa").build());
        vnicProfileVoList.add(VnicProfileVo.builder().name("bb").build());

        NetworkCreateVo create =
                NetworkCreateVo.builder()
                        .name("tf")
                        .description("test")
                        .comment("test")
                        .usageVm(true)
                        .externalProvider(false)
                        .clusterVo(NetworkClusterVo.builder().id(id).connected(true).required(true).build())
                        .vnics(vnicProfileVoList)
                .build();

        CommonVo<Boolean> result = clusterService.addClusterNetwork(id, create);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("클러스터 네트워크 생성- 이름중복")
    void addNetwork2(){
        String id = "729bd062-f5b6-44f1-a3c2-64d81e2dbf1b";

        List<VnicProfileVo> vnicProfileVoList = new ArrayList<>();
        vnicProfileVoList.add(VnicProfileVo.builder().name("a").build());

        NetworkCreateVo create =
                NetworkCreateVo.builder()
                        .name("tes")
                        .description("test")
                        .comment("test")
                        .usageVm(true)
                        .externalProvider(false)
                        .clusterVo(NetworkClusterVo.builder().id(id).connected(true).required(true).build())
                        .vnics(vnicProfileVoList)
                        .build();

        CommonVo<Boolean> result = clusterService.addClusterNetwork(id, create);
        assertThat(result.getHead().getCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("클러스터 네트워크 관리 창")
    void setNetworkManage() {
        String id = "729bd062-f5b6-44f1-a3c2-64d81e2dbf1b";
        List<NetworkClusterVo> result = clusterService.setManageNetwork(id);

        result.forEach(System.out::println);
    }


    @Test
    @DisplayName("클러스터 네트워크 관리")
    void editNetworkManage() {
        String cid = "729bd062-f5b6-44f1-a3c2-64d81e2dbf1b";

        List<NetworkClusterVo> ncVoList = new ArrayList<>();

//        NetworkClusterVo nc =
//                NetworkClusterVo.builder()
//                        .id("")
//                        .networkUsageVo(
//                                NetworkUsageVo.builder()
//                                        .management()
//                                        .migration()
//                                        .display()
//                                        .gluster()
//                                        .defaultRoute()
//                                        .build()
//                        )
//                        .build();

//        List<NetworkClusterVo> result = clusterService.manageNetwork(cid, );

    }




    @Test
    @DisplayName("클러스터 호스트 목록")
    void getHost() {
        List<HostVo> result = clusterService.getHost(defaultId);

        result.forEach(System.out::println);
        assertThat(2).isEqualTo(result.size());
    }

    @Test
    @DisplayName("클러스터 가상머신 목록")
    void getVm() {
        List<VmVo> result = clusterService.getVm(defaultId);

        result.forEach(System.out::println);
        assertThat(8).isEqualTo(result.size());
        assertThat(true).isEqualTo(result.stream().anyMatch(vmVo -> vmVo.getName().equals("HostedEngine")));
    }


    //  선호도 그룹/ 레이블




    @Test
    @DisplayName("클러스터 권한 목록")
    void getPermission() {
        List<PermissionVo> result = clusterService.getPermission(id);

        assertThat(3).isEqualTo(result.size());
        result.stream().map(PermissionVo::getUser).forEach(System.out::println);
    }

    @Test
    void getEvent() {
        assertThat(1045).isEqualTo(clusterService.getEvent(defaultId).size());
    }
}