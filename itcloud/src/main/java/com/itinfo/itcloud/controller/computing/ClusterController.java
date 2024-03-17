package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.service.ItClusterService;

import com.itinfo.itcloud.service.ItMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/computing")
public class ClusterController {
	private final ItClusterService itClusterService;
	private final ItMenuService menu;


	// 클러스터 목록
	@GetMapping("/clusters")
	public String clusters(Model model) {
		List<ClusterVo> clusterVOList = itClusterService.getList();
		model.addAttribute("clusters", clusterVOList);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/clusters";
	}

	// 클러스터 일반 출력
	@GetMapping("/cluster")
	public String cluster(String id, Model model) {
		ClusterVo cluster = itClusterService.getInfo(id);
		model.addAttribute("cluster", cluster);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/cluster";
	}

	// 클러스터 네트워크 출력
	@GetMapping("/cluster/network")
	public String network(String id, Model model) {
		List<NetworkVo> network = itClusterService.getNetwork(id);
		model.addAttribute("network", network);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/cluster-network";
	}

	// 클러스터 호스트 출력
	@GetMapping("/cluster/host")
	public String hosts(String id, Model model) {
		List<HostVo> hosts = itClusterService.getHost(id);
		model.addAttribute("hosts", hosts);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/cluster-host";
	}

	// 클러스터 가상머신 출력
	@GetMapping("/cluster/vm")
	public String vm(String id, Model model) {
		List<VmVo> vms = itClusterService.getVm(id);
		model.addAttribute("vms", vms);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/cluster-vm";
	}


	// 클러스터 선호도 그룹 출력
	@GetMapping("/cluster/affinityGroup")
	public String aff(String id, Model model) {
		List<AffinityGroupVo> aff = itClusterService.getAffinitygroup(id);
		model.addAttribute("aff", aff);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "computing/cluster-affGroup";
	}

	// 클러스터 선호도 그룹 생성
	@PostMapping("/cluster/affinityGroup/add")
	public CommonVo<Boolean> addAffinitygroup(@RequestBody AffinityGroupCreateVo agVo){
		return itClusterService.addAffinitygroup(agVo);
	}

	// 클러스터 선호도 그룹 편집
	@PostMapping("/cluster/affinityGroup/edit")
	public CommonVo<Boolean> editAffinitygroup(@RequestBody AffinityGroupCreateVo agVo){
		return itClusterService.editAffinitygroup(agVo);
	}

	// 클러스터 선호도 그룹 삭제
	@PostMapping("/cluster/affinityGroup/delete")
	public CommonVo<Boolean> deleteAffinitygroup(@RequestParam String clusterId, @RequestParam String id){
		return itClusterService.deleteAffinitygroup(clusterId, id);
	}


	// 클러스터 선호도 레이블 출력
	@GetMapping("/cluster/affinityLabel")
	public String affLabel(String id, Model model) {
		List<AffinityLabelVo> aff = itClusterService.getAffinitylabelList(id);
		model.addAttribute("aff", aff);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/cluster-affLabel";
	}


	// 해당 cluster가 가지고 있는 host, 레이블 생성시 필요
	@GetMapping("/cluster/hostme")
	@ResponseBody
	public List<HostVo> getHostMember(String clusterId) {
		log.info("-----클러스터 호스트목록");
		return itClusterService.getHostMember(clusterId);
	}

	// 해당 cluster가 가지고 있는 vm, 레이블 생성시 필요
	@GetMapping("/cluster/vmme")
	@ResponseBody
	public List<VmVo> getVmMember(String clusterId) {
		log.info("-----클러스터 가상머신목록");
		return itClusterService.getVmMember(clusterId);
	}


	// 클러스터 선호도 레이블 생성
	@PostMapping("/cluster/affinityLabel/add")
	public CommonVo<Boolean> addAff(@RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- 선호도 레이블 생성");
		return itClusterService.addAffinitylabel(alVo);
	}

	// 클러스터 선호도 레이블 편집
	@PostMapping("/cluster/affinityLabel/edit")
	public CommonVo<Boolean> editAff(@RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- 선호도 레이블 편집");
		return itClusterService.editAffinitylabel(alVo);
	}

	// 클러스터 선호도 레이블 삭제
	@PostMapping("/cluster/affinityLabel/delete")
	public CommonVo<Boolean> deleteAff(String id) {
		log.info("--- 선호도 레이블 삭제");
		return itClusterService.deleteAffinitylabel(id);
	}



	// 클러스터 권한 출력
	@GetMapping("/cluster/permission")
	public String permission(String id, Model model) {
		List<PermissionVo> permission = itClusterService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/cluster-permission";
	}

	// 클러스터 이벤트 출력
	@GetMapping("/cluster/event")
	public String event(String id, Model model) {
		List<EventVo> event = itClusterService.getEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/cluster-event";
	}

	


	// 데이터센터 생성 출력
	@GetMapping("/cluster/add")
	public String addShow(Model model) {
		List<DataCenterVo> dcList = itClusterService.getDcList();
		model.addAttribute("dc", dcList);
		return "computing/cluster-add";
	}

	@PostMapping("/cluster/add")
	public CommonVo<Boolean> addCluster(@RequestBody ClusterCreateVo cVo){
		log.info("클러스터 추가");
		return itClusterService.addCluster(cVo);
	}

