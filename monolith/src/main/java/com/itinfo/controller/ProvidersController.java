package com.itinfo.controller;

import com.itinfo.service.ProvidersService;
import com.itinfo.model.ProviderVo;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class ProvidersController {
	@Autowired private ProvidersService providersService;

	@RequestMapping({"/admin/providers"})
	public String providersView() {
		log.info("... providersView");
		return "/castanets/admin/providers";
	}

	@RequestMapping({"/admin/retrieveProviders"})
	public String retrieveUsers(Model model) {
		log.info("... retrieveUsers");
		List<ProviderVo> providers = this.providersService.retrieveProviders();
		model.addAttribute("resultKey", providers);
		return "jsonView";
	}
}

