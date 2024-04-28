package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
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
        assertThat(4).isEqualTo(dcService.getList().size());
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
        assertThat(result.getHead().getCode()).isEqualTo(200);

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
        String id = "ea9ced37-0df4-40c5-8dcc-5bb5183236da";

        DataCenterCreateVo result =  dcService.getDatacenter(defaultDcId);

        DataCenterCreateVo dc = DataCenterCreateVo.builder()
                .id(defaultDcId)
                .name(result.getName())
                .comment(result.getComment())
                .description(result.getDescription())
                .version(result.getVersion())
                .storageType(result.isStorageType())
                .quotaMode(result.getQuotaMode())
                .build();

        assertThat(defaultDcId).isEqualTo(result.getId());
        assertThat(dc.getName()).isEqualTo(result.getName());
        assertThat(result).isEqualTo(dc);
    }

    @Test
    @DisplayName("데이터센터 수정")
    void editDatacenter() {
        String id = "ea9ced37-0df4-40c5-8dcc-5bb5183236da";

        DataCenterCreateVo dc = DataCenterCreateVo.builder()
                .id(id)
                .name("tsfe")
                .comment("testComment")
                .description("asd")
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
        String id = "ea9ced37-0df4-40c5-8dcc-5bb5183236da";

        DataCenterCreateVo dc = DataCenterCreateVo.builder()
                .id(id)
                .name("e")
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
        String id = "7d736eda-a541-4fc9-95a2-03b0d4617b31";

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