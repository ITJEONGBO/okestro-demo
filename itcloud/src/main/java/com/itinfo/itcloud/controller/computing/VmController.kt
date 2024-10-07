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
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.model.storage.StorageDomainVo
import com.itinfo.itcloud.service.computing.*
import io.swagger.annotations.*
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

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 목록 조회",
		notes="전체 가상머신 목록을 조회한다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun vms(): ResponseEntity<List<VmVo>> {
		log.info("/computing/vms ... 가상머신 목록")
		return ResponseEntity.ok(iVm.findAll())
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 상세정보",
		notes="선택된 가상머신의 정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{vmId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun vm(
		@PathVariable vmId: String? = null,
	): ResponseEntity<VmVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{} ... 가상머신 상세정보", vmId)
		return ResponseEntity.ok(iVm.findOne(vmId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 생성창 - nic 목록",
		notes="가상머신 생성시에 필요한 vnicProfile 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/nic/{clusterId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun vnicProfiles(
		@PathVariable clusterId: String? = null,
	): ResponseEntity<List<VnicProfileVo>?> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("/computing/vms/nic/{} ... 가상머신 생성창 - nic 목록", clusterId)
		return ResponseEntity.ok(iVm.findAllVnicProfilesFromCluster(clusterId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 생성창 - 디스크 연결 목록",
		notes="가상머신 생성시에 필요한 디스크 연결 목록을 조회한다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/disks")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun disks(
	): ResponseEntity<List<DiskImageVo>?> {
		log.info("/computing/vms/disks ... 가상머신 생성창 - 디스크 연결 목록")
		return ResponseEntity.ok(iVm.findAllDiskImage())
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 생성창 - CD/DVD 연결할 ISO 목록",
		notes="가상머신 생성시에 필요한 CD/DVD 연결할 ISO 목록을 조회한다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/iso")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun iso(
	): ResponseEntity<List<IdentifiedVo>?> {
		log.info("/computing/vms/iso ... 가상머신 생성창 - CD/DVD 연결할 ISO 목록")
		return ResponseEntity.ok(iVm.findAllISO())
	}


	@ApiOperation(
		httpMethod="POST",
		value="가상머신 생성",
		notes="가상머신을 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vm", value="가상머신", dataTypeClass=VmVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun add(
		@RequestBody vm: VmVo? = null,
	): ResponseEntity<VmVo?> {
		if (vm == null)
			throw ErrorPattern.VM_VO_INVALID.toException()
		log.info("/computing/vms ... 가상머신 생성")
		return ResponseEntity.ok(iVm.add(vm))
	}

	@ApiOperation(
		httpMethod="PUT",
		value="가상머신 편집",
		notes="가상머신을 편집한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="vm", value="가상머신", dataTypeClass=VmVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@PutMapping("/{vmId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun update(
		@PathVariable vmId: String?,
		@RequestBody vm: VmVo?
	): ResponseEntity<VmVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (vm == null)
			throw ErrorPattern.VM_VO_INVALID.toException()
		log.info("/computing/vms/{} ... 가상머신 편집", vmId)
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
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@DeleteMapping("/{vmId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun remove(
		@PathVariable vmId: String? = null,
//		@PathVariable disk: Boolean? = null // disk 삭제여부
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{} ... 가상머신 삭제", vmId)
		return ResponseEntity.ok(iVm.remove(vmId, true))
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 내 어플리케이션 목록",
		notes="선택된 가상머신의 어플리케이션 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{vmId}/applications")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun applications(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<IdentifiedVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/applications ... 가상머신 어플리케이션 목록", vmId)
		return ResponseEntity.ok(iVm.findAllApplicationsFromVm(vmId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 이벤트 목록",
		notes="선택된 가상머신의 이벤트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{vmId}/events")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun events(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<EventVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/events ... 가상머신 이벤트", vmId)
		return ResponseEntity.ok(iVm.findAllEventsFromVm(vmId))
	}


	@Deprecated("필요없음")
	@ApiOperation(
		value="가상머신 게스트 목록",
		notes="선택된 가상머신의 게스트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{vmId}/guest")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun guest(
		@PathVariable vmId: String? = null,
	): ResponseEntity<GuestInfoVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/guest ... 가상머신 게스트", vmId)
		return ResponseEntity.ok(iVm.findGuestFromVm(vmId))
	}

	@Deprecated("필요없음")
	@ApiOperation(
		httpMethod="GET",
		value="가상머신 권한 목록",
		notes="선택된 가상머신의 권한 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{vmId}/permissions")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun permissions(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<PermissionVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/permissions ... 가상머신 권한", vmId)
		return ResponseEntity.ok(iVm.findAllPermissionsFromVm(vmId))
	}


	//region: vmOp
	@Autowired private lateinit var iVmOp: ItVmOperationService

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 실행",
		notes="선택된 가상머신을 실행한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/start")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun start(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/start ... 가상머신 시작", vmId)
		return ResponseEntity.ok(iVmOp.start(vmId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 일시정지",
		notes="선택된 가상머신을 일시정지한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/pause")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun pause(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/pause ... 가상머신 일시정지", vmId)
		return ResponseEntity.ok(iVmOp.pause(vmId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 전원끔",
		notes="선택된 가상머신의 전원을 끈다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/powerOff")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun powerOff(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/powerOff ... 가상머신 전원끄기", vmId)
		return ResponseEntity.ok(iVmOp.powerOff(vmId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 종료",
		notes="선택된 가상머신을 종료한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/shutdown")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun shutdown(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/shutdown ... 가상머신 종료", vmId)
		return ResponseEntity.ok(iVmOp.shutdown(vmId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 재부팅",
		notes="선택된 가상머신을 재부팅한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/reboot")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun reboot(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/reboot ... 가상머신 재부팅", vmId)
		return ResponseEntity.ok(iVmOp.reboot(vmId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 재설정",
		notes="선택된 가상머신을 재설정한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/reset")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun reset(
		@PathVariable vmId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/reset ... 가상머신 재설정", vmId)
		return ResponseEntity.ok(iVmOp.reset(vmId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 마이그레이션 호스트 목록",
		notes="선택된 가상머신의 마이그레이션 가능한 호스트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@PostMapping("/{vmId}/migrateHosts")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun migrateHosts(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<IdentifiedVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/migrateHosts ... 가상머신 호스트 목록 조회", vmId)
		return ResponseEntity.ok(iVmOp.migrateHostList(vmId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 마이그레이션",
		notes="선택된 가상머신을 재설정한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/migrate/{hostId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun reset(
		@PathVariable vmId: String? = null,
		@PathVariable hostId: String? = null
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/migrate ... 가상머신 재설정", vmId)
		return ResponseEntity.ok(iVmOp.migrate(vmId, hostId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 내보내기",
		notes="선택된 가상머신을 OVA로 내보낸다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, paramType="path"),
		ApiImplicitParam(name="vmExport", value="가상머신 내보내기", dataTypeClass=VmExportVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/export")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun exportOva(
		@PathVariable vmId: String? = null,
		@RequestBody vmExport: VmExportVo? = null,
	): ResponseEntity<Boolean?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (vmExport == null)
			throw ErrorPattern.VM_VO_INVALID.toException()
		log.info("/computing/vms/{}/export ... 가상머신 생성", vmId)
		return ResponseEntity.ok(iVmOp.exportOva(vmId, vmExport))
	}

	@ApiOperation(
		value="가상머신 콘솔",
		notes="선택된 가상머신의 콘솔을 출력한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		// ApiImplicitParam(name="console", value="콘솔", dataTypeClass=ConsoleVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/console")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun console(
		@PathVariable vmId: String? = null,
		// @RequestBody console: ConsoleVo? = null,
	): ResponseEntity<ConsoleVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
//		if (console == null)
//			throw ErrorPattern.CONSOLE_VO_INVALID.toException()
		log.info("/computing/vms/{}/console ... 가상머신 콘솔", vmId)
		return ResponseEntity.ok(iVmOp.findConsole(vmId))
	}


	//endregion


	//region: vmNic
	@Autowired private lateinit var vmNic: ItVmNicService

	@ApiOperation(
		httpMethod="GET",
		value = "가상머신 네트워크 인터페이스 목록",
		notes = "선택된 가상머신의 네트워크 인터페이스 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{vmId}/nics")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun nics(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<NicVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/nics ... 가상머신 nic 목록", vmId)
		return ResponseEntity.ok(vmNic.findAllNicsFromVm(vmId))
	}

	@ApiOperation(
		httpMethod="GET",
		value = "가상머신 네트워크 인터페이스",
		notes = "선택된 가상머신의 네트워크 인터페이스를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="nicId", value="네트워크 인터페이스 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{vmId}/nics/{nicId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun nic(
		@PathVariable vmId: String? = null,
		@PathVariable nicId: String? = null,
	): ResponseEntity<NicVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (nicId.isNullOrEmpty())
			throw ErrorPattern.NIC_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/nics/{} ... 가상머신 nic 일반 ", vmId, nicId)
		return ResponseEntity.ok(vmNic.findNicFromVm(vmId, nicId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 네트워크 인터페이스 생성",
		notes="선택된 가상머신의 네트워크 인터페이스를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="nic", value="네트워크 인터페이스", dataTypeClass=NicVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/nics")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun addNic(
		@PathVariable vmId: String? = null,
		@RequestBody nic: NicVo? = null,
	): ResponseEntity<NicVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (nic == null)
			throw ErrorPattern.NIC_VO_INVALID.toException()
		log.info("/computing/vms/{}/nics ... 가상머신 nic 생성 ", vmId)
		return ResponseEntity.ok(vmNic.addNicFromVm(vmId, nic))
	}

	@ApiOperation(
		httpMethod="PUT",
		value="가상머신 네트워크 인터페이스 편집",
		notes="선택된 가상머신의 네트워크 인터페이스를 편집한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="nicId", value="네트워크 인터페이스 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="nic", value="네트워크 인터페이스", dataTypeClass=NicVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@PutMapping("/{vmId}/nics/{nicId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun updateNic(
		@PathVariable vmId: String? = null,
		@PathVariable nicId: String? = null,
		@RequestBody nic: NicVo? = null,
	): ResponseEntity<NicVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (nicId.isNullOrEmpty())
			throw ErrorPattern.NIC_ID_NOT_FOUND.toException()
		if (nic == null)
			throw ErrorPattern.NIC_VO_INVALID.toException()
		log.info("/computing/vms/{}/nics/{} ... 가상머신 nic 편집 ", vmId, nicId)
		return ResponseEntity.ok(vmNic.updateNicFromVm(vmId, nic))
	}

	@ApiOperation(
		httpMethod="DELETE",
		value="가상머신 네트워크 인터페이스 삭제",
		notes="선택된 가상머신의 네트워크 인터페이스를 삭제한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="nicId", value="네트워크 인터페이스 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@DeleteMapping("/{vmId}/nics/{nicId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun removeNic(
		@PathVariable vmId: String? = null,
		@PathVariable nicId: String? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (nicId.isNullOrEmpty())
			throw ErrorPattern.NIC_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/nics/{} ... 가상머신 nic 삭제 ", vmId, nicId)
		return ResponseEntity.ok(vmNic.removeNicFromVm(vmId, nicId))
	}
	// endregion


	//region: vmDisk
	@Autowired private lateinit var iVmDisk: ItVmDiskService

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 디스크 목록",
		notes="선택된 가상머신의 디스크 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{vmId}/disks")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun disks(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<DiskAttachmentVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/disks ... 가상머신 disk 목록", vmId)
		return ResponseEntity.ok(iVmDisk.findAllDiskAttachmentsFromVm(vmId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 디스크",
		notes="선택된 가상머신의 디스크를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="diskAttachmentId", value="디스크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{vmId}/disks/{diskAttachmentId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun disk(
		@PathVariable vmId: String? = null,
		@PathVariable diskAttachmentId: String? = null,
	): ResponseEntity<DiskAttachmentVo> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (diskAttachmentId.isNullOrEmpty())
			throw ErrorPattern.DISK_ATTACHMENT_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/disks/{} ... 가상머신 disk 목록", vmId, diskAttachmentId)
		return ResponseEntity.ok(iVmDisk.findDiskFromVm(vmId, diskAttachmentId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 디스크 생성",
		notes="선택된 가상머신의 디스크를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="diskAttachment", value="디스크", dataTypeClass=DiskAttachmentVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/disks")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun addDisk(
		@PathVariable vmId: String? = null,
		@RequestBody diskAttachment: DiskAttachmentVo? = null,
	): ResponseEntity<DiskAttachmentVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (diskAttachment == null)
			throw ErrorPattern.DISK_ATTACHMENT_VO_INVALID.toException()
		log.info("/computing/vms/{}/disks ... 가상머신 디스크 생성 ", vmId)
		return ResponseEntity.ok(iVmDisk.addDiskFromVm(vmId, diskAttachment))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 디스크 연결",
		notes="선택된 가상머신의 디스크를 연결한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="diskAttachment", value="디스크 ID 목록", dataTypeClass=Array<DiskAttachmentVo>::class, required=true, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/disks/attach")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun attachDisk(
		@PathVariable vmId: String? = null,
		@RequestBody diskAttachments: List<DiskAttachmentVo>? = null,
	): ResponseEntity<Boolean?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (diskAttachments == null)
			throw ErrorPattern.DISK_ATTACHMENT_VO_INVALID.toException()
		log.info("/computing/vms/{}/disks/attach ... 가상머신 디스크 연결 ", vmId)
		return ResponseEntity.ok(iVmDisk.attachMultiDiskFromVm(vmId, diskAttachments))
	}

	@ApiOperation(
		httpMethod="PUT",
		value="가상머신 디스크 편집",
		notes="선택된 가상머신의 디스크를 편집한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="diskAttachmentId", value="디스크 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="diskAttachment", value="디스크", dataTypeClass=DiskAttachmentVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@PutMapping("/{vmId}/disks/{diskAttachmentId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun updateDisk(
		@PathVariable vmId: String? = null,
		@PathVariable diskAttachmentId: String? = null,
		@RequestBody diskAttachment: DiskAttachmentVo? = null,
	): ResponseEntity<DiskAttachmentVo?> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (diskAttachmentId.isNullOrEmpty())
			throw ErrorPattern.DISK_ATTACHMENT_ID_NOT_FOUND.toException()
		if (diskAttachment == null)
			throw ErrorPattern.DISK_ATTACHMENT_VO_INVALID.toException()
		log.info("/computing/vms/{}/disks/{} ... 가상머신 디스크 편집 ", vmId, diskAttachmentId)
		return ResponseEntity.ok(iVmDisk.updateDiskFromVm(vmId, diskAttachment))
	}

	@ApiOperation(
		httpMethod="DELETE",
		value="가상머신 디스크 삭제",
		notes="선택된 가상머신의 디스크를 삭제한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="diskAttachments", value="디스크 ID 목록", dataTypeClass=Array<DiskAttachmentVo>::class, required=true, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@DeleteMapping("/{vmId}/disks")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun removeDisks(
		@PathVariable vmId: String? = null,
		@RequestBody diskAttachments: List<DiskAttachmentVo>? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (diskAttachments == null)
			throw ErrorPattern.DISK_ATTACHMENT_NOT_FOUND.toException()
		log.info("/computing/vms/{}/disks ... 가상머신 디스크 삭제 ", vmId)
		return ResponseEntity.ok(iVmDisk.removeDisksFromVm(vmId, diskAttachments))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 디스크 활성화",
		notes="선택된 가상머신의 디스크 활성화한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="diskAttachments", value="디스크 ID 목록", dataTypeClass=Array<String>::class, required=true, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/disks/activate")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun activateDisk(
		@PathVariable vmId: String? = null,
		@RequestBody diskAttachments: List<String>? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (diskAttachments == null)
			throw ErrorPattern.DISK_ATTACHMENT_NOT_FOUND.toException()
		log.info("/computing/vms/{}/disks/activate ... 가상머신 디스크 활성화", vmId)
		return ResponseEntity.ok(iVmDisk.activeDisksFromVm(vmId, diskAttachments))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 디스크 비활성화",
		notes="선택된 가상머신의 디스크 비활성화한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="diskAttachments", value="디스크 ID 목록", dataTypeClass=Array<String>::class, required=true, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/disks/deactivate")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun deactivateDisk(
		@PathVariable vmId: String? = null,
		@RequestBody diskAttachments: List<String>? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (diskAttachments == null)
			throw ErrorPattern.DISK_ATTACHMENT_NOT_FOUND.toException()
		log.info("/computing/vms/{}/disks/deactivate ... 가상머신 디스크 활성화", vmId)
		return ResponseEntity.ok(iVmDisk.deactivateDisksFromVm(vmId, diskAttachments))
	}


	@ApiOperation(
		httpMethod="GET",
		value="가상머신 디스크 이동 스토리지 도메인 목록",
		notes="선택된 가상머신의 디스크를 이동할 스토리지 도메인 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="diskAttachmentId", value="디스크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{vmId}/disks/{diskAttachmentId}/storageDomains")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun storageDomains(
		@PathVariable vmId: String? = null,
		@PathVariable diskAttachmentId: String? = null,
	): ResponseEntity<List<StorageDomainVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (diskAttachmentId.isNullOrEmpty())
			throw ErrorPattern.DISK_ATTACHMENT_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/disks/{} ... 가상머신 디스크 이동 스토리지 목록", vmId, diskAttachmentId)
		return ResponseEntity.ok(iVmDisk.findAllStorageDomains(vmId, diskAttachmentId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="가상머신 디스크 이동",
		notes="선택된 가상머신의 디스크를 이동한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="diskAttachment", value="디스크", dataTypeClass=DiskAttachmentVo::class, required=true, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{vmId}/disks/move")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun move(
		@PathVariable vmId: String? = null,
		@RequestBody diskAttachment: DiskAttachmentVo? = null,
	): ResponseEntity<Boolean> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (diskAttachment == null)
			throw ErrorPattern.DISK_ATTACHMENT_VO_INVALID.toException()
		log.info("/computing/vms/{}/disks/move ... 가상머신 디스크 이동", vmId)
		return ResponseEntity.ok(iVmDisk.moveDiskFromVm(vmId, diskAttachment))
	}

	// endregion


	//region: vmSnapshot
	@Autowired private lateinit var iVmSnapshot: ItVmSnapshotService

	@ApiOperation(
		httpMethod="GET",
		value="가상머신 스냅샷 목록",
		notes="선택된 가상머신의 스냅샷 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{vmId}/snapshots")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun snapshots(
		@PathVariable vmId: String? = null,
	): ResponseEntity<List<SnapshotVo>> {
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		log.info("/computing/vms/{}/snapshots ... 가상머신 스냅샷 목록", vmId)
		return ResponseEntity.ok(iVmSnapshot.findAllSnapshotsFromVm(vmId))
	}


	// endregion


	companion object {
		private val log by LoggerDelegate()
	}
}