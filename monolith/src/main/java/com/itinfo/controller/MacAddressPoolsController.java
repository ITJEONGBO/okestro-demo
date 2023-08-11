package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.MacAddressService;
import com.itinfo.model.MacAddressPoolsVo;

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
@Api(value = "MacAddressPoolsController", tags = {"mac-address-pool"})
public class MacAddressPoolsController extends BaseController {
	@Autowired private MacAddressService macAddressService;

	@ApiOperation(httpMethod = "GET", value = "macAddressPoolsView", notes = "페이지 이동 > /admin/macAddressPools")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/macAddressPools"})
	public String macAddressPoolsView() {
		log.info("... macAddressPools");
		return "/castanets/admin/macAddressPools";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveMacAddressPools", notes = "mac address pool 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/retrieveMacAddressPools"})
	public String retrieveMacAddressPools(Model model) {
		log.info("... retrieveMacAddressPools");
		List<MacAddressPoolsVo> macAddressVo = macAddressService.retrieveMacAddressPools();
		model.addAttribute(ItInfoConstant.RESULT_KEY, macAddressVo);
		return ItInfoConstant.JSON_VIEW;
	}
}

