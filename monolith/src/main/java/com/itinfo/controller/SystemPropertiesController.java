package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.SystemPropertiesService;
import com.itinfo.model.SystemPropertiesVo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class SystemPropertiesController {
	@Autowired private SystemPropertiesService systemPropertiesService;

	@RequestMapping({"/admin/systemProperties"})
	public String systemProperties() {
		log.info("... systemProperties");
		return "/castanets/admin/systemProperties";
	}

	@RequestMapping(value = {"/admin/retrieveSystemProperties"}, method = {RequestMethod.GET})
	public String retrieveSystemProperties(Model model) {
		log.info("... retrieveSystemProperties");
		SystemPropertiesVo systemProperties = systemPropertiesService.retrieveSystemProperties();
		model.addAttribute(ItInfoConstant.RESULT_KEY, systemProperties);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/saveSystemProperties"})
	public String saveSystemProperties(@RequestBody SystemPropertiesVo systemProperties, Model model) {
		log.info("... saveSystemProperties");
		int result = systemPropertiesService.saveSystemProperties(systemProperties);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/retrieveProgramVersion"})
	public String retrieveProgramVersion(Model model) {
		log.info("... retrieveProgramVersion");
		Object[] result = systemPropertiesService.retrieveProgramVersion();
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

}
