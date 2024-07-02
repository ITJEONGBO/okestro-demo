package com.itinfo.itcloud.model;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ovirt.engine.sdk4.builders.DataCenterBuilder;
import org.ovirt.engine.sdk4.builders.VersionBuilder;
import org.ovirt.engine.sdk4.types.DataCenter;
import org.ovirt.engine.sdk4.types.QuotaModeType;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DatacenterVoTest {
    @Test
    @DisplayName("데이터 센터 객체 모델 테스트")
    void createDatacenter(){
        DataCenterVo dcVo = DataCenterVo.builder()
                .name("test")
                .description("testDescription")
                .storageType(false)
                .version("4.2")
                .quotaMode("audit")
                .comment("testComment")
                .build();

        DataCenter dataCenter = new DataCenterBuilder()
                .name(dcVo.getName())
                .description(dcVo.getDescription())
                .local(dcVo.isStorageType())
                .version(
                    new VersionBuilder()
                        .major(4)
                        .minor(2)
                    .build()
                )
                .quotaMode(QuotaModeType.AUDIT)
                .comment("testComment")
                .build();

        // TODO: ???
        assertThat(dcVo.getName()).isEqualTo(dataCenter.name());
        assertThat(dcVo.getDescription()).isEqualTo(dataCenter.description());
        assertThat(dcVo.isStorageType()).isEqualTo(dataCenter.local());
        assertThat(dcVo.getVersion()).isEqualTo("4.2");
        assertThat(dcVo.getQuotaMode()).isEqualTo(dataCenter.quotaMode().value());
        assertThat(dcVo.getComment()).isEqualTo(dataCenter.comment());
    }

    @Test
    @DisplayName("데이터 센터 편집 테스트")
    void editDatacenter(){

    }



}
