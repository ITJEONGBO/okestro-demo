package com.itinfo.service;

import com.itinfo.model.UserVo;

import java.util.List;

public interface UsersService {
	List<UserVo> retrieveUsers();
	UserVo retrieveUser(String id);
	Boolean isExistUser(UserVo user);
	Integer removeUsers(List<UserVo> users);
	Integer addUser(UserVo user);
	Integer updateUser(UserVo user);
	Integer updatePassword(UserVo user);
	Integer updateLoginCount(UserVo user);
	String login(String id);
	void setBlockTime(UserVo user);
	void initLoginCount(String userId);
}
