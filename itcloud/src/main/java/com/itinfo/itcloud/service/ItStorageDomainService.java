package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.StorageDomainVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItStorageDomainService {
    List<StorageDomainVo> getList();
    StorageDomainVo getDomain(String id);
    List<DataCenterVo> getDatacenter(String id);
    List<VmVo> getVm(String id);
    List<TemplateVo> getTemplate(String id);
    List<DiskVo> getDisk(String id);
    // 디스크 스냅샷
    List<EventVo> getEvent(String id);
    List<PermissionVo> getPermission(String id);
}
