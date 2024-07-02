package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ovirt.engine.sdk4.types.QuotaModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItDataCenterServiceTest {
    @Autowired
    ItDataCenterService dcService;

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
                .name("ds")
                .comment("testComment")
                .description("testDescription")
                .version("4.7")
                .storageType(false)
                .quotaMode(QuotaModeType.AUDIT)
                .build();

        CommonVo<Boolean> result =  dcService.addDatacenter(dc);
        assertThat(result.getHead().getCode()).isEqualTo(201);

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
                        .name("ds")
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
        String id = "12c222e7-3165-42dd-a413-fc62852bb2d6";
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
        String id = "d90c6a1e-1ba8-4fa7-80c9-21adad5aab54";

        DataCenterCreateVo dc = DataCenterCreateVo.builder()
                .id(id)
                .name("tsfe")
                .comment("testedit")
                .description("s")
                .version("4.7")
                .storageType(false)
                .quotaMode(QuotaModeType.AUDIT)
                .build();

        CommonVo<Boolean> result = dcService.editDatacenter(dc);
        assertThat(result.getHead().getCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("데이터센터 수정 - 이름 중복")
    void editDatacenter2() {
        String id = "d90c6a1e-1ba8-4fa7-80c9-21adad5aab54";

        DataCenterCreateVo dc = DataCenterCreateVo.builder()
                .id(id)
                .name("Default")
                .comment("testComment")
                .description("testDescription")
                .version("4.7")
                .storageType(false)
                .quotaMode(QuotaModeType.AUDIT)
                .build();

        CommonVo<Boolean> result = dcService.editDatacenter(dc);
        assertThat(result.getHead().getCode()).isEqualTo(404);
    }


    @Test
    @DisplayName("데이터센터 삭제")
    void deleteDatacenter() {
        String id = "12c222e7-3165-42dd-a413-fc62852bb2d6";

        CommonVo<Boolean> result = dcService.deleteDatacenter(id);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("데이터센터 이벤트 목록")
    void getEvent() {
        String id = "12c222e7-3165-42dd-a413-fc62852bb2d6";
        List<EventVo> result = dcService.getEvent(id);

        result.forEach(System.out::println);
    }
}