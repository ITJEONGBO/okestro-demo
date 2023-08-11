package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.InstanceTypesService;
import com.itinfo.model.InstanceTypeVo;

import java.util.List;

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
@Api(value = "InstanceTypesController", tags = {"instance-type"})
public class InstanceTypesController {
	@Autowired private InstanceTypesService instanceTypesService;

	@ApiOperation(httpMethod = "GET", value = "instanceTypesView", notes = "페이지 이동 > /admin/instanceTypes")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/instanceTypes"})
	public String instanceTypesView() {
		log.info("... instanceTypesView");
		return "/castanets/admin/instanceTypes";
	}

	@ApiOperation(httpMethod = "GET", value = "createInstanceTypeView", notes = "페이지 이동 > /admin/createInstanceType")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/createInstanceType"})
	public String createInstanceTypeView() {
		log.info("... createInstanceTypeView");
		return "/castanets/admin/createInstanceType";
	}

	@ApiOperation(httpMethod = "GET", value = "updateInstanceTypeView", notes = "페이지 이동 > /admin/updateInstanceType")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/updateInstanceType"})
	public String updateInstanceTypeView() {
		log.info("... updateInstanceTypeView");
		return "/castanets/admin/updateInstanceType";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveInstanceTypes", notes = "인스턴스 유형 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/retrieveInstanceTypes"})
	public String retrieveInstanceTypes(Model model) {
		log.info("... retrieveInstanceTypes");
		List<InstanceTypeVo> instanceTypes
				= instanceTypesService.retrieveInstanceTypes();
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceTypes);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveInstanceTypeCreateInfo", notes = "인스턴스 유형 생성 정보 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/retrieveInstanceTypeCreateInfo"})
	public String retrieveInstanceTypeCreateInfo(Model model) {
		log.info("... retrieveInstanceTypeCreateInfo");
		InstanceTypeVo instanceType
				= instanceTypesService.retrieveInstanceTypeCreateInfo();
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceType);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "createInstanceType", notes = "인스턴스 유형 생성")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "instanceType", value = "생성할 인스턴스 유형")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/admin/instanceType/createInstanceType"})
	public String createInstanceType(@RequestBody InstanceTypeVo instanceType,
									 Model model) {
		log.info("... createInstanceType");
		String instanceTypeId
				= instanceTypesService.createInstanceType(instanceType);
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceTypeId);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveInstanceTypeUpdateInfo", notes = "인스턴스 유형 갱신 정보 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "갱신할 인스턴스 유형 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/retrieveInstanceTypeUpdateInfo"})
	public String retrieveInstanceTypeUpdateInfo(String id,
												 Model model) {
		log.info("... retrieveInstanceTypeUpdateInfo('{}')", id);
		InstanceTypeVo instanceType
				= instanceTypesService.retrieveInstanceTypeUpdateInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceType);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "updateInstanceType", notes = "인스턴스 유형 갱신")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "instanceType", value = "갱신할 인스턴스 유형")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/admin/instanceType/updateInstanceType"})
	public String updateInstanceType(@RequestBody InstanceTypeVo instanceType,
									 Model model) {
		log.info("... updateInstanceType");
		String instanceTypeId
				= instanceTypesService.updateInstanceType(instanceType);
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceTypeId);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "removeInstanceType", notes = "인스턴스 유형 제거")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "instanceType", value = "제거할 인스턴스 유형")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/admin/instanceType/removeInstanceType"})
	public String removeInstanceType(@RequestBody InstanceTypeVo instanceType,
									 Model model) {
		log.info("... removeInstanceType");
		String result
				= instanceTypesService.removeInstanceType(instanceType);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}
}
