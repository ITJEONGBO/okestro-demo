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
import org.ovirt.engine.sdk4.types.BiosType
import org.ovirt.engine.sdk4.types.InheritableBoolean
import org.ovirt.engine.sdk4.types.VmType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigInteger
import java.util.Arrays

/**
 * [ItVmServiceTest]
 * [ItVmService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.03.05
 */
@SpringBootTest
class  ItVmServiceTest {
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
		host01 = "722096d3-4cb2-43b0-bf41-dd69c3a70779"
		host02 = "789b78c4-3fcf-4f19-9b69-d382aa66c12f"
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
		assertThat(result.size, `is`(5))

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
	 * [ItVmService.findAllDiskImage]에 대한 단위테스트
	 * TODO 디스크 목록기준이 애매함
	 * @see [ItVmService.findAllDiskImage]
	 */
	@Test
	fun should_findAllDiskImagesBy() {
		log.debug("should_findAllDiskImagesBy ...")
		val result: List<DiskImageVo> =
			service.findAllDiskImage()

		assertThat(result, `is`(not(nullValue())))

		result.forEach { println(it) }
		assertThat(result.size, `is`(7))
	}

	/**
	 * [should_findAllVnicProfilesFromCluster]
	 * [ItVmService.findAllVnicProfilesFromCluster]에 대한 단위테스트
	 *
	 * @see [ItVmService.findAllVnicProfilesFromCluster]
	 */
	@Test
	fun should_findAllVnicProfilesFromCluster() {
		log.debug("should_findAllVnicProfilesFromCluster ...")
		val result: List<VnicProfileVo> =
			service.findAllVnicProfilesFromCluster(clusterId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(5))
	}

	/**
	 * [should_findAllIso]
	 * [ItVmService.findAllISO]에 대한 단위테스트
	 * @see [ItVmService.findAllISO]
	 */
	@Test
	fun should_findAllIso() {
		log.debug("should_findAllISOsBy ...")
		val result: List<IdentifiedVo> =
			service.findAllISO()

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(2))

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
		log.debug("should_add_Vm ... {}", clusterId)
		val addVm: VmVo = VmVo.builder {
			clusterVo {
				IdentifiedVo.builder {
					id { clusterId }
					name { "Default" }
				}
			}
			templateVo {
				IdentifiedVo.builder {
					id { "00000000-0000-0000-0000-000000000000" }
					name { "Blank" }
				}
			}
			osSystem { "other_linux" }
			chipsetFirmwareType { "Q35_SEA_BIOS" }  // String.valueOf(BiosType.Q35_OVMF }
			optimizeOption { "SERVER" }  // String.valueOf(VmType.SERVER
			name { "random1" }
			description { "" }
			comment { "" }
			stateless { false }
			startPaused { false }
			deleteProtected { false }
			vnicProfileVos {
				Arrays.asList(
					VnicProfileVo.builder { id { "0000000a-000a-000a-000a-000000000398" } },
					VnicProfileVo.builder { id { "86106902-bf8b-4637-95d7-8cf5aca28fc5" } }
				)
			}
			diskAttachmentVos { null }
//			diskAttachmentVos {
//					Arrays.asList(
//						DiskImageVo.builder {
//							size { 200 }
//							alias { randomName + 1 }
//							description { "" }
//							interfaces { "VIRTIO_SCSI" } // 인터페이스
//							storageDomainId { } // hosted_storage
//							allocationPolicy { true } // 할당정책: 씬
//							diskProfile { 3ab66ac-26c3-4b21-ba78-691ec2a004df" }
//							wipeAfterDelete { false }
//							bootable { true } // 기본값:t
//							shareable { false }
//							readOnly { false } // 읽기전용
//							backup { true } // 증분백업 기본값 t
//							build { ) }
//						}
//					)
//				}
			instanceType { "none" } //tiny 안됨 ( none,small, medium, xlarge)
			memorySize { BigInteger.valueOf(2048) }
			memoryMax { BigInteger.valueOf(8192) }
			memoryActual { BigInteger.valueOf(2048) }
			memoryBalloon { true }
			cpuTopologySocket { 2 }
			cpuTopologyCore { 1 }
			cpuTopologyThread { 1 }
			timeOffset { "Asia/Seoul"}  // Asia/Seoul , Etc/GMT
			cloudInit { false }   // 일단 안됨
			hostInCluster { true }  // 특정 호스트(false)
			hostVos {
//				Arrays.asList(
//					IdentifiedVo.builder{ id { "" } }
//				)
				null
			}
			migrationEncrypt { InheritableBoolean.INHERIT }
			migrationMode {"MIGRATABLE" }  // 마이그레이션
			ha { false } // 기본 false
			priority { 1 }  // 우선순위: 기본 1(낮음)
			cpuProfileVo { IdentifiedVo.builder { id { "58ca604e-01a7-003f-01de-000000000250" } }  } // 클러스터 밑에 있는 cpu profile
			cpuShare  { 0 } // 비활성화됨 0
			cpuPinningPolicy  { "NONE" }
			memoryBalloon  { true }    // 메모리 balloon 활성화
			multiQue  { false } // 멀티 큐 사용
			virtSCSIEnable  { true }  // virtIO-SCSI 활성화
			firstDevice  { "HD" }
			connVo  { IdentifiedVo.builder { id { "4754b4fa-39c5-438d-81f1-d0defc08f7aa" } } }
		}

		val addResult: VmVo? =
			service.add(addVm)

		assertThat(addResult, `is`(not(nullValue())))
		assertThat(addResult?.id, `is`(not(nullValue())))
		assertThat(addResult?.name, `is`(addVm.name))
		assertThat(addResult?.clusterVo?.id, `is`(addVm.clusterVo.id))
		assertThat(addResult?.templateVo?.id, `is`(addVm.templateVo.id))
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
	 * [ItVmService.findAllApplicationsFromVm]에 대한 단위테스트
	 *
	 * @see ItVmService.findAllApplicationsFromVm
	 */
	@Test
	fun should_findAllApplicationsByVm() {
		log.debug("should_findAllApplicationsByVm ... ")
		val result: List<IdentifiedVo> =
			service.findAllApplicationsFromVm(hostVm)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(2))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllGuestFromVm]
	 * [ItVmService.findGuestFromVm]에 대한 단위테스트
	 *
	 * @see ItVmService.findGuestFromVm
	 */
	@Test
	fun should_findAllGuestFromVm() {
		log.debug("should_findAllGuestFromVm ... ")
		val result: GuestInfoVo? =
			service.findGuestFromVm(hostVm)

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
		assertThat(result.size, `is`(5))

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