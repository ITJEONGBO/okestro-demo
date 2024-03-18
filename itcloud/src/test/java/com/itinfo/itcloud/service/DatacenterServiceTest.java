package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.ovirt.engine.sdk4.types.QuotaModeType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class DatacenterServiceTest {

    ItDataCenterService dcService;


    @Test
    @DisplayName("데이터센터 생성")
    public void createMemberSuccess(){
        DataCenterCreateVo dcVo = DataCenterCreateVo.builder()
            .name("test")
            .description("testDescription")
            .storageType(false)
            .version("4.2")
            .quotaMode(QuotaModeType.DISABLED)
            .comment("testComment")
        .build();

        dcService.addDatacenter(dcVo);

//        verify(dcVo).getName();
    }


}
