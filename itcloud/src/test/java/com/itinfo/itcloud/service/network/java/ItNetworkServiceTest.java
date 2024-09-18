/*
package com.itinfo.itcloud.service.network;

import com.itinfo.itcloud.model.setting.PermissionVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ItNetworkServiceTest {

    @Autowired
    ItNetworkService networkService;

    String defaultId = "00000000-0000-0000-0000-000000000009";

    @Test
    @DisplayName("네트워크 목록")
    void getList() {
        List<NetworkVo> result = networkService.findAll();
        assertThat(13).isEqualTo(result.size());
        assertThat(true).isEqualTo(result.stream().anyMatch(networkVo -> networkVo.getName().equals("ovirtmgmt")));
    }

    @Test
    void setDcCluster() {

    }

    @Test
    void addNetwork() {
    }

    @Test
    @DisplayName("네트워크 편집창")
    void setEditNetwork() {
    }

    @Test
    @DisplayName("네트워크 편집")
    void editNetwork() {
    }

    @Test
    @DisplayName("네트워크 삭제")
    void deleteNetwork() {
    }

    @Test
    void setImportNetwork() {
    }

    @Test
    void importNetwork() {
    }

    @Test
    void getNetwork() {
    }

    @Test
    void getVnic() {
    }

    @Test
    void setVnic() {
    }

    @Test
    void addVnic() {
    }

    @Test
    void setEditVnic() {
    }

    @Test
    void editVnic() {
    }

    @Test
    void deleteVnic() {
    }

    @Test
    void getCluster() {
    }

    @Test
    void getUsage() {
    }

    @Test
    void editUsage() {
    }

    @Test
    void getHost() {
    }

    @Test
    void getVm() {
    }

    @Test
    void deleteVmNic() {
    }

    @Test
    void getTemplate() {
    }

    @Test
    void deleteTempNic() {
    }

    @Test
    void getPermission() {
    }
}*/
