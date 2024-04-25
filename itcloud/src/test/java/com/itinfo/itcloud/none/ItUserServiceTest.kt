package com.itinfo.itcloud.none

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.service.ItUserService
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

	companion object {
		private val log by LoggerDelegate()
	}
}