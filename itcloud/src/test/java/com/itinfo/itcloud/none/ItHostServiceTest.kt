package com.itinfo.itcloud.none

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.service.computing.ItHostService
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItHostServiceTest]
 * [ItHostService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
@SpringBootTest
class ItHostServiceTest {
	@Autowired
	private lateinit var service: ItHostService

	/**
	 * [should_getName]
	 * [ItHostService.getName]에 대한 단위테스트
	 * 
	 * @see [ItHostService.getName]
	 */
	@Test
	fun should_getName() {
		log.debug("should_getName ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getList]
	 * [ItHostService.getList]에 대한 단위테스트
	 * 
	 * @see [ItHostService.getList]
	 */
	@Test
	fun should_getList() {
		log.debug("should_getList ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getInfo]
	 * [ItHostService.getInfo]에 대한 단위테스트
	 * 
	 * @see [ItHostService.getInfo]
	 */
	@Test
	fun should_getInfo() {
		log.debug("should_getInfo ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getVm]
	 * [ItHostService.getVm]에 대한 단위테스트
	 * 
	 * @see [ItHostService.getVm]
	 */
	@Test
	fun should_getVm() {
		log.debug("should_getVm ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getNic]
	 * [ItHostService.getNic]에 대한 단위테스트
	 * 
	 * @see [ItHostService.getNic]
	 */
	@Test
	fun should_getNic() {
		log.debug("should_getNic ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getHostDevice]
	 * [ItHostService.getHostDevice]에 대한 단위테스트
	 * 
	 * @see [ItHostService.getHostDevice]
	 */
	@Test
	fun should_getHostDevice() {
		log.debug("should_getHostDevice ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getPermission]
	 * [ItHostService.getPermission]에 대한 단위테스트
	 * 
	 * @see [ItHostService.getPermission]
	 */
	@Test
	fun should_getPermission() {
		log.debug("should_getPermission ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getAffinitylabels]
	 * [ItHostService.getAffinitylabels]에 대한 단위테스트
	 * 
	 * @see [ItHostService.getAffinitylabels]
	 */
	@Test
	fun should_getAffinitylabels() {
		log.debug("should_getAffinitylabels ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getEvent]
	 * [ItHostService.getEvent]에 대한 단위테스트
	 * 
	 * @see [ItHostService.getEvent]
	 */
	@Test
	fun should_getEvent() {
		log.debug("should_getEvent ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getClusterList]
	 * [ItHostService.getClusterList]에 대한 단위테스트
	 * 
	 * @see [ItHostService.getClusterList]
	 */
	@Test
	fun should_getClusterList() {
		log.debug("should_getClusterList ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_getHostCreate]
	 * [ItHostService.getHostCreate]에 대한 단위테스트
	 * 
	 * @see [ItHostService.getHostCreate]
	 */
	@Test
	fun should_getHostCreate() {
		log.debug("should_getHostCreate ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_addHost]
	 * [ItHostService.add]에 대한 단위테스트
	 * 
	 * @see [ItHostService.add]
	 */
	@Test
	fun should_addHost() {
		log.debug("should_addHost ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_editHost]
	 * [ItHostService.update]에 대한 단위테스트
	 * 
	 * @see [ItHostService.update]
	 */
	@Test
	fun should_editHost() {
		log.debug("should_editHost ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_rebootHost]
	 * [ItHostService.rebootHost]에 대한 단위테스트
	 * 
	 * @see [ItHostService.rebootHost]
	 */
	@Test
	fun should_rebootHost() {
		log.debug("should_rebootHost ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_deleteHost]
	 * [ItHostService.remove]에 대한 단위테스트
	 * 
	 * @see [ItHostService.remove]
	 */
	@Test
	fun should_deleteHost() {
		log.debug("should_deleteHost ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_deActive]
	 * [ItHostService.deActive]에 대한 단위테스트
	 * 
	 * @see [ItHostService.deActive]
	 */
	@Test
	fun should_deActive() {
		log.debug("should_deActive ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_active]
	 * [ItHostService.active]에 대한 단위테스트
	 * 
	 * @see [ItHostService.active]
	 */
	@Test
	fun should_active() {
		log.debug("should_active ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_refresh]
	 * [ItHostService.refresh]에 대한 단위테스트
	 * 
	 * @see [ItHostService.refresh]
	 */
	@Test
	fun should_refresh() {
		log.debug("should_refresh ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_reStart]
	 * [ItHostService.reStart]에 대한 단위테스트
	 * 
	 * @see [ItHostService.reStart]
	 */
	@Test
	fun should_reStart() {
		log.debug("should_reStart ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_start]
	 * [ItHostService.start]에 대한 단위테스트
	 * 
	 * @see [ItHostService.start]
	 */
	@Test
	fun should_start() {
		log.debug("should_start ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}

	/**
	 * [should_stop]
	 * [ItHostService.stop]에 대한 단위테스트
	 * 
	 * @see [ItHostService.stop]
	 */
	@Test
	fun should_stop	() {
		log.debug("should_stop ...")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}


	companion object {
		private val log by LoggerDelegate()
	}
}