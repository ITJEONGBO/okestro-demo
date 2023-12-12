package com.itinfo.itcloud.dao;

import com.itinfo.itcloud.model.UserVO;
import com.itinfo.itcloud.model.LoginDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO {

    public int getTestValue();

    public String login(LoginDTO loginDTO);

    public List<UserVO> allUsers();

}
