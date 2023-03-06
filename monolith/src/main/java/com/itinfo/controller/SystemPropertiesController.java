package com.itinfo.controller;

import com.itinfo.service.SystemPropertiesService;
import com.itinfo.model.SystemPropertiesVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SystemPropertiesController {
	@Autowired private SystemPropertiesService systemPropertiesService;

	@RequestMapping({"/admin/systemProperties"})
	public String systemProperties() {
		return "/castanets/admin/systemProperties";
	}

	@RequestMapping(value = {"/admin/retrieveSystemProperties"}, method = {RequestMethod.GET})
	public String retrieveSystemProperties(Model model) {
		SystemPropertiesVo systemProperties = this.systemPropertiesService.retrieveSystemProperties();
		model.addAttribute("resultKey", systemProperties);
		return "jsonView";
	}

	@RequestMapping({"/admin/saveSystemProperties"})
	public String saveSystemProperties(@RequestBody SystemPropertiesVo systemProperties, Model model) {
		int result = this.systemPropertiesService.saveSystemProperties(systemProperties);
		model.addAttribute("resultKey", Integer.valueOf(result));
		return "jsonView";
	}

	@RequestMapping({"/admin/retrieveProgramVersion"})
	public String retrieveProgramVersion(Model model) {
		Object[] result = this.systemPropertiesService.retrieveProgramVersion();
		model.addAttribute("resultKey", result);
		return "jsonView";
	}

}
