package com.itinfo.itcloud.controller.admin

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.ovirt.ConnectionService
import com.itinfo.itcloud.service.admin.ItSystemPropertiesService
import com.itinfo.itcloud.service.admin.ItUserService
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@Api(tags = ["login"])
class LoginController {
	@Autowired private lateinit var ovirtConnection: ConnectionService
	@Autowired private lateinit var systemPropertyService: ItSystemPropertiesService
	@Autowired private lateinit var userService: ItUserService

	// name이 null이면 login이 나오는데 로그인이 되지 않으면 이상한 페이지가 뜸
	// login_check 명으로 dashboard가 뜸
/*
	@PostMapping("/login_check")
	fun login_check(loginDTO: LoginDTO, model: Model?, httpServletRequest: HttpServletRequest?): String {
		val name: String = userService.login(loginDTO)
		val id: String = loginDTO.getId()

		val systemPropertiesVO: SystemPropertiesVO = this.systemPropertiesService.searchSystemProperties()
		return if (systemPropertiesVO.getId() !== "" && systemPropertiesVO.getPassword() !== "" && systemPropertiesVO.getIp() !== "") {
			"redirect:dashboard"
		} else {
			"redirect:login/loginpage"
		}
	}


	// ovirt 연결 테스트
	@GetMapping("/scopetest")
	fun scopeTest(model: Model, request: HttpServletRequest?): String {
		model.addAttribute("uid", ovirtConnection.getUid())
		return "scopetest"
	}


	// db연결 확인 테스트
	@GetMapping("/userCount")
	fun goTestPage(model: Model, request: HttpServletRequest?): String {
		var result = 0
		result = userService.getTestValue()

		model.addAttribute("userCount", result)
		return "user"
	}

	// json 형식
	@GetMapping("/testuser")
	@ResponseBody
	fun list(): List<UserVO>? {
		var users: List<UserVO>? = null
		try {
			users = userService.allUsers()
		} catch (e: Exception) {
			e.printStackTrace()
		}
		return users
	}
*/
	companion object {
		private val log by LoggerDelegate()
	}
}