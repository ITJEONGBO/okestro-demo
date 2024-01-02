package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItNetworkService {
    List<NetworkVo> getList();
    NetworkVo getNetwork(String id);

    List<VnicProfileVo> getVnic(String id);
    List<ClusterVo> getCluster(String id);
    List<HostVo> getHost(String id);
    List<VmVo> getVm(String id);
    List<TemplateVo> getTemplate(String id);
//    List<PermissionVo> getPermission(String id);

}
