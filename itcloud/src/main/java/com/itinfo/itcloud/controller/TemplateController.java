package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.storage.TempStorageVo;
import com.itinfo.itcloud.service.ItTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/computing")
public class TemplateController {
	private final ItTemplateService tService;

	@GetMapping("/templates")
	@ResponseBody
	public List<TemplateVo> templates(){
		log.info("--- templates 목록");
		return tService.getList();
	}

	@GetMapping("/templates/{id}")
	@ResponseBody
	public TemplateVo temp(@PathVariable String id){
		log.info("--- template 일반");
		return tService.getInfo(id);
	}

	@GetMapping("/templates/{id}/vms")
	@ResponseBody
	public List<VmVo> vm(String id){
		return tService.getVm(id);
	}

	@GetMapping("/templates/{id}/nics")
	@ResponseBody
	public List<NicVo> nic(String id){
		return tService.getNic(id);
	}

	@GetMapping("/templates/{id}/storages")
	@ResponseBody
	public TempStorageVo storage(@PathVariable String id){
		return tService.getStorage(id);
	}

	@GetMapping("/templates/{id}/permissions")
	@ResponseBody
	public List<PermissionVo> permission(String id){
		return tService.getPermission(id);
	}

	@GetMapping("/templates/{id}/events")
	@ResponseBody
	public List<EventVo> event(String id){
		return tService.getEvent(id);
	}

	


//	@GetMapping("/templates/{id}/disks")
//	@ResponseBody
//	public List<TemDiskVo> disk(String id){
//		return tService.getDisk(id);
//	}
	
}
