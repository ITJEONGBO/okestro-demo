package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItTemplateServiceTest]
 * [ItTemplateService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
@SpringBootTest
class ItTemplateServiceTest {
	@Autowired
	private lateinit var service: ItTemplateService

	/**
	 * [should_getName]
	 * [ItTemplateService.getName]에 대한 단위테스트
	 * 
	 * @see ItTemplateService.getName
	 */
	@Test
	fun should_getName() {
		log.debug("should_getName ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getList]
	 * [ItTemplateService.getList]에 대한 단위테스트
	 * 
	 * @see ItTemplateService.getList
	 */
	@Test
	fun should_getList() {
		log.debug("should_getList ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getInfo]
	 * [ItTemplateService.getInfo]에 대한 단위테스트
	 * 
	 * @see ItTemplateService.getInfo
	 */
	@Test
	fun should_getInfo() {
		log.debug("should_getInfo ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getVm]
	 * [ItTemplateService.getVm]에 대한 단위테스트
	 * 
	 * @see ItTemplateService.getVm
	 */
	@Test
	fun should_getVm() {
		log.debug("should_getVm ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getNic]
	 * [ItTemplateService.getNic]에 대한 단위테스트
	 * 
	 * @see ItTemplateService.getNic
	 */
	@Test
	fun should_getNic() {
		log.debug("should_getNic ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getDisk]
	 * [ItTemplateService.getDisk]에 대한 단위테스트
	 * 
	 * @see ItTemplateService.getDisk
	 */
	@Test
	fun should_getDisk() {
		log.debug("should_getDisk ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getStorage]
	 * [ItTemplateService.getStorage]에 대한 단위테스트
	 * 
	 * @see ItTemplateService.getStorage
	 */
	@Test
	fun should_getStorage() {
		log.debug("should_getStorage ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getPermission]
	 * [ItTemplateService.getPermission]에 대한 단위테스트
	 * 
	 * @see ItTemplateService.getPermission
	 */
	@Test
	fun should_getPermission() {
		log.debug("should_getPermission ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getEvent]
	 * [ItTemplateService.getEvent]에 대한 단위테스트
	 * 
	 * @see ItTemplateService.getEvent
	 */
	@Test
	fun should_getEvent() {
		log.debug("should_getEvent ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}


	companion object {
		private val log by LoggerDelegate()
	}
}