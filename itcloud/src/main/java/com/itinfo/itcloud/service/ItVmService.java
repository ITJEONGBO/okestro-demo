package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.VDiskImageVo;
import com.itinfo.itcloud.model.create.VDiskLunVo;
import com.itinfo.itcloud.model.create.VmCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.model.storage.VmDiskVo;
import org.ovirt.engine.sdk4.types.VmStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItVmService {
    List<VmVo> getList();


    List<VmSetVo> setVmSet();               // 생성 창
    List<IdentifiedVo> getCpuProfileList(String clusterId); // cpuprofile
    CommonVo<Boolean> addVm(VmCreateVo vmCreateVo);   // 생성
    VmCreateVo setEditVm(String id);      // 편집 창
    CommonVo<Boolean> editVm(String id, VmCreateVo vmCreateVo);     // 편집
    CommonVo<Boolean> deleteVm(String id, boolean disk);            // 삭제


    List<VnicProfileVo> setVnic(String clusterId);  // nic 연결시 필요한 리스트
    List<DiskVo> setDiskConn();    // 인스턴스 이미지 연결
    List<DomainVo> setDiskAttach(String clusterId); // 이미지 생성시 필요한 스토리지도메인


    // 마우스 오른쪽버튼
    VmStatus getStatus(String id);   // 마우스 오른쪽버튼?
    CommonVo<Boolean> startVm(String id);  // 실행
    CommonVo<Boolean> pauseVm(String id);    // 일시정지
    CommonVo<Boolean> stopVm(String id);     // 전원끔
    CommonVo<Boolean> shutdownVm(String id); // 종료
    CommonVo<Boolean> rebootVm(String id);   // 재부팅
    CommonVo<Boolean> resetVm(String id);    // 재설정

    List<SnapshotDiskVo> setSnapshot(String id);  // vmid
    CommonVo<Boolean> addSnapshot(SnapshotVo snapshotVo);   // 스냅샷 생성



    VmVo getInfo(String id);        // 일반

    List<NicVo> getNic(String id);  // 네트워크 인터페이스
    CommonVo<Boolean> addNic(String id, NicVo nicVo);    // id=vmid, nic 추가
    NicVo setEditNic(String id, String nicId); // nic 수정창
    CommonVo<Boolean> editNic(String id, NicVo nicVo);    // nic 수정
    CommonVo<Boolean> deleteNic(String id, String nicId);    // nic 삭제


    // 디스크
    List<VmDiskVo> getDisk(String id);
    CommonVo<Boolean> addDiskImage(String id, VDiskImageVo image);  // 디스크 이미지 생성, id=vmId
    CommonVo<Boolean> editDiskImage(String id, VDiskImageVo image); // 디스크 이미지 수정
    CommonVo<Boolean> addDiskLun(String id, VDiskLunVo lun);        // 디스크 lun 생성, id=vmId
//    CommonVo<Boolean> connectDisk(String id);     // 디스크 연결
    CommonVo<Boolean> editDiskLun(String id, VDiskLunVo lun);       // 디스크 lun 수정
    CommonVo<Boolean> deleteDisk(String id, String daId, boolean type); // 디스크 삭제
    CommonVo<Boolean> activeDisk(String id, String daId);        // 디스크 활성화
    CommonVo<Boolean> deactivateDisk(String id, String daId);    // 디스크 비활성화
    DiskVo setDiskMove(String id, String daId);                     // 디스크 이동창
    CommonVo<Boolean> moveDisk(String id, String daId, DiskVo diskVo);     // 디스크 스토리지 이동


    // 스냅샷
    List<SnapshotVo> getSnapshot(String id);
    CommonVo<Boolean> previewSnapshot(String id, String snapId);    // 스냅샷 미리보기
    CommonVo<Boolean> commitSnapshot(String id, String snapId);     // 스냅샷 커밋
    CommonVo<Boolean> restoreSnapshot(String id, String snapId);    // 스냅샷 되돌리기
    CommonVo<Boolean> deleteSnapshot(String id, String snapId);     // 스냅샷 삭제
    CommonVo<Boolean> copySnapshot();   // 스냅샷 복제
    CommonVo<Boolean> addTemplate();    // 스냅샷 템플릿 생성




    List<ApplicationVo> getApplication(String id);  // 어플리케이션
    List<AffinityGroupVo> getAffinitygroup(String id);  // 선호도 그룹
    List<AffinityLabelVo> getAffinitylabel(String id);  // 선호도 레이블
    GuestInfoVo getGuestInfo(String id);    // 게스트 정보
    List<PermissionVo> getPermission(String id);    // 권한
    List<EventVo> getEvent(String id);      // 이벤트

}
