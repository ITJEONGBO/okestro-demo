package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.HostVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItHostService {
    List<HostVO> getHosts();
    List<HostVO> getList();

    HostVO getInfo(String id);
    HostVO getVm(String id);
    HostVO getNic(String id);
    HostVO getHostDevice(String id);
    HostVO getPermission(String id);
    HostVO getAffinitylabels(String id);

}