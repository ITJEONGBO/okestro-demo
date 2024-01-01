package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItHostService {
    List<HostVo> getList();
    HostVo getInfo(String id);
    List<VmVo> getVm(String id);
    List<NicVo> getNic(String id);
    List<HostDeviceVo> getHostDevice(String id);
//    HostVO getPermission(String id);
    List<AffinityLabelVo> getAffinitylabels(String id);

}