package com.itinfo.itcloud.service.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.network.NetworkVo
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
    private lateinit var vnic: String // hostVm

    @BeforeEach
    fun setup() {
        dataCenterId = "023b0a26-3819-11ef-8d02-00163e6c8feb"
        clusterId = "023c79d8-3819-11ef-bf08-00163e6c8feb"
        networkId = "00000000-0000-0000-0000-000000000009"
        host01 = "671e18b2-964d-4cc6-9645-08690c94d249"
        host02 = "0d7ba24e-452f-47fe-a006-f4702aa9b37f"
        hostVm = "c2ae1da5-ce4f-46df-b337-7c471bea1d8d"
        vnic = "0000000a-000a-000a-000a-000000000398"
    }

    /**
     * [should_findAll]
     * [ItVnicProfileService.findAll]에 대한 단위테스트
     *
     * @see ItVnicProfileService.findAll
     */
    @Test
    fun should_findAll() {
        log.debug("findAll ... ")
        val result: List<VnicProfileVo> =
            service.findAll()

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
        assertThat(result.size, `is`(9))
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
        result.forEach { println(it) }
        assertThat(result.size, `is`(1))
    }

    /**
     * [should_findVnicProfile]
     * [ItVnicProfileService.findOne]에 대한 단위테스트
     *
     * @see ItVnicProfileService.findOne
     */
    @Test
    fun should_findOne() {
        log.debug("should_findVnicProfile ... ")
        val result: VnicProfileVo? =
            service.findVnicProfile(vnic)

        assertThat(result, `is`(not(nullValue())))
        println(result)
        assertThat(result?.name, `is`("ovirtmgmt"))
    }

    /**
     * [should_add_update_and_remove_network]
     * [ItVnicProfileService.add]에 대한 단위테스트
     *
     * @see ItVnicProfileService.addVnicProfile
     * @see ItVnicProfileService.updateVnicProfile
     * @see ItVnicProfileService.removeVnicProfile
     */
    @Test
    fun should_add_update_and_remove_network() {
        log.debug("add ... ")
        val addVnic: VnicProfileVo = VnicProfileVo.builder {
            name { "xvx" }
            networkVo { IdentifiedVo.builder { id { networkId } } }
            description { "" }
            migration { true }
        }

        val addResult: VnicProfileVo? =
            service.addVnicProfile(addVnic)

        assertThat(addResult, `is`(not(nullValue())))
        assertThat(addResult?.name, `is`(addVnic.name))
        assertThat(addResult?.description, `is`(addVnic.description))

        log.debug("update... ")
        val updateVnic: VnicProfileVo = VnicProfileVo.builder {
            id { addResult?.id }
            name { "xxx" }
            networkVo { IdentifiedVo.builder { id { networkId } } }
            description { "" }
            migration { true }
        }

        val updateResult: VnicProfileVo? =
            service.updateVnicProfile(updateVnic)

        assertThat(updateResult, `is`(not(nullValue())))
        assertThat(updateResult?.name, `is`(updateVnic.name))
        assertThat(updateResult?.description, `is`(addVnic.description))

        log.debug("remove... ")
        val removeResult: Boolean =
            updateResult?.let { service.removeVnicProfile(it.id) } == true

        assertThat(removeResult, `is`(true))
    }


    companion object {
        private val log by LoggerDelegate()
    }

}