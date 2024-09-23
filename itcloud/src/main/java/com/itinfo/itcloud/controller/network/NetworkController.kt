package com.itinfo.itcloud.controller.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.controller.BaseController
import com.itinfo.itcloud.error.toException
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.itcloud.model.computing.ClusterVo
import com.itinfo.itcloud.model.computing.HostVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.network.VnicProfileVo
import com.itinfo.itcloud.model.network.NetworkTemplateVo
import com.itinfo.itcloud.model.network.OpenStackNetworkVo
import com.itinfo.itcloud.service.network.ItNetworkService
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@Api(tags = ["Network"])
@RequestMapping("/api/v1/networks")
class NetworkController: BaseController() {
	@Autowired private lateinit var iNetwork: ItNetworkService

	@ApiOperation(
		httpMethod="GET",
		value="전체 네트워크 목록 조회",
		notes="전체 네트워크 목록을 조회한다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun networks(): ResponseEntity<List<NetworkVo>> {
		log.info("GET /api/v1/networks ... 네트워크 목록")
		return ResponseEntity.ok(iNetwork.findAll())
	}

	@ApiOperation(
		httpMethod="GET",
		value="네트워크 상세정보",
		notes="네트워크의 상세정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{networkId}")
	@ResponseBody
	fun network(
		@PathVariable networkId: String? = null
	): ResponseEntity<NetworkVo?> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		log.info("--- Network 일반")
		return ResponseEntity.ok(iNetwork.findOne(networkId))
	}


	@ApiOperation(
		httpMethod="POST",
		value="/networks",
		notes="네트워크를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="network", value="네트워크", dataTypeClass=NetworkVo::class, required=true, paramType="body")
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED")
	)
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun addNetwork(
		@RequestBody network: NetworkVo? = null
	): ResponseEntity<NetworkVo?> {
		if (network == null)
			throw ErrorPattern.NETWORK_VO_INVALID.toException()
		log.info("/networks ... 네트워크 생성")
		return ResponseEntity.ok(iNetwork.add(network))
	}


	@ApiOperation(
		httpMethod="PUT",
		value="/networks/{networkId}",
		notes="네트워크를 편집한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@PutMapping("/{networkId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun editNetwork(
		@PathVariable networkId: String? = null,
		@RequestBody networkVo: NetworkVo? = null,
	): ResponseEntity<NetworkVo?> {
		log.info("/networks/{} ... Network 편집", networkId)
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		if (networkVo == null)
			throw ErrorPattern.NETWORK_VO_INVALID.toException()
		return ResponseEntity.ok(iNetwork.update(networkVo))
	}

	@ApiOperation(
		httpMethod="DELETE",
		value="/networks/{networkId}",
		notes="네트워크를 삭제한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@DeleteMapping("/{networkId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun deleteNetwork(
		@PathVariable networkId: String? = null,
	): ResponseEntity<Boolean> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		log.info("/networks/{} ... Network 삭제", networkId)
		return ResponseEntity.ok(iNetwork.remove(networkId))
	}

	@ApiOperation(
		httpMethod="PUT",
		value="/networks/import/settings",
		notes="네트워크 가져오기 창"
	)
	@GetMapping("/import/settings")
	@ResponseBody
	fun setImportNetwork(): ResponseEntity<OpenStackNetworkVo?> {
		log.info("--- Network 가져오기 창(openstack networkProvider 목록)")
		return ResponseEntity.ok(iNetwork.findAllNetworkProviderFromNetwork())
	}

	// TODO: 정리필요
	// 네트워크 가져오기
//	@PostMapping("/import")
//	fun importNw(): ResponseEntity<Boolean> {
//		log.info("--- Network 가져오기")
//		return ResponseEntity.ok(iNetwork.importNetwork())
//	}


	// region: vnicProfile
	@ApiOperation(
		httpMethod="GET",
		value="네트워크 vnic 프로파일 목록",
		notes="선택된 네트워크의 vnic 프로파일 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{networkId}/vnics")
	@ResponseBody
	fun vnic(
		@PathVariable networkId: String? = null,
	): ResponseEntity<List<VnicProfileVo>> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		log.info("--- Network vnic 프로파일")
		return ResponseEntity.ok(iNetwork.findAllVnicProfilesFromNetwork(networkId))
	}

//	// vnic 생성 창
//	@ApiImplicitParams(
//		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
//	)
//	@GetMapping("/{networkId}/vnics/settings")
//	@ResponseBody
//	fun setVnic(
//		@PathVariable networkId: String? = null,
//	): ResponseEntity<VnicProfileVo?> {
//		if (networkId.isNullOrEmpty())
//			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
//		log.info("--- Network vnic 프로파일 생성창")
//		return ResponseEntity.ok(iVnicProfile.findOneVnicProfile(networkId) }
//	}
//
//	// vnic 생성
//	@ApiImplicitParams(
//		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
//		ApiImplicitParam(name="vnicProfile", value="VNIC 생성", dataTypeClass=VnicProfileVo::class, paramType="body"),
//	)
//	@PostMapping("/{networkId}/vnic")
//	@ResponseBody
//	fun addVnic(
//		@PathVariable networkId: String?,
//		@RequestBody vnicProfile: VnicProfileVo?
//	): ResponseEntity<VnicProfileVo?> {
//		log.info("--- Network vnic 프로파일 생성")
//		if (networkId.isNullOrEmpty())
//			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
//		if (vnicProfile == null)
//			throw ErrorPattern.VNIC_PROFILE_VO_INVALID.toException()
//		return ResponseEntity.ok(iVnicProfile.addVnicProfileByNetwork(networkId, vnicProfile) }
//	}
//
//	// vnic 편집 창
//	@ApiImplicitParams(
//		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
//		ApiImplicitParam(name="vnicProfileId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
//	)
//	@GetMapping("/{networkId}/vnics/{vnicProfileId}/settings")
//	@ResponseBody
//	fun setEditVnic(
//		@PathVariable networkId: String? = null,
//		@PathVariable vnicProfileId: String? = null,
//	): ResponseEntity<VnicProfileVo?> {
//		if (networkId.isNullOrEmpty())
//			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
//		if (vnicProfileId == null)
//			throw ErrorPattern.VNIC_PROFILE_ID_NOT_FOUND.toException()
//		log.info("--- Network vnic프로파일 편집창")
//		return Res.success { iVnicProfile.setEditVnicProfileByNetwork(networkId, vnicProfileId) }
//	}
//
//	// vnic 편집
//	@ApiImplicitParams(
//		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
//		ApiImplicitParam(name="vnicProfileId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
//		ApiImplicitParam(name="vnicProfile", value="VNIC 생성", dataTypeClass=VnicProfileVo::class, paramType="body"),
//	)
//	@PutMapping("/{networkId}/vnics/{vnicProfileId}")
//	@ResponseBody
//	fun editVnic(
//		@PathVariable networkId: String? = null,
//		@PathVariable vnicProfileId: String? = null,
//		@RequestBody vnicProfile: VnicProfileVo? = null,
//	): ResponseEntity<VnicProfileVo?> {
//		log.info("--- Network vnic프로파일 편집")
//		if (networkId.isNullOrEmpty())
//			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
//		if (vnicProfileId.isNullOrEmpty())
//			throw ErrorPattern.VNIC_PROFILE_ID_NOT_FOUND.toException()
//		if (vnicProfile == null)
//			throw ErrorPattern.VNIC_PROFILE_VO_INVALID.toException()
//		return ResponseEntity.ok(iVnicProfile.updateVnicProfileFromNetwork(networkId, vnicProfileId, vnicProfile) }
//	}
//
//	// vnic 삭제
//	@ApiImplicitParams(
//		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
//		ApiImplicitParam(name="vnicProfileId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
//	)
//	@DeleteMapping("/{id}/vnics/{vcId}")
//	@ResponseBody
//	fun deleteVnic(
//		@PathVariable networkId: String? = null,
//		@PathVariable vnicProfileId: String? = null,
//	): ResponseEntity<Boolean> {
//		if (networkId.isNullOrEmpty())
//			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
//		if (vnicProfileId.isNullOrEmpty())
//			throw ErrorPattern.VNIC_PROFILE_ID_NOT_FOUND.toException()
//		log.info("--- Network vnic프로파일 삭제")
//		return ResponseEntity.ok(iVnicProfile.deleteVnicProfileByNetwork(networkId, vnicProfileId) }
//	}
	// endregion



	// 클러스터 목록
	@GetMapping("/{networkId}/clusters")
	@ApiOperation(value = "네트워크 클러스터 목록", notes = "선택된 네트워크의 클러스터 목록을 조회한다")
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ResponseBody
	fun cluster(
		@PathVariable networkId: String? = null,
	): ResponseEntity<List<ClusterVo>> {
		log.info("--- Network 클러스터")
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iNetwork.findAllClustersFromNetwork(networkId))
	}

	// 클러스터 네트워크 관리창
//	@ApiImplicitParams(
//		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
//		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
//	)
//	@GetMapping("/{networkId}/clusters/{clusterId}/settings")
//	@ResponseBody
//	fun getUsage(
//		@PathVariable networkId: String? = null,
//		@PathVariable clusterId: String? = null,
//	): ResponseEntity<NetworkUsageVo> {
//		log.info("--- Network  네트워크 관리 창")
//		if (networkId.isNullOrEmpty())
//			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
//		if (clusterId.isNullOrEmpty())
//			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
//		return ResponseEntity.ok(iNetwork.getUsage(networkId, clusterId) }
//	}

	@ApiOperation(value = "네트워크 호스트 목록", notes = "선택된 네트워크의 호스트 목록을 조회한다")
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{networkId}/hosts")
	@ResponseBody
	fun host(
		@PathVariable networkId: String? = null,
	): ResponseEntity<List<HostVo>> {
		log.info("--- Network 호스트")
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iNetwork.findAllHostsFromNetwork(networkId))
	}

	@GetMapping("/{networkId}/vms")
	@ApiOperation(value = "네트워크 가상머신 목록", notes = "선택된 네트워크의 가상머신 목록을 조회한다")
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ResponseBody
	fun findAllVmsFromNetwork(
		@PathVariable networkId: String? = null,
	): ResponseEntity<List<VmVo>> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		log.info("--- Network 가상머신")
		return ResponseEntity.ok(iNetwork.findAllVmsFromNetwork(networkId))
	}

	// 가상머신 nic 제거
