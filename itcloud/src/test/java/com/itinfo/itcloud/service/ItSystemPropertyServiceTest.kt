package com.itinfo.itcloud.service

import com.itinfo.common.LoggerDelegate
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItSystemPropertyServiceTest]
 * [ItSystemPropertyService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
@SpringBootTest
class ItSystemPropertyServiceTest {
	@Autowired
	private lateinit var service: ItSystemPropertyService

	companion object {
		private val log by LoggerDelegate()
	}
}