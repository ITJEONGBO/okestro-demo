package com.itinfo.itcloud.service.setting

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.setting.UsersVo
import com.itinfo.itcloud.model.setting.toUsersMenu
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.findAllUsers
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.types.User
import org.springframework.stereotype.Service

interface ItSettingService {
    /**
     * [ItSettingService.findAllUser]
     * 사용자 목록
     *
     * @return List<[UsersVo]>
     */
    @Throws(Error::class)
    fun findAllUser(): List<UsersVo>

    /**
     * [ItSettingService.findAllUser]
     * 활성 사용자 세션 목록
     *
     * @return List<[UsersVo]>
     */
    @Throws(Error::class)
    fun findAllUserSessions(): List<UsersVo>

    /**
     * [ItSettingService.addUser]
     * 사용자 생성
     *
     * @param usersVo [UsersVo]
     * @return [UsersVo]
     */
    @Throws(Error::class)
    fun addUser(usersVo: UsersVo): UsersVo?

    /**
     * [ItSettingService.changePWUser]
     * 사용자 비밀번호 변경
     *
     * @param usersVo [UsersVo]
     * @return [UsersVo]
     */
    @Throws(Error::class)
    fun changePWUser(usersVo: UsersVo): UsersVo?

    /**
     * [ItSettingService.removeUser]
     * 활성 사용자 세션 목록
     *
     * @param usersId [String]
     * @return [Boolean]
     */
    @Throws(Error::class)
    fun removeUser(usersId: String): Boolean


//    /**
//     * [ItSettingService.findAllLicence]
//     * 라이선스 목록
//     *
//     * @return List<[UsersVo]>
//     */
//    @Deprecated("나중구현")
//    fun findAllLicence(): List<>



}
@Service
class SettingServiceImpl(

): BaseService(), ItSettingService {

    @Throws(Error::class)
    override fun findAllUser(): List<UsersVo> {
        log.info("findAllUser ... ")
        val res: List<User> =
            conn.findAllUsers().getOrDefault(listOf())
        return res.toUsersMenu()
    }

    @Throws(Error::class)
    override fun findAllUserSessions(): List<UsersVo> {
        TODO("Not yet implemented")
    }

    @Throws(Error::class)
    override fun addUser(usersVo: UsersVo): UsersVo? {
        TODO("Not yet implemented")
    }

    @Throws(Error::class)
    override fun changePWUser(usersVo: UsersVo): UsersVo? {
        TODO("Not yet implemented")
    }

    @Throws(Error::class)
    override fun removeUser(usersId: String): Boolean {
        TODO("Not yet implemented")
    }


    companion object {
        private val log by LoggerDelegate()
    }

}