package com.itinfo.itcloud.controller.storage;

import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVmVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.service.ItMenuService;
import com.itinfo.itcloud.service.ItDomainService;

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
public class DomainController {
	private final ItDomainService itDomainService;
	private final ItMenuService menu;

	@GetMapping("/storage/storageDomains")
	public String domains(Model model){
		List<DomainVo> domains = itDomainService.getList();
		model.addAttribute("domains", domains);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomains";
	}

	@GetMapping("/storage/storageDomain")
	public String domain(String id, Model model){
		DomainVo domain = itDomainService.getDomain(id);
		model.addAttribute("domain", domain);
		model.addAttribute("id", id);
		model.addAttribute("name", itDomainService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain";
	}
	@GetMapping("/storage/storageDomain-datacenter")
	public String datacenter(String id, Model model){
		List<DataCenterVo> datacenter = itDomainService.getDatacenter(id);
		model.addAttribute("datacenter", datacenter);
		model.addAttribute("id", id);
		model.addAttribute("name", itDomainService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain-datacenter";
	}

	@GetMapping("/storage/storageDomain-vm")
	public String vm(String id, Model model){
		List<DomainVmVo> vm = itDomainService.getVm(id);
		model.addAttribute("vm", vm);
		model.addAttribute("id", id);
		model.addAttribute("name", itDomainService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain-vm";
	}

	@GetMapping("/storage/storageDomain-template")
	public String template(String id, Model model){
		List<TemplateVo> template = itDomainService.getTemplate(id);
		model.addAttribute("template", template);
		model.addAttribute("id", id);
		model.addAttribute("name", itDomainService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain-template";
	}

	@GetMapping("/storage/storageDomain-disk")
	public String disk(String id, Model model){
		List<DiskVo> disk = itDomainService.getDisk(id);
		model.addAttribute("disks", disk);
		model.addAttribute("id", id);
		model.addAttribute("name", itDomainService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain-disk";
	}

	@GetMapping("/storage/storageDomain-snapshot")
	public String snapshot(String id, Model model){
		List<SnapshotVo> snapshot = itDomainService.getSnapshot(id);
		model.addAttribute("snapshot", snapshot);
		model.addAttribute("id", id);
	model.addAttribute("name", itDomainService.getName(id));
		return "storage/storageDomain-snapshot";
	}


	@GetMapping("/storage/storageDomain-event")
	public String event(String id, Model model){
		List<EventVo> event = itDomainService.getEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);
		model.addAttribute("name", itDomainService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain-event";
	}

	@GetMapping("/storage/storageDomain-permission")
	public String permission(String id, Model model){
		List<PermissionVo> permission = itDomainService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);
		model.addAttribute("name", itDomainService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain-permission";
	}


	//region: ResponseBody
	@GetMapping("/domainsStatus")
	@ResponseBody
	public List<DomainVo> domains(){
		return itDomainService.getList();
	}

	@GetMapping("/storage/storageDomain/domainStatus")
	@ResponseBody
	public DomainVo domain(String id){
		return itDomainService.getDomain(id);
	}

	@GetMapping("/storage/storageDomain/datacenterStatus")
	@ResponseBody
	public List<DataCenterVo> datacenter(String id){
		return itDomainService.getDatacenter(id);
	}

	@GetMapping("/storage/storageDomain/vmStatus")
	@ResponseBody
	public List<DomainVmVo> vm(String id){
		return itDomainService.getVm(id);
	}

	@GetMapping("/storage/storageDomain/templateStatus")
	@ResponseBody
	public List<TemplateVo> template(String id){
		return itDomainService.getTemplate(id);
	}

	@GetMapping("/storage/storageDomain/diskStatus")
	@ResponseBody
	public List<DiskVo> disk(String id){
		return itDomainService.getDisk(id);
	}

//	@GetMapping("/storage/storageDomain/snapshotStatus")
//	@ResponseBody
//	public List<DiskVo> snapshot(String id){
//		return itStorageDomainService.getSnapshot(id);
//	}

	@GetMapping("/storage/storageDomain/eventStatus")
	@ResponseBody
	public List<EventVo> event(String id){
		return itDomainService.getEvent(id);
	}

	@GetMapping("/storage/storageDomain/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id){
		return itDomainService.getPermission(id);
	}

	//endregion


}
