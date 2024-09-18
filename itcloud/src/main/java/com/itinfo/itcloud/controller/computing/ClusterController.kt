package com.itinfo.itcloud.controller.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.controller.BaseController
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.*
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.service.computing.ItClusterService
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@Api(tags = ["Computing", "Cluster"])
@RequestMapping("/api/v1/computing/clusters")
class ClusterController: BaseController() {
	@Autowired private lateinit var iCluster: ItClusterService

	@ApiOperation(
		httpMethod="GET",
		value="클러스터 목록 조회",
		notes="전체 클러스터 목록을 조회한다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAll(): ResponseEntity<List<ClusterVo>> {
		log.info("/computing/clusters ... 클러스터 목록")
		return ResponseEntity.ok(iCluster.findAll())
	}


	@ApiOperation(
		httpMethod="GET",
		value="클러스터의 정보 상세조회",
		notes="선택된 클러스터의 정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{clusterId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findOne(
		@PathVariable clusterId: String? = null
	): ResponseEntity<ClusterVo?> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("/computing/clusters/{} ... 클러스터 상세정보", clusterId)
		return ResponseEntity.ok(iCluster.findOne(clusterId))
	}

	// 클러스터 생성창 - 데이터센터 목록 [ItDataCenterController.findAll] 쓰기
	// 클러스터 생성창 - 네트워크 목록 [ItDataCenterController.findAllNetworksFromDataCenter] 쓰기


	@ApiOperation(
		httpMethod="POST",
		value="클러스터 생성",
		notes="클러스터를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="cluster", value="클러스터", dataTypeClass=ClusterVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun add(
		@RequestBody cluster: ClusterVo? = null
	): ResponseEntity<ClusterVo?> {
		if (cluster == null)
			throw ErrorPattern.CLUSTER_VO_INVALID.toException()
		log.info("/computing/clusters ... 클러스터 생성\n{}", cluster)
		return ResponseEntity.ok(iCluster.add(cluster))
	}


	@ApiOperation(
		httpMethod="PUT",
		value="클러스터 편집",
		notes="클러스터를 편집한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="cluster", value="클러스터", dataTypeClass=ClusterVo::class, paramType="body")
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@PutMapping("/{clusterId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun update(
		@PathVariable clusterId: String? = null,
		@RequestBody cluster: ClusterVo? = null,
	): ResponseEntity<ClusterVo?> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		if (cluster == null)
			throw ErrorPattern.CLUSTER_VO_INVALID.toException()
		log.info("/computing/clusters/{} ... 클러스터 편집\n{}", clusterId, cluster)
		return ResponseEntity.ok(iCluster.update(cluster))
	}


	@ApiOperation(
		httpMethod="DELETE",
		value="클러스터 삭제",
		notes="클러스터를 삭제한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@DeleteMapping("/{clusterId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun remove(
		@RequestBody clusterId: String? = null,
	): ResponseEntity<Boolean> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("/computing/clusters/{} ... 클러스터 삭제", clusterId)
		return ResponseEntity.ok(iCluster.remove(clusterId))
	}


	@ApiOperation(
		httpMethod="GET",
		value="클러스터 네트워크 목록조회",
		notes="클러스터의 네트워크 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{clusterId}/networks")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAllNetworksFromCluster(
		@PathVariable clusterId: String? = null
	): ResponseEntity<List<NetworkVo>> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("--- 클러스터 네트워크 목록")
		return ResponseEntity.ok(iCluster.findAllNetworksFromCluster(clusterId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="클러스터 네트워크 생성",
		notes="클러스터 네트워크를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="network", value="네트워크", dataTypeClass=NetworkVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping("/{clusterId}/networks")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun addNetworkFromCluster(
		@PathVariable clusterId: String? = null,
		@RequestBody network: NetworkVo? = null
	): ResponseEntity<NetworkVo?> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		if (network == null)
			throw ErrorPattern.CLUSTER_VO_INVALID.toException()
		log.info("/computing/clusters/{}/addNetworkFromCluster ... 클러스터 네트워크 생성\n{}", clusterId, network)
		return ResponseEntity.ok(iCluster.addNetworkFromCluster(clusterId, network))
	}
	// findAllManageNetworksFromCluster
	// manageNetworksFromCluster


	@ApiOperation(
		httpMethod="GET",
		value="클러스터 호스트 목록",
		notes="클러스터의 호스트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{clusterId}/hosts")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAllHostsFromCluster(
		@PathVariable clusterId: String? = null
	): ResponseEntity<List<HostVo>> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("--- 클러스터 내 호스트 목록")
		return ResponseEntity.ok(iCluster.findAllHostsFromCluster(clusterId))
	}


	@ApiOperation(
		httpMethod="GET",
		value="클러스터 가상머신 목록조회",
		notes="클러스터의 가상머신 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{clusterId}/vms")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAllVmsFromCluster(
		@PathVariable clusterId: String? = null
	): ResponseEntity<List<VmVo>> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("--- 클러스터 가상머신 목록")
		return ResponseEntity.ok(iCluster.findAllVmsFromCluster(clusterId))
	}


	@ApiOperation(
		httpMethod="GET",
		value="클러스터 Cpu Profile 목록",
		notes="선택된 클러스터의 Cpu Profile 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{clusterId}/cpuProfiles")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAllCpuProfilesFromCluster(
		@PathVariable clusterId: String? = null
	): ResponseEntity<List<CpuProfileVo>> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("--- 클러스터 Cpu Profile")
		return ResponseEntity.ok(iCluster.findAllCpuProfilesFromCluster(clusterId))
	}


	@ApiOperation(
		httpMethod="GET",
		value="클러스터 권한 목록",
		notes="선택된 클러스터의 권한 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{clusterId}/permissions")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAllPermissionsFromCluster(
		@PathVariable clusterId: String? = null
	): ResponseEntity<List<PermissionVo>> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("--- 클러스터 권한")
		return ResponseEntity.ok(iCluster.findAllPermissionsFromCluster(clusterId))
	}


	@ApiOperation(
		httpMethod="GET",
		value="클러스터 이벤트 목록",
		notes="선택된 클러스터의 이벤트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{clusterId}/events")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAllEventsFromCluster(@PathVariable clusterId: String): ResponseEntity<List<EventVo>> {
		log.info("--- 클러스터 이벤트")
		return ResponseEntity.ok(iCluster.findAllEventsFromCluster(clusterId))
	}

	companion object {
		private val log by LoggerDelegate()
	}
}