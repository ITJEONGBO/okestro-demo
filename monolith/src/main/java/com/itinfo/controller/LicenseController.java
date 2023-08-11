package com.itinfo.controller;

import io.swagger.annotations.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@Api(value = "LicenseController", tags = {"license"})
public class LicenseController extends BaseController {

	@ApiOperation(httpMethod = "GET", value = "systemProperties", notes = "페이지 이동 > /admin/license")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/license"})
	public String systemProperties() {
		log.info("... systemProperties");
		return "/castanets/admin/license";
	}
}
