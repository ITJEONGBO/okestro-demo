package com.itinfo.itcloud.controller.computing;

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
@RequiredArgsConstructor
@Slf4j
public class ClusterController {
	private final ItClusterService itClusterService;
	private final ItMenuService menu;

	//region: get Cluster

	@GetMapping("/computing/clusters")
	public String clusters(Model model) {
		long start = System.currentTimeMillis();

		List<ClusterVo> clusterVOList = itClusterService.getList();
		model.addAttribute("clusters", clusterVOList);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		log.info("---clusters");

		long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		System.out.println("cluster 수행시간(ms): " + (end-start));

		return "computing/clusters";
	}

	@GetMapping("/computing/cluster")
	public String cluster(String id, Model model) {
		ClusterVo cluster = itClusterService.getInfo(id);
		model.addAttribute("cluster", cluster);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		log.info("---cluster: " + id);

		return "computing/cluster";
	}

	@GetMapping("/computing/cluster-network")
	public String network(String id, Model model) {
		List<NetworkVo> network = itClusterService.getNetwork(id);
		model.addAttribute("network", network);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		log.info("---cluster-network: " + id);

		return "computing/cluster-network";
	}


	@GetMapping("/computing/cluster-host")
	public String hosts(String id, Model model) {
		List<HostVo> hosts = itClusterService.getHost(id);
		model.addAttribute("hosts", hosts);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/cluster-host";
	}


	@GetMapping("/computing/cluster-vm")
	public String vm(String id, Model model) {
		List<VmVo> vms = itClusterService.getVm(id);
		model.addAttribute("vms", vms);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/cluster-vm";
	}

	@GetMapping("/computing/cluster-affGroup")
	public String aff(String id, Model model) {
		List<AffinityGroupVo> aff = itClusterService.getAffinitygroup(id);
		model.addAttribute("aff", aff);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "computing/cluster-affGroup";
	}

	@GetMapping("/computing/cluster-affLabel")
	public String affLabel(String id, Model model) {
		List<AffinityLabelVo> aff = itClusterService.getAffinitylabel();
		model.addAttribute("aff", aff);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/cluster-affLabel";
	}

//	@GetMapping("/computing/cluster-cpu")
//	public String cpu(String id, Model model) {
//		List<CpuProfileVo> cpu = itClusterService.getCpuProfile(id);
//		model.addAttribute("cpu", cpu);
//		model.addAttribute("id", id);
//
//		return "computing/cluster-cpu";
//	}
//

	@GetMapping("/computing/cluster-permission")
	public String permission(String id, Model model) {
		List<PermissionVo> permission = itClusterService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/cluster-permission";
	}

	@GetMapping("/computing/cluster-event")
	public String event(String id, Model model) {
		List<EventVo> event = itClusterService.getEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/cluster-event";
	}
	// endregion


	//region: set Cluster


	// 데이터센터 생성 창출력
	@GetMapping("/computing/cluster-add")
	public String addShow(Model model) {
		List<DataCenterVo> dcList = itClusterService.getDcList();

		model.addAttribute("dc", dcList);
		return "computing/cluster-add";
	}

	 // 데이터센터 생성
	@PostMapping("/computing/cluster-add2")
	public String add(Model model, @ModelAttribute ClusterCreateVo cVo) {
		if(itClusterService.addCluster(cVo)){
			model.addAttribute("result", "클러스터 생성 완료");
		}else{
			model.addAttribute("result", "클러스터 생성 실패");
		}

		return "computing/cluster-add2";
	}

	// 클러스터 수정 창출력
	@GetMapping("/computing/cluster-edit")
	public String editShow(Model model, String id) {
		ClusterCreateVo cVo = itClusterService.getClusterCreate(id);
		model.addAttribute("c", cVo);
		return "computing/cluster-edit";
	}

	// 클러스터 수정
	@PostMapping("/computing/cluster-edit2")
	public String edit(Model model, @ModelAttribute ClusterCreateVo cVo ) {
		log.info("edit 시작");

		itClusterService.editCluster(cVo);
		model.addAttribute("result", "완료");
		return "computing/cluster-edit2";
	}

	@GetMapping("/computing/cluster-delete")
	public String deleteShow(Model model, String id){
		model.addAttribute("id", id);
		model.addAttribute("name", itClusterService.getName(id));
		return "computing/cluster-delete";
	}

	@PostMapping("/computing/cluster-delete2")
	public String delete(Model model, @RequestParam String id){
		if(itClusterService.deleteCluster(id)){
			model.addAttribute("result", "완료");
		}else {
			model.addAttribute("result", "실패");
		}

		return "computing/cluster-delete2";
	}




	// endregion


	//region: @ResponseBody
	@GetMapping("/computing/clustersStatus")
	@ResponseBody
	public List<ClusterVo> clusters() {
		log.info("-----clustersStatus");
		return itClusterService.getList();
	}

	@GetMapping("/computing/clusterStatus")
	@ResponseBody
	public ClusterVo cluster(String id) {
		log.info("-----clusterStatus: " + id);
		return itClusterService.getInfo(id);
	}

	@GetMapping("/computing/cluster/networkStatus")
	@ResponseBody
	public List<NetworkVo> network(String id) {
		log.info("-----cluster/networkStatus: " + id);
		return itClusterService.getNetwork(id);
	}

	@GetMapping("/computing/cluster/hostStatus")
	@ResponseBody
	public List<HostVo> host(String id) {
		log.info("----- 클러스터 host 목록 불러오기: " + id);
		return itClusterService.getHost(id);
	}

	@GetMapping("/computing/cluster/vmStatus")
	@ResponseBody
	public List<VmVo> vm(String id) {
		log.info("----- 클러스터 vm 목록 불러오기: " + id);
		return itClusterService.getVm(id);
	}

	@GetMapping("/computing/cluster/affGroupStatus")
	@ResponseBody
	public List<AffinityGroupVo> affGroup(String id) {
		log.info("----- 클러스터 선호도 목록 불러오기: " + id);
		return itClusterService.getAffinitygroup(id);
	}

	@GetMapping("/computing/cluster/affLabelStatus")
	@ResponseBody
	public List<AffinityLabelVo> affLabel(String id) {
		log.info("----- 클러스터 선호도 목록 불러오기: " + id);
		return itClusterService.getAffinitylabel();
	}

//	@GetMapping("/computing/cluster/cpuStatus")
//	@ResponseBody
//	public List<CpuProfileVo> cpu(String id) {
//		log.info("----- 클러스터 cpu 목록 불러오기: " + id);
//		return itClusterService.getCpuProfile(id);
//	}

	@GetMapping("/computing/cluster/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id) {
		log.info("----- 클러스터 permission 불러오기: " + id);
		return itClusterService.getPermission(id);
	}

	@GetMapping("/computing/cluster/eventStatus")
	@ResponseBody
	public List<EventVo> event(String id) {
		log.info("----- event 목록 불러오기: " + id);
		return itClusterService.getEvent(id);
	}

	//endregion

}
