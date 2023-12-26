package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItVmService {
    List<VmVo> getList();
    VmVo getInfo(String id);
    List<NicVo> getNic(String id);
    List<VmDiskVo> getDisk(String id);
    //    VmVO getSnapshot(String id);
    List<AppVo> getApplication(String id);
    List<AffinityGroupVo> getAffinitygroup(String id);
    List<AffinityLabelVo> getAffinitylabel(String id);
    GuestInfoVo getGuestInfo(String id);
//    VmVO getPermission(String id);
//    VmVO getEvent(String id);
}
