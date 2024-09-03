package com.itinfo.itcloud.service.auth

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.aaarepository.entity.OvirtUser
import com.itinfo.itcloud.model.setting.UserVo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItOvirtUserServiceTest]
 * [ItUserService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
@SpringBootTest
class ItOvirtUserServiceTest {
	@Autowired private lateinit var ovirtUser: ItOvirtUserService

	@Test
	fun test_user_add(){
		val userVo: UserVo =
			UserVo.builder {
				userName { "rutilRutil" }
			}
		/*
		val result: UserVo =
			service.addUser(userVo)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.userName, `is`(userVo.userName+"@internal-authz"))
		*/
	}


	@Test
	fun should_changePassword() {
		val username = "rutil"
		val currentPw = "rutil1234"
		val newPw = "rutil123"
		val userBefore: OvirtUser = ovirtUser.findOne(username)
		assertThat(userBefore, `is`(not(nullValue())))
		assertThat(userBefore.password, `is`(not(nullValue())))
		assertThat(userBefore.passwordValidTo, `is`(not(nullValue())))
		val userAfter: OvirtUser = ovirtUser.changePassword(username, currentPw, newPw)
		assertThat(userAfter, `is`(not(nullValue())))
		assertThat(userAfter.password, `is`(not(nullValue())))
		assertThat(userAfter.passwordValidTo, `is`(not(nullValue())))
		val isPwValid: Boolean = ovirtUser.authenticateUser(username, newPw)
		assertThat(isPwValid, `is`(true))
	}


	companion object {
		private val log by LoggerDelegate()
	}
}