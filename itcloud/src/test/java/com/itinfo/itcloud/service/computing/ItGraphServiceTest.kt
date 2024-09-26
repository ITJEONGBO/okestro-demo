package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.DashBoardVo
import com.itinfo.itcloud.repository.dto.HostUsageDto
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ItGraphServiceTest {
    @Autowired private lateinit var service: ItGraphService

    /**
     * [should_getDashboard]
     * [ItGraphService.getDashboard]에 대한 단위테스트
     *
     * @see ItGraphService.getDashboard
     **/
    @Test
    fun should_getDashboard() {
        log.debug("should_getDashboard ... ")
        val result: DashBoardVo =
            service.getDashboard()

        assertThat(result, `is`(not(nullValue())))
        assertThat(result.events, `is`(7))
        assertThat(result.eventsAlert, `is`(6))
        assertThat(result.eventsError, `is`(1))
    }

    /**
     * [should_totalCpuMemory]
     * [ItGraphService.totalCpuMemory]에 대한 단위테스트
     *
     * @see ItGraphService.totalCpuMemory
     **/
    @Test
    fun should_totalCpuMemory() {
        log.debug("should_totalCpuMemory ... ")
        val result: HostUsageDto =
            service.totalCpuMemory()

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }


    companion object{
        private val log by LoggerDelegate()
    }

}