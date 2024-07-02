package com.itinfo.itcloud.none

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.create.DataCenterCreateVo
import com.itinfo.itcloud.model.error.CommonVo
import com.itinfo.itcloud.service.computing.ItDataCenterService
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
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
	@Autowired
	private lateinit var service: ItDataCenterService

	/**
	 * [should_getName]
	 * [ItDataCenterService.getName]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.getName
	 **/
	@Test
	fun should_getName() {
		log.debug("should_getName ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getList]
	 * [ItDataCenterService.getList]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.getList
	 **/
	@Test
	fun should_getList() {
		log.debug("should_getList ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getEvent]
	 * [ItDataCenterService.getEvent]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.getEvent
	 **/
	@Test
	fun should_getEvent() {
		log.debug("should_getEvent ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getDatacenter]
	 * [ItDataCenterService.getDatacenter]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.getDatacenter
	 **/
	@Test
	fun should_getDatacenter() {
		log.debug("should_getDatacenter ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_add_and_delete_datacenter]
	 * [ItDataCenterService.addDatacenter], [ItDataCenterService.deleteDatacenter]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.addDatacenter
	 * @see ItDataCenterService.deleteDatacenter
	 **/
	@Test
	fun should_add_and_delete_datacenter() {
		log.debug("should_add_and_delete_datacenter ... ")
		assertThat(service, `is`(not(nullValue())))

		val dc2Add: DataCenterCreateVo = DataCenterCreateVo.builder()
			.name("test")
			.description("testDescription")
			.storageType(false)
			.version("4.2")
			.quotaMode(QuotaModeType.DISABLED)
			.comment("testComment")
			.build()

		val result: CommonVo<Boolean> =
			service.addDatacenter(dc2Add)
		// Boolean 값으로는 검증이 어려워 보임
		// 사유: dataCenterId

		assertThat(result, `is`(notNullValue()))
		assertThat(result.body, `is`(notNullValue()))
		assertThat(result.body.content, `is`(notNullValue()))
		assertThat(result.body.content, `is`(true))

		val dcAdded: DataCenterVo? =
			service.getList().firstOrNull { it.name == dc2Add.name }

		assertThat(dcAdded, `is`(notNullValue()))
		assertThat(dcAdded?.name, `is`(dc2Add.name))
		assertThat(dcAdded?.description, `is`(dc2Add.description))
		assertThat(dcAdded?.isStorageType, `is`(dc2Add.isStorageType))
		assertThat(dcAdded?.version, `is`(dc2Add.version))
		// assertThat(dcAdded?.quotaMode, `is`(dc2Add.quotaMode))
		// TODO: 메시지 출력임으로 extension함수 생성 필요

		val deleteRes: CommonVo<Boolean> =
			service.deleteDatacenter(dcAdded?.id)
		assertThat(deleteRes, `is`(notNullValue()))
		assertThat(deleteRes.body, `is`(notNullValue()))
		assertThat(deleteRes.body.content, `is`(notNullValue()))
		assertThat(deleteRes.body.content, `is`(true))

	}

	/**
	 * [should_editDatacenter]
	 * [ItDataCenterService.editDatacenter]에 대한 단위테스트
	 * 
	 * @see ItDataCenterService.editDatacenter
	 **/
	@Test
	fun should_editDatacenter() {
		log.debug("should_editDatacenter ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}


	companion object {
		private val log by LoggerDelegate()
	}
}