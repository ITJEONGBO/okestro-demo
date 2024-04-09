package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.service.ItClusterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/computing")
public class ClusterController {
	private final ItClusterService clusterService;

	// 클러스터 목록
	@GetMapping("/clusters")
	@ResponseBody
	public List<ClusterVo> clusters() {
		log.info("-----클러스터 목록");
		return clusterService.getList();
	}

	// 클러스터 생성 위해 필요한 데이터센터 리스트 출력
	@GetMapping("/cluster/settings")
	@ResponseBody
	public List<DataCenterVo> setClusterDefaultInfo(){
		log.info("---클러스터 생성 위해 필요한 데이터센터 리스트");
		return clusterService.setClusterDefaultInfo();
	}

	// 클러스터 생성
	@PostMapping("/cluster")
	@ResponseBody
	public CommonVo<Boolean> addCluster(@RequestBody ClusterCreateVo cVo){
		log.info("클러스터 생성");
		return clusterService.addCluster(cVo);
	}

	// 클러스터 수정 창
	@GetMapping("/cluster/{id}/settings")
	@ResponseBody
	public ClusterCreateVo setEditCluster(@PathVariable String id){
		log.info("클러스터 수정창");
		return clusterService.setEditCluster(id);
	}
	
	// 클러스터 수정
	@PutMapping("/cluster/{id}")
	@ResponseBody
	public CommonVo<Boolean> editCluster(@PathVariable String id, @RequestBody ClusterCreateVo cVo){
		log.info("클러스터 수정");
		return clusterService.editCluster(id, cVo);
	}

	// 클러스터 삭제
	@DeleteMapping("/cluster/{id}")
	@ResponseBody
	public CommonVo<Boolean> deleteCluster(@PathVariable String id){
		log.info("클러스터 삭제");
		return clusterService.deleteCluster(id);
	}



	// 클러스터 id 일반
	@GetMapping("/cluster/{id}")
	@ResponseBody
	public ClusterVo cluster(@PathVariable String id) {
		log.info("-----클러스터 일반");
		return clusterService.getInfo(id);
	}

	// 클러스터 네트워크 목록
	@GetMapping("/cluster/{id}/networks")
	@ResponseBody
	public List<NetworkVo> network(@PathVariable String id) {
		log.info("-----클러스터 네트워크");
		return clusterService.getNetwork(id);
	}

	// 클러스터 호스트 목록
	@GetMapping("/cluster/{id}/hosts")
	@ResponseBody
	public List<HostVo> host(@PathVariable String id) {
		log.info("----- 클러스터 호스트");
		return clusterService.getHost(id);
	}

	// 클러스터 가상머신 목록
	@GetMapping("/cluster/{id}/vms")
	@ResponseBody
	public List<VmVo> vm(@PathVariable String id) {
		log.info("----- 클러스터 가상머신");
		return clusterService.getVm(id);
	}

	// 클러스터 선호도 그룹 목록
	@GetMapping("/cluster/{id}/affinitygroups")
	@ResponseBody
	public List<AffinityGroupVo> affGroup(@PathVariable String id) {
		log.info("----- 클러스터 선호도 목록");
		return clusterService.getAffinitygroup(id);
	}

	// TODO: 클러스터 선호도 그룹 생성위한 목록
	@GetMapping("/cluster/{id}/affinitygroup/settings")
	@ResponseBody
	public ClusterAffGroupHostVm setAffinitygroupDefaultInfo(@PathVariable String id){
		return clusterService.setAffinitygroupDefaultInfo(id);
	}

	// 클러스터 선호도 그룹 생성
	@PostMapping("/cluster/{id}/affinitygroup")
	@ResponseBody
	public CommonVo<Boolean> addAffinitygroup(@PathVariable String id,
											  @RequestBody AffinityGroupCreateVo agVo){
		return clusterService.addAffinitygroup(id, agVo);
	}

