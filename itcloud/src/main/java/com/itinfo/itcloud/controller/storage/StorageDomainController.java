package com.itinfo.itcloud.controller.storage;

import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.StorageDomainVo;
import com.itinfo.itcloud.service.ItMenuService;
import com.itinfo.itcloud.service.ItStorageDomainService;

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
public class StorageDomainController {
	private final ItStorageDomainService itStorageDomainService;
	private final ItMenuService menu;

	@GetMapping("/storage/storageDomains")
	public String domains(Model model){
		List<StorageDomainVo> domains = itStorageDomainService.getList();
		model.addAttribute("domains", domains);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomains";
	}

	@GetMapping("/domainsStatus")
	@ResponseBody
	public List<StorageDomainVo> domains(){
		return itStorageDomainService.getList();
	}


	@GetMapping("/storage/storageDomain")
	public String domain(String id, Model model){
		StorageDomainVo domain = itStorageDomainService.getDomain(id);
		model.addAttribute("domain", domain);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain";
	}

	@GetMapping("/storage/storageDomain/domainStatus")
	@ResponseBody
	public StorageDomainVo domain(String id){
		return itStorageDomainService.getDomain(id);
	}


	@GetMapping("/storage/storageDomain-datacenter")
	public String datacenter(String id, Model model){
		List<DataCenterVo> datacenter = itStorageDomainService.getDatacenter(id);
		model.addAttribute("datacenter", datacenter);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain-datacenter";
	}

	@GetMapping("/storage/storageDomain/datacenterStatus")
	@ResponseBody
	public List<DataCenterVo> datacenter(String id){
		return itStorageDomainService.getDatacenter(id);
	}


	@GetMapping("/storage/storageDomain-vm")
	public String vm(String id, Model model){
		List<VmVo> vm = itStorageDomainService.getVm(id);
		model.addAttribute("vm", vm);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain-vm";
	}

	@GetMapping("/storage/storageDomain/vmStatus")
	@ResponseBody
	public List<VmVo> vm(String id){
		return itStorageDomainService.getVm(id);
	}



	@GetMapping("/storage/storageDomain-template")
	public String template(String id, Model model){
		List<TemplateVo> template = itStorageDomainService.getTemplate(id);
		model.addAttribute("template", template);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain-template";
	}

	@GetMapping("/storage/storageDomain/templateStatus")
	@ResponseBody
	public List<TemplateVo> template(String id){
		return itStorageDomainService.getTemplate(id);
	}


	@GetMapping("/storage/storageDomain-disk")
	public String disk(String id, Model model){
		List<DiskVo> disk = itStorageDomainService.getDisk(id);
		model.addAttribute("disk", disk);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain-disk";
	}

	@GetMapping("/storage/storageDomain/diskStatus")
	@ResponseBody
	public List<DiskVo> disk(String id){
		return itStorageDomainService.getDisk(id);
	}


//	@GetMapping("/storage/storageDomain-snapshot")
//	public String snapshot(String id, Model model){
//		List<DiskVo> snapshot = itStorageDomainService.getSnapshot(id);
//		model.addAttribute("snapshot", snapshot);
//		model.addAttribute("id", id);
//		return "storage/storageDomain-snapshot";
//	}
//
//	@GetMapping("/storage/storageDomain/snapshotStatus")
//	@ResponseBody
//	public List<DiskVo> snapshot(String id){
//		return itStorageDomainService.getSnapshot(id);
//	}


	@GetMapping("/storage/storageDomain-event")
	public String event(String id, Model model){
		List<EventVo> event = itStorageDomainService.getEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain-event";
	}

	@GetMapping("/storage/storageDomain/eventStatus")
	@ResponseBody
	public List<EventVo> event(String id){
		return itStorageDomainService.getEvent(id);
	}


	@GetMapping("/storage/storageDomain-permission")
	public String permission(String id, Model model){
		List<PermissionVo> permission = itStorageDomainService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/storageDomain-permission";
	}

	@GetMapping("/storage/storageDomain/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id){
		return itStorageDomainService.getPermission(id);
	}



}
