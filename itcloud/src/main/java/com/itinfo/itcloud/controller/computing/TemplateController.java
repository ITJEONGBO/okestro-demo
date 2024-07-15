package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.service.computing.ItTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "Computing-Template")
@RequestMapping("/computing/templates")
public class TemplateController {
	private final ItTemplateService tService;

	@GetMapping
	@ApiOperation(value = "템플릿 목록", notes = "전체 템플릿 목록을 조회한다")
	@ResponseBody
	public List<TemplateVo> templates(){
		log.info("--- templates 목록");
		return tService.getTemplates();
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "템플릿 상세정보", notes = "템플릿의 상세정보를 조회한다")
	@ApiImplicitParam(name = "id", value = "템플릿 아이디", dataTypeClass = String.class)
	@ResponseBody
	public TemplateVo template(@PathVariable String id){
		log.info("--- template 일반");
		return tService.getTemplateInfo(id);
	}


	@GetMapping("/{id}/disks")
	@ApiOperation(value = "템플릿 디스크 목록", notes = "선택된 템플릿의 디스크 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "템플릿 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<DiskVo> getDisk(@PathVariable String id){
		log.info("--- template 디스크");
		return tService.getDisksByTemplate(id);
	}

	@GetMapping("/{id}/permissions")
	@ApiOperation(value = "템플릿 권한 목록", notes = "선택된 템플릿의 권한 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "템플릿 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<PermissionVo> permissions(@PathVariable String id){
		log.info("--- template 권한");
		return tService.getPermissionsByTemplate(id);
	}

	@GetMapping("/{id}/events")
	@ApiOperation(value = "템플릿 이벤트 목록", notes = "선택된 템플릿의 이벤트 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "템플릿 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<EventVo> events(@PathVariable String id){
		log.info("--- template 이벤트");
		return tService.getEventsByTemplate(id);
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
