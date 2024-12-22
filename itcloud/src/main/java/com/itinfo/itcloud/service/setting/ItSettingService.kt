package com.itinfo.itcloud.service.setting

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.setting.UsersVo
import com.itinfo.itcloud.model.setting.toUsersMenu
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.findAllUsers
import org.ovirt.engine.sdk4.types.User
import org.springframework.stereotype.Service

interface ItSettingService {
    /**
     * [ItSettingService.findAllUsers]
     * 시스템 설정 값 조회
     *
     * @return []
     */
    fun findAllUsers(): List<UsersVo>
}

@Service
class ItSettingServiceImpl(
): BaseService(), ItSettingService {

    override fun findAllUsers(): List<UsersVo> {
		log.info("findAllUsers ... ")
        val users: List<User> = conn.findAllUsers()
            .getOrDefault(listOf())
        return users.toUsersMenu()
    }


    companion object {
        private val log by LoggerDelegate()
    }
}