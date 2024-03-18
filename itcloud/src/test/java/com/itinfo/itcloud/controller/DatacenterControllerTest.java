package com.itinfo.itcloud.controller;


import com.google.gson.Gson;
import com.itinfo.itcloud.controller.computing.DataCenterController;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.service.ItDataCenterService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.ovirt.engine.sdk4.types.QuotaModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DataCenterController.class)
public class DatacenterControllerTest {
    @Autowired private MockMvc mvc;

    @MockBean ItDataCenterService dcService;


    @DisplayName("hello")
    @Test
    public void hello() throws Exception{
        mvc.perform(get("/computing/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @DisplayName("데이터센터 리스트 출력")
    @Test
    public void datacenterList() throws Exception {
        List<DataCenterVo> dcList = new ArrayList<>();
        dcList.add(
                DataCenterVo.builder()
                        .name("test")
                        .description("testDescription")
                        .storageType(false)
                        .version("4.2")
                        .quotaMode("audit")
                        .comment("testComment")
                        .build()
        );

        given(dcService.getList()).willReturn(dcList);

        mvc.perform(get("/computing/datacentersStatus"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test")));
    }

    @DisplayName("데이터센터 추가")
    @Test
    public void addDc() throws Exception{
        DataCenterCreateVo dc = DataCenterCreateVo.builder()
                .name("s")
                .description("f")
                .storageType(false)
                .quotaMode(QuotaModeType.DISABLED)
                .version("4.1")
                .comment("s")
                .build();

        Gson gson = new Gson();
        String content = gson.toJson(dc);

        mvc.perform(post("/computing/datacenter/addDc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

//        verify(dcService).addDatacenter(dc);
        verify(dcService).addDatacenter(refEq(dc));
        // 객체 비교시 오류가 가끔있음 refEq( ) 사용 시 해결됨  https://zorba91.tistory.com/235
    }




}
