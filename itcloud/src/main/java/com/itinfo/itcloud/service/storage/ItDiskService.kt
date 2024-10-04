package com.itinfo.itcloud.service.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.computing.toVmVoInfos
import com.itinfo.itcloud.model.response.Res
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.itcloud.service.storage.StorageServiceImpl.Companion
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.services.ImageTransferService
import org.ovirt.engine.sdk4.types.*
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

interface ItDiskService {

    /**
     * [ItDiskService.findAll]
     * 전체 디스크 목록
     *
     * @return List<[DiskImageVo]> 디스크 목록
     */
    @Throws(Error::class)
    fun findAll(): List<DiskImageVo>

    /**
     * [ItDiskService.findAllFromStorageDomain]
     * 스토리지 도메인 - 디스크 목록
     *
     * @param storageDomainId [String] 스토리지 도메인 Id
     * @return List<[DiskImageVo]> 디스크 목록
     */
    @Throws(Error::class)
    fun findAllFromStorageDomain(storageDomainId: String): List<DiskImageVo>

    /**
     * [ItDiskService.findOne]
     * 디스크 정보
     *
     * @param diskId [String] 디스크 id
     * @return [DiskImageVo]?
     */
    @Throws(Error::class)
    fun findOne(diskId: String): DiskImageVo?

    // 디스크 생성창 - 이미지 데이터센터 목록 [ItDataCenterService.findAll]

    /**
     * [ItDiskService.findAllDomainsFromDataCenter]
     * 디스크 이미지 생성창
     * 디스크 생성 - 이미지 도메인 목록
     * 디스크 복사
     * 단순 데이터센터 내부에있는 스토리지 도메인을 선택하기 위해 존재
     *
     * @param dataCenterId [String]
     * @return List<[StorageDomainVo]> 스토리지 도메인 목록
     */
    @Throws(Error::class)
    fun findAllDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo>
    /**
     * [ItDiskService.findAllDiskProfilesFromStorageDomain]
     * 디스크 생성 - 이미지프로파일 목록
     * 디스크 복사
     *
     * @param storageDomainId [String]
     * @return [List]<[DiskProfileVo]> 디스크 프로파일 목록
     */
    @Throws(Error::class)
    fun findAllDiskProfilesFromStorageDomain(storageDomainId: String): List<DiskProfileVo>
    /**
     * [ItDiskService.add]
     * 디스크 생성 (이미지)
     * 가상 디스크 생성 - Lun, 관리되는 블록 제외
     * ovirt에서 dc정보는 스토리지 도메인을 파악하기 위해있음
     *
     * @param image [DiskImageVo] 이미지 객체
     * @return [Res]<[Boolean]> 201 (create), 404(fail)
     */
    @Throws(Error::class)
    fun add(image: DiskImageVo): DiskImageVo?
    /**
     * [ItDiskService.update]
     * 디스트 편집
     *
     * @param image [DiskImageVo] 이미지 생성
     * @return [DiskImageVo]
     */
    @Throws(Error::class)
    fun update(image: DiskImageVo): DiskImageVo?
    /**
     * [ItDiskService.remove]
     * 디스크 삭제
     *
     * @param diskId [String] 디스크 ID
     * @return [Boolean] 성공여부
     */
    @Throws(Error::class)
    fun remove(diskId: String): Boolean
    /**
     * [ItDiskService.findAllStorageDomainsFromDisk]
     * 디스크 이동- 창
     * TODO 디스크 이동시 대상은 디스크가 가지고 있는 스토리지 도메인은 목록에서 제외
     * ItDiskService.findAllStorageDomainsFromDataCenter 에서 disk가 가지고있는 스토리지도메인은 제외
     *
     * @param diskId [String] 디스크 ID
     * @return List<[StorageDomainVo]> 성공여부
     */
    @Throws(Error::class)
    fun findAllStorageDomainsFromDisk(diskId: String): List<StorageDomainVo>
    /**
     * [ItDiskService.move]
     * 디스크 이동
     *
     * @param diskId [String] 디스크 아이디
     * @param storageDomainId [String] 도메인 아이디
     * @return [Boolean] 성공여부
     */
    @Throws(Error::class)
    fun move(diskId: String, storageDomainId: String): Boolean
    /**
     * [ItDiskService.copy]
     * 디스크 복사
     *
     * @param diskImageVo [DiskImageVo] 디스크 객체
     * @return [Boolean]
     */
    @Throws(Error::class)
    fun copy(diskImageVo: DiskImageVo): Boolean
    /**
     * [ItDiskService.upload]
     * 디스크 이미지 업로드
     * required: provisioned_size, alias, description, wipe_after_delete, shareable, backup, disk_profile.
     * @param file [MultipartFile] 업로드 할 파일
     * @param image [DiskImageVo] 이미지 객체
     * @return [Boolean] 성공여부
     * @throws IOException
     */
    @Throws(Error::class, IOException::class)
    fun upload(file: MultipartFile, image: DiskImageVo): Boolean
    /**
     * [ItDiskService.refreshLun]
     * lun 새로고침
     *
     * @param diskId [String] 도메인 ID
     * @param hostId [String] host Id
     * @return [Boolean]
     */
    @Throws(Error::class)
    fun refreshLun(diskId: String, hostId: String): Boolean

