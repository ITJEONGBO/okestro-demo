package com.itinfo.itcloud.controller.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.HostNicVo
import com.itinfo.itcloud.model.storage.HostStorageVo
import com.itinfo.itcloud.model.storage.IscsiDetailVo
import com.itinfo.itcloud.service.computing.ItHostNicService
import com.itinfo.itcloud.service.computing.ItHostOperationService
import com.itinfo.itcloud.service.computing.ItHostService
import com.itinfo.util.ovirt.error.ErrorPattern
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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
		value="호스트 목록 조회",
		notes="전체 호스트 목록을 조회한다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun hosts(): ResponseEntity<List<HostVo>> {
		log.info("/computing/hosts ... 호스트 목록")
		return ResponseEntity.ok(iHost.findAll())
	}

	@ApiOperation(
		httpMethod="GET",
		value = "호스트 정보 상세조회",
		notes = "선택된 호스트의 정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{hostId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun host(
		@PathVariable hostId: String? = null,
	): ResponseEntity<HostVo?> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{} ... 호스트 상세정보", hostId)
		return ResponseEntity.ok(iHost.findOne(hostId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="호스트 생성",
		notes="호스트를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="host", value="호스트", dataTypeClass=HostVo::class, paramType="body")
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun add(
		@RequestBody host: HostVo? = null
	): ResponseEntity<HostVo?> {
		if (host == null)
			throw ErrorPattern.HOST_VO_INVALID.toException()
		log.info("/computing/hosts ... 호스트 생성\n{}", host)
		return ResponseEntity.ok(iHost.add(host))
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
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@PutMapping("/{hostId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun update(
		@PathVariable hostId: String? = null,
		@RequestBody host: HostVo? = null
	): ResponseEntity<HostVo?> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		if (host == null)
			throw ErrorPattern.HOST_VO_INVALID.toException()
		log.info("/computing/hosts/{} ... 호스트 편집\n{}", hostId, host)
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
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@DeleteMapping("/{hostId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun remove(
		@PathVariable hostId: String? = null,
	): ResponseEntity<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{} ... 호스트 삭제", hostId)
		return ResponseEntity.ok(iHost.remove(hostId))
	}

//	@ApiOperation(
//		httpMethod="DELETE",
//		value="호스트 멀티 삭제",
//		notes="호스트를 멀티 삭제한다"
//	)
//	@ApiImplicitParams(
//		ApiImplicitParam(name="hostIdList", value="호스트 ID 목록", dataTypeClass=Array<String>::class, required=true, paramType="body"),
//	)
//	@ApiResponses(
//		ApiResponse(code = 200, message = "OK")
//	)
//	@DeleteMapping
//	@ResponseBody
//	@ResponseStatus(HttpStatus.OK)
//	fun removeMultiple(
//		@RequestBody hostIdList: List<String>? = null,
//	): ResponseEntity<List<Boolean>> {
//		if (hostIdList.isNullOrEmpty())
//			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
//		log.info("/computing/hosts ... 호스트 다중 삭제")
//		return ResponseEntity.ok(iHost.removeMultiple(hostIdList))
//	}


	@ApiOperation(
		httpMethod="GET",
		value="호스트 가상머신 목록",
		notes="선택된 호스트의 가상머신 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{hostId}/vms")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun vms(
		@PathVariable hostId: String? = null,
	): ResponseEntity<List<VmVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/vms ... 호스트 가상머신 목록", hostId)
		return ResponseEntity.ok(iHost.findAllVmsFromHost(hostId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="호스트 장치 목록",
		notes="선택된 호스트의 장치 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{hostId}/devices")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun devices(
		@PathVariable hostId: String? = null,
	): ResponseEntity<List<HostDeviceVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/devices ...  호스트 장치 목록", hostId)
		return ResponseEntity.ok(iHost.findAllHostDevicesFromHost(hostId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="호스트 이벤트 목록",
		notes="선택된 호스트의 이벤트 목록을 조회한다")
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{hostId}/events")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun events(
		@PathVariable hostId: String?
	): ResponseEntity<List<EventVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/events ... 호스트 이벤트 목록", hostId)
		return ResponseEntity.ok(iHost.findAllEventsFromHost(hostId))
	}


	@ApiOperation(
		httpMethod="GET",
		value="도메인 생성시 필요한 iSCSI 목록",
		notes="도메인 생성 - iSCSI 유형 대상 LUN 목록"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name = "hostId", value = "호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{hostId}/iscsis")
	@ResponseBody
	fun iSCSIs(
		@PathVariable("hostId") hostId: String? = null
	): ResponseEntity<List<HostStorageVo>> {
		if (hostId == null)
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/iscsis ... 호스트 iscsis 목록", hostId)
		return ResponseEntity.ok(iHost.findAllIscsiFromHost(hostId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="도메인 생성시 필요한 Fibre Channel ",
		notes="도메인 생성 - Fibre Channel 유형 대상 LUN 목록"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name = "hostId", value = "호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{hostId}/fibres")
	@ResponseBody
	fun fibres(
		@PathVariable("hostId") hostId: String? = null
	): ResponseEntity<List<HostStorageVo>> {
		if (hostId == null)
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/fibres ... 호스트 fibres 목록", hostId)
		return ResponseEntity.ok(iHost.findAllFibreFromHost(hostId))
	}


	@ApiOperation(
		httpMethod="POST",
		value="도메인 가져오기에 필요한 iSCSI 요청",
		notes="도메인 가져오기 - iSCSI 요쳥"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name = "hostId", value = "호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name= "iscsiDetailVo", value="address, port", dataTypeClass=IscsiDetailVo::class, paramType="body")
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@PostMapping("/{hostId}/iscsisToImport")
	@ResponseBody
	fun importISCSIs(
		@PathVariable("hostId") hostId: String? = null,
		@RequestBody iscsiDetailVo: IscsiDetailVo? = null  // 로그인은 나중에(chap 이름, 암호도 들어가야함)
	): ResponseEntity<List<IscsiDetailVo>> {
		if (hostId == null)
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		if(iscsiDetailVo == null)
			throw ErrorPattern.DISCOVER_TARGET_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/iscsisToImport {} {} ... 호스트 iscsis 가져오기 목록", hostId, iscsiDetailVo.address, iscsiDetailVo.port)
		return ResponseEntity.ok(iHost.findImportIscsiFromHost(hostId, iscsiDetailVo))
	}


	@ApiOperation(
		httpMethod="POST",
		value="도메인 가져오기에 필요한 fcp 요청",
		notes="도메인 가져오기 - fcp 요쳥"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name = "hostId", value = "호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@PostMapping("/{hostId}/fcpToImport")
	@ResponseBody
	fun importFcps(
		@PathVariable("hostId") hostId: String? = null
	): ResponseEntity<List<IdentifiedVo>> {
		if (hostId == null)
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/fcpToImport ... 호스트 fcp 가져오기 목록", hostId)
		return ResponseEntity.ok(iHost.findUnregisterDomainFromHost(hostId))
	}


//	@Deprecated("필요없음")
//	@ApiOperation(
//		httpMethod="GET",
//		value="호스트 권한 목록",
//		notes="선택된 호스트의 권한 목록을 조회한다"
//	)
//	@ApiImplicitParams(
//		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
//	)
//	@ApiResponses(
//		ApiResponse(code = 200, message = "OK")
//	)
//	@GetMapping("/{hostId}/permissions")
//	@ResponseBody
//	@ResponseStatus(HttpStatus.OK)
//	fun permissions(
//		@PathVariable hostId: String? = null
//	): ResponseEntity<List<PermissionVo>> {
//		log.info("/computing/hosts/{}/permissions ... 호스트 권한 목록", hostId)
//		if (hostId.isNullOrEmpty())
//			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
//		return ResponseEntity.ok(iHost.findAllPermissionsFromHost(hostId))
//	}



	@Autowired private lateinit var iHostNic: ItHostNicService

	@ApiOperation(
		httpMethod="GET",
		value="호스트 네트워크 인터페이스 목록",
		notes="선택된 호스트의 네트워크 인터페이스 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{hostId}/nics")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun nics(
		@PathVariable hostId: String? = null
	): ResponseEntity<List<HostNicVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/nics ... 호스트 nic 목록", hostId)
		return ResponseEntity.ok(iHostNic.findAllFromHost(hostId))
	}


//	@ApiOperation(
//		httpMethod="POST",
//		value="호스트 네트워크 설정",
//		notes="선택된 호스트의 네트워크를 설정한다"
//	)
//	@ApiImplicitParams(
//		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
//		ApiImplicitParam(name="network", value="네트워크", dataTypeClass=NetworkVo::class, required=true, paramType="body"),
//	)
//	@ApiResponses(
//		ApiResponse(code = 200, message = "OK")
//	)
//	@PostMapping("/{hostId}/nics/setup")
//	@ResponseBody
//	@ResponseStatus(HttpStatus.OK)
//	fun setupNetwork(
//		@PathVariable hostId: String? = null,
//		@RequestBody network: NetworkVo
//	): ResponseEntity<Boolean> {
//		if (hostId.isNullOrEmpty())
//			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
//		log.info("/computing/hosts/{}/nics/setup ... 호스트 네트워크 생성", hostId)
//		return ResponseEntity.ok(iHostNic.setUpNetworksFromHost(hostId, network))
//	}



	@Autowired private lateinit var iHostOp: ItHostOperationService

	@ApiOperation(
		httpMethod="POST",
		value="호스트 유지보수 모드전환",
		notes="호스트를 유지보수 모드로 전환한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{hostId}/deactivate")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun deactivate(
		@PathVariable hostId: String?
	): ResponseEntity<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/deactivate ... 호스트 유지보수", hostId)
		return ResponseEntity.ok(iHostOp.deactivate(hostId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="호스트 멀티 유지보수 모드전환",
		notes="호스트를 멀티 유지보수 모드로 전환한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostIdList", value="호스트 ID 목록", dataTypeClass=Array<String>::class, required=true, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/deactivate")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun deactivateMultiple(
		@RequestBody hostIdList: List<String>? = null,
	): ResponseEntity<Map<String, String>> {
		if (hostIdList.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/deactivate ... 호스트 멀티 유지보수")
		return ResponseEntity.ok(iHostOp.deactivateMultiple(hostIdList))
	}

	@ApiOperation(
		httpMethod="POST",
		value="호스트 활성화 모드전환",
		notes="호스트를 활성화 모드로 전환한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{hostId}/activate")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun activate(
		@PathVariable hostId: String?
	): ResponseEntity<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/activate ... 호스트 활성", hostId)
		return ResponseEntity.ok(iHostOp.activate(hostId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="호스트 멀티 활성 모드전환",
		notes="호스트를 멀티 활성 모드로 전환한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostIdList", value="호스트 ID 목록", dataTypeClass=Array<String>::class, required=true, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/activate")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun activateMultiple(
		@RequestBody hostIdList: List<String>? = null,
	): ResponseEntity<Map<String, String>> {
		if (hostIdList.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/activate ... 호스트 멀티 활성")
		return ResponseEntity.ok(iHostOp.activateMultiple(hostIdList))
	}


//	@ApiOperation(
//		httpMethod="POST",
//		value="호스트 새로고침",
//		notes="호스트를 새로고침 한다"
//	)
//	@ApiImplicitParams(
//		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
//	)
//	@ApiResponses(
//		ApiResponse(code = 201, message = "CREATED"),
//		ApiResponse(code = 404, message = "NOT_FOUND")
//	)
//	@PostMapping("/{hostId}/refresh")
//	@ResponseBody
//	@ResponseStatus(HttpStatus.CREATED)
//	fun refresh(
//		@PathVariable hostId: String?
//	): ResponseEntity<Boolean> {
//		if (hostId.isNullOrEmpty())
//			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
//		log.info("/computing/hosts/{}/refresh ... 호스트 새로고침", hostId)
//		return ResponseEntity.ok(iHostOp.refresh(hostId))
//	}


	@ApiOperation(
		httpMethod="POST",
		value="호스트 재시작",
		notes="호스트를 재시작 한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{hostId}/restart")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun restart(
		@PathVariable hostId: String?
	): ResponseEntity<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/restart ... 호스트 ssh 재시작", hostId)
		return ResponseEntity.ok(iHostOp.restart(hostId))
	}

//	@ApiOperation(
//		httpMethod="POST",
//		value="호스트 멀티 재시작",
//		notes="호스트를 멀티 재시작 한다"
//	)
//	@ApiImplicitParams(
//		ApiImplicitParam(name="hostIdList", value="호스트 ID 목록", dataTypeClass=Array<String>::class, required=true, paramType="body"),
//	)
//	@ApiResponses(
//		ApiResponse(code = 201, message = "CREATED"),
//		ApiResponse(code = 404, message = "NOT_FOUND")
//	)
//	@PostMapping("/restart")
//	@ResponseBody
//	@ResponseStatus(HttpStatus.CREATED)
//	fun restartMultiple(
//		@RequestBody hostIdList: List<String>? = null,
//	): ResponseEntity<Boolean> {
//		if (hostIdList.isNullOrEmpty())
//			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
//		log.info("/computing/hosts/restart ... 호스트 ssh 재시작")
//		return ResponseEntity.ok(iHostOp.restartMultiple(hostIdList))
//	}

	@ApiOperation(
		httpMethod="POST",
		value="호스트 글로벌 HA 활성화",
		notes="호스트를 글로벌 HA 활성화한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{hostId}/activateGlobal")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun activateGlobal(
		@PathVariable hostId: String?
	): ResponseEntity<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/activateGlobal ... 호스트 글로벌 HA 활성화", hostId)
		return ResponseEntity.ok(iHostOp.globalHaActivate(hostId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="호스트 글로벌 HA 비활성화",
		notes="호스트를 글로벌 HA 비활성화한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{hostId}/deactivateGlobal")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun deactivateGlobal(
		@PathVariable hostId: String?
	): ResponseEntity<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("/computing/hosts/{}/deactivateGlobal ... 호스트 글로벌 HA 비활성화", hostId)
		return ResponseEntity.ok(iHostOp.globalHaDeactivate(hostId))
	}

	companion object {
		private val log by LoggerDelegate()
	}
}