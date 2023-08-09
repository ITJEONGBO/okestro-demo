package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.TemplatesService;
import com.itinfo.model.CpuProfileVo;
import com.itinfo.model.TemplateDiskVo;
import com.itinfo.model.TemplateEditVo;
import com.itinfo.model.TemplateVo;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class TemplatesController {
	@Autowired private TemplatesService templatesService;

	@RequestMapping({"/compute/templates"})
	public String templates() {
		log.info("... templates");
		return "/castanets/compute/templates";
	}

	@RequestMapping({"/compute/updateTemplateInfo"})
	public String updateTemplate() {
		log.info("... updateTemplate");
		return "/castanets/compute/updateTemplate";
	}

	@RequestMapping(value = {"/compute/template/retrieveTemplates"}, method = {RequestMethod.GET})
	public String retrieveTemplates(Model model) {
		log.info("... retrieveTemplates");
		List<TemplateVo> tamplates = templatesService.retrieveTemplates();
		model.addAttribute(ItInfoConstant.RESULT_KEY, tamplates);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/template/retrieveTemplate"}, method = {RequestMethod.GET})
	public String retrieveTemplate(String id, Model model) {
		log.info("... retrieveTemplate('{}')", id);
		TemplateVo template = templatesService.retrieveTemplate(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, template);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/template/cpuProfiles"}, method = {RequestMethod.GET})
	public String retrieveCpuProfiles(Model model) {
		log.info("... retrieveCpuProfiles");
		List<CpuProfileVo> cpuProfiles = templatesService.retrieveCpuProfiles();
		model.addAttribute(ItInfoConstant.RESULT_KEY, cpuProfiles);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/template/rootTemplates"}, method = {RequestMethod.GET})
	public String retrieveRootTemplates(Model model) {
		log.info("... retrieveRootTemplates");
		List<TemplateVo> rootTemplates = templatesService.retrieveRootTemplates();
		model.addAttribute(ItInfoConstant.RESULT_KEY, rootTemplates);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/template/retrieveDisks"}, method = {RequestMethod.GET})
	public String retrieveDisks(String id, Model model) {
		log.info("... retrieveDisks('{}')", id);
		List<TemplateDiskVo> list = templatesService.retrieveDisks(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, list);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/template/checkDuplicateName"})
	public String checkDuplicateName(String name, Model model) {
		log.info("... checkDuplicateName('{}')", name);
		Boolean result = templatesService.checkDuplicateName(name);
		model.addAttribute(ItInfoConstant.RESULT_KEY, Boolean.valueOf(result));
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/createTemplate"})
	public String createTemplate(@RequestBody TemplateVo template, Model model) {
		log.info("... createTemplate");
		templatesService.createTemplate(template);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/removeTemplate"})
	public String removeTemplate(String id, Model model) {
		log.info("... removeTemplate('{}')", id);
		templatesService.removeTemplate(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/retrieveTemplateEditInfo"})
	public String retrieveTemplateEditInfo(String id, Model model) {
		log.info("... retrieveTemplateEditInfo('{}')", id);
		TemplateEditVo templateEditInfo = templatesService.retrieveTemplateEditInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, templateEditInfo);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/updateTemplate"})
	public String updateTemplate(@RequestBody TemplateEditVo templateEditInfo, Model model) {
		log.info("... updateTemplate");
		String result = templatesService.updateTemplate(templateEditInfo);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/exportTemplate"})
	public String exportTemplate(@RequestBody TemplateVo template, Model model) {
		log.info("... exportTemplate");
		templatesService.exportTemplate(template);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/checkExportTemplate"})
	public String checkExportTemplate(String id, Model model) {
		log.info("... checkExportTemplate('{}')", id);
		boolean result = templatesService.checkExportTemplate(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}
}
