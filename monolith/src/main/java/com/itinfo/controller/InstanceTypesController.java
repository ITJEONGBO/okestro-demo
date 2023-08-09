package com.itinfo.controller;
import com.itinfo.ItInfoConstant;
import com.itinfo.service.InstanceTypesService;
import com.itinfo.model.InstanceTypeVo;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class InstanceTypesController {
	@Autowired private InstanceTypesService instanceTypesService;

	@RequestMapping({"/admin/instanceTypes"})
	public String instanceTypes() {
		log.info("... instanceTypes");
		return "/castanets/admin/instanceTypes";
	}

	@RequestMapping({"/admin/createInstanceType"})
	public String createInstanceTypeView() {
		log.info("... createInstanceTypeView");
		return "/castanets/admin/createInstanceType";
	}

	@RequestMapping({"/admin/updateInstanceType"})
	public String updateInstanceTypeView() {
		log.info("... updateInstanceTypeView");
		return "/castanets/admin/updateInstanceType";
	}

	@RequestMapping(value = {"/admin/retrieveInstanceTypes"}, method = {RequestMethod.GET})
	public String retrieveInstanceTypes(Model model) {
		log.info("... retrieveInstanceTypes");
		List<InstanceTypeVo> instanceTypes = instanceTypesService.retrieveInstanceTypes();
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceTypes);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/admin/retrieveInstanceTypeCreateInfo"}, method = {RequestMethod.GET})
	public String retrieveInstanceTypeCreateInfo(Model model) throws Exception {
		log.info("... retrieveInstanceTypeCreateInfo");
		InstanceTypeVo instanceType = instanceTypesService.retrieveInstanceTypeCreateInfo();
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceType);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/instanceType/createInstanceType"})
	public String createInstanceType(@RequestBody InstanceTypeVo instanceType, Model model) {
		log.info("... createInstanceType");
		String instanceTypeId = instanceTypesService.createInstanceType(instanceType);
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceTypeId);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/retrieveInstanceTypeUpdateInfo"})
	public String retrieveInstanceTypeUpdateInfo(String id, Model model) {
		log.info("... retrieveInstanceTypeUpdateInfo('{}')", id);
		InstanceTypeVo instanceType = instanceTypesService.retrieveInstanceTypeUpdateInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceType);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/instanceType/updateInstanceType"})
	public String updateInstanceType(@RequestBody InstanceTypeVo instanceType, Model model) {
		log.info("... updateInstanceType");
		String instanceTypeId = instanceTypesService.updateInstanceType(instanceType);
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceTypeId);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/instanceType/removeInstanceType"})
	public String removeInstanceType(@RequestBody InstanceTypeVo instanceType, Model model) {
		log.info("... removeInstanceType");
		String result = instanceTypesService.removeInstanceType(instanceType);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}
}
