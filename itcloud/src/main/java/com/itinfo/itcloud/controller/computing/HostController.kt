package com.itinfo.itcloud.controller.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.HostNicVo
import com.itinfo.itcloud.model.response.Res
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.service.computing.ItHostOperationService
import com.itinfo.itcloud.service.computing.ItHostService
import com.itinfo.util.ovirt.error.ErrorPattern
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
@Api(tags = ["Computing-Host"])
@RequestMapping("/computing/hosts")
class HostController {
	@Autowired private lateinit var iHost: ItHostService

	@ApiOperation(
		httpMethod="GET",
		value="호스트 목록",
		notes="전체 호스트 목록을 조회한다"
	)
	@GetMapping
	@ResponseBody
	fun findAll(): Res<List<HostVo>> {
		log.info("--- 호스트 목록")
		return Res.safely { iHost.findAll() }
	}
/*
	@GetMapping("/settings")
	@ApiOperation(value = "호스트 생성창", notes = "호스트 생성시 필요한 내용을 조회한다")
	@ResponseBody
	public List<ClusterVo> setClusterList(){
		log.info("--- 호스트 생성 창");
		return hostService.setClusterList();
	}
*/
	@ApiOperation(
		httpMethod="POST",
		value="호스트 생성",
		notes="호스트를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostVo", value="호스트", dataTypeClass=HostVo::class)
	)
	@PostMapping
	@ResponseBody
	fun add(
		@RequestBody hostVo: HostVo?
	): Res<HostVo?> {
		log.info("--- 호스트 생성")
		if (hostVo == null)
			throw ErrorPattern.HOST_VO_INVALID.toException()
		return Res.safely { iHost.add(hostVo)  }
	}

	@ApiOperation(
		httpMethod="GET",
		value="호스트 수정창",
		notes="선택된 호스트의 정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{hostId}/edit")
	@ResponseBody
	fun getHostCreate(
		@PathVariable hostId: String? = null,
	): Res<HostVo?> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 수정창")
		return Res.safely { iHost.findOne(hostId) }
	}

	@ApiOperation(
		httpMethod="PUT",
		value="호스트 편집",
		notes="호스트를 편집한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="host", value="호스트", dataTypeClass=HostVo::class)
	)
	@PutMapping("/{hostId}")
	@ResponseBody
	fun update(
		@PathVariable hostId: String? = null,
		@RequestBody host: HostVo? = null
	): Res<HostVo?> {
		if (host == null)
			throw ErrorPattern.HOST_VO_INVALID.toException()
		log.info("--- 호스트 편집")
		return Res.safely { iHost.update(host) }
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
	): Res<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 삭제")
		return Res.safely { iHost.remove(hostId) }
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
	): Res<HostVo?> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 일반")
		return Res.safely { iHost.findOne(hostId) }
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
	): Res<List<VmVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 vm")
		return Res.safely { iHost.findAllVmsFromHost(hostId) }
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
	fun nic(
	@PathVariable hostId: String? = null
	): Res<List<HostNicVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 nic")
		return Res.safely { iHost.findAllHostNicsFromHost(hostId) }
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
	fun device(
		@PathVariable hostId: String? = null,
	): Res<List<HostNicVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 장치")
		return Res.safely { iHost.findAllHostNicsFromHost(hostId) }
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
	fun permission(
		@PathVariable hostId: String? = null
	): Res<List<PermissionVo>> {
		log.info("--- 호스트 권한")
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		return Res.safely { iHost.findAllPermissionsFromHost(hostId) }
	}
	@ApiOperation(
		httpMethod="GET",
		value="호스트 선호도 레이블 목록",
		notes="선택된 호스트의 선호도 레이블 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{hostId}/affinitylabels")
	@ResponseBody
	fun getAffinitylabels(
		@PathVariable hostId: String? = null
	): Res<List<AffinityLabelVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 선호도 레이블")
		return Res.safely { iHost.findAllAffinityLabelsFromHost(hostId); }
	}
/*
	@ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class)
	@GetMapping("/{hostId}/affinitylabel/settings")
	@ResponseBody
	fun setAffinitygroup(
		@PathVariable hostId: String? = null
	): Res<AffinityHostVm?> {
		log.info("--- 호스트 선호도 레이블 생성 창");
		return host.setAffinityDefaultInfo(id, "label");
	}


	@ApiImplicitParam(name="hostId", value="호스트 ID", dataTypeClass=String::class)
	@PostMapping("/{id}/affinitylabel")
	@ResponseBody
	public CommonVo<Boolean> addAff(
		@PathVariable String id,
		@RequestBody AffinityLabelCreateVo alVo
	): Aff {
		log.info("--- 호스트 선호도 레이블 생성");
		return host.addAffinitylabel(id, alVo);
	}
*/
	//	@GetMapping("/{id}/affinitylabel/{alId}/settings")
	//	@ResponseBody
	//	public AffinityHostVm setAffinityDefaultInfo(@PathVariable String id,
	//												 @PathVariable String type){
	//		log.info("--- 호스트 선호도 레이블 수정 창");
	//		return hostService.setAffinityDefaultInfo(id, "label");
	//	}
	//
	//
	//	@PutMapping("/{id}/affinitylabel/{alId}")
	//	@ResponseBody
	//	public CommonVo<Boolean> editAff(@PathVariable String id,
	//									 @PathVariable String alId,
	//									 @RequestBody AffinityLabelCreateVo alVo) {
	//		log.info("--- 호스트 선호도 레이블 편집");
	//		return hostService.editAffinitylabel(id, alId, alVo);
	//	}
	//
	//	@DeleteMapping("/{id}/affinitylabel/{alId}")
	//	@ResponseBody
	//	public CommonVo<Boolean> deleteAff(@PathVariable String id,
	//									   @PathVariable String alId) {
	//		log.info("--- 호스트 선호도 레이블 삭제");
	//		return hostService.deleteAffinitylabel(id, alId);
	//	}
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
	): Res<List<EventVo>> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 이벤트")
		return Res.safely { iHost.findAllEventsFromHost(hostId) }
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
	): Res<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 유지보수")
		return Res.safely { iHostOp.deactivate(hostId) }
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
	): Res<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 활성")
		return Res.safely { iHostOp.activate(hostId) }
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
	): Res<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 새로고침")
		return Res.safely { iHostOp.refresh(hostId) }
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
	): Res<Boolean> {
		if (hostId.isNullOrEmpty())
			throw ErrorPattern.HOST_ID_NOT_FOUND.toException()
		log.info("--- 호스트 ssh 재시작")
		return Res.safely { iHostOp.restart(hostId) }
	}

	companion object {
		private val log by LoggerDelegate()
	}
}