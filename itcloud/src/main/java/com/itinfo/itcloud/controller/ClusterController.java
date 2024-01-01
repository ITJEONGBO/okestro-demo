package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.NetworkVo;
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
public class ClusterController {
	private final ItClusterService itClusterService;

	@GetMapping("/computing/clusters")
	public String clusters(Model model) {
		List<ClusterVo> clusterVOList = itClusterService.getList();
		model.addAttribute("clusters", clusterVOList);
		log.info("---clusters");
		return "computing/clusters";
	}

	// cluster 목록
	@GetMapping("/computing/clustersStatus")
	@ResponseBody
	public List<ClusterVo> clusters() {
		log.info("-----clustersStatus");
		return itClusterService.getList();
	}


	@GetMapping("/computing/cluster")
	public String cluster(String id, Model model) {
		ClusterVo cluster = itClusterService.getInfo(id);
		model.addAttribute("cluster", cluster);
		model.addAttribute("id", id);
		log.info("---cluster: " + id);

		return "computing/cluster";
	}

	@GetMapping("/computing/clusterStatus")
	@ResponseBody
	public ClusterVo cluster(String id) {
		log.info("-----clusterStatus: " + id);
		return itClusterService.getInfo(id);
	}


	@GetMapping("/computing/cluster-network")
	public String network(String id, Model model) {
		List<NetworkVo> network = itClusterService.getNetwork(id);
		model.addAttribute("network", network);
		model.addAttribute("id", id);
		log.info("---cluster-network: " + id);

		return "computing/cluster-network";
	}

	//
	@GetMapping("/computing/cluster/networkStatus")
	@ResponseBody
	public List<NetworkVo> network(String id) {
		log.info("-----cluster/networkStatus: " + id);
		return itClusterService.getNetwork(id);
	}


	@GetMapping("/computing/cluster-host")
	public String hosts(String id, Model model) {
		List<HostVo> hosts = itClusterService.getHost(id);
		model.addAttribute("hosts", hosts);
		model.addAttribute("id", id);

		return "computing/cluster-host";
	}

	//
	@GetMapping("/computing/cluster/hostStatus")
	@ResponseBody
	public List<HostVo> host(String id) {
		log.info("----- 클러스터 host 목록 불러오기: " + id);
		return itClusterService.getHost(id);
	}


	@GetMapping("/computing/cluster-vm")
	public String vm(String id, Model model) {
		List<VmVo> vms = itClusterService.getVm(id);
		model.addAttribute("vms", vms);
		model.addAttribute("id", id);

		return "computing/cluster-vm";
	}

	//
	@GetMapping("/computing/cluster/vmStatus")
	@ResponseBody
	public List<VmVo> vm(String id) {
		log.info("----- 클러스터 vm 목록 불러오기: " + id);
		return itClusterService.getVm(id);
	}

	@GetMapping("/computing/cluster-affGroup")
	public String aff(String id, Model model) {
		List<AffinityGroupVo> aff = itClusterService.getAffinitygroups(id);
		model.addAttribute("aff", aff);
		model.addAttribute("id", id);
		return "computing/cluster-affGroup";
	}


	@GetMapping("/computing/cluster/affGroupStatus")
	@ResponseBody
	public List<AffinityGroupVo> affGroup(String id) {
		log.info("----- 클러스터 선호도 목록 불러오기: " + id);
		return itClusterService.getAffinitygroups(id);
	}

	@GetMapping("/computing/cluster-affLabel")
	public String affLabel(String id, Model model) {
		List<AffinityGroupVo> aff = itClusterService.getAffinitygroups(id);
		model.addAttribute("aff", aff);
		model.addAttribute("id", id);
		return "computing/cluster-affLabel";
	}


	@GetMapping("/computing/cluster/affLabelStatus")
	@ResponseBody
	public List<AffinityLabelVo> affLabel(String id) {
		log.info("----- 클러스터 선호도 목록 불러오기: " + id);
		return itClusterService.getAffinitylabels(id);
	}

	@GetMapping("/computing/cluster-cpu")
	public String cpu(String id, Model model) {
		List<CpuProfileVo> cpu = itClusterService.getCpuProfile(id);
		model.addAttribute("cpu", cpu);
		model.addAttribute("id", id);

		return "computing/cluster-cpu";
	}

	//
	@GetMapping("/computing/cluster/cpuStatus")
	@ResponseBody
	public List<CpuProfileVo> cpu(String id) {
		log.info("----- 클러스터 cpu 목록 불러오기: " + id);
		return itClusterService.getCpuProfile(id);
	}
}
