package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.service.ItTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TemplateController {
	private final ItTemplateService itTemplateService;

/*
	@GetMapping("/computing/templates")
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
	}
*/
}
