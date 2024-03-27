package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVmVo;
import com.itinfo.itcloud.model.storage.StorageDomainVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItDomainService {
    String getName(String id);
    List<StorageDomainVo> getList();
    StorageDomainVo getDomain(String id);
    List<DataCenterVo> getDatacenter(String id);
    List<DomainVmVo> getVm(String id);
    List<TemplateVo> getTemplate(String id);
    List<DiskVo> getDisk(String id);
    List<SnapshotVo> getSnapshot(String id);
    List<EventVo> getEvent(String id);
    List<PermissionVo> getPermission(String id);
}
