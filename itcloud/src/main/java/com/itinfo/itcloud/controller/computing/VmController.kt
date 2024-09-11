package com.itinfo.itcloud.controller.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.controller.BaseController
import com.itinfo.itcloud.error.toException
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.NicVo
import com.itinfo.itcloud.model.network.VnicProfileVo
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.storage.DiskAttachmentVo
import com.itinfo.itcloud.service.computing.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@Api(tags = ["Computing", "Vm"])
@RequestMapping("/api/v1/computing/vms")
class VmController: BaseController() {
	@Autowired private lateinit var iVm: ItVmService
	@Autowired private lateinit var idiskVm: ItVmDiskService

	@ApiOperation(
		value="가상머신 목록",
		notes="전체 가상머신 목록을 조회한다"
	)
	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun vms(): ResponseEntity<List<VmVo>> {
		log.info("--- 가상머신 목록")
		return ResponseEntity.ok(iVm.findAll())
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 생성창",
		notes="가상머신 생성시 필요한 내용을 조회한다"
	)
	@GetMapping("/settings/vnic")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun setVnic(): ResponseEntity<List<VnicProfileVo>> {
		log.info("--- 가상머신 생성 창 - vnic")
//		return vmService.setVnic();
		return ResponseEntity.ok(listOf())
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 생성",
		notes="가상머신을 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vm", value="가상머신", dataTypeClass=VmVo::class, paramType="body"),
	)
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun add(
		@RequestBody vm: VmVo? = null,
	): ResponseEntity<VmVo?> {
		if (vm == null)
			throw ErrorPattern.VM_VO_INVALID.toException()
		log.info("--- 가상머신 생성")
		return ResponseEntity.ok(iVm.add(vm))
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 수정창",
		notes="선택된 가상머신의 정보를 조회한다")
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{vmId}/edit")
	@ResponseBody
	@ResponseStatus(
		HttpStatus.OK
	)
	fun getVmCreate(
		@PathVariable vmId: String? = null,
	): ResponseEntity<VmVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("--- 가상머신 편집 창")
		return ResponseEntity.ok(iVm.findOne(vmId))
	}

	@ApiOperation(
		httpMethod="PUT",
		value="가상머신 수정",
		notes="가상머신을 수정한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="vm", value="가상머신", dataTypeClass=VmVo::class, paramType="body"),
	)
	@PutMapping("/{vmId}")
	@ResponseBody
	@ResponseStatus(
		HttpStatus.CREATED
	)
	fun update(
		@PathVariable vmId: String?,
		@RequestBody vm: VmVo?
	): ResponseEntity<VmVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (vm == null)
			throw ErrorPattern.VM_VO_INVALID.toException()
		log.info("--- 가상머신 편집")
		return ResponseEntity.ok(iVm.update(vm))
	}

	@ApiOperation(
		httpMethod="DELETE",
		value="가상머신 삭제",
		notes="가상머신을 삭제한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@DeleteMapping("/{vmId}")
	@ResponseBody
	@ResponseStatus(
		HttpStatus.OK
	)
	fun remove(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iVm.remove(vmId, true))
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 상세정보",
		notes="가상머신의 상세정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{vmId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun vm(
		@PathVariable vmId: String? = null,
	): ResponseEntity<VmVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iVm.findOne(vmId))
	}


	@Autowired private lateinit var iVmDisk: ItVmDiskService
	@GetMapping("/{vmId}/disks")
	@ApiOperation(
		httpMethod="GET",
		value="가상머신 디스크 목록",
		notes="선택된 가상머신의 디스크 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun disk(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<DiskAttachmentVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("----- vm disk 일반 불러오기: $vmId")
		return ResponseEntity.ok(iVmDisk.findAllDisksFromVm(vmId))
	}


	@Autowired private lateinit var iVmSnapshot: ItVmSnapshotService
	@ApiOperation(
		httpMethod="GET",
		value="가상머신 스냅샷 목록",
		notes="선택된 가상머신의 스냅샷 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{vmId}/snapshots")
	@ResponseBody
	fun snapshot(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<SnapshotVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("----- vm snapshot 불러오기: $vmId")
		return ResponseEntity.ok(iVmSnapshot.findAllSnapshotsFromVm(vmId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 내 어플리케이션 목록",
		notes="선택된 가상머신 내 어플리케이션 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{vmId}/applications")
	@ResponseBody
	fun app(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<IdentifiedVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("----- vm app 불러오기: $vmId")
		return ResponseEntity.ok(iVm.findAllApplicationsFromVm(vmId))
	}
	//region: affinity


	@Autowired private lateinit var iAffinity: ItAffinityService
	@ApiOperation(
		httpMethod="GET",
		value="가상머신의 선호도그룹 목록",
		notes="선택된 가상머신의 선호도그룹 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{vmId}/affinitygroups")
	@ResponseBody
	fun findAllGroupsForVm(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<AffinityGroupVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("----- vm affGroup 불러오기: $vmId")
		return ResponseEntity.ok(iAffinity.findAllGroupsForVm(vmId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신의 선호도레이블 목록",
		notes="선택된 가상머신의 선호도레이블 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{vmId}/affinitylabels")
	@ResponseBody
	fun findAllLabelsForVm(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<AffinityLabelVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("----- vm affLabel 불러오기: {}", vmId)
		return ResponseEntity.ok(iAffinity.findAllLabelsForVm(vmId))
	}
	//endregion

	@ApiOperation(
		value="가상머신 게스트 목록",
		notes="선택된 가상머신의 게스트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{vmId}/guests")
	@ResponseBody
	fun guest(
		@PathVariable vmId: String? = null,
	): ResponseEntity<GuestInfoVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("----- vm disk 일반 불러오기: {}", vmId)
		return ResponseEntity.ok(iVm.findAllGuestFromVm(vmId))
	}

	@GetMapping("/{vmId}/permissions")
	@ApiOperation(
		httpMethod="GET",
		value="가상머신 권한 목록",
		notes="선택된 가상머신의 권한 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ResponseBody
	fun permission(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<PermissionVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("----- vm event 일반 불러오기: {}", vmId)
		return ResponseEntity.ok(iVm.findAllPermissionsFromVm(vmId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 이벤트 목록",
		notes="선택된 가상머신의 이벤트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{vmId}/events")
	@ResponseBody
	fun event(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<EventVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("----- vm event 일반 불러오기: $vmId")
		return ResponseEntity.ok(iVm.findAllEventsFromVm(vmId))
	}


	@ApiOperation(
		value="가상머신 콘솔",
		notes="선택된 가상머신의 콘솔을 출력한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		// ApiImplicitParam(name="console", value="콘솔", dataTypeClass=ConsoleVo::class, paramType="body"),
	)
	@PostMapping("/{vmId}/console")
	@ResponseStatus(HttpStatus.OK)
	fun console(
		@PathVariable vmId: String? = null,
		// @RequestBody console: ConsoleVo? = null,
	): ResponseEntity<ConsoleVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
//		if (console == null)
//			throw ErrorPattern.CONSOLE_VO_INVALID.toException()
		log.info("--- 가상머신 콘솔")
		return ResponseEntity.ok(iVm.findConsole(vmId))
	}

	@GetMapping("/console/vncView")
	fun vncView(): String {
		return "vnc"
	}

	//region: vmOp
	@Autowired private lateinit var iVmOp: ItVmOperationService
	@ApiOperation(
		httpMethod="POST",
		value="가상머신 시작",
		notes="가상머신을 시작한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	fun start(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iVmOp.start(vmId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 일시정지",
		notes="가상머신을 일시정지한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	fun pause(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iVmOp.pause(vmId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 전원끔",
		notes="가상머신의 전원을 끈다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	fun powerOff(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iVmOp.powerOff(vmId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 종료",
		notes="가상머신을 종료한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	fun shutdown(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iVmOp.shutdown(vmId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 재부팅",
		notes="가상머신을 재부팅한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	fun reboot(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iVmOp.reboot(vmId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 재설정",
		notes="가상머신을 재설정한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	fun reset(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iVmOp.reset(vmId))
	}
	//endregion

	@Autowired private lateinit var vmNic: ItVmNicService

	@ApiOperation(value = "가상머신 네트워크 인터페이스 목록", notes = "선택된 가상머신의 네트워크 인터페이스 목록을 조회한다")
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{vmId}/nics")
	@ResponseBody
	fun findAllNicsFromVm(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<NicVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("----- vm nic 일반 불러오기: $vmId")
		return ResponseEntity.ok(vmNic.findAllNicsFromVm(vmId))
	}

	@ApiOperation(value = "가상머신 네트워크 인터페이스 목록", notes = "선택된 가상머신의 네트워크 인터페이스 목록을 조회한다")
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{vmId}/nics/{nicId}")
	@ResponseBody
	fun findNicFromVm(
		@PathVariable vmId: String? = null,
		@PathVariable nicId: String? = null,
	): ResponseEntity<NicVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (nicId.isNullOrEmpty())
			throw ErrorPattern.NIC_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(vmNic.findOneNicFromVm(vmId, nicId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 네트워크 인터페이스 생성",
		notes="선택된 가상머신의 네트워크 인터페이스를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="nic", value="네트워크 인터페이스 컨트롤러", dataTypeClass=NicVo::class, paramType="body"),
	)
	@PostMapping("/{vmId}/nics/{nicId}")
	@ResponseBody
	fun addNicFromVm(
		@PathVariable vmId: String? = null,
		@PathVariable nic: NicVo? = null,
	): ResponseEntity<NicVo?> {
		if (vmId.isNullOrEmpty())   throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (nic == null)            throw ErrorPattern.NIC_VO_INVALID.toException()
		return ResponseEntity.ok(vmNic.addFromVm(vmId, nic))
	}

	@ApiOperation(
		httpMethod="PUT",
		value="가상머신 네트워크 인터페이스 편집",
		notes="선택된 가상머신의 네트워크 인터페이스를 편집한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="nicId", value="네트워크 인터페이스 컨트롤러 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="nic", value="네트워크 인터페이스 컨트롤러", dataTypeClass=NicVo::class, paramType="body"),
	)
	@PutMapping("/{vmId}/nics/{nicId}")
	@ResponseBody
	fun updateNicFromVm(
		@PathVariable vmId: String? = null,
		@PathVariable nicId: String? = null,
		@PathVariable nic: NicVo? = null,
	): ResponseEntity<NicVo?> {
		if (vmId.isNullOrEmpty())   throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (nicId.isNullOrEmpty())  throw ErrorPattern.NIC_ID_NOT_FOUND.toException()
		if (nic == null)            throw ErrorPattern.NIC_VO_INVALID.toException()
		return ResponseEntity.ok(vmNic.updateFromVm(vmId, nic))
	}

	@ApiOperation(
		httpMethod="DELETE",
		value="가상머신 네트워크 인터페이스 삭제",
		notes="선택된 가상머신의 네트워크 인터페이스를 삭제한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="nicId", value="네트워크 인터페이스 컨트롤러 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@DeleteMapping("/{vmId}/nics/{nicId}")
	@ResponseBody
	fun removeNicFromVm(
		@PathVariable vmId: String? = null,
		@PathVariable nicId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())   throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (nicId.isNullOrEmpty())  throw ErrorPattern.NIC_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(vmNic.removeFromVm(vmId, nicId))
	}

	companion object {
		private val log by LoggerDelegate()
	}
}