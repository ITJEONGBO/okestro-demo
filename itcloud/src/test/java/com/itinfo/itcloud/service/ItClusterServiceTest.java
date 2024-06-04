package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkClusterVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItClusterServiceTest {
    @Autowired ItClusterService clusterService;
    @Autowired ItAffinityService affinityService;

    String dcId = "9c72ff12-a5f3-11ee-941d-00163e39cb43";
    String id = "99ce9472-cabc-4338-80f7-9fd3d9367027";
    String defaultId = "9c7452ea-a5f3-11ee-93d2-00163e39cb43";

    @Test
    @DisplayName("클러스터 리스트 출력")
    void getList() {
        List<ClusterVo> result = clusterService.getList();

        result.forEach(System.out::println);
        assertThat(2).isEqualTo(result.size());
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
    void setEditCluster() {
        String id = "ff23fc50-3d5c-4b5f-a018-8d8bff70819a";
        ClusterCreateVo c = clusterService.setCluster(id);

        System.out.println(c);
        assertThat("MD").isEqualTo(c.getName());
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

        CommonVo<Boolean> result = clusterService.editCluster(id, c);

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

        CommonVo<Boolean> result = clusterService.editCluster(id, c);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("클러스터 삭제")
    void deleteCluster() {
        String did = "ff23fc50-3d5c-4b5f-a018-8d8bff70819a";

        CommonVo<Boolean> result = clusterService.deleteCluster(did);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("클러스터 정보")
    void getInfo() {
        ClusterVo c = clusterService.getInfo(defaultId);

        assertThat("Default").isEqualTo(c.getName());
        assertThat(8).isEqualTo(c.getVmCnt());
    }

    @Test
    @DisplayName("클러스터 네트워크 목록")
    void getNetwork() {
        List<NetworkVo> result = clusterService.getNetwork(defaultId);

        result.forEach(System.out::println);
        assertThat(5).isEqualTo(result.size());
        assertThat(true).isEqualTo(result.stream().anyMatch(networkVo -> networkVo.getName().equals("ovirtmgmt")));
    }

    @Test
    @DisplayName("클러스터 네트워크 생성")
    void addNetwork(){
        NetworkCreateVo create =
                NetworkCreateVo.builder()
                        .name("test")
                        .description("test")
                        .comment("test")
                        .usageVm(true)
                        .externalProvider(false)
                        .clusterVoList(
                                Arrays.asList(
                                    NetworkClusterVo.builder()
                                        .id("9c7452ea-a5f3-11ee-93d2-00163e39cb43")
                                        .connected(true)
                                        .required(true)
                                        .build()
                                )
                        )
                .build();

        CommonVo<Boolean> result = clusterService.addNetwork(defaultId, create);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("클러스터 네트워크 관리 창")
    void setNetworkManage() {
        List<NetworkClusterVo> result = clusterService.setManageNetwork(defaultId);

        result.forEach(System.out::println);
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

    @Test
    @DisplayName("클러스터 선호도 그룹 목록")
    void getAgList() {
        String clusterId = "9c7452ea-a5f3-11ee-93d2-00163e39cb43";

        List<AffinityGroupVo> result = affinityService.getAffinitygroup(clusterId, "cluster");
        result.forEach(System.out::println);
    }


    @Test
    @DisplayName("클러스터 선호도 그룹 생성")
    void addAffinitygroup() {
        List<IdentifiedVo> hostList = new ArrayList<>();
        hostList.add(IdentifiedVo.builder().id("6a8e5257-0b2f-4b3c-b720-1d5eee1cbbfc").build());

        List<IdentifiedVo> vmList = new ArrayList<>();
        vmList.add(IdentifiedVo.builder().id("c9c1c52d-d2a4-4f2a-93fe-30200f1e0bff").build());

        AffinityGroupCreateVo ag =
                AffinityGroupCreateVo.builder()
                        .name("asdf")
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

        CommonVo<Boolean> result = affinityService.addAffinitygroup(defaultId, "cluster", ag);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }


    @Test
    @DisplayName("선호도 그룹 편집 창")
    void setEditAffinitygroup() {
        String agId = "2f60703a-0f8b-49f4-912a-e3a0bc097294";
        AffinityGroupCreateVo group = affinityService.setEditAffinitygroup(defaultId, "cluster", agId);

        System.out.println(group.toString());
    }

//    @Test
//    @DisplayName("선호도 그룹 편집")
//    void editAffinitygroup() {
//        String agId = "2f60703a-0f8b-49f4-912a-e3a0bc097294";
//
//        AffinityGroupCreateVo ag =
//                AffinityGroupCreateVo.builder()
//                        .name("gg")
//                        .description("n")
//                        .priority(5)
//                        .clusterId(defaultId)
//                        .vmEnabled(false)
//                        .vmEnforcing(false)
//                        .vmPositive(true)
//                        .hostEnabled(false)
//                        .hostEnforcing(false)
//                        .hostPositive(false)
////                        .hostList()
////                        .vmList()
//                        .build();
//
//        CommonVo<Boolean> result = affinityService.editAffinitygroup(defaultId, agId, ag);
//
//        assertThat(result.getHead().getCode()).isEqualTo(201);
//    }

//    @Test
//    void deleteAffinitygroup() {
//        String agId = "ca3ef16e-c188-4d13-9311-3ea8a123201c";
//
//        CommonVo<Boolean> result = clusterService.deleteAffinitygroup(id, agId);
//        assertThat(result.getHead().getCode()).isEqualTo(200);
//    }

    @Test
    @DisplayName("클러스터 선호도 레이블 목록")
    void getAffinitylabelList() {
        List<AffinityLabelVo> al = clusterService.getAffinitylabelList(defaultId);

        assertThat(2).isEqualTo(al.size());
        assertThat(true).isEqualTo(al.stream().anyMatch(als -> als.getName().equals("test")));
        al.stream().map(AffinityLabelVo::getName).forEach(System.out::println);
    }

    @Test
    @DisplayName("클러스터 선호도 레이블 생성")
    void addAffinitylabel() {
        List<HostVo> hostList = new ArrayList<>();
        hostList.add(HostVo.builder().id("1c8ed321-28e5-4f83-9e34-e13f9125f253").build());

        List<VmVo> vmList = new ArrayList<>();
//        vmList.add(VmVo.builder().build());

        AffinityLabelCreateVo label =
                AffinityLabelCreateVo.builder()
                        .name("as")
                        .hostList(hostList)
                        .vmList(vmList)
                        .build();

        CommonVo<Boolean> result = clusterService.addAffinitylabel(id, label);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("클러스터 선호도 레이블 편집 창")
    void getAffinityLabel() {
        String alId = "fe36a56c-b366-42d9-80de-5f02aa4eff09";
        AffinityLabelCreateVo label = clusterService.getAffinityLabel(defaultId, alId);

        assertThat("test").isEqualTo(label.getName());
        System.out.println(label.toString());
    }

    @Test
    void editAffinitylabel() {

    }

    @Test
    void deleteAffinitylabel() {
        String alId = "0baef571-18dd-4b9f-8519-de50a05bb428";

        CommonVo<Boolean> result = clusterService.deleteAffinitylabel(id, alId);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
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
