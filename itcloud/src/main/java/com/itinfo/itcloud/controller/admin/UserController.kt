package com.itinfo.itcloud.controller.admin

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.service.admin.ItUserService
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@Api(tags = ["user"])
@RequestMapping("/api/v1/")
class UserController {
	@Autowired private lateinit var user: ItUserService
	
	companion object {
		private val log by LoggerDelegate()
	}
}