package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.HostNicVo
import com.itinfo.itcloud.model.setting.PermissionVo
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.BeforeEach
import org.ovirt.engine.sdk4.types.Host
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItHostServiceTest]
 * [ItHostService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.08.28
 */
@SpringBootTest
class ItHostServiceTest {
	@Autowired private lateinit var service: ItHostService

	private lateinit var dataCenterId: String
	private lateinit var clusterId: String // Default
	private lateinit var networkId: String // ovirtmgmt(dc: Default)
	private lateinit var hostId: String // host02.ititinfo.local
	private lateinit var host01: String // host01

	@BeforeEach
	fun setup() {
		dataCenterId = "6cde7270-6459-11ef-8be2-00163e5d0646"
		clusterId = "6ce0356a-6459-11ef-a03a-00163e5d0646"
		networkId = "00000000-0000-0000-0000-000000000009"
		hostId = "789b78c4-3fcf-4f19-9b69-d382aa66c12f"
		host01 = "c36563e3-83eb-49c7-91c0-fc4b197387b2"
	}

	/**
	 * [should_findAll]
	 * [ItHostService.findAll]에 대한 단위테스트
	 * 
	 * @see [ItHostService.findAll]
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
	 * @see [ItHostService.findOne]
	 */
	@Test
	fun should_findOne() {
		log.debug("should_findOne ...")
		val result: HostVo? =
			service.findOne(hostId)

		assertThat(result, `is`(not(nullValue())))
		println(result)
	}


	/**
	 * [should_add_Host]
	 * [ItHostService.add]에 대한 단위테스트
	 *
	 * @see [ItHostService.add]
	 */
	@Test
	fun should_add_Host() {
		log.debug("should_add_Host ...")
		val addHost: HostVo = HostVo.builder {
			clusterVo { IdentifiedVo.builder { id { clusterId } } }
			name { "host01.ititinfo.local" }
			comment { "192.168.0.71" }
			address { "host01.ititinfo.local" }
			sshPort { 22 }
			sshPassWord { "adminRoot!@#" }
            spmPriority { 1 }
		}

		val addResult: HostVo? =
			service.add(addHost)

		assertThat(addResult, `is`(not(nullValue())))
		assertThat(addResult?.id, `is`(not(nullValue())))
		assertThat(addResult?.clusterVo?.id, `is`(addHost.clusterVo.id))
		assertThat(addResult?.name, `is`(addHost.name))
		assertThat(addResult?.comment, `is`(addHost.comment))
		assertThat(addResult?.address, `is`(addHost.address))
		assertThat(addResult?.sshPort, `is`(addHost.sshPort))
		assertThat(addResult?.spmPriority, `is`(addHost.spmPriority))
	}

	/**
	 * [should_update_Host]
	 * [ItHostService.update]에 대한 단위테스트
	 *
	 * @see [ItHostService.update]
	 */
	@Test
	fun should_update_Host() {
		// TODO
		// com.itinfo.util.ovirt.error.ItCloudException: Fault reason is 'Operation Failed'. Fault detail is '[Cannot edit Host. Host parameters cannot be modified while Host is operational.
		// Please switch Host to Maintenance mode first.]'. HTTP response code is '409'. HTTP response message is 'Conflict'.
		log.debug("should_update_Host ...")
		val updateHost: HostVo = HostVo.builder {
			id { host01 }
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
	}

	/**
	 * [should_remove_Host]
	 * [ItHostService.remove]에 대한 단위테스트
	 *
	 * @see [ItHostService.remove]
	 */
	@Test
	fun should_remove_Host() {
		log.debug("should_remove_Host ...")
		val removeResult: Boolean =
			service.remove(host01)
		assertThat(removeResult, `is`(not(nullValue())))
		assertThat(removeResult, `is`(true))
	}


	/**
	 * [should_findAllVmsFromHost]
	 * [ItHostService.findAllVmsFromHost]에 대한 단위테스트
	 * 
	 * @see [ItHostService.findAllVmsFromHost]
	 */
	@Test
	fun should_findAllVmsFromHost() {
		log.debug("should_findAllVmsFromHost ...")
		val result: List<VmVo> =
			service.findAllVmsFromHost(hostId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(1))
		result.forEach { println(it) }
	}

	/**
	 * [should_findAllHostNicsFromHost]
	 * [ItHostService.findAllHostNicsFromHost]에 대한 단위테스트
	 *
	 * @see [ItHostService.findAllHostNicsFromHost]
	 */
	@Test
	fun should_findAllHostNicsFromHost() {
		log.debug("should_findAllHostNicsFromHost ...")
		val result: List<HostNicVo> =
			service.findAllHostNicsFromHost(hostId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(1))
		result.forEach { println(it) }
	}

	/**
	 * [should_findAllHostDevicesFromHost]
	 * [ItHostService.findAllHostDevicesFromHost]에 대한 단위테스트
	 *
	 * @see [ItHostService.findAllHostDevicesFromHost]
	 */
	@Test
	fun should_findAllHostDevicesFromHost() {
		log.debug("should_findAllHostDevicesFromHost ...")
		val result: List<HostDeviceVo> =
			service.findAllHostDevicesFromHost(hostId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(1))
		result.forEach { println(it) }
	}

	/**
	 * [should_findAllPermissionsFromHost]
	 * [ItHostService.findAllPermissionsFromHost]에 대한 단위테스트
	 *
	 * @see [ItHostService.findAllPermissionsFromHost]
	 */
	@Test
	fun should_findAllPermissionsFromHost() {
		log.debug("should_findAllPermissionsFromHost ...")
		val result: List<PermissionVo> =
			service.findAllPermissionsFromHost(hostId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(3))
		result.forEach { println(it) }
	}

	/**
	 * [should_findAllAffinityLabelsFromHost]
	 * [ItHostService.findAllAffinityLabelsFromHost]에 대한 단위테스트
	 * TODO
	 * @see [ItHostService.findAllAffinityLabelsFromHost]
	 */
	@Test
	fun should_findAllAffinityLabelsFromHost() {
		log.debug("should_findAllAffinityLabelsFromHost ...")
		val result: List<AffinityLabelVo> =
			service.findAllAffinityLabelsFromHost(hostId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(0))
		result.forEach { println(it) }
	}

	/**
	 * [should_findAllEventsFromHost]
	 * [ItHostService.findAllEventsFromHost]에 대한 단위테스트
	 *
	 * @see [ItHostService.findAllEventsFromHost]
	 */
	@Test
	fun should_findAllEventsFromHost() {
		log.debug("should_findAllEventsFromHost ...")
		val result: List<EventVo> =
			service.findAllEventsFromHost(hostId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(197)) // ?
		result.forEach { println(it) }
	}
	
	companion object {
		private val log by LoggerDelegate()
	}
}