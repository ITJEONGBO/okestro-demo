package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.CpuProfileVo
import com.itinfo.itcloud.model.computing.HostVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.network.VnicProfileVo
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.service.computing.ItHostServiceTest.Companion
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
		host01 = "c36563e3-83eb-49c7-91c0-fc4b197387b2"
		hostVm = "c2ae1da5-ce4f-46df-b337-7c471bea1d8d"
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
		assertThat(result.size, `is`(1))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllDiskImagesBy]
	 * [ItVmService.findAllDiskImagesBy]에 대한 단위테스트
	 * TODO
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
		assertThat(result.size, `is`(2))

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
	 * [should_findAllIsosBy]
	 * [ItVmService.findAllIsosBy]에 대한 단위테스트
	 * TODO
	 * @see [ItVmService.findAllIsosBy]
	 */
	@Test
	fun should_findAllIsosBy() {
		log.debug("should_findAllIsosBy ...")
		val result: List<DiskImageVo> =
			service.findAllIsosBy()

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(0))

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
		val addVm: VmVo = VmVo.builder {

		}

		val updateResult: VmVo? =
			service.update(addVm)

		assertThat(updateResult, `is`(not(nullValue())))
		assertThat(updateResult?.id, `is`(not(nullValue())))
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
	 * [should_migrateHostList_Vm]
	 * [ItVmService.migrateHostList]에 대한 단위테스트
	 * TODO
	 * @see [ItVmService.migrateHostList]
	 */
	@Test
	fun should_migrateHostList_Vm() {
		log.debug("should_migrateHostList ...")
		val vmId = ""
		val result: List<IdentifiedVo> =
			service.migrateHostList(vmId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(0))

		result.forEach { println(it) }
	}

	/**
	 * [should_migrate_Vm]
	 * [ItVmService.migrate]에 대한 단위테스트
	 *
	 * @see ItVmService.migrate
	 */
	@Test
	fun should_migrate_Vm() {
		log.debug("should_migrate_Vm ... ")
		val vmId = ""
		val result: Boolean =
			service.migrate(vmId, host01)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result, `is`(true))
	}

	/**
	 * [should_cancelMigration_Vm]
	 * [ItVmService.cancelMigration]에 대한 단위테스트
	 *
	 * @see ItVmService.cancelMigration
	 */
	@Test
	fun should_cancelMigration_Vm() {
		log.debug("should_cancelMigration_Vm ... ")
		val vmId = ""
		val result: Boolean =
			service.cancelMigration(vmId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result, `is`(true))
	}

	/**
	 * [should_exportOvaVm_Vm]
	 * [ItVmService.cancelMigration]에 대한 단위테스트
	 *
	 * @see ItVmService.exportOvaVm
	 */
	@Test
	fun should_exportOvaVm_Vm() {
		log.debug("should_exportOvaVm_Vm ... ")
		assertThat(service, `is`(not(nullValue())))
	}

	/**
	 * [should_findVm_Vm]
	 * [ItVmService.findVm]에 대한 단위테스트
	 *
	 * @see ItVmService.findVm
	 */
	@Test
	fun should_findVm_Vm() {
		log.debug("should_findVm_Vm ... ")
		assertThat(service, `is`(not(nullValue())))
	}

	/**
	 * [should_findAllApplicationsByVm_Vm]
	 * [ItVmService.findAllApplicationsByVm]에 대한 단위테스트
	 *
	 * @see ItVmService.findAllApplicationsByVm
	 */
	@Test
	fun should_findAllApplicationsByVm_Vm() {
		log.debug("should_findAllApplicationsByVm_Vm ... ")
		assertThat(service, `is`(not(nullValue())))
	}

	/**
	 * [should_findAllGuestFromVm_Vm]
	 * [ItVmService.findAllGuestFromVm]에 대한 단위테스트
	 *
	 * @see ItVmService.findAllGuestFromVm
	 */
	@Test
	fun should_findAllGuestFromVm_Vm() {
		log.debug("should_findAllGuestFromVm_Vm ... ")
		assertThat(service, `is`(not(nullValue())))
	}

	/**
	 * [should_findAllPermissionsFromVm_Vm]
	 * [ItVmService.findAllPermissionsFromVm]에 대한 단위테스트
	 *
	 * @see ItVmService.findAllPermissionsFromVm
	 */
	@Test
	fun should_findAllPermissionsFromVm_Vm() {
		log.debug("should_findAllPermissionsFromVm_Vm ... ")
		assertThat(service, `is`(not(nullValue())))
	}

	/**
	 * [should_findAllEventsFromVm_Vm]
	 * [ItVmService.findAllEventsFromVm]에 대한 단위테스트
	 *
	 * @see ItVmService.findAllEventsFromVm
	 */
	@Test
	fun should_findAllEventsFromVm_Vm() {
		log.debug("should_findAllEventsFromVm_Vm ... ")
		assertThat(service, `is`(not(nullValue())))
	}

	/**
	 * [should_findConsole_Vm]
	 * [ItVmService.findConsole]에 대한 단위테스트
	 *
	 * @see ItVmService.findConsole
	 */
	@Test
	fun should_findConsole_Vm() {
		log.debug("should_findConsole_Vm ... ")
		assertThat(service, `is`(not(nullValue())))
	}
		

	companion object {
		private val log by LoggerDelegate()
	}
}