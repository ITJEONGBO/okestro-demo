package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.create.TemplateCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.model.storage.TemDiskVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItTemplateService {
    String getName(String id);
    List<TemplateVo> getList();
    TemplateVo getInfo(String id);
    List<TemDiskVo> getDisk(String id);
    List<DomainVo> getStorage(String id);
    List<PermissionVo> getPermission(String id);
    List<EventVo> getEvent(String id);


    CommonVo<Boolean> addTemplate(TemplateCreateVo tVo);
    CommonVo<Boolean> editTemplate(TemplateCreateVo tVo);
    CommonVo<Boolean> deleteTemplate(String id);


//    List<VmVo> getVm(String id);

}
