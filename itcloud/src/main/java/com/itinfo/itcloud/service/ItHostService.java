package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.HostCreateVo;
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
    List<PermissionVo> getPermission(String id);
    List<AffinityLabelVo> getAffinitylabels(String id);
    List<EventVo> getEvent(String id);


    List<ClusterVo> getClusterList();      // cluster리스트 출력
    HostCreateVo getHostCreate(String id);

    boolean addHost(HostCreateVo hostCreateVo);     // 생성
    void editHost(HostCreateVo hostCreateVo);       // 수정
    boolean deleteHost(String id);                  // 삭제


    void deActive(String id);       // 유지보수
    void active(String id);         // 활성
    void refresh(String id);        // 새로고침

    // ssh 관리
    void reStart(String id);        // 새시작
    void start(String id);          // 시작
    void stop(String id);           // 중지

}