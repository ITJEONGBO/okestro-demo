package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.storage.StorageDomainVo;
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

	@GetMapping("/storage/storageDomains")
	public String domains(Model model){
		List<StorageDomainVo> domains = itStorageDomainService.getList();
		model.addAttribute("domains", domains);
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
		return "storage/storageDomain-datacenter";
	}

	@GetMapping("/storage/storageDomain/datacenterStatus")
	@ResponseBody
	public List<DataCenterVo> datacenter(String id){
		return itStorageDomainService.getDatacenter(id);
	}





	@GetMapping("/storage/storageDomain-permission")
	public String permission(String id, Model model){
		List<PermissionVo> permission = itStorageDomainService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);
		return "storage/storageDomain-permission";
	}

	@GetMapping("/storage/storageDomain/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id){
		return itStorageDomainService.getPermission(id);
	}


	@GetMapping("/storage/storageDomain-event")
	public String event(String id, Model model){
		List<EventVo> event = itStorageDomainService.getEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);
		return "storage/storageDomain-event";
	}

	@GetMapping("/storage/storageDomain/eventStatus")
	@ResponseBody
	public List<EventVo> event(String id){
		return itStorageDomainService.getEvent(id);
	}


}
