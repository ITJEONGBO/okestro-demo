package com.itinfo.itcloud.service.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.ClusterVo
import com.itinfo.itcloud.model.computing.HostVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.network.*
import com.itinfo.itcloud.model.setting.PermissionVo
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.BeforeEach
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
	@Autowired private lateinit var service: ItNetworkService

	private lateinit var dataCenterId: String
	private lateinit var clusterId: String // Default
	private lateinit var networkId: String // ovirtmgmt(dc: Default)
	private lateinit var host02: String // host02.ititinfo.local
	private lateinit var host01: String // host01
	private lateinit var hostVm: String // hostVm

	@BeforeEach
	fun setup() {
		dataCenterId = "6cde7270-6459-11ef-8be2-00163e5d0646"
		clusterId = "6ce0356a-6459-11ef-a03a-00163e5d0646"
		networkId = "00000000-0000-0000-0000-000000000009"
		host02 = "789b78c4-3fcf-4f19-9b69-d382aa66c12f"
		host01 = "722096d3-4cb2-43b0-bf41-dd69c3a70779"
		hostVm = "c26e287c-bc48-4da7-9977-61203abf9e64"
	}

	/**
	 * [should_findAll]
	 * [ItNetworkService.findAll]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.findAll
	 */
//	@Test
//	fun should_findAll() {
//		log.debug("should_findAll ... ")
//		val result: List<NetworkVo> =
//			service.findAll()
//
//		assertThat(result, `is`(not(nullValue())))
////		assertThat(result.size, `is`(4))
//
//		result.forEach { print(it) }
//	}

	/**
	 * [should_findAll]
	 * [ItNetworkService.findAll]에 대한 단위테스트
	 *
	 * @see ItNetworkService.findAll
	 */
	@Test
	fun should_findAll() {
		log.debug("should_findAll ... ")
		val result: List<NetworkVo> =
			service.findAllFromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(3))

		result.forEach { print(it) }
	}

	/**
	 * [should_findOne]
	 * [ItNetworkService.findOne]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.findOne
	 */
	@Test
	fun should_findOne() {
		log.debug("should_findOne ... ")
		val result: NetworkVo? =
			service.findOne(networkId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result?.name, `is`("ovirtmgmt"))

		println(result)
	}


	/**
	 * [should_add]
	 * [ItNetworkService.add]에 대한 단위테스트
	 *
	 * @see ItNetworkService.add
	 */
	@Test
	fun should_add() {
		log.debug("should_add ... ")
		val addNetworkVo: NetworkVo = NetworkVo.builder {
			dataCenter { IdentifiedVo.builder { id { dataCenterId } } }
			name { "fs" }
			description { "t" }
			comment { "t" }
			usage { UsageVo.builder { vm { true } } }
			portIsolation { false }
			mtu { 0 }
			vlan { 0 }
			openStackNetwork { null }
		}

		val addResult: NetworkVo? =
			service.add(addNetworkVo)

		assertThat(addResult, `is`(not(nullValue())))
		assertThat(addResult?.name, `is`(addNetworkVo.name))
		assertThat(addResult?.description, `is`(addNetworkVo.description))
		assertThat(addResult?.comment, `is`(addNetworkVo.comment))
		assertThat(addResult?.usage?.vm, `is`(addNetworkVo.usage.vm))
		assertThat(addResult?.portIsolation, `is`(addNetworkVo.portIsolation))
		assertThat(addResult?.mtu, `is`(addNetworkVo.mtu))
		assertThat(addResult?.vlan, `is`(addNetworkVo.vlan))
	}

	/**
	 * [should_add2]
	 * [ItNetworkService.add]에 대한 단위테스트
	 *
	 * @see ItNetworkService.add
	 */
	@Test
	fun should_add2() {
		log.debug("should_add2 ... ")
		val addNetworkVo: NetworkVo = NetworkVo.builder {
			dataCenter { IdentifiedVo.builder { id { dataCenterId } } }
			name { "asdf2" }
			description { "t2" }
			comment { "t2" }
			usage { UsageVo.builder { vm { false } } }  // TODO
			portIsolation { false }
			mtu { 142 }
			vlan { 23 }
			openStackNetwork { null }
		}

		val addResult: NetworkVo? =
			service.add(addNetworkVo)

		assertThat(addResult, `is`(not(nullValue())))
		assertThat(addResult?.name, `is`(addNetworkVo.name))
		assertThat(addResult?.description, `is`(addNetworkVo.description))
		assertThat(addResult?.comment, `is`(addNetworkVo.comment))
//		assertThat(addResult?.usageVo?.vm, `is`(addNetworkVo.usageVo.vm))
		assertThat(addResult?.portIsolation, `is`(addNetworkVo.portIsolation))
		assertThat(addResult?.mtu, `is`(addNetworkVo.mtu))
		assertThat(addResult?.vlan, `is`(addNetworkVo.vlan))
	}

	/**
	 * [should_update]
	 * [ItNetworkService.update]에 대한 단위테스트
	 *
	 * @see ItNetworkService.update
	 */
	@Test
	fun should_update() {
		log.debug("should_update ... ")

		val networkId = "2255ff6c-51fc-431d-bf19-07d486de95fd"
		val updateNetworkVo: NetworkVo = NetworkVo.builder {
			dataCenter { IdentifiedVo.builder { id { dataCenterId } } }
			id { networkId }
			name { "asdf5" }
			description { "t2" }
			comment { "t2" }
			usage { UsageVo.builder { vm { true } } }  // TODO
			mtu { 0 }
			vlan { 0 }
		}

		val updateResult: NetworkVo? =
			service.update(updateNetworkVo)

		assertThat(updateResult, `is`(not(nullValue())))
		assertThat(updateResult?.name, `is`(updateNetworkVo.name))
		assertThat(updateResult?.description, `is`(updateNetworkVo.description))
		assertThat(updateResult?.comment, `is`(updateNetworkVo.comment))
//		assertThat(updateResult?.usageVo?.vm, `is`(updateNetworkVo.usageVo.vm)) // ?
		assertThat(updateResult?.mtu, `is`(updateNetworkVo.mtu))
		assertThat(updateResult?.vlan, `is`(updateNetworkVo.vlan))
	}

	/**
	 * [should_remove]
	 * [ItNetworkService.remove]에 대한 단위테스트
	 *
	 * @see ItNetworkService.remove
	 */
	@Test
	fun should_remove () {
		log.debug("should_remove ... ")
		val networkId = "80ca0671-f6dd-4c42-b2f4-8332150deecd"
		val removeResult: Boolean =
			service.remove(networkId)

		assertThat(removeResult, `is`(not(nullValue())))
		assertThat(removeResult, `is`(true))
	}

	/**
	 * [should_findAllNetworkProviderFromNetwork]
	 * [ItNetworkService.findAllNetworkProviderFromNetwork]에 대한 단위테스트
	 *
	 * @see ItNetworkService.findAllNetworkProviderFromNetwork
	 */
	@Test
	fun should_findAllNetworkProviderFromNetwork () {
		log.debug("should_findAllNetworkProviderFromNetwork ... ")
		val result: OpenStackNetworkVo? =
			service.findAllNetworkProviderFromNetwork()

		assertThat(result, `is`(not(nullValue())))
		print(result)
	}

	/**
	 * [should_findAllExternalNetworkFromNetworkProvider]
	 * [ItNetworkService.findAllExternalNetworkFromNetworkProvider]에 대한 단위테스트
	 *
	 * @see ItNetworkService.findAllExternalNetworkFromNetworkProvider
	 */
	@Test
	fun should_findAllExternalNetworkFromNetworkProvider () {
		log.debug("should_findAllExternalNetworkFromNetworkProvider ... ")
		val providerId = "4a21ce43-7bb2-4cf9-951b-5ea210ecfca6"
		val result: List<NetworkVo> =
			service.findAllExternalNetworkFromNetworkProvider(providerId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(1))
		result.forEach { print(it) }
	}

	/**
	 * [should_importNetwork]
	 * [ItNetworkService.importNetwork]에 대한 단위테스트
	 *
	 * @see ItNetworkService.importNetwork
	 */
	@Test
	fun should_importNetwork () {
		log.debug("should_importNetwork ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}


	/**
	 * [should_findAllVnicProfilesFromNetwork]
	 * [ItNetworkService.findAllVnicProfilesFromNetwork]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.findAllVnicProfilesFromNetwork
	 */
	@Test
	fun should_findAllVnicProfilesFromNetwork() {
		log.debug("findAllVnicProfilesFromNetwork ... ")
		val result: List<VnicProfileVo> =
			service.findAllVnicProfilesFromNetwork(networkId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(2))
		result.forEach { println(it) }
	}

	/**
	 * [should_findAllClustersFromNetwork]
	 * [ItNetworkService.findAllClustersFromNetwork]에 대한 단위테스트
	 *
	 * @see ItNetworkService.findAllClustersFromNetwork
	 */
	@Test
	fun should_findAllClustersFromNetwork() {
		log.debug("should_findAllClustersFromNetwork ... ")
		val result: List<ClusterVo> =
			service.findAllClustersFromNetwork(networkId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(3))
		result.forEach { println(it) }
	}

	/**
	 * [should_findAllHostsFromNetwork]
	 * [ItNetworkService.findAllHostsFromNetwork]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.findAllHostsFromNetwork
	 */
	@Test
	fun should_findAllHostsFromNetwork() {
		log.debug("should_findAllHostsFromNetwork ... ")
		val result: List<HostVo> =
			service.findAllHostsFromNetwork(networkId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(2))
		result.forEach { println(it) }
	}

	/**
	 * [should_findAllVmsFromNetwork]
	 * [ItNetworkService.findAllVmsFromNetwork]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.findAllVmsFromNetwork
	 */
	@Test
	fun should_findAllVmsFromNetwork() {
		log.debug("should_findAllVmsFromNetwork ... ")
		val result: List<VmVo> =
			service.findAllVmsFromNetwork(networkId)

		assertThat(result, `is`(not(nullValue())))
//		assertThat(result.size, `is`(7))

		println("---" + result.size)
		result.forEach { println(it) }
	}

	/**
	 * [should_findAllTemplatesFromNetwork]
	 * [ItNetworkService.findAllTemplatesFromNetwork]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.findAllTemplatesFromNetwork
	 */
	@Test
	fun should_findAllTemplatesFromNetwork() {
		log.debug("should_findAllTemplatesFromNetwork ... ")
		val result: List<NetworkTemplateVo> =
			service.findAllTemplatesFromNetwork(networkId)

		assertThat(result, `is`(not(nullValue())))
//		assertThat(result.size, `is`(3))
		result.forEach { println(it) }
	}

	/**
	 * [should_findAllPermissionsFromNetwork]
	 * [ItNetworkService.findAllPermissionsFromNetwork]에 대한 단위테스트
	 * 
	 * @see ItNetworkService.findAllPermissionsFromNetwork
	 */
	@Test
	fun should_findAllPermissionsFromNetwork() {
		log.debug("should_findAllPermissionsFromNetwork ... ")
		val result: List<PermissionVo> =
			service.findAllPermissionsFromNetwork(networkId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(4))
		result.forEach { println(it) }
	}


	companion object {
		private val log by LoggerDelegate()
	}
}
