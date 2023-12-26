package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.DataCenterVO;
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
		List<DataCenterVO> dataCenterVOList = itDataCenterService.getDatacenters();
		model.addAttribute("datacenters", dataCenterVOList);
		log.info("***** datacenters 목록 화면출력");

		return "computing/datacenters";
	}

	// datacenters 목록
	@GetMapping("/computing/datacenters/status")
	@ResponseBody
	public List<DataCenterVO> datacenters() {
		log.info("----- datacenters 목록 불러오기");
		return itDataCenterService.getDatacenters();
	}


	// 스토리지
	@GetMapping("/computing/datacenter-storage")
	public String storage(String id, Model model) {
		DataCenterVO storage = itDataCenterService.getStorage(id);
		model.addAttribute("storage", storage);
		model.addAttribute("id", id);

		return "computing/datacenter-storage";
	}

	@GetMapping("/computing/datacenter/storageStatus")
	@ResponseBody
	public DataCenterVO storage(String id) {
		log.info("----- 데이터센터 스토리지 목록 불러오기: " + id);
		return itDataCenterService.getStorage(id);
	}


	// 네트워크
	@GetMapping("/computing/datacenter-network")
	public String network(String id, Model model) {
		DataCenterVO network = itDataCenterService.getNetwork(id);
		model.addAttribute("network", network);
		model.addAttribute("id", id);

		return "computing/datacenter-network";
	}

	@GetMapping("/computing/datacenter/networkStatus")
	@ResponseBody
	public DataCenterVO network(String id) {
		log.info("----- 데이터센터 network 목록 불러오기: " + id);
		return itDataCenterService.getNetwork(id);
	}

	// 네트워크
	@GetMapping("/computing/datacenter-cluster")
	public String cluster(String id, Model model) {
		DataCenterVO cluster = itDataCenterService.getCluster(id);
		model.addAttribute("cluster", cluster);
		model.addAttribute("id", id);

		return "computing/datacenter-cluster";
	}

	@GetMapping("/computing/datacenter/clusterStatus")
	@ResponseBody
	public DataCenterVO cluster(String id) {
		log.info("----- 데이터센터 cluster 목록 불러오기: " + id);
		return itDataCenterService.getCluster(id);
	}
}
