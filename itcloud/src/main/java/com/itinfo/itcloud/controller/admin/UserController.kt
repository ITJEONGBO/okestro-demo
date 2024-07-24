package com.itinfo.itcloud.controller.admin

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.service.admin.ItUserService
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.stereotype.Controller

@Controller
@Api(tags = ["user"])
class UserController {
	@Autowired private lateinit var user: ItUserService
	
	companion object {
		private val log by LoggerDelegate()
	}
}