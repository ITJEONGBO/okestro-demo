package com.itinfo.itcloud.service.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.computing.toVmVoInfos
import com.itinfo.itcloud.model.computing.toVmsMenu
import com.itinfo.itcloud.model.response.Res
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.repository.engine.DiskVmElementRepository
import com.itinfo.itcloud.repository.engine.entity.DiskVmElementEntity
import com.itinfo.itcloud.repository.engine.entity.toVmId
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.services.ImageTransferService
import org.ovirt.engine.sdk4.types.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.streams.toList


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
     * [ItDiskService.findAllStorageDomainsToMoveFromDisk]
     * 디스크 이동- 창
     * TODO 디스크 이동시 대상은 디스크가 가지고 있는 스토리지 도메인은 목록에서 제외
     * ItDiskService.findAllStorageDomainsFromDataCenter 에서 disk가 가지고있는 스토리지도메인은 제외
     *
     * @param diskId [String] 디스크 ID
     * @return List<[StorageDomainVo]>
     */
    @Throws(Error::class)
    fun findAllStorageDomainsToMoveFromDisk(diskId: String): List<StorageDomainVo>
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
     * [ItDiskService.findAllStorageDomainsFromDisk]
     * 스토리지도메인 - 스토리지
     *
     * @param diskId [String] 디스크 ID
     * @return List<[StorageDomainVo]>
     */
    @Throws(Error::class)
    fun findAllStorageDomainsFromDisk(diskId: String): List<StorageDomainVo>
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

    @Autowired private lateinit var diskVmElementRepository: DiskVmElementRepository

    @Throws(Error::class)
    override fun findAll(): List<DiskImageVo> {
        log.info("findAll ... ")
        val res: List<Disk> =
            conn.findAllDisks().getOrDefault(listOf())

        val diskIds = res.map { UUID.fromString(it.id()) }
        val diskVmElements = diskVmElementRepository.findByDiskIdIn(diskIds)
        val diskVmElementMap = diskVmElements.associateBy { it.diskId }

        return res.parallelStream().map { disk ->
            val diskVmElementEntityOpt = diskVmElementMap[UUID.fromString(disk.id())]
            val id: String = diskVmElementEntityOpt?.toVmId() ?: ""
            disk.toDiskMenu(conn, id)
        }.toList()
    }

    @Throws(Error::class)
    override fun findOne(diskId: String): DiskImageVo? {
        log.info("findOne ... diskId: $diskId")
        val res: Disk? = conn.findDisk(diskId)
            .getOrNull()
        return res?.toDiskInfo(conn)
    }

    @Throws(Error::class)
    override fun findAllDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo> {
        log.info("findAllStorageDomainsFromDataCenter ... dataCenterId: $dataCenterId")
        val res: List<StorageDomain> =
            conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId).getOrDefault(listOf())
                .filter { it.status() == StorageDomainStatus.ACTIVE }
        return res.toStorageDomainSizes()
    }

    @Throws(Error::class)
    override fun findAllDiskProfilesFromStorageDomain(storageDomainId: String): List<DiskProfileVo> {
        log.info("findAllDiskProfilesFromStorageDomain ... domainId: $storageDomainId")
        val res: List<DiskProfile> =
            conn.findAllDiskProfilesFromStorageDomain(storageDomainId).getOrDefault(listOf())
        return res.toDiskProfileVos()
    }

    @Throws(Error::class)
    override fun add(image: DiskImageVo): DiskImageVo? {
        log.info("addDisk ... image: $image")
        val res: Disk? =
            conn.addDisk(
                image.toAddDiskBuilder()
            ).getOrNull()
        return res?.toDiskIdName()
    }

    @Throws(Error::class)
    override fun update(image: DiskImageVo): DiskImageVo? {
        log.info("updateDisk ... image: $image")
        val res: Disk? =
            conn.updateDisk(
                image.toEditDiskBuilder()
            ).getOrNull()
        return res?.toDiskIdName()
    }

    @Throws(Error::class)
    override fun remove(diskId: String): Boolean {
        log.info("removeDisk ... diskId: $diskId")
        val res: Result<Boolean> =
            conn.removeDisk(diskId)
        return res.isSuccess
    }

    @Throws(Error::class)
    override fun findAllStorageDomainsToMoveFromDisk(diskId: String): List<StorageDomainVo> {
        log.info("findAllStorageDomainsToMoveFromDisk ... diskId: $diskId")
        val disk: Disk =
            conn.findDisk(diskId).getOrNull()
                ?: throw ErrorPattern.DISK_NOT_FOUND.toException()
        val res: List<StorageDomain> =
            conn.findAllStorageDomains().getOrDefault(listOf())
                .filter { it.id() != disk.storageDomains().first().id() }
        return res.toStorageDomainSizes()
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

        // disk에 연결된 vm이 up이면 복사 불가능
        val res: Result<Boolean> =
            conn.copyDisk(
                diskImageVo.id,
                diskImageVo.alias,
                diskImageVo.storageDomainVo.id
            )
        return res.isSuccess
    }

    @Throws(Error::class, IOException::class)
    override fun upload(file: MultipartFile, image: DiskImageVo): Boolean {
        log.info("uploadDisk ... ")
        if (file.isEmpty)
            throw ErrorPattern.FILE_NOT_FOUND.toException()
        log.debug("${file.size}")
		val imageTransferId: String =
            conn.uploadSetDisk(image.toUploadDiskBuilder(file.size))
                .getOrNull() ?: throw ErrorPattern.DISK_NOT_FOUND.toException()

        val res = upload2(file, imageTransferId)
        // tODO 안됨
		return res
    }

    @Throws(Error::class)
    fun upload3(file: MultipartFile, imageTransferId: String): Boolean{
        log.debug("imageSend")
        var httpsConn: HttpsURLConnection? = null
        val imageTransferService: ImageTransferService = conn.srvImageTransfer(imageTransferId)
//            disableSSLVerification()

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true")
        val url = URL(imageTransferService.get().send().imageTransfer().transferUrl())
        httpsConn = url.openConnection() as HttpsURLConnection
        httpsConn.requestMethod = "PUT"
        httpsConn.setRequestProperty("Content-Length", file.size.toString())
        httpsConn.setFixedLengthStreamingMode(file.size.toInt())
        httpsConn.doOutput = true
        httpsConn.connect()

        val bufferSize = 131072
        file.inputStream.use { inputStream ->
            BufferedInputStream(inputStream, bufferSize).use { bis ->
                BufferedOutputStream(httpsConn.outputStream, bufferSize).use { bos ->
                    val buffer = ByteArray(bufferSize)
                    var bytesRead: Int
                    while (bis.read(buffer).also { bytesRead = it } != -1) {
                        bos.write(buffer, 0, bytesRead)
                    }
                    bos.flush()
                }
            }
        }
        imageTransferService.finalize_().send()
        httpsConn.disconnect()

        return true
    }

    @Throws(Error::class)
    fun upload2(file: MultipartFile, imageTransferId: String): Boolean {
        log.info("upload2 .. ")
        val https: HttpsURLConnection
        val imageTransferService: ImageTransferService =
            conn.srvImageTransfer(imageTransferId)

        disableSSLVerification()
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true")
        val url = URL(imageTransferService.get().send().imageTransfer().transferUrl())

        https = url.openConnection() as HttpsURLConnection
        https.setRequestMethod("PUT")
        https.setRequestProperty("PUT", url.path)
        https.setRequestProperty("Content-Length", file.size.toString())
        https.setFixedLengthStreamingMode(file.size)
        https.setDoOutput(true)
        log.debug("7")
        https.connect()
        log.debug("8")

        val bufferSize = 131072
        val bufferedInputStream = BufferedInputStream(file.inputStream, bufferSize)
        val bufferedOutputStream = BufferedOutputStream(https.outputStream, bufferSize)

        log.debug("9")
        val buffer = ByteArray(bufferSize)
        var bytesRead: Int
        log.debug("10")
        while (bufferedInputStream.read(buffer).also { bytesRead = it } != -1) {
            bufferedOutputStream.write(buffer, 0, bytesRead)
        }
        bufferedOutputStream.flush()
        log.debug("11")

        imageTransferService.finalize_().send()
        log.debug("12")
        https.disconnect()
        return true
    }

    @Throws(Error::class)
    fun disableSSLVerification() {
        log.debug("disableSSLVerification")
        val hostnameVerifier = HostnameVerifier { _, _ -> true }
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate>? {
                    return null
                }
                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
            }
        )

        val sc = SSLContext.getInstance("TLS")
        sc.init(null, trustAllCerts, SecureRandom())
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier)
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
    }

    @Throws(Error::class)
    override fun refreshLun(diskId: String, hostId: String): Boolean {
        log.info("refreshLun ... ")
        // TODO HostId 구하는 방법
        val res: Result<Boolean> =
            conn.refreshLunDisk(diskId, hostId)
        return res.isSuccess
    }


    @Throws(Error::class)
    override fun findAllVmsFromDisk(diskId: String): List<VmVo> {
        log.info("findAllVmsFromDisk ... ")
        val res: List<Vm> =
            conn.findAllVmsFromDisk(diskId).getOrDefault(listOf())
        return res.toVmsMenu(conn)
    }

    @Throws(Error::class)
    override fun findAllStorageDomainsFromDisk(diskId: String): List<StorageDomainVo> {
        log.info("findAllStorageDomainsFromDisk ... diskId: $diskId")
        val res: List<StorageDomain> =
            conn.findAllStorageDomainsFromDisk(diskId).getOrDefault(listOf())
        return res.toStorageDomainsMenu(conn)
    }

    @Deprecated("")
    @Throws(Error::class)
    override fun findAllPermissionsFromDisk(diskId: String): List<PermissionVo> {
        log.info("findAllPermissionsFromDisk ... diskId: {}", diskId)
        val res: List<Permission> =
            conn.findAllPermissionsFromDisk(diskId).getOrDefault(listOf())
        return res.toPermissionVos(conn)
    }


    companion object {
        private val log by LoggerDelegate()
    }
}