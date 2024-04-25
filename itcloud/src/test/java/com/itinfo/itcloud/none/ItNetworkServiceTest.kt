package com.itinfo.itcloud.none

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.service.ItNetworkService
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItNetworkServiceTest]
 * [ItNetworkService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
@SpringBootTest
class ItNetworkServiceTest {
	@Autowired
	private lateinit var service: ItNetworkService

	/**
	 * [should_getName]
	 * [ItNetworkService.getName]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.getName
	 */
	@Test
	fun should_getName() {
		log.debug("should_getName ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getList]
	 * [ItNetworkService.getList]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.getList
	 */
	@Test
	fun should_getList() {
		log.debug("should_getList ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getNetwork]
	 * [ItNetworkService.getNetwork]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.getNetwork
	 */
	@Test
	fun should_getNetwork() {
		log.debug("should_getNetwork ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getVnic]
	 * [ItNetworkService.getVnic]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.getVnic
	 */
	@Test
	fun should_getVnic() {
		log.debug("should_getVnic ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getCluster]
	 * [ItNetworkService.getCluster]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.getCluster
	 */
	@Test
	fun should_getCluster() {
		log.debug("should_getCluster ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getHost]
	 * [ItNetworkService.getHost]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.getHost
	 */
	@Test
	fun should_getHost() {
		log.debug("should_getHost ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getVm]
	 * [ItNetworkService.getVm]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.getVm
	 */
	@Test
	fun should_getVm() {
		log.debug("should_getVm ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getTemplate]
	 * [ItNetworkService.getTemplate]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.getTemplate
	 */
	@Test
	fun should_getTemplate() {
		log.debug("should_getTemplate ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getPermission]
	 * [ItNetworkService.getPermission]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.getPermission
	 */
	@Test
	fun should_getPermission() {
		log.debug("should_getPermission ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_addNetwork]
	 * [ItNetworkService.addNetwork]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.addNetwork
	 */
	@Test
	fun should_addNetwork() {
		log.debug("should_addNetwork ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_editNetwork]
	 * [ItNetworkService.editNetwork]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.editNetwork
	 */
	@Test
	fun should_editNetwork() {
		log.debug("should_editNetwork ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_deleteNetwork]
	 * [ItNetworkService.deleteNetwork]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.deleteNetwork
	 */
	@Test
	fun should_deleteNetwork	() {
		log.debug("should_deleteNetwork ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}


	companion object {
		private val log by LoggerDelegate()
	}
}
