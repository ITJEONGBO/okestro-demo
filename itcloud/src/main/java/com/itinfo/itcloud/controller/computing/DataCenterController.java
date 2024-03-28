package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.service.ItDataCenterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/computing")
public class DataCenterController {
	private final ItDataCenterService itDataCenterService;


	@GetMapping("/hello")
	@ResponseBody
	public String hello(){
		return "hello";
	}

	//region: @ResponseBody

	@GetMapping("/datacentersStatus")
	@ResponseBody
	public List<DataCenterVo> datacenters() {
		log.info("----- 데이터센터 목록");
		return itDataCenterService.getList();
	}

	@GetMapping("/datacenter-eventStatus")
	@ResponseBody
	public List<EventVo> event(String id) {
		log.info("----- 데이터센터 이벤트 : " + id);
		return itDataCenterService.getEvent(id);
	}

	//endregion





	// region: 안쓸것 같음

	// 스토리지
//	@GetMapping("/datacenter-storage")
//	public String storage(String id, Model model) {
//		List<DomainVo> storage = itDataCenterService.getStorage(id);
//		model.addAttribute("storage", storage);
//		model.addAttribute("id", id);
//		model.addAttribute("name", itDataCenterService.getName(id));
//
//		MenuVo m = menu.getMenu();
//		model.addAttribute("m", m);
//		log.info("---datacenter-storage");
//
//		return "computing/datacenter-storage";
//	}
//
//	// 네트워크
//	@GetMapping("/datacenter-network")
//	public String network(String id, Model model) {
//		List<NetworkVo> network = itDataCenterService.getNetwork(id);
//		model.addAttribute("network", network);
//		model.addAttribute("id", id);
//		model.addAttribute("name", itDataCenterService.getName(id));
//
//		MenuVo m = menu.getMenu();
//		model.addAttribute("m", m);
//
//		return "computing/datacenter-network";
//	}
//	// 네트워크
//	@GetMapping("/datacenter-cluster")
//	public String cluster(String id, Model model) {
//		List<ClusterVo> cluster = itDataCenterService.getCluster(id);
//		model.addAttribute("cluster", cluster);
//		model.addAttribute("id", id);
//		model.addAttribute("name", itDataCenterService.getName(id));
//
//		MenuVo m = menu.getMenu();
//		model.addAttribute("m", m);
//
//		return "computing/datacenter-cluster";
//	}
//
//	@GetMapping("/datacenter-permission")
//	public String permission(String id, Model model) {
//		List<PermissionVo> permission = itDataCenterService.getPermission(id);
//		model.addAttribute("permission", permission);
//		model.addAttribute("id", id);
//		model.addAttribute("name", itDataCenterService.getName(id));
//		System.out.println("name");
//
//		MenuVo m = menu.getMenu();
//		model.addAttribute("m", m);
//
//		return "computing/datacenter-permission";
//	}





//	@GetMapping("/datacenter/storageStatus")
//	@ResponseBody
//	public List<DomainVo> storage(String id) {
//		log.info("-----datacenter/storageStatus: " + id);
//		return itDataCenterService.getStorage(id);
//	}
//
//
//	@GetMapping("/datacenter/networkStatus")
//	@ResponseBody
//	public List<NetworkVo> network(String id) {
//		log.info("----- 데이터센터 network 목록 불러오기: " + id);
//		return itDataCenterService.getNetwork(id);
//	}
//
//
//	@GetMapping("/datacenter/clusterStatus")
//	@ResponseBody
//	public List<ClusterVo> cluster(String id) {
//		log.info("----- 데이터센터 cluster 목록 불러오기: " + id);
//		return itDataCenterService.getCluster(id);
//	}
//
//
//	@GetMapping("/datacenter/permissionStatus")
//	@ResponseBody
//	public List<PermissionVo> permission(String id) {
//		log.info("----- permission 목록 불러오기: " + id);
//		return itDataCenterService.getPermission(id);
//	}


	// endregion

}
