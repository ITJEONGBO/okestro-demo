package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.AffinityGroupVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ItAffinityServiceTest {
    @Autowired ItAffinityService affinityService;

    private final String clusterId = "9c7452ea-a5f3-11ee-93d2-00163e39cb43";

    @Test
    void setHostList() {
        String id = "729bd062-f5b6-44f1-a3c2-64d81e2dbf1b";
        List<IdentifiedVo> result =  affinityService.getHostList(id);

        result.forEach(System.out::println);
    }

    @Test
    void getVmList() {
        List<IdentifiedVo> result =  affinityService.getVmList(clusterId);

        result.forEach(System.out::println);
    }

    @Test
    void getLabel() {
        List<IdentifiedVo> result = affinityService.getLabel();

        result.forEach(System.out::println);
    }

    @Test
    void getAgList() {
        String clusterId = "9c7452ea-a5f3-11ee-93d2-00163e39cb43";
        String vmId = "c9c1c52d-d2a4-4f2a-93fe-30200f1e0bff";

        List<AffinityGroupVo> result = affinityService.getAffinitygroup(vmId, "vm");
        result.forEach(System.out::println);
    }


    @Test
    void getLabelName() {
        String alId = "fe36a56c-b366-42d9-80de-5f02aa4eff09";
        List<AffinityLabelVo> result = affinityService.getLabelName(alId);

        result.forEach(System.out::println);
    }

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