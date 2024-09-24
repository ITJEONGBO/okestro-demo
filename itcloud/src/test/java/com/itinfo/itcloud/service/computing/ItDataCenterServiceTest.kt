package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.storage.StorageDomainVo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ovirt.engine.sdk4.types.QuotaModeType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItDataCenterServiceTest]
 * [ItDataCenterService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.09.24
 */
@SpringBootTest
class ItDataCenterServiceTest {
	@Autowired private lateinit var service: ItDataCenterService

	private lateinit var dataCenterId: String

	@BeforeEach
	fun setup() {
		dataCenterId = "6cde7270-6459-11ef-8be2-00163e5d0646"
	}

	/**
	 * [should_findAll]
	 * [ItDataCenterService.findAll]에 대한 단위테스트
	 *
	 * @see ItDataCenterService.findAll
	 **/
	@Test
	fun should_findAll() {
		log.debug("should_findAll ... ")
		val result: List<DataCenterVo> =
			service.findAll()

		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(2)) // 데이터센터 목록의 개수가 2인지 확인
		result.forEach { println(it) }
	}

	/**
	 * [should_findOne]
	 * [ItDataCenterService.findOne]에 대한 단위테스트
	 *
	 * @see ItDataCenterService.findOne
	 **/
	@Test
	fun should_findOne() {
		log.debug("should_findOne ... ")
		val result: DataCenterVo? =
			service.findOne(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		assertThat(result?.name, `is`("Default"))
	}

	/**
	 * [should_add_update_and_delete_datacenter]
	 * [ItDataCenterService.add], [ItDataCenterService.update]] [ItDataCenterService.remove]에 대한 단위테스트
	 *
	 * @see ItDataCenterService.add
	 * @see ItDataCenterService.update
	 * @see ItDataCenterService.remove
	 **/
	@Test
	fun should_add_update_and_delete_datacenter() {
		log.debug("should_add_update_and_delete_datacenter ... ")
		assertThat(service, `is`(not(nullValue())))

		// 생성
		val dcAdd: DataCenterVo = DataCenterVo.builder {
			name { "test" }
			description { "testDescription" }
			storageType { false }
			version { "4.7" }
			quotaMode { QuotaModeType.DISABLED }
			comment { "testComment" }
		}

		val addResult: DataCenterVo? =
			service.add(dcAdd)

		assertThat(addResult, `is`(notNullValue()))
		assertThat(addResult?.id, `is`(notNullValue())) // 여기서 검증이 애매
		assertThat(addResult?.name, `is`(dcAdd.name))
		assertThat(addResult?.description, `is`(dcAdd.description))
		assertThat(addResult?.storageType, `is`(dcAdd.storageType))
		assertThat(addResult?.version, `is`(dcAdd.version))
		assertThat(addResult?.quotaMode, `is`(dcAdd.quotaMode))
		assertThat(addResult?.comment, `is`(dcAdd.comment))

		// 편집
		val dcUpdate: DataCenterVo = DataCenterVo.builder {
			id { addResult?.id }
			name { "test2" }
			description { "editDescriptionTest2" }
			storageType { false }
			version { "4.7" }
			quotaMode { QuotaModeType.DISABLED }
			comment { "editCommentTest2" }
		}

		val updateResult: DataCenterVo? =
			service.update(dcUpdate)

		assertThat(updateResult, `is`(notNullValue()))
		assertThat(updateResult?.id, `is`(addResult?.id))
		assertThat(updateResult?.name, `is`(dcUpdate.name))
		assertThat(updateResult?.description, `is`(dcUpdate.description))
		assertThat(updateResult?.storageType, `is`(dcUpdate.storageType))
		assertThat(updateResult?.version, `is`(dcUpdate.version))
		assertThat(updateResult?.quotaMode, `is`(dcUpdate.quotaMode))
		assertThat(updateResult?.comment, `is`(dcUpdate.comment))

		// 편집
		// TODO: 메시지 출력임으로 extension함수 생성 필요
		val removeResult: Boolean? =
			addResult?.let { service.remove(it.id) }

		assertThat(removeResult, `is`(notNullValue()))
	}

	/**
	 * [should_add_failure_datacenter]
	 * [ItDataCenterService.add]에 대한 단위테스트
	 *
	 * 실패: 데이터센터 이름 중복으로 생성 불가
	 * @see ItDataCenterService.add
	 **/
//	@Test
//	fun should_add_failure_datacenter() {
//		log.debug("should_add_failure_datacenter ... ")
//
//		val dcAdd: DataCenterVo = DataCenterVo.builder {
//			name { "Default" } // 기본 생성되는 데이터센터 명 (무조건 중복)
//			description { "testDescription" }
//			storageType { false }
//			version { "4.7" }
//			quotaMode { QuotaModeType.DISABLED }
//			comment { "testComment" }
//		}
//
//		val addResult: DataCenterVo? =
//			service.add(dcAdd)
//
//		assertThat(addResult, `is`(nullValue()))
//		// id가 null 인경우, 여기서 검증이 애매
//		assertThat(addResult?.id, `is`(nullValue()))
//	}


	/**
	 * [should_findAllClustersFromDataCenter]
	 * [ItDataCenterService.findAllClustersFromDataCenter]에 대한 단위테스트
	 *
	 * @see ItDataCenterService.findAllClustersFromDataCenter
	 **/
	@Test
	fun should_findAllClustersFromDataCenter() {
		log.debug("should_findAllClustersFromDataCenter ... ")
		val result: List<ClusterVo> =
			service.findAllClustersFromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(2))
	}

	/**
	 * [should_findAllHostsFromDataCenter]
	 * [ItDataCenterService.findAllHostsFromDataCenter]에 대한 단위테스트
	 *
	 * @see ItDataCenterService.findAllHostsFromDataCenter
	 **/
	@Test
	fun should_findAllHostsFromDataCenter() {
		log.debug("should_findAllHostsFromDataCenter ... ")
		val result: List<HostVo> =
			service.findAllHostsFromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(2))
	}

	/**
	 * [should_findAllVmsFromDataCenter]
	 * [ItDataCenterService.findAllVmsFromDataCenter]에 대한 단위테스트
	 *
	 * @see ItDataCenterService.findAllVmsFromDataCenter
	 **/
	@Test
	fun should_findAllVmsFromDataCenter() {
		log.debug("should_findAllVmsFromDataCenter ... ")
		val result: List<VmVo> =
			service.findAllVmsFromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(9))
	}

	/**
	 * [should_findAllNetworksFromDataCenter]
	 * [ItDataCenterService.findAllNetworksFromDataCenter]에 대한 단위테스트
	 *
	 * @see ItDataCenterService.findAllNetworksFromDataCenter
	 **/
	@Test
	fun should_findAllNetworksFromDataCenter() {
		log.debug("should_findAllNetworkFromDataCenter ... ")
		val result: List<NetworkVo> =
			service.findAllNetworksFromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(4))
	}

	/**
	 * [should_findAllStorageDomainsFromDataCenter]
	 * [ItDataCenterService.findAllStorageDomainsFromDataCenter]에 대한 단위테스트
	 *
	 * @see ItDataCenterService.findAllStorageDomainsFromDataCenter
	 **/
	@Test
	fun should_findAllStorageDomainsFromDataCenter() {
		log.debug("should_findAllStorageDomainsFromDataCenter ... ")
		val result: List<StorageDomainVo> =
			service.findAllStorageDomainsFromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(1))
	}

	/**
	 * [should_findAllPermissionsFromDataCenter]
	 * [ItDataCenterService.findAllPermissionsFromDataCenter]에 대한 단위테스트
	 *
	 * @see ItDataCenterService.findAllPermissionsFromDataCenter
	 **/
	@Test
	fun should_findAllPermissionsFromDataCenter() {
		log.debug("should_findAllPermissionsFromDataCenter ... ")
		val result: List<PermissionVo> =
			service.findAllPermissionsFromDataCenter(dataCenterId)

		assertThat(result, `is`(not(nullValue())))
		result.forEach { println(it) }
		assertThat(result.size, `is`(5))
	}

	/**
	 * [should_findAllEventsBy]
	 * [ItDataCenterService.findAllEventsFromDataCenter]에 대한 단위테스트
	 *
	 * @see ItDataCenterService.findAllEventsFromDataCenter
	 **/
	@Test
	fun should_findAllEventsBy() {
		log.debug("should_findAllEventsBy ... ")
		val result: List<EventVo> =
			service.findAllEventsFromDataCenter(dataCenterId)
		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(500))
	}

	companion object {
		private val log by LoggerDelegate()
	}
}