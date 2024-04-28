package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItClusterServiceTest {
    @Autowired ItClusterService cService;

    String dcId = "9c72ff12-a5f3-11ee-941d-00163e39cb43";
    String id = "b9259be8-4beb-4616-8481-23de48944bf9";
    String defaultId = "9c7452ea-a5f3-11ee-93d2-00163e39cb43";

    @Test
    @DisplayName("클러스터 리스트 출력")
    void getList() {
        assertThat(2).isEqualTo(cService.getList().size());
    }

    @Test
    @DisplayName("클러스터 생성 창")
    void setClusterDefaultInfo() {
        assertThat(4).isEqualTo(cService.setClusterDefaultInfo().size());
        assertThat("Default").isEqualTo(cService.setClusterDefaultInfo().get(0).getName());
        assertThat(6).isEqualTo(cService.setClusterDefaultInfo().get(0).getNetworkList().size());
    }

    @Test
    @DisplayName("클러스터 생성 - 외부 공급자 없이")
    void addCluster() {
        ClusterCreateVo c =
                ClusterCreateVo.builder()
                        .datacenterId(dcId)
                        .name("test1")
                        .cpuArc(Architecture.X86_64)
                        .cpuType("Intel Nehalem Family")
//                        .description("")
//                        .comment("")
                        .networkId("00000000-0000-0000-0000-000000000009")
                        .biosType(BiosType.CLUSTER_DEFAULT)
                        .fipsMode(FipsMode.ENABLED)
                        .version("4.7")
                        .switchType(SwitchType.LEGACY)
                        .firewallType(FirewallType.FIREWALLD)
                        .logMaxMemory(95)
                        .logMaxType(LogMaxMemoryUsedThresholdType.PERCENTAGE)
                        .virtService(true)
                        .glusterService(false)
                        .recoveryPolicy(MigrateOnError.MIGRATE)
                        .bandwidth(MigrationBandwidthAssignmentMethod.AUTO)
                        .encrypted(InheritableBoolean.INHERIT)
                        .networkProvider(false)
                        .build();

        CommonVo<Boolean> result = cService.addCluster(c);

        assertThat(result.getHead().getCode()).isEqualTo(201);
        assertThat("test1").isEqualTo(c.getName());
    }

    @Test
    @DisplayName("클러스터 수정 창")
    void setEditCluster() {
        ClusterCreateVo c = cService.setEditCluster(id);
        assertThat("test1").isEqualTo(c.getName());
        assertThat(BiosType.Q35_OVMF).isEqualTo(c.getBiosType());

    }

    @Test
    @DisplayName("클러스터 수정 - 단순 이름변경 ")
    void editCluster() {
        ClusterCreateVo c =
                ClusterCreateVo.builder()
                        .id(id)
//                        .datacenterId(dcId)
                        .name("te1")
                        .cpuArc(Architecture.X86_64)
                        .cpuType("Intel Nehalem Family")
                        .description("asdf")
                        .networkId("00000000-0000-0000-0000-000000000009")
                        .biosType(BiosType.CLUSTER_DEFAULT)
                        .fipsMode(FipsMode.ENABLED)
                        .version("4.7")
                        .firewallType(FirewallType.FIREWALLD)
                        .logMaxMemory(95)
                        .logMaxType(LogMaxMemoryUsedThresholdType.PERCENTAGE)
                        .virtService(true)
                        .glusterService(false)
                        .recoveryPolicy(MigrateOnError.MIGRATE)
                        .bandwidth(MigrationBandwidthAssignmentMethod.AUTO)
                        .encrypted(InheritableBoolean.INHERIT)
                        .networkProvider(false)
                        .build();

        CommonVo<Boolean> result = cService.editCluster(id, c);

        assertThat(result.getHead().getCode()).isEqualTo(201);
        assertThat("te1").isEqualTo(c.getName());
        assertThat("asdf").isEqualTo(c.getDescription());
        assertThat("Intel Nehalem Family").isEqualTo(c.getCpuType());
    }

    @Test
    @DisplayName("클러스터 수정 - 다른 클러스터와 같은 이름변경 ")
    void editCluster2() {
        ClusterCreateVo c =
                ClusterCreateVo.builder()
                        .id(id)
                        .name("s")
                        .cpuArc(Architecture.X86_64)
                        .cpuType("Intel Nehalem Family")
                        .description("asdf")
                        .networkId("00000000-0000-0000-0000-000000000009")
                        .biosType(BiosType.CLUSTER_DEFAULT)
                        .fipsMode(FipsMode.ENABLED)
                        .version("4.7")
                        .firewallType(FirewallType.FIREWALLD)
                        .logMaxMemory(95)
                        .logMaxType(LogMaxMemoryUsedThresholdType.PERCENTAGE)
                        .virtService(true)
                        .glusterService(false)
                        .recoveryPolicy(MigrateOnError.MIGRATE)
                        .bandwidth(MigrationBandwidthAssignmentMethod.AUTO)
                        .encrypted(InheritableBoolean.INHERIT)
                        .networkProvider(false)
                        .build();

        CommonVo<Boolean> result = cService.editCluster(id, c);
        assertThat(result.getHead().getCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("클러스터 삭제")
    @Disabled
    void deleteCluster() {
        String did = "5eb";

        CommonVo<Boolean> result = cService.deleteCluster(did);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("클러스터 정보")
    void getInfo() {
        ClusterVo c = cService.getInfo(id);

        assertThat("te1").isEqualTo(c.getName());
        assertThat("asdf").isEqualTo(c.getDescription());
        assertThat(0).isEqualTo(c.getHostCnt());
        assertThat(0).isEqualTo(c.getVmCnt());
    }

    @Test
    @DisplayName("클러스터 네트워크 목록")
    void getNetwork() {
        assertThat(1).isEqualTo(cService.getNetwork(id).size());
        assertThat("ovirtmgmt").isEqualTo(cService.getNetwork(id).get(0).getName());
        assertThat(true).isEqualTo(cService.getNetwork(id).stream().anyMatch(networkVo -> networkVo.getName().equals("ovirtmgmt")));
    }

    @Test
    @DisplayName("클러스터 호스트 목록")
    void getHost() {
//        assertThat(0).isEqualTo(cService.getHost(id).size());
        assertThat(2).isEqualTo(cService.getHost(defaultId).size());
        assertThat(true).isEqualTo(cService.getHost(defaultId).stream().anyMatch(hostVo -> hostVo.getName().equals("host01.ititinfo.com")));
        assertThat("host01.ititinfo.com").isEqualTo(cService.getHost(defaultId).get(0).getName());
    }

    @Test
    @DisplayName("클러스터 가상머신 목록")
    void getVm() {
//        assertThat(0).isEqualTo(cService.getVm(id).size());
        assertThat(10).isEqualTo(cService.getVm(defaultId).size());
        assertThat(true).isEqualTo(cService.getVm(defaultId).stream().anyMatch(vmVo -> vmVo.getName().equals("HostedEngine")));
        assertThat("HostedEngine").isEqualTo(cService.getVm(defaultId).get(0).getName());
    }

    @Test
    @DisplayName("클러스터 선호도 그룹&레이블 생성 창")
    void setAffinityDefaultInfo() {
        AffinityHostVm ahv = cService.setAffinityDefaultInfo(defaultId, "label");
        AffinityHostVm ahv2 = cService.setAffinityDefaultInfo(defaultId, ""); // group

        assertThat(2).isEqualTo(ahv.getHostList().size());
        assertThat(10).isEqualTo(ahv.getVmList().size());

        assertThat(2).isEqualTo(ahv2.getHostList().size());
        assertThat(10).isEqualTo(ahv2.getVmList().size());
    }

    @Test
    @DisplayName("클러스터 선호도 그룹 목록")
    void getAffinitygroup() {
        List<AffinityGroupVo> ag = cService.getAffinitygroup(defaultId);

        assertThat(3).isEqualTo(ag.size());
        assertThat(true).isEqualTo(ag.stream().anyMatch(affinityGroupVo -> affinityGroupVo.getName().equals("d")));
        ag.stream().map(AffinityGroupVo::getName).forEach(System.out::println);
    }

    @Test
    void addAffinitygroup() {
        List<HostVo> hostList = new ArrayList<>();
        hostList.add(HostVo.builder().id("1c8ed321-28e5-4f83-9e34-e13f9125f253").build());

        List<VmVo> vmList = new ArrayList<>();
        vmList.add(VmVo.builder().id("c9c1c52d-d2a4-4f2a-93fe-30200f1e0bff").build());

        AffinityGroupCreateVo ag =
                AffinityGroupCreateVo.builder()
                        .name("asdsfsfs")
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

        CommonVo<Boolean> result = cService.addAffinitygroup(defaultId, ag);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    void setEditAffinitygroup() {
        String agId = "b99e6ca4-4bff-45a4-8f58-d0e203d4a081";
        AffinityGroupCreateVo group = cService.setEditAffinitygroup(defaultId, agId);

        assertThat("ta").isEqualTo(group.getName());
        System.out.println(group.toString());
    }

    @Test
    void editAffinitygroup() {
    }

    @Test
    void deleteAffinitygroup() {
        String agId = "ca3ef16e-c188-4d13-9311-3ea8a123201c";

        CommonVo<Boolean> result = cService.deleteAffinitygroup(id, agId);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("클러스터 선호도 레이블 목록")
    void getAffinitylabelList() {
        List<AffinityLabelVo> al = cService.getAffinitylabelList(defaultId);

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

        CommonVo<Boolean> result = cService.addAffinitylabel(id, label);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("클러스터 선호도 레이블 편집 창")
    void getAffinityLabel() {
        String alId = "fe36a56c-b366-42d9-80de-5f02aa4eff09";
        AffinityLabelCreateVo label = cService.getAffinityLabel(defaultId, alId);

        assertThat("test").isEqualTo(label.getName());
        System.out.println(label.toString());
    }

    @Test
    void editAffinitylabel() {

    }

    @Test
    void deleteAffinitylabel() {
        String alId = "0baef571-18dd-4b9f-8519-de50a05bb428";

        CommonVo<Boolean> result = cService.deleteAffinitylabel(id, alId);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    void getPermission() {
        assertThat(3).isEqualTo(cService.getPermission(id).size());
    }

    @Test
    void getEvent() {
        assertThat(1045).isEqualTo(cService.getEvent(defaultId).size());
    }
}
