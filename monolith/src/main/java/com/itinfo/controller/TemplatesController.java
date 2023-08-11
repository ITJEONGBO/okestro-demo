package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.TemplatesService;
import com.itinfo.model.CpuProfileVo;
import com.itinfo.model.TemplateDiskVo;
import com.itinfo.model.TemplateEditVo;
import com.itinfo.model.TemplateVo;

import java.util.List;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@Api(value = "TemplatesController", tags = {"templates"})
public class TemplatesController {
	@Autowired private TemplatesService templatesService;

	@ApiOperation(httpMethod = "GET", value = "templatesView", notes = "페이지 이동 > /compute/templates")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/templates"})
	public String templatesView() {
		log.info("... templatesView");
		return "/castanets/compute/templates";
	}

	@ApiOperation(httpMethod = "GET", value = "updateTemplateView", notes = "페이지 이동 > /compute/updateTemplateInfo")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/updateTemplateInfo"})
	public String updateTemplateView() {
		log.info("... updateTemplateView");
		return "/castanets/compute/updateTemplate";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveTemplates", notes = "탬플릿 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/template/retrieveTemplates"})
	public String retrieveTemplates(Model model) {
		log.info("... retrieveTemplates");
		List<TemplateVo> tamplates = templatesService.retrieveTemplates();
		model.addAttribute(ItInfoConstant.RESULT_KEY, tamplates);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveTemplate", notes = "탬플릿 상세 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value="탬플릿 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/template/retrieveTemplate"})
	public String retrieveTemplate(String id,
								   Model model) {
		log.info("... retrieveTemplate('{}')", id);
		TemplateVo template = templatesService.retrieveTemplate(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, template);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveCpuProfiles", notes = "CPU 프로필 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/template/cpuProfiles"})
	public String retrieveCpuProfiles(Model model) {
		log.info("... retrieveCpuProfiles");
		List<CpuProfileVo> cpuProfiles = templatesService.retrieveCpuProfiles();
		model.addAttribute(ItInfoConstant.RESULT_KEY, cpuProfiles);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveRootTemplates", notes = "ROOT 탬플릿 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/template/rootTemplates"})
	public String retrieveRootTemplates(Model model) {
		log.info("... retrieveRootTemplates");
		List<TemplateVo> rootTemplates = templatesService.retrieveRootTemplates();
		model.addAttribute(ItInfoConstant.RESULT_KEY, rootTemplates);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveDisks", notes = "디스크 목록 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value="VM ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/template/retrieveDisks"})
	public String retrieveDisks(String id,
								Model model) {
		log.info("... retrieveDisks('{}')", id);
		List<TemplateDiskVo> list = templatesService.retrieveDisks(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, list);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "createTemplate", notes = "탬플릿 생성")
	@ApiImplicitParams({
			@ApiImplicitParam(name="template", value="생성할 탬플릿 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/createTemplate"})
	public String createTemplate(@RequestBody TemplateVo template,
								 Model model) {
		log.info("... createTemplate");
		templatesService.createTemplate(template);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "checkDuplicateName", notes = "ROOT 탬플릿 목록 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="name", value="검색대상 이름")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/template/checkDuplicateName"})
	public String checkDuplicateName(String name,
									 Model model) {
		log.info("... checkDuplicateName('{}')", name);
		Boolean result = templatesService.checkDuplicateName(name);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "removeTemplate", notes = "탬플릿 삭제")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value="삭제할 탬플릿 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/removeTemplate"})
	public String removeTemplate(String id,
								 Model model) {
		log.info("... removeTemplate('{}')", id);
		templatesService.removeTemplate(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveTemplateEditInfo", notes = "탬플릿 수정 정보 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value="수정할 탬플릿 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/retrieveTemplateEditInfo"})
	public String retrieveTemplateEditInfo(String id,
										   Model model) {
		log.info("... retrieveTemplateEditInfo('{}')", id);
		TemplateEditVo templateEditInfo = templatesService.retrieveTemplateEditInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, templateEditInfo);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "updateTemplate", notes = "탬플릿 수정")
	@ApiImplicitParams({
			@ApiImplicitParam(name="templateEditInfo", value="수정할 탬플릿 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/updateTemplate"})
	public String updateTemplate(@RequestBody TemplateEditVo templateEditInfo,
								 Model model) {
		log.info("... updateTemplate");
		String result = templatesService.updateTemplate(templateEditInfo);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "exportTemplate", notes = "탬플릿 내보내기")
	@ApiImplicitParams({
			@ApiImplicitParam(name="template", value="내보낼 탬플릿 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/exportTemplate"})
	public String exportTemplate(@RequestBody TemplateVo template,
								 Model model) {
		log.info("... exportTemplate");
		templatesService.exportTemplate(template);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "checkExportTemplate", notes = "탬플릿 내보내기 전 확인")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value="내보낼 탬플릿 id")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/checkExportTemplate"})
	public String checkExportTemplate(String id, Model model) {
		log.info("... checkExportTemplate('{}')", id);
		boolean result = templatesService.checkExportTemplate(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}
}
