package com.itinfo.itcloud.controller;

import com.google.gson.Gson;
import com.itinfo.itcloud.controller.computing.DataCenterController;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.service.computing.ItDataCenterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ovirt.engine.sdk4.types.QuotaModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// https://frozenpond.tistory.com/82
@WebMvcTest(DataCenterController.class)
class DataCenterControllerTest {
    @MockBean ItDataCenterService dcService;
    @Autowired MockMvc mvc;
    @Autowired private Gson gson;
//    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("hello Test")
    void hello() throws Exception {
        mvc.perform(get("/computing/hello"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("데이터센터 리스트 출력")
    void datacenters() throws Exception {
        List<DataCenterVo> dataCenterList = new ArrayList<>();
        dataCenterList.add(DataCenterVo.builder().id("asdf").name("asdf").build());
        dataCenterList.add(DataCenterVo.builder().id("asdf").name("asdf").build());

        given(dcService.getList()).willReturn(dataCenterList);

        mvc.perform(get("/computing/datacenters"))
                .andExpect(content().string(containsString("asdf")))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        verify(dcService).getList();
    }

    @Test
    @DisplayName("데이터센터 생성")
    void addDatacenter() throws Exception {
        DataCenterCreateVo dc = DataCenterCreateVo.builder()
                        .name("test21")
                        .comment("testComment")
                        .description("testDescription")
                        .version("4.7")
                        .storageType(false)
                        .quotaMode(QuotaModeType.AUDIT)
                        .build();

        mvc.perform(post("/computing/datacenter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(dc)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        verify(dcService).addDatacenter(dc);
    }

    @Test
    @DisplayName("데이터센터 수정 창")
    void getDatacenter() throws Exception {
        String id = "9c72ff12-a5f3-11ee-941d-00163e39cb43";
        
        mvc.perform(get("/computing/datacenter/" + id + "/settings"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        verify(dcService).setDatacenter(id);
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

        mvc.perform(put("/computing/datacenter/" + id)
                                .content(gson.toJson(dc))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        verify(dcService).editDatacenter(dc);

    }

    @Test
    @DisplayName("데이터센터 삭제")
    void deleteDatacenter() throws Exception {
        String id = "b6d4202f-85e8-4786-b350-17195078f100";

        mvc.perform(delete("/computing/datacenter/" + id))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        verify(dcService).deleteDatacenter(id);
    }



    @Test
    @DisplayName("데이터센터 이벤트 출력")
    void event() throws Exception {
        String id = "9c72ff12-a5f3-11ee-941d-00163e39cb43";

        mvc.perform(get("/computing/datacenter/" + id + "/events"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        verify(dcService).getEvent(id);
    }
}