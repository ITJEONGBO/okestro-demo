package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.NetworkVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItClusterService {
    List<ClusterVo> getList();
    ClusterVo getInfo(String id);
    List<NetworkVo> getNetwork(String id);
    List<HostVo> getHost(String id);
    List<VmVo> getVm(String id);
    List<AffinityGroupVo> getAffinitygroups(String id);
    //    List<AffinityLabelVo> getAffinitylabels(String id);
    List<CpuProfileVo> getCpuProfile(String id);
}
