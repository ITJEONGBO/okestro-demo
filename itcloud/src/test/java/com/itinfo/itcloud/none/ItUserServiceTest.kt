package com.itinfo.itcloud.none

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.setting.UserVo
import com.itinfo.itcloud.service.admin.ItUserService
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItUserServiceTest]
 * [ItUserService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
@SpringBootTest
class ItUserServiceTest {
	@Autowired
	private lateinit var service: ItUserService

	@Test
	fun test_user_add(){
		val userVo: UserVo =
			UserVo.builder {
				userName { "rutilRutil" }
			}

		val result: UserVo =
			service.addUser(userVo)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.userName, `is`(userVo.userName+"@internal-authz"))
	}

	@Test
	fun test_change_password_user(){
		val userVo: UserVo =
			UserVo.builder {
				userName { "rutil" }
				password { "rutil" }
			}

		val result: Boolean =
			service.changePwUser(userVo)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result, `is`(true))
	}



	companion object {
		private val log by LoggerDelegate()
	}
}