package com.itinfo.dao;

import com.itinfo.model.UserVo;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository("usersDao")
public class UsersDao {
	@Resource(name = "systemSqlSessionTemplate")
	private SqlSessionTemplate systemSqlSessionTemplate;
	public List<UserVo> retrieveUsers() {
		return this.systemSqlSessionTemplate.selectList("USERS.retrieveUsers");
	}

	public UserVo retrieveUser(String id) {
		return (UserVo)this.systemSqlSessionTemplate.selectOne("USERS.retrieveUser", id);
	}

	public boolean isExistUser(UserVo user) {
		return ((Boolean)this.systemSqlSessionTemplate.selectOne("USERS.isExistUser", user)).booleanValue();
	}

	public int addUser(UserVo user) {
		return this.systemSqlSessionTemplate.insert("USERS.addUser", user);
	}

	public int updateUser(UserVo user) {
		return this.systemSqlSessionTemplate.insert("USERS.updateUser", user);
	}

	public int updatePassword(UserVo user) {
		return this.systemSqlSessionTemplate.insert("USERS.updatePassword", user);
	}

	public int updateLoginCount(UserVo user) {
		return this.systemSqlSessionTemplate.insert("USERS.updateLoginCount", user);
	}

	public int setBlockTime(UserVo user) {
		return this.systemSqlSessionTemplate.insert("USERS.setBlockTime", user);
	}

	public int initLoginCount(String userId) {
		return this.systemSqlSessionTemplate.insert("USERS.initLoginCount", userId);
	}

	public int removeUsers(List<UserVo> users) {
		return this.systemSqlSessionTemplate.delete("USERS.removeUsers", users);
	}

	public String login(String id) {
		return (String)this.systemSqlSessionTemplate.selectOne("USERS.login", id);
	}
}
