package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItTemplateServiceTest {
    @Autowired
    ItTemplateService templateService;

    String defaultId = "00000000-0000-0000-0000-000000000000";
    String id = "41301e9d-20a1-44f1-9f86-c67bc581d453";

    @Test
    @DisplayName("템플릿 목록")
    void getList() {
        List<TemplateVo> result = templateService.getTemplates();

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("템플릿 생성창")
    void clusters() {
        List<ClusterVo> result = templateService.setClusterList();

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("템플릿 생성")
    void addTemplate() {
        String vmId = "8e812723-305c-41da-8dee-e4713b8020f3";
        TemplateVo templateVo =
                TemplateVo.builder()
                        .name("testMe2")
//                        .clusterId()
                        .build();

        CommonVo<Boolean> result = templateService.addTemplate(vmId, templateVo);

    }

    @Test
    @DisplayName("템플릿 수정창")
    void setTemplate() {
        TemplateVo result = templateService.setTemplate(id);

        System.out.println(result);
    }

    @Test
    @DisplayName("템플릿 수정")
    void editTemplate() {
    }

    @Test
    @DisplayName("템플릿 삭제")
    void deleteTemplate() {
    }


    @Test
    @DisplayName("템플릿 일반")
    void getInfo(){
        TemplateVo result = templateService.getTemplateInfo(id);

        System.out.println(result);
    }


    @Test
    @DisplayName("템플릿 가상머신 목록")
    void getVm() {
        List<VmVo> result = templateService.getVmsByTemplate(id);

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("템플릿 네트워크 인터페이스 목록")
    void getNic() {
        List<NicVo> result = templateService.getNicsByTemplate(id);

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("템플릿 디스크 목록")
    void getDiskList(){
        List<DiskVo> result = templateService.getDisksByTemplate(id);

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("템플릿 스토리지 도메인 목록")
    void getDomain() {
        List<DomainVo> result = templateService.getDomainsByTemplate(id);

        result.forEach(System.out::println);
    }

    @Test
    void getPermission() {
    }

    @Test
    @DisplayName("템플릿 이벤트 목록")
    void getEvent() {
        String id = "a03f9c8e-3668-489b-8aac-2fe8e495b2e3";
        List<EventVo> result = templateService.getEventsByTemplate(id);

        result.forEach(System.out::println);
    }
}