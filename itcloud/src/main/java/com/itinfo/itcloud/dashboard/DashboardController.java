package com.itinfo.itcloud.dashboard;

import com.itinfo.itcloud.VO.DashBoardVO;
import com.itinfo.itcloud.ovirt.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("DashboardController")
@Slf4j
public class DashboardController {

    @Autowired
    private ConnectionService ovirtConnection;

    @Autowired
    private DashboardService dashboardService;

    public DashboardController() {
    }


    // dashboard 화면
    @GetMapping("/dashboard")
    public String dashboard(Model model){
        DashBoardVO dashboard = dashboardService.showDashboard();
        model.addAttribute("dashboard", dashboard);
        return "dashboard";
    }

    @GetMapping("/dashboardStatus")
    @ResponseBody
    public DashBoardVO compute(){
        log.info("--- databaord");

        DashBoardVO dashboardStatus = null;

        long start = System.currentTimeMillis();

        try{
            dashboardStatus = dashboardService.showDashboard();
        }catch (Exception e){
            e.printStackTrace();
        }

        long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        System.out.println("수행시간(ms): " + (end-start));
        return dashboardStatus;
    }


}
