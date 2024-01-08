package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItNetworkService {
    List<NetworkVo> getList();
    NetworkVo getNetwork(String id);
    List<VnicProfileVo> getVnic(String id);
    List<NetworkClusterVo> getCluster(String id);
    List<NetworkHostVo> getHost(String id);
    List<NetworkVmVo> getVm(String id);
    List<TemplateVo> getTemplate(String id);
    List<PermissionVo> getPermission(String id);
}
