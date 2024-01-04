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
 * [ItDomainServiceTest]
 * [ItDomainService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
@SpringBootTest
class ItDomainServiceTest {
	@Autowired
	private lateinit var service: ItDomainService

	/**
	 * [should_getName]
	 * [ItDomainService.getName]에 대한 단위테스트
	 * 
	 * @see [ItDomainService.getName]
	 */
	@Test
	fun should_getName() {
		log.debug("should_getName ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getList]
	 * [ItDomainService.getList]에 대한 단위테스트
	 * 
	 * @see [ItDomainService.getList]
	 */
	@Test
	fun should_getList() {
		log.debug("should_getList ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getDomain]
	 * [ItDomainService.getDomain]에 대한 단위테스트
	 * 
	 * @see ItDomainService.getDomain
	 */
	@Test
	fun should_getDomain() {
		log.debug("should_getDomain ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getDatacenter]
	 * [ItDomainService.getDatacenter]에 대한 단위테스트
	 * 
	 * @see ItDomainService.getDatacenter
	 */
	@Test
	fun should_getDatacenter() {
		log.debug("should_getDatacenter ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getVm]
	 * [ItDomainService.getVm]에 대한 단위테스트
	 * 
	 * @see ItDomainService.getVm
	 */
	@Test
	fun should_getVm() {
		log.debug("should_getVm ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getTemplate]
	 * [ItDomainService.getTemplate]에 대한 단위테스트
	 * 
	 * @see ItDomainService.getTemplate
	 */
	@Test
	fun should_getTemplate() {
		log.debug("should_getTemplate ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getDisk]
	 * [ItDomainService.getDisk]에 대한 단위테스트
	 * 
	 * @see [ItDomainService.getDisk]
	 */
	@Test
	fun should_getDisk() {
		log.debug("should_getDisk ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getSnapshot]
	 * [ItDomainService.getSnapshot]에 대한 단위테스트
	 * 
	 * @see [ItDomainService.getSnapshot]
	 */
	@Test
	fun should_getSnapshot() {
		log.debug("should_getSnapshot ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getEvent]
	 * [ItDomainService.getEvent]에 대한 단위테스트
	 * 
	 * @see [ItDomainService.getEvent]
	 */
	@Test
	fun should_getEvent() {
		log.debug("should_getEvent ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getPermission]
	 * [ItDomainService.getPermission]에 대한 단위테스트
	 * 
	 * @see [ItDomainService.getPermission]
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