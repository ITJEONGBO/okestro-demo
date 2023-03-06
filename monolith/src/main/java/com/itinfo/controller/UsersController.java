package com.itinfo.controller;


import com.itinfo.service.UsersService;
import com.itinfo.model.UserVo;

import com.itinfo.security.SecurityUtils;

import java.util.List;

import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("usersController")
public class UsersController {
	@Autowired private UsersService usersService;
	@Autowired private SecurityUtils securityUtils;

	@RequestMapping({"/admin/users"})
	public String viewUsers() {
		return "/castanets/admin/users";
	}

	@RequestMapping({"/admin/users/viewAddUser"})
	public String viewAddUsers() {
		return "/castanets/admin/addUser";
	}

	@RequestMapping({"/admin/users/password"})
	public String viewPassword() {
		return "/castanets/admin/password";
	}

	@RequestMapping({"/admin/users/retrieveUsers"})
	public String retrieveUsers(Model model) {
		List<UserVo> users = this.usersService.retrieveUsers();
		model.addAttribute("resultKey", users);
		return "jsonView";
	}

	@RequestMapping(value = {"/admin/users/retrieveUser"}, method = {RequestMethod.GET})
	public String retrieveUser(String id, Model model) {
		UserVo user = this.usersService.retrieveUser(id);
		model.addAttribute("resultKey", user);
		return "jsonView";
	}

	@RequestMapping({"/admin/users/removeUsers"})
	public String removeUsers(@RequestBody List<UserVo> users, Model model) {
		int count = this.usersService.removeUsers(users);
		model.addAttribute("resultKey", Integer.valueOf(count));
		return "jsonView";
	}

	@RequestMapping({"/admin/users/addUser"})
	public String addUser(@RequestBody UserVo user, Model model) {
		if (Base64.isArrayByteBase64(user.getPassword().getBytes()))
			user.setPassword(this.securityUtils.decodeBase64(user.getPassword()));
		if (this.usersService.isExistUser(user)) {
			model.addAttribute("resultKey", Integer.valueOf(0));
		} else {
			int count = this.usersService.addUser(user);
			model.addAttribute("resultKey", Integer.valueOf(count));
		}
		return "jsonView";
	}

	@RequestMapping({"/admin/users/updateUser"})
	public String updateUser(@RequestBody UserVo user, Model model) {
		int count = this.usersService.updateUser(user);
		model.addAttribute("resultKey", Integer.valueOf(count));
		return "jsonView";
	}

	@RequestMapping({"/admin/users/updatePassword"})
	public String updatePassword(@RequestBody UserVo user, Model model) {
		String password = this.usersService.login(user.getId());
		if (Base64.isArrayByteBase64(user.getPassword().getBytes()))
			user.setPassword(this.securityUtils.decodeBase64(user.getPassword()));
		if (SecurityUtils.validatePassword(user.getPassword(), password)) {
			if (Base64.isArrayByteBase64(user.getNewPassword().getBytes()))
				user.setNewPassword(this.securityUtils.decodeBase64(user.getNewPassword()));
			Integer count
					= this.usersService.updatePassword(user);
			model.addAttribute("resultKey", Integer.valueOf(count));
		} else {
			model.addAttribute("resultKey", "비밀번호를 정확하게 입력해 주십시오.");
		}
		return "jsonView";
	}
}
