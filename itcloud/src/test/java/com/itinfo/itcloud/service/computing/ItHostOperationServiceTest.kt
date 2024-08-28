package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItHostOperationServiceTest]
 * [ItHostOperationService]에 대한 단위 테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.08.28
 */
@SpringBootTest
class ItHostOperationServiceTest {
    @Autowired private lateinit var service: ItHostOperationService

    private lateinit var hostId: String // host01.ititinfo.local

    @BeforeEach
    fun setup() {
        hostId = "c36563e3-83eb-49c7-91c0-fc4b197387b2"
    }


    /**
     * [should_deactivate]
     * [ItHostOperationService.deactivate]에 대한 단위테스트
     * 유지보수
     *
     * @see [ItHostOperationService.deactivate]
     */
    @Test
    fun should_deactivate() {
        log.debug("should_deactivate ...")
        val result: Boolean =
            service.deactivate(hostId)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }

    /**
     * [should_activate]
     * [ItHostOperationService.activate]에 대한 단위테스트
     * 활성
     *
     * @see [ItHostOperationService.activate]
     */
    @Test
    fun should_activate() {
        log.debug("should_activate ...")
        val result: Boolean =
            service.activate(hostId)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }

    /**
     * [should_refresh]
     * [ItHostOperationService.refresh]에 대한 단위테스트
     * 기능 새로고침
     * @see [ItHostOperationService.refresh]
     */
    @Test
    fun should_refresh() {
        log.debug("should_refresh ...")
        val result: Boolean =
            service.refresh(hostId)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }

    /**
     * [should_restart]
     * [ItHostOperationService.restart]에 대한 단위테스트
     * 재시작
     * 실행되지만 유저명과 암호 입력 문제 있음
     * TODO
     *
     * @see [ItHostOperationService.restart]
     */
    @Test
    fun should_restart() {
        log.debug("should_restart ...")
        val result: Boolean =
            service.restart(hostId)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }

    companion object {
        private val log by LoggerDelegate()
    }
}