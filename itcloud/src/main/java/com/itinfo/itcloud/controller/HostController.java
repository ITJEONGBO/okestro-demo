package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.service.ItHostService;

import com.itinfo.itcloud.service.ItMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HostController {

	private final ItHostService itHostService;
	private final ItMenuService menu;

	@GetMapping("/computing/hosts")
	public String hosts(Model model) {
		List<HostVo> hosts = itHostService.getList();
		model.addAttribute("hosts", hosts);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		log.info("***** hosts 목록 화면출력");

		return "computing/hosts";
	}

	// cluster 목록
	@GetMapping("/computing/hosts/status")
	@ResponseBody
	public List<HostVo> hosts() {
		log.info("----- Host 목록 불러오기");
		return itHostService.getList();
	}


	@GetMapping("/computing/host")
	public String host(String id, Model model) {
		HostVo host = itHostService.getInfo(id);
		model.addAttribute("host", host);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host";
	}

	//	http://localhost:8080/computing/host/status?id=
	@GetMapping("/computing/host/status")
	@ResponseBody
	public HostVo host(String id) {
		log.info("----- host id 일반 불러오기: " + id);
		return itHostService.getInfo(id);
	}


	@GetMapping("/computing/host-vm")
	public String vm(String id, Model model) {
		List<VmVo> vm = itHostService.getVm(id);
		model.addAttribute("vm", vm);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host-vm";
	}

	@GetMapping("/computing/host/vmstatus")
	@ResponseBody
	public List<VmVo> vm(String id) {
		log.info("----- host vm 일반 불러오기: " + id);
		return itHostService.getVm(id);
	}

	@GetMapping("/computing/host-nic")
	public String nic(String id, Model model) {
		List<NicVo> nic = itHostService.getNic(id);
		model.addAttribute("nic", nic);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host-nic";
	}

	@GetMapping("/computing/host/nicstatus")
	@ResponseBody
	public List<NicVo> nic(String id) {
		log.info("----- host nic 일반 불러오기: " + id);
		return itHostService.getNic(id);
	}


	@GetMapping("/computing/host-device")
	public String device(String id, Model model) {
		List<HostDeviceVo> device = itHostService.getHostDevice(id);
		model.addAttribute("device", device);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host-device";
	}

	@GetMapping("/computing/host/devicestatus")
	@ResponseBody
	public List<HostDeviceVo> device(String id) {
		log.info("----- host device 일반 불러오기: " + id);
		return itHostService.getHostDevice(id);
	}

	@GetMapping("/computing/host-permission")
	public String permission(String id, Model model) {
		List<PermissionVo> permission = itHostService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host-permission";
	}

	//
	@GetMapping("/computing/host/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id) {
		log.info("----- host permission 불러오기: " + id);
		return itHostService.getPermission(id);
	}


	@GetMapping("/computing/host-aff")
	public String aff(String id, Model model) {
		List<AffinityLabelVo> aff = itHostService.getAffinitylabels(id);
		model.addAttribute("aff", aff);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host-aff";
	}

	@GetMapping("/computing/host/affstatus")
	@ResponseBody
	public List<AffinityLabelVo> aff(String id) {
		log.info("----- host aff 일반 불러오기: " + id);
		return itHostService.getAffinitylabels(id);
	}


	@GetMapping("/computing/host-event")
	public String event(String id, Model model) {
		List<EventVo> event = itHostService.getEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host-event";
	}

	@GetMapping("/computing/host/eventStatus")
	@ResponseBody
	public List<EventVo> event(String id) {
		log.info("----- event 목록 불러오기: " + id);
		return itHostService.getEvent(id);
	}


}
