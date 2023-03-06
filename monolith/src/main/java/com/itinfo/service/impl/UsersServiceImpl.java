package com.itinfo.service.impl;

import com.itinfo.dao.UsersDao;
import com.itinfo.service.UsersService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.model.UserVo;
import com.itinfo.security.SecurityUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("usersServiceImpl")
public class UsersServiceImpl implements UsersService {
	@Autowired private ConnectionService connectionService;
	@Autowired private UsersDao usersDao;
	@Autowired private SecurityUtils securityUtils;

	public List<UserVo> retrieveUsers() {
		return this.usersDao.retrieveUsers();
	}

	public Integer removeUsers(List<UserVo> users) {
		return this.usersDao.removeUsers(users);
	}

	public Boolean isExistUser(UserVo user) {
		return this.usersDao.isExistUser(user);
	}

	public Integer addUser(UserVo user) {
		user.setPassword(SecurityUtils.createHash(user.getPassword()));
		return this.usersDao.addUser(user);
	}

	public String login(String id) {
		return this.usersDao.login(id);
	}

	public UserVo retrieveUser(String id) {
		return this.usersDao.retrieveUser(id);
	}

	public Integer updateUser(UserVo user) {
		return this.usersDao.updateUser(user);
	}

	public Integer updatePassword(UserVo user) {
		user.setNewPassword(SecurityUtils.createHash(user.getNewPassword()));
		return this.usersDao.updatePassword(user);
	}

	public Integer updateLoginCount(UserVo user) {
		return this.usersDao.updateLoginCount(user);
	}

	public void setBlockTime(UserVo user) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String blockTime = LocalDateTime.now().plusMinutes(10L).format(formatter);
		user.setBlockTime(blockTime);
		this.usersDao.setBlockTime(user);
	}

	public void initLoginCount(String userId) {
		this.usersDao.initLoginCount(userId);
	}
}
