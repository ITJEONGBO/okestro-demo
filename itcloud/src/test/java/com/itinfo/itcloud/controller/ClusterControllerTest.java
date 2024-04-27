package com.itinfo.itcloud.controller;

import com.google.gson.Gson;
import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.service.ItClusterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DataCenterController.class)
class ClusterControllerTest {

    @MockBean ItClusterService cService;
    @Autowired MockMvc mvc;
    @Autowired private Gson gson;

    @Test
    @DisplayName("클러스터 리스트 출력")
    void clusters() throws Exception {
        List<ClusterVo> clusterList = new ArrayList<>();

        given(cService.getList()).willReturn(clusterList);

        mvc.perform(get("/computing/clusters"))
                .andExpect(content().string(containsString("asdf")))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void setClusterDefaultInfo() {
    }

    @Test
    void addCluster() {
    }

    @Test
    void setEditCluster() {
    }

    @Test
    void editCluster() {
    }

    @Test
    void deleteCluster() {
    }

    @Test
    void cluster() {
    }

    @Test
    void network() {
    }

    @Test
    void host() {
    }

    @Test
    void vm() {
    }

    @Test
    void affGroup() {
    }

    @Test
    void setAffinitygroup() {
    }

    @Test
    void addAffinitygroup() {
    }

    @Test
    void setEditAffinitygroup() {
    }

    @Test
    void editAffinitygroup() {
    }

    @Test
    void deleteAffinitygroup() {
    }

    @Test
    void affLabel() {
    }

    @Test
    void setAffinitylabel() {
    }

    @Test
    void addAff() {
    }

    @Test
    void getAffinityLabel() {
    }

    @Test
    void editAff() {
    }

    @Test
    void deleteAff() {
    }

    @Test
    void permission() {
    }

    @Test
    void event() {
    }
}