package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.VmCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.storage.VmDiskVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItVmService {
    List<VmVo> getList();


    List<VmSetVo> setVmSet();               // 생성 창
    CommonVo<Boolean> addVm(VmCreateVo vmCreateVo);   // 생성
    VmCreateVo setEditVm(String id);      // 편집 창
    CommonVo<Boolean> editVm(String id, VmCreateVo vmCreateVo);     // 편집
    CommonVo<Boolean> deleteVm(String id);            // 삭제


    VmVo getInfo(String id);
    List<NicVo> getNic(String id);
    List<VmDiskVo> getDisk(String id);
    List<SnapshotVo> getSnapshot(String id);
    List<ApplicationVo> getApplication(String id);
    List<AffinityGroupVo> getAffinitygroup(String id);
    List<AffinityLabelVo> getAffinitylabel(String id);
    GuestInfoVo getGuestInfo(String id);
    List<PermissionVo> getPermission(String id);
    List<EventVo> getEvent(String id);

}
