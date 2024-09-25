package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.HostNicVo
import com.itinfo.itcloud.model.network.NetworkVo
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
 * [ItHostServiceTest]
 * [ItHostService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.09.24
 */
@SpringBootTest
class ItHostServiceTest {
	@Autowired private lateinit var service: ItHostService

	private lateinit var dataCenterId: String
	private lateinit var clusterId: String // Default
	private lateinit var networkId: String // ovirtmgmt(dc: Default)
	private lateinit var host01: String // host01
	private lateinit var host02: String // host02.ititinfo.local

	@BeforeEach
	fun setup() {
		dataCenterId = "6cde7270-6459-11ef-8be2-00163e5d0646"
		clusterId = "6ce0356a-6459-11ef-a03a-00163e5d0646"
		networkId = "00000000-0000-0000-0000-000000000009"
		host01 = "722096d3-4cb2-43b0-bf41-dd69c3a70779"
		host02 = "789b78c4-3fcf-4f19-9b69-d382aa66c12f"
	}

	/**
	 * [should_findAll]
	 * [ItHostService.findAll]에 대한 단위테스트
	 * 
	 * @see ItHostService.findAll
	 */
	@Test
	fun should_findAll() {
		log.debug("should_findAll ...")
		val result: List<HostVo> =
			service.findAll()

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(2))

		result.forEach { println(it) }
	}

	/**
	 * [should_findOne]
	 * [ItHostService.findOne]에 대한 단위테스트
	 * 
	 * @see ItHostService.findOne
	 */
	@Test
	fun should_findOne() {
		log.debug("should_findOne ...")
		val result: HostVo? =
			service.findOne(host02)

		assertThat(result, `is`(not(nullValue())))
		println(result)
	}

	/**
	 * [should_add_update_and_remove_Host]
	 * [ItHostService.add], [ItHostService.update], [ItHostService.remove]에 대한 단위테스트
	 *
	 * @see ItHostService.add
	 * @see ItHostService.update
	 * @see ItHostService.remove
	 */
	@Test
	fun should_add_update_and_remove_Host() {
//		log.debug("should_add_update_and_remove_Host ...")
//		val addHost: HostVo = HostVo.builder {
//			clusterVo { IdentifiedVo.builder { id { clusterId } } }
//			name { "host01.ititinfo.local" }
//			comment { "192.168.0.71" }
//			address { "host01.ititinfo.local" }
//			sshPort { 22 }
//			sshPassWord { "adminRoot!@#" }
//            spmPriority { 5 }
//		}
//
//		val addResult: HostVo? =
//			service.add(addHost)
//
//		assertThat(addResult, `is`(not(nullValue())))
//		assertThat(addResult?.id, `is`(not(nullValue())))
////		assertThat(addResult?.clusterVo?.id, `is`(addHost.clusterVo.id))
//		assertThat(addResult?.name, `is`(addHost.name))
//		assertThat(addResult?.comment, `is`(addHost.comment))
//		assertThat(addResult?.address, `is`(addHost.address))
//		assertThat(addResult?.sshPort, `is`(addHost.sshPort))
//		assertThat(addResult?.spmPriority, `is`(addHost.spmPriority))


		log.debug("should_update_Host ...")
		val updateHost: HostVo = HostVo.builder {
			id { "5169e7c0-789c-47f6-b9b1-0c5a7f3bb52c"}
//			id { addResult?.id }
			name { "host01.ititinfo.local2" }
			comment { "192.168.0.71-test" }
			spmPriority { 5 }
		}

		val updateResult: HostVo? =
			service.update(updateHost)

		assertThat(updateResult, `is`(not(nullValue())))
		assertThat(updateResult?.id, `is`(updateHost.id))
		assertThat(updateResult?.name, `is`(updateHost.name))
		assertThat(updateResult?.comment, `is`(updateHost.comment))
		assertThat(updateResult?.spmPriority, `is`(updateHost.spmPriority))

//		log.debug("should_remove_Host ...")
//		val removeResult =
//			updateResult?.let { service.remove(it.id) }
//
//		assertThat(removeResult, `is`(not(nullValue())))
//		assertThat(removeResult, `is`(true))
	}

	/**
	 * [should_findAllVmsFromHost]
	 * [ItHostService.findAllVmsFromHost]에 대한 단위테스트
	 * 
	 * @see ItHostService.findAllVmsFromHost
	 */
	@Test
	fun should_findAllVmsFromHost() {
		log.debug("should_findAllVmsFromHost ...")
		val result: List<VmVo> =
			service.findAllVmsFromHost(host02)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(3))
	}

	/**
	 * [should_findAllHostNicsFromHost]
	 * [ItHostService.findAllNicsFromHost]에 대한 단위테스트
	 *
	 * @see ItHostService.findAllNicsFromHost
	 */
	@Test
	fun should_findAllHostNicsFromHost() {
		log.debug("should_findAllHostNicFromHost ...")
		val result: List<HostNicVo> =
			service.findAllNicsFromHost(host02)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(1))
	}

	/**
	 * [should_setUpNetworksFromHost]
	 * [ItHostService.setUpNetworksFromHost]에 대한 단위테스트
	 *
	 * @see ItHostService.setUpNetworksFromHost
	 */
	@Test
	fun should_setUpNetworksFromHost() {
		log.debug("should_setUpNetworksFromHost ...")
		val hostId = ""
		val networkVo: NetworkVo =
			NetworkVo.builder {

			}

		val result: Boolean =
			service.setUpNetworksFromHost(hostId, networkVo)

		assertThat(result, `is`(not(nullValue())))
	}

	/**
	 * [should_findAllHostDevicesFromHost]
	 * [ItHostService.findAllHostDevicesFromHost]에 대한 단위테스트
	 *
	 * @see ItHostService.findAllHostDevicesFromHost
	 */
	@Test
	fun should_findAllHostDevicesFromHost() {
		log.debug("should_findAllHostDevicesFromHost ...")
		val result: List<HostDeviceVo> =
			service.findAllHostDevicesFromHost(host02)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(97))
	}

	/**
	 * [should_findAllPermissionsFromHost]
	 * [ItHostService.findAllPermissionsFromHost]에 대한 단위테스트
	 *
	 * @see ItHostService.findAllPermissionsFromHost
	 */
	@Test
	fun should_findAllPermissionsFromHost() {
		log.debug("should_findAllPermissionsFromHost ...")
		val result: List<PermissionVo> =
			service.findAllPermissionsFromHost(host02)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(5))
	}

	/**
	 * [should_findAllEventsFromHost]
	 * [ItHostService.findAllEventsFromHost]에 대한 단위테스트
	 *
	 * @see ItHostService.findAllEventsFromHost
	 */
	@Test
	fun should_findAllEventsFromHost() {
		log.debug("should_findAllEventsFromHost ...")
		val result: List<EventVo> =
			service.findAllEventsFromHost(host02)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(727))
	}
	
	companion object {
		private val log by LoggerDelegate()
	}
}