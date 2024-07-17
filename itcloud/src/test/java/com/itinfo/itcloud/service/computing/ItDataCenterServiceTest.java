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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;

@SpringBootTest
class ItDataCenterServiceTest {
    @Autowired ItDataCenterService dcService;

    private final String defaultDcId = "ae1d4138-f642-11ee-9c1b-00163e4b3128"; // Default

    @Test
    @DisplayName("데이터센터 리스트")
    void getList() {
        List<DataCenterVo> dcList = dcService.getDataCenters();

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
        DataCenterCreateVo result =  dcService.setDatacenter(defaultDcId);

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
    }

    @Test
    @DisplayName("데이터센터 수정")
    void editDatacenter() {
        String id = "99c24506-4bfa-4995-ba69-2692a8e81186";

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
        DataCenterCreateVo dc = DataCenterCreateVo.builder()
                .id(defaultDcId)
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
        List<String> ids = Arrays.asList("402935aa-a550-4785-b5a1-02b46c6c2c20", "b997508c-0bf4-4944-94ae-61d56ca76984", "e4eae0ec-280d-460c-bd79-5c778664bbc4");

        CommonVo<Boolean> result = dcService.deleteDatacenter(ids);
        assertThat(result.getHead().getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("데이터센터 이벤트 목록")
    void getEvent() {
        List<EventVo> result = dcService.getEventsByDatacenter(defaultDcId);

        result.forEach(System.out::println);
    }



    @Test
    @DisplayName("대시보드 - 컴퓨팅")
    void dash(){
        List<DataCenterVo> result = dcService.dashboardComputing();

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("대시보드 - 네트워크")
    void networks(){
        List<DataCenterVo> result = dcService.dashboardNetwork();

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("대시보드 - 스토리지")
    void setStorage(){
        List<DataCenterVo> result = dcService.dashboardStorage();

        result.forEach(System.out::println);
    }



}