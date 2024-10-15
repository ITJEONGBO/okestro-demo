package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.*
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.types.Template
import org.ovirt.engine.sdk4.types.Disk
import org.ovirt.engine.sdk4.types.Vm
import org.ovirt.engine.sdk4.types.Nic
import org.ovirt.engine.sdk4.types.DiskAttachment
import org.ovirt.engine.sdk4.types.Event
import org.ovirt.engine.sdk4.types.Permission
import org.ovirt.engine.sdk4.types.StorageDomain
import org.springframework.stereotype.Service

interface ItTemplateService {
	/**
	 * [ItTemplateService.findAll]
	 * 템플릿 목록
	 *
	 * @return 템플릿 목록
	 */
	@Throws(Error::class)
	fun findAll(): List<TemplateVo>
	/**
	 * [ItTemplateService.findOne]
	 * 템플릿 정보
	 * @param templateId [String] 템플릿 id
	 * @return [TemplateVo]
	 */
	@Throws(Error::class)
	fun findOne(templateId: String): TemplateVo?

	// 템플릿 생성창 - 클러스터 목록 [ItClusterService.findAll]
	// 템플릿 생성창 - cpuProfile 목록 []

	/**
	 * [ItTemplateService.findAllDiskAttachmentsFromVm]
	 * 디스크 목록(가상머신) [ItVmDiskService.findAllFromVm] 약간 다름
	 *
	 * @param vmId [String] 가상머신 Id
	 * @return List<[DiskAttachmentVo]>
	 */
	@Throws(Error::class)
	fun findAllDiskAttachmentsFromVm(vmId: String): List<DiskAttachmentVo>

	// 템플릿 생성창
	// 		스토리지 목록(가상머신) [ItStorageService.findAllFromDataCenter]
	// 		디스크 프로파일 목록(가상머신) [ItStorageService.findAllDiskProfilesFromStorageDomain]

	/**
	 * [ItTemplateService.add]
	 * 템플릿 생성 (Vm Status != up)
	 * 템플릿 디스크 붙이는 작업 (if, 도메인 용량이 초과 되었을 때, ovirt에서 알아서 다른 도메인을 설정해주는지는 알 수 없음)
	 *
	 * @param vmId [String] TemplateVo 내부에  vm이 있어서 이걸로 id값을 가져와도 될거같음
	 * @param templateVo [TemplateVo]
	 * @return [TemplateVo]?
	 */
	@Throws(Error::class)
	fun add(vmId: String, templateVo: TemplateVo): TemplateVo?
	/**
	 * [ItTemplateService.update]
	 * 템플릿 편집
	 *
	 * @param templateVo [TemplateVo] 템플릿 객체
	 * @return [TemplateVo]?
	 */
	@Throws(Error::class)
	fun update(templateVo: TemplateVo): TemplateVo?
	/**
	 * [ItTemplateService.remove]
	 * 템플릿 삭제
	 *
	 * @param templateId [String] 템플릿 id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun remove(templateId: String): Boolean

	/**
	 * [ItTemplateService.findAllVmsFromTemplate]
	 * 템플릿 가상머신 목록
	 * 가상머신에서 생성시 템플릿 선택하면 나오는 항목같음 => 아님 이거 뭐임?
	 *
	 * @param templateId [String] 템플릿 id
	 * @return List<[VmVo]>
	 */
	@Throws(Error::class)
	fun findAllVmsFromTemplate(templateId: String): List<VmVo>
	/**
	 * [ItTemplateService.findAllNicsFromTemplate]
	 * 템플릿 네트워크 인터페이스 목록
	 *
	 * @param templateId [String] 템플릿 id
	 * @return 네트워크 인터페이스 목록
	 */
	fun findAllNicsFromTemplate(templateId: String): List<NicVo>
	// TODO NIC 생성, 편집, 삭제 테스트 안해봄
	/**
	 * [ItTemplateService.addNicFromTemplate]
	 * 템플릿 nic 생성
	 *
	 * @param templateId [String] 템플릿 아이디
	 * @param nicVo [NicVo]
	 * @return [NicVo]
	 */
	fun addNicFromTemplate(templateId: String, nicVo: NicVo): NicVo?
	/**
	 * [ItTemplateService.updateNicFromTemplate]
	 * 템플릿 nic 편집
	 *
	 * @param templateId [String] 템플릿 아이디
	 * @param nicVo [NicVo] 템플릿 아이디
	 * @return [NicVo]
	 */
	fun updateNicFromTemplate(templateId: String, nicVo: NicVo): NicVo?
	/**
	 * [ItTemplateService.removeNicFromTemplate]
	 * 템플릿 nic 제거
	 *
	 * @param templateId [String] 템플릿 아이디
	 * @param nicId [String] nic 아이디
	 * @return
	 */
	@Throws(Error::class)
	fun removeNicFromTemplate(templateId: String, nicId: String): Boolean
	/**
	 * 템플릿 디스크 목록
	 * @param templateId [String] 템플릿 id
	 * @return List<[DiskAttachmentVo]> 디스크 목록
	 */
	@Throws(Error::class)
	fun findAllDisksFromTemplate(templateId: String): List<DiskAttachmentVo>
	/**
	 * [ItTemplateService.findAllStorageDomainsFromTemplate]
	 * 템플릿 스토리지 도메인 목록
	 *
	 * @param templateId [String] 템플릿 id
	 * @return List<[StorageDomainVo]> 스토리지 도메인 목록
	 */
	@Throws(Error::class)
	fun findAllStorageDomainsFromTemplate(templateId: String): List<StorageDomainVo>

