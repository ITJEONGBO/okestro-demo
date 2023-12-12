package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.service.SystemPropertiesService;
import com.itinfo.itcloud.service.UserService;
import com.itinfo.itcloud.ovirt.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("LoginController")
@Slf4j
public class LoginController {

    @Autowired
    private ConnectionService ovirtConnection;

    @Autowired
    private SystemPropertiesService systemPropertiesService;

    @Autowired
    private UserService userService;


    public LoginController(){}

    // 로그인 페이지
    @GetMapping("/login")
    public String login(){
        log.info("-----login");
        return "login/loginpage";
    }

    /* name이 null이면 login이 나오는데 로그인이 되지 않으면 이상한 페이지가 뜸 *//*
    // login_check 명으로 dashboard가 뜸
    @PostMapping("/login_check")
    public String login_check(LoginDTO loginDTO, Model model, HttpServletRequest httpServletRequest) {
        String name = userService.login(loginDTO);
        String id = loginDTO.getId();

        SystemPropertiesVO systemPropertiesVO = this.systemPropertiesService.searchSystemProperties();
        if(systemPropertiesVO.getId() != "" && systemPropertiesVO.getPassword() != "" && systemPropertiesVO.getIp() != ""){
            return "redirect:dashboard";
        }else{
            return "redirect:login/loginpage";
        }

    }



    // ovirt 연결 테스트
    @GetMapping("/scopetest")
    public String scopeTest(Model model, HttpServletRequest request) {
        model.addAttribute("uid", this.ovirtConnection.getUid());
        return "scopetest";
    }


    // db연결 확인 테스트
    @GetMapping("/userCount")
    public String goTestPage(Model model, HttpServletRequest request) {
        int result = 0;
        result = userService.getTestValue();

        model.addAttribute("userCount", result);
        return "user";
    }

    // json 형식
    @GetMapping("/testuser")
    @ResponseBody
    public List<UserVO> list(){
        List<UserVO> users = null;
        try {
            users = userService.allUsers();
        }catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }
*/

}
