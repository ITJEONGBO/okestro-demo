package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainCreateVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.model.storage.ImageCreateVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ItStorageService {
    List<DiskVo> getDiskList(String dcId);    // 디스크 리스트

    List<IdentifiedVo> setDatacenterList(); // 디스크 생성 - 이미지 DC List
    List<IdentifiedVo> setDomainList(String dcId, String diskId);  // 디스크 생성 - 이미지 도메인 목록
    List<IdentifiedVo> setDiskProfile(String domainId); // 디스크 생성 -  이미지프로파일 목록

    // 가상 디스크 생성 - Lun, 관리되는 블록 제외
    CommonVo<Boolean> addDiskImage(ImageCreateVo image);    // 디스크: 이미지 생성
    ImageCreateVo setDiskImage(String diskId);
    CommonVo<Boolean> editDiskImage(ImageCreateVo image);   // 디스크: 이미지 수정
    CommonVo<Boolean> deleteDisk(String diskId);   // 디스크: 삭제

    // 디스크 이동/복사 창은 setDiskImage()/setDomainList()/setDiskProfile 사용예정
    CommonVo<Boolean> moveDisk(String diskId, String domainId);       // 디스크 이동
    CommonVo<Boolean> copyDisk(DiskVo diskVo);       // 디스크 복사
    CommonVo<Boolean> uploadDisk(MultipartFile file, ImageCreateVo image) throws IOException;     // 디스크 업로드 시작

    // 도메인 생성 창은 setDatacenterList(),
    List<IdentifiedVo> setHostList(String dcId);    // 도메인 생성 창 - 호스트 목록
    CommonVo<Boolean> addDomain(DomainCreateVo dcVo);      // 도메인 생성
//    CommonVo<Boolean> manageDomain();   // 관리
    CommonVo<Boolean> deleteDomain(String domainId);   // 삭제


    List<DomainVo> getDomainList(String dcId);    // 도메인 리스트
    List<NetworkVo> getNetworkVoList(String dcId);
    List<ClusterVo> getClusterVoList(String dcId);
    List<PermissionVo> getPermission(String dcId);
    List<EventVo> getEvent(String id);


    //region:나중

//    LunCreateVo setDiskLun(String dcId);     // 디스크-lun: 생성 창
//    CommonVo<Boolean> addDiskLun(LunCreateVo lun);      // 디스크-lun: 생성
//    CommonVo<Boolean> editDiskLun(LunCreateVo lun);     // 디스크-lun: 수정


//    CommonVo<Boolean> cancelUpload(String diskId); // 업로드 취소
//    CommonVo<Boolean> pauseUpload(String diskId);  // 업로드 일시정지
//    CommonVo<Boolean> resumeUpload(String diskId); // 업로드 재시작
//    CommonVo<Boolean> downloadDisk();               // 디스크 다운로드

    //endregion
}
