package com.itinfo.itcloud.service.storage

import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.ClusterVo
import com.itinfo.itcloud.model.computing.EventVo
import com.itinfo.itcloud.model.computing.PermissionVo
import com.itinfo.itcloud.model.error.CommonVo
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.storage.DiskVo
import com.itinfo.itcloud.model.storage.DomainCreateVo
import com.itinfo.itcloud.model.storage.DomainVo
import com.itinfo.itcloud.model.storage.ImageCreateVo
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

interface ItStorageService {
	/**
	 * [ItStorageService.getDiskList]
	 * 디스크 리스트
	 * 
	 * @param dcId [String] 디스크ID
	 * @return [List]<[DiskVo]> 디스크 정보 목록
	 */
	fun getDiskList(dcId: String): List<DiskVo>

	/**
	 * [ItStorageService.setDatacenterList]
	 * 디스크 생성 - 이미지 DC List
	 * 
	 * @return [List]<[IdentifiedVo]> ?
	 */
	fun setDatacenterList(): List<IdentifiedVo> 
	
	fun setDomainList(dcId: String, diskId: String): List<IdentifiedVo?>? // 디스크 생성 - 이미지 도메인 목록
	fun setDiskProfile(domainId: String): List<IdentifiedVo?>? // 디스크 생성 -  이미지프로파일 목록

	// 가상 디스크 생성 - Lun, 관리되는 블록 제외
	fun addDiskImage(image: ImageCreateVo?): CommonVo<Boolean> // 디스크: 이미지 생성
	fun setDiskImage(diskId: String): ImageCreateVo?
	fun editDiskImage(image: ImageCreateVo?): CommonVo<Boolean> // 디스크: 이미지 수정
	fun deleteDisk(diskId: String): CommonVo<Boolean> // 디스크: 삭제

	// 디스크 이동/복사 창은 setDiskImage()/setDomainList()/setDiskProfile 사용예정
	fun moveDisk(diskId: String, domainId: String): CommonVo<Boolean> // 디스크 이동
	fun copyDisk(diskVo: DiskVo): CommonVo<Boolean> // 디스크 복사

	@Throws(IOException::class)
	fun uploadDisk(file: MultipartFile?, image: ImageCreateVo?): CommonVo<Boolean> // 디스크 업로드 시작

	// 도메인 생성 창은 setDatacenterList(),
	fun setHostList(dcId: String): List<IdentifiedVo?>? // 도메인 생성 창 - 호스트 목록
	fun addDomain(dcVo: DomainCreateVo?): CommonVo<Boolean> // 도메인 생성

	// fun manageDomain(): CommonVo<Boolean>   // 관리
	fun deleteDomain(domainId: String): CommonVo<Boolean> // 삭제


	fun getDomainList(dcId: String): List<DomainVo?> // 도메인 리스트
	fun getNetworkVoList(dcId: String): List<NetworkVo>
	fun getClusterVoList(dcId: String): List<ClusterVo>
	fun getPermission(dcId: String): List<PermissionVo>
	fun getEvent(id: String): List<EventVo>
//region:나중
/*
	LunCreateVo setDiskLun(String dcId);     // 디스크-lun: 생성 창
	CommonVo<Boolean> addDiskLun(LunCreateVo lun);      // 디스크-lun: 생성
	CommonVo<Boolean> editDiskLun(LunCreateVo lun);     // 디스크-lun: 수정
	CommonVo<Boolean> cancelUpload(String diskId); // 업로드 취소
	CommonVo<Boolean> pauseUpload(String diskId);  // 업로드 일시정지
	CommonVo<Boolean> resumeUpload(String diskId); // 업로드 재시작
	CommonVo<Boolean> downloadDisk();               // 디스크 다운로드
 */
//endregion
}