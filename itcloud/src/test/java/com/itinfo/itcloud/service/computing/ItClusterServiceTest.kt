package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.ClusterVo
import com.itinfo.itcloud.model.computing.HostVo
import com.itinfo.itcloud.model.network.NetworkVo
import org.apache.catalina.Cluster
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.ovirt.engine.sdk4.types.*
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

	private lateinit var dataCenterId: String
	private lateinit var clusterId: String

	@BeforeEach
	fun setup() {
		dataCenterId = "ae1d4138-f642-11ee-9c1b-00163e4b3128"
		clusterId = "ae1ea51e-f642-11ee-bcc4-00163e4b3128" // Default
	}


	/**
	 * [should_findAll]
	 * [ItClusterService.findAll]에 대한 단위테스트
	 * 
	 * @see ItClusterService.findAll
	 **/
	@Test
	fun should_findAll() {
		log.debug("should_findAll ... ")
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
		println(result)
	}


	/**
	 * [should_findAllNetworksFromDataCenter]
	 * [ItClusterService.findAllNetworksFromDataCenter]에 대한 단위테스트
	 * 클러스터 생성 위한 네트워크 목록
	 *
	 * @see ItClusterService.findAllNetworksFromDataCenter
	 **/
	@Test
	fun should_findAllNetworksFromDataCenter() {
		log.debug("should_findAllNetworksFromDataCenter ... ")
		val result: List<NetworkVo> =
			service.findAllNetworksFromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(5))
		result.forEach { println(it) }
	}

	/**
	 * [should_add_Cluster]
	 * [ItClusterService.add]에 대한 단위테스트
	 *
	 * @see ItClusterService.add
	 **/
	@Test
	fun should_add_Cluster() {
		log.debug("should_add_Cluster ... ")

		val addCluster: ClusterVo = ClusterVo.builder {
			name { "testCluster" }
			cpuArc { Architecture.X86_64 }
			cpuType { "" }
			description { "" }
			comment { "" }
			networkVo { IdentifiedVo.builder { id { "" } } }// 관리 네트워크
			biosType { BiosType.CLUSTER_DEFAULT }
			fipsMode { FipsMode.ENABLED }
			version { "4.7" }
			switchType { SwitchType.LEGACY }
			firewallType { FirewallType.FIREWALLD }
			logMaxMemory { 90 }
			logMaxMemoryType { LogMaxMemoryUsedThresholdType.PERCENTAGE }
			virtService { true }
			glusterService { false }
			errorHandling { MigrateOnError.MIGRATE }
			bandwidth { MigrationBandwidthAssignmentMethod.AUTO }
			encrypted { InheritableBoolean.INHERIT }
			networkProvider { false }
		}

		val result: ClusterVo? =
			service.add(addCluster)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result?.id, `is`(not(nullValue())))
		assertThat(result?.name, `is`(not(nullValue())))
		assertThat(result?.id, `is`(not(nullValue())))
	}

	/**
	 * [should_update_Cluster]
	 * [ItClusterService.update]에 대한 단위테스트
	 *
	 * @see ItClusterService.update
	 **/
	@Test
	fun should_update_Cluster() {
		log.debug("should_update_Cluster ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_remove_Cluster]
	 * [ItClusterService.remove]에 대한 단위테스트
	 *
	 * @see ItClusterService.remove
	 **/
	@Test
	fun should_remove_Cluster() {
		log.debug("should_remove_Cluster ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getNetworkList]
	 * [ItClusterService.findAllNetworksFromCluster]에 대한 단위테스트
	 *
	 * @see ItClusterService.findAllNetworksFromCluster
	 **/
	@Test
	fun should_getNetworkList() {
		log.debug("should_getNetworkList ... ")
		val result: List<NetworkVo> =
			service.findAllNetworksFromCluster(clusterId)

		assertThat(result, `is`(not(nullValue())))
//		assertThat(result.size, `is`(5))
		result.forEach { println(it) }
	}

	/**
	 * [should_getHostList]
	 * [ItClusterService.findAllHostsFromCluster]에 대한 단위테스트
	 *
	 * @see ItClusterService.findAllHostsFromCluster
	 **/
	@Test
	fun should_getHostList() {
		log.debug("should_getHostList ... ")
		val result: List<HostVo> =
			service.findAllHostsFromCluster(clusterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(5))
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

	companion object {
		private val log by LoggerDelegate()
	}
}