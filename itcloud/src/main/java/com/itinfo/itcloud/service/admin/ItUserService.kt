package com.itinfo.itcloud.service.admin

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.setting.UserVo
import com.itinfo.itcloud.model.setting.toAddUserBuilder
import com.itinfo.itcloud.model.setting.toUserVo
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.BasicConfiguration
import com.itinfo.util.PropertiesHelper
import com.itinfo.util.ovirt.addUser
import com.itinfo.util.ovirt.changeUserPw
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.User
import org.springframework.stereotype.Service

interface ItUserService {
	/*
	fun getTestValue(): Int
	fun login(loginDTO: LoginDTO): String
	fun logout(httpSession: HttpSession)
	fun allUsers(): List<UserVO>
	*/
	/**
	 * [ItUserService.addUser]
	 *
	 * ovirt 계정생성은 cli 로 해야함
	 * ovirt 내에서 추가하는 함수
	 * @param userVo [UserVo]
	 * @return userVo [UserVo]
	 */
	fun addUser(userVo: UserVo): UserVo
	/**
	 * [ItUserService.changePwUser]
	 *
	 * @param userVo [UserVo]
	 * @return userVo [UserVo]
	 */
	fun changePwUser(userVo: UserVo): Boolean
}

@Service
class UserServiceImpl(

): BaseService(), ItUserService {
	/*
	@Autowired private lateinit var userDAO: UserDao

	override fun getTestValue(): Int {
		return userDAO.getTestValue()
	}

	override fun login(loginDTO: LoginDTO?): String {
		return userDAO.login(loginDTO)
	}

	override fun logout(httpSession: HttpSession) {
		httpSession.invalidate() // 세션 초기화
	}

	override fun allUsers(): List<UserVO> {
		return userDAO.allUsers()
	}
	*/

	override fun addUser(userVo: UserVo): UserVo {
		log.info("addUser")
		val res: User =
			conn.addUser(userVo.toAddUserBuilder())
				.getOrNull() ?: throw ErrorPattern.UNKNOWN.toError()
		return res.toUserVo()
	}

	// TODO 비번 변경 실패
	override fun changePwUser(userVo: UserVo): Boolean {
		log.info("changePwUser")
		log.debug(BasicConfiguration.getInstance().ovirtIp.toString())
		log.debug("--- $userVo.password")

		val res: Result<Boolean> =
			conn.changeUserPw(
				BasicConfiguration.getInstance().ovirtIp,
				"adminRoot!@#",
				userVo.userName,
				userVo.password
			)
		return res.isSuccess
	}

	companion object {
		private val log by LoggerDelegate()
	}
}