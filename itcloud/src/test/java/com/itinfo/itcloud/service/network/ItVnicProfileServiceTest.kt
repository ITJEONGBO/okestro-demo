package com.itinfo.itcloud.service.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.network.VnicProfileVo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItVnicProfileServiceTest]
 * [ItVnicProfileService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.09.26
 */
@SpringBootTest
class ItVnicProfileServiceTest {
    @Autowired private lateinit var service: ItVnicProfileService

    private lateinit var dataCenterId: String
    private lateinit var clusterId: String // Default
    private lateinit var networkId: String // ovirtmgmt(dc: Default)
    private lateinit var host01: String // host01
    private lateinit var host02: String // host02.ititinfo.local
    private lateinit var hostVm: String // hostVm

    @BeforeEach
    fun setup() {
        dataCenterId = "023b0a26-3819-11ef-8d02-00163e6c8feb"
        clusterId = "023c79d8-3819-11ef-bf08-00163e6c8feb"
        networkId = "00000000-0000-0000-0000-000000000009"
        host01 = "671e18b2-964d-4cc6-9645-08690c94d249"
        host02 = "0d7ba24e-452f-47fe-a006-f4702aa9b37f"
        hostVm = "c2ae1da5-ce4f-46df-b337-7c471bea1d8d"
    }

    /**
     * [should_findAllVnicProfilesFromNetwork]
     * [ItVnicProfileService.findAllVnicProfilesFromNetwork]에 대한 단위테스트
     *
     * @see ItVnicProfileService.findAllVnicProfilesFromNetwork
     */
    @Test
    fun should_findAllVnicProfilesFromNetwork() {
        log.debug("findAllVnicProfilesFromNetwork ... ")
        val result: List<VnicProfileVo> =
            service.findAllVnicProfilesFromNetwork(networkId)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result.size, `is`(2))
        result.forEach { println(it) }
    }


    companion object {
        private val log by LoggerDelegate()
    }

}