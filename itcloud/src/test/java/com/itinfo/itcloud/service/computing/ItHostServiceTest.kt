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
	private lateinit var host05: String // host02.ititinfo.local

	@BeforeEach
	fun setup() {
		dataCenterId = "023b0a26-3819-11ef-8d02-00163e6c8feb"
		clusterId = "023c79d8-3819-11ef-bf08-00163e6c8feb"
		networkId = "00000000-0000-0000-0000-000000000009"
		host01 = "671e18b2-964d-4cc6-9645-08690c94d249"
		host05 = "70457998-0298-4e25-92ac-c74446bd19e9"
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
		val start = System.currentTimeMillis()
		val result: List<HostVo> =
			service.findAll()
		val end = System.currentTimeMillis()

		log.info("수행시간: {}", end-start)
		assertThat(result, `is`(not(nullValue())))
//		assertThat(result.size, `is`(2))
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
			service.findOne(host01)

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
		log.debug("should_add_update_and_remove_Host ...")
//		val addHost: HostVo = HostVo.builder {
//			clusterVo { IdentifiedVo.builder { id { "5c563dce-7a1c-4465-9018-2f9ef76e9772" } } }
//			name { "rutilvm-dev.host04" }
//			comment { "rutilvm-dev.host04" }
//			address { "rutilvm-dev.host04" }
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
//			id { addResult?.id }
			id { "b5b6490a-6ce7-4d66-a454-fbea6f164b9a" }
			name { "rutilvm-dev.host04-test" }
			comment { "rutilvm-dev.host04-0" }
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

	@Test
	fun should_remove_Host() {
		log.debug("should_remove_Host ...")
		val removeResult = service.remove(host05)

		assertThat(removeResult, `is`(not(nullValue())))
		assertThat(removeResult, `is`(true))
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
			service.findAllVmsFromHost(host01)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(1))
	}

	/**
	 * [should_findAllHostNicsFromHost]
	 * [ItHostService.findAllHostNicsFromHost]에 대한 단위테스트
	 *
	 * @see ItHostService.findAllHostNicsFromHost
	 */
	@Test
	fun should_findAllHostNicsFromHost() {
		log.debug("should_findAllHostNicFromHost ...")

		val result: List<HostNicVo> =
			service.findAllHostNicsFromHost("1d3a2fdb-0873-4837-8eaa-28cca20ffb12")

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(1))
	}

	/**
	 * [should_setHostNicsFromHost]
	 * [ItHostService.setHostNicsFromHost]에 대한 단위테스트
	 *
	 * @see ItHostService.setHostNicsFromHost
	 */
	@Test
	fun should_setHostNicsFromHost() {
		log.debug("should_setHostNicsFromHost ...")

		val result: List<HostNicVo> =
			service.setHostNicsFromHost("1d3a2fdb-0873-4837-8eaa-28cca20ffb12")

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
			service.findAllHostDevicesFromHost(host01)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(99))
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
			service.findAllEventsFromHost(host01)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(842))
	}
	
	companion object {
		private val log by LoggerDelegate()
	}
}