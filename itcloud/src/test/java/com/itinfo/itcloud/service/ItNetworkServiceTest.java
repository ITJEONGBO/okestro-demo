package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkClusterVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ItNetworkServiceTest {

    @Autowired ItNetworkService networkService;

    @Test
    void getList() {
        assertThat(10).isEqualTo(networkService.getList().size());
    }

    @Test
    void setDcCluster() {
    }

    @Test
    void addNetwork() {
        List<NetworkClusterVo> networkClusterVoList = new ArrayList<>();
        networkClusterVoList.add(NetworkClusterVo.builder().id("9c7452ea-a5f3-11ee-93d2-00163e39cb43").connected(true).required(true).build());

        NetworkCreateVo create =
                NetworkCreateVo.builder()
                        .datacenterId("9c72ff12-a5f3-11ee-941d-00163e39cb43")
                        .name("tets")
                        .description("test")
                        .comment("test")
                        .usageVm(true)
                        .externalProvider(false)
                        .clusterVoList(networkClusterVoList)
                        .build();

        CommonVo<Boolean> result = networkService.addNetwork(create);

        assertThat(result.getHead().getCode()).isEqualTo(200);

    }

    @Test
    void setEditNetwork() {
    }

    @Test
    void editNetwork() {
    }

    @Test
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
}