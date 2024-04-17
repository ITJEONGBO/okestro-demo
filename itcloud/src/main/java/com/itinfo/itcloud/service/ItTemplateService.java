package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.TemplateCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.storage.TempStorageVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItTemplateService {
    List<TemplateVo> getList();
    TemplateVo getInfo(String id);
    TempStorageVo getStorage(String id);
    List<PermissionVo> getPermission(String id);
    List<EventVo> getEvent(String id);


    CommonVo<Boolean> addTemplate(TemplateCreateVo tVo);
    CommonVo<Boolean> editTemplate(TemplateCreateVo tVo);
    CommonVo<Boolean> deleteTemplate(String id);


    List<VmVo> getVm(String id);

    List<NicVo> getNic(String id);
//    List<TemDiskVo> getDisk(String id);

}
