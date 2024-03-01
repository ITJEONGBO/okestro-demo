package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItNetworkService {
    String getName(String id);
    List<NetworkVo> getList();
    NetworkVo getNetwork(String id);
    List<VnicProfileVo> getVnic(String id);
    List<NetworkClusterVo> getCluster(String id);
    List<NetworkHostVo> getHost(String id);
    List<NetworkVmVo> getVm(String id);
    List<TemplateVo> getTemplate(String id);
    List<PermissionVo> getPermission(String id);


    CommonVo<Boolean> addNetwork(NetworkCreateVo ncVo);
    CommonVo<Boolean> editNetwork(NetworkCreateVo ncVo);
    CommonVo<Boolean> deleteNetwork(String id);


}
