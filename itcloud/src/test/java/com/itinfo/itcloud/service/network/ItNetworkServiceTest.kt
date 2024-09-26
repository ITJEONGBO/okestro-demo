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
import java.util.*

/**
 * [ItNetworkServiceTest]
 * [ItNetworkService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.09.26
 */
@SpringBootTest
class ItNetworkServiceTest {
	@Autowired private lateinit var service: ItNetworkService

	private lateinit var dataCenterId: String
	private lateinit var clusterId: String // Default
	private lateinit var networkId: String // ovirtmgmt(dc: Default)
	private lateinit var host01: String // host01
	private lateinit var host02: String // host02.ititinfo.local
	private lateinit var hostVm: String // hostVm

	@BeforeEach
	fun setup() {
		dataCenterId = "023b0a26-3819-11ef-8d02-00163e6c8feb"
		clusterId = "023c79d8-3819-11ef-bf08-00163e6c8feb"
		networkId = "00000000-0000-0000-0000-000000000009"
		host01 = "671e18b2-964d-4cc6-9645-08690c94d249"
		host02 = "0d7ba24e-452f-47fe-a006-f4702aa9b37f"
		hostVm = "c2ae1da5-ce4f-46df-b337-7c471bea1d8d"
	}


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
			service.findAll()

		assertThat(result, `is`(not(nullValue())))
		result.forEach { print(it) }
		assertThat(result.size, `is`(5))
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
		println(result)
		assertThat(result?.name, `is`("ovirtmgmt"))
	}

	/**
	 * [should_add_update_and_remove_network]
	 * [ItNetworkService.add]에 대한 단위테스트
	 *
	 * @see ItNetworkService.add
	 * @see ItNetworkService.update
	 * @see ItNetworkService.remove
	 */
	@Test
	fun should_add_update_and_remove_network() {
		log.debug("should_add ... ")
		val addNetworkVo: NetworkVo = NetworkVo.builder {
			dataCenter { IdentifiedVo.builder { id { dataCenterId } } }
			name { "gs" }
			description { "asdfasdf" }
			comment { "t" }
			usage { UsageVo.builder { vm { true } } }
			portIsolation { false }
			mtu { 0 }
			vlan { 0 }
			openStackNetwork { null }
			clusters {
				Arrays.asList(
					ClusterVo.builder {
						id { "023c79d8-3819-11ef-bf08-00163e6c8feb" }
						required { true }
					},
				)
			}
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


		log.debug("should_update ... ")
		val updateNetworkVo: NetworkVo = NetworkVo.builder {
			dataCenter { IdentifiedVo.builder { id { dataCenterId } } }
			id { addResult?.id }
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
		assertThat(updateResult?.mtu, `is`(updateNetworkVo.mtu))
		assertThat(updateResult?.vlan, `is`(updateNetworkVo.vlan))

		log.debug("should_remove ... ")
		val removeResult: Boolean =
			addResult?.id?.let { service.remove(it) } == true

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
		val result: IdentifiedVo =
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
//		assertThat(result.size, `is`(1))
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
