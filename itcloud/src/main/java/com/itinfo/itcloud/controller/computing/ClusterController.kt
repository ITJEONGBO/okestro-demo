package com.itinfo.itcloud.controller.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.controller.BaseController
import com.itinfo.itcloud.error.toException
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.itcloud.model.computing.ClusterVo
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.computing.HostVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.computing.EventVo
import com.itinfo.itcloud.model.response.Res
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.service.computing.ItAffinityService
import com.itinfo.itcloud.service.computing.ItClusterService
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@Api(tags = ["Computing-Cluster"])
@RequestMapping("/computing/clusters")
class ClusterController: BaseController() {
	@Autowired private lateinit var iCluster: ItClusterService
	@Autowired private lateinit var iAffinity: ItAffinityService

	@ApiOperation(
		httpMethod="GET",
		value="클러스터 목록 조회",
		notes="전체 클러스터 목록을 조회한다")
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAll(): Res<List<ClusterVo>> {
		log.info("/computing/clusters ... 클러스터 목록")
		return Res.safely { iCluster.findAll() }
	}

	@ApiOperation(
		httpMethod="GET",
		value="/computing/clusters/settings",
		notes="클러스터 생성창(데이터센터) > 클러스터 생성시 필요한 데이터센터 목록을 조회한다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/settings")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	// TODO: 데이터센터와의 관계 확인 후 이동
	fun findAllDataCentersFromCluster(
	): Res<List<DataCenterVo>> {
		log.info("/computing/clusters/settings ... 클러스터 생성 창: 데이터센터 목록")
		return Res.safely { iCluster.findAllDataCentersFromCluster() }
	}

	@ApiOperation(
		httpMethod="GET",
		value="/computing/clusters/settings/{clusterId}",
		notes="클러스터 생성창(네트워크) > 클러스터 생성 시 필요한 네트워크 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@GetMapping("/settings/{clusterId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	// TODO: 데이터센터와의 관계 확인 후 이동
	fun findAllNetworksFromDataCenter(
		@PathVariable clusterId: String? = null
	): Res<List<NetworkVo>> {
		if (clusterId == null)
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("/computing/clusters/settings/{} ... 클러스터 생성 창: 네트워크 목록", clusterId)
		return Res.safely { iCluster.findAllNetworksFromDataCenter(clusterId) }
	}

	@ApiOperation(
		httpMethod="POST",
		value="/computing/clusters",
		notes="클러스터 생성 > 클러스터를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="cluster", value="클러스터", dataTypeClass=ClusterVo::class),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun create(
		@RequestBody cluster: ClusterVo? = null
	): Res<ClusterVo?> {
		if (cluster == null)
			throw ErrorPattern.CLUSTER_VO_INVALID.toException()
		log.info("/computing/clusters ... 클러스터 생성\n{}", cluster)
		return Res.safely { iCluster.add(cluster) }
	}
	

	@ApiOperation(
		httpMethod="GET",
		value="/computing/clusters/{clusterId}/edit",
		notes = "클러스터 수정창 > 선택된 클러스터의 정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{clusterId}/edit")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun getCluster(
		@PathVariable clusterId: String? = null,
	): Res<ClusterVo?> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("/computing/clusters/{}/edit ... 클러스터 편집 창", clusterId)
		return Res.safely { iCluster.findOne(clusterId) }
	}

	@ApiOperation(
		httpMethod="PUT",
		value="/computing/clusters/{clusterId}",
		notes="클러스터 수정 > 클러스터를 수정한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@PutMapping("/{clusterId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun editCluster(
		@PathVariable clusterId: String? = null,
		@RequestBody clusterVo: ClusterVo? = null, // TODO: clusterId를 clusterVo에 포함시켜야 할거 같음?
	): Res<ClusterVo?> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		if (clusterVo == null)
			throw ErrorPattern.CLUSTER_VO_INVALID.toException()
		log.info("/computing/clusters/{} ... 클러스터 편집\n{}", clusterId, clusterVo)
		return Res.safely { iCluster.update(clusterVo) }
	}


	@ApiOperation(
		httpMethod="DELETE",
		value="/computing/clusters/{clusterIds}",
		notes="클러스터 삭제 > 클러스터를 삭제한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@DeleteMapping("/{clusterIds}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun remove(
		@RequestBody clusterId: String? = null,
	): Res<Boolean> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("/computing/clusters/{} ... 클러스터 삭제", clusterId)
		return Res.safely { iCluster.remove(clusterId) }
	}

	@ApiOperation(
		httpMethod="GET",
		value="클러스터 상세정보 조회",
		notes="클러스터 상세정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{clusterId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findOne(
		@PathVariable clusterId: String? = null
	): Res<ClusterVo?> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("/computing/clusters/{} ... 클러스터 상세정보", clusterId)
		return Res.safely { iCluster.findOne(clusterId) }
	}

	@ApiOperation(
		httpMethod="GET",
		value="클러스터 내 네트워크 목록조회",
		notes="클러스터의 네트워크 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{clusterId}/networks")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAllNetworksFromCluster(
		@PathVariable clusterId: String? = null
	): Res<List<NetworkVo>> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("--- 클러스터 네트워크 목록")
		return Res.success { iCluster.findAllNetworksFromCluster(clusterId) }
	}

