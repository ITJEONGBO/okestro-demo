package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.dto.UsageChartDto;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.VDiskImageVo;
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
    List<UsageChartDto> getCpuChart();
    List<UsageChartDto> getMemoryChart();
    List<VmVo> getList();

    // 생성 창
    List<ClusterVo> setClusterList();    // 클러스터 리스트
    List<TemplateVo> setTemplateList(); // 템플릿 리스트
    List<DiskVo> setDiskList(); // 인스턴스 이미지 리스트 - 연결
    List<DomainVo> setDiskAttach(String clusterId); // 인스턴스 이미지 리스트 - 생성
    List<VnicProfileVo> setVnic(String clusterId); // vnicProfile
    List<IdentifiedVo> setHostList(String clusterId);   // 호스트 리스트
    List<IdentifiedVo> setStorageList();    // 스토리지 도메인 리스트
    List<IdentifiedVo> setCpuProfileList(String clusterId); // cpu 할당
    List<IdentifiedVo> setIsoImage();    // 부트 옵션 - CD/DVD 연결 ISO
    List<IdentifiedVo> setAgList(String clusterId); // 선호도 그룹 리스트
    List<IdentifiedVo> setAlList(); // 선호도 레이블 리스트

    VmStatus getStatus(String id);

    CommonVo<Boolean> addVm(VmCreateVo vmCreateVo);   // 생성
    VmCreateVo setVm(String id);      // 편집 창
    CommonVo<Boolean> editVm(VmCreateVo vmCreateVo);     // 편집
    CommonVo<Boolean> deleteVm(String id, boolean disk);            // 삭제

    CommonVo<Boolean> startVm(String id);  // 실행
    CommonVo<Boolean> pauseVm(String id);    // 일시정지
    CommonVo<Boolean> powerOffVm(String id);     // 전원끔
    CommonVo<Boolean> shutDownVm(String id); // 종료
    CommonVo<Boolean> rebootVm(String id);   // 재부팅
    CommonVo<Boolean> resetVm(String id);    // 재설정
    // 스냅샷 생성은 스냅샷에서 api로 연결

    List<IdentifiedVo> migrateHostList(String id);  // 마이그레이션할 호스트 목록
    CommonVo<Boolean> migrateVm(String id, String hostId);         // 마이그레이션
    CommonVo<Boolean> migrateCancelVm(String id);   // 마이그레이션 취소


    CommonVo<Boolean> exportOvaVm(VmExportVo vmExportVo); // ova로 내보내기


    VmVo getInfo(String id);        // 일반

    List<NicVo> getNic(String id);  // 네트워크 인터페이스
    CommonVo<Boolean> addNic(String id, NicVo nicVo);    // id=vmid, nic 추가
    NicVo setEditNic(String id, String nicId); // nic 수정창
    CommonVo<Boolean> editNic(String id, NicVo nicVo);    // nic 수정
    CommonVo<Boolean> deleteNic(String id, String nicId);    // nic 삭제


    // 디스크
    List<VmDiskVo> getDisk(String id);
    CommonVo<Boolean> addDiskImage(String id, VDiskImageVo image);  // 디스크 이미지 생성, id=vmId

    CommonVo<Boolean> deleteDisk(String id, String daId, boolean type); // 디스크 삭제
    CommonVo<Boolean> activeDisk(String id, String daId);        // 디스크 활성화
    CommonVo<Boolean> deactivateDisk(String id, String daId);    // 디스크 비활성화
    DiskVo setDiskMove(String id, String daId);                     // 디스크 이동창
    CommonVo<Boolean> moveDisk(String id, String daId, DiskVo diskVo);     // 디스크 스토리지 이동


    // 스냅샷
    List<SnapshotVo> getSnapshot(String id);    // 목록
    List<SnapshotDiskVo> setSnapshot(String vmId);  // 스냅샷 생성창
    CommonVo<Boolean> addSnapshot(SnapshotVo snapshotVo);   // 스냅샷 생성
    CommonVo<Boolean> deleteSnapshot(String id, String snapId);     // 스냅샷 삭제

    CommonVo<Boolean> addTemplate();    // 스냅샷 템플릿 생성


    List<IdentifiedVo> getApplication(String id);  // 어플리케이션
    GuestInfoVo getGuestInfo(String id);    // 게스트 정보
    List<PermissionVo> getPermission(String id);    // 권한
    List<EventVo> getEvent(String id);      // 이벤트

    ConsoleVo getConsole(String vmId);



//    CommonVo<Boolean> editDiskImage(String id, VDiskImageVo image); // 디스크 이미지 수정
//    CommonVo<Boolean> addDiskLun(String id, VDiskLunVo lun);        // 디스크 lun 생성, id=vmId
//    CommonVo<Boolean> connectDisk(String id);     // 디스크 연결
//    CommonVo<Boolean> editDiskLun(String id, VDiskLunVo lun);       // 디스크 lun 수정
//    CommonVo<Boolean> previewSnapshot(String id, String snapId);    // 스냅샷 미리보기
//    CommonVo<Boolean> commitSnapshot(String id, String snapId);     // 스냅샷 커밋
//    CommonVo<Boolean> restoreSnapshot(String id, String snapId);    // 스냅샷 되돌리기
//    CommonVo<Boolean> copySnapshot();   // 스냅샷 복제


//    List<AffinityGroupVo> getAffinitygroup(String id);  // 선호도 그룹
//    List<AffinityLabelVo> getAffinitylabel(String id);  // 선호도 레이블

}
