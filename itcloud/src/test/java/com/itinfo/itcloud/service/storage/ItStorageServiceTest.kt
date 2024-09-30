package com.itinfo.itcloud.service.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.computing.EventVo
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
	private lateinit var clusterId: String // Default
	private lateinit var networkId: String // ovirtmgmt(dc: Default)
	private lateinit var host01: String // host01
	private lateinit var domainId: String // hosted=engin
	private lateinit var nfs: String // hosted=engin

	@BeforeEach
	fun setup() {
		dataCenterId = "023b0a26-3819-11ef-8d02-00163e6c8feb"
		clusterId = "023c79d8-3819-11ef-bf08-00163e6c8feb"
		networkId = "00000000-0000-0000-0000-000000000009"
		host01 = "671e18b2-964d-4cc6-9645-08690c94d249"
		domainId = "213b1a0a-b0c0-4d10-95a4-7aafed4f76b9"
		nfs = "06faa572-f1ac-4874-adcc-9d26bb74a54d"
	}


	/**
	 * [should_findAll]
	 * [ItStorageService.findAll] 의 단위테스트
	 *
	 * @see [ItStorageService.findAll]
	 */
	@Test
	fun should_findAll() {
		log.debug("should_findAllDomains ... ")
		val result: List<StorageDomainVo> =
			service.findAll()

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(3))
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
			service.findAllFromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(2))
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
		val result: StorageDomainVo? =
			service.findOne(domainId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result?.id, `is`(domainId))
		println(result)
	}

	/**
	 * [findAllConn]
	 * [ItStorageService.findAllConn] 의 단위테스트
	 *
	 * @see [ItStorageService.findAllConn]
	 */
	@Test
	fun findAllConn() {
		log.debug("findAllConn ... ")
		val result: Int = service.findAllConn()
//		print(result)

	}



	/**
	 * [should_add]
	 * [ItStorageService.add] 의 단위테스트
	 *
	 * @see [ItStorageService.add]
	 */
	@Test
	fun should_add() {
		log.debug("should_add ... ")
	}

	/**
	 * [should_import]
	 * [ItStorageService.import] 의 단위테스트
	 *
	 * @see [ItStorageService.import]
	 */
	@Test
	fun should_import() {
		log.debug("should_import ... ")
	}

	/**
	 * [should_update]
	 * [ItStorageService.update] 의 단위테스트
	 *
	 * @see [ItStorageService.update]
	 */
	@Test
	fun should_update() {
		log.debug("should_update")
	}

	/**
	 * [should_remove]
	 * [ItStorageService.remove] 의 단위테스트
	 *
	 * @see [ItStorageService.remove]
	 */
	@Test
	fun should_remove() {
		log.debug("should_remove ... ")
	}

	/**
	 * [should_destroy]
	 * [ItStorageService.destroy] 의 단위테스트
	 *
	 * @see [ItStorageService.destroy]
	 */
	@Test
	fun should_destroy() {
		log.debug("should_destroy ... ")
	}


	/**
	 * [should_findAllDataCentersFromStorageDomain]
	 * [ItStorageService.findAllDataCentersFromStorageDomain] 의 단위테스트
	 *
	 * @see [ItStorageService.findAllDataCentersFromStorageDomain]
	 */
	@Test
	fun should_findAllDataCentersFromStorageDomain() {
		log.debug("should_findAllDataCentersFromStorageDomain ... ")
		val result: List<DataCenterVo> =
			service.findAllDataCentersFromStorageDomain(domainId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
//		assertThat(result.size, `is`(1))
	}

	/**
	 * [should_findAllVmsFromStorageDomain]
	 * [ItStorageService.findAllVmsFromStorageDomain] 의 단위테스트
	 *
	 * @see [ItStorageService.findAllVmsFromStorageDomain]
	 */
	@Test
	fun should_findAllVmsFromStorageDomain() {
		log.debug("should_findAllVmsFromStorageDomain ... ")
		val result: List<VmVo> =
			service.findAllVmsFromStorageDomain(domainId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(3))
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
		result.forEach { println(it) }
		assertThat(result.size, `is`(13))
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
	}


	companion object {
		private val log by LoggerDelegate()
	}
}