package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItStorageService {
    String getName(String id);

    List<DiskVo> getDiskVoList(String dcId);    // 디스크 리스트


    DiskDcVo setDiskImage(String dcId); // 화면에 보여지기 위한건 String type을 쓰든 어떻게 해보기
    CommonVo<Boolean> addDiskImage(ImageCreateVo imageCreateVo);    // 디스크-이미지: 생성
    CommonVo<Boolean> editDiskImage(ImageCreateVo imageCreateVo);   // 디스크-이미지: 수정
    CommonVo<Boolean> deleteDiskImage(String imageId);              // 디스크-이미지: 삭제


    LunCreateVo setDiskLun(String dcId);
    CommonVo<Boolean> addDiskLun(LunCreateVo lunCreateVo);      // 디스크-lun: 생성
    CommonVo<Boolean> editDiskLun(LunCreateVo lunCreateVo);     // 디스크-lun: 수정
    CommonVo<Boolean> deleteDiskLun(String lunId);              // 디스크-lun: 삭제


    BlockVo setDiskBlock(String dcId);
    CommonVo<Boolean> addDiskBlock(BlockVo blockVo);      // 디스크-block: 생성


    CommonVo<Boolean> moveDisk();       // 디스크 이동

    CommonVo<Boolean> copyDisk();       // 디스크 복사
    
    CommonVo<Boolean> uploadDisk();     // 디스크 업로드

    // 디스크 다운로드


    List<DomainVo> getDomainList();    // 도메인 리스트
    List<VolumeVo> getVolumeVoList(String dcId);   // 나중에
    List<DomainVo> getStorageList(String dcId);    // 스토리지 리스트(이게 dc있는거)
    List<NetworkVo> getNetworkVoList(String dcId);
    List<ClusterVo> getClusterVoList(String dcId);
    List<PermissionVo> getPermission(String dcId);
    List<EventVo> getEvent(String id);
}
