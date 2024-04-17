package com.itinfo.itcloud.controller;


import com.google.gson.Gson;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.service.ItDataCenterService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ovirt.engine.sdk4.types.QuotaModeType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @RunWith(SpringRunner.class)  // junit4
@ExtendWith(MockitoExtension.class)
// @WebMvcTest(DataCenterController.class)
public class DatacenterControllerTest {

    @InjectMocks
    private DataCenterController controller;
    @Mock
    private ItDataCenterService dcService;

    private MockMvc mvc;

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @DisplayName("hello")
    @Test
    public void hello() throws Exception {
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
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test")));

        verify(dcService, times(1)).getList();
    }

    @DisplayName("데이터센터 추가")
    @Test
    public void addDc() throws Exception {
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


    @DisplayName("데이터센터 편집")
    @Test
    public void editDc() throws Exception {

    }

}
