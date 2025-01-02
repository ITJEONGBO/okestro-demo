package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.network.HostNicVo
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.service.computing.ItHostServiceTest.Companion
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItHostNicServiceTest]
 * [ItHostNicService]에 대한 단위 테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.12.27
 */
@SpringBootTest
class ItHostNicServiceTest {
    @Autowired private lateinit var service: ItHostNicService

    private lateinit var host01: String
    private lateinit var host04: String

    @BeforeEach
    fun setup() {
        host01 = "671e18b2-964d-4cc6-9645-08690c94d249"
        host04 = "c35ee370-b9f6-4c5b-9c65-fe2e716795b5"
    }


    /**
     * [should_findAllHostNicsFromHost]
     * [ItHostNicService.findAllFromHost]에 대한 단위테스트
     *
     * @see ItHostNicService.findAllFromHost
     */
    @Test
    fun should_findAllHostNicsFromHost() {
        log.debug("should_findAllHostNicFromHost ...")

        val result: List<HostNicVo> =
            service.findAllFromHost("8d60dce0-a79a-46f2-b332-4f15492f3afa")

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
    }


    /**
     * [should_setUpNetworksFromHost]
     * [ItHostNicService.setUpNetworksFromHost]에 대한 단위테스트
     *
     * @see ItHostNicService.setUpNetworksFromHost
     */
    @Test
    fun should_setUpNetworksFromHost() {
        log.debug("should_setUpNetworksFromHost ...")
        val hostId = "c35ee370-b9f6-4c5b-9c65-fe2e716795b5"
        val networkVo: NetworkVo =
            NetworkVo.builder {

            }

        val result: Boolean =
            service.setUpNetworksFromHost(hostId, networkVo)

        assertThat(result, `is`(not(nullValue())))
    }


    companion object {
        private val log by LoggerDelegate()
    }
}