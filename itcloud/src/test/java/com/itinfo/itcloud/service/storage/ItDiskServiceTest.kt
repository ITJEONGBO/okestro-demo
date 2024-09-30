package com.itinfo.itcloud.service.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.model.storage.DiskProfileVo
import com.itinfo.itcloud.model.storage.StorageDomainVo
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.disk.DiskFileItem
import org.apache.commons.io.IOUtils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

    @BeforeEach
    fun setup() {
        dataCenterId = "023b0a26-3819-11ef-8d02-00163e6c8feb"
        clusterId = "023c79d8-3819-11ef-bf08-00163e6c8feb"
        networkId = "00000000-0000-0000-0000-000000000009"
        host01 = "671e18b2-964d-4cc6-9645-08690c94d249"
        domainId = "213b1a0a-b0c0-4d10-95a4-7aafed4f76b9"
    }



    /**
     * [should_findDisk]
     * [ItStorageService.findDisk] 의 단위테스트
     *
     * @see [ItStorageService.findDisk]
     */
    @Test
    fun should_findDisk() {
        log.debug("should_findDisk ... ")
//		val result: DiskImageVo? =
//			service.findDisk(diskId)
//
//		assertThat(result, `is`(not(nullValue())))
//		println(result)
    }


    /**
     * [should_findAllDiskProfilesFromStorageDomain]
     * [ItStorageService.findAllDiskProfilesFromStorageDomain] 의 단위테스트
     *
     * @see [ItStorageService.findAllDiskProfilesFromStorageDomain]
     */
    @Test
    fun should_findAllDiskProfilesFromStorageDomain() {
        log.info("should_findAllDiskProfilesFromStorageDomain ... ")
        val result: List<DiskProfileVo> =
            service.findAllDiskProfilesFromStorageDomain(domainId)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result.size, `is`(1))
        result.forEach { println(it) }
    }

    /**
     * [should_addDisk]
     * [ItStorageService.addDisk] 의 단위테스트
     *
     * @see [ItStorageService.addDisk]
     */
    @Test
    fun should_addDisk() {
        log.info("should_addDisk ... ")
        val storageDomainVo = IdentifiedVo.builder { id { domainId } }
//		val diskProfileVo = IdentifiedVo.builder { id { diskProfile } }

        val addDisk =
            DiskImageVo.builder {
                alias { "image4" }
                size { 2 }
                description { "test" }
                storageDomainVo { storageDomainVo }
//				diskProfileVo { diskProfileVo }
                sparse { true }
                wipeAfterDelete { false }
                sharable { false }
                backup { true }
            }

        val addResult: DiskImageVo? =
            service.addDisk(addDisk)

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


    @Test
    fun should_editDiskImage() {
        val storageDomainVo = IdentifiedVo.builder { id { domainId } }
//		val diskProfileVo = IdentifiedVo.builder { id { diskProfile } }

        val updateDisk =
            DiskImageVo.builder {
                id { "7db6dfa4-d757-4f06-b6e8-02271a88bd0a" }
                alias { "image41" }
                size { 3 }
                appendSize { 1 }
                description { "test" }
                storageDomainVo { storageDomainVo }
//				diskProfileVo { diskProfileVo }
                sparse { true }
                wipeAfterDelete { false }
                sharable { false }
                backup { true }
            }

        val updateResult: DiskImageVo? =
            service.updateDisk(updateDisk)

        assertThat(updateResult, `is`(not(nullValue())))
        assertThat(updateResult?.alias, `is`(updateDisk.alias))
//		assertThat(updateResult?.size, `is`(updateDisk.size))
        assertThat(updateResult?.description, `is`(updateDisk.description))
//		assertThat(updateResult?.storageDomainVo?.id, `is`(updateDisk.storageDomainVo.id))
//		assertThat(updateResult?.diskProfileVo?.id, `is`(updateDisk.diskProfileVo.id))
        assertThat(updateResult?.sparse, `is`(updateDisk.sparse))
        assertThat(updateResult?.wipeAfterDelete, `is`(updateDisk.wipeAfterDelete))
        assertThat(updateResult?.sharable, `is`(updateDisk.sharable))
        assertThat(updateResult?.backup, `is`(updateDisk.backup))
    }


    @Test
    fun should_removeDisk() {
        val diskId = "7b5522b4-cf3e-4b61-a135-3284a97105dc"
        val result: Boolean =
            service.removeDisk(diskId)

        assertThat(result, `is`(true))
    }

    // TODO 추가 구현필요
    @Test
    fun should_findAllStorageDomainsFromDisk(){
        log.info("should_findAllStorageDomainsFromDisk ... ")
        val diskId2 = "e5c11e4d-53d8-442d-9bcd-f2d0fc6b2731"
        val result: List<StorageDomainVo> =
            service.findAllStorageDomainsFromDisk(diskId2)

        assertThat(result.size, `is`(1))

        result.forEach { println(it) }
    }


    @Test
    fun should_moveDisk() {
        val diskId = "2c73c9c0-6552-4ddc-9727-8b2de7f54267"
        val result: Boolean =
            service.moveDisk(diskId, domainId)

        assertThat(result, `is`(true))
    }


    @Test
    fun should_copyDisk() {
        val diskId = "2c73c9c0-6552-4ddc-9727-8b2de7f54267"
        val domainId2 = ""
        val storageDomainVo = IdentifiedVo.builder { id { domainId }}

        val image =
            DiskImageVo.builder {
                id { diskId }
                alias { "image4test" }
                storageDomainVo { storageDomainVo }
            }
        val result =
            service.copyDisk(image)

        assertThat(result, `is`(true))
    }


    @Test
    @Throws(IOException::class)
    fun should_uploadDisk() {
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
            service.uploadDisk(multipartFile, iVo)
    }


    /**
     * [should_findAllVmsFromDisk]
     * [ItStorageService.findAllVmsFromDisk] 의 단위테스트
     *
     * @see [ItStorageService.findAllVmsFromDisk]
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

    /**
     * [should_findAllPermissionsFromDisk]
     * [ItStorageService.findAllPermissionsFromDisk] 의 단위테스트
     *
     * @see [ItStorageService.findAllPermissionsFromDisk]
     */
    @Test
    fun should_findAllPermissionsFromDisk() {
        log.info("should_findAllPermissionsFromDisk ... ")
        val diskId = "6ebde818-0b00-425d-b1c2-8a6c066140c8"
        val result: List<PermissionVo> =
            service.findAllPermissionsFromDisk(diskId)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result.size, `is`(5))
        result.forEach { println(it) }
    }

    companion object{
        private val log by LoggerDelegate()
    }

}