    /**
     * [ItDiskService.findAllVmsFromDisk]
     * 스토리지도메인 - 가상머신
     *
     * @param diskId [String] 도메인 ID
     * @return List<[VmVo]> 가상머신
     */
    @Throws(Error::class)
    fun findAllVmsFromDisk(diskId: String): List<VmVo>
    /**
     * [ItDiskService.findAllPermissionsFromDisk]
     * 스토리지도메인 - 권한
     *
     * @param diskId [String] 도메인 ID
     * @return List<[PermissionVo]> 권한 목록
     */
    @Throws(Error::class)
    @Deprecated("나중구현")
    fun findAllPermissionsFromDisk(diskId: String): List<PermissionVo>
}

@Service
class DiskServiceImpl(
    
): BaseService(), ItDiskService {

    @Throws(Error::class)
    override fun findAll(): List<DiskImageVo> {
        log.info("findAll ... ")
        val res: List<Disk> =
            conn.findAllDisks()
                .getOrDefault(listOf())
        return res.toDiskImageVos(conn)
    }

    @Throws(Error::class)
    override fun findAllFromStorageDomain(storageDomainId: String): List<DiskImageVo> {
        log.info("findAllFromStorageDomain ... storageDomainId: {}", storageDomainId)
        val res: List<Disk> =
            conn.findAllDisksFromStorageDomain(storageDomainId)
                .getOrDefault(listOf())
        return res.toDiskImageVos(conn)
    }

    @Throws(Error::class)
    override fun findOne(diskId: String): DiskImageVo? {
        log.info("findOne ... diskId: $diskId")
        val res: Disk? =
            conn.findDisk(diskId)
                .getOrNull()
        return res?.toDiskImageVo(conn)
    }

    @Throws(Error::class)
    override fun findAllDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo> {
        log.info("findAllStorageDomainsFromDataCenter ... dataCenterId: $dataCenterId")
        val res: List<StorageDomain> =
            conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId)
                .getOrDefault(listOf())
                .filter { it.status() == StorageDomainStatus.ACTIVE }
        return res.toStorageDomainSizes()
    }

    @Throws(Error::class)
    override fun findAllDiskProfilesFromStorageDomain(storageDomainId: String): List<DiskProfileVo> {
        log.info("findAllDiskProfilesFromStorageDomain ... domainId: $storageDomainId")
        val res: List<DiskProfile> =
            conn.findAllDiskProfilesFromStorageDomain(storageDomainId)
                .getOrDefault(listOf())
        return res.toDiskProfileVos()
    }

    @Throws(Error::class)
    override fun add(image: DiskImageVo): DiskImageVo? {
        log.info("addDisk ... image: $image")
        val res: Disk? =
            conn.addDisk(image.toAddDiskBuilder())
                .getOrNull()
        return res?.toDiskImageVo(conn)
    }

    @Throws(Error::class)
    override fun update(image: DiskImageVo): DiskImageVo? {
        log.info("updateDisk ... image: $image")
        val res: Disk? =
            conn.updateDisk(image.toEditDiskBuilder())
                .getOrNull()
        return res?.toDiskImageVo(conn)
    }

    @Throws(Error::class)
    override fun remove(diskId: String): Boolean {
        log.info("removeDisk ... diskId: $diskId")
        val res: Result<Boolean> =
            conn.removeDisk(diskId)
        return res.isSuccess
    }

    @Throws(Error::class)
    override fun findAllStorageDomainsFromDisk(diskId: String): List<StorageDomainVo> {
        log.info("findAllStorageDomainsFromDisk ... diskId: $diskId")
        val res: List<StorageDomain> =
            conn.findAllStorageDomainsFromDisk(diskId)
                .getOrDefault(listOf())
        return res.toStorageDomainVos(conn)
    }

    @Throws(Error::class)
    override fun move(diskId: String, storageDomainId: String): Boolean {
        log.info("move ... diskId: $diskId, storageDomainId: $storageDomainId")
        val res: Result<Boolean> =
            conn.moveDisk(diskId, storageDomainId)
        return res.isSuccess
    }

    @Throws(Error::class)
    override fun copy(diskImageVo: DiskImageVo): Boolean {
        log.info("copy ... diskName: ${diskImageVo.alias}")
        // vm 연결대상이 있을 경우, 복사 불가능
        val res: Result<Boolean> =
            conn.copyDisk(
                diskImageVo.id,
                diskImageVo.alias,
                diskImageVo.storageDomainVo.id
            )
        return res.isSuccess
    }

    // (화면표시) 파일 선택시 파일에 있는 포맷, 컨텐츠(파일 확장자로 칭하는건지), 크기 출력
    //		   파일 크기가 자동으로 디스크 옵션에 추가, 파일 명칭이 파일의 이름으로 지정됨 (+설명)
    // 디스크 이미지 업로드
    // required: provisioned_size, alias, description, wipe_after_delete, shareable, backup, disk_profile.
    @Throws(Error::class, IOException::class)
    override fun upload(file: MultipartFile, image: DiskImageVo): Boolean {
        log.info("uploadDisk ... ")
        if (file.isEmpty) throw ErrorPattern.FILE_NOT_FOUND.toException()

//		val res: Res<Boolean> =
//			conn.uploadDisk(file, image.toUploadDiskBuilder(file.size))
//				.getOrNull()
//		return res.isSuccess
        TODO("Not yet implemented")
    }

    override fun refreshLun(diskId: String, hostId: String): Boolean {
        log.info("refreshLun ... ")

        TODO("Not yet implemented")
    }


    @Throws(Error::class)
    override fun findAllVmsFromDisk(diskId: String): List<VmVo> {
        log.info("findAllVmsFromDisk ... ")
        val res: List<Vm> =
            conn.findAllVmsFromDisk(diskId)
                .getOrDefault(listOf())
        return res.toVmVoInfos(conn)
    }

    @Deprecated("나중구현")
    @Throws(Error::class)
    override fun findAllPermissionsFromDisk(diskId: String): List<PermissionVo> {
        log.info("findAllPermissionsFromDisk ... diskId: {}", diskId)
        val res: List<Permission> =
            conn.findAllPermissionsFromDisk(diskId)
                .getOrDefault(listOf())
        return res.toPermissionVos(conn)
    }


    // uploadImage
    /*
    val disksService = system.disksService()
    val imageTransfersService = system.imageTransfersService() // 이미지 추가를 위한 서비스
    try {//
        // 우선 입력된 디스크 정보를 바탕으로 디스크 추가
        val disk = disksService.add().disk(file?.let { createDisk(image, it.size) }).send().disk()
        val diskService = disksService.diskService(disk.id())

        // 디스크 ok 상태여야 이미지 업로드 가능
        if (conn.expectDiskStatus(disk.id(), DiskStatus.OK, 30000)) {
            log.info("디스크 생성 성공")
        } else {
            log.error("디스크 대기시간 초과")
            return false
        }

        // 이미지를 저장하거나 관리하는 컨테이너,.이미지의 생성, 삭제, 편집 등의 작업을 지원
        val imageContainer = ImageContainer()
        imageContainer.id(disk.id())

        // 이미지 전송 작업(업로드나 다운로드)을 관리하는 컨테이너. 전송 상태, 진행률, 오류 처리 등의 기능을 포함.
        val imageTransferContainer = ImageTransferContainer()
        imageTransferContainer.direction(ImageTransferDirection.UPLOAD)
        imageTransferContainer.image(imageContainer)

        // 이미지 전송
        val imageTransfer = imageTransfersService.add().imageTransfer(imageTransferContainer).send().imageTransfer()
        while (imageTransfer.phase() == ImageTransferPhase.INITIALIZING) {
            log.debug("이미지 업로드 가능상태 확인 {}", imageTransfer.phase())
            Thread.sleep(1000)
        }

        val imageTransferService = imageTransfersService.imageTransferService(imageTransfer.id())

        val transferUrl = imageTransfer.transferUrl()
        if (transferUrl == null || transferUrl.isEmpty()) {
            log.warn("transferUrl 없음")
            return false
        } else {
            log.debug("imageTransfer.transferUrl(): {}", transferUrl)
            if (file != null) {
                imageSend(imageTransferService, file)
            }
        }
        return true
    } catch (e: Exception) {
        log.error("이미지 업로드 실패 ... reason: {}", e.localizedMessage)
        return false
    }
}


*//**
     * [createDisk]
     * 디스크 생성
     *
     * @param image [DiskImageVo]
     * @param fileSize [Long]
     * @return [Disk]
     *//*

private fun createDisk(image: DiskImageVo, fileSize: Long): Disk {
    log.debug("createDisk ... ")
    return DiskBuilder()
        .provisionedSize(fileSize)
        .alias(image.alias)
        .description(image.description)
        .storageDomains(*arrayOf(StorageDomainBuilder().id(image.storageDomainVo.id).build()))
        .diskProfile(DiskProfileBuilder().id(image.diskProfileVo.id).build())
        .wipeAfterDelete(image.wipeAfterDelete)
        .shareable(image.sharable)
        .backup(DiskBackup.NONE) // 증분백업 되지 않음
        .format(DiskFormat.RAW) // 이미지 업로드는 raw형식만 가능
        .contentType(DiskContentType.ISO) // iso 업로드
        .build()
        }

	*//**
     * [imageSend]
     * 디스크 이미지 전송
     *
     * @param imageTransferService [ImageTransferService]
     * @param file [MultipartFile]
     *//*
	private fun imageSend(imageTransferService: ImageTransferService, file: MultipartFile): Boolean {
		log.debug("imageSend ... ")
		var httpsConn: HttpsURLConnection? = null
		try {
//			disableSSLVerification()
			// 자바에서 HTTP 요청을 보낼 때 기본적으로 제한된 헤더를 사용자 코드에서 설정할 수 있도록 허용하는 설정
			System.setProperty("sun.net.http.allowRestrictedHeaders", "true")

			val url = URL(imageTransferService.get().send().imageTransfer().transferUrl())
			httpsConn = url.openConnection() as HttpsURLConnection
			httpsConn.requestMethod = "PUT"
			httpsConn.setRequestProperty("Content-Length", file.size.toString())
			httpsConn.setFixedLengthStreamingMode(file.size) // 메모리 사용 최적화
			httpsConn.doOutput = true // 서버에 데이터를 보낼수 있게 설정
			httpsConn.connect()

			// 버퍼 크기 설정 (128KB)
			val bufferSize = 131072
			try {
				BufferedInputStream(file.inputStream, bufferSize).use { bufferedInputStream ->
					BufferedOutputStream(
						httpsConn.outputStream, bufferSize
					).use { bufferedOutputStream ->
						val buffer = ByteArray(bufferSize)
						var bytesRead: Int
						while ((bufferedInputStream.read(buffer).also { bytesRead = it }) != -1) {
							bufferedOutputStream.write(buffer, 0, bytesRead)
						}
						bufferedOutputStream.flush()
						imageTransferService.finalize_().send() // image 전송 완료
						httpsConn.disconnect()

						val imageTransfer = imageTransferService.get().send().imageTransfer()
						log.debug("phase() : {}", imageTransfer.phase())
						return true
					}
				}
			} catch (e: IOException) {
				e.printStackTrace()
				log.error("disk image 업로드 실패")
				return false
			}
		} catch (e: Exception) {
			e.printStackTrace()
			return false
		} finally {
			httpsConn?.disconnect()
		}
	}
	*//**
     * [disableSSLVerification]
     * SSL 인증서를 검증하지 않도록 설정하는 메서드
     *//*
	private fun disableSSLVerification() {
		log.debug("disableSSLVerification ... ")
		try {
			val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
				override fun getAcceptedIssuers(): Array<X509Certificate> { return arrayOf() }
				override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
				override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
			})

			val sc = SSLContext.getInstance("TLS")
			sc.init(null, trustAllCerts, SecureRandom())
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)

			val allHostsValid = HostnameVerifier { _: String?, _: SSLSession? -> true }
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
		} catch (e: Exception) {
			log.error("something went WRONG ... reason: ${e.localizedMessage}")
		}
	}

*/



