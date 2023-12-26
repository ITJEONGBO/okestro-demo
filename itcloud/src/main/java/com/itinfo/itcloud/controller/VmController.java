package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.NicVo;
import com.itinfo.itcloud.model.computing.VmDiskVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.service.ItVmService;
import com.itinfo.itcloud.service.ItSystemPropertyService;

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
public class VmController {

	private final ItVmService itVmService;
	private final ItSystemPropertyService itSystemPropertyService;


	@GetMapping("/computing/vms")
	public String vmList(Model model) {
		List<VmVo> vmVOList = itVmService.getList();
		model.addAttribute("vmList", vmVOList);
		return "computing/vms";
	}

	@GetMapping("/vmsList")
	@ResponseBody
	public List<VmVo> vms() {
		long start = System.currentTimeMillis();

		List<VmVo> vmsList = itVmService.getList();

		long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		log.debug("수행시간(ms): {}", end - start);

		return vmsList;
	}

	@GetMapping("/computing/vm")
	public String vm(String id, Model model) {
		VmVo vm = itVmService.getInfo(id);
		model.addAttribute("vm", vm);
		model.addAttribute("id", id);

		return "computing/vm";
	}

	@GetMapping("/computing/vm/status")
	@ResponseBody
	public VmVo vm(String id) {
		log.info("----- vm id 일반 불러오기: {}", id);
		return itVmService.getInfo(id);
	}


	@GetMapping("/computing/vm-nic")
	public String nic(String id, Model model) {
		List<NicVo> nic = itVmService.getNic(id);
		model.addAttribute("nic", nic);
		model.addAttribute("id", id);

		return "computing/vm-nic";
	}

	@GetMapping("/computing/vm/nicstatus")
	@ResponseBody
	public List<NicVo> nic(String id) {
		log.info("----- vm nic 일반 불러오기: " + id);
		return itVmService.getNic(id);
	}


	@GetMapping("/computing/vm-disk")
	public String disk(String id, Model model) {
		List<VmDiskVo> disk = itVmService.getDisk(id);
		model.addAttribute("disk", disk);
		model.addAttribute("id", id);

		return "computing/vm-disk";
	}

	@GetMapping("/computing/vm/diskstatus")
	@ResponseBody
	public List<VmDiskVo> disk(String id) {
		log.info("----- vm disk 일반 불러오기: " + id);
		return itVmService.getDisk(id);
	}
}
