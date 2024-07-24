package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.ClusterVo
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItClusterServiceTest]
 * [ItClusterService]에 대한 테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
//@ExtendWith(MockitoExtension::class)
@SpringBootTest
/*internal */class ItClusterServiceTest {

//	@Mock private lateinit var service: ItClusterService
	@Autowired private lateinit var service: ItClusterService

	private lateinit var clusterId: String

	@BeforeEach
	fun setup() {
		clusterId = "ae1ea51e-f642-11ee-bcc4-00163e4b3128" // Default
	}


	/**
	 * [should_getList]
	 * [ItClusterService.findAll]에 대한 단위테스트
	 * 
	 * @see ItClusterService.findAll
	 **/
	@Test
	fun should_getList() {
		log.debug("should_getList ... ")
		val result: List<ClusterVo> =
			service.findAll()

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(2))
		result.forEach { println(it) }
	}

	/**
	 * [should_findOne]
	 * [ItClusterService.findOne]에 대한 단위테스트
	 * 
	 * @see ItClusterService.findOne
	 **/
	@Test
	fun should_findOne() {
		log.debug("should_findOne ... ")
		val result: ClusterVo? =
			service.findOne(clusterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result?.name, `is`("Default"))
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
	 * [ItClusterService.add]에 대한 단위테스트
	 * 
	 * @see ItClusterService.add
	 **/
	@Test
	fun should_addCluster() {
		log.debug("should_addCluster ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_editCluster]
	 * [ItClusterService.update]에 대한 단위테스트
	 * 
	 * @see ItClusterService.update
	 **/
	@Test
	fun should_editCluster() {
		log.debug("should_editCluster ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_deleteCluster]
	 * [ItClusterService.remove]에 대한 단위테스트
	 * 
	 * @see ItClusterService.remove
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