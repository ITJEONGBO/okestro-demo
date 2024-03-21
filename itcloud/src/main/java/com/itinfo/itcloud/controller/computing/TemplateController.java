package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.service.ItMenuService;
import com.itinfo.itcloud.service.ItTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/computing")
public class TemplateController {
	private final ItTemplateService itTemplateService;
	private final ItMenuService menu;

	@GetMapping("/templates")
	public String templates(Model model){
		List<TemplateVo> templates = itTemplateService.getList();
		model.addAttribute("templates", templates);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "computing/templates";
	}

	@GetMapping("/template")
	public String template(String id, Model model){
		TemplateVo template = itTemplateService.getInfo(id);
		model.addAttribute("template", template);
		model.addAttribute("id", id);
		model.addAttribute("name", itTemplateService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "computing/template";
	}

	@GetMapping("/template-vm")
	public String vm(String id, Model model){
		List<VmVo> vm = itTemplateService.getVm(id);
		model.addAttribute("vm", vm);
		model.addAttribute("id", id);
		model.addAttribute("name", itTemplateService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "computing/template-vm";
	}

	@GetMapping("/template-nic")
	public String nic(String id, Model model){
		List<NicVo> nic = itTemplateService.getNic(id);
		model.addAttribute("nic", nic);
		model.addAttribute("id", id);
		model.addAttribute("name", itTemplateService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "computing/template-nic";
	}

	@GetMapping("/template-disk")
	public String disk(String id, Model model){
		List<VmDiskVo> disk = itTemplateService.getDisk(id);
		model.addAttribute("disk", disk);
		model.addAttribute("id", id);
		model.addAttribute("name", itTemplateService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "computing/template-disk";
	}

	@GetMapping("/template-storage")
	public String storage(String id, Model model){
		List<DomainVo> storage = itTemplateService.getStorage(id);
		model.addAttribute("storage", storage);
		model.addAttribute("id", id);
		model.addAttribute("name", itTemplateService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "computing/template-storage";
	}

	@GetMapping("/template-permission")
	public String permission(String id, Model model){
		List<PermissionVo> permission = itTemplateService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);
		model.addAttribute("name", itTemplateService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "computing/template-permission";
	}

	@GetMapping("/template-event")
	public String event(String id, Model model){
		List<EventVo> event = itTemplateService.getEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);
		model.addAttribute("name", itTemplateService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "computing/template-event";
	}



	//region: ResponseBody
	@GetMapping("/templatesStatus")
	@ResponseBody
	public List<TemplateVo> temps(){
		return itTemplateService.getList();
	}

	@GetMapping("/templateStatus")
	@ResponseBody
	public TemplateVo temp(String id){
		return itTemplateService.getInfo(id);
	}

	@GetMapping("/template-vmStatus")
	@ResponseBody
	public List<VmVo> vm(String id){
		return itTemplateService.getVm(id);
	}
	@GetMapping("/template-nicStatus")
	@ResponseBody
	public List<NicVo> nic(String id){
		return itTemplateService.getNic(id);
	}

	@GetMapping("/template-diskStatus")
	@ResponseBody
	public List<VmDiskVo> disk(String id){
		return itTemplateService.getDisk(id);
	}

	@GetMapping("/template-storageStatus")
	@ResponseBody
	public List<DomainVo> storage(String id){
		return itTemplateService.getStorage(id);
	}

	@GetMapping("/template-permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id){
		return itTemplateService.getPermission(id);
	}

	@GetMapping("/template-eventStatus")
	@ResponseBody
	public List<EventVo> event(String id){
		return itTemplateService.getEvent(id);
	}

	//endregion
}
