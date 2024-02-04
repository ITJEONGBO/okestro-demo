package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.HostCreateVo;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface ItHostService {
    String getName(String id);
    List<HostVo> getList();
    HostVo getInfo(String id);
    List<VmVo> getVm(String id);
    List<NicVo> getNic(String id);
    List<HostDeviceVo> getHostDevice(String id);
    List<PermissionVo> getPermission(String id);
    List<AffinityLabelVo> getAffinitylabels(String id);
    List<EventVo> getEvent(String id);

    HostCreateVo getHostCreate(String id);
    boolean addHost(HostCreateVo hostCreateVo);
    void editHost(HostCreateVo hostCreateVo);
    boolean deleteHost(String id);

}