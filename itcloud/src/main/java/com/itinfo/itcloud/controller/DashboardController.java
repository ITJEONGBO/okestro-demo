package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.service.ItDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
	private final ItDashboardService dashboardService;


}

