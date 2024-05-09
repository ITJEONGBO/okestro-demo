package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.service.ItHostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HostServiceImplTest {
    @Autowired ItHostService hostService;
    String defaultId = "1c8ed321-28e5-4f83-9e34-e13f9125f253";

    @Test
    void getList() {
    }

    @Test
    void setHostDefaultInfo() {
    }

    @Test
    void addHost() {
    }

    @Test
    void getHostCreate() {
    }

    @Test
    void editHost() {
    }

    @Test
    void deleteHost() {
    }

    @Test
    void deActive() {
    }

    @Test
    void active() {
    }

    @Test
    void refresh() {
    }

    @Test
    void reStart() {
    }

    @Test
    void stop() {
    }

    @Test
    void getInfo() {
    }

    @Test
    @DisplayName("호스트 가상머신 목록")
    void getVm() {
        List<VmVo> result = hostService.getVm(defaultId);

        Assertions.assertThat(result.size()).isEqualTo(3);

    }

    @Test
    void getNic() {
    }

    @Test
    void getHostDevice() {
    }

    @Test
    void getPermission() {
    }

    @Test
    void getAffinitylabels() {
    }

    @Test
    void setAffinityDefaultInfo() {
    }

    @Test
    void addAffinitylabel() {
    }

    @Test
    void getAffinityLabel() {
    }

    @Test
    void editAffinitylabel() {
    }

    @Test
    void deleteAffinitylabel() {
    }

    @Test
    void getEvent() {
    }
}