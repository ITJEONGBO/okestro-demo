package com.itinfo.itcloud.dao.impl;

import com.itinfo.itcloud.dao.UserDAO;
import com.itinfo.itcloud.model.UserVO;
import com.itinfo.itcloud.model.LoginDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SqlSession SqlSession;


    @Override
    public int getTestValue() {
        return SqlSession.selectOne("Login.userCount");
    }

    @Override
    public String login(LoginDTO loginDTO) {
        return SqlSession.selectOne("Login.login", loginDTO);
    }

    @Override
    public List<UserVO> allUsers() {
        return SqlSession.selectList("Login.users");
    }



}
