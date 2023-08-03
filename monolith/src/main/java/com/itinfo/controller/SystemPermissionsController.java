package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.SystemPermissionsService;
import com.itinfo.service.UsersService;
import com.itinfo.model.PermissionVo;
import com.itinfo.model.RoleVo;
import com.itinfo.model.UserVo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SystemPermissionsController {
	@Autowired private SystemPermissionsService systemPermissionsService;
	@Autowired private UsersService usersService;

	@RequestMapping({"/admin/systemPermissions"})
	public String viewSystemPermissions() {
		return "/castanets/admin/systemPermissions";
	}

	@RequestMapping({"/admin/systemPermissions/viewAddSystemPermissions"})
	public String viewAddSystemPermissions() {
		return "/castanets/admin/addSystemPermissions";
	}

	@RequestMapping({"/admin/systemPermissions/retrieveSystemPermissions"})
	public String retrieveSystemPermissions(Model model) {
		List<PermissionVo> permissions = this.systemPermissionsService.retrieveSystemPermissions();
		model.addAttribute(ItInfoConstant.RESULT_KEY, permissions);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/systemPermissions/retrieveRoles"})
	public String retrieveRoles(Model model) {
		List<RoleVo> roles = this.systemPermissionsService.retrieveRoles();
		model.addAttribute(ItInfoConstant.RESULT_KEY, roles);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/users/retrieveAllUsers"})
	public String retrieveAllUsers(Model model) {
		List<UserVo> users = this.usersService.retrieveUsers();
		model.addAttribute(ItInfoConstant.RESULT_KEY, users);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/systemPermissions/addSystemPermissions"})
	public String addSystemPermissions(@RequestBody List<UserVo> users, Model model) {
		this.systemPermissionsService.addSystemPermissions(users);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/systemPermissions/removeSystemPermissions"})
	public String removeSystemPermissions(@RequestBody List<PermissionVo> permissions, Model model) {
		this.systemPermissionsService.removeSystemPermissions(permissions);
		return ItInfoConstant.JSON_VIEW;
	}
}

