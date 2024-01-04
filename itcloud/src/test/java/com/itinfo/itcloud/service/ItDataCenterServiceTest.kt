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
 * [ItDataCenterServiceTest]
 * [ItDataCenterService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
@SpringBootTest
class ItDataCenterServiceTest {
	@Autowired
	private lateinit var service: ItDataCenterService

	/**
	 * [should_getName]
	 * [ItDataCenterService.getName]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.getName
	 **/
	@Test
	fun should_getName() {
		log.debug("should_getName ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getList]
	 * [ItDataCenterService.getList]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.getList
	 **/
	@Test
	fun should_getList() {
		log.debug("should_getList ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getEvent]
	 * [ItDataCenterService.getEvent]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.getEvent
	 **/
	@Test
	fun should_getEvent() {
		log.debug("should_getEvent ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getDatacenter]
	 * [ItDataCenterService.getDatacenter]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.getDatacenter
	 **/
	@Test
	fun should_getDatacenter() {
		log.debug("should_getDatacenter ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_addDatacenter]
	 * [ItDataCenterService.addDatacenter]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.addDatacenter
	 **/
	@Test
	fun should_addDatacenter() {
		log.debug("should_addDatacenter ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_editDatacenter]
	 * [ItDataCenterService.editDatacenter]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.editDatacenter
	 **/
	@Test
	fun should_editDatacenter() {
		log.debug("should_editDatacenter ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_deleteDatacenter]
	 * [ItDataCenterService.deleteDatacenter]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.deleteDatacenter
	 **/
	@Test
	fun should_deleteDatacenter() {
		log.debug("should_deleteDatacenter ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}


	companion object {
		private val log by LoggerDelegate()
	}
}