package com.itinfo.itcloud.admin.user;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class UserServiceImpl implements UserService {

    @Inject
    UserDAO userDAO;

    public UserServiceImpl() {
    }

    /*@Override
    public int getTestValue(){
        return userDAO.getTestValue();
    }

    @Override
    public String login(LoginDTO loginDTO) {
        return userDAO.login(loginDTO);
    }

    @Override
    public void logout(HttpSession httpSession) {
        httpSession.invalidate(); // 세션 초기화
    }

    @Override
    public List<UserVO> allUsers() {
        return userDAO.allUsers();
    }*/


}
