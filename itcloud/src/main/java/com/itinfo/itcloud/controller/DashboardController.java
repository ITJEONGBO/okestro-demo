package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.service.ItDashboardService;
import com.itinfo.itcloud.model.DashBoardVO;
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
    private ItDashboardService itDashboardService;

    public DashboardController() {
    }


    // dashboard 화면
    @GetMapping("/dashboard")
    public String dashboard(Model model){
        DashBoardVO dashboard = itDashboardService.showDashboard();
        model.addAttribute("dashboard", dashboard);
        log.info("---view dashboard");
        return "dashboard";
    }

    @GetMapping("/dashboardStatus")
    @ResponseBody
    public DashBoardVO compute(){
        long start = System.currentTimeMillis();

        DashBoardVO dashboardStatus = itDashboardService.showDashboard();

        long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        log.info("수행시간(ms): " + (end-start));

        log.info("----- databaordStatus");
        return dashboardStatus;
    }




}
