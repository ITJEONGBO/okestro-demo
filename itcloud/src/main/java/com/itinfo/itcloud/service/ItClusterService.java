package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkClusterVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItClusterService {
    List<ClusterVo> getList();  // 클러스터 목록

    List<DataCenterVo> setDatacenterList(); // 생성 위한 dc 목록
    List<NetworkVo> setNetworkList(String dcId);    // 생성 위한 network 목록

    CommonVo<Boolean> addCluster(ClusterCreateVo cVo);  // 생성
    ClusterCreateVo setCluster(String id);  // 수정 창
    CommonVo<Boolean> editCluster(ClusterCreateVo cVo);  // 수정
    CommonVo<Boolean> deleteCluster(String id); // 삭제

    ClusterVo getInfo(String id);   // 일반

    List<NetworkVo> getNetwork(String id);  // 네트워크 목록
    CommonVo<Boolean> addClusterNetwork(String id, NetworkCreateVo ncVo);// 네트워크 추가
    List<NetworkClusterVo> setManageNetwork(String id); // 네트워크 관리 창
    CommonVo<Boolean> manageNetwork(String id, List<NetworkClusterVo> ncVoList);// 네트워크 관리

    List<HostVo> getHost(String id);    // 호스트 목록
    List<VmVo> getVm(String id);        // 가상머신 목록

    // 선호도 그룹
    // 선호도 레이블

    List<PermissionVo> getPermission(String id);    // 권한
    List<EventVo> getEvent(String id);  // 이벤트

//    List<CpuProfileVo> getCpuProfile(String id);  // 안쓸듯
}
