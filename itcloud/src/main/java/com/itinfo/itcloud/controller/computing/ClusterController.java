package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.service.ItAffinityService;
import com.itinfo.itcloud.service.ItClusterService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@Api(tags = "cluster")
@RequestMapping("/computing/clusters")
public class ClusterController {
	private final ItClusterService clusterService;
	private final ItAffinityService affinityService;


	@GetMapping("")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)

	public List<ClusterVo> clusters() {
		log.info("--- 클러스터 목록");
		return clusterService.getList();
	}

	@GetMapping("/settings")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<DataCenterVo> setDatacenterList(){
		log.info("--- 클러스터 생성 창: 데이터센터 목록");
		return clusterService.setDatacenterList();
	}

	@GetMapping("/settings/{dcId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<NetworkVo> setNetworkList(@PathVariable String dcId){
		log.info("--- 클러스터 생성 창: 네트워크 목록");
		return clusterService.setNetworkList(dcId);
	}



	@PostMapping("")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> addCluster(@RequestBody ClusterCreateVo cVo){
		log.info("--- 클러스터 생성");
		return clusterService.addCluster(cVo);
	}

	@GetMapping("/{id}/settings")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ClusterCreateVo setEditCluster(@PathVariable String id){
		log.info("--- 클러스터 편집 창");
		return clusterService.setCluster(id);
	}
	
	@PutMapping("/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> editCluster(@PathVariable String id,
										 @RequestBody ClusterCreateVo cVo){
		log.info("--- 클러스터 편집");
		return clusterService.editCluster(cVo);
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> deleteCluster(@PathVariable String id){
		log.info("--- 클러스터 삭제");
		return clusterService.deleteCluster(id);
	}




	@GetMapping("/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ClusterVo cluster(@PathVariable String id) {
		log.info("--- 클러스터 일반");
		return clusterService.getInfo(id);
	}

	@GetMapping("/{id}/networks")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<NetworkVo> network(@PathVariable String id) {
		log.info("--- 클러스터 네트워크 목록");
		return clusterService.getNetwork(id);
	}

	@GetMapping("/{id}/hosts")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<HostVo> host(@PathVariable String id) {
		log.info("--- 클러스터 호스트 목록");
		return clusterService.getHost(id);
	}

	@GetMapping("/{id}/vms")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<VmVo> vm(@PathVariable String id) {
		log.info("--- 클러스터 가상머신 목록");
		return clusterService.getVm(id);
	}




	@PostMapping("/{id}/affinitygroups")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> addAffinitygroup(@PathVariable String id,
											  @RequestBody AffinityGroupCreateVo agVo){
		log.info("--- 클러스터 선호도 그룹 생성");
		return affinityService.addAffinitygroup(id, "cluster", agVo);
	}

	@GetMapping("/{id}/affinitygroups/{agId}/settings")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public AffinityGroupCreateVo setEditAffinitygroup(@PathVariable String id,
													  @PathVariable String agId){
		log.info("--- 클러스터 선호도 그룹 편집 창");
		return affinityService.setEditAffinitygroup(id, "cluster", agId);
	}

	@PutMapping("/{id}/affinitygroups/{agId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> editAffinitygroup(@PathVariable String id,
											   @RequestBody AffinityGroupCreateVo agVo){
		log.info("--- 클러스터 선호도 그룹 편집");
		return affinityService.editAffinitygroup(id, agVo);
	}

	@DeleteMapping("/{id}/affinitygroups/{agId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> deleteAffinitygroup(@PathVariable String id,
												 @PathVariable String agId){
		log.info("--- 클러스터 선호도 그룹 삭제");
		return affinityService.deleteAffinitygroup(id, "cluster",agId);
	}


	@GetMapping("/{id}/affinitylabels")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<AffinityLabelVo> affLabel() {
		log.info("--- 클러스터 선호도 레이블 목록");
		return affinityService.getAffinitylabel();
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
//	public CommonVo<Boolean> deleteAff(@PathVariable String id,
//									   @PathVariable String alId) {
//		log.info("--- 클러스터 선호도 레이블 삭제");
//		return clusterService.deleteAffinitylabel(id, alId);
//	}



	@GetMapping("/{id}/permissions")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<PermissionVo> permission(@PathVariable String id) {
		log.info("--- 클러스터 권한");
		return clusterService.getPermission(id);
	}

	@GetMapping("/{id}/events")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<EventVo> event(@PathVariable String id) {
		log.info("--- 클러스터 이벤트");
		return clusterService.getEvent(id);
	}








	
	// region: 안쓸 것 같음
//	@GetMapping("/cluster/cpuStatus")
//	@ResponseBody
//	public List<CpuProfileVo> cpu(String id) {
//		log.info("----- 클러스터 cpu 목록 불러오기: " + id);
//		return clusterService.getCpuProfile(id);
//	}
	// endregion

}
