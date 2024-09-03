package com.itinfo.itcloud.controller.auth

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.controller.BaseController
import com.itinfo.itcloud.model.auth.UserVo

import com.itinfo.itcloud.service.auth.ItOvirtUserService
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@Api(tags = ["login"])
@RequestMapping("/api/v1/auth/")
class LoginController: BaseController() {
	@Autowired private lateinit var ovirtUser: ItOvirtUserService
	// name이 null이면 login이 나오는데 로그인이 되지 않으면 이상한 페이지가 뜸
	// login_check 명으로 dashboard가 뜸
/*
	@PostMapping
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
*/
	@GetMapping("users")
	fun findAllOvirtUsers(): ResponseEntity<List<UserVo>> {
		log.info("findAllOvirtUsers ... ")
		val res: List<UserVo> = ovirtUser.findAll()
		return ResponseEntity.ok(res)
	}

	companion object {
		private val log by LoggerDelegate()
	}
}