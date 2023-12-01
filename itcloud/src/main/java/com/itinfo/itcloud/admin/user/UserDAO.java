package com.itinfo.itcloud.admin.user;

import com.itinfo.itcloud.VO.UserVO;
import com.itinfo.itcloud.login.LoginDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO {

    public int getTestValue();

    public String login(LoginDTO loginDTO);

    public List<UserVO> allUsers();

}
