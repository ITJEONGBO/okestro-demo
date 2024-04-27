package com.itinfo.itcloud.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItClusterServiceTest {
    @Autowired ItClusterService cService;

    @Test
    @DisplayName("클러스터 리스트 출력")
    void getList() {
        assertThat(2).isEqualTo(cService.getList().size());
    }

    @Test
    @DisplayName("클러스터 리스트 출력")
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
    void getInfo() {
    }

    @Test
    void getNetwork() {
    }

    @Test
    void getHost() {
    }

    @Test
    void getVm() {
    }

    @Test
    void setAffinityDefaultInfo() {
    }

    @Test
    void getAffinitygroup() {
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
    void getAffinitylabelList() {
    }

    @Test
    void getAffinityLabel() {
    }

    @Test
    void addAffinitylabel() {
    }

    @Test
    void editAffinitylabel() {
    }

    @Test
    void deleteAffinitylabel() {
    }

    @Test
    void getPermission() {
    }

    @Test
    void getEvent() {
    }
}