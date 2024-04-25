package com.itinfo.itcloud.none

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.service.ItVmService
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItVmServiceTest]
 * [ItVmService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @since 2024.03.05
 */
@SpringBootTest
class ItVmServiceTest {
	@Autowired
	private lateinit var service: ItVmService
	
	/**
	 * [should_getName]
	 * [ItVmService.getName]에 대한 단위테스트
	 * 
	 * @see ItVmService.getName
	 */
	@Test
	fun should_getName() {
		log.debug("should_getName ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getList]
	 * [ItVmService.getList]에 대한 단위테스트
	 * 
	 * @see ItVmService.getList
	 */
	@Test
	fun should_getList() {
		log.debug("should_getList ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getInfo]
	 * [ItVmService.getInfo]에 대한 단위테스트
	 * 
	 * @see ItVmService.getInfo
	 */
	@Test
	fun should_getInfo() {
		log.debug("should_getInfo ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getNic]
	 * [ItVmService.getNic]에 대한 단위테스트
	 * 
	 * @see ItVmService.getNic
	 */
	@Test
	fun should_getNic() {
		log.debug("should_getNic ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getDisk]
	 * [ItVmService.getDisk]에 대한 단위테스트
	 * 
	 * @see ItVmService.getDisk
	 */
	@Test
	fun should_getDisk() {
		log.debug("should_getDisk ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getSnapshot]
	 * [ItVmService.getSnapshot]에 대한 단위테스트
	 * 
	 * @see ItVmService.getSnapshot
	 */
	@Test
	fun should_getSnapshot() {
		log.debug("should_getSnapshot ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getApplication]
	 * [ItVmService.getApplication]에 대한 단위테스트
	 * 
	 * @see ItVmService.getApplication
	 */
	@Test
	fun should_getApplication() {
		log.debug("should_getApplication ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getAffinitygroup]
	 * [ItVmService.getAffinitygroup]에 대한 단위테스트
	 * 
	 * @see ItVmService.getAffinitygroup
	 */
	@Test
	fun should_getAffinitygroup() {
		log.debug("should_getAffinitygroup ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getAffinitylabel]
	 * [ItVmService.getAffinitylabel]에 대한 단위테스트
	 * 
	 * @see ItVmService.getAffinitylabel
	 */
	@Test
	fun should_getAffinitylabel() {
		log.debug("should_getAffinitylabel ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getGuestInfo]
	 * [ItVmService.getGuestInfo]에 대한 단위테스트
	 * 
	 * @see ItVmService.getGuestInfo
	 */
	@Test
	fun should_getGuestInfo() {
		log.debug("should_getGuestInfo ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getPermission]
	 * [ItVmService.getPermission]에 대한 단위테스트
	 * 
	 * @see ItVmService.getPermission
	 */
	@Test
	fun should_getPermission() {
		log.debug("should_getPermission ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getEvent]
	 * [ItVmService.getEvent]에 대한 단위테스트
	 * 
	 * @see ItVmService.getEvent
	 */
	@Test
	fun should_getEvent() {
		log.debug("should_getEvent ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getClusterList]
	 * [ItVmService.getClusterList]에 대한 단위테스트
	 * 
	 * @see ItVmService.getClusterList
	 */
	@Test
	fun should_getClusterList() {
		log.debug("should_getClusterList ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_getVmCreate]
	 * [ItVmService.getVmCreate]에 대한 단위테스트
	 * 
	 * @see ItVmService.getVmCreate
	 */
	@Test
	fun should_getVmCreate() {
		log.debug("should_getVmCreate ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_addVm]
	 * [ItVmService.addVm]에 대한 단위테스트
	 * 
	 * @see ItVmService.addVm
	 */
	@Test
	fun should_addVm() {
		log.debug("should_addVm ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_editVm]
	 * [ItVmService.editVm]에 대한 단위테스트
	 * 
	 * @see ItVmService.editVm
	 */
	@Test
	fun should_editVm() {
		log.debug("should_editVm ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	
	/**
	 * [should_deleteVm]
	 * [ItVmService.deleteVm]에 대한 단위테스트
	 * 
	 * @see ItVmService.deleteVm
	 */
	@Test
	fun should_deleteVm() {
		log.debug("should_deleteVm ... ")
		assertThat(service, `is`(not(nullValue())))
		// TODO: 메소드의 결과값에 대한 검증처리
	}
	companion object {
		private val log by LoggerDelegate()
	}
}