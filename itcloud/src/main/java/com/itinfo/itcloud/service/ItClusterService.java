package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.ClusterVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItClusterService {
    List<ClusterVO> getClusters();
    List<ClusterVO> getList();
    ClusterVO getInfo(String id);
    ClusterVO getNetwork(String id);
    ClusterVO getHost(String id);
    ClusterVO getVm(String id);
    ClusterVO getAffinitygroups(String id);
    ClusterVO getCpuProfile(String id);
}
