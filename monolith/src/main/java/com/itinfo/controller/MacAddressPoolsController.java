package com.itinfo.controller;

import com.itinfo.service.MacAddressService;
import com.itinfo.model.MacAddressPoolsVo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MacAddressPoolsController {
	@Autowired private MacAddressService macAddressService;

	@RequestMapping({"/admin/macAddressPools"})
	public String macAddressPools() {
		return "/castanets/admin/macAddressPools";
	}

	@RequestMapping(value = {"/admin/retrieveMacAddressPools"}, method = {RequestMethod.GET})
	public String retrieveMacAddressPools(Model model) {
		List<MacAddressPoolsVo> macAddressVo = this.macAddressService.retrieveMacAddressPools();
		model.addAttribute("resultKey", macAddressVo);
		return "jsonView";
	}
}

