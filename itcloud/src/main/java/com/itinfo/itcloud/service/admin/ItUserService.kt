package com.itinfo.itcloud.service.admin

import com.itinfo.common.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ItUserService {
	/*
	fun getTestValue(): Int
	fun login(loginDTO: LoginDTO): String
	fun logout(httpSession: HttpSession)
	fun allUsers(): List<UserVO>
	*/
}

@Service
class UserServiceImpl : ItUserService {
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
	companion object {
		private val log by LoggerDelegate()
	}
}