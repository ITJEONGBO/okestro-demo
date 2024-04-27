package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.ovirt.engine.sdk4.types.QuotaModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
class ItDataCenterServiceTest {
//    @Autowired DataCenterServiceImpl dcService;
    @Autowired ItDataCenterService dcService;

    @Test
    @DisplayName("test 출력")
    @Order(1)
    void test(){
        String a = "test";
        assertThat(a).isEqualTo("test");
        System.out.println(a);
    }
    @Test
    @DisplayName("데이터센터 리스트 출력")
    @Order(2)
    void getList() {
        assertThat(3).isEqualTo(dcService.getList().size());
    }

    @Test
    @DisplayName("데이터센터 생성 테스트")
    @Order(3)
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
    @DisplayName("데이터센터 수정창 테스트")
    @Order(4)
    void getDatacenter() {
        String id = "90fc30fb-7f77-48b4-9897-df7baf1dd6fa";

        DataCenterCreateVo result =  dcService.getDatacenter(id);

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
    }

    @Test
    @DisplayName("데이터센터 수정 테스트")
    @Order(5)
    void editDatacenter() {
        String id = "90fc30fb-7f77-48b4-9897-df7baf1dd6fa";

        DataCenterCreateVo dc = DataCenterCreateVo.builder()
                .id(id)
                .name("tsfe")
                .comment("testComment")
                .description("testDescription")
                .version("4.7")
                .storageType(false)
                .quotaMode(QuotaModeType.AUDIT)
                .build();

        CommonVo<Boolean> result = dcService.editDatacenter(id, dc);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }


    @Test
    @DisplayName("데이터센터 삭제 테스트")
    @Order(6)
    void deleteDatacenter() {
        String id = "7d736eda-a541-4fc9-95a2-03b0d4617b31";

        CommonVo<Boolean> result = dcService.deleteDatacenter(id);

        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("데이터센터 이벤트 목록")
    void getEvent() {
        String id = "ea9ced37-0df4-40c5-8dcc-5bb5183236da";

        List<EventVo> result = dcService.getEvent(id);
        System.out.println(dcService.getEvent(id).size());
        assertThat(result.size()).isEqualTo(1);
    }
}