package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.service.ItTemplateService;
import io.swagger.annotations.Api;
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
@Api(tags = "template")
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
	public TemplateVo template(@PathVariable String id){
		log.info("--- template 일반");
		return tService.getInfo(id);
	}


	@GetMapping("/templates/{id}/disks")
	@ResponseBody
	public List<DiskVo> getDisk(@PathVariable String id){
		log.info("--- template 디스크");
		return tService.getDisk(id);
	}

	@GetMapping("/templates/{id}/permissions")
	@ResponseBody
	public List<PermissionVo> permissions(@PathVariable String id){
		log.info("--- template 권한");
		return tService.getPermission(id);
	}

	@GetMapping("/templates/{id}/events")
	@ResponseBody
	public List<EventVo> events(@PathVariable String id){
		log.info("--- template 이벤트");
		return tService.getEvent(id);
	}





//	@GetMapping("/templates/{id}/vms")
//	@ResponseBody
//	public List<VmVo> vms(@PathVariable String id){
//		log.info("--- template 일반");
//		return tService.getVm(id);
//	}

//	@GetMapping("/templates/{id}/nics")
//	@ResponseBody
//	public List<NicVo> nics(@PathVariable String id){
//		log.info("--- template 일반");
//		return tService.getNic(id);
//	}




}
