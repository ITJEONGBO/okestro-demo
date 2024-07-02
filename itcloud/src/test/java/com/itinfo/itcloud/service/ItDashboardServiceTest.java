package com.itinfo.itcloud.service;

import com.itinfo.itcloud.service.setting.ItDashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItDashboardServiceTest {
    @Autowired
    ItDashboardService dashboardService;

    @Test
    void getDatacenters() {
        int upResult = dashboardService.getDatacenters("up");
        int downResult = dashboardService.getDatacenters("down");
        int totalResult = dashboardService.getDatacenters("");

        assertThat(1).isEqualTo(upResult);
        assertThat(3).isEqualTo(downResult);
        assertThat(4).isEqualTo(totalResult);
    }

    @Test
    void getClusters() {
    }

    @Test
    void gethosts() {
    }

    @Test
    void getvms() {
    }

    @Test
    void getStorages() {
    }

    @Test
    void getEvents() {
    }

    @Test
    void getCpu() {
    }

    @Test
    void getMemory() {
    }

    @Test
    void getStorage() {
    }
}