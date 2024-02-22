package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.VmCreateVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItVmService {
    String getName(String id);
    List<VmVo> getList();
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


    List<ClusterVo> getClusterList();
    VmCreateVo getVmCreate(String id);

    boolean addVm(VmCreateVo vmCreateVo);   // 생성
    void editVm(VmCreateVo vmCreateVo);     // 수정
    boolean deleteVm(String id);            // 삭제



}
