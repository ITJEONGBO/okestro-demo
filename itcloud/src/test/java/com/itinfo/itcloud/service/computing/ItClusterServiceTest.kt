package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.setting.PermissionVo
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.BeforeEach
import org.ovirt.engine.sdk4.types.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItClusterServiceTest]
 * [ItClusterService]에 대한 테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.03.05
 */
//@ExtendWith(MockitoExtension::class)
@SpringBootTest
/*internal */class ItClusterServiceTest {

//	@Mock private lateinit var service: ItClusterService
	@Autowired private lateinit var service: ItClusterService

	private lateinit var dataCenterId: String
	private lateinit var clusterId: String // Default
	private lateinit var networkId: String // ovirtmgmt(dc: Default)

	@BeforeEach
	fun setup() {
		dataCenterId = "6cde7270-6459-11ef-8be2-00163e5d0646"
		clusterId = "6ce0356a-6459-11ef-a03a-00163e5d0646"
		networkId = "00000000-0000-0000-0000-000000000009"
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
		assertThat(result.size, `is`(2))
		result.forEach { println(it) }
	}

	/**
	 * [should_add_update_and_remove_Cluster]
	 * [ItClusterService.add], [ItClusterService.update], [ItClusterService.remove]에 대한 단위테스트
	 * 외부공급자 생성x
	 *
	 * @see ItClusterService.add
	 * @see ItClusterService.update
	 * @see ItClusterService.remove
	 **/
	@Test
	fun should_add_update_and_remove_Cluster() {
		log.debug("should_addCluster ... ")
		val addCluster: ClusterVo = ClusterVo.builder {
			dataCenterVo { IdentifiedVo.builder { id { dataCenterId } } }
			name { "testCluster" }
			cpuArc { Architecture.X86_64 }
			cpuType { "Intel Nehalem Family" }
			description { "testDescription" }
			comment { "testComment" }
			networkVo { IdentifiedVo.builder { id { networkId } } }
			biosType { BiosType.Q35_SEA_BIOS }
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

		val addResult: ClusterVo? =
			service.add(addCluster)

		assertThat(addResult, `is`(not(nullValue())))
		assertThat(addResult?.id, `is`(not(nullValue())))
		assertThat(addResult?.dataCenterVo?.id, `is`(addCluster.dataCenterVo.id))
		assertThat(addResult?.name, `is`(addCluster.name))
		assertThat(addResult?.description, `is`(addCluster.description))
		assertThat(addResult?.comment, `is`(addCluster.comment))
		assertThat(addResult?.networkVo?.id, `is`(addCluster.networkVo.id))
		assertThat(addResult?.biosType, `is`(addCluster.biosType))
		assertThat(addResult?.fipsMode, `is`(addCluster.fipsMode))
		assertThat(addResult?.version, `is`(addCluster.version))
		assertThat(addResult?.switchType, `is`(addCluster.switchType))
		assertThat(addResult?.firewallType, `is`(addCluster.firewallType))
		assertThat(addResult?.logMaxMemory, `is`(addCluster.logMaxMemory))
		assertThat(addResult?.logMaxMemoryType, `is`(addCluster.logMaxMemoryType))
		assertThat(addResult?.virtService, `is`(addCluster.virtService))
		assertThat(addResult?.glusterService, `is`(addCluster.glusterService))
		assertThat(addResult?.errorHandling, `is`(addCluster.errorHandling))
		assertThat(addResult?.bandwidth, `is`(addCluster.bandwidth))
		assertThat(addResult?.encrypted, `is`(addCluster.encrypted))
		assertThat(addResult?.networkProvider, `is`(addCluster.networkProvider))

		log.debug("should_updateCluster ... ")
		val updateCluster: ClusterVo = ClusterVo.builder {
			id { addResult?.id }
			dataCenterVo { IdentifiedVo.builder { id { dataCenterId } } }
			name { "testCluster1" }
			cpuArc { Architecture.X86_64 }
			cpuType { "Intel Nehalem Family" }
			description { "testDescription" }
			comment { "testComment" }
			networkVo { IdentifiedVo.builder { id { networkId } } }
			biosType { BiosType.Q35_SEA_BIOS }
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

		val updateResult: ClusterVo? =
			service.update(updateCluster)

		assertThat(updateResult, `is`(not(nullValue())))
		assertThat(updateResult?.id, `is`(updateCluster.id))
		assertThat(updateResult?.dataCenterVo?.id, `is`(updateCluster.dataCenterVo.id))
		assertThat(updateResult?.name, `is`(updateCluster.name))
		assertThat(updateResult?.description, `is`(updateCluster.description))
		assertThat(updateResult?.comment, `is`(updateCluster.comment))
		assertThat(updateResult?.networkVo?.id, `is`(updateCluster.networkVo.id))
		assertThat(updateResult?.biosType, `is`(updateCluster.biosType))
		assertThat(updateResult?.fipsMode, `is`(updateCluster.fipsMode))
		assertThat(updateResult?.version, `is`(updateCluster.version))
		assertThat(updateResult?.switchType, `is`(updateCluster.switchType))
		assertThat(updateResult?.firewallType, `is`(updateCluster.firewallType))
		assertThat(updateResult?.logMaxMemory, `is`(updateCluster.logMaxMemory))
		assertThat(updateResult?.logMaxMemoryType, `is`(updateCluster.logMaxMemoryType))
		assertThat(updateResult?.virtService, `is`(updateCluster.virtService))
		assertThat(updateResult?.glusterService, `is`(updateCluster.glusterService))
		assertThat(updateResult?.errorHandling, `is`(updateCluster.errorHandling))
		assertThat(updateResult?.bandwidth, `is`(updateCluster.bandwidth))
		assertThat(updateResult?.encrypted, `is`(updateCluster.encrypted))
		assertThat(updateResult?.networkProvider, `is`(updateCluster.networkProvider))

		log.debug("should_removeCluster ... ")
		val removeResult =
			updateResult?.let { service.remove(it.id) }

		assertThat(removeResult, `is`(true))
	}

	/**
	 * [should_add_success_Cluster]
	 * [ItClusterService.add]에 대한 단위테스트
	 * 외부공급자 생성x
	 *
	 * @see ItClusterService.add
	 **/
	@Test
	fun should_add_success_Cluster() {
		log.debug("should_add_success_Cluster ... ")
		val addCluster: ClusterVo = ClusterVo.builder {
			dataCenterVo { IdentifiedVo.builder { id { dataCenterId } } }
			name { "testCluster" }
			cpuArc { Architecture.X86_64 }
			cpuType { "Intel Nehalem Family" }
			description { "testDescription" }
			comment { "testComment" }
			networkVo { IdentifiedVo.builder { id { networkId } } }
			biosType { BiosType.Q35_SEA_BIOS }
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
		assertThat(result?.dataCenterVo?.id, `is`(addCluster.dataCenterVo.id))
		assertThat(result?.name, `is`(addCluster.name))
		assertThat(result?.description, `is`(addCluster.description))
		assertThat(result?.comment, `is`(addCluster.comment))
		assertThat(result?.networkVo?.id, `is`(addCluster.networkVo.id))
		assertThat(result?.biosType, `is`(addCluster.biosType))
		assertThat(result?.fipsMode, `is`(addCluster.fipsMode))
		assertThat(result?.version, `is`(addCluster.version))
		assertThat(result?.switchType, `is`(addCluster.switchType))
		assertThat(result?.firewallType, `is`(addCluster.firewallType))
		assertThat(result?.logMaxMemory, `is`(addCluster.logMaxMemory))
		assertThat(result?.logMaxMemoryType, `is`(addCluster.logMaxMemoryType))
		assertThat(result?.virtService, `is`(addCluster.virtService))
		assertThat(result?.glusterService, `is`(addCluster.glusterService))
		assertThat(result?.errorHandling, `is`(addCluster.errorHandling))
		assertThat(result?.bandwidth, `is`(addCluster.bandwidth))
		assertThat(result?.encrypted, `is`(addCluster.encrypted))
		assertThat(result?.networkProvider, `is`(addCluster.networkProvider))
	}

	/**
	 * [should_add_networkProvider_Cluster]
	 * [ItClusterService.add]에 대한 단위테스트
	 * 외부공급자 생성o
	 *
	 * @see ItClusterService.add
	 **/
	@Test
	fun should_add_networkProvider_Cluster() {
		log.debug("should_add_networkProvider_Cluster ... ")
		val addCluster: ClusterVo = ClusterVo.builder {
			dataCenterVo { IdentifiedVo.builder { id { dataCenterId } } }
			name { "testCluster2" }
			cpuArc { Architecture.X86_64 }
			cpuType { "Intel Nehalem Family" }
			description { "networkProvider" }
			comment { "testComment" }
			networkVo { IdentifiedVo.builder { id { networkId } } }// 관리 네트워크 ovirtmgmt
			biosType { BiosType.Q35_SEA_BIOS }
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
			networkProvider { true }
		}

		val result: ClusterVo? =
			service.add(addCluster)

		log.debug(result?.networkProvider.toString() + ", " + addCluster.networkProvider)


		assertThat(result, `is`(not(nullValue())))
		assertThat(result?.id, `is`(not(nullValue())))
		assertThat(result?.dataCenterVo?.id, `is`(addCluster.dataCenterVo.id))
		assertThat(result?.name, `is`(addCluster.name))
		assertThat(result?.description, `is`(addCluster.description))
		assertThat(result?.comment, `is`(addCluster.comment))
		assertThat(result?.networkVo?.id, `is`(addCluster.networkVo.id))
		assertThat(result?.biosType, `is`(addCluster.biosType))
		assertThat(result?.fipsMode, `is`(addCluster.fipsMode))
		assertThat(result?.version, `is`(addCluster.version))
		assertThat(result?.switchType, `is`(addCluster.switchType))
		assertThat(result?.firewallType, `is`(addCluster.firewallType))
		assertThat(result?.logMaxMemory, `is`(addCluster.logMaxMemory))
		assertThat(result?.logMaxMemoryType, `is`(addCluster.logMaxMemoryType))
		assertThat(result?.virtService, `is`(addCluster.virtService))
		assertThat(result?.glusterService, `is`(addCluster.glusterService))
		assertThat(result?.errorHandling, `is`(addCluster.errorHandling))
		assertThat(result?.bandwidth, `is`(addCluster.bandwidth))
		assertThat(result?.encrypted, `is`(addCluster.encrypted))
		assertThat(result?.networkProvider, `is`(addCluster.networkProvider))
	}

	/**
	 * [should_update_Cluster]
	 * [ItClusterService.update]에 대한 단위테스트
	 *	미완
	 *
	 * @see ItClusterService.update
	 **/
	@Test
	fun should_update_Cluster() {
		log.debug("should_update_Cluster ... ")
		val clusterId = ""

		val updateCluster: ClusterVo = ClusterVo.builder {
			id { clusterId }
			dataCenterVo { IdentifiedVo.builder { id { dataCenterId } } }
			name { "testCluster1" }
			cpuArc { Architecture.X86_64 }
			cpuType { "Intel Nehalem Family" }
			description { "testDescription" }
			comment { "testComment" }
			networkVo { IdentifiedVo.builder { id { networkId } } }
			biosType { BiosType.Q35_SEA_BIOS }
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
			service.add(updateCluster)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result?.id, `is`(not(nullValue())))
		assertThat(result?.dataCenterVo?.id, `is`(updateCluster.dataCenterVo.id))
		assertThat(result?.name, `is`(updateCluster.name))
		assertThat(result?.description, `is`(updateCluster.description))
		assertThat(result?.comment, `is`(updateCluster.comment))
		assertThat(result?.networkVo?.id, `is`(updateCluster.networkVo.id))
		assertThat(result?.biosType, `is`(updateCluster.biosType))
		assertThat(result?.fipsMode, `is`(updateCluster.fipsMode))
		assertThat(result?.version, `is`(updateCluster.version))
		assertThat(result?.switchType, `is`(updateCluster.switchType))
		assertThat(result?.firewallType, `is`(updateCluster.firewallType))
		assertThat(result?.logMaxMemory, `is`(updateCluster.logMaxMemory))
		assertThat(result?.logMaxMemoryType, `is`(updateCluster.logMaxMemoryType))
		assertThat(result?.virtService, `is`(updateCluster.virtService))
		assertThat(result?.glusterService, `is`(updateCluster.glusterService))
		assertThat(result?.errorHandling, `is`(updateCluster.errorHandling))
		assertThat(result?.bandwidth, `is`(updateCluster.bandwidth))
		assertThat(result?.encrypted, `is`(updateCluster.encrypted))
		assertThat(result?.networkProvider, `is`(updateCluster.networkProvider))
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
		val clusterId = ""
		val removeCluster =
			service.remove(clusterId)

//		assertThat(removeCluster, `is`(not(nullValue())))
		assertThat(removeCluster, `is`(false))
	}

	/**
	 * [should_findAllNetworksFromCluster]
	 * [ItClusterService.findAllNetworksFromCluster]에 대한 단위테스트
	 *
	 * @see ItClusterService.findAllNetworksFromCluster
	 **/
	@Test
	fun should_findAllNetworksFromCluster() {
		log.debug("should_findAllNetworksFromCluster ... ")
		val result: List<NetworkVo> =
			service.findAllNetworksFromCluster(clusterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(3))
		result.forEach { println(it) }
	}

	/**
	 * [should_findAllHostsFromCluster]
	 * [ItClusterService.findAllHostsFromCluster]에 대한 단위테스트
	 *
	 * @see ItClusterService.findAllHostsFromCluster
	 **/
	@Test
	fun should_findAllHostsFromCluster() {
		log.debug("should_findAllHostsFromCluster ... ")
		val result: List<HostVo> =
			service.findAllHostsFromCluster(clusterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(2))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllVmsFromCluster]
	 * [ItClusterService.findAllVmsFromCluster]에 대한 단위테스트
	 * 
	 * @see ItClusterService.findAllVmsFromCluster
	 **/
	@Test
	fun should_findAllVmsFromCluster() {
		log.debug("should_findAllVmsFromCluster ... ")
		val result: List<VmVo> =
			service.findAllVmsFromCluster(clusterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(5))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllAffinityGroupsByCluster]
	 * [ItClusterService.findAllAffinityGroupsByCluster]에 대한 단위테스트
	 *
	 * @see ItClusterService.findAllAffinityGroupsByCluster
	 */
	/*@Test
	fun should_findAllAffinityGroupsByCluster() {
		log.debug("should_findAllAffinityGroupsByCluster ... ")
		val result: List<AffinityGroupVo> =
			service.findAllAffinityGroupsByCluster(clusterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(1))

		result.forEach { println(it) }
	}*/

	/*@Test
	fun should_getAffinitylabel() {
		log.debug("should_getAffinitylabel ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}*/

	/**
	 * [should_findAllPermissionsFromCluster]
	 * [ItClusterService.findAllPermissionsFromCluster]에 대한 단위테스트
	 * 
	 * @see ItClusterService.findAllPermissionsFromCluster
	 **/
	@Test		
	fun should_findAllPermissionsFromCluster() {
		log.debug("should_findAllPermissionsFromCluster ... ")
		val result: List<PermissionVo> =
			service.findAllPermissionsFromCluster(clusterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(3))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllEventsFromCluster]
	 * [ItClusterService.findAllEventsFromCluster]에 대한 단위테스트
	 * 
	 * @see ItClusterService.findAllEventsFromCluster
	 **/
	@Test			
	fun should_findAllEventsFromCluster() {
		log.debug("should_findAllEventsFromCluster ... ")
		val result: List<EventVo> =
			service.findAllEventsFromCluster(clusterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(329))
	}


	companion object {
		private val log by LoggerDelegate()
	}
}