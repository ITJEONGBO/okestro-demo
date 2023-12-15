package com.itinfo.service.impl

import com.itinfo.common.LoggerDelegate
import com.itinfo.dao.OvirtUsersDao
import com.itinfo.model.UserVo
import com.itinfo.security.createHash
import com.itinfo.service.UsersService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * [UsersServiceImpl]
 * 사용자 관리 서비스 응용
 *
 * @author chlee
 * @since 2023.12.07
 */
@Service
class UsersServiceImpl : UsersService {
    @Autowired private lateinit var usersDao: OvirtUsersDao

    /**
     * [UsersService.fetchUsers]
     */
    override fun fetchUsers(): List<UserVo> {
        log.info("... retrieveUsers")
        val users: List<UserVo> =
            usersDao.retrieveUsers().also { log.debug("found: $it") }
        return users
    }

    /**
     * [UsersService.removeUsers]
     */
    override fun removeUsers(users: List<UserVo>): Int {
        log.info("... removeUsers")
        return usersDao.removeUsers(users)
    }

    /**
     * [UsersService.isExistUser]
     */
    override fun isExistUser(user: UserVo): Boolean {
        log.info("... isExistUser")
        return usersDao.isExistUser(user)
    }

    /**
     * [UsersService.addUser]
     */
    override fun addUser(user: UserVo): Int {
        log.info("... addUser")
        user.password = user.password.createHash()
        return usersDao.addUser(user)
    }

    override fun login(id: String): String {
        log.info("... login($id)")
        return usersDao.login(id)
    }

    override fun fetchUser(id: String): UserVo? {
        log.info("... retrieveUser($id)")
        return usersDao.retrieveUser(id)
    }

    override fun updateUser(user: UserVo): Int {
        log.info("... retrieveUser")
        return usersDao.updateUser(user)
    }

    override fun updatePassword(user: UserVo): Int {
        log.info("... updatePassword")
        user.newPassword = user.newPassword.createHash()
        return usersDao.updatePassword(user)
    }

    override fun updateLoginCount(user: UserVo): Int {
        log.info("... updateLoginCount")
        return usersDao.updateLoginCount(user)
    }

    override fun setBlockTime(user: UserVo) {
        log.info("... setBlockTime")
        user.blockTime = LocalDateTime.now()
                .plusMinutes(10L)
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        usersDao.setBlockTime(user)
    }

    override fun initLoginCount(userId: String) {
        log.info("... initLoginCount($userId)")
        usersDao.initLoginCount(userId)
    }
    
    companion object {
        private val log by LoggerDelegate()
    }
}
