package com.itinfo.itcloud.controller;

import com.itinfo.common.LoggerDelegate

import io.swagger.annotations.Api
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * [DashboardController]
 * 대시보드 관련
 */
@Controller
@Api(tags = ["dashboard"])
@RequestMapping("/api/v1")
class DashboardController: BaseController() {
	@GetMapping("/vnc")
	fun vnc(): String {
		log.debug("/vnc ...")
		return "vnc"
	}

	companion object {
		private val log by LoggerDelegate()
	}
}
