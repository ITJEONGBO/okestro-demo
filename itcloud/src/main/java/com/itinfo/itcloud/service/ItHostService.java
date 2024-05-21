package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.HostCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItHostService {

    List<HostVo> getList(); // 호스트 리스트

    List<ClusterVo> setHostDefaultInfo();      // 호스트 생성 - 클러스터 리스트 출력
    CommonVo<Boolean> addHost(HostCreateVo hostCreateVo);     // 생성
    HostCreateVo setHost(String id);      // 수정 창
    CommonVo<Boolean> editHost(String id, HostCreateVo hostCreateVo);       // 수정
    CommonVo<Boolean> deleteHost(String id);                  // 삭제

//    boolean rebootHost(String hostId);              // 재기동
    CommonVo<Boolean> deActive(String id);       // 유지보수
    CommonVo<Boolean> active(String id);         // 활성
    CommonVo<Boolean> refresh(String id);        // 새로고침

    // ssh 관리
    CommonVo<Boolean> reStart(String id);        // 재시작
//    void start(String id);          // 시작
    CommonVo<Boolean> stop(String id);           // 중지


    HostVo getInfo(String id);  // 일반

    List<VmVo> getVm(String id);    // 가상머신
    CommonVo<Boolean> startVm(String vmId);     // 실행
    CommonVo<Boolean> pauseVm(String vmId);     // 일시중지
    CommonVo<Boolean> stopVm(String vmId);      // 종료
    CommonVo<Boolean> shutdownVm(String vmId);  // 전원끔
    CommonVo<Boolean> migrationVm(String vmId);     // 마이그레이션
    CommonVo<Boolean> migrationCancelVm(String vmId);     // 마이그레이션 취소


    List<NicVo> getNic(String id);  // 네트워크 인터페이스

    List<HostDeviceVo> getHostDevice(String id);    // 호스트 장치
    List<PermissionVo> getPermission(String id);    // 권한


    // 선호도 그룹&레이블  생성 위한 host&vm (dc 밑에 붙어있어야함)
    AffinityHostVm setAffinityDefaultInfo(String id, String type);  // 선호도 레이블 생성창

    // 선호도 레이블
    List<AffinityLabelVo> getAffinitylabels(String id);     // 선호도 레이블 목록
    CommonVo<Boolean> addAffinitylabel(String id, AffinityLabelCreateVo alVo);     // 선호도 레이블 생성
    AffinityLabelCreateVo getAffinityLabel(String alid);   // id는 alid
    CommonVo<Boolean> editAffinitylabel(String id, String alId, AffinityLabelCreateVo alVo);     // 선호도 레이블 편집
    CommonVo<Boolean> deleteAffinitylabel(String id, String alId);           // 선호도 레이블 삭제


    List<EventVo> getEvent(String id);
}
