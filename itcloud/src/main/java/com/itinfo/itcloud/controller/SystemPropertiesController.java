package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.service.SystemPropertiesService;
import com.itinfo.itcloud.model.SystemPropertiesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemPropertiesController {

    @Autowired
    private SystemPropertiesService systemPropertiesService;

    public SystemPropertiesController(){}

    @GetMapping("/admin/setting")
    public String setting(Model model){
        SystemPropertiesVO systemProperties = this.systemPropertiesService.searchSystemProperties();
        model.addAttribute("setting", systemProperties);
        return "admin/setting";
    }

    @GetMapping("/admin/searchSystemProperties")
    @ResponseBody
    public SystemPropertiesVO searchSystemProperties() {
        SystemPropertiesVO systemPropertiesVO = null;
        try{
            systemPropertiesVO = this.systemPropertiesService.searchSystemProperties();
        }catch (Exception e){
            e.printStackTrace();
        }
        return systemPropertiesVO;
    }

    // 관리-설정-엔진 에서 id, pw, ip를 입력하고 저장을 하면 대시보드 결과가 뜬다.
    // 이거는 json방식이긴한데.. 일단 나중에
    @GetMapping("/admin/saveSystemProperties")
    @ResponseBody
    public int systemPropertiesVO(){
        SystemPropertiesVO systemPropertiesVO = null;
        int result = 0;
        try{
            result = systemPropertiesService.saveSystemProperties(systemPropertiesVO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


}