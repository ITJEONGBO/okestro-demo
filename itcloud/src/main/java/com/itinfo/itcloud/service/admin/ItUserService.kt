package com.itinfo.itcloud.service.admin

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.setting.UserVo
import com.itinfo.itcloud.model.setting.toAddUserBuilder
import com.itinfo.itcloud.model.setting.toUserVo
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.addUser
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
import org.ovirt.engine.sdk4.types.User
import org.springframework.stereotype.Service

interface ItUserService {
	/**
	 * [ItUserService.addUser]
	 *
	 * ovirt 계정생성은 cli 로 해야함
	 * ovirt 내에서 추가하는 함수
	 * @param userVo [UserVo]
	 * @return userVo [UserVo]
	 */
	fun addUser(userVo: UserVo): UserVo
}

@Service
class UserServiceImpl(

): BaseService(), ItUserService {
	/*
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

	companion object {
		private val log by LoggerDelegate()
	}
}