	 // 데이터센터 생성
	@PostMapping("/cluster/add2")
	public String add(Model model, @ModelAttribute ClusterCreateVo cVo) {
		CommonVo<Boolean> res = itClusterService.addCluster(cVo);

		if(res.getBody().getContent().equals(true)){
			model.addAttribute("result", "클러스터 생성 완료");
		}else{
			model.addAttribute("result", "클러스터 생성 실패");
		}
		model.addAttribute("message", res.getHead().getMessage());
		model.addAttribute("body", res.getBody().getContent());

		return "computing/cluster-add2";
	}

	// 클러스터 수정 출력
	@GetMapping("/cluster/edit")
	public String editShow(Model model, String id) {
		ClusterCreateVo cVo = itClusterService.getClusterCreate(id);
		model.addAttribute("c", cVo);
		return "computing/cluster-edit";
	}

	// 클러스터 수정
	@PostMapping("/cluster/edit2")
	public String edit(Model model, @ModelAttribute ClusterCreateVo cVo ) {
		CommonVo<Boolean> res = itClusterService.editCluster(cVo);

		if(res.getBody().getContent().equals(true)){
			model.addAttribute("result", "클러스터 수정 완료");
		}else{
			model.addAttribute("result", "클러스터 수정 실패");
		}
		model.addAttribute("message", res.getHead().getMessage());
		model.addAttribute("body", res.getBody().getContent());

		return "computing/cluster-edit2";
	}

	// 클러스터 삭제 출력
	@GetMapping("/cluster/delete")
	public String deleteShow(Model model, String id){
		model.addAttribute("name", itClusterService.getName(id));
		return "computing/cluster-delete";
	}

	// 클러스터 삭제
	@PostMapping("/cluster/delete2")
	public String delete(Model model, @RequestParam String id){
		CommonVo<Boolean> res = itClusterService.deleteCluster(id);

		if(res.getBody().getContent().equals(true)){
			model.addAttribute("result", "클러스터 삭제 완료");
		}else{
			model.addAttribute("result", "클러스터 삭제 실패");
		}
		model.addAttribute("message", res.getHead().getMessage());
		model.addAttribute("body", res.getBody().getContent());

		return "computing/cluster-delete2";
	}





	//region: @ResponseBody
	@GetMapping("/clustersStatus")
	@ResponseBody
	public List<ClusterVo> clusters() {
		log.info("-----클러스터 목록");
		return itClusterService.getList();
	}

	@GetMapping("/clusterStatus")
	@ResponseBody
	public ClusterVo cluster(String id) {
		log.info("-----클러스터 일반: " + id);
		return itClusterService.getInfo(id);
	}

	@GetMapping("/cluster/networkStatus")
	@ResponseBody
	public List<NetworkVo> network(String id) {
		log.info("-----클러스터 네트워크: " + id);
		return itClusterService.getNetwork(id);
	}

	@GetMapping("/cluster/hostStatus")
	@ResponseBody
	public List<HostVo> host(String id) {
		log.info("----- 클러스터 호스트: " + id);
		return itClusterService.getHost(id);
	}

	@GetMapping("/cluster/vmStatus")
	@ResponseBody
	public List<VmVo> vm(String id) {
		log.info("----- 클러스터 가상머신: " + id);
		return itClusterService.getVm(id);
	}

	@GetMapping("/cluster/affGroupStatus")
	@ResponseBody
	public List<AffinityGroupVo> affGroup(String id) {
		log.info("----- 클러스터 선호도 목록: " + id);
		return itClusterService.getAffinitygroup(id);
	}


	@GetMapping("/cluster/affLabelStatus")
	@ResponseBody
	public List<AffinityLabelVo> affLabel(String id) {
		log.info("----- 클러스터 레이블 목록 불러오기: " + id);
		return itClusterService.getAffinitylabelList(id);
	}

	@GetMapping("/cluster/affLabel")
	@ResponseBody
	public AffinityLabelCreateVo getAffinityLabel(String id) {
		log.info("----- 클러스터 선호도 레이블 생성 출력: " + id);
		return itClusterService.getAffinityLabel(id);
	}

	@GetMapping("/cluster/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id) {
		log.info("----- 클러스터 권한: " + id);
		return itClusterService.getPermission(id);
	}

	@GetMapping("/cluster/eventStatus")
	@ResponseBody
	public List<EventVo> event(String id) {
		log.info("----- 클러스터 이벤트: " + id);
		return itClusterService.getEvent(id);
	}

	//endregion
	
	
	
	
	// region: 안쓸 것 같음

//	@GetMapping("/cluster-cpu")
//	public String cpu(String id, Model model) {
//		List<CpuProfileVo> cpu = itClusterService.getCpuProfile(id);
//		model.addAttribute("cpu", cpu);
//		model.addAttribute("id", id);
//
//		return "computing/cluster-cpu";
//	}

//	@GetMapping("/cluster/cpuStatus")
//	@ResponseBody
//	public List<CpuProfileVo> cpu(String id) {
//		log.info("----- 클러스터 cpu 목록 불러오기: " + id);
//		return itClusterService.getCpuProfile(id);
//	}
	
	// endregion

}
