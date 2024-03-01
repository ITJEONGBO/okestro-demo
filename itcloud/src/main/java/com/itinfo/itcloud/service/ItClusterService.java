package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItClusterService {
    String getName(String id);
    List<ClusterVo> getList();
    ClusterVo getInfo(String id);
    List<NetworkVo> getNetwork(String id);
    List<HostVo> getHost(String id);
    List<VmVo> getVm(String id);
    List<AffinityGroupVo> getAffinitygroup(String id);
    List<AffinityLabelVo> getAffinitylabel();
//    List<CpuProfileVo> getCpuProfile(String id);
    List<PermissionVo> getPermission(String id);
    List<EventVo> getEvent(String id);


    ClusterCreateVo getClusterCreate(String id);

    List<DataCenterVo> getDcList();
    CommonVo<Boolean> addCluster(ClusterCreateVo cVo);   // 생성
    CommonVo<Boolean> editCluster(ClusterCreateVo cVo);   // 수정
    CommonVo<Boolean> deleteCluster(String id);       // 삭제

}
