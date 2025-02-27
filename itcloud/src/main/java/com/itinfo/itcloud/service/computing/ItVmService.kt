package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.*
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.repository.engine.DiskVmElementRepository
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.types.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.Error


interface ItVmService {
	/**
	 * [ItVmService.findAll]
	 * 가상머신 목록
	 *
	 * @return List<[VmVo]> 가상머신 목록
	 */
	@Throws(Error::class)
	fun findAll(): List<VmVo>
	/**
	 * [ItVmService.findOne]
	 * 가상머신 정보, 편집
	 *
	 * @param vmId [String] 가상머신 Id
	 * @return [VmVo]
	 */
	@Throws(Error::class)
	fun findOne(vmId: String): VmVo?

	/**
	 * [ItVmService.add]
	 * 가상머신 생성
	 * TODO 템플릿 선택하면 인스턴스 이미지 선택 불가
	 *
	 * @param vmVo [VmVo]
	 * @return [VmVo]
	 */
	@Throws(Error::class)
	fun add(vmVo: VmVo): VmVo?
	/**
	 * [ItVmService.update]
	 * 가상머신 편집
	 *
	 * @param vmVo [VmVo]
	 * @return [VmVo]
	 */
	@Throws(Error::class)
	fun update(vmVo: VmVo): VmVo?
	/**
	 * [ItVmService.remove]
	 * 가상머신 삭제
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskDelete [Boolean] disk 삭제여부, disk가 true면 디스크 삭제하라는 말
	 * @return [Boolean]
	 * detachOnly => true==가상머신만 삭제/ false==가상머신+디스크 삭제
	 */
	@Throws(Error::class)
	fun remove(vmId: String, diskDelete: Boolean): Boolean

	/**
	 * [ItVmService.findAllApplicationsFromVm]
	 * 가상머신 어플리케이션
	 *
	 * @param vmId [String] 가상머신 Id
	 */
	@Throws(Error::class)
	fun findAllApplicationsFromVm(vmId: String): List<IdentifiedVo>
	/**
	 * [ItVmService.findAllHostDevicesFromVm]
	 * 가상머신 호스트 장치
	 *
	 * @param vmId [String] 가상머신 Id
	 */
	@Throws(Error::class)
	fun findAllHostDevicesFromVm(vmId: String): List<HostDeviceVo>
	/**
	 * [ItVmService.findAllEventsFromVm]
	 * 가상머신 이벤트
	 *
	 * @param vmId [String] 가상머신 Id
	 */
	@Throws(Error::class)
	fun findAllEventsFromVm(vmId: String): List<EventVo>

	/**
	 * [ItVmService.findGuestFromVm]
	 * 가상머신 게스트 정보
	 *
	 * @param vmId [String] 가상머신 Id
	 */
	@Deprecated("필요없음")
	@Throws(Error::class)
	fun findGuestFromVm(vmId: String): GuestInfoVo?
	/**
	 * [ItVmService.findAllPermissionsFromVm]
	 * 가상머신 권한
	 *
	 * @param vmId [String] 가상머신 Id
	 */
	@Deprecated("필요없음")
	@Throws(Error::class)
	fun findAllPermissionsFromVm(vmId: String): List<PermissionVo>
}

