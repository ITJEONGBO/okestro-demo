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
 * [ItClusterServiceTest]
 * [ItClusterService]에 대한 테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
@SpringBootTest
class ItClusterServiceTest {

	@Autowired
	private lateinit var service: ItClusterService

	/**
	 * [should_getName]
	 * [ItClusterService.getName]에 대한 단위테스트
	 * 
	 * @see ItClusterService.getName
	 **/
	@Test
	fun should_getName() {
		log.debug("should_getName ... ")
		assertThat(service, `is`(not(nullValue())))
		val _id = ""
		val output: String? =
			service.getName(_id)
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getList]
	 * [ItClusterService.getList]에 대한 단위테스트
	 * 
	 * @see ItClusterService.getList
	 **/
	@Test
	fun should_getList() {
		log.debug("should_getList ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getInfo]
	 * [ItClusterService.getInfo]에 대한 단위테스트
	 * 
	 * @see ItClusterService.getInfo
	 **/
	@Test
	fun should_getInfo() {
		log.debug("should_getInfo ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

    /**
     * [should_getNetwork]
     * [ItClusterService.getNetwork]에 대한 단위테스트
     * 
     * @see ItClusterService.getNetwork
     **/
    @Test
    fun should_getNetwork() {
    	log.debug("should_getNetwork ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
    }

	/**
	 * [should_getHost]
	 * [ItClusterService.getHost]에 대한 단위테스트
	 * 
	 * @see ItClusterService.getHost
	 **/
	@Test
	fun should_getHost() {
		log.debug("should_getHost ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getVm]
	 * [ItClusterService.getVm]에 대한 단위테스트
	 * 
	 * @see ItClusterService.getVm
	 **/
	@Test
	fun should_getVm() {
		log.debug("should_getVm ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	@Test
	fun should_getAffinitygroup() {
		log.debug("should_getAffinitygroup ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	@Test
	fun should_getAffinitylabel() {
		log.debug("should_getAffinitylabel ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getPermission]
	 * [ItClusterService.getPermission]에 대한 단위테스트
	 * 
	 * @see ItClusterService.getPermission
	 **/
	@Test		
	fun should_getPermission() {
		log.debug("should_getPermission ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getEvent]
	 * [ItClusterService.getEvent]에 대한 단위테스트
	 * 
	 * @see ItClusterService.getEvent
	 **/
	@Test			
	fun should_getEvent() {
		log.debug("should_getEvent ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getClusterCreate]
	 * [ItClusterService.getClusterCreate]에 대한 단위테스트
	 * 
	 * @see ItClusterService.getClusterCreate
	 **/
	@Test
	fun should_getClusterCreate() {
		log.debug("should_getClusterCreate ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getDcList]
	 * [ItClusterService.getDcList]에 대한 단위테스트
	 * 
	 * @see ItClusterService.getDcList
	 **/
	@Test
    fun should_getDcList() {
    	log.debug("should_getDcList ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
    }

	/**
	 * [should_addCluster]
	 * [ItClusterService.addCluster]에 대한 단위테스트
	 * 
	 * @see ItClusterService.addCluster
	 **/
	@Test
	fun should_addCluster() {
		log.debug("should_addCluster ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_editCluster]
	 * [ItClusterService.editCluster]에 대한 단위테스트
	 * 
	 * @see ItClusterService.editCluster
	 **/
	@Test
	fun should_editCluster() {
		log.debug("should_editCluster ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_deleteCluster]
	 * [ItClusterService.deleteCluster]에 대한 단위테스트
	 * 
	 * @see ItClusterService.deleteCluster
	 **/
	@Test
	fun should_deleteCluster() {
		log.debug("should_deleteCluster ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	companion object {
		private val log by LoggerDelegate()
	}
}