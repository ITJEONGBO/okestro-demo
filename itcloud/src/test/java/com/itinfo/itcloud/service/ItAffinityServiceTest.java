package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.AffinityGroupVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItAffinityServiceTest {
    @Autowired ItAffinityService affinityService;

    String dcId = "9c72ff12-a5f3-11ee-941d-00163e39cb43";
    private final String clusterId = "9c7452ea-a5f3-11ee-93d2-00163e39cb43";

    @Test
    @DisplayName("선호도 생성위한 host 목록")
    void getHostList() {
        List<IdentifiedVo> result =  affinityService.getHostList(clusterId);

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("선호도 생성위한 vm 목록")
    void getVmList() {
        List<IdentifiedVo> result =  affinityService.getVmList(clusterId);

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("선호도 생성위한 레이블 목록")
    void getLabelList() {
        List<IdentifiedVo> result = affinityService.getLabelList();

        result.forEach(System.out::println);
    }


    // ---------------------------------------------------


    @Test
    @DisplayName("선호도 그룹 목록(cluster)")
    void getAgClsuterLists() {
        List<AffinityGroupVo> result = affinityService.getAffinitygroup(clusterId, "cluster");

        result.forEach(System.out::println);
    }


    @Test
    @DisplayName("선호도 그룹 목록(vm)")
    void getAgVmLists() {
        String vmId = "c9c1c52d-d2a4-4f2a-93fe-30200f1e0bff";  // hostedengine
        List<AffinityGroupVo> result = affinityService.getAffinitygroup(vmId, "vm");

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
                        .name("x1")
                        .description("asktestDescriptinn")
                        .priority(5)
                        .clusterId(clusterId)
                        .vmEnabled(false)
                        .vmEnforcing(false)
                        .vmPositive(true)
                        .hostEnabled(false)
                        .hostEnforcing(false)
                        .hostPositive(false)
                        .hostList(hostList)
                        .vmList(vmList)
                        .build();

        CommonVo<Boolean> result = affinityService.addAffinitygroup(clusterId, "cluster", ag);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }


    @Test
    @DisplayName("선호도 그룹 편집 창")
    void setEditAffinitygroup() {
        String agId = "781d91bc-6ca7-4fed-b39c-84c8a4b0eab2";
        AffinityGroupCreateVo group = affinityService.setEditAffinitygroup(clusterId, "cluster", agId);

        System.out.println(group.toString());
    }

    @Test
    @DisplayName("선호도 그룹 편집")
    void editAffinitygroup() {
        String agId = "781d91bc-6ca7-4fed-b39c-84c8a4b0eab2";

        List<IdentifiedVo> hostList = new ArrayList<>();
//        hostList.add(IdentifiedVo.builder().id("6a8e5257-0b2f-4b3c-b720-1d5eee1cbbfc").build());
//        hostList.add(IdentifiedVo.builder().id("f08baae8-2137-490c-bec2-fd00f67a37b9").build());

        List<IdentifiedVo> vmList = new ArrayList<>();
        vmList.add(IdentifiedVo.builder().id("c4326298-567b-465a-98d3-799c9bbc59b1").build());
//        vmList.add(IdentifiedVo.builder().id("c9c1c52d-d2a4-4f2a-93fe-30200f1e0bff").build());
//        vmList.add(IdentifiedVo.builder().id("eec63849-5026-482c-8f05-1d8e419ef548").build());

        List<IdentifiedVo> hostLabels = new ArrayList<>();
//        hostLabels.add(IdentifiedVo.builder().id("fe36a56c-b366-42d9-80de-5f02aa4eff09").build());

        List<IdentifiedVo> vmLabels = new ArrayList<>();
//        vmLabels.add(IdentifiedVo.builder().id("ecfe4125-d1aa-44ce-8811-d4a4ce8328ce").build());


        AffinityGroupCreateVo ag =
                AffinityGroupCreateVo.builder()
                        .id(agId)
                        .name("saaaa")
                        .description("n")
                        .priority(5)
                        .clusterId(clusterId)
                        .vmEnabled(false)
                        .vmEnforcing(false)
                        .vmPositive(true)
                        .hostEnabled(false)
                        .hostEnforcing(false)
                        .hostPositive(false)
                        .hostLabels(hostLabels)
                        .vmLabels(vmLabels)
                        .hostList(hostList)
                        .vmList(vmList)
                        .build();

        CommonVo<Boolean> result = affinityService.editAffinitygroup(clusterId, ag);

        assertThat(result.getHead().getCode()).isEqualTo(201);
    }


    @Test
    @DisplayName("클러스터 선호도 그룹 삭제")
    void deleteAffinitygroup() {
        String agId = "92ad2ff1-2c7d-475d-80e6-8174e187cafe";

        CommonVo<Boolean> result = affinityService.deleteAffinitygroup(clusterId, "cluster", agId);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("클러스터 선호도 레이블 목록")
    void getAffinitylabelList() {
        List<AffinityLabelVo> result = affinityService.getAffinitylabel();

        assertThat(2).isEqualTo(result.size());

        result.forEach(System.out::println);
    }




//    @Test
//    @DisplayName("클러스터 선호도 레이블 생성")
//    void addAffinitylabel() {
//        List<HostVo> hostList = new ArrayList<>();
//        hostList.add(HostVo.builder().id("1c8ed321-28e5-4f83-9e34-e13f9125f253").build());
//
//        List<VmVo> vmList = new ArrayList<>();
////        vmList.add(VmVo.builder().build());
//
//        AffinityLabelCreateVo label =
//                AffinityLabelCreateVo.builder()
//                        .name("as")
//                        .hostList(hostList)
//                        .vmList(vmList)
//                        .build();
//
//        CommonVo<Boolean> result = affinityService.addAffinitylabel(clusterId, label);
//        assertThat(result.getHead().getCode()).isEqualTo(201);
//    }
//
//    @Test
//    @DisplayName("클러스터 선호도 레이블 편집 창")
//    void getAffinityLabel() {
//        String alId = "fe36a56c-b366-42d9-80de-5f02aa4eff09";
//        AffinityLabelCreateVo label = affinityService.getAffinityLabel(clusterId, alId);
//
//        assertThat("test").isEqualTo(label.getName());
//        System.out.println(label.toString());
//    }
//
//    @Test
//    void editAffinitylabel() {
//
//    }
//
//    @Test
//    void deleteAffinitylabel() {
//        String alId = "0baef571-18dd-4b9f-8519-de50a05bb428";
//
//        CommonVo<Boolean> result = affinityService.deleteAffinitylabel(id, alId);
//        assertThat(result.getHead().getCode()).isEqualTo(200);
//    }


    @Test
    void getAgHostList() {
    }

    @Test
    void getAgVmList() {
    }

    @Test
    void getHostLabelMember() {
    }

    @Test
    void getVmLabelMember() {
    }


}