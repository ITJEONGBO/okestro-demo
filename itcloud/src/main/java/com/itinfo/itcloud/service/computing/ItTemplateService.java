package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.TemplateCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItTemplateService {
    List<TemplateVo> getList();

    CommonVo<Boolean> addTemplate(String vmId, TemplateVo templateVo); // 생성창
    TemplateCreateVo setEditTemplate(String id);  // 편집 창
    CommonVo<Boolean> editTemplate(TemplateCreateVo tVo);   // 편집
    CommonVo<Boolean> deleteTemplate(String id);        // 삭제


    TemplateVo getInfo(String id);

    List<VmVo> getVm(String id);
    List<NicVo> getNic(String id);  // 네트워크 인터페이스
    // 생성, 수정, 제거

    List<DiskVo> getDisk(String id);    // 디스크
    List<DomainVo> getDomain(String id);    // 스토리지
    List<PermissionVo> getPermission(String id);    // 권한
    List<EventVo> getEvent(String id);
}
