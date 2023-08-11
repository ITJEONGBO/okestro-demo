package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.SystemPermissionsService;
import com.itinfo.service.UsersService;
import com.itinfo.model.PermissionVo;
import com.itinfo.model.RoleVo;
import com.itinfo.model.UserVo;

import java.util.List;

import io.swagger.annotations.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@Api(value = "SystemPermissionsController", tags = {"system-permissions"})
public class SystemPermissionsController {
	@Autowired private SystemPermissionsService systemPermissionsService;
	@Autowired private UsersService usersService;

	@ApiOperation(httpMethod = "GET", value = "systemPermissionsView", notes = "페이지 이동 > /admin/systemPermissions")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/systemPermissions"})
	public String systemPermissionsView() {
		log.info("systemPermissionsView ...");
		return "/castanets/admin/systemPermissions";
	}

	@ApiOperation(httpMethod = "GET", value = "addSystemPermissionsView", notes = "페이지 이동 > /admin/systemPermissions/viewAddSystemPermissions")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/systemPermissions/viewAddSystemPermissions"})
	public String addSystemPermissionsView() {
		log.info("addSystemPermissionsView ...");
		return "/castanets/admin/addSystemPermissions";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveSystemPermissions", notes = "시스템 권한 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/systemPermissions/retrieveSystemPermissions"})
	public String retrieveSystemPermissions(Model model) {
		log.info("retrieveSystemPermissions ...");
		List<PermissionVo> permissions = systemPermissionsService.retrieveSystemPermissions();
		model.addAttribute(ItInfoConstant.RESULT_KEY, permissions);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveRoles", notes = "역할 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/systemPermissions/retrieveRoles"})
	public String retrieveRoles(Model model) {
		log.info("retrieveRoles ...");
		List<RoleVo> roles = systemPermissionsService.retrieveRoles();
		model.addAttribute(ItInfoConstant.RESULT_KEY, roles);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveAllUsers", notes = "사용자 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/users/retrieveAllUsers"})
	public String retrieveAllUsers(Model model) {
		log.info("retrieveAllUsers ...");
		List<UserVo> users = this.usersService.retrieveUsers();
		model.addAttribute(ItInfoConstant.RESULT_KEY, users);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "addSystemPermissions", notes = "시스템 권한 등록")
	@ApiImplicitParams({
			@ApiImplicitParam(name="users", value = "등록대상 사용자")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/admin/systemPermissions/addSystemPermissions"})
	public String addSystemPermissions(@RequestBody List<UserVo> users, 
									   Model model) {
		log.info("addSystemPermissions[{}] ...", users.size());
		systemPermissionsService.addSystemPermissions(users);
		return ItInfoConstant.JSON_VIEW;
	}


	@ApiOperation(httpMethod = "POST", value = "removeSystemPermissions", notes = "시스템 권한 삭제")
	@ApiImplicitParams({
			@ApiImplicitParam(name="permissions", value = "제거대상 권한 ")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/admin/systemPermissions/removeSystemPermissions"})
	public String removeSystemPermissions(@RequestBody List<PermissionVo> permissions, 
										  Model model) {
		log.info("removeSystemPermissions[{}] ...", permissions.size());
		systemPermissionsService.removeSystemPermissions(permissions);
		return ItInfoConstant.JSON_VIEW;
	}
}

