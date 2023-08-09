package com.itinfo.service.impl;

import com.itinfo.dao.UsersDao;
import com.itinfo.service.UsersService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.model.UserVo;
import com.itinfo.security.SecurityUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsersServiceImpl implements UsersService {
	@Autowired private ConnectionService connectionService;
	@Autowired private UsersDao usersDao;
	@Autowired private SecurityUtils securityUtils;

	public List<UserVo> retrieveUsers() {
		log.info("... retrieveUsers");
		return usersDao.retrieveUsers();
	}

	public Integer removeUsers(List<UserVo> users) {
		log.info("... removeUsers");
		return usersDao.removeUsers(users);
	}

	public Boolean isExistUser(UserVo user) {
		log.info("... isExistUser");
		return usersDao.isExistUser(user);
	}

	public Integer addUser(UserVo user) {
		log.info("... addUser");
		user.setPassword(SecurityUtils.createHash(user.getPassword()));
		return usersDao.addUser(user);
	}

	public String login(String id) {
		log.info("... login("+id+")");
		return usersDao.login(id);
	}

	public UserVo retrieveUser(String id) {
		log.info("... retrieveUser("+id+")");
		return usersDao.retrieveUser(id);
	}

	public Integer updateUser(UserVo user) {
		log.info("... retrieveUser");
		return usersDao.updateUser(user);
	}

	public Integer updatePassword(UserVo user) {
		log.info("... updatePassword");
		user.setNewPassword(SecurityUtils.createHash(user.getNewPassword()));
		return usersDao.updatePassword(user);
	}

	public Integer updateLoginCount(UserVo user) {
		log.info("... updateLoginCount");
		return usersDao.updateLoginCount(user);
	}

	public void setBlockTime(UserVo user) {
		log.info("... setBlockTime");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String blockTime = LocalDateTime.now().plusMinutes(10L).format(formatter);
		user.setBlockTime(blockTime);
		usersDao.setBlockTime(user);
	}

	public void initLoginCount(String userId) {
		log.info("... initLoginCount("+userId+")");
		usersDao.initLoginCount(userId);
	}
}
