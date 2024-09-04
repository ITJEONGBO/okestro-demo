package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.VnicProfileVo
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.storage.DiskImageVo
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItVmServiceTest]
 * [ItVmService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.03.05
 */
@SpringBootTest
class ItVmServiceTest {
	@Autowired private lateinit var service: ItVmService

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
	 * [ItVmService.findAll]에 대한 단위테스트
	 *
	 * @see [ItVmService.findAll]
	 */
	@Test
	fun should_findAll() {
		log.debug("should_findAll ...")
		val result: List<VmVo> =
			service.findAll()

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(3))

		result.forEach { println(it) }
	}

	/**
	 * [should_findOne]
	 * [ItVmService.findOne]에 대한 단위테스트
	 *
	 * @see ItVmService.findOne
	 */
	@Test
	fun should_findOne() {
		log.debug("should_findOne ... ")
		val result: VmVo? =
			service.findOne(hostVm)
		assertThat(result, `is`(not(nullValue())))
		assertThat(result?.id, `is`(not(nullValue())))
		assertThat(result?.name, `is`("HostedEngine"))
		print(result)
	}

	/**
	 * [should_findAllDiskImagesBy]
	 * [ItVmService.findAllDiskImagesBy]에 대한 단위테스트
	 * TODO 디스크 목록기준이 애매함
	 * @see [ItVmService.findAllDiskImagesBy]
	 */
	@Test
	fun should_findAllDiskImagesBy() {
		log.debug("should_findAllDiskImagesBy ...")
		val result: List<DiskImageVo> =
			service.findAllDiskImagesBy()

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(0))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllVnicProfilesBy]
	 * [ItVmService.findAllVnicProfilesBy]에 대한 단위테스트
	 *
	 * @see [ItVmService.findAllVnicProfilesBy]
	 */
	@Test
	fun should_findAllVnicProfilesBy() {
		log.debug("should_findAllVnicProfilesBy ...")
		val result: List<VnicProfileVo> =
			service.findAllVnicProfilesBy(clusterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(4))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllCpuProfilesBy]
	 * [ItVmService.findAllCpuProfilesBy]에 대한 단위테스트
	 *
	 * @see [ItVmService.findAllCpuProfilesBy]
	 */
	@Test
	fun should_findAllCpuProfilesBy() {
		log.debug("should_findAllCpuProfilesBy ...")
		val result: List<CpuProfileVo> =
			service.findAllCpuProfilesBy(clusterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(1))

		result.forEach { println(it) }
	}

	/**
	 * [findAllISOsBy]
	 * [ItVmService.findAllISOsBy]에 대한 단위테스트
	 * TODO
	 * @see [ItVmService.findAllISOsBy]
	 */
	@Test
	fun should_findAllIsosBy() {
		log.debug("should_findAllISOsBy ...")
		val result: List<IdentifiedVo> =
			service.findAllISOsBy()

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(2))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllAffinityGroupsBy]
	 * [ItVmService.findAllAffinityGroupsBy]에 대한 단위테스트
	 * TODO
	 * @see [ItVmService.findAllAffinityGroupsBy]
	 */
	@Test
	fun should_findAllAffinityGroupsBy() {
		log.debug("should_findAllAffinityGroupsBy ...")
		val result: List<IdentifiedVo> =
			service.findAllAffinityGroupsBy(clusterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(0))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllAffinityLabelsBy]
	 * [ItVmService.findAllAffinityLabelsBy]에 대한 단위테스트
	 * TODO
	 * @see [ItVmService.findAllAffinityLabelsBy]
	 */
	@Test
	fun should_findAllAffinityLabelsBy() {
		log.debug("should_findAllAffinityLabelsBy ...")
		val result: List<IdentifiedVo> =
			service.findAllAffinityLabelsBy()

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(0))

		result.forEach { println(it) }
	}


	/**
	 * [should_add_Vm]
	 * [ItVmService.add]에 대한 단위테스트
	 * 
	 * @see ItVmService.add
	 */
	@Test
	fun should_add_Vm() {
		log.debug("should_add_Vm ... ")
		val addVm: VmVo = VmVo.builder {

		}

		val addResult: VmVo? =
			service.add(addVm)

		assertThat(addResult, `is`(not(nullValue())))
		assertThat(addResult?.id, `is`(not(nullValue())))
		assertThat(addResult?.id, `is`(addVm.id))
	}

	/**
	 * [should_update_Vm]
	 * [ItVmService.update]에 대한 단위테스트
	 *
	 * @see ItVmService.update
	 */
	@Test
	fun should_update_Vm() {
		log.debug("should_update_Vm ... ")
		val updateVm: VmVo = VmVo.builder {

		}

		val updateResult: VmVo? =
			service.update(updateVm)

		assertThat(updateResult, `is`(not(nullValue())))
		assertThat(updateResult?.id, `is`(not(nullValue())))
		assertThat(updateResult?.id, `is`(updateVm.id))
	}

	/**
	 * [should_remove_Vm]
	 * [ItVmService.remove]에 대한 단위테스트
	 *
	 * @see ItVmService.remove
	 */
	@Test
	fun should_remove_Vm() {
		log.debug("should_remove_Vm ... ")
		val vmId = "s"
		val disk = false
		val removeResult: Boolean =
			service.remove(vmId, disk)

		assertThat(removeResult, `is`(not(nullValue())))
		assertThat(removeResult, `is`(true))
	}


	/**
	 * [should_findAllApplicationsByVm]
	 * [ItVmService.findAllApplicationsByVm]에 대한 단위테스트
	 *
	 * @see ItVmService.findAllApplicationsByVm
	 */
	@Test
	fun should_findAllApplicationsByVm() {
		log.debug("should_findAllApplicationsByVm ... ")
		val result: List<IdentifiedVo> =
			service.findAllApplicationsByVm(hostVm)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(2))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllGuestFromVm]
	 * [ItVmService.findAllGuestFromVm]에 대한 단위테스트
	 *
	 * @see ItVmService.findAllGuestFromVm
	 */
	@Test
	fun should_findAllGuestFromVm() {
		log.debug("should_findAllGuestFromVm ... ")
		val result: GuestInfoVo? =
			service.findAllGuestFromVm(hostVm)

		assertThat(result, `is`(not(nullValue())))

		println(result)
	}

	/**
	 * [should_findAllPermissionsFromVm]
	 * [ItVmService.findAllPermissionsFromVm]에 대한 단위테스트
	 *
	 * @see ItVmService.findAllPermissionsFromVm
	 */
	@Test
	fun should_findAllPermissionsFromVm() {
		log.debug("should_findAllPermissionsFromVm ... ")
		val result: List<PermissionVo> =
			service.findAllPermissionsFromVm(hostVm)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(3))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllEventsFromVm]
	 * [ItVmService.findAllEventsFromVm]에 대한 단위테스트
	 *
	 * @see ItVmService.findAllEventsFromVm
	 */
	@Test
	fun should_findAllEventsFromVm() {
		log.debug("should_findAllEventsFromVm ... ")
		val result: List<EventVo> =
			service.findAllEventsFromVm(hostVm)

		assertThat(result, `is`(not(nullValue())))
//		assertThat(result.size, `is`(0))

		result.forEach { println(it) }
	}

	/**
	 * [should_findConsole]
	 * [ItVmService.findConsole]에 대한 단위테스트
	 *
	 * @see ItVmService.findConsole
	 */
	@Test
	fun should_findConsole() {
		log.debug("should_findConsole ... ")
		val result: ConsoleVo? =
			service.findConsole(hostVm)

		assertThat(result, `is`(not(nullValue())))
		print(result)
	}
		

	companion object {
		private val log by LoggerDelegate()
	}
}