package com.itinfo.itcloud.service.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.computing.EventVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.model.storage.DiskProfileVo
import com.itinfo.itcloud.model.storage.StorageDomainVo
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.disk.DiskFileItem
import org.apache.commons.io.IOUtils

import org.assertj.core.api.Assertions
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
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
 * [ItStorageServiceTest]
 * 스토리지 서비스 테스트
 *
 * @author chlee
 * @since 2024.07.15
 */
@SpringBootTest
class ItStorageServiceTest {
	@Autowired private lateinit var service: ItStorageService
	private lateinit var dataCenterId: String
	private lateinit var domainId: String
	private lateinit var diskProfile: String
	private lateinit var diskId: String

	@BeforeEach
	fun setup() {
		dataCenterId = "6cde7270-6459-11ef-8be2-00163e5d0646"
		domainId = "dc38dcb4-c3f9-4568-af0b-0d6a225d25e5" //hosted_storage
		diskProfile = "df3d6b80-5326-4855-96a4-455147016fc7"
		diskId = "c42fcba8-9021-4dea-b23a-802eb932247c"
	}

	/**
	 * [should_findAllStorageDomainsFromDataCenter]
	 * [ItStorageService.findAllStorageDomainsFromDataCenter] 의 단위테스트
	 *
	 * @see [ItStorageService.findAllStorageDomainsFromDataCenter]
	 */
	@Test
	fun should_findAllStorageDomainsFromDataCenter() {
		log.debug("should_findAllStorageDomainsFromDataCenter ... ")
		val result: List<StorageDomainVo> =
			service.findAllStorageDomainsFromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
//		assertThat(result.size, `is`(1))

		result.forEach { println(it) }
	}

	/**
	 * [should_findStorageDomain]
	 * [ItStorageService.findStorageDomain] 의 단위테스트
	 *
	 * @see [ItStorageService.findStorageDomain]
	 */
	@Test
	fun should_findStorageDomain() {
		log.debug("should_findStorageDomain ... ")
		val result: StorageDomainVo =
			service.findStorageDomain(domainId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.id, `is`(domainId))
		println(result)
	}

	/**
	 * [should_findAllHostsFromDataCenter]
	 * [ItStorageService.findAllHostsFromDataCenter] 의 단위테스트
	 *
	 * @see [ItStorageService.findAllHostsFromDataCenter]
	 */
	@Test
	fun should_findAllHostsFromDataCenter() {
		log.debug("should_findAllHostsFromDataCenter ... ")
		val result: List<IdentifiedVo> =
			service.findAllHostsFromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(2))

		result.forEach { println(it) }
	}

	/**
	 * [should_addDomain]
	 * [ItStorageService.addDomain] 의 단위테스트
	 *
	 * @see [ItStorageService.addDomain]
	 */
	@Test
	fun should_addDomain() {
		log.debug("should_addDomain ... ")



	}

	/**
	 * [should_removeDomain]
	 * [ItStorageService.removeDomain] 의 단위테스트
	 *
	 * @see [ItStorageService.removeDomain]
	 */
	@Test
	fun should_removeDomain() {
		log.debug("should_removeDomain ... ")



	}


	/**
	 * [should_findAllDisksFromStorageDomain]
	 * [ItStorageService.findAllDisksFromStorageDomain] 의 단위테스트
	 *
	 * @see [ItStorageService.findAllDisksFromStorageDomain]
	 */
	@Test
	fun should_findAllDisksFromStorageDomain() {
		log.debug("should_findAllDisksFromStorageDomain ... ")
		val result: List<DiskImageVo> =
			service.findAllDisksFromStorageDomain(domainId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(14))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllPermissionsFromStorageDomain]
	 * [ItStorageService.findAllPermissionsFromStorageDomain] 의 단위테스트
	 *
	 * @see [ItStorageService.findAllPermissionsFromStorageDomain]
	 */
	@Test
	fun should_findAllPermissionsFromStorageDomain() {
		log.debug("should_findAllPermissionsFromStorageDomain ... ")
		val result: List<PermissionVo> =
			service.findAllPermissionsFromStorageDomain(domainId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(4))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllEventsFromStorageDomain]
	 * [ItStorageService.findAllEventsFromStorageDomain] 의 단위테스트
	 *
	 * @see [ItStorageService.findAllEventsFromStorageDomain]
	 */
	@Test
	fun should_findAllEventsFromStorageDomain() {
		log.debug("should_findAllEventsFromStorageDomain ... ")
		val result: List<EventVo> =
			service.findAllEventsFromStorageDomain(domainId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(33))
		println(result.size)
		result.forEach { println(it) }
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
		val result: DiskImageVo =
			service.findDisk(diskId)

		assertThat(result, `is`(not(nullValue())))
		println(result)
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
		val diskProfileVo = IdentifiedVo.builder { id { diskProfile } }

		val addDisk =
			DiskImageVo.builder {
				alias { "image4" }
				size { 2 }
				description { "test" }
				storageDomainVo { storageDomainVo }
				diskProfileVo { diskProfileVo }
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
		val diskProfileVo = IdentifiedVo.builder { id { diskProfile } }

		val updateDisk =
			DiskImageVo.builder {
				id { "7db6dfa4-d757-4f06-b6e8-02271a88bd0a" }
				alias { "image41" }
				size { 3 }
				appendSize { 1 }
				description { "test" }
				storageDomainVo { storageDomainVo }
				diskProfileVo { diskProfileVo }
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


	// TODO 스토리지도메인 2개이상이어야 테스트 가능
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
		val diskProfileVo = IdentifiedVo.builder {
			id { diskProfile }
		}

		val iVo =
			DiskImageVo.builder {
				alias { "absc" }
				description { "test" }
				storageDomainVo { storageDomainVo }
				diskProfileVo { diskProfileVo }
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


	companion object {
		private val log by LoggerDelegate()
	}
}