package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.storage.TempStorageVo;
import com.itinfo.itcloud.service.ItTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/computing")
public class TemplateController {
	private final ItTemplateService tService;

	//region: ResponseBody
	@GetMapping("/templatesStatus")
	@ResponseBody
	public List<TemplateVo> temps(){
		return tService.getList();
	}

	@GetMapping("/templateStatus")
	@ResponseBody
	public TemplateVo temp(String id){
		return tService.getInfo(id);
	}



	@GetMapping("/template-storageStatus")
	@ResponseBody
	public TempStorageVo storage(String id){
		return tService.getStorage(id);
	}

	@GetMapping("/template-permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id){
		return tService.getPermission(id);
	}

	@GetMapping("/template-eventStatus")
	@ResponseBody
	public List<EventVo> event(String id){
		return tService.getEvent(id);
	}

	//endregion






	// region : 안쓸거 같음
	//	@GetMapping("/template-vmStatus")
//	@ResponseBody
//	public List<VmVo> vm(String id){
//		return tService.getVm(id);
//	}
//	@GetMapping("/template-nicStatus")
//	@ResponseBody
//	public List<NicVo> nic(String id){
//		return tService.getNic(id);
//	}

//	@GetMapping("/template-diskStatus")
//	@ResponseBody
//	public List<TemDiskVo> disk(String id){
//		return tService.getDisk(id);
//	}
	// endregion
}
