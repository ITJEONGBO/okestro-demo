package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.NicVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.storage.DiskVo;
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


    @Test
    @DisplayName("템플릿 목록")
    void getList() {
        List<TemplateVo> result = templateService.getList();

        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(3);
    }


    @Test
    void addTemplate() {
    }

    @Test
    void setEditTemplate() {
    }

    @Test
    void editTemplate() {
    }

    @Test
    void deleteTemplate() {
    }


    @Test
    @DisplayName("템플릿 일반")
    void getInfo(){
        String id = "e9dec4ae-dbe3-4aea-ad37-89272c1a5c1c";
        TemplateVo result = templateService.getInfo(id);

        System.out.println(result);
    }


    @Test
    @DisplayName("템플릿 가상머신 목록")
    void getVm() {
        String id = "a03f9c8e-3668-489b-8aac-2fe8e495b2e3";
        List<VmVo> result = templateService.getVm(id);

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("템플릿 네트워크 인터페이스 목록")
    void getNic() {
        String id = "a03f9c8e-3668-489b-8aac-2fe8e495b2e3";
        List<NicVo> result = templateService.getNic(id);

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("템플릿 디스크 목록")
    void getDiskList(){
        String id = "a03f9c8e-3668-489b-8aac-2fe8e495b2e3";
        List<DiskVo> result = templateService.getDisk(id);

        result.forEach(System.out::println);
    }


    @Test
    void getDomain() {
    }

    @Test
    void getPermission() {
    }

    @Test
    @DisplayName("템플릿 이벤트 목록")
    void getEvent() {
        String id = "a03f9c8e-3668-489b-8aac-2fe8e495b2e3";
        List<EventVo> result = templateService.getEvent(id);

        result.forEach(System.out::println);
    }
}