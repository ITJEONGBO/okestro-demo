package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.*;
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
    CommonVo<Boolean> deleteVm(String id);            // 삭제


    List<VnicProfileVo> setVnic();  // nic 연결시 필요한 리스트
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



    VmVo getInfo(String id);
    List<NicVo> getNic(String id);
    List<VmDiskVo> getDisk(String id);
    List<SnapshotVo> getSnapshot(String id);
    List<ApplicationVo> getApplication(String id);
    List<AffinityGroupVo> getAffinitygroup(String id);
    List<AffinityLabelVo> getAffinitylabel(String id);
    GuestInfoVo getGuestInfo(String id);
    List<PermissionVo> getPermission(String id);
    List<EventVo> getEvent(String id);

}
