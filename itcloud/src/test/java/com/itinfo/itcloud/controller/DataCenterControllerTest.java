package com.itinfo.itcloud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.service.ItDataCenterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ovirt.engine.sdk4.types.QuotaModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DataCenterController.class)
class DataCenterControllerTest {

    @Autowired MockMvc mvc;
    @MockBean ItDataCenterService dcService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("hello Test")
    void hello() throws Exception {
        mvc.perform(get("/computing/hello"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("데이터센터 리스트 출력")
    void datacenters() throws Exception {
        mvc.perform(get("/computing/datacenters"))
                .andExpect(status().isOk());

        verify(dcService).getList();
    }

    @Test
    @DisplayName("데이터센터 이벤트 출력")
    void event() throws Exception {
        String id = "9c72ff12-a5f3-11ee-941d-00163e39cb43";

        mvc.perform(
                get("/computing/datacenter/" + id + "/events"))
                .andExpect(status().isOk());

        verify(dcService).getEvent(id);
    }

    @Test
    @DisplayName("데이터센터 생성")
    void addDatacenter() throws Exception {
        DataCenterCreateVo dc =
                DataCenterCreateVo.builder()
                        .name("test21")
                        .comment("testComment")
                        .description("testDescription")
                        .version("4.7")
                        .storageType(false)
                        .quotaMode(QuotaModeType.AUDIT)
                        .build();

        mvc.perform(
                post("/computing/datacenter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dc)))
                .andExpect(status().isOk());

        verify(dcService).addDatacenter(dc);
    }

    @Test
    @DisplayName("데이터센터 수정 창")
    void getDatacenter() throws Exception {
        String id = "9c72ff12-a5f3-11ee-941d-00163e39cb43";
        
        mvc.perform(
                get("/computing/datacenter/" + id + "/settings"))
                .andExpect(status().isOk());

        verify(dcService).getDatacenter(id);
    }

    @Test
    @DisplayName("데이터센터 수정")
    void editDatacenter() throws Exception {
        String id = "b6d4202f-85e8-4786-b350-17195078f100";

        DataCenterCreateVo dc =
                DataCenterCreateVo.builder()
                        .id(id)
                        .name("test1")
                        .comment("tesomment")
                        .description("testcription")
                        .version("4.7")
                        .storageType(false)
                        .quotaMode(QuotaModeType.AUDIT)
                        .build();

        mvc.perform(
                        put("/computing/datacenter/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dc)))
                .andExpect(status().isOk());

        verify(dcService).editDatacenter(id, dc);

    }

    @Test
    @DisplayName("데이터센터 삭제")
    void deleteDatacenter() throws Exception {
        String id = "b6d4202f-85e8-4786-b350-17195078f100";

        mvc.perform(
                delete("/computing/datacenter/" + id))
                .andExpect(status().isOk());

        verify(dcService).deleteDatacenter(id);
    }
}