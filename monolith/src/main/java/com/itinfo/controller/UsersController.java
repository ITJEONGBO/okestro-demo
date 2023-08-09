package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.UsersService;
import com.itinfo.model.UserVo;
import com.itinfo.security.SecurityUtils;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class UsersController {
	@Autowired private UsersService usersService;
	@Autowired private SecurityUtils securityUtils;

	@RequestMapping({"/admin/users"})
	public String viewUsers() {
		log.info("... viewUsers");
		return "/castanets/admin/users";
	}

	@RequestMapping({"/admin/users/viewAddUser"})
	public String viewAddUsers() {
		log.info("... viewAddUsers");
		return "/castanets/admin/addUser";
	}

	@RequestMapping({"/admin/users/password"})
	public String viewPassword() {
		log.info("... viewPassword");
		return "/castanets/admin/password";
	}

	@RequestMapping({"/admin/users/retrieveUsers"})
	public String retrieveUsers(Model model) {
		log.info("... retrieveUsers");
		List<UserVo> users = usersService.retrieveUsers();
		model.addAttribute(ItInfoConstant.RESULT_KEY, users);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/admin/users/retrieveUser"}, method = {RequestMethod.GET})
	public String retrieveUser(String id, Model model) {
		log.info("... retrieveUser('{}')", id);
		UserVo user = usersService.retrieveUser(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, user);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/users/removeUsers"})
	public String removeUsers(@RequestBody List<UserVo> users, Model model) {
		log.info("... removeUsers[{}]", users.size());
		int count = usersService.removeUsers(users);
		model.addAttribute(ItInfoConstant.RESULT_KEY, count);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/users/addUser"})
	public String addUser(@RequestBody UserVo user, Model model) {
		log.info("... addUser");
		if (Base64.isArrayByteBase64(user.getPassword().getBytes()))
			user.setPassword(this.securityUtils.decodeBase64(user.getPassword()));
		if (usersService.isExistUser(user)) {
			model.addAttribute(ItInfoConstant.RESULT_KEY, Integer.valueOf(0));
		} else {
			int count = usersService.addUser(user);
			model.addAttribute(ItInfoConstant.RESULT_KEY, Integer.valueOf(count));
		}
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/users/updateUser"})
	public String updateUser(@RequestBody UserVo user, Model model) {
		log.info("... updateUser");
		int count = usersService.updateUser(user);
		model.addAttribute(ItInfoConstant.RESULT_KEY, count);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/users/updatePassword"})
	public String updatePassword(@RequestBody UserVo user, Model model) {
		log.info("... updatePassword");
		String password = usersService.login(user.getId());
		if (Base64.isArrayByteBase64(user.getPassword().getBytes()))
			user.setPassword(this.securityUtils.decodeBase64(user.getPassword()));
		if (SecurityUtils.validatePassword(user.getPassword(), password)) {
			if (Base64.isArrayByteBase64(user.getNewPassword().getBytes()))
				user.setNewPassword(this.securityUtils.decodeBase64(user.getNewPassword()));
			Integer count = usersService.updatePassword(user);
			model.addAttribute(ItInfoConstant.RESULT_KEY, count);
		} else {
			model.addAttribute(ItInfoConstant.RESULT_KEY, "비밀번호를 정확하게 입력해 주십시오.");
		}
		return ItInfoConstant.JSON_VIEW;
	}
}
