package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ItStorageService {

    // region: disk
    List<DiskVo> getDiskList(String dcId);    // 디스크 리스트

    // 가상 디스크 생성
    DiskDcVo setDiskImage(String dcId); // 디스크-이미지: 생성창 / 화면에 보여지기 위한건 String type을 쓰든 어떻게 해보기
    CommonVo<Boolean> addDiskImage(ImageCreateVo image);    // 디스크-이미지: 생성
    CommonVo<Boolean> editDiskImage(ImageCreateVo image);   // 디스크-이미지: 수정

    LunCreateVo setDiskLun(String dcId);     // 디스크-lun: 생성 창
    CommonVo<Boolean> addDiskLun(LunCreateVo lun);      // 디스크-lun: 생성
    CommonVo<Boolean> editDiskLun(LunCreateVo lun);     // 디스크-lun: 수정

    // 가상 디스크 생성 - 관리되는 블록은 제외
    
    CommonVo<Boolean> deleteDisk(String diskId);   // 디스크: 삭제
    CommonVo<Boolean> moveDisk(DiskVo disk);       // 디스크 이동
    CommonVo<Boolean> copyDisk(DiskVo disk);       // 디스크 복사

    CommonVo<Boolean> uploadDisk(MultipartFile file, ImageCreateVo image) throws IOException;     // 디스크 업로드 시작
    CommonVo<Boolean> cancelUpload(String diskId); // 업로드 취소
    CommonVo<Boolean> pauseUpload(String diskId);  // 업로드 일시정지
    CommonVo<Boolean> resumeUpload(String diskId); // 업로드 재시작

    CommonVo<Boolean> downloadDisk();               // 디스크 다운로드

    // endregion


    // region: domain

    List<DomainVo> getDomainList(String dcId);    // 도메인 리스트

    CommonVo<Boolean> addDomain(DomainCreateVo dcVo);      // 도메인 생성
//    CommonVo<Boolean> manageDomain();   // 관리
    CommonVo<Boolean> deleteDomain(String domainId);   // 삭제

    // endregion


    // region: volume

    List<VolumeVo> getVolumeVoList(String dcId);   // 나중에

    // endregion


    // region: volume

    List<DomainVo> getStorageList(String dcId);    // 스토리지 리스트(이게 dc있는거)

    // endregion


    List<NetworkVo> getNetworkVoList(String dcId);

    List<ClusterVo> getClusterVoList(String dcId);

    List<PermissionVo> getPermission(String dcId);

    List<EventVo> getEvent(String id);
}
