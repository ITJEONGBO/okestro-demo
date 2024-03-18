package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.create.HostCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.service.ItHostService;

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
public class HostController {
	private final ItHostService itHostService;
	private final ItMenuService menu;

	@GetMapping("/hosts")
	public String hosts(Model model) {
		List<HostVo> hosts = itHostService.getList();
		model.addAttribute("hosts", hosts);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		log.info("***** hosts 목록 화면출력");

		return "computing/hosts";
	}

	@GetMapping("/host")
	public String host(String id, Model model) {
		HostVo host = itHostService.getInfo(id);
		model.addAttribute("host", host);
		model.addAttribute("id", id);
		model.addAttribute("name", itHostService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host";
	}

	@GetMapping("/host/vm")
	public String vm(String id, Model model) {
		List<VmVo> vm = itHostService.getVm(id);
		model.addAttribute("vm", vm);
		model.addAttribute("id", id);
		model.addAttribute("name", itHostService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host-vm";
	}

	@GetMapping("/host/nic")
	public String nic(String id, Model model) {
		List<NicVo> nic = itHostService.getNic(id);
		model.addAttribute("nic", nic);
		model.addAttribute("id", id);
		model.addAttribute("name", itHostService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host-nic";
	}

	@GetMapping("/host/device")
	public String device(String id, Model model) {
		List<HostDeviceVo> device = itHostService.getHostDevice(id);
		model.addAttribute("device", device);
		model.addAttribute("id", id);
		model.addAttribute("name", itHostService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host-device";
	}


	@GetMapping("/host/affinityLabel")
	public String aff(String id, Model model) {
		List<AffinityLabelVo> aff = itHostService.getAffinitylabels(id);
		model.addAttribute("aff", aff);
		model.addAttribute("id", id);
		model.addAttribute("name", itHostService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host-aff";
	}

	@GetMapping("/host/affinityLabels")
	@ResponseBody
	public List<AffinityLabelVo> getAffinitylabels(String id){
		return itHostService.getAffinitylabels(id);
	}

	// 해당 cluster가 가지고 있는 host, 레이블 생성시 필요
	@GetMapping("/host/hostme")
	@ResponseBody
	public List<HostVo> getHostMember(String clusterId) {
		log.info("-----클러스터 호스트목록");
		return itHostService.getHostMember(clusterId);
	}

	// 해당 cluster가 가지고 있는 vm, 레이블 생성시 필요
	@GetMapping("/host/vmme")
	@ResponseBody
	public List<VmVo> getVmMember(String clusterId) {
		log.info("-----클러스터 가상머신목록");
		return itHostService.getVmMember(clusterId);
	}


	// 클러스터 선호도 레이블 생성
	@PostMapping("/host/affinityLabel/add")
	public CommonVo<Boolean> addAff(@RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- 선호도 레이블 생성");
		return itHostService.addAffinitylabel(alVo);
	}

	// 클러스터 선호도 레이블 편집
	@PostMapping("/host/affinityLabel/edit")
	public CommonVo<Boolean> editAff(@RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- 선호도 레이블 편집");
		return itHostService.editAffinitylabel(alVo);
	}

	// 클러스터 선호도 레이블 삭제
	@PostMapping("/host/affinityLabel/delete")
	public CommonVo<Boolean> deleteAff(String id) {
		log.info("--- 선호도 레이블 삭제");
		return itHostService.deleteAffinitylabel(id);
	}



	@GetMapping("/host/permission")
	public String permission(String id, Model model) {
		List<PermissionVo> permission = itHostService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);
		model.addAttribute("name", itHostService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host-permission";
	}


	@GetMapping("/host/event")
	public String event(String id, Model model) {
		List<EventVo> event = itHostService.getEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);
		model.addAttribute("name", itHostService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "computing/host-event";
	}


	@GetMapping("/host/add")
	public String add(Model model) {
		List<ClusterVo> cList = itHostService.getClusterList();
		model.addAttribute("c", cList);


		return "computing/host-add";
	}

	// host 생성
	@PostMapping("/host/add2")
	public String add2(Model model, @ModelAttribute HostCreateVo hVo) {
		if(itHostService.addHost(hVo)){
			model.addAttribute("result", "호스트 생성 완료");
		}else{
			model.addAttribute("result", "호스트 생성 실패");
		}
		return "computing/host-add2";
	}

	// host 수정 창출력
	@GetMapping("/host/edit")
	public String edit(Model model, String id) {
		HostCreateVo hVo = itHostService.getHostCreate(id);

		model.addAttribute("h", hVo);
		return "computing/host-edit";
	}

	// 클러스터 수정
	@PostMapping("/host/edit2")
	public String edit2(Model model, @ModelAttribute HostCreateVo hVo) {
		log.info("edit 시작");

		itHostService.editHost(hVo);
		model.addAttribute("result", "호스트 수정 완료");
		return "computing/host-edit2";
	}

	@GetMapping("/host/delete")
	public String delete(Model model, String id){
		model.addAttribute("id", id);
		model.addAttribute("name", itHostService.getName(id));

		return "computing/host-delete";
	}

	@PostMapping("/host/delete2")
	public String delete2(Model model, @RequestParam String id){
		if(itHostService.deleteHost(id)){
			model.addAttribute("result", "완료");
		}else {
			model.addAttribute("result", "실패");
		}

		return "computing/host-delete2";
	}


	@PostMapping("/host/reboot")
	public String reboot(Model model, @ModelAttribute String hostId) {
		log.info("reboot 시작");
		Boolean rebootSuccess =
				itHostService.rebootHost(hostId);
		log.info("rebootSuccess: {}", rebootSuccess);
		model.addAttribute("result", "호스트 재기동 완료");
		return rebootSuccess.toString();
	}

	@GetMapping("/host/active")
	public String active(Model model, String id){
		model.addAttribute("id", id);
		model.addAttribute("name", itHostService.getName(id));

		return "computing/host-active";
	}

	@PostMapping("/host/active2")
	@ResponseBody
	public void active2(String id){
		itHostService.active(id);
	}

	@GetMapping("/host-deActive")
	public String deactive(Model model, String id){
		model.addAttribute("id", id);
		model.addAttribute("name", itHostService.getName(id));

		return "computing/host-deactive";
	}

	@PostMapping("/host-deActive2")
	@ResponseBody
	public void deactive2(Model model, String id){
		itHostService.deActive(id);
	}


	@GetMapping("/host/restart")
	public String restart(Model model, String id){
		model.addAttribute("id", id);
		model.addAttribute("name", itHostService.getName(id));

		return "computing/host-restart";
	}

	@PostMapping("/host/restart2")
	@ResponseBody
	public void restart2(Model model, String id){
		itHostService.reStart(id);
	}



	@GetMapping("/host/refresh")
	public String refresh(Model model, String id){
		model.addAttribute("id", id);
		model.addAttribute("name", itHostService.getName(id));

		return "computing/host-refresh";
	}

	@PostMapping("/host/refresh2")
	@ResponseBody
	public void refresh2(String id){
		itHostService.refresh(id);
	}






	//region: ResponseBody
	@GetMapping("/hosts/status")
	@ResponseBody
	public List<HostVo> hosts() {
		log.info("----- Host 목록 불러오기");
		return itHostService.getList();
	}

	@GetMapping("/host/status")
	@ResponseBody
	public HostVo host(String id) {
		log.info("----- host id 일반 불러오기: " + id);
		return itHostService.getInfo(id);
	}

	@GetMapping("/host/vmstatus")
	@ResponseBody
	public List<VmVo> vm(String id) {
		log.info("----- host vm 일반 불러오기: " + id);
		return itHostService.getVm(id);
	}

	@GetMapping("/host/nicstatus")
	@ResponseBody
	public List<NicVo> nic(String id) {
		log.info("----- host nic 일반 불러오기: " + id);
		return itHostService.getNic(id);
	}

	@GetMapping("/host/devicestatus")
	@ResponseBody
	public List<HostDeviceVo> device(String id) {
		log.info("----- host device 일반 불러오기: " + id);
		return itHostService.getHostDevice(id);
	}

	@GetMapping("/host/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id) {
		log.info("----- host permission 불러오기: " + id);
		return itHostService.getPermission(id);
	}

	@GetMapping("/host/affstatus")
	@ResponseBody
	public List<AffinityLabelVo> aff(String id) {
		log.info("----- host aff 일반 불러오기: " + id);
		return itHostService.getAffinitylabels(id);
	}

	@GetMapping("/host/eventStatus")
	@ResponseBody
	public List<EventVo> event(String id) {
		log.info("----- event 목록 불러오기: " + id);
		return itHostService.getEvent(id);
	}

	//endregion

}
