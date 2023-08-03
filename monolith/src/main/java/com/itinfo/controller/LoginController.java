package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.SystemPropertiesService;
import com.itinfo.service.UsersService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.model.SystemPropertiesVo;
import com.itinfo.security.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@Slf4j
public class LoginController {
	@Autowired private ConnectionService connectionService;
	@Autowired private SystemPropertiesService systemPropertiesService;
	@Autowired private UsersService usersService;

	@RequestMapping({"/scopeTest"})
	public String scopeTest(Model model) {
		model.addAttribute("connectionService uid", this.connectionService.getUid());
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/loginTest"})
	public String loginTest(Model model) {
		MemberService memberService = (MemberService)SecurityContextHolder.getContext().getAuthentication().getDetails();
		model.addAttribute("memberService", memberService.getUsername());
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/login"})
	public String loginView() {
		return "/castanets/login/login";
	}

	@RequestMapping({"/accessDenied"})
	public String accessDeniedView() {
		return "/castanets/login/accessDenied";
	}

	@RequestMapping({"/loginSuccess"})
	public String loginSuccess(Model model, HttpServletRequest httpServletRequest) {
		MemberService memberService = (MemberService)SecurityContextHolder.getContext().getAuthentication().getDetails();
		HttpSession ht = httpServletRequest.getSession();
		ht.setAttribute("userId", memberService.getUsername().split("@")[0]);
		model.addAttribute(ItInfoConstant.RESULT_KEY, "OK");
		SystemPropertiesVo systemProperties = this.systemPropertiesService.retrieveSystemProperties();
		if (!systemProperties.getIp().equals("") && !systemProperties.getPassword().equals("") && !systemProperties.getIp().equals("")) {
			model.addAttribute("redirect", "/dashboard");
		} else {
			model.addAttribute("redirect", "/admin/systemProperties");
		}
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/userConvenienceSetting"})
	public String userConvenienceSetting(Model model) {
		model.addAttribute("result", "oke");
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/"})
	public String contextRoot() {
		return "redirect:dashboard";
	}

	@RequestMapping({"/login/serverStatus"})
	public String serverStatus(Model model) {
		model.addAttribute(ItInfoConstant.RESULT_KEY, "OK");
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/login/resetLoginCount"})
	public String resetAdminLoginCount(String userId) {
		int updateResult = -1;
		this.usersService.initLoginCount(userId);
		SystemPropertiesVo systemProperties = this.systemPropertiesService.retrieveSystemProperties();
		systemProperties.setIp("");
		systemProperties.setPassword("");
		systemProperties.setVncIp("");
		systemProperties.setVncPort("");
		systemProperties.setGrafanaUri("");
		systemProperties.setDeepLearningUri("");
		updateResult = this.systemPropertiesService.saveSystemProperties(systemProperties);
		if (updateResult > 0)
			log.debug("systemProperties reset SUCCESS");
		else
			log.debug("systemProperties reset FAIL");
		return ItInfoConstant.JSON_VIEW;
	}
}
