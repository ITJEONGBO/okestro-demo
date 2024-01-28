package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.service.ItDataCenterService;

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
public class DataCenterController {

	private final ItDataCenterService itDataCenterService;
	private final ItMenuService menu;


	// region: get Datacenter
	@GetMapping("/computing/datacenters")
	public String datacenters(Model model) {
		long start = System.currentTimeMillis();

		List<DataCenterVo> dataCenterVOList = itDataCenterService.getList();
		model.addAttribute("datacenters", dataCenterVOList);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		System.out.println("datacenters controller 수행시간(ms): " + (end-start));

		log.info("datacenterList");
		return "computing/datacenters";
	}

	// 스토리지
	@GetMapping("/computing/datacenter-storage")
	public String storage(String id, Model model) {
		List<DomainVo> storage = itDataCenterService.getStorage(id);
		model.addAttribute("storage", storage);
		model.addAttribute("id", id);
		model.addAttribute("name", itDataCenterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		log.info("---datacenter-storage");

		return "computing/datacenter-storage";
	}

	// 네트워크
	@GetMapping("/computing/datacenter-network")
	public String network(String id, Model model) {
		List<NetworkVo> network = itDataCenterService.getNetwork(id);
		model.addAttribute("network", network);
		model.addAttribute("id", id);
		model.addAttribute("name", itDataCenterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/datacenter-network";
	}
	// 네트워크
	@GetMapping("/computing/datacenter-cluster")
	public String cluster(String id, Model model) {
		List<ClusterVo> cluster = itDataCenterService.getCluster(id);
		model.addAttribute("cluster", cluster);
		model.addAttribute("id", id);
		model.addAttribute("name", itDataCenterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/datacenter-cluster";
	}

	@GetMapping("/computing/datacenter-permission")
	public String permission(String id, Model model) {
		List<PermissionVo> permission = itDataCenterService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);
		model.addAttribute("name", itDataCenterService.getName(id));
		System.out.println("name");

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/datacenter-permission";
	}


	@GetMapping("/computing/datacenter-event")
	public String event(String id, Model model) {
		List<EventVo> event = itDataCenterService.getEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);
		model.addAttribute("name", itDataCenterService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/datacenter-event";
	}

	// endregion



	// region: set Datacenter

	// 데이터센터 생성 창출력
	@GetMapping("/computing/datacenter-add")
	public String add() {
		return "computing/datacenter-add";
	}

	// 데이터센터 생성
	@GetMapping("/computing/datacenter-add2")
	public String add2(Model model, DataCenterVo dcVo) {
		this.itDataCenterService.addDatacenter(dcVo);
		model.addAttribute("result", "완료");
		return "computing/datacenter-add2";
	}


	// 데이터센터 수정 창출력
	@GetMapping("/computing/datacenter-edit")
	public String edit(Model model, DataCenterVo dcVo) {
		model.addAttribute("dc", dcVo);
		return "computing/datacenter-edit";
	}

	// 데이터센터 수정
	@GetMapping("/computing/datacenter-edit2")
	public String edit2(Model model, DataCenterVo dcVo ) {
		this.itDataCenterService.editDatacenter(dcVo);
		model.addAttribute("result", "완료");
		return "computing/datacenter-edit2";
	}




	// endregion


	//region: @ResponseBody
	@GetMapping("/computing/datacentersStatus")
	@ResponseBody
	public List<DataCenterVo> datacenters() {
		log.info("-----datacentersStatus");
		return itDataCenterService.getList();
	}


	@GetMapping("/computing/datacenter/storageStatus")
	@ResponseBody
	public List<DomainVo> storage(String id) {
		log.info("-----datacenter/storageStatus: " + id);
		return itDataCenterService.getStorage(id);
	}


	@GetMapping("/computing/datacenter/networkStatus")
	@ResponseBody
	public List<NetworkVo> network(String id) {
		log.info("----- 데이터센터 network 목록 불러오기: " + id);
		return itDataCenterService.getNetwork(id);
	}


	@GetMapping("/computing/datacenter/clusterStatus")
	@ResponseBody
	public List<ClusterVo> cluster(String id) {
		log.info("----- 데이터센터 cluster 목록 불러오기: " + id);
		return itDataCenterService.getCluster(id);
	}


	@GetMapping("/computing/datacenter/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id) {
		log.info("----- permission 목록 불러오기: " + id);
		return itDataCenterService.getPermission(id);
	}

	@GetMapping("/computing/datacenter/eventStatus")
	@ResponseBody
	public List<EventVo> event(String id) {
		log.info("----- event 목록 불러오기: " + id);
		return itDataCenterService.getEvent(id);
	}
	//endregion

}
