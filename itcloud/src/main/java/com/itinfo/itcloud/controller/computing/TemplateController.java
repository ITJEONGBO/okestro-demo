package com.itinfo.itcloud.controller.computing;

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
	private final ItTemplateService itTemplateService;

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



	@GetMapping("/template-storageStatus")
	@ResponseBody
	public TempStorageVo storage(String id){
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






	// region : 안쓸거 같음
	//	@GetMapping("/template-vmStatus")
//	@ResponseBody
//	public List<VmVo> vm(String id){
//		return itTemplateService.getVm(id);
//	}
//	@GetMapping("/template-nicStatus")
//	@ResponseBody
//	public List<NicVo> nic(String id){
//		return itTemplateService.getNic(id);
//	}

//	@GetMapping("/template-diskStatus")
//	@ResponseBody
//	public List<TemDiskVo> disk(String id){
//		return itTemplateService.getDisk(id);
//	}
	// endregion
}
