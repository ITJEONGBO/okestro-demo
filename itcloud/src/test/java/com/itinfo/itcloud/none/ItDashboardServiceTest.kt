package com.itinfo.itcloud.none

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.service.ItDashboardService
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItDashboardServiceTest]
 * [ItDashboardService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
@SpringBootTest
class ItDashboardServiceTest {

	@Autowired
	private lateinit var service: ItDashboardService

	/**
	 * [should_getDashboard]
	 * [ItDashboardService.getDashboard]에 대한 단위테스트
	 * 
	 * @see ItDashboardService.getDashboard
	 */
	@Test
	fun should_getDashboard() {
		log.debug("should_getDashboard ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	companion object {
		private val log by LoggerDelegate()
	}
}