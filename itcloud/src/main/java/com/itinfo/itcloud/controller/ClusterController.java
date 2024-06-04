package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.service.ItClusterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/computing")
public class ClusterController {
	private final ItClusterService clusterService;


	// 목록
	@GetMapping("/clusters")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<ClusterVo> clusters() {
		log.info("--- Cluster 목록");
		return clusterService.getList();
	}

	// 클러스터 생성 창
	@GetMapping("/cluster/settings")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<DataCenterVo> setDatacenterList(){
		log.info("--- Cluster 데이터센터 생성 창");
		return clusterService.setDatacenterList();
	}

	@GetMapping("/cluster/settings/{dcId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<NetworkVo> setNetworkList(@PathVariable String dcId){
		log.info("--- Cluster 네트워크 생성 창");
		return clusterService.setNetworkList(dcId);
	}



	// 클러스터 생성
	@PostMapping("/cluster")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> addCluster(@RequestBody ClusterCreateVo cVo){
		log.info("--- Cluster 생성");
		return clusterService.addCluster(cVo);
	}

	// 클러스터 편집 창
	@GetMapping("/cluster/{id}/settings")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public ClusterCreateVo setEditCluster(@PathVariable String id){
		log.info("--- Cluster 편집 창");
		return clusterService.setCluster(id);
	}
	
	// 클러스터 편집
	@PutMapping("/cluster/{id}")
	@ResponseBody
	public CommonVo<Boolean> editCluster(@PathVariable String id,
										 @RequestBody ClusterCreateVo cVo){
		log.info("--- Cluster 편집");
		return clusterService.editCluster(id, cVo);
	}

	// 클러스터 삭제
	@DeleteMapping("/cluster/{id}")
	@ResponseBody
	public CommonVo<Boolean> deleteCluster(@PathVariable String id){
		log.info("--- Cluster 삭제");
		return clusterService.deleteCluster(id);
	}



	// 일반 id 
	@GetMapping("/cluster/{id}")
	@ResponseBody
	public ClusterVo cluster(@PathVariable String id) {
		log.info("--- Cluster 일반");
		return clusterService.getInfo(id);
	}

	// 네트워크 목록
	@GetMapping("/cluster/{id}/networks")
	@ResponseBody
	public List<NetworkVo> network(@PathVariable String id) {
		log.info("--- Cluster 네트워크");
		return clusterService.getNetwork(id);
	}

	// 호스트 목록
	@GetMapping("/cluster/{id}/hosts")
	@ResponseBody
	public List<HostVo> host(@PathVariable String id) {
		log.info("--- Cluster 호스트");
		return clusterService.getHost(id);
	}

	// 가상머신 목록
	@GetMapping("/cluster/{id}/vms")
	@ResponseBody
	public List<VmVo> vm(@PathVariable String id) {
		log.info("--- Cluster 가상머신");
		return clusterService.getVm(id);
	}




	// 선호도 그룹 생성
//	@PostMapping("/cluster/{id}/affinitygroup")
//	@ResponseBody
//	@ResponseStatus(HttpStatus.CREATED)
//	public CommonVo<Boolean> addAffinitygroup(@PathVariable String id,
//											  @RequestBody AffinityGroupCreateVo agVo){
//		log.info("--- Cluster 선호도 그룹 생성");
//		return clusterService.addAffinitygroup(id, agVo);
//	}

	// 선호도 그룹 편집 창
//	@GetMapping("/cluster/{id}/affinitygroup/{agId}/settings")
//	@ResponseBody
//	public AffinityGroupCreateVo setEditAffinitygroup(@PathVariable String id,
//													  @PathVariable String agId){
//		log.info("--- Cluster 선호도 그룹 편집 창");
//		return clusterService.setEditAffinitygroup(id, agId);
//	}

	// 선호도 그룹 편집
//	@PutMapping("/cluster/{id}/affinitygroup/{agId}")
//	@ResponseBody
//	@ResponseStatus(HttpStatus.CREATED)
//	public CommonVo<Boolean> editAffinitygroup(@PathVariable String id,
//											   @PathVariable String agId,
//											   @RequestBody AffinityGroupCreateVo agVo){
//		log.info("--- Cluster 선호도 그룹 편집");
//		return clusterService.editAffinitygroup(id, agId,agVo);
//	}

	// 선호도 그룹 삭제
//	@DeleteMapping("/cluster/{id}/affinitygroup/{agId}")
//	@ResponseBody
//	public CommonVo<Boolean> deleteAffinitygroup(@PathVariable String id,
//												 @PathVariable String agId){
//		log.info("--- Cluster 선호도 그룹 삭제");
//		return clusterService.deleteAffinitygroup(id, agId);
//	}


	// 선호도 레이블 목룍
	@GetMapping("/cluster/{id}/affinitylabels")
	@ResponseBody
	public List<AffinityLabelVo> affLabel(@PathVariable String id) {
		log.info("--- Cluster 선호도 레이블");
		return clusterService.getAffinitylabelList(id);
	}

	// 선호도 레이블 생성위한 목록 \ 문제있음요
//	@GetMapping("/cluster/{id}/affinitylabel/settings")
//	@ResponseBody
//	public AffinityHostVm setAffinitylabel(@PathVariable String id){
//		log.info("--- Cluster 선호도 레이블 생성 창");
//		return clusterService.setAffinityDefault(id, "label");
//	}


	// 선호도 레이블 생성
	@PostMapping("/cluster/{id}/affinitylabel")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> addAff(@PathVariable String id,
									@RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- Cluster 선호도 레이블 생성");
		return clusterService.addAffinitylabel(id, alVo);
	}

	// 선호도 레이블 편집 창
	@GetMapping("/cluster/{id}/affinitylabel/{alId}")
	@ResponseBody
	public AffinityLabelCreateVo getAffinityLabel(@PathVariable String id,
												  @PathVariable String alId){
		log.info("--- Cluster 선호도 레이블 편집창");
		return clusterService.getAffinityLabel(id, alId);
	}


	// 선호도 레이블 편집
	@PutMapping("/cluster/{id}/affinitylabel/{alId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> editAff(@PathVariable String id,
									 @PathVariable String alId,
									 @RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- Cluster 선호도 레이블 편집");
		return clusterService.editAffinitylabel(id, alId, alVo);
	}

	// 선호도 레이블 삭제
	@PostMapping("/cluster/{id}/affinitylabel/{alId}")
	@ResponseBody
	public CommonVo<Boolean> deleteAff(@PathVariable String id,
									   @PathVariable String alId) {
		log.info("--- Cluster 선호도 레이블 삭제");
		return clusterService.deleteAffinitylabel(id, alId);
	}


	// 권한
	@GetMapping("/cluster/{id}/permissions")
	@ResponseBody
	public List<PermissionVo> permission(@PathVariable String id) {
		log.info("--- Cluster 권한");
		return clusterService.getPermission(id);
	}

	// 이벤트
	@GetMapping("/cluster/{id}/events")
	@ResponseBody
	public List<EventVo> event(@PathVariable String id) {
		log.info("--- Cluster 이벤트");
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
