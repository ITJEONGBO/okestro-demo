package com.itinfo.service;


import com.itinfo.model.CpuProfileVo;
import com.itinfo.model.EventVo;
import com.itinfo.model.StorageDomainVo;
import com.itinfo.model.TemplateDiskVo;
import com.itinfo.model.TemplateEditVo;
import com.itinfo.model.TemplateVo;
import com.itinfo.model.VmNicVo;
import com.itinfo.model.VmSystemVo;
import java.util.List;

public interface TemplatesService {
	List<TemplateVo> retrieveTemplates();

	TemplateVo retrieveTemplate(String id);

	VmSystemVo retrieveSystemInfo(String id);

	List<VmNicVo> retrieveNicInfo(String id);

	List<StorageDomainVo> retrieveStorageInfo(String id);

	List<EventVo> retrieveEvents(String id);

	List<CpuProfileVo> retrieveCpuProfiles();

	List<TemplateVo> retrieveRootTemplates();

	List<TemplateDiskVo> retrieveDisks(String id);

	Boolean checkDuplicateName(String name);

	void createTemplate(TemplateVo template);

	void removeTemplate(String id);

	TemplateEditVo retrieveTemplateEditInfo(String id);

	String updateTemplate(TemplateEditVo templateEditInfo);

	void exportTemplate(TemplateVo template);

	boolean checkExportTemplate(String id);
}
