package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.SystemPropertiesService;
import com.itinfo.model.SystemPropertiesVo;

import io.swagger.annotations.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@Api(value = "SystemPropertiesController", tags = {"system-properties"})
public class SystemPropertiesController {
	@Autowired private SystemPropertiesService systemPropertiesService;

	@ApiOperation(httpMethod = "GET", value = "systemPropertiesView", notes = "페이지 이동 > /admin/systemProperties")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/systemProperties"})
	public String systemPropertiesView() {
		log.info("... systemPropertiesView");
		return "/castanets/admin/systemProperties";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveSystemProperties", notes = "시스템 속성값 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/retrieveSystemProperties"})
	public String retrieveSystemProperties(Model model) {
		log.info("... retrieveSystemProperties");
		SystemPropertiesVo systemProperties = systemPropertiesService.retrieveSystemProperties();
		model.addAttribute(ItInfoConstant.RESULT_KEY, systemProperties);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "saveSystemProperties", notes = "시스템 속성값 저장")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/admin/saveSystemProperties"})
	public String saveSystemProperties(@RequestBody SystemPropertiesVo systemProperties,
									   Model model) {
		log.info("... saveSystemProperties");
		int result = systemPropertiesService.saveSystemProperties(systemProperties);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveProgramVersion", notes = "프로그램 버전 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/retrieveProgramVersion"})
	public String retrieveProgramVersion(Model model) {
		log.info("... retrieveProgramVersion");
		Object[] result = systemPropertiesService.retrieveProgramVersion();
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

}
