package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.MacAddressService;
import com.itinfo.model.MacAddressPoolsVo;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class MacAddressPoolsController {
	@Autowired private MacAddressService macAddressService;

	@RequestMapping({"/admin/macAddressPools"})
	public String macAddressPools() {
		log.info("... macAddressPools");
		return "/castanets/admin/macAddressPools";
	}

	@RequestMapping(value = {"/admin/retrieveMacAddressPools"}, method = {RequestMethod.GET})
	public String retrieveMacAddressPools(Model model) {
		log.info("... retrieveMacAddressPools");
		List<MacAddressPoolsVo> macAddressVo = macAddressService.retrieveMacAddressPools();
		model.addAttribute(ItInfoConstant.RESULT_KEY, macAddressVo);
		return ItInfoConstant.JSON_VIEW;
	}
}