	@ApiOperation(
		httpMethod="GET",
		value="클러스터 내 호스트 목록",
		notes="클러스터의 호스트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{clusterId}/hosts")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAllHostsFromCluster(
		@PathVariable clusterId: String? = null
	): Res<List<HostVo>> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("--- 클러스터 내 호스트 목록")
		return Res.safely { iCluster.findAllHostsFromCluster(clusterId) }
	}


	@ApiOperation(
		httpMethod="GET",
		value="클러스터 내 가상머신 목록조회",
		notes="클러스터의 가상머신 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{clusterId}/vms")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAllVmsFromCluster(
		@PathVariable clusterId: String? = null
	): Res<List<VmVo>> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("--- 클러스터 가상머신 목록")
		return Res.safely { iCluster.findAllVmsFromCluster(clusterId) }
	}

/*
	@ApiOperation(
		httpMethod="POST"
		value="클러스터 선호도 그룹 생성",
		notes="선택된 클러스터의 선호도 그룹을 생성한다"
	)
	@ApiImplicitParam(name = "id", value = "클러스터 아이디", dataTypeClass = String.class)
	@PostMapping("/{clusterId}/affinitygroups")
	@ResponseBody
    fun addAffinitygroup(
        @PathVariable clusterId: String,
		@RequestBody AffinityGroupCreateVo agVo
	) {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("--- 클러스터 선호도 그룹 생성");
		return affinityService.addAffinityGroup(id, true, agVo);
	}

	//	@GetMapping("/{id}/affinitygroups/{agId}/settings")
	//	@ResponseBody
	//	public AffinityGroupCreateVo setEditAffinitygroup(@PathVariable String id,
	//													  @PathVariable String agId){
	//		log.info("--- 클러스터 선호도 그룹 편집 창");
	//		return affinityService.setAffinityGroup(id, "cluster", agId);
	//	}
	//
	//	@PutMapping("/{id}/affinitygroups/{agId}")
	//	@ResponseBody
	//	public CommonVo<Boolean> editAffinitygroup(@PathVariable String id,
	//											   @RequestBody AffinityGroupCreateVo agVo){
	//		log.info("--- 클러스터 선호도 그룹 편집");
	//		return affinityService.editAffinityGroup(id, agVo);
	//	}
	//
	//	@DeleteMapping("/{id}/affinitygroups/{agId}")
	//	@ResponseBody
	//	public CommonVo<Boolean> deleteAffinitygroup(@PathVariable String id,
	//												 @PathVariable String agId){
	//		log.info("--- 클러스터 선호도 그룹 삭제");
	//		return affinityService.deleteAffinityGroup(id, "cluster",agId);
	//	}

	@ApiOperation(
		httpMethod="GET",
		value="/computing/clusters/{clusterId}/affinitylabels",
		notes="클러스터 선호도 레이블 목록 > 클러스터의 선호도 레이블 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name = "clusterId", value = "클러스터 아이디", dataTypeClass = String::class)
	)
	@GetMapping("/{clusterId}/affinitylabels")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun affLabel(): List<AffinityLabelVo> {
		log.info("--- 클러스터 선호도 레이블 목록")
		return affinity.findAllAffinityLabels()
	}

	//	@PostMapping("/cluster/{id}/affinitylabel")
	//	@ResponseBody
	//	@ResponseStatus(HttpStatus.CREATED)
	//	public CommonVo<Boolean> addAff(@PathVariable String id,
	//									@RequestBody AffinityLabelCreateVo alVo) {
	//		log.info("--- 클러스터 선호도 레이블 생성");
	//		return clusterService.addAffinitylabel(id, alVo);
	//	}
	//
	//	@GetMapping("/cluster/{id}/affinitylabel/{alId}")
	//	@ResponseBody
	//	@ResponseStatus(HttpStatus.OK)
	//	public AffinityLabelCreateVo getAffinityLabel(@PathVariable String id,
	//												  @PathVariable String alId){
	//		log.info("--- 클러스터 선호도 레이블 편집창");
	//		return clusterService.getAffinityLabel(id, alId);
	//	}
	//
	//	@PutMapping("/cluster/{id}/affinitylabel/{alId}")
	//	@ResponseBody
	//	@ResponseStatus(HttpStatus.CREATED)
	//	public CommonVo<Boolean> editAff(@PathVariable String id,
	//									 @PathVariable String alId,
	//									 @RequestBody AffinityLabelCreateVo alVo) {
	//		log.info("--- 클러스터 선호도 레이블 편집");
	//		return clusterService.editAffinitylabel(id, alId, alVo);
	//	}
	//
	//	@PostMapping("/cluster/{id}/affinitylabel/{alId}")
	//	@ResponseBody
	//	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> deleteAff(@PathVariable String id,
									   @PathVariable String alId) {
		log.info("--- 클러스터 선호도 레이블 삭제");
		return clusterService.deleteAffinitylabel(id, alId);
	}
*/
	@ApiOperation(
		httpMethod="GET",
		value="/computing/clusters/{clusterId}/permissions",
		notes="클러스터 권한 목록 > 선택된 클러스터의 권한 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{clusterId}/permissions")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun permission(
		@PathVariable clusterId: String? = null
	): Res<List<PermissionVo>> {
		if (clusterId.isNullOrEmpty())
			throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()
		log.info("--- 클러스터 권한")
		return Res.safely { iCluster.findAllPermissionsFromCluster(clusterId) }
	}


	@ApiOperation(
		httpMethod="GET",
		value="/computing/clusters/{clusterId}/events",
		notes="클러스터 이벤트 목록 > 선택된 클러스터의 이벤트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="clusterId", value="클러스터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{clusterId}/events")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun event(@PathVariable clusterId: String): Res<List<EventVo>> {
		log.info("--- 클러스터 이벤트")
		return Res.safely { iCluster.findAllEventsFromCluster(clusterId) }
	}

	companion object {
		private val log by LoggerDelegate()
	}
}