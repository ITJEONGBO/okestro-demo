package com.itinfo.itcloud.controller.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.controller.BaseController
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.itcloud.model.computing.ClusterVo
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.computing.HostVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.network.VnicProfileVo
import com.itinfo.itcloud.model.network.NetworkTemplateVo
import com.itinfo.itcloud.model.network.OpenStackNetworkVo
import com.itinfo.itcloud.service.network.ItNetworkService
import com.itinfo.itcloud.service.network.ItVnicProfileService
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
		value="네트워크 목록 조회",
		notes="전체 네트워크 목록을 조회한다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun networks(): ResponseEntity<List<NetworkVo>> {
		log.info("/networks ... 네트워크 목록")
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
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{networkId}")
	@ResponseBody
	fun network(
		@PathVariable networkId: String? = null
	): ResponseEntity<NetworkVo?> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		log.info("/networks/{} ... 네트워크 상세정보", networkId)
		return ResponseEntity.ok(iNetwork.findOne(networkId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="네트워크 생성",
		notes="네트워크를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="network", value="네트워크", dataTypeClass=NetworkVo::class, required=true, paramType="body")
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun add(
		@RequestBody network: NetworkVo? = null
	): ResponseEntity<NetworkVo?> {
		if (network == null)
			throw ErrorPattern.NETWORK_VO_INVALID.toException()
		log.info("/networks ... 네트워크 생성")
		return ResponseEntity.ok(iNetwork.add(network))
	}

	@ApiOperation(
		httpMethod="PUT",
		value="네트워크 편집",
		notes="네트워크를 편집한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="network", value="네트워크", dataTypeClass=NetworkVo::class, paramType="body")
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@PutMapping("/{networkId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun update(
		@PathVariable networkId: String? = null,
		@RequestBody network: NetworkVo? = null,
	): ResponseEntity<NetworkVo?> {
		log.info("/networks/{} ... 네트워크 편집", networkId)
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		if (network == null)
			throw ErrorPattern.NETWORK_VO_INVALID.toException()
		return ResponseEntity.ok(iNetwork.update(network))
	}

	@ApiOperation(
		httpMethod="DELETE",
		value="네트워크 삭제",
		notes="네트워크를 삭제한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@DeleteMapping("/{networkId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun remove(
		@PathVariable networkId: String? = null,
	): ResponseEntity<Boolean> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		log.info("/networks/{} ... 네트워크 삭제", networkId)
		return ResponseEntity.ok(iNetwork.remove(networkId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="네트워크 가져오기 창",
		notes="네트워크 가져오기 창 - 네트워크 공급자 목록을 가져온다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/import/settings")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun importProviders(): ResponseEntity<IdentifiedVo?> {
		log.info("/networks/import/settings ... 네트워크 가져오기 창")
		return ResponseEntity.ok(iNetwork.findNetworkProviderFromNetwork())
	}

	@ApiOperation(
		httpMethod="GET",
		value="네트워크 가져오기 창",
		notes="네트워크 가져오기 창 - 네트워크 공급자가 가지고 있는 네트워크 목록을 가져온다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="providerId", value="공급자 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/import/settings/{providerId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun providers(
		@PathVariable providerId: String? = null
	): ResponseEntity<List<OpenStackNetworkVo?>> {
		if (providerId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_NOT_FOUND.toException()
		log.info("/networks/import/setting/{} ... 네트워크 가져오기 창", providerId)
		return ResponseEntity.ok(iNetwork.findAllOpenStackNetworkFromNetworkProvider(providerId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="네트워크 가져오기 창",
		notes="네트워크 가져오기 창 - 네트워크를 가져올 수 있는 데이터센터 목록"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="openstackNetworkId", value="공급자 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/import/datacenters/{openstackNetworkId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun providersDatacenter(
		@PathVariable openstackNetworkId: String? = null
	): ResponseEntity<List<DataCenterVo?>> {
		if (openstackNetworkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_NOT_FOUND.toException()
		log.info("/networks/import/settingDatacenter/{} ... 네트워크 가져오기 창", openstackNetworkId)
		return ResponseEntity.ok(iNetwork.findAllDataCentersFromNetwork(openstackNetworkId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="네트워크 가져오기",
		notes="네트워크를 가져온다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="openstackNetwork", value="네트워크", dataTypeClass=Array<OpenStackNetworkVo>::class, required=true, paramType="body")
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/import")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun import(
		@RequestBody openStackNetworkVos: List<OpenStackNetworkVo>? = null
	): ResponseEntity<Boolean?> {
		if (openStackNetworkVos == null)
			throw ErrorPattern.NETWORK_VO_INVALID.toException()
		log.info("/networks/import ... 네트워크 가져오기")
		return ResponseEntity.ok(iNetwork.importNetwork(openStackNetworkVos))
	}


	@ApiOperation(
		httpMethod="GET",
		value="네트워크 클러스터 목록",
		notes="선택된 네트워크의 클러스터 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{networkId}/clusters")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun clusters(
		@PathVariable networkId: String? = null,
	): ResponseEntity<List<ClusterVo>> {
		log.info("/networks/{}/clusters ... 네트워크 클러스터 목록", networkId)
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iNetwork.findAllClustersFromNetwork(networkId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="네트워크 호스트 목록",
		notes="선택된 네트워크의 호스트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{networkId}/hosts")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun hosts(
		@PathVariable networkId: String? = null,
	): ResponseEntity<List<HostVo>> {
		log.info("/networks/{}/hosts ... 네트워크 호스트 목록", networkId)
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		return ResponseEntity.ok(iNetwork.findAllHostsFromNetwork(networkId))
	}

	@ApiOperation(
		httpMethod="GET",
		value = "네트워크 가상머신 목록",
		notes = "선택된 네트워크의 가상머신 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{networkId}/vms")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun vms(
		@PathVariable networkId: String? = null,
	): ResponseEntity<List<VmVo>> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		log.info("/networks/{}/vms ... 네트워크 가상머신 목록", networkId)
		return ResponseEntity.ok(iNetwork.findAllVmsFromNetwork(networkId))
	}

	@ApiOperation(
		httpMethod="GET",
		value = "네트워크 템플릿 목록",
		notes = "선택된 네트워크의 템플릿 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{networkId}/templates")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun templates(
		@PathVariable networkId: String? = null,
	): ResponseEntity<List<NetworkTemplateVo>> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		log.info("/networks/{}/templates ... 네트워크 템플릿 목록", networkId)
		return ResponseEntity.ok(iNetwork.findAllTemplatesFromNetwork(networkId))
	}

	@ApiOperation(
		httpMethod="GET",
		value = "네트워크 권한 목록",
		notes = "선택된 네트워크의 권한 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{networkId}/permissions")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@Deprecated("사용안함")
	fun permissions(
		@PathVariable networkId: String? = null,
	): ResponseEntity<List<PermissionVo>> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		log.info("/networks/{}/permissions ... 네트워크 권한 목록", networkId)
		return ResponseEntity.ok(iNetwork.findAllPermissionsFromNetwork(networkId))
	}


	// region: vnicProfile
	@Autowired private lateinit var iVnic: ItVnicProfileService

	
	@ApiOperation(
		httpMethod="GET",
		value="전체 vnicProfile 목록 조회",
		notes="전체 vnicProfile 목록을 조회한다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/vnicProfiles")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun vnicProfiles(): ResponseEntity<List<VnicProfileVo>> {
		log.info("/vnicProfiles ... 네트워크 목록")
		return ResponseEntity.ok(iVnic.findAll())
	}

	@ApiOperation(
		httpMethod="GET",
		value="네트워크 vnic 프로파일 목록",
		notes="선택된 네트워크의 vnic 프로파일 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{networkId}/vnicProfiles")
	@ResponseBody
	fun vnics(
		@PathVariable networkId: String? = null,
	): ResponseEntity<List<VnicProfileVo>> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		log.info("/networks/{}/vnicProfiles ... 네트워크 vnic profile 목록", networkId)
		return ResponseEntity.ok(iVnic.findAllFromNetwork(networkId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="네트워크 vnic 프로파일",
		notes="선택된 네트워크의 vnic 프로파일의 상세정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="vnicProfileId", value="Vnic Profile ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{networkId}/vnicProfiles/{vnicProfileId}")
	@ResponseBody
	fun vnic(
		@PathVariable networkId: String? = null,
		@PathVariable vnicProfileId: String? = null,
	): ResponseEntity<VnicProfileVo?> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		if (vnicProfileId.isNullOrEmpty())
			throw ErrorPattern.VNIC_PROFILE_ID_NOT_FOUND.toException()
		log.info("/networks/{}/vnicProfiles/{} ... 네트워크 vnic profile", networkId, vnicProfileId)
		return ResponseEntity.ok(iVnic.findOne(vnicProfileId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="vnic 프로파일 생성",
		notes="vnic 프로파일을 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="vnicProfile", value="Vnic Profile", dataTypeClass=VnicProfileVo::class, required=true, paramType="body")
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{networkId}/vnicProfiles")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun addVnic(
		@PathVariable networkId: String? = null,
		@RequestBody vnicProfile: VnicProfileVo? = null
	): ResponseEntity<VnicProfileVo?> {
		if (vnicProfile == null)
			throw ErrorPattern.VNIC_PROFILE_VO_INVALID.toException()
		log.info("/networks/{}/vnicProfiles ... vnicProfile 생성", networkId)
		return ResponseEntity.ok(iVnic.add(vnicProfile))
	}

	@ApiOperation(
		httpMethod="PUT",
		value="vnic 프로파일 편집",
		notes="vnic 프로파일을 편집한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="vnicProfileId", value="Vnic Profile ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="vnicProfile", value="Vnic Profile", dataTypeClass=VnicProfileVo::class, paramType="body")
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@PutMapping("/{networkId}/vnicProfiles/{vnicProfileId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun updateVnic(
		@PathVariable networkId: String? = null,
		@PathVariable vnicProfileId: String? = null,
		@RequestBody vnicProfile: VnicProfileVo? = null,
	): ResponseEntity<VnicProfileVo?> {
		log.info("/networks/{}/vnicProfiles/{} ... vnicProfile 생성", networkId, vnicProfileId)
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		if (vnicProfileId.isNullOrEmpty())
			throw ErrorPattern.VNIC_PROFILE_ID_NOT_FOUND.toException()
		if (vnicProfile == null)
			throw ErrorPattern.VNIC_PROFILE_NOT_FOUND.toException()
		return ResponseEntity.ok(iVnic.update(vnicProfile))
	}

	@ApiOperation(
		httpMethod="DELETE",
		value="vnic 프로파일 삭제",
		notes="vnic 프로파일을 삭제한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="networkId", value="네트워크 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="vnicProfileId", value="Vnic Profile ID", dataTypeClass=String::class, required=true, paramType="path")
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@DeleteMapping("/{networkId}/vnicProfiles/{vnicProfileId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun removeVnic(
		@PathVariable networkId: String? = null,
		@PathVariable vnicProfileId: String? = null,
	): ResponseEntity<Boolean> {
		if (networkId.isNullOrEmpty())
			throw ErrorPattern.NETWORK_ID_NOT_FOUND.toException()
		if (vnicProfileId.isNullOrEmpty())
			throw ErrorPattern.VNIC_PROFILE_ID_NOT_FOUND.toException()
		log.info("/{}/vnicProfiles/{} ... vnic 프로파일 삭제", networkId, vnicProfileId)
		return ResponseEntity.ok(iVnic.remove(vnicProfileId))
	}


	// endregion

	companion object {
		private val log by LoggerDelegate()
	}

}