@Service
class VmServiceImpl(

) : BaseService(), ItVmService {

	@Throws(Error::class)
	override fun findAll(): List<VmVo> {
		log.info("findAll ... ")
		val res: List<Vm> = conn.findVms()
		return res.toVmsMenu(conn)
	}

	@Throws(Error::class)
	override fun findOne(vmId: String): VmVo? {
		log.info("findOne ... vmId : {}", vmId)
		val res: Vm? = conn.findVm(vmId).getOrNull()
		return res?.toVmVo(conn)
	}


	@Throws(Error::class)
	override fun add(vmVo: VmVo): VmVo? {
		log.info("add ... ")
//		if(vmVo.diskAttachmentVos.filter { it.bootable }.size != 1){
//			log.error("디스크 부팅가능은 한개만 가능")
//			throw ErrorPattern.VM_VO_INVALID.toException()
//		}

		val res: Vm? = conn.addVm(
			vmVo.toAddVmBuilder(),
			vmVo.diskAttachmentVos.takeIf { it.isNotEmpty() }?.toAddDiskAttachmentList(),
			vmVo.vnicProfileVos.map { it.id }.takeIf { it.isNotEmpty() }, // NIC가 있는 경우만 전달
			vmVo.connVo.id.takeIf { it.isNotEmpty() }  // ISO 설정이 있는 경우만 전달
		).getOrNull()
		return res?.toVmVo(conn)
	}

	//TODO
	@Throws(Error::class)
	override fun update(vmVo: VmVo): VmVo? {
		log.info("update ... {}", vmVo.name)
		// 나중에 조건풀기
//		if(vmVo.diskAttachmentVos.filter { it.bootable }.size != 1){
//			log.error("디스크 부팅가능은 한개만 가능")
//			throw ErrorPattern.VM_VO_INVALID.toException()
//		}

		val existDiskAttachments: List<DiskAttachment> = conn.findAllDiskAttachmentsFromVm(vmVo.id)
			.getOrDefault(listOf())

		val diskAttachmentListToAdd = mutableListOf<DiskAttachment>()
		val diskAttachmentListToDelete = mutableListOf<DiskAttachment>()

		vmVo.diskAttachmentVos.forEach { diskAttachmentVo ->
			if (existDiskAttachments.none { it.id() == diskAttachmentVo.id }) {
				diskAttachmentListToAdd.add(diskAttachmentVo.toAddDiskAttachment())
			}
		}
		existDiskAttachments.forEach { existingDisk ->
			if (vmVo.diskAttachmentVos.none { it.id == existingDisk.id() }) {
				// 기존 디스크가 새 목록에 없으면 삭제할 목록에 넣음
				diskAttachmentListToDelete.add(existingDisk)
			}
		}

		val res: Vm? = conn.updateVm(
			vmVo.toEditVmBuilder(),
			diskAttachmentListToAdd,
			diskAttachmentListToDelete,
//				vmVo.diskAttachmentVos.toEditDiskAttachmentList(conn, vmVo.id),
			vmVo.vnicProfileVos.map { it.id },
			vmVo.connVo?.id
		).getOrNull()

		return res?.toVmVo(conn)
	}

	// diskDelete가 detachOnly
	// diskDelete가 false 면 디스크는 삭제 안함, true면 삭제
	@Throws(Error::class)
	override fun remove(vmId: String, diskDelete: Boolean): Boolean {
		log.info("remove ...  vmId: {}", vmId)
		val res: Result<Boolean> = conn.removeVm(vmId, diskDelete)
		return res.isSuccess
	}


	@Throws(Error::class)
	override fun findAllApplicationsFromVm(vmId: String): List<IdentifiedVo> {
		log.info("findAllApplicationsFromVm ... vmId: {}", vmId)
		val res: List<Application> = conn.findAllApplicationsFromVm(vmId)
			.getOrDefault(listOf())
		return res.fromApplicationsToIdentifiedVos()
	}

	@Throws(Error::class)
	override fun findAllHostDevicesFromVm(vmId: String): List<HostDeviceVo> {
		log.info("findAllHostDevicesFromVm ... vmId: {}", vmId)
		val res: List<HostDevice> = conn.findAllHostDevicesFromVm(vmId)
			.getOrDefault(listOf())
		return res.toHostDeviceVos()
	}

	@Throws(Error::class)
	override fun findAllEventsFromVm(vmId: String): List<EventVo> {
		log.info("findAllEventsFromVm ... vmId: {}", vmId)
		val vm: Vm = conn.findVm(vmId)
			.getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toException()
		val res: List<Event> = conn.findAllEvents()
			.getOrDefault(listOf())
			.filter { it.vmPresent() && it.vm().name() == vm.name() }
		return res.toEventVos()
	}

	@Deprecated("필요없음")
	@Throws(Error::class)
	override fun findGuestFromVm(vmId: String): GuestInfoVo? {
		log.info("findGuestFromVm ... vmId: {}", vmId)
		val res: Vm = conn.findVm(vmId)
			.getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toException()
		if (!res.guestOperatingSystemPresent()) {
			log.warn("게스트 운영 체제 정보가 없습니다.")
			return null
		}
		return res.toGuestInfoVo()
	}

	@Deprecated("필요없음")
	@Throws(Error::class)
	override fun findAllPermissionsFromVm(vmId: String): List<PermissionVo> {
		log.info("findAllPermissionsFromVm ... vmId: {}", vmId)
		val res: List<Permission> = conn.findAllAssignedPermissionsFromVm(vmId).getOrDefault(listOf())
		return res.toPermissionVos(conn)
	}


	companion object {
		private val log by LoggerDelegate()
	}
}