package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.ovirt.engine.sdk4.types.DataCenter;
import org.ovirt.engine.sdk4.types.QuotaModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItDataCenterServiceTest {
    @Autowired ItDataCenterService dcService;

    String defaultDcId = "9c72ff12-a5f3-11ee-941d-00163e39cb43";

    @Test
    @DisplayName("데이터센터 리스트")
    void getList() {
        List<DataCenterVo> dcList = dcService.getList();

        assertThat(2).isEqualTo(dcList.size());
    }

    @Test
    @DisplayName("데이터센터 생성")
    void addDatacenter() {
        DataCenterCreateVo dc =
                DataCenterCreateVo.builder()
                .name("dfdf")
                .comment("testComment")
                .description("testDescription")
                .version("4.7")
                .storageType(false)
                .quotaMode(QuotaModeType.AUDIT)
                .build();

        CommonVo<Boolean> result =  dcService.addDatacenter(dc);
        assertThat(result.getHead().getCode()).isEqualTo(201);

        assertThat("dfdf").isEqualTo(dc.getName());
        assertThat("testComment").isEqualTo(dc.getComment());
        assertThat("testDescription").isEqualTo(dc.getDescription());
        assertThat("4.7").isEqualTo(dc.getVersion());
        assertThat(false).isEqualTo(dc.isStorageType());
        assertThat(QuotaModeType.AUDIT).isEqualTo(dc.getQuotaMode());
    }

    @Test
    @DisplayName("데이터센터 생성 - 이름 중복")
    void addDatacenter2() {
        DataCenterCreateVo dc =
                DataCenterCreateVo.builder()
                        .name("dfdf")
                        .comment("testComment")
                        .description("testDescription")
                        .version("4.7")
                        .storageType(false)
                        .quotaMode(QuotaModeType.AUDIT)
                        .build();

        CommonVo<Boolean> result =  dcService.addDatacenter(dc);
        assertThat(result.getHead().getCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("데이터센터 수정 창")
    void getDatacenter() {
        String id = "5e3e972b-7000-4e8e-a43b-d90fd3e304a6";
        DataCenterCreateVo result =  dcService.setDatacenter(id);

        DataCenterCreateVo dc = DataCenterCreateVo.builder()
                .id(id)
                .name(result.getName())
                .comment(result.getComment())
                .description(result.getDescription())
                .version(result.getVersion())
                .storageType(result.isStorageType())
                .quotaMode(result.getQuotaMode())
                .build();

        assertThat(id).isEqualTo(result.getId());
        assertThat(dc.getName()).isEqualTo(result.getName());
        assertThat(result).isEqualTo(dc);

        System.out.println(dc);
    }

    @Test
    @DisplayName("데이터센터 수정")
    void editDatacenter() {
        String id = "5e3e972b-7000-4e8e-a43b-d90fd3e304a6";

        DataCenterCreateVo dc = DataCenterCreateVo.builder()
                .id(id)
                .name("tsfe")
                .comment("testedit")
                .description("s")
                .version("4.7")
                .storageType(false)
                .quotaMode(QuotaModeType.AUDIT)
                .build();

        CommonVo<Boolean> result = dcService.editDatacenter(id, dc);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("데이터센터 수정 - 이름 중복")
    void editDatacenter2() {
        String id = "5e3e972b-7000-4e8e-a43b-d90fd3e304a6";

        DataCenterCreateVo dc = DataCenterCreateVo.builder()
                .id(id)
                .name("Default")
                .comment("testComment")
                .description("testDescription")
                .version("4.7")
                .storageType(false)
                .quotaMode(QuotaModeType.AUDIT)
                .build();

        CommonVo<Boolean> result = dcService.editDatacenter(id, dc);
        assertThat(result.getHead().getCode()).isEqualTo(404);
    }


    @Test
    @DisplayName("데이터센터 삭제")
    void deleteDatacenter() {
        String id = "5e3e972b-7000-4e8e-a43b-d90fd3e304a6";

        CommonVo<Boolean> result = dcService.deleteDatacenter(id);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("데이터센터 이벤트 목록")
    void getEvent() {
        List<EventVo> result = dcService.getEvent(defaultDcId);
        assertThat(result.size()).isEqualTo(12);
    }
}