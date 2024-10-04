package com.itinfo.itcloud.service.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.model.storage.DiskProfileVo
import com.itinfo.itcloud.model.storage.StorageDomainVo
import com.itinfo.itcloud.service.storage.ItStorageServiceTest.Companion
import com.itinfo.util.ovirt.expectDiskStatus
import com.itinfo.util.ovirt.findDisk
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.disk.DiskFileItem
import org.apache.commons.io.IOUtils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ovirt.engine.sdk4.types.Disk
import org.ovirt.engine.sdk4.types.DiskStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files

/**
 * [ItDiskServiceTest]
 * [ItDiskService]에 대한 단위테스트
 * 디스크 서비스 테스트
 *
 * @author chlee
 * @author deh22
 * @since 2024.09.30
 */
@SpringBootTest
class ItDiskServiceTest {
    @Autowired
    private lateinit var service: ItDiskService

    private lateinit var dataCenterId: String
    private lateinit var clusterId: String // Default
    private lateinit var networkId: String // ovirtmgmt(dc: Default)
    private lateinit var host01: String // host01
    private lateinit var domainId: String // host01
    private lateinit var diskId: String // host01

    @BeforeEach
    fun setup() {
        dataCenterId = "023b0a26-3819-11ef-8d02-00163e6c8feb"
        clusterId = "023c79d8-3819-11ef-bf08-00163e6c8feb"
        networkId = "00000000-0000-0000-0000-000000000009"
        host01 = "671e18b2-964d-4cc6-9645-08690c94d249"
        domainId = "213b1a0a-b0c0-4d10-95a4-7aafed4f76b9"
        diskId = "ebe58983-3c96-473a-9553-98bee3606f0e"
    }


    /**
     * [should_findAll]
     * [ItDiskService.findAll] 의 단위테스트
     *
     * @see [ItDiskService.findAll]
     */
    @Test
    fun should_findAll() {
        log.debug("should_findAll ... ")
        val result: List<DiskImageVo> =
            service.findAll()

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
        assertThat(result.size, `is`(34))
    }

    /**
     * [should_findAllFromStorageDomain]
     * [ItDiskService.findAllFromStorageDomain] 의 단위테스트
     *
     * @see [ItDiskService.findAllFromStorageDomain]
     */
    @Test
    fun should_findAllFromStorageDomain() {
        log.debug("should_findAllFromStorageDomain ... ")
        val result: List<DiskImageVo> =
            service.findAllFromStorageDomain(domainId)

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
        assertThat(result.size, `is`(14))
    }

    /**
     * [should_findOne]
     * [ItDiskService.findOne] 의 단위테스트
     *
     * @see [ItDiskService.findOne]
     */
    @Test
    fun should_findOne() {
        log.debug("should_findDisk ... ")
		val result: DiskImageVo? =
			service.findOne(diskId)

		assertThat(result, `is`(not(nullValue())))
		println(result)
    }

