package com.itinfo.itcloud.service.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.ClusterVo
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.network.UsageVo
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
	@Autowired private lateinit var storageService: ItStorageService
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


	@Test
	fun should_findTotalStorage() {
		log.debug("should_findTotalStorage ... ")
		val result: List<StorageDomainVo> =
			storageService.findAllStorageDomainsFromDataCenter(dataCenterId)

		Assertions.assertThat(result.size).isEqualTo(2)
		log.debug("result: $result")
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
			storageService.findAllDisksFromDataCenter(dataCenterId)
		val aliases: List<String> = result.map { disk: DiskImageVo ->
			log.debug("alias: ${disk.alias}")
			return@map disk.alias
		}
		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(1))

		result.forEach { println(it) }
//		Assertions.assertThat(disks.size).isEqualTo(38)
	}

	@Test
	@DisplayName("디스크- 데이터센터 목록")
	fun should_setDcList() {
		val result: List<DataCenterVo> = storageService.findAllDataCenters()
		result.forEach { _ -> println(this) }
	}

//	@Test
//	@DisplayName("디스크- 스토리지 도메인 목록")
//	fun should_setDomainList() {
//		val result: List<StorageDomainVo> = storageService.setDomainList(dcId, "")
//		result.forEach { x: StorageDomainVo? -> println(x) }
//	}


	@Test
	@DisplayName("디스크- 디스크 프로파일 목록")
	fun should_setDiskProfile() {
		val result: List<DiskProfileVo> = storageService.findAllDiskProfilesFromStorageDomain(domainId)
		result.forEach { x: DiskProfileVo? -> println(x) }
	}


	@Test
	@DisplayName("디스크 이미지 생성")
	fun should_addDiskImage() {
		val storageDomainVo = IdentifiedVo.builder {
			id { domainId }
		}
		val diskProfileVo = IdentifiedVo.builder {
			id { diskProfile }
		}

		val image =
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

		val result: DiskImageVo? = storageService.addDiskImage(image)
		Assertions.assertThat(result?.alias).isEqualTo(image.alias)
	}


	@Test
	@DisplayName("디스크 이미지 수정창")
	fun should_setDiskImage() {
		val diskId = "d1947b58-84df-4ff3-802a-b4a6497853c6"
		val result = storageService.findOneDisk(diskId)

		println(result)
	}


	@Test
	@DisplayName("디스크 이미지 수정")
	fun should_editDiskImage() {
		val diskId2 = "d1947b58-84df-4ff3-802a-b4a6497853c6"

		val image =
			DiskImageVo.builder {
				id { diskId2 }
				alias { "Sdd1" }
				size { 3 }
				appendSize { 0 }
				description { "tes2t" }
				sparse { false }
				wipeAfterDelete { false }
				sharable { false }
				backup { true }
			}

		val result = storageService.updateDiskImage(image)

		Assertions.assertThat(result?.alias).isEqualTo(image.alias)
	}


	@Test
	@DisplayName("디스크 이미지 삭제")
	fun should_deleteDisk() {
		val diskId = "b0ded29b-04fc-451b-a7a4-bfd436f47890"
		val result: Boolean = storageService.deleteDiskImage(diskId)
		Assertions.assertThat(result).isEqualTo(true)
	}

//	@Test
//	@DisplayName("디스크 이동창")
//	fun should_setDiskMoveDomainList() {
//		val diskId = "f89493dd-51f8-44bd-9bfb-4687f43c822c"
//		val result = storageService.setDomainList(dcId, diskId)
//		result.forEach { x: IdentifiedVo? -> println(x) }
//	}

	@Test
	@DisplayName("디스크 이동")
	fun should_moveDisk() {
		val diskId = "f89493dd-51f8-44bd-9bfb-4687f43c822c"
		val domainId = "43a786b3-e37e-4545-ba1f-5e3ae0ca6f0f"
		val result = storageService.moveDisk(diskId, domainId)
		Assertions.assertThat(result).isEqualTo(true)
	}


	@Test
	@DisplayName("디스크 복사")
	fun should_copyDisk() {
		val diskId = "f89493dd-51f8-44bd-9bfb-4687f43c822c"
		val domainId = "12d17014-a612-4b6e-a512-6ec4e1aadba6"

		val storageDomainVo = IdentifiedVo.builder {
			id { domainId }
		}

		val image =
			DiskImageVo.builder {
				alias { "abcdTest2" }
				description { "test" }
				storageDomainVo { storageDomainVo }
			}

		val result = storageService.copyDisk(image)
		Assertions.assertThat(result).isEqualTo(true)
	}


	@Test
	@DisplayName("디스크 이미지 업로드")
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

		val result = storageService.uploadDisk(multipartFile, iVo)
	}


	@Test
	fun should_findDomainList() {
		log.debug("should_findDomainList ... ")
		val result = storageService.findAllStorageDomainsFromDataCenter(dataCenterId)
		result.forEach { x: StorageDomainVo? -> println(x) }
	}

	@Test
	fun should_setHostList() {
		log.debug("should_setHostList ... ")
		val result = storageService.setHostList(dataCenterId)
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
	fun should_findNetworkVoList() {
		log.debug("should_findNetworkVoList ... ")
		val result = storageService.findAllNetworksFromDataCenter(dataCenterId)
		result.forEach { x: NetworkVo? -> println(x) }
	}

	@Test
	@DisplayName("데이터센터 - 클러스터 목록")
	fun should_findClusters() {
		log.debug("should_findClusters ... ")
		val result = storageService.findAllClustersFromDataCenter(dataCenterId)
		result.forEach {  println(it) }
	}

	@Test
	@DisplayName("권한")
	fun should_findPermission() {
		log.debug("should_findPermission ... ")
		
	}

	@Test
	@DisplayName("이벤트")
	fun findEvent() {
		log.debug("should_findEvent ... ")
	
	}
	
	companion object {
		private val log by LoggerDelegate()
	}
}