package com.itinfo.controller;
import com.itinfo.ItInfoConstant;
import com.itinfo.service.InstanceTypesService;
import com.itinfo.model.InstanceTypeVo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("instanceTypesController")
public class InstanceTypesController {
	@Autowired private InstanceTypesService instanceTypesService;

	@RequestMapping({"/admin/instanceTypes"})
	public String instanceTypes() {
		return "/castanets/admin/instanceTypes";
	}

	@RequestMapping({"/admin/createInstanceType"})
	public String createInstanceTypeView() {
		return "/castanets/admin/createInstanceType";
	}

	@RequestMapping({"/admin/updateInstanceType"})
	public String updateInstanceTypeView() {
		return "/castanets/admin/updateInstanceType";
	}

	@RequestMapping(value = {"/admin/retrieveInstanceTypes"}, method = {RequestMethod.GET})
	public String retrieveInstanceTypes(Model model) {
		List<InstanceTypeVo> instanceTypes = this.instanceTypesService.retrieveInstanceTypes();
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceTypes);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/admin/retrieveInstanceTypeCreateInfo"}, method = {RequestMethod.GET})
	public String retrieveInstanceTypeCreateInfo(Model model) throws Exception {
		InstanceTypeVo instanceType = this.instanceTypesService.retrieveInstanceTypeCreateInfo();
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceType);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/instanceType/createInstanceType"})
	public String createInstanceType(@RequestBody InstanceTypeVo instanceType, Model model) {
		String instanceTypeId = this.instanceTypesService.createInstanceType(instanceType);
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceTypeId);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/retrieveInstanceTypeUpdateInfo"})
	public String retrieveInstanceTypeUpdateInfo(String id, Model model) {
		InstanceTypeVo instanceType = this.instanceTypesService.retrieveInstanceTypeUpdateInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceType);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/instanceType/updateInstanceType"})
	public String updateInstanceType(@RequestBody InstanceTypeVo instanceType, Model model) {
		String instanceTypeId = this.instanceTypesService.updateInstanceType(instanceType);
		model.addAttribute(ItInfoConstant.RESULT_KEY, instanceTypeId);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/instanceType/removeInstanceType"})
	public String removeInstanceType(@RequestBody InstanceTypeVo instanceType, Model model) {
		String result = this.instanceTypesService.removeInstanceType(instanceType);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}
}
