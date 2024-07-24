package com.itinfo.itcloud.controller.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.controller.BaseController
import com.itinfo.itcloud.error.toException
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.itcloud.model.computing.EventVo
import com.itinfo.itcloud.model.computing.TemplateVo
import com.itinfo.itcloud.model.response.Res
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.storage.DiskAttachmentVo
import com.itinfo.itcloud.service.computing.ItTemplateService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody


@Controller
@Api(tags = ["Computing-Template"])
@RequestMapping("/computing/templates")
class TemplateController: BaseController() {
	@Autowired private lateinit var iTemplate: ItTemplateService

	@GetMapping
	@ApiOperation(
		httpMethod="GET",
		value="템플릿 목록",
		notes="전체 템플릿 목록을 조회한다"
	)
	@ResponseBody
	fun findAll(): Res<List<TemplateVo>> {
		log.info("--- templates 목록")
		return Res.safely { iTemplate.findAll() }
	}

	@GetMapping("/{templateId}")
	@ApiOperation(
		httpMethod="GET",
		value="템플릿 상세정보",
		notes="템플릿의 상세정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="templateId", value="템플릿 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ResponseBody
	fun findOne(
		@PathVariable templateId: String? = null,
	): Res<TemplateVo?> {
		if (templateId.isNullOrEmpty())
			throw ErrorPattern.TEMPLATE_ID_NOT_FOUND.toException()
		log.info("--- template 일반")
		return Res.safely { iTemplate.findOne(templateId) }
	}

	@GetMapping("/{templateId}/disks")
	@ApiOperation(
		httpMethod="GET",
		value="템플릿 디스크 목록",
		notes="선택된 템플릿의 디스크 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="templateId", value="템플릿 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ResponseBody
	fun findAllDisksFromTemplate(
		@PathVariable templateId: String? = null,
	): Res<List<DiskAttachmentVo>> {
		if (templateId.isNullOrEmpty())
			throw ErrorPattern.TEMPLATE_ID_NOT_FOUND.toException()
		log.info("--- template 디스크")
		return Res.safely { iTemplate.findAllDisksFromTemplate(templateId) }
	}

	@GetMapping("/{templateId}/permissions")
	@ApiOperation(
		httpMethod="GET",
		value="템플릿 권한 목록",
		notes="선택된 템플릿의 권한 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="templateId", value="템플릿 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ResponseBody
	fun findAllPermissions(
		@PathVariable templateId: String? = null,
	): Res<List<PermissionVo>> {
		if (templateId.isNullOrEmpty())
			throw ErrorPattern.TEMPLATE_ID_NOT_FOUND.toException()
		log.info("--- template 권한")
		return Res.safely { iTemplate.findAllPermissionsFromTemplate(templateId) }
	}

	@GetMapping("/{templateId}/events")
	@ApiOperation(
		httpMethod="GET",
		value="템플릿 이벤트 목록",
		notes="선택된 템플릿의 이벤트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="templateId", value="템플릿 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ResponseBody
	fun findAllEventsFromTemplate(
		@PathVariable templateId: String? = null,
	): Res<List<EventVo>> {
		if (templateId.isNullOrEmpty())
			throw ErrorPattern.TEMPLATE_ID_NOT_FOUND.toException()
		log.info("--- template 이벤트")
		return Res.safely { iTemplate.findAllEventsFromTemplate(templateId) }
	}
/*
	@GetMapping("/{templateId}/vms")
	@ResponseBody
	public List<VmVo> vms(@PathVariable String id){
		log.info("--- template 일반");
		return tService.getVm(id);
	}
	
	@GetMapping("/templates/{id}/nics")
	@ResponseBody
	public List<NicVo> nics(@PathVariable String id){
		log.info("--- template 일반");
		return tService.getNic(id);
	}
*/
	companion object {
		private val log by LoggerDelegate()
	}
}