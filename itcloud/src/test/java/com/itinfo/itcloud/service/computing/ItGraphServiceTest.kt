package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.DashBoardVo
import com.itinfo.itcloud.model.computing.RutilVo
import com.itinfo.itcloud.repository.history.dto.HostUsageDto
import com.itinfo.itcloud.repository.history.dto.StorageUsageDto
import com.itinfo.itcloud.repository.history.dto.UsageDto
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

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
        println(result)
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

    /**
     * [should_totalStorage]
     * [ItGraphService.totalStorage]에 대한 단위테스트
     *
     * @see ItGraphService.totalStorage
     **/
    @Test
    fun should_totalStorage() {
        log.debug("should_totalStorage ... ")
        val result: StorageUsageDto =
            service.totalStorage()

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }

    /**
     * [should_totalCpuMemoryList]
     * [ItGraphService.totalCpuMemoryList]에 대한 단위테스트
     *
     * @see ItGraphService.totalCpuMemoryList
     **/
    @Test
    fun should_totalCpuMemoryList() {
        log.debug("should_totalCpuMemoryList ... ")
        val result: List<HostUsageDto> =
            service.totalCpuMemoryList(
                UUID.fromString("1d3a2fdb-0873-4837-8eaa-28cca20ffb12"),
                5
            )

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }

    /**
     * [should_vmCpuChart]
     * [ItGraphService.vmCpuChart]에 대한 단위테스트
     *
     * @see ItGraphService.vmCpuChart
     **/
    @Test
    fun should_vmCpuChart() {
        log.debug("should_vmCpuChart ... ")
        val result: List<UsageDto> =
            service.vmCpuChart()

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }

    /**
     * [should_vmMemoryChart]
     * [ItGraphService.vmMemoryChart]에 대한 단위테스트
     *
     * @see ItGraphService.vmMemoryChart
     **/
    @Test
    fun should_vmMemoryChart() {
        log.debug("should_vmMemoryChart ... ")
        val result: List<UsageDto> =
            service.vmMemoryChart()

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }

    /**
     * [should_storageChart]
     * [ItGraphService.storageChart]에 대한 단위테스트
     *
     * @see ItGraphService.storageChart
     **/
    @Test
    fun should_storageChart() {
        log.debug("should_storageChart... ")
        val result: List<UsageDto> =
            service.storageChart()

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }

    /**
     * [should_hostCpuChart]
     * [ItGraphService.hostCpuChart]에 대한 단위테스트
     *
     * @see ItGraphService.hostCpuChart
     **/
    @Test
    fun should_hostCpuChart() {
        log.debug("should_hostCpuChart... ")
        val result: List<UsageDto> =
            service.hostCpuChart()

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }

    /**
     * [should_hostMemoryChart]
     * [ItGraphService.hostMemoryChart]에 대한 단위테스트
     *
     * @see ItGraphService.hostMemoryChart
     **/
    @Test
    fun should_hostMemoryChart() {
        log.debug("should_hostMemoryChart... ")
        val result: List<UsageDto> =
            service.hostMemoryChart()

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }

    /**
     * [should_hostPercent]
     * [ItGraphService.hostPercent]에 대한 단위테스트
     *
     * @see ItGraphService.hostPercent
     **/
    @Test
    fun should_hostPercent() {
        log.debug("should_hostPercent... ")
        val hostId = "1d3a2fdb-0873-4837-8eaa-28cca20ffb12"
        val hostNicId = "e28eb0dd-933a-4752-9b40-7482071c48f0"
        val result: UsageDto =
            service.hostPercent(hostId,hostNicId)

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }

    /**
     * [should_vmPercent]
     * [ItGraphService.vmPercent]에 대한 단위테스트
     *
     * @see ItGraphService.vmPercent
     **/
    @Test
    fun should_vmPercent() {
        log.debug("should_vmPercent ... ")
        val vmId = "9181fa0b-d031-4dbd-a031-6de2e2913eb6"
        val vmNicId = "c4ade3a7-347f-47f3-8d76-e7a52adece96"

        val result: UsageDto =
            service.vmPercent(vmId, vmNicId)

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }

    @Test
    fun should_rutilInfo() {
        log.debug("should_rutilInfo ... ")
        val result: RutilVo =
            service.rutilInfo()

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }


    companion object{
        private val log by LoggerDelegate()
    }

}