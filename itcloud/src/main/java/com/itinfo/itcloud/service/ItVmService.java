package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.VmVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItVmService {
    List<VmVO> getVms();
    List<VmVO> getList();
    VmVO getInfo(String id);
    VmVO getNic(String id);
    VmVO getDisk(String id);
    VmVO getSnapshot(String id);
    VmVO getApplication(String id);
    VmVO getAffinitygroup(String id);
    VmVO getAffinitylabel(String id);
    VmVO getGuestInfo(String id);
    VmVO getPermission(String id);
    VmVO getEvent(String id);
}
