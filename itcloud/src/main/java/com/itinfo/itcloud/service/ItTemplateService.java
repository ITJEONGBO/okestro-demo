package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.StorageDomainVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItTemplateService {
    String getName(String id);
    List<TemplateVo> getList();
    TemplateVo getInfo(String id);
    List<VmVo> getVm(String id);
    List<NicVo> getNic(String id);
    List<VmDiskVo> getDisk(String id);
    List<StorageDomainVo> getStorage(String id);
    List<PermissionVo> getPermission(String id);
    List<EventVo> getEvent(String id);
}
