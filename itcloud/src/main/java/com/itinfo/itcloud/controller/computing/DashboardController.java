package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.DashboardVo;
import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.service.ItDashboardService;

import com.itinfo.itcloud.service.ItMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

	private final ItDashboardService itDashboardService;
	private final ItMenuService menu;

	// dashboard 화면
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		DashboardVo dashboard = itDashboardService.getDashboard();
		model.addAttribute("dashboard", dashboard);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		log.info("---/dashboard");
		return "dashboard";
	}

	@GetMapping("/dashboardStatus")
	@ResponseBody
	public DashboardVo dashboard() {
		long start = System.currentTimeMillis();

		DashboardVo dashboardStatus = itDashboardService.getDashboard();

		long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		log.info("수행시간(ms): {}", end - start);

		log.info("---/dashboardStatus");
		return dashboardStatus;
	}



}

