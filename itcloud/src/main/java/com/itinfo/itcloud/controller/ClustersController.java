package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.ClusterVO;
import com.itinfo.itcloud.service.ItClusterService;

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
public class ClustersController {
	private final ItClusterService itClusterService;

	@GetMapping("/clusterStatus")
	@ResponseBody
	public List<ClusterVO> clusterStatus() {
		long start = System.currentTimeMillis();

		List<ClusterVO> cluster = itClusterService.getClusters();

		long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		log.info("수행시간(ms): {}", end - start);
		log.info("----- clusterStatus");
		return cluster;
	}


	@GetMapping("/computing/clusters")
	public String clusters(Model model) {
		List<ClusterVO> clusterVOList = itClusterService.getList();
		model.addAttribute("clusters", clusterVOList);
		log.info("***** 클러스터 목록 화면출력");
		return "computing/clusters";
	}

	// cluster 목록
	@GetMapping("/computing/clusters/status")
	@ResponseBody
	public List<ClusterVO> clusters() {
		log.info("----- 클러스터 목록 불러오기");
		return itClusterService.getList();
	}


	@GetMapping("/computing/cluster")
	public String cluster(String id, Model model) {
		ClusterVO cluster = itClusterService.getInfo(id);
		model.addAttribute("cluster", cluster);
		model.addAttribute("id", id);
		return "computing/cluster";
	}

	//    http://localhost:8080/computing/cluster/status?id=c7ea23ce-8810-11ee-af5e-00163e0202ee
	@GetMapping("/computing/cluster/status")
	@ResponseBody
	public ClusterVO cluster(String id) {
		log.info("----- 클러스터 id 일반 불러오기: " + id);
		return itClusterService.getInfo(id);
	}


	@GetMapping("/computing/cluster-network")
	public String network(String id, Model model) {
		ClusterVO network = itClusterService.getNetwork(id);
		model.addAttribute("network", network);
		model.addAttribute("id", id);

		return "computing/cluster-network";
	}

	//
	@GetMapping("/computing/cluster/networkStatus")
	@ResponseBody
	public ClusterVO network(String id) {
		log.info("----- 클러스터 network 목록 불러오기: " + id);
		return itClusterService.getNetwork(id);
	}


	@GetMapping("/computing/cluster-host")
	public String hosts(String id, Model model) {
		ClusterVO hosts = itClusterService.getHost(id);
		model.addAttribute("hosts", hosts);
		model.addAttribute("id", id);

		return "computing/cluster-host";
	}

	//
	@GetMapping("/computing/cluster/hostStatus")
	@ResponseBody
	public ClusterVO host(String id) {
		log.info("----- 클러스터 host 목록 불러오기: " + id);
		return itClusterService.getHost(id);
	}


	@GetMapping("/computing/cluster-vm")
	public String vm(String id, Model model) {
		ClusterVO vms = itClusterService.getVm(id);
		model.addAttribute("vms", vms);
		model.addAttribute("id", id);

		return "computing/cluster-vm";
	}

	//
	@GetMapping("/computing/cluster/vmStatus")
	@ResponseBody
	public ClusterVO vm(String id) {
		log.info("----- 클러스터 vm 목록 불러오기: " + id);
		return itClusterService.getVm(id);
	}

	@GetMapping("/computing/cluster-aff")
	public String aff(String id, Model model) {
		ClusterVO aff = itClusterService.getAffinitygroups(id);
		model.addAttribute("aff", aff);
		model.addAttribute("id", id);
		return "computing/cluster-aff";
	}


	@GetMapping("/computing/cluster/affStatus")
	@ResponseBody
	public ClusterVO aff(String id) {
		log.info("----- 클러스터 선호도 목록 불러오기: " + id);
		return itClusterService.getAffinitygroups(id);
	}

	@GetMapping("/computing/cluster-cpu")
	public String cpu(String id, Model model) {
		ClusterVO cpu = itClusterService.getCpuProfile(id);
		model.addAttribute("cpu", cpu);
		model.addAttribute("id", id);

		return "computing/cluster-cpu";
	}

	//
	@GetMapping("/computing/cluster/cpuStatus")
	@ResponseBody
	public ClusterVO cpu(String id) {
		log.info("----- 클러스터 cpu 목록 불러오기: " + id);
		return itClusterService.getCpuProfile(id);
	}
}
