package com.itinfo.itcloud.controller.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.HostNicVo
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.service.computing.ItHostOperationService
import com.itinfo.itcloud.service.computing.ItHostService
import com.itinfo.util.ovirt.error.ErrorPattern
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
@Api(tags = ["Computing", "Host"])
@RequestMapping("/api/v1/computing/hosts")
class HostController {
	@Autowired private lateinit var iHost: ItHostService

	@ApiOperation(
		httpMethod="GET",
		value="호스트 목록",
		notes="전체 호스트 목록을 조회한다"
	)
	@GetMapping
	@ResponseBody
	fun findAll(): ResponseEntity<List<HostVo>> {
		log.info("--- 호스트 목록")
		return ResponseEntity.ok(iHost.findAll())
	}


	@ApiOperation(
		httpMethod="GET",
		value = "호스트 상세정보",
		notes = "호스트의 상세정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{hostId}")
	@ResponseBody
	fun findOne(
		@PathVariable hostId: String? = null,
	): ResponseEntity<HostVo?> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 일반")
		return ResponseEntity.ok(iHost.findOne(hostId))
	}


	@ApiOperation(
		httpMethod="POST",
		value="호스트 생성",
		notes="호스트를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostVo", value="호스트", dataTypeClass=HostVo::class, paramType="body")
	)
	@PostMapping
	@ResponseBody
	fun add(
		@RequestBody hostVo: HostVo?
	): ResponseEntity<HostVo?> {
		log.info("--- 호스트 생성")
		if (hostVo == null)
			throw ErrorPattern.HOST_VO_INVALID.toException()
		return ResponseEntity.ok(iHost.add(hostVo))
	}


	@ApiOperation(
		httpMethod="PUT",
		value="호스트 편집",
		notes="호스트를 편집한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="host", value="호스트", dataTypeClass=HostVo::class, paramType="body")
	)
	@PutMapping("/{hostId}")
	@ResponseBody
	fun update(
		@PathVariable hostId: String? = null,
		@RequestBody host: HostVo? = null
	): ResponseEntity<HostVo?> {
		if (host == null)
			throw ErrorPattern.HOST_VO_INVALID.toException()
		log.info("--- 호스트 편집")
		return ResponseEntity.ok(iHost.update(host))
	}

	@ApiOperation(
		httpMethod="DELETE",
		value="호스트 삭제",
		notes="호스트를 삭제한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@DeleteMapping("/{hostId}")
	@ResponseBody
	fun remove(
		@PathVariable hostId: String? = null,
	): ResponseEntity<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 삭제")
		return ResponseEntity.ok(iHost.remove(hostId))
	}


	@ApiOperation(
		httpMethod="GET",
		value="호스트 가상머신 목록",
		notes="선택된 호스트의 가상머신 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{hostId}/vms")
	@ResponseBody
	fun findAllVmsFromHost(
		@PathVariable hostId: String? = null,
	): ResponseEntity<List<VmVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 vm")
		return ResponseEntity.ok(iHost.findAllVmsFromHost(hostId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="호스트 네트워크 인터페이스 목록",
		notes="선택된 호스트의 네트워크 인터페이스 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{hostId}/nics")
	@ResponseBody
	fun findAllHostNicsFromHost(
	@PathVariable hostId: String? = null
	): ResponseEntity<List<HostNicVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 nic")
		return ResponseEntity.ok(iHost.findAllHostNicsFromHost(hostId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="호스트 장치 목록",
		notes="선택된 호스트의 장치 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{hostId}/devices")
	@ResponseBody
	fun findAllHostDevicesFromHost(
		@PathVariable hostId: String? = null,
	): ResponseEntity<List<HostNicVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 장치")
		return ResponseEntity.ok(iHost.findAllHostNicsFromHost(hostId))
	}


	@ApiOperation(
		httpMethod="GET",
		value="호스트 권한 목록",
		notes="선택된 호스트의 권한 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{hostId}/permissions")
	@ResponseBody
	fun findAllPermissionsFromHost(
		@PathVariable hostId: String? = null
	): ResponseEntity<List<PermissionVo>> {
		log.info("--- 호스트 권한")
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iHost.findAllPermissionsFromHost(hostId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="호스트 이벤트 목록",
		notes="선택된 호스트의 이벤트 목록을 조회한다")
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{hostId}/events")
	@ResponseBody
	fun findAllEventsFromHost(
		@PathVariable hostId: String?
	): ResponseEntity<List<EventVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 이벤트")
		return ResponseEntity.ok(iHost.findAllEventsFromHost(hostId))
	}

	@Autowired private lateinit var iHostOp: ItHostOperationService

	@ApiOperation(
		httpMethod="POST",
		value="호스트 유지보수 모드전환",
		notes="호스트를 유지보수 모드로 전환한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@PostMapping("/{hostId}/deactivate")
	@ResponseBody
	fun deactivate(
		@PathVariable hostId: String?
	): ResponseEntity<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 유지보수")
		return ResponseEntity.ok(iHostOp.deactivate(hostId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="호스트 활성화 모드전환",
		notes="호스트를 활성화 모드로 전환한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@PostMapping("/{hostId}/activate")
	@ResponseBody
	fun activeHost(
		@PathVariable hostId: String?
	): ResponseEntity<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 활성")
		return ResponseEntity.ok(iHostOp.activate(hostId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="호스트 새로고침",
		notes="호스트를 새로고침 한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@PostMapping("/{hostId}/refresh")
	@ResponseBody
	fun refresh(
		@PathVariable hostId: String?
	): ResponseEntity<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 새로고침")
		return ResponseEntity.ok(iHostOp.refresh(hostId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="호스트 재시작",
		notes="호스트를 재시작 한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@PostMapping("/{hostId}/restart")
	@ResponseBody
	fun reStartHost(
		@PathVariable hostId: String?
	): ResponseEntity<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 ssh 재시작")
		return ResponseEntity.ok(iHostOp.restart(hostId))
	}

	companion object {
		private val log by LoggerDelegate()
	}
}