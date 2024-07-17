package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.HostCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.List;

@Service
public interface ItHostService {
    List<HostVo> getHosts(); // 호스트 리스트

    List<ClusterVo> setClusterList();   // 호스트 생성 - 클러스터 리스트 출력

    CommonVo<Boolean> addHost(HostCreateVo hostCreateVo);   // 생성
    HostCreateVo setHost(String id);    // 수정 창
    CommonVo<Boolean> editHost(HostCreateVo hostCreateVo);   // 수정
    CommonVo<Boolean> deleteHost(List<String> ids);    // 삭제

    CommonVo<Boolean> deactiveHost(String id);       // 관리 - 유지보수
    CommonVo<Boolean> activeHost(String id);         // 관리 - 활성
    CommonVo<Boolean> refreshHost(String id);        // 관리 - 새로고침
    CommonVo<Boolean> reStartHost(String id) throws UnknownHostException;        // ssh 관리 - 재시작
//    CommonVo<Boolean> stopHost(String id);           // ssh 관리- 중지

    HostVo getHost(String id);  // 일반

    // 호스트 내 가상머신
    List<VmVo> getVmsByHost(String id);    // 가상머신 리스트
//    CommonVo<Boolean> startVm(String vmId);     // 실행
//    CommonVo<Boolean> pauseVm(String vmId);     // 일시중지
//    CommonVo<Boolean> stopVm(String vmId);      // 종료
//    CommonVo<Boolean> shutdownVm(String vmId);  // 전원끔
//    CommonVo<Boolean> migrationVm(String vmId);     // 마이그레이션
//    CommonVo<Boolean> migrationCancelVm(String vmId);     // 마이그레이션 취소

    List<NicVo> getNicsByHost(String id);  // 네트워크 인터페이스
    // 호스트 네트워크 설정

    List<HostDeviceVo> getHostDevicesByHost(String id);    // 호스트 장치
    List<PermissionVo> getPermissionsByHost(String id);    // 권한


    // 선호도 그룹&레이블  생성 위한 host&vm (dc 밑에 붙어있어야함)
//    AffinityHostVm setAffinityDefaultInfo(String id, String type);  // 선호도 레이블 생성창
//
//    // 선호도 레이블
//    List<AffinityLabelVo> getAffinitylabels(String id);     // 선호도 레이블 목록
//    CommonVo<Boolean> addAffinitylabel(String id, AffinityLabelCreateVo alVo);     // 선호도 레이블 생성
//    AffinityLabelCreateVo getAffinityLabel(String alid);   // id는 alid
//    CommonVo<Boolean> editAffinitylabel(String id, String alId, AffinityLabelCreateVo alVo);     // 선호도 레이블 편집
//    CommonVo<Boolean> deleteAffinitylabel(String id, String alId);           // 선호도 레이블 삭제


    List<EventVo> getEventsByHost(String id); // 이벤트 출력
}
