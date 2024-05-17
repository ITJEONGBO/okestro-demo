package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.NicVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItHostServiceTest {
    @Autowired ItHostService hostService;

    String defaultId = "f08baae8-2137-490c-bec2-fd00f67a37b9";

    @Test
    void getList() {
        List<HostVo> hostList = hostService.getList();

        assertThat(hostList.size()).isEqualTo(2);
        hostList.forEach(System.out::println);
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
    void getVm() {
    }

    @Test
    void getNic() {
        String hostId = "1c8ed321-28e5-4f83-9e34-e13f9125f253";
        List<NicVo> nicList = hostService.getNic(hostId);

        nicList.forEach(System.out::println);
        assertThat(nicList.size()).isEqualTo(2);
    }

    @Test
    void getHostDevice() {
    }

    @Test
    void getPermission() {
    }

    @Test
    void setAffinityDefaultInfo() {
    }

    @Test
    void getAffinitylabels() {
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