	 /**
	  * [ItTemplateService.findAllEventsFromTemplate]
	  * 템플릿 이벤트 목록
	  *
	  * @param templateId [String] 템플릿 id
	  * @return List<[EventVo]> 이벤트 목록
	  */
	 @Throws(Error::class)
	fun findAllEventsFromTemplate(templateId: String): List<EventVo>
	/**
	 * [ItTemplateService.findAllPermissionsFromTemplate]
	 * 템플릿 권한 목록
	 *
	 * @param templateId [String] 템플릿 id
	 * @return 권한 목록
	 */
	@Deprecated("사용안함")
	@Throws(Error::class)
	fun findAllPermissionsFromTemplate(templateId: String): List<PermissionVo>
}

@Service
class TemplateServiceImpl(
): BaseService(), ItTemplateService {

	@Throws(Error::class)
	override fun findAll(): List<TemplateVo> {
		log.info("findAll ... ")
		val res: List<Template> =
			conn.findAllTemplates().getOrDefault(listOf())
		return res.toTemplatesMenu(conn)
	}

	@Throws(Error::class)
	override fun findOne(templateId: String): TemplateVo? {
		log.info("findOne ... templateId: {}", templateId)
		val res: Template? =
			conn.findTemplate(templateId).getOrNull()
		return res?.toTemplateInfo(conn)
	}

	@Throws(Error::class)
	override fun findAllDiskAttachmentsFromVm(vmId: String): List<DiskAttachmentVo> {
		log.info("findAllDisksFromVm ... vmId: {}", vmId)
		val res: List<DiskAttachment> =
			conn.findAllDiskAttachmentsFromVm(vmId).getOrDefault(listOf())

		return res.toDiskAttachmentsToTemplate(conn)
	}

	@Throws(Error::class)
	override fun add(vmId: String, templateVo: TemplateVo): TemplateVo? {
		log.info("add ... ")
		val res: Template? =
			conn.addTemplate(
				vmId,
				templateVo.toAddTemplateBuilder()
			).getOrNull()
		return res?.toTemplateInfo(conn)
	}

	@Throws(Error::class)
	override fun update(templateVo: TemplateVo): TemplateVo? {
		log.info("update ... templateId: {}", templateVo.id)
		val res: Template? =
			conn.updateTemplate(
				templateVo.id,
				templateVo.toEditTemplateBuilder()
			).getOrNull()
		return res?.toTemplateInfo(conn)
	}

	@Throws(Error::class)
	override fun remove(templateId: String): Boolean {
		log.info("remove ... templateId: {}", templateId)
		val res: Result<Boolean> =
			conn.removeTemplate(templateId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun findAllVmsFromTemplate(templateId: String): List<VmVo> {
		log.info("findAllVmsFromTemplate ... templateId: {}", templateId)
		val vms: List<Vm> =
			conn.findAllVms().getOrDefault(listOf())
				.filter {
					it.templatePresent() && it.template().id() == templateId
				}
		return vms.toVmVos(conn)
	}

	@Throws(Error::class)
	override fun findAllNicsFromTemplate(templateId: String): List<NicVo> {
		log.info("findAllNicsFromTemplate ... templateId: {}", templateId)
		val res: List<Nic> =
			conn.findAllNicsFromTemplate(templateId).getOrDefault(listOf())
		return res.toNicVosFromTemplate(conn)
	}

	@Throws(Error::class)
	override fun addNicFromTemplate(templateId: String, nicVo: NicVo): NicVo? {
		log.info("addNicFromTemplate ... templateId: {}", templateId)
		val res: Nic? =
			conn.addNicFromTemplate(
				templateId,
				nicVo.toAddNicBuilder()
			).getOrNull()
		return res?.toNicVoFromTemplate(conn)
	}

	@Throws(Error::class)
	override fun updateNicFromTemplate(templateId: String, nicVo: NicVo): NicVo? {
		log.info("updateNicFromTemplate ... templateId: {}", templateId)
		val res: Nic? =
			conn.updateNicFromTemplate(
				templateId,
				nicVo.toEditNicBuilder()
			).getOrNull()
		return res?.toNicVoFromTemplate(conn)
	}

	@Throws(Error::class)
	override fun removeNicFromTemplate(templateId: String, nicId: String): Boolean {
		log.info("removeNicFromTemplate ... templateId: {}", templateId)
		val res: Result<Boolean> =
			conn.removeNicFromTemplate(templateId, nicId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun findAllDisksFromTemplate(templateId: String): List<DiskAttachmentVo> {
		log.info("findAllDisksFromTemplate ... templateId: {}", templateId)
		val res: List<DiskAttachment> =
			conn.findAllDiskAttachmentsFromTemplate(templateId).getOrDefault(listOf())
				.filter { it.diskPresent() }
		return res.toDiskAttachmentsToTemplate(conn)
	}

	@Throws(Error::class)
	override fun findAllStorageDomainsFromTemplate(templateId: String): List<StorageDomainVo> {
		log.info("findAllStorageDomainsFromTemplate ... ")
		val res: List<StorageDomain> =
			conn.findAllDiskAttachmentsFromTemplate(templateId)
				.getOrDefault(listOf())
				.mapNotNull { diskAttachment ->
					val disk: Disk? =
						conn.findDisk(diskAttachment.disk().id()).getOrNull()

					disk?.storageDomains()?.first()?.let {
						conn.findStorageDomain(it.id()).getOrNull()
					}
				}
				.distinctBy { it.id() } // ID를 기준으로 중복 제거
		return res.toStorageDomainSizes()
	}

	@Throws(Error::class)
	override fun findAllEventsFromTemplate(templateId: String): List<EventVo> {
		log.info("findAllEventsFromTemplate ... ")
		val template: Template? =
			conn.findTemplate(templateId).getOrNull()
		val res: List<Event> =
			conn.findAllEvents().getOrDefault(listOf())
				.filter { it.templatePresent() && it.template().name() == template?.name() }
		return res.toEventVos()
	}


	@Deprecated("사용안함")
	@Throws(Error::class)
	override fun findAllPermissionsFromTemplate(templateId: String): List<PermissionVo> {
		log.info("findAllPermissionsFromTemplate ... ")
		val res: List<Permission> =
			conn.findAllPermissionsFromCluster(templateId).getOrDefault(listOf())
		return res.toPermissionVos(conn)
	}

	companion object {
		private val log by LoggerDelegate()
	}

}