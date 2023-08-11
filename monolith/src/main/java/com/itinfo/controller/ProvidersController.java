package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.ProvidersService;
import com.itinfo.model.ProviderVo;

import java.util.List;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@Api(value = "ProvidersController", tags = {"providers"})
public class ProvidersController {
	@Autowired private ProvidersService providersService;

	@ApiOperation(httpMethod = "GET", value = "providersView", notes = "페이지 이동 > /admin/providers")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/providers"})
	public String providersView() {
		log.info("... providersView");
		return "/castanets/admin/providers";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveUsers", notes = "사용자 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping( method = {RequestMethod.GET}, value = {"/admin/retrieveProviders"})
	public String retrieveUsers(Model model) {
		log.info("... retrieveUsers");
		List<ProviderVo> providers = providersService.retrieveProviders();
		model.addAttribute(ItInfoConstant.RESULT_KEY, providers);
		return "jsonView";
	}
}

