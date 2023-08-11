package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.SystemPropertiesService;
import com.itinfo.service.UsersService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.model.SystemPropertiesVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import io.swagger.annotations.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@Api(value = "LoginController", tags = {"login"})
public class LoginController extends BaseController {
	@Autowired private ConnectionService connectionService;
	@Autowired private SystemPropertiesService systemPropertiesService;
	@Autowired private UsersService usersService;

	@ApiOperation(httpMethod = "GET", value = "loginView", notes = "페이지 이동 > /login")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/login"})
	public String loginView() {
		log.info("... loginView");
		return "/castanets/login/login";
	}

	@ApiOperation(httpMethod = "GET", value = "accessDeniedView", notes = "페이지 이동 > /accessDenied")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/accessDenied"})
	public String accessDeniedView() {
		log.info("... accessDeniedView");
		return "/castanets/login/accessDenied";
	}

	@ApiOperation(httpMethod = "GET", value = "contextRoot", notes = "페이지 이동 > /dashboard")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/"})
	public String contextRoot() {
		log.info("... contextRoot");
		return "redirect:dashboard";
	}

	@ApiOperation(httpMethod = "GET", value = "scopeTest", notes = "scopeTest?")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/scopeTest"})
	public String scopeTest(Model model) {
		log.info("... scopeTest");
		model.addAttribute("connectionService uid", connectionService.getUid());
		return ItInfoConstant.JSON_VIEW;
	}

	// @GetMapping("/loginTest")
	@ApiOperation(httpMethod = "GET", value = "loginTest", notes = "loginTest??")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/loginTest"})
	public String loginTest(Model model) {
		log.info("... loginTest");
		model.addAttribute("memberService", getMemberService().getUsername());
		return ItInfoConstant.JSON_VIEW;
	}


	@ApiOperation(httpMethod = "GET", value = "loginSuccess", notes = "로그인 성공처리")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/loginSuccess"})
	public String loginSuccess(Model model,
							   HttpServletRequest httpServletRequest) {
		log.info("... loginSuccess");
		HttpSession ht = httpServletRequest.getSession();
		ht.setAttribute("userId", getMemberService().getUsername().split("@")[0]);
		model.addAttribute(ItInfoConstant.RESULT_KEY, "OK");
		SystemPropertiesVo systemProperties = systemPropertiesService.retrieveSystemProperties();
		if (!systemProperties.getIp().isEmpty() &&
			!systemProperties.getPassword().isEmpty() &&
			!systemProperties.getIp().isEmpty()) {
			model.addAttribute("redirect", "/dashboard");
		} else {
			model.addAttribute("redirect", "/admin/systemProperties");
		}
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "userConvenienceSetting", notes = "사용자 편의 설정")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/userConvenienceSetting"})
	public String userConvenienceSetting(Model model) {
		log.info("... userConvenienceSetting");
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "serverStatus", notes = "서버상태 확인")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/login/serverStatus"})
	public String serverStatus(Model model) {
		log.info("... serverStatus");
		model.addAttribute(ItInfoConstant.RESULT_KEY, "OK");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			httpMethod = "GET", //TODO POST 처리 필요
			value = "resetAdminLoginCount", notes = "로그인 회수 초기화")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "사용자 ID", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/login/resetLoginCount"})
	public String resetAdminLoginCount(String userId) {
		log.info("... resetAdminLoginCount");
		usersService.initLoginCount(userId);

		SystemPropertiesVo systemProperties = systemPropertiesService.retrieveSystemProperties();
		systemProperties.setIp("");
		systemProperties.setPassword("");
		systemProperties.setVncIp("");
		systemProperties.setVncPort("");
		systemProperties.setGrafanaUri("");
		systemProperties.setDeeplearningUri("");
		int updateResult = systemPropertiesService.saveSystemProperties(systemProperties);
		if (updateResult > 0)
			log.debug("systemProperties reset SUCCESS");
		else // -1
			log.debug("systemProperties reset FAIL");
		return ItInfoConstant.JSON_VIEW;
	}
}
