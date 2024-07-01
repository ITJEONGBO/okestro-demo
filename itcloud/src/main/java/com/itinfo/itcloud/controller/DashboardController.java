package com.itinfo.itcloud.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@Api(tags = "dashboard")
public class DashboardController {

	@GetMapping("/dash")
	public String dashboard(){
		return "dashboard";
	}

	@GetMapping("/vnc")
	public String vnc(){
		return "vnc";
	}

}