//	@Throws(Error::class)
//	override fun findAllDisksFromDataCenter(dataCenterId: String): List<DiskImageVo> {
//		log.info("findAllDisksFromDataCenter ... dataCenterId: {}", dataCenterId)
//		conn.findDataCenter(dataCenterId).getOrNull() ?: throw ErrorPattern.DATACENTER_NOT_FOUND.toException()
//
//		val storageDomains: List<StorageDomain> =
//			conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId)
//				.getOrDefault(listOf())
//
//		val disks: List<Disk> = storageDomains.flatMap { sd ->
//			conn.findAllAttachedStorageDomainDisksFromDataCenter(dataCenterId, sd.id())
//				.getOrDefault(listOf())
//		}
//		return disks.toDiskImageVos(conn)
//		// 연결대상때문에 필요한듯 == vm의 diskattachment에 disk id가 같은지 비교. 근데 전체 vms를 다 뒤져야함 => 복잡
//		return storageDomains.flatMap { sd ->
//			conn.findAllAttachedStorageDomainDisksFromDataCenter(dataCenterId, sd.id())
//				.getOrDefault(listOf())
////				.map { disk ->
////					val isAttached = conn.findAllVms()
////						.getOrDefault(listOf())
////						.any {
////							conn.findAllDiskAttachmentsFromVm(it.id())
////								.getOrDefault(listOf())
////								.any { d -> d.id() == disk.id() }
////						}
////					disk.toDiskImageVo(conn)
////				}
//		}
//	}
    //	override fun findAllNetworksFromDataCenter(dataCenterId: String): List<NetworkVo> {
