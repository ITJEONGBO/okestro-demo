package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkClusterVo;
import com.itinfo.itcloud.model.network.NetworkImportVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.network.OpenstackVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ItNetworkServiceTest {

    @Autowired ItNetworkService networkService;

    String defaultId = "00000000-0000-0000-0000-000000000009";

    @Test
    @DisplayName("네트워크 리스트")
    void getList() {
        List<NetworkVo> result = networkService.getList();

        assertThat(13).isEqualTo(result.size());
        assertThat(true).isEqualTo(result.stream().anyMatch(networkVo -> networkVo.getName().equals("ovirtmgmt")));
    }

    @Test
    void setDcCluster() {

    }

    @Test
    void addNetwork() {
        NetworkCreateVo create =
                NetworkCreateVo.builder()
                        .datacenterId("9c72ff12-a5f3-11ee-941d-00163e39cb43")
                        .name("tets")
                        .description("test")
                        .comment("test")
                        .usageVm(true)
                        .externalProvider(false)
                        .clusterVoList(
                                Arrays.asList(
                                        NetworkClusterVo.builder()
                                                .id("9c7452ea-a5f3-11ee-93d2-00163e39cb43")
                                                .connected(true)
                                                .required(true)
                                                .build()
                                )
                        )
                        .build();

        CommonVo<Boolean> result = networkService.addNetwork(create);

        assertThat(result.getHead().getCode()).isEqualTo(201);

    }

    @Test
    @DisplayName("네트워크 수정창")
    void setEditNetwork() {
        String id = "9f6566bc-61c1-49bf-98c0-5f5e2c760d21";
        NetworkCreateVo result = networkService.setEditNetwork(defaultId);

        System.out.println(result);
    }

    @Test
    @DisplayName("네트워크 수정")
    void editNetwork() {
        String id = "9f6566bc-61c1-49bf-98c0-5f5e2c760d21";

        NetworkCreateVo create =
                NetworkCreateVo.builder()
                        .id(id)
                        .datacenterId("9c72ff12-a5f3-11ee-941d-00163e39cb43")
                        .name("st")
                        .description("tttt")
                        .usageVm(true)
                        .vlan(2)
                        .mtu(122)
//                        .stp(true)
                        .vlan(23)
                        .build();

        CommonVo<Boolean> result =  networkService.editNetwork(create);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("네트워크 삭제")
    void deleteNetwork() {
        String id = "d85b837f-51ac-40b4-bede-a05771542144";

        CommonVo<Boolean> result = networkService.deleteNetwork(id);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    void setImportNetwork() {
        NetworkImportVo result = networkService.setImportNetwork();

        result.getOsVoList().stream().map(OpenstackVo::getName).forEach(System.out::println);
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
        List<PermissionVo> result = networkService.getPermission(defaultId);

        assertThat(4).isEqualTo(result.size());
        result.stream().map(PermissionVo::getUser).forEach(System.out::println);
    }
}