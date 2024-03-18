package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.HostCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface ItHostService {
    String getName(String id);
    List<HostVo> getList();
    HostVo getInfo(String id);
    List<VmVo> getVm(String id);
    List<NicVo> getNic(String id);
    List<HostDeviceVo> getHostDevice(String id);

    // 선호도 레이블
    List<AffinityLabelVo> getAffinitylabels(String id);
    List<HostVo> getHostMember(String clusterId);       // 생성 시 필요한 host
    List<VmVo> getVmMember(String clusterId);       // 생성 시 필요한 vms 목록
    AffinityLabelCreateVo getAffinityLabel(String id);      // 편집창 출력
    CommonVo<Boolean> addAffinitylabel(AffinityLabelCreateVo alVo);     // 선호도 레이블 생성
    CommonVo<Boolean> editAffinitylabel(AffinityLabelCreateVo alVo);     // 선호도 레이블 편집
    CommonVo<Boolean> deleteAffinitylabel(String id);


    List<PermissionVo> getPermission(String id);
    List<EventVo> getEvent(String id);


    List<ClusterVo> getClusterList();      // cluster리스트 출력
    HostCreateVo getHostCreate(String id);

    boolean addHost(HostCreateVo hostCreateVo);     // 생성

    void editHost(HostCreateVo hostCreateVo);       // 수정

    boolean deleteHost(String id);                  // 삭제

    boolean rebootHost(String hostId);              // 재기동

    void deActive(String id);       // 유지보수
    void active(String id);         // 활성
    void refresh(String id);        // 새로고침

    // ssh 관리
    void reStart(String id);        // 새시작
    void start(String id);          // 시작
    void stop(String id);           // 중지

}
