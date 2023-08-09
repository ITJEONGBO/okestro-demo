package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.SystemPermissionsService;
import com.itinfo.service.UsersService;
import com.itinfo.model.PermissionVo;
import com.itinfo.model.RoleVo;
import com.itinfo.model.UserVo;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class SystemPermissionsController {
	@Autowired private SystemPermissionsService systemPermissionsService;
	@Autowired private UsersService usersService;

	@RequestMapping({"/admin/systemPermissions"})
	public String viewSystemPermissions() {
		log.info("viewSystemPermissions ...");
		return "/castanets/admin/systemPermissions";
	}

	@RequestMapping({"/admin/systemPermissions/viewAddSystemPermissions"})
	public String viewAddSystemPermissions() {
		log.info("viewAddSystemPermissions ...");
		return "/castanets/admin/addSystemPermissions";
	}

	@RequestMapping({"/admin/systemPermissions/retrieveSystemPermissions"})
	public String retrieveSystemPermissions(Model model) {
		log.info("retrieveSystemPermissions ...");
		List<PermissionVo> permissions = systemPermissionsService.retrieveSystemPermissions();
		model.addAttribute(ItInfoConstant.RESULT_KEY, permissions);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/systemPermissions/retrieveRoles"})
	public String retrieveRoles(Model model) {
		log.info("retrieveRoles ...");
		List<RoleVo> roles = systemPermissionsService.retrieveRoles();
		model.addAttribute(ItInfoConstant.RESULT_KEY, roles);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/users/retrieveAllUsers"})
	public String retrieveAllUsers(Model model) {
		log.info("retrieveAllUsers ...");
		List<UserVo> users = this.usersService.retrieveUsers();
		model.addAttribute(ItInfoConstant.RESULT_KEY, users);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/systemPermissions/addSystemPermissions"})
	public String addSystemPermissions(@RequestBody List<UserVo> users, Model model) {
		log.info("addSystemPermissions[{}] ...", users.size());
		systemPermissionsService.addSystemPermissions(users);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/admin/systemPermissions/removeSystemPermissions"})
	public String removeSystemPermissions(@RequestBody List<PermissionVo> permissions, Model model) {
		log.info("removeSystemPermissions[{}] ...", permissions.size());
		systemPermissionsService.removeSystemPermissions(permissions);
		return ItInfoConstant.JSON_VIEW;
	}
}

