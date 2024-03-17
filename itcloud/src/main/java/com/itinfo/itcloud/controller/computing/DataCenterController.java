package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.service.ItDataCenterService;

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
public class DataCenterController {
	private final ItDataCenterService itDataCenterService;
	private final ItMenuService menu;


	// 데이터 센터 목록 출력
	@GetMapping("/datacenters")
	public String datacenters(Model model) {
		List<DataCenterVo> dataCenterVOList = itDataCenterService.getList();
		model.addAttribute("datacenters", dataCenterVOList);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/datacenters";
	}

	// 데이터 센터 이벤트 출력
	@GetMapping("/datacenter/event")
	public String event(String id, Model model) {
		List<EventVo> event = itDataCenterService.getEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);
		model.addAttribute("name", itDataCenterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/datacenter-event";
	}


	// 데이터센터 생성 출력
	@GetMapping("/datacenter/add")
	public String add() {
		return "computing/datacenter-add";
	}

	// 데이터센터 생성
	@PostMapping("/datacenter/add2")
	public String add2(Model model, @ModelAttribute DataCenterCreateVo dcVo) {
		CommonVo<Boolean> result = itDataCenterService.addDatacenter(dcVo);

		if(result.getBody().getContent().equals(true)){
			model.addAttribute("result", "Datacenter 생성 완료");
		}else {
			model.addAttribute("result", "Datacenter 생성 실패");
		}
		model.addAttribute("message", result.getHead().getMessage());
		model.addAttribute("body", result.getBody().getContent());

		return "computing/datacenter-add2";
	}


	// 데이터센터 수정 출력
	@GetMapping("/datacenter/edit")
	public String edit(Model model, String id) {
		DataCenterCreateVo dcVo = itDataCenterService.getDatacenter(id);
		model.addAttribute("dc", dcVo);
		return "computing/datacenter-edit";
	}

	// 데이터센터 수정
	@PostMapping("/datacenter/edit2")
	public String edit2(Model model, @ModelAttribute DataCenterCreateVo dcVo ) {
		CommonVo<Boolean> result = itDataCenterService.editDatacenter(dcVo);

		if(result.getBody().getContent().equals(true)){
			model.addAttribute("result", "Datacenter 수정 완료");
		}else {
			model.addAttribute("result", "Datacenter 수정 실패");
		}
		model.addAttribute("message", result.getHead().getMessage());
		model.addAttribute("body", result.getBody().getContent());

		return "computing/datacenter-edit2";
	}


	// 데이터센터 삭제 창출력
	@GetMapping("/datacenter/delete")
	public String delete(Model model, String id){
		model.addAttribute("id", id);
		model.addAttribute("name", itDataCenterService.getName(id));
		return "computing/datacenter-delete";
	}

	// 데이터센터 삭제
	@PostMapping("/datacenter/delete2")
	public String delete2(Model model, @RequestParam String id){
		CommonVo<Boolean> result = itDataCenterService.deleteDatacenter(id);

		if(result.getBody().getContent().equals(true)){
			model.addAttribute("result", "Datacenter 생성 완료");
		}else {
			model.addAttribute("result", "Datacenter 생성 실패");
		}
		model.addAttribute("message", result.getHead().getMessage());
		model.addAttribute("body", result.getBody().getContent());

		return "computing/datacenter-delete2";
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