    /**
     * [should_findAllDomainsFromDataCenter]
     * [ItDiskService.findAllDomainsFromDataCenter] 의 단위테스트
     *
     * @see [ItDiskService.findAllDomainsFromDataCenter]
     */
    @Test
    fun should_findAllDomainsFromDataCenter() {
        log.info("should_findAllDomainsFromDataCenter ... ")
        val result: List<StorageDomainVo> =
            service.findAllDomainsFromDataCenter(dataCenterId)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result.size, `is`(2))
        result.forEach { println(it) }
    }

    /**
     * [should_findAllDiskProfilesFromStorageDomain]
     * [ItDiskService.findAllDomainsFromDataCenter] 의 단위테스트
     *
     * @see [ItDiskService.findAllDiskProfilesFromStorageDomain]
     */
    @Test
    fun should_findAllDiskProfilesFromStorageDomain() {
        log.info("should_findAllDiskProfilesFromStorageDomain ... ")
        val result: List<DiskProfileVo> =
            service.findAllDiskProfilesFromStorageDomain(domainId)

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
        assertThat(result.size, `is`(1))
    }

    /**
     * [should_add]
     * [ItDiskService.add] 의 단위테스트
     *
     * @see [ItDiskService.add]
     */
    @Test
    fun should_add() {
        log.info("should_add... ")
        val storageDomainVo = IdentifiedVo.builder { id { domainId } }
        val diskProfileVo = IdentifiedVo.builder { id { "71ba3cf0-7062-4bff-9b36-e9141857d148" } }

        val addDisk =
            DiskImageVo.builder {
                alias { "a3" }
                size { 2 }
                description { "test" }
                storageDomainVo { storageDomainVo }
				diskProfileVo { diskProfileVo }
                sparse { false } // false 사전할당
                wipeAfterDelete { false }
                sharable { false }
                backup { true }
            }

        val addResult: DiskImageVo? =
            service.add(addDisk)

        assertThat(addResult, `is`(not(nullValue())))
        assertThat(addResult?.alias, `is`(addDisk.alias))
        assertThat(addResult?.size, `is`(addDisk.size))
        assertThat(addResult?.description, `is`(addDisk.description))
        assertThat(addResult?.storageDomainVo?.id, `is`(addDisk.storageDomainVo.id))
        assertThat(addResult?.diskProfileVo?.id, `is`(addDisk.diskProfileVo.id))
        assertThat(addResult?.sparse, `is`(addDisk.sparse))
        assertThat(addResult?.wipeAfterDelete, `is`(addDisk.wipeAfterDelete))
        assertThat(addResult?.sharable, `is`(addDisk.sharable))
        assertThat(addResult?.backup, `is`(addDisk.backup))
    }


    /**
     * [should_update]
     * [ItDiskService.update] 의 단위테스트
     *
     * @see [ItDiskService.update]
     */
    @Test
    fun should_update() {
        log.info("should_update... ")
        val storageDomainVo = IdentifiedVo.builder { id { domainId } }
        val diskProfileVo = IdentifiedVo.builder { id { "71ba3cf0-7062-4bff-9b36-e9141857d148" } }

        val updateDisk =
            DiskImageVo.builder {
                id { "471a36ae-822f-42cc-9947-c3aea332d644" }
                alias { "image1" }
                size { 4 }
                appendSize { 1 }
                description { "test" }
                storageDomainVo { storageDomainVo }
				diskProfileVo { diskProfileVo }
                wipeAfterDelete { false }
                sharable { false }
                backup { true }
            }

        val updateResult: DiskImageVo? =
            service.update(updateDisk)

        assertThat(updateResult, `is`(not(nullValue())))
//        assertThat(updateResult?.alias, `is`(updateDisk.alias))
        assertThat(updateResult?.description, `is`(updateDisk.description))
        assertThat(updateResult?.wipeAfterDelete, `is`(updateDisk.wipeAfterDelete))
        assertThat(updateResult?.sharable, `is`(updateDisk.sharable))
        assertThat(updateResult?.backup, `is`(updateDisk.backup))
    }

    /**
     * [should_remove]
     * [ItDiskService.remove] 의 단위테스트
     *
     * @see [ItDiskService.remove]
     */
    @Test
    fun should_remove() {
        log.info("should_remove ... ")
        val diskId = "471a36ae-822f-42cc-9947-c3aea332d644"
        val result: Boolean =
            service.remove(diskId)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }


    /**
     * [should_findAllStorageDomainsFromDisk]
     * [ItDiskService.findAllStorageDomainsFromDisk] 의 단위테스트
     *
     * @see [ItDiskService.findAllStorageDomainsFromDisk]
     */
    @Test
    fun should_findAllStorageDomainsFromDisk(){
        log.info("should_findAllStorageDomainsFromDisk ... ")
        val diskId2 = "f6896f27-7ba6-4d0d-b204-1a6aa2c16d86"
        val result: List<StorageDomainVo> =
            service.findAllStorageDomainsFromDisk(diskId2)

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
        assertThat(result.size, `is`(1))
    }

    /**
     * [should_move]
     * [ItDiskService.move] 의 단위테스트
     *
     * @see [ItDiskService.move]
     */
    @Test
    fun should_move() {
        val diskId = "f6896f27-7ba6-4d0d-b204-1a6aa2c16d86"
        val domainId2 = "213b1a0a-b0c0-4d10-95a4-7aafed4f76b9"
        val result: Boolean =
            service.move(diskId, domainId2)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }

    /**
     * [should_copy]
     * [ItDiskService.copy] 의 단위테스트
     *
     * @see [ItDiskService.copy]
     */
    @Test
    fun should_copy() {
        val diskId = "2c73c9c0-6552-4ddc-9727-8b2de7f54267"
        val domainId2 = "213b1a0a-b0c0-4d10-95a4-7aafed4f76b9"
        val storageDomainVo = IdentifiedVo.builder { id { domainId2 }}

        val image =
            DiskImageVo.builder {
                id { diskId }
                alias { "copytest" }
                storageDomainVo { storageDomainVo }
            }
        val result =
            service.copy(image)

        assertThat(result, `is`(true))
    }

    /**
     * [should_upload]
     * [ItDiskService.upload] 의 단위테스트
     *
     * @see [ItDiskService.upload]
     */
    @Test
    @Throws(IOException::class)
    fun should_upload() {
        // test환경에서는 실패할 경우 있음
        val path = "C:/Users/deh22/Documents/Rocky-8.4-x86_64-minimal.iso"

        val file = File(path)
        val fileItem: FileItem = DiskFileItem(
            "file",
            Files.probeContentType(file.toPath()),
            false,
            file.name,
            file.length().toInt(),
            file.parentFile
        )
        val inputStream: InputStream = FileInputStream(file)
        val outputStream = fileItem.outputStream
        IOUtils.copy(inputStream, outputStream)
        val multipartFile: MultipartFile = CommonsMultipartFile(fileItem)

        val storageDomainVo = IdentifiedVo.builder {
            id { domainId }
        }
//		val diskProfileVo = IdentifiedVo.builder {
//			id { diskProfile }
//		}

        val iVo =
            DiskImageVo.builder {
                alias { "absc" }
                description { "test" }
                storageDomainVo { storageDomainVo }
//				diskProfileVo { diskProfileVo }
                sparse { false }
                wipeAfterDelete { false }
                sharable { false }
                backup { false }
            }

        val result =
            service.upload(multipartFile, iVo)
    }


    /**
     * [should_findAllVmsFromDisk]
     * [ItDiskService.findAllVmsFromDisk] 의 단위테스트
     *
     * @see [ItDiskService.findAllVmsFromDisk]
     */
    @Test
    fun should_findAllVmsFromDisk() {
        log.info("should_findAllVmsFromDisk ... ")
        val diskId = "6ebde818-0b00-425d-b1c2-8a6c066140c8"
        val result: List<VmVo> =
            service.findAllVmsFromDisk(diskId)

        assertThat(result, `is`(not(nullValue())))
//		assertThat(result.size, `is`(0))
        result.forEach { println(it) }
    }

//    /**
//     * [should_findAllPermissionsFromDisk]
//     * [ItDiskService.findAllPermissionsFromDisk] 의 단위테스트
//     *
//     * @see [ItDiskService.findAllPermissionsFromDisk]
//     */
//    @Test
//    fun should_findAllPermissionsFromDisk() {
//        log.info("should_findAllPermissionsFromDisk ... ")
//        val diskId = "6ebde818-0b00-425d-b1c2-8a6c066140c8"
//        val result: List<PermissionVo> =
//            service.findAllPermissionsFromDisk(diskId)
//
//        assertThat(result, `is`(not(nullValue())))
//        assertThat(result.size, `is`(5))
//        result.forEach { println(it) }
//    }

    companion object{
        private val log by LoggerDelegate()
    }

}