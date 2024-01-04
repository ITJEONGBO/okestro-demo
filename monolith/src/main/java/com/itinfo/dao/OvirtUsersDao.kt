package com.itinfo.dao

import com.itinfo.common.LoggerDelegate

import com.itinfo.dao.aaa.OvirtUser
import com.itinfo.dao.aaa.OvirtUserRepository
import com.itinfo.dao.aaa.toUserVo
import com.itinfo.dao.aaa.toUserVos

import com.itinfo.model.UserVo
import org.postgresql.util.PSQLException

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class OvirtUsersDao {
	@Autowired private lateinit var ovirtUserRepository: OvirtUserRepository

	@Throws(PSQLException::class)
	fun retrieveUsers(): List<UserVo> {
		log.info("... retrieveUsers")
		val itemsFound: List<OvirtUser> =
			ovirtUserRepository.findAll()
		log.debug("itemFound: $itemsFound")
		return itemsFound.toUserVos()
	}

	@Throws(PSQLException::class)
	fun retrieveUser(username: String): UserVo? {
		log.info("... retrieveUser('$username')")
		val itemFound: OvirtUser? =
			ovirtUserRepository.findByName(username)
		log.debug("itemFound: $itemFound")
		return itemFound?.toUserVo()
//			?.also { log.debug("returning ... $it") }
//		return this.systemSqlSessionTemplate.selectOne("USERS.retrieveUser", id)
	}

	@Throws(PSQLException::class)
	fun isExistUser(user: UserVo): Boolean {
		log.info("... isExistUser > user: $user")
		val isUserFound: Boolean = retrieveUser(user.id) != null
		log.debug("isUserFound: $isUserFound")
		return isUserFound
		// return this.systemSqlSessionTemplate.selectOne("USERS.isExistUser", user)
	}

	@Throws(PSQLException::class)
	fun addUser(user: UserVo): Int {
		return -1
		// TODO: ovirt-aaa-jdbc 에서 처리 법 찾아 구현
		// return this.systemSqlSessionTemplate.insert("USERS.addUser", user)
	}

	@Throws(PSQLException::class)
	fun updateUser(user: UserVo): Int {
		return -1
		// TODO: ovirt-aaa-jdbc 에서 처리 법 찾아 구현
		// return this.systemSqlSessionTemplate.insert("USERS.updateUser", user)
	}

	@Throws(PSQLException::class)
	fun updatePassword(user: UserVo): Int {
		return -1
		// TODO: ovirt-aaa-jdbc 에서 처리 법 찾아 구현
		// return this.systemSqlSessionTemplate.insert("USERS.updatePassword", user)
	}

	@Throws(PSQLException::class)
	fun updateLoginCount(user: UserVo): Int {
		return -1
		// TODO: ovirt-aaa-jdbc 에서 처리 법 찾아 구현
		// return this.systemSqlSessionTemplate.insert("USERS.updateLoginCount", user)
	}

	@Throws(PSQLException::class)
	fun setBlockTime(user: UserVo): Int {
		return -1
		// TODO: ovirt-aaa-jdbc 에서 처리 법 찾아 구현
		// return this.systemSqlSessionTemplate.insert("USERS.setBlockTime", user);
	}

	fun initLoginCount(userId: String): Int {
		return -1
		// TODO: ovirt-aaa-jdbc 에서 처리 법 찾아 구현
		// return this.systemSqlSessionTemplate.insert("USERS.initLoginCount", userId);
	}

	fun removeUsers(users: List<UserVo>): Int {
		return -1
		// TODO: ovirt-aaa-jdbc 에서 처리 법 찾아 구현
		// return this.systemSqlSessionTemplate.delete("USERS.removeUsers", users);
	}

	fun login(id: String): String {
		log.info("login('$id') ...")
		val itemFound: UserVo? = retrieveUser(id)
		return itemFound?.password ?: ""
		// return (String)this.systemSqlSessionTemplate.selectOne("USERS.login", id);
	}

	companion object {
		private val log by LoggerDelegate()
	}
}