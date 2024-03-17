package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import org.ovirt.engine.sdk4.services.SystemService;
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

    // 선호도 그룹
    List<AffinityGroupVo> getAffinitygroup(String id);
    CommonVo<Boolean> addAffinitygroup(AffinityGroupCreateVo agVo);
    CommonVo<Boolean> editAffinitygroup(AffinityGroupCreateVo agVo);
    CommonVo<Boolean> deleteAffinitygroup(String clusterId, String id);

    // 선호도 레이블
    List<AffinityLabelVo> getAffinitylabelList(String id);  // 전체 출력 목록
    List<HostVo> getHostMember(String clusterId);       // 생성 시 필요한 host
    List<VmVo> getVmMember(String clusterId);       // 생성 시 필요한 vms 목록
    AffinityLabelCreateVo getAffinityLabel(String id);      // 편집창 출력
    CommonVo<Boolean> addAffinitylabel(AffinityLabelCreateVo alVo);     // 선호도 레이블 생성
    CommonVo<Boolean> editAffinitylabel(AffinityLabelCreateVo alVo);     // 선호도 레이블 편집
    CommonVo<Boolean> deleteAffinitylabel(String id);                     // 선호도 레이블 삭제


    List<PermissionVo> getPermission(String id);
    List<EventVo> getEvent(String id);


    List<DataCenterVo> getDcList();
    ClusterCreateVo getClusterCreate(String id);
    CommonVo<Boolean> addCluster(ClusterCreateVo cVo);   // 생성
    CommonVo<Boolean> editCluster(ClusterCreateVo cVo);   // 수정
    CommonVo<Boolean> deleteCluster(String id);       // 삭제

    // 안쓸듯
//    List<CpuProfileVo> getCpuProfile(String id);
}
