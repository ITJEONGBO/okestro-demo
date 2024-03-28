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
 * [ItStorageServiceTest]
 * [ItStorageService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
@SpringBootTest
class ItStorageServiceTest {
	@Autowired
	private lateinit var service: ItStorageService

	/**
	 * [should_getName]
	 * [ItStorageService.getName]에 대한 단위테스트
	 * 
	 * @see [ItStorageService.getName]
	 */
	@Test
	fun should_getName() {
		log.debug("should_getName ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getList]
	 * [ItStorageService.getList]에 대한 단위테스트
	 * 
	 * @see [ItStorageService.getList]
	 */
	@Test
	fun should_getList() {
		log.debug("should_getList ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getDomain]
	 * [ItStorageService.getDomain]에 대한 단위테스트
	 * 
	 * @see ItStorageService.getDomain
	 */
	@Test
	fun should_getDomain() {
		log.debug("should_getDomain ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getDatacenter]
	 * [ItStorageService.getDatacenter]에 대한 단위테스트
	 * 
	 * @see ItStorageService.getDatacenter
	 */
	@Test
	fun should_getDatacenter() {
		log.debug("should_getDatacenter ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getVm]
	 * [ItStorageService.getVm]에 대한 단위테스트
	 * 
	 * @see ItStorageService.getVm
	 */
	@Test
	fun should_getVm() {
		log.debug("should_getVm ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getTemplate]
	 * [ItStorageService.getTemplate]에 대한 단위테스트
	 * 
	 * @see ItStorageService.getTemplate
	 */
	@Test
	fun should_getTemplate() {
		log.debug("should_getTemplate ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getDisk]
	 * [ItStorageService.getDisk]에 대한 단위테스트
	 * 
	 * @see [ItStorageService.getDisk]
	 */
	@Test
	fun should_getDisk() {
		log.debug("should_getDisk ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getSnapshot]
	 * [ItStorageService.getSnapshot]에 대한 단위테스트
	 * 
	 * @see [ItStorageService.getSnapshot]
	 */
	@Test
	fun should_getSnapshot() {
		log.debug("should_getSnapshot ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getEvent]
	 * [ItStorageService.getEvent]에 대한 단위테스트
	 * 
	 * @see [ItStorageService.getEvent]
	 */
	@Test
	fun should_getEvent() {
		log.debug("should_getEvent ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getPermission]
	 * [ItStorageService.getPermission]에 대한 단위테스트
	 * 
	 * @see [ItStorageService.getPermission]
	 */
	@Test
	fun should_getPermission() {
		log.debug("should_getPermission ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	companion object {
		private val log by LoggerDelegate()
	}
}