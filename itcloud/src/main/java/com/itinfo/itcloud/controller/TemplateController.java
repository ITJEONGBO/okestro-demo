package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.service.ItTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TemplateController {

    @Autowired
    private ItTemplateService itTemplateService;

    public TemplateController(){}

    /*@GetMapping("/computing/templates")
    public String templates(Model model){
        List<TemplateVO> templateVOList = this.templateService.retrieveTemplates();
        model.addAttribute("tmList", templateVOList);
        return "compute/templates";
    }

    @GetMapping("/templateStatus")
    @ResponseBody
    public List<TemplateVO> temp(){
        List<TemplateVO> list = null;
        try{
            list = templateService.retrieveTemplates();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }*/


}
