package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.computing.EventVo
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
 * @since 2024.03.05
 */
@SpringBootTest
class ItDataCenterServiceTest {
	@Autowired private lateinit var service: ItDataCenterService

	private lateinit var dataCenterId: String

	@BeforeEach
	fun setup() {
		dataCenterId = "6cde7270-6459-11ef-8be2-00163e5d06468"
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
//		assertThat(result.size, `is`(1)) // 데이터센터 목록의 개수가 2인지 확인
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

		// 수정
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
	 * [should_add_success_datacenter]
	 * [ItDataCenterService.add]에 대한 단위테스트
	 *
	 * 성공
	 * @see ItDataCenterService.add
	 */
	@Test
	fun should_add_success_datacenter(){
		log.debug("should_add_success_datacenter ... ")

		val dcAdd: DataCenterVo = DataCenterVo.builder {
			name { "testDc1" }
			description { "testDescriptionDc" }
			storageType { false }
			version { "4.7" }
			quotaMode { QuotaModeType.DISABLED }
			comment { "testCommentDc" }
		}

		val addResult: DataCenterVo? =
			service.add(dcAdd)

		assertThat(addResult, `is`(not(nullValue())))
		assertThat(addResult?.id, `is`(not(nullValue())))
		assertThat(addResult?.id, `is`(dcAdd.id))
		assertThat(addResult?.name, `is`(dcAdd.name))
		assertThat(addResult?.description, `is`(dcAdd.description))
		assertThat(addResult?.storageType, `is`(dcAdd.storageType))
		assertThat(addResult?.version, `is`(dcAdd.version))
		assertThat(addResult?.quotaMode, `is`(dcAdd.quotaMode))
		assertThat(addResult?.comment, `is`(dcAdd.comment))
	}

	/**
	 * [should_add_failure_datacenter]
	 * [ItDataCenterService.add]에 대한 단위테스트
	 *
	 * 실패: 데이터센터 이름 중복으로 생성 불가
	 * @see ItDataCenterService.add
	 **/
	@Test
	fun should_add_failure_datacenter() {
		log.debug("should_add_failure_datacenter ... ")

		val dcAdd: DataCenterVo = DataCenterVo.builder {
			name { "Default" } // 기본 생성되는 데이터센터 명 (무조건 중복)
			description { "testDescription" }
			storageType { false }
			version { "4.7" }
			quotaMode { QuotaModeType.DISABLED }
			comment { "testComment" }
		}

		val addResult: DataCenterVo? =
			service.add(dcAdd)

		assertThat(addResult, `is`(nullValue()))
		// id가 null 인경우, 여기서 검증이 애매
		assertThat(addResult?.id, `is`(nullValue()))
	}

	/**
	 * [should_update_success_datacenter]
	 * [ItDataCenterService.update]에 대한 단위테스트
	 *
	 * 성공
	 * 미완
	 * @see ItDataCenterService.update
	 */
	@Test
	fun should_update_success_datacenter(){
		log.debug("should_update_success_datacenter ... ")

		val dcEdit: DataCenterVo = DataCenterVo.builder {
//			id { }
			name { "testDc2" }
			description { "testDescriptionDc2" }
			storageType { false }
			version { "4.7" }
			quotaMode { QuotaModeType.DISABLED }
			comment { "testCommentDc2" }
		}

		val updateResult: DataCenterVo? =
			service.update(dcEdit)

		assertThat(updateResult, `is`(not(nullValue())))
		assertThat(updateResult?.id, `is`(not(nullValue())))
		assertThat(updateResult?.name, `is`(dcEdit.name))
		assertThat(updateResult?.description, `is`(dcEdit.description))
		assertThat(updateResult?.storageType, `is`(dcEdit.storageType))
		assertThat(updateResult?.version, `is`(dcEdit.version))
		assertThat(updateResult?.quotaMode, `is`(dcEdit.quotaMode))
		assertThat(updateResult?.comment, `is`(dcEdit.comment))
	}

/**
	 * [should_remove_success_datacenter]
	 * [ItDataCenterService.remove]에 대한 단위테스트
	 *
	 * 성공
	 * 미완
	 * @see ItDataCenterService.remove
	 */
	@Test
	fun should_remove_success_datacenter(){
		log.debug("should_remove_success_datacenter ... ")

		val dataCenterId = ""
		val removeResult: Boolean =
			service.remove(dataCenterId)

		assertThat(removeResult, `is`(not(nullValue())))
		assertThat(removeResult, `is`(true))
	}



	/**
	 * [should_findAllEventsBy]
	 * [ItDataCenterService.findAllEventFromDataCenter]에 대한 단위테스트
	 *
	 * @see ItDataCenterService.findAllEventFromDataCenter
	 **/
	@Test
	fun should_findAllEventsBy() {
		log.debug("should_findAllEventsBy ... ")
		val result: List<EventVo> =
			service.findAllEventFromDataCenter(dataCenterId)
		assertThat(result, `is`(not(nullValue())))
		assertThat(result.size, `is`(110))
	}

	companion object {
		private val log by LoggerDelegate()
	}
}