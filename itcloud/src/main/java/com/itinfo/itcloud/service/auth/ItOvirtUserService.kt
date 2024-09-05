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
import com.itinfo.itcloud.model.auth.toUserVo
import com.itinfo.itcloud.model.auth.user2Add
import com.itinfo.itcloud.ovirt.hashPassword

import com.itinfo.itcloud.ovirt.validatePassword
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.addUser
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.ItCloudException
import com.itinfo.util.ovirt.error.toError
import org.ovirt.engine.sdk4.types.User
import org.postgresql.util.PSQLException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ItOvirtUserService {
	/*
	fun getTestValue(): Int
	fun login(loginDTO: LoginDTO): String
	fun logout(httpSession: HttpSession)
	fun allUsers(): List<UserVO>
	*/

	/**
	 * [ItOvirtUserService.findAll]
	 * 모든 사용자 조회
	 *
	 * @return List<[UserVo]>
	 */
	@Throws(PSQLException::class)
	fun findAll(): List<UserVo>
	/**
	 * [ItOvirtUserService.findOne]
	 * 특정 사용자 조회
	 *
	 * @param username [String]
	 * @return [OvirtUser]
	 */
	@Throws(PSQLException::class)
	fun findOne(username: String): OvirtUser
	/**
	 * [ItOvirtUserService.findAll]
	 *
	 * @param username [String]
	 * @return [UserVo]
	 */
	@Throws(PSQLException::class)
	fun findFullDetailByName(username: String): UserVo?
	/**
	 * [ItOvirtUserService.findEncryptedValue]
	 * DB 비밀번호 암호문에서 base64로 디코딩한 값 출력
	 *
	 * @param input [String]
	 * @return [String]
	 */
	fun findEncryptedValue(input: String): String
	/**
	 * [ItOvirtUserService.authenticateUser]
	 *
	 * @param username [String]
	 * @param password [String]
	 * @return List<[UserVo]>
	 */
	@Throws(PSQLException::class)
	fun authenticateUser(username: String, password: String): Boolean

	/**
	 * [ItOvirtUserService.addUser]
	 * ovirt 계정생성은 cli 로 해야함
	 * ovirt 에서 사용자 추가하는 함수
	 * => ovirt-설정-사용자-add-add_user_and_group
	 *
	 * cli로 계정생성할 때 필요한 정보는
	 * userName, firstName, password-valid-to(기본은 1970)
	 * @param userVo [UserVo]
	 * @return userVo [UserVo]
	 */
	fun addUser(userVo: UserVo): UserVo
	/**
	 * [ItOvirtUserService.changePassword]
	 *
	 * @param username [String]
	 * @param currentPassword [String]
	 * @param newPassword [String]
	 * @return [OvirtUser]
	 */
	fun changePassword(username: String, currentPassword: String, newPassword: String): OvirtUser
}

@Service
class OvirtUserServiceImpl(

): BaseService(), ItOvirtUserService {
	@Autowired private lateinit var ovirtUsers: OvirtUserRepository
	@Autowired private lateinit var userDetails: UserDetailRepository

	@Throws(PSQLException::class)
	override fun findAll(): List<UserVo> {
		log.info("findAll ... ")
		val res: List<OvirtUser> =
			ovirtUsers.findAll()
		if (res.isEmpty()) return listOf()

		val userDetails: List<UserDetail> =
			userDetails.findAll()
		log.debug("detailsFound: {}", userDetails)
		return res.toUserVos(userDetails)
	}

	@Throws(ItCloudException::class)
	override fun findOne(username: String): OvirtUser =
		ovirtUsers.findByName(username)
			?: throw ErrorPattern.OVIRTUSER_NOT_FOUND.toException()

	@Throws(ItCloudException::class, PSQLException::class)
	override fun findFullDetailByName(username: String): UserVo? {
		log.info("findByName ... name: {}", username)
		val oUser: OvirtUser = findOne(username)
		log.debug("itemFound: {}", oUser)
		val oUserDetail: UserDetail =
			userDetails.findByExternalId(oUser.uuid) ?:
			throw ErrorPattern.OVIRTUSER_NOT_FOUND.toException()
		log.debug("detailFound: {}", oUserDetail)
		return oUser.toUserVo(oUserDetail) // USERS.retrieveUser
	}

	override fun findEncryptedValue(input: String): String =
		input.hashPassword()

	@Throws(PSQLException::class)
	override fun authenticateUser(username: String, password: String): Boolean {
		log.info("authenticateUser ... username: {}, password: {}", username, password)
		if (username.isEmpty()) throw ErrorPattern.OVIRTUSER_ID_NOT_FOUND.toException()
		if (password.isEmpty()) throw ErrorPattern.REQUIRED_VALUE_EMPTY.toException()
		val user: OvirtUser = findOne(username)
		return password.validatePassword(user.password)
	}

	override fun addUser(userVo: UserVo): UserVo {
		log.info("addUser ... ")
		val res: User =
			conn.addUser(userVo.user2Add())
				.getOrNull() ?: throw ErrorPattern.UNKNOWN.toError()
		return res.toUserVo()
	}

	@Transactional("aaaTransactionManager")
	override fun changePassword(username: String, currentPassword: String, newPassword: String): OvirtUser {
		log.info("changePassword ... username: {}", username)
		val user: OvirtUser = findOne(username)

		if (!authenticateUser(username, currentPassword))
			throw ErrorPattern.OVIRTUSER_AUTH_INVALID.toException()

		user.password = newPassword.hashPassword()
		val userUpdated: OvirtUser = ovirtUsers.save(user)
		return userUpdated
	}

	companion object {
		private val log by LoggerDelegate()
	}
}