	// 클러스터 선호도 그룹 편집 창
	@GetMapping("/cluster/{id}/affinitygroup/{agId}/settings")
	@ResponseBody
	public AffinityGroupCreateVo setEditAffinitygroup(@PathVariable String id, @PathVariable String agId){
		return clusterService.setEditAffinitygroup(id, agId);
	}

	// 클러스터 선호도 그룹 편집
	@PutMapping("/cluster/{id}/affinitygroup/{agId}")
	@ResponseBody
	public CommonVo<Boolean> editAffinitygroup(@PathVariable String id,
											   @PathVariable String agId,
											   @RequestBody AffinityGroupCreateVo agVo){
		return clusterService.editAffinitygroup(id, agId,agVo);
	}

	// 클러스터 선호도 그룹 삭제
	@DeleteMapping("/cluster/{id}/affinitygroup/{agId}")
	@ResponseBody
	public CommonVo<Boolean> deleteAffinitygroup(@PathVariable String id, @PathVariable String agId){
		return clusterService.deleteAffinitygroup(id, agId);
	}


	// 클러스터 선호도 레이블 목룍
	@GetMapping("/cluster/{id}/affinitylabels")
	@ResponseBody
	public List<AffinityLabelVo> affLabel(@PathVariable String id) {
		log.info("----- 클러스터 레이블 목록");
		return clusterService.getAffinitylabelList(id);
	}

	// TODO: 클러스트 선호도 레이블 생성위한 목록
	// 문제있음요
	@GetMapping("/cluster/{id}/affinitylabel/settings")
	@ResponseBody
	public AffinityLabelCreateVo getAffinityLabel(@PathVariable String id) {
		log.info("----- 클러스터 선호도 레이블 생성 출력");
		return clusterService.getAffinityLabel(id);
	}

	// 클러스터 선호도 레이블 생성
	@PostMapping("/cluster/{id}/affinitylabel")
	@ResponseBody
	public CommonVo<Boolean> addAff(@PathVariable String id, @RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- 선호도 레이블 생성");
		return clusterService.addAffinitylabel(alVo);
	}

	// 클러스터 선호도 레이블 편집
	@PutMapping("/cluster/{id}/affinitylabel/{alId}")
	@ResponseBody
	public CommonVo<Boolean> editAff(@PathVariable String id,
									 @PathVariable String alId,
									 @RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- 선호도 레이블 편집");
		return clusterService.editAffinitylabel(alVo);
	}

	// 클러스터 선호도 레이블 삭제
	@PostMapping("/cluster/{id}/affinitylabel/{alId}")
	@ResponseBody
	public CommonVo<Boolean> deleteAff(@PathVariable String id, @PathVariable String alId) {
		log.info("--- 선호도 레이블 삭제");
		return clusterService.deleteAffinitylabel(id);
	}



	@GetMapping("/cluster/{id}/permissions")
	@ResponseBody
	public List<PermissionVo> permission(@PathVariable String id) {
		log.info("----- 클러스터 권한");
		return clusterService.getPermission(id);
	}

	@GetMapping("/cluster/{id}/events")
	@ResponseBody
	public List<EventVo> event(@PathVariable String id) {
		log.info("----- 클러스터 이벤트");
		return clusterService.getEvent(id);
	}





	// 해당 cluster가 가지고 있는 host, 레이블 생성시 필요
	@GetMapping("/cluster/hostme")
	@ResponseBody
	public List<HostVo> getHostMember(String clusterId) {
		log.info("-----클러스터 호스트목록");
		return clusterService.getHostMember(clusterId);
	}

	// 해당 cluster가 가지고 있는 vm, 레이블 생성시 필요
	@GetMapping("/cluster/vmme")
	@ResponseBody
	public List<VmVo> getVmMember(String clusterId) {
		log.info("-----클러스터 가상머신목록");
		return clusterService.getVmMember(clusterId);
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
