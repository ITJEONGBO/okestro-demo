package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItClusterService {
    List<ClusterVo> getList();

    // 클러스터
    List<DataCenterVo> setClusterDefaultInfo(); // 생성 위한 dc&network 목록
    CommonVo<Boolean> addCluster(ClusterCreateVo cVo);   // 생성
    ClusterCreateVo setEditCluster(String id);      // 수정 창
    CommonVo<Boolean> editCluster(ClusterCreateVo cVo);   // 수정
    CommonVo<Boolean> deleteCluster(String id);       // 삭제

    ClusterVo getInfo(String id);   // 일반

    // 네트워크
    List<NetworkVo> getNetwork(String id);  // 네트워크 목록
    // 네트워크 추가
    // 네트워크 관리

    List<HostVo> getHost(String id);    // 호스트 목록
    List<VmVo> getVm(String id);        // 가상머신 목록

    // 선호도 그룹
    List<AffinityGroupVo> getAffinitygroup(String clusterId);  // 선호도 그룹 목록
    ClusterAffGroupHostVm setAffinitygroupDefaultInfo(String clusterId);   // 선호도 그룹 생성 위한 host&vm
    CommonVo<Boolean> addAffinitygroup(AffinityGroupCreateVo agVo); // 선호도 그룹 생성
    AffinityGroupCreateVo setEditAffinitygroup(String clusterId, String agId);    // 선호도 그룹 편집 위한 값 세팅
    CommonVo<Boolean> editAffinitygroup(AffinityGroupCreateVo agVo); // 선호도 그룹 편집
    CommonVo<Boolean> deleteAffinitygroup(String clusterId, String id); // 선호도 그룹 삭제

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



    // 안쓸듯
//    List<CpuProfileVo> getCpuProfile(String id);
}
