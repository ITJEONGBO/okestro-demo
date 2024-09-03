package com.itinfo.itcloud.controller.auth

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.ItCloudOutput
import com.itinfo.itcloud.aaarepository.entity.OvirtUser
import com.itinfo.itcloud.controller.BaseController
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.auth.UserVo

import com.itinfo.itcloud.service.auth.ItOvirtUserService
import com.itinfo.util.ovirt.error.ErrorPattern
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@Api(tags = ["Login"])
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

	@ApiOperation(
		httpMethod="GET",
		value="아이디/비밀번호 검증 (테스트용)",
		notes="사용자의 아이디/비밀번호가 맞는지 확인한다.")
	@ApiImplicitParams(
		ApiImplicitParam(name="username", value="ovirt 사용자 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="password", value="ovirt 사용자 비밀번호", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "성공"),
		ApiResponse(code = 404, message = "찾을 수 없는 사용자")
	)
	@GetMapping("users/{username}/{password}")
	fun validateUser(
		@PathVariable username: String = "",
		@PathVariable password: String = ""
	): ResponseEntity<Boolean> {
		log.info("findPasswordEncrypted ... username: {}, password: {}", username, password)
		val res: Boolean = ovirtUser.authenticateUser(username, password)
		return ResponseEntity.ok(res)
	}

	@ApiOperation(
		httpMethod="GET",
		value="비밀번호 변경",
		notes="비밀번호 변경")
	@ApiImplicitParams(
		ApiImplicitParam(name="username", value="ovirt 사용자 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="currentPassword", value="ovirt 사용자 기존 비밀번호", dataTypeClass=String::class, required=true, paramType="query"),
		ApiImplicitParam(name="newPassword", value="ovirt 사용자 신규 비밀번호", dataTypeClass=String::class, required=true, paramType="query"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "성공"),
		ApiResponse(code = 401, message = "인증 불량"),
		ApiResponse(code = 404, message = "찾을 수 없는 사용자")
	)
	@PutMapping("users/{username}")
	fun changePassword(
		@PathVariable(required=true) username: String = "",
		@RequestParam(required=true) currentPassword: String? = null,
		@RequestParam(required=true) newPassword: String? = null,
	):  ResponseEntity<OvirtUser> {
		log.info("changePassword ... username: {}", username)
		if (username.isEmpty()) throw ErrorPattern.OVIRTUSER_ID_NOT_FOUND.toException()
		if (currentPassword.isNullOrEmpty()) throw ErrorPattern.REQUIRED_VALUE_EMPTY.toException()
		if (newPassword.isNullOrEmpty()) throw ErrorPattern.REQUIRED_VALUE_EMPTY.toException()
		val res: OvirtUser = ovirtUser.changePassword(username, currentPassword, newPassword)
		return ResponseEntity.ok(res)
	}


	@GetMapping("users")
	fun findAllOvirtUsers(): ResponseEntity<List<UserVo>> {
		log.info("findAllOvirtUsers ... ")
		val res: List<UserVo> = ovirtUser.findAll()
		return ResponseEntity.ok(res)
	}


	@GetMapping("password/{input}")
	fun spitPasswordEncoded(
		@PathVariable input: String = ""
	): ResponseEntity<ItCloudOutput> {
		log.info("spitPasswordEncoded ... input: {}", input)
		val res: String = ovirtUser.findEncryptedValue(input)
		return ResponseEntity.ok(ItCloudOutput(res))
	}

	companion object {
		private val log by LoggerDelegate()
	}
}