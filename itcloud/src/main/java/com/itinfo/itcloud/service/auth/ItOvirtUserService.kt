package com.itinfo.itcloud.service.auth

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.auth.UserVo
import com.itinfo.itcloud.aaarepository.OvirtUserRepository
import com.itinfo.itcloud.aaarepository.UserDetailRepository
import com.itinfo.itcloud.aaarepository.entity.OvirtUser
import com.itinfo.itcloud.aaarepository.entity.UserDetail
import com.itinfo.itcloud.aaarepository.entity.toUserVo
import com.itinfo.itcloud.aaarepository.entity.toUserVos
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.BasicConfiguration
import com.itinfo.util.ovirt.changeUserPw
import com.itinfo.util.ovirt.error.ErrorPattern
import org.postgresql.util.PSQLException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ItOvirtUserService {
	/*
	fun getTestValue(): Int
	fun login(loginDTO: LoginDTO): String
	fun logout(httpSession: HttpSession)
	fun allUsers(): List<UserVO>
	*/
	@Throws(PSQLException::class)
	fun findAll(): List<UserVo>

	@Throws(PSQLException::class)
	fun findByName(name: String): UserVo?

	@Throws(PSQLException::class)
	fun authenticateUser(username: String, password: String): Boolean
	/**
	 * [ItOvirtUserService.changePwUser]
	 *
	 * @param userVo [UserVo]
	 * @return userVo [UserVo]
	 */
	fun changePwUser(userVo: UserVo): Boolean
}

@Service
class OvirtUserServiceImpl(

): BaseService(), ItOvirtUserService {
	@Autowired private lateinit var ovirtUsers: OvirtUserRepository
	@Autowired private lateinit var userDetails: UserDetailRepository

	@Throws(PSQLException::class)
	override fun findAll(): List<UserVo> {
		log.info("findAll ... ")
		val res: List<OvirtUser> = ovirtUsers.findAll()
		if (res.isEmpty()) return listOf()

		val userDetails: List<UserDetail> =
			userDetails.findAll()
		log.debug("detailsFound: {}", userDetails)
		return res.toUserVos(userDetails)
	}

	@Throws(PSQLException::class)
	override fun findByName(name: String): UserVo? {
		log.info("findByName ... name: {}", name)
		val oUser: OvirtUser =
			ovirtUsers.findByName(name) ?: throw ErrorPattern.OVIRTUSER_NOT_FOUND.toException()
		log.debug("itemFound: {}", oUser)
		val oUserDetail: UserDetail =
			userDetails.findByExternalId(oUser.uuid) ?: throw ErrorPattern.OVIRTUSER_NOT_FOUND.toException()
		log.debug("detailFound: {}", oUserDetail)
		return oUser.toUserVo(oUserDetail) // USERS.retrieveUser
	}

	override fun authenticateUser(username: String, password: String): Boolean {
		TODO("Not yet implemented")
	}

	/*
	override fun login(loginDTO: LoginDTO?): String {
		return userDAO.login(loginDTO)
	}

	override fun logout(httpSession: HttpSession) {
		httpSession.invalidate() // 세션 초기화
	}
	*/

	// TODO 비번 변경 실패
	override fun changePwUser(userVo: UserVo): Boolean {
		log.info("changePwUser")
		log.debug(BasicConfiguration.getInstance().ovirtIp.toString())
		log.debug("--- ${userVo.password}")
		val res: Result<Boolean> =
			conn.changeUserPw(
				BasicConfiguration.getInstance().ovirtIp,
				"adminRoot!@#",
				userVo.username,
				userVo.password
			)
		return res.isSuccess
	}

	companion object {
		private val log by LoggerDelegate()
	}
}