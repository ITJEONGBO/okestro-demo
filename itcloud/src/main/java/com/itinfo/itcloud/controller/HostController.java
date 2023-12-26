package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.HostVO;
import com.itinfo.itcloud.service.ItHostService;

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
public class HostController {

	private final ItHostService itHostService;

/*
	@GetMapping("/computing/hosts")
	public String hosts(Model model){
		List<HostDetailVO> hostVOList = this.hostdService.retrieveHosts();
		model.addAttribute("hostVOList", hostVOList);
		return "compute/hosts";
	}

	@GetMapping("/hostStatus")
	@ResponseBody
	public List<HostDetailVO> ho(){
		List<HostDetailVO> list = null;
		try{
			list = hostdService.retrieveHosts();
		}catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}
*/

	@GetMapping("/hostStatus")
	@ResponseBody
	public List<HostVO> clusterStatus() {
		long start = System.currentTimeMillis();

		List<HostVO> host = itHostService.getHosts();

		long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		log.info("수행시간(ms): " + (end - start));

		log.info("----- hostStatus");

		return host;
	}


	@GetMapping("/computing/hosts")
	public String hosts(Model model) {
		List<HostVO> hostVOList = itHostService.getList();
		model.addAttribute("hostVOList", hostVOList);
		log.info("***** hosts 목록 화면출력");

		return "computing/hosts";
	}

	// cluster 목록
	@GetMapping("/computing/hosts/status")
	@ResponseBody
	public List<HostVO> hosts() {
		log.info("----- Host 목록 불러오기");
		return itHostService.getList();
	}


	@GetMapping("/computing/host")
	public String host(String id, Model model) {
		HostVO host = itHostService.getInfo(id);
		model.addAttribute("host", host);
		model.addAttribute("id", id);

		return "computing/host";
	}

	//	http://localhost:8080/computing/host/status?id=
	@GetMapping("/computing/host/status")
	@ResponseBody
	public HostVO host(String id) {
		log.info("----- host id 일반 불러오기: " + id);
		return itHostService.getInfo(id);
	}


	@GetMapping("/computing/host-vm")
	public String vm(String id, Model model) {
		HostVO vm = itHostService.getVm(id);
		model.addAttribute("vm", vm);
		model.addAttribute("id", id);

		return "computing/host-vm";
	}

	@GetMapping("/computing/host/vmstatus")
	@ResponseBody
	public HostVO vm(String id) {
		log.info("----- host vm 일반 불러오기: " + id);
		return itHostService.getVm(id);
	}

	@GetMapping("/computing/host-nic")
	public String nic(String id, Model model) {
		HostVO nic = itHostService.getNic(id);
		model.addAttribute("nic", nic);
		model.addAttribute("id", id);

		return "computing/host-nic";
	}

	@GetMapping("/computing/host/nicstatus")
	@ResponseBody
	public HostVO nic(String id) {
		log.info("----- host nic 일반 불러오기: " + id);
		return itHostService.getNic(id);
	}


	@GetMapping("/computing/host-device")
	public String device(String id, Model model) {
		HostVO device = itHostService.getHostDevice(id);
		model.addAttribute("device", device);
		model.addAttribute("id", id);

		return "computing/host-device";
	}

	@GetMapping("/computing/host/devicestatus")
	@ResponseBody
	public HostVO device(String id) {
		log.info("----- host device 일반 불러오기: " + id);
		return itHostService.getHostDevice(id);
	}

	@GetMapping("/computing/host-aff")
	public String aff(String id, Model model) {
		HostVO aff = itHostService.getAffinitylabels(id);
		model.addAttribute("aff", aff);
		model.addAttribute("id", id);

		return "computing/host-aff";
	}

	@GetMapping("/computing/host/affstatus")
	@ResponseBody
	public HostVO aff(String id) {
		log.info("----- host aff 일반 불러오기: " + id);
		return itHostService.getAffinitylabels(id);
	}


}
