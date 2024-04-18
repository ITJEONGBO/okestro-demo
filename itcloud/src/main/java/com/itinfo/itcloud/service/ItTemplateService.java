package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.storage.TempDiskVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItTemplateService {
    List<TemplateVo> getList();

    // 가져오기 창
    // 가져오기
//    TemplateCreateVo setEditTemplate(String id);  // 편집 창
//    CommonVo<Boolean> editTemplate(TemplateCreateVo tVo);   // 편집
//    CommonVo<Boolean> deleteTemplate(String id);        // 삭제
//
//    CommonVo<Boolean> exportTemplate(String id);    // 내보내기 도메인으로 내보내기
//    CommonVo<Boolean> exportOVATemplate(String id); // 내보내기 OVA로 내보내기
//    // 가상머신 생성 창 (템플릿 값이 들어간ㄷ)
//    CommonVo<Boolean> addVm(String id); // 새 가상머신


    TemplateVo getInfo(String id);

    List<TempDiskVo> getDisk(String id);
//    CommonVo<Boolean> copyDisk(String id);  // 복사


    List<PermissionVo> getPermission(String id);
    List<EventVo> getEvent(String id);




//    List<VmVo> getVm(String id);
//    List<NicVo> getNic(String id);
    // 새로만들기
    // 수정
    // 제거
//    TempStorageVo getStorage(String id);
    // 삭제


}
