package com.itinfo.itcloud.service.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.computing.EventVo
import com.itinfo.itcloud.model.network.NetworkVo
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
		diskProfile = ""
		diskId = ""
	}

	/**
	 * [should_findAllFromDataCenter]
	 * [ItStorageService.findAllFromDataCenter] 의 단위테스트
	 *
	 * @see [ItStorageService.findAllFromDataCenter]
	 */
	@Test
	fun should_findAllFromDataCenter() {
		log.debug("should_findAllFromDataCenter ... ")
		val result: List<StorageDomainVo> =
			service.findAllStorageDomainsFromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(2))

		result.forEach { println(it) }
	}

	/**
	 * [should_findOne]
	 * [ItStorageService.findOne] 의 단위테스트
	 *
	 * @see [ItStorageService.findOne]
	 */
	@Test
	fun should_findOne() {
		log.debug("should_findOne ... ")
		val result: StorageDomainVo =
			service.findStorageDomain(domainId)

		assertThat(result, `is`(not(nullValue())))
		println(result)
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
	 * [should_findAllDisksFromDataCenter]
	 * [ItStorageService.findAllDisksFromDataCenter] 의 단위테스트
	 *
	 * @see [ItStorageService.findAllDisksFromDataCenter]
	 */
	@Test
	fun should_findAllDisksFromDataCenter() {
		log.debug("should_findAllDisksFromDataCenter ... ")
		val result: List<DiskImageVo> =
			service.findAllDisksFromStorageDomain(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(14))

		result.forEach { println(it) }
	}

	/**
	 * [should_findAllDataCenters]
	 * [ItStorageService.findAllDataCenters] 의 단위테스트
	 *
	 * @see [ItStorageService.findAllDataCenters]
	 */
	@Test
	@Deprecated("중복")
	fun should_findAllDataCenters() {
		log.info("should_findAllDataCenters ... ")
		val result: List<DataCenterVo> =
			service.findAllDataCenters()

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(1))
		result.forEach { println(it) }
	}

	/**
	 * [should_findAllStorageDomainsfromDataCenter]
	 * [ItStorageService.findAllStorageDomainsfromDataCenter] 의 단위테스트
	 *
	 * @see [ItStorageService.findAllStorageDomainsfromDataCenter]
	 */
	@Test
	fun should_findAllStorageDomainsfromDataCenter() {
		log.info("should_findAllStorageDomainsfromDataCenter ... ")
		val result: List<StorageDomainVo> =
			service.findAllStorageDomainsfromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(1))
		result.forEach { println(it) }
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
	 * [should_addDiskImage]
	 * [ItStorageService.addDiskImage] 의 단위테스트
	 *
	 * @see [ItStorageService.addDiskImage]
	 */
	@Test
	fun should_addDiskImage() {
		val storageDomainVo = IdentifiedVo.builder { id { domainId } }
		val diskProfileVo = IdentifiedVo.builder { id { diskProfile } }

		val addDisk =
			DiskImageVo.builder {
				alias { "image1" }
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
//		assertThat(addResult?.storageDomainVo?.id, `is`(addDisk.storageDomainVo.id))
//		assertThat(addResult?.diskProfileVo?.id, `is`(addDisk.diskProfileVo.id))
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
				id { "" }
				alias { "image1" }
				size { 2 }
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
		assertThat(updateResult?.size, `is`(updateDisk.size))
		assertThat(updateResult?.description, `is`(updateDisk.description))
//		assertThat(updateResult?.storageDomainVo?.id, `is`(updateDisk.storageDomainVo.id))
//		assertThat(updateResult?.diskProfileVo?.id, `is`(updateDisk.diskProfileVo.id))
		assertThat(updateResult?.sparse, `is`(updateDisk.sparse))
		assertThat(updateResult?.wipeAfterDelete, `is`(updateDisk.wipeAfterDelete))
		assertThat(updateResult?.sharable, `is`(updateDisk.sharable))
		assertThat(updateResult?.backup, `is`(updateDisk.backup))
	}


	@Test
	fun should_deleteDisk() {
		val diskId = "b0ded29b-04fc-451b-a7a4-bfd436f47890"
		val result: Boolean =
			service.removeDisk(diskId)

		assertThat(result, `is`((nullValue())))
		assertThat(result, `is`(true))
	}


	@Test
	fun should_moveDisk() {
		val diskId = "f89493dd-51f8-44bd-9bfb-4687f43c822c"
		val domainId = "43a786b3-e37e-4545-ba1f-5e3ae0ca6f0f"
		val result: Boolean =
			service.moveDisk(diskId, domainId)

		assertThat(result, `is`((nullValue())))
		assertThat(result, `is`(true))
	}


	@Test
	fun should_copyDisk() {
		val diskId = "f89493dd-51f8-44bd-9bfb-4687f43c822c"
		val domainId = "12d17014-a612-4b6e-a512-6ec4e1aadba6"
		val storageDomainVo = IdentifiedVo.builder { id { domainId }}

		val image =
			DiskImageVo.builder {
				alias { "abcdTest2" }
				description { "test" }
				storageDomainVo { storageDomainVo }
			}
		val result =
			service.copyDisk(image)

		assertThat(result, `is`((nullValue())))
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


	@Test
	fun should_setHostList() {
		log.debug("should_setHostList ... ")
		val result = service.findAllHostsFromDataCenter(dataCenterId)
		result.forEach { x: IdentifiedVo? -> println(x) }
	}

	@Test
	fun should_addDomain() {
		log.debug("should_addDomain ... ")
	}

	@Test
	fun should_deleteDomain() {
		log.debug("should_deleteDomain ... ")
	}


	@Test
	fun should_findPermission() {
		log.debug("should_findPermission ... ")
		
	}

	@Test
	fun findEvent() {
		log.debug("findEvent ... ")
		val result: List<EventVo> =
			service.findAllEventsFromStorageDomain(domainId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(33))
		println(result.size)
		result.forEach { println(it) }
	}
	
	companion object {
		private val log by LoggerDelegate()
	}
}