/*
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="vmId", value="가상머신 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="nicId", value="네트워크 인터페이스 컨트롤러 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@DeleteMapping("/{networkId}/vms/{vmId}/{nicId}")
	@ResponseBody
	fun deleteVmNic(
		@PathVariable networkId: String? = null,
		@PathVariable vmId: String? = null,
		@PathVariable nicId: String? = null,
	): ResponseEntity<Boolean> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		if (vmId.isNullOrEmpty())
			throw ErrorPattern.VM_ID_NOT_FOUND.toException()
		if (nicId.isNullOrEmpty())
			throw ErrorPattern.NIC_ID_NOT_FOUND.toException()
		log.info("--- Network 가상머신 nic 제거")
		return ResponseEntity.ok(iNetwork.deleteVmNicFromNetwork(/* networkId, */vmId, nicId) }
	}
*/
	@ApiOperation(value = "네트워크 템플릿 목록", notes = "선택된 네트워크의 템플릿 목록을 조회한다")
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{networkId}/templates")
	@ResponseBody
	fun findAllTemplatesFromNetwork(
		@PathVariable networkId: String? = null,
	): ResponseEntity<List<NetworkTemplateVo>> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		log.info("--- Network 템플릿")
		return ResponseEntity.ok(iNetwork.findAllTemplatesFromNetwork(networkId))
	}
/*
	@ApiOperation(value = "템플릿 제거", notes = "선택된 네트워크의 템플릿 제거한다")
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="templateId", value="탬플릿 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="nicId", value="네트워크 인터페이스 컨트롤러 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@DeleteMapping("/{networkId}/templates/{templateId}/{nicId}")
	@ResponseBody
	fun deleteTempNic(
		@PathVariable networkId: String? = null,
		@PathVariable templateId: String? = null,
		@PathVariable nicId: String? = null
	): ResponseEntity<Boolean> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		if (templateId.isNullOrEmpty())
			throw ErrorPattern.TEMPLATE_ID_NOT_FOUND.toException()
		if (nicId.isNullOrEmpty())
			throw ErrorPattern.NIC_ID_NOT_FOUND.toException()
		log.info("--- Network 템플릿 nic 제거")
		return ResponseEntity.ok(iNetwork.deleteTemplateNicFromNetwork(templateId, nicId) }
	}
*/
	@ApiOperation(value = "네트워크 권한 목록", notes = "선택된 네트워크의 권한 목록을 조회한다")
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{networkId}/permissions")
	@ResponseBody
	fun permission(
		@PathVariable networkId: String? = null,
	): ResponseEntity<List<PermissionVo>> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		log.info("--- Network 권한")
		return ResponseEntity.ok(iNetwork.findAllPermissionsFromNetwork(networkId))
	}

	companion object {
		private val log by LoggerDelegate()
	}

}