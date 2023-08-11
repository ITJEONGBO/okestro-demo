package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.UsersService;
import com.itinfo.model.UserVo;
import com.itinfo.security.SecurityUtils;

import java.util.List;

import io.swagger.annotations.*;

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
@Api(value = "UsersController", tags = {"users"})
public class UsersController {
	@Autowired private UsersService usersService;
	@Autowired private SecurityUtils securityUtils;

	@ApiOperation(httpMethod = "GET", value = "usersView", notes = "페이지 이동 > /admin/users")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/users"})
	public String usersView() {
		log.info("... usersView");
		return "/castanets/admin/users";
	}

	@ApiOperation(httpMethod = "GET", value = "addUsersView", notes = "페이지 이동 > /admin/addUser")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/users/viewAddUser"})
	public String addUsersView() {
		log.info("... addUsersView");
		return "/castanets/admin/addUser";
	}

	@ApiOperation(httpMethod = "GET", value = "passwordView", notes = "페이지 이동 > /admin/users/password")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/users/password"})
	public String passwordView() {
		log.info("... passwordView");
		return "/castanets/admin/password";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveUsers", notes = "사용자 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/users/retrieveUsers"})
	public String retrieveUsers(Model model) {
		log.info("... retrieveUsers");
		List<UserVo> users = usersService.retrieveUsers();
		model.addAttribute(ItInfoConstant.RESULT_KEY, users);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveUser", notes = "사용자 상세 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value = "사용자 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/admin/users/retrieveUser"})
	public String retrieveUser(String id, Model model) {
		log.info("... retrieveUser('{}')", id);
		UserVo user = usersService.retrieveUser(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, user);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "removeUsers", notes = "사용자 삭제")
	@ApiImplicitParams({
			@ApiImplicitParam(name="users", value = "사용자 (여러개)")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/admin/users/removeUsers"})
	public String removeUsers(@RequestBody List<UserVo> users, Model model) {
		log.info("... removeUsers[{}]", users.size());
		int count = usersService.removeUsers(users);
		model.addAttribute(ItInfoConstant.RESULT_KEY, count);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "addUser", notes = "사용자 등록")
	@ApiImplicitParams({
			@ApiImplicitParam(name="user", value = "등록할 사용자 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/admin/users/addUser"})
	public String addUser(@RequestBody UserVo user, Model model) {
		log.info("... addUser");
		if (Base64.isArrayByteBase64(user.getPassword().getBytes()))
			user.setPassword(this.securityUtils.decodeBase64(user.getPassword()));
		if (usersService.isExistUser(user)) {
			model.addAttribute(ItInfoConstant.RESULT_KEY, 0);
		} else {
			int count = usersService.addUser(user);
			model.addAttribute(ItInfoConstant.RESULT_KEY, count);
		}
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "updateUser", notes = "사용자 정보 수정")
	@ApiImplicitParams({
			@ApiImplicitParam(name="user", value = "수정할 사용자 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/admin/users/updateUser"})
	public String updateUser(@RequestBody UserVo user, Model model) {
		log.info("... updateUser");
		int count = usersService.updateUser(user);
		model.addAttribute(ItInfoConstant.RESULT_KEY, count);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "updatePassword", notes = "사용자 비밀번호 갱신")
	@ApiImplicitParams({
			@ApiImplicitParam(name="user", value = "갱신할 사용자 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/admin/users/updatePassword"})
	public String updatePassword(@RequestBody UserVo user, Model model) {
		log.info("... updatePassword");
		String password = usersService.login(user.getId());
		if (Base64.isArrayByteBase64(user.getPassword().getBytes()))
			user.setPassword(securityUtils.decodeBase64(user.getPassword()));
		if (SecurityUtils.validatePassword(user.getPassword(), password)) {
			if (Base64.isArrayByteBase64(user.getNewPassword().getBytes()))
				user.setNewPassword(securityUtils.decodeBase64(user.getNewPassword()));
			Integer count = usersService.updatePassword(user);
			model.addAttribute(ItInfoConstant.RESULT_KEY, count);
		} else {
			model.addAttribute(ItInfoConstant.RESULT_KEY, "비밀번호를 정확하게 입력해 주십시오.");
		}
		return ItInfoConstant.JSON_VIEW;
	}
}
