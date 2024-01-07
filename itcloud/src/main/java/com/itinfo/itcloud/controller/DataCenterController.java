package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.StorageDomainVo;
import com.itinfo.itcloud.service.ItDataCenterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DataCenterController {

	private final ItDataCenterService itDataCenterService;

	@GetMapping("/computing/datacenters")
	public String datacenters(Model model) {
		List<DataCenterVo> dataCenterVOList = itDataCenterService.getList();
		model.addAttribute("datacenters", dataCenterVOList);
		log.info("---datacenters");

		return "computing/datacenters";
	}

	@GetMapping("/computing/datacentersStatus")
	@ResponseBody
	public List<DataCenterVo> datacenters() {
		log.info("-----datacentersStatus");
		return itDataCenterService.getList();
	}


	// 스토리지
	@GetMapping("/computing/datacenter-storage")
	public String storage(String id, Model model) {
		List<StorageDomainVo> storage = itDataCenterService.getStorage(id);
		model.addAttribute("storage", storage);
		model.addAttribute("id", id);
		log.info("---datacenter-storage");

		return "computing/datacenter-storage";
	}

	@GetMapping("/computing/datacenter/storageStatus")
	@ResponseBody
	public List<StorageDomainVo> storage(String id) {
		log.info("-----datacenter/storageStatus: " + id);
		return itDataCenterService.getStorage(id);
	}


	// 네트워크
	@GetMapping("/computing/datacenter-network")
	public String network(String id, Model model) {
		List<NetworkVo> network = itDataCenterService.getNetwork(id);
		model.addAttribute("network", network);
		model.addAttribute("id", id);

		return "computing/datacenter-network";
	}

	@GetMapping("/computing/datacenter/networkStatus")
	@ResponseBody
	public List<NetworkVo> network(String id) {
		log.info("----- 데이터센터 network 목록 불러오기: " + id);
		return itDataCenterService.getNetwork(id);
	}


	// 네트워크
	@GetMapping("/computing/datacenter-cluster")
	public String cluster(String id, Model model) {
		List<ClusterVo> cluster = itDataCenterService.getCluster(id);
		model.addAttribute("cluster", cluster);
		model.addAttribute("id", id);

		return "computing/datacenter-cluster";
	}

	@GetMapping("/computing/datacenter/clusterStatus")
	@ResponseBody
	public List<ClusterVo> cluster(String id) {
		log.info("----- 데이터센터 cluster 목록 불러오기: " + id);
		return itDataCenterService.getCluster(id);
	}


	@GetMapping("/computing/datacenter-permission")
	public String permission(String id, Model model) {
		List<PermissionVo> permission = itDataCenterService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);

		return "computing/datacenter-permission";
	}

	@GetMapping("/computing/datacenter/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id) {
		log.info("----- permission 목록 불러오기: " + id);
		return itDataCenterService.getPermission(id);
	}

	@GetMapping("/computing/datacenter-event")
	public String event(String id, Model model) {
		List<EventVo> event = itDataCenterService.getEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);

		return "computing/datacenter-event";
	}

	@GetMapping("/computing/datacenter/eventStatus")
	@ResponseBody
	public List<EventVo> event(String id) {
		log.info("----- event 목록 불러오기: " + id);
		return itDataCenterService.getEvent(id);
	}
}