//		log.info("findNetworksInDc ... dcId: {}", dataCenterId)
//		val networks: List<Network> =
//			conn.findAllNetworksFromFromDataCenter(dataCenterId)
//				.getOrDefault(listOf())
//		return networks.toNetworkVos(conn)
//	}
//
//	@Throws(Error::class)
//	override fun findAllClustersFromDataCenter(dataCenterId: String): List<ClusterVo> {
//		log.info("findAllClustersFromDataCenter ... dataCenterId: {}", dataCenterId)
//		val clusters: List<Cluster> =
//			conn.findAllClustersFromDataCenter(dataCenterId).
//				getOrDefault(listOf())
//		return clusters.toClusterVos(conn)
//	}


// region: 나중
    /*
            @Override
            public LunCreateVo setDiskLun(String dcId) {
                SystemService system = admin.getConnection().systemService();
                DataCenter dataCenter = system.dataCentersService().dataCenterService(dcId).get().follow("clusters").send().dataCenter();
                List<Host> hostList = system.hostsService().list().send().hosts();
                hostList.stream()
                        .filter(Host::clusterPresent)
                        .map(host ->
                                HostVo.builder()
                                        .id(host.id())
                                        .name(host.name())
                                .build()
                        )
                        .collect(Collectors.toList());
    
                return null;
            }
    
            // 스토리지 > 디스크 > 새로만들기 - 직접 LUN
            // 오케는 lun생성 없음(코드는 있음)
            @Override
            public CommonVo<Boolean> addDiskLun(LunCreateVo lun) {
                SystemService system = admin.getConnection().systemService();
                DisksService disksService = system.disksService();
                // Host host = system.hostsService().hostService(lunVo.getHostId()).get().send().host();
    
                // host 사용 -> 호스트는 상태 확인(예: LUN 표시) 및 LUN에 대한 기본 정보(예: 크기 및 일련 번호) 검색에 사용
                // host 사용X ->  데이터베이스 전용 작업. 스토리지에 액세스되지 않습니다.
    
                try{
                    DiskBuilder diskBuilder = new DiskBuilder();
                    diskBuilder
                            .alias(lun.getAlias())
                            .description(lun.getDescription())
                            .lunStorage(
                                new HostStorageBuilder()
                                    .host(new HostBuilder().id(lun.getHostId()).build())
                                    .type(lun.getStorageType())
                                    .logicalUnits(
                                        new LogicalUnitBuilder()
                                            .address(lun.getAddress())
                                            .port(lun.getPort())
                                            .target(lun.getTarget())
                                        .build()
                                    )
                                .build()
                            )
                    .build();
    
                    Disk disk = disksService.addLun().disk(diskBuilder).send().disk();
    
                    do {
                        log.info("ok");
                    } while (disk.status().equals(DiskStatus.OK));
    
                    log.info("성공: 새 가상 디스크 (lun) 생성");
                    return CommonVo.successResponse();
                } catch (Exception e) {
                    log.error("실패: 새 가상 디스크 (lun) 생성");
                    return CommonVo.failResponse(e.getMessage());
                }
            }
    
            @Override
            public CommonVo<Boolean> editDiskLun(LunCreateVo lunCreateVo) {
                SystemService system = admin.getConnection().systemService();
    
                return null;
            }
    
            @Override
            public CommonVo<Boolean> cancelUpload(String diskId) {
                SystemService system = admin.getConnection().systemService();
                imageTranService.cancel().send();
                return CommonVo.successResponse();
            }
    
            @Override
            public CommonVo<Boolean> pauseUpload(String diskId) {
                SystemService system = admin.getConnection().systemService();
    
                imageTranService.pause().send();
    
                return CommonVo.successResponse();
            }
    
            @Override
            public CommonVo<Boolean> resumeUpload(String diskId) {
                SystemService system = admin.getConnection().systemService();
    
                imageTranService.resume().send();
    
                return CommonVo.successResponse();
            }
    
            @Override
            public CommonVo<Boolean> downloadDisk() {
                SystemService system = admin.getConnection().systemService();
    
    
                return CommonVo.successResponse();
            }
    
            // 데이터가 많이 없음 생성요청
            @Override
            public List<VolumeVo> getVolumeVoList(String dcId) {
                SystemService system = admin.getConnection().systemService();
    
                return null;
            }
    
        private fun configureDiskBuilder(image: ImageCreateVo, isAdd: Boolean): DiskBuilder {
            log.info("configureDiskBuilder ... image: $image, isAdd: $isAdd")
            val diskBuilder = DiskBuilder()
                .name(image.alias)
                .description(image.description)
                .wipeAfterDelete(image.wipeAfterDelete) // 삭제후 초기화
                .shareable(image.isSharable) // 공유 가능 (공유가능 o 이라면 증분백업 안됨 FRONT에서 막기?)
                .backup(if (image.backup) DiskBackup.INCREMENTAL else DiskBackup.NONE) // 증분 백업 사용(기본이 true)
                .format(if (image.backup) DiskFormat.COW else DiskFormat.RAW) // 백업 안하면 RAW
    
            return if (isAdd) { // 생성
                diskBuilder
                    .provisionedSize(
                        BigInteger.valueOf(image.size.toLong()).multiply(BigInteger.valueOf(1024).pow(3))
                    ) // 값 받은 것을 byte로 변환하여 준다
                    .storageDomains(*arrayOf(StorageDomainBuilder().id(image.domainId).build()))
                    .sparse(image.sparse) // 할당정책: 씬 true
                    .diskProfile(DiskProfileBuilder().id(image.profileId).build()) // 없어도 상관없음
            } else { // 편집
                diskBuilder
                    .provisionedSize(
                        BigInteger.valueOf((image.size + image.appendSize).toLong())
                            .multiply(BigInteger.valueOf(1024).pow(3))
                    ) // 값 받은 것을 byte로 변환하여 준다
                    .id(image.id)
                // 기본 설정 크기, 데이터센터, 스토리지 도메인, 할당정책은 변경 불가능
            }
        }
    */


    companion object {
        private val log by LoggerDelegate()
    }
}