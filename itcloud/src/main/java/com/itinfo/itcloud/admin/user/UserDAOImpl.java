package com.itinfo.itcloud.admin.user;

import com.itinfo.itcloud.VO.UserVO;
import com.itinfo.itcloud.login.LoginDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO{

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
