package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.network.*
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
            service.findAllFromHost("c35ee370-b9f6-4c5b-9c65-fe2e716795b5")

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
        val hostId = "a0816fc5-d06a-478e-850e-854b2e5a1f66" // host-05
        val bonds: List<HostNicVo> =
            listOf(
                HostNicVo.builder {
                    name { "bond1" }
                    bondingVo {
                        BondingVo.builder {
                            slaves {
                                listOf(
                                    IdentifiedVo.builder { name { "ens192" } } ,
                                    IdentifiedVo.builder { name { "ens224" } }
//                                    IdentifiedVo.builder { id { "9d00376a-dce6-40f4-ae57-52149f42fcfb" } } ,
//                                    IdentifiedVo.builder { id { "6045a812-7d28-4c9d-80aa-6d08fbea7869" } }
                                )
                            }
                        }
                    }
                }
            )
        val networkAttach: List<NetworkAttachmentVo> =
            listOf(
                NetworkAttachmentVo.builder {
                    networkVo {
                        IdentifiedVo.builder {
                            id { "3b38dba9-5b14-4345-b7da-96990423a8e1" }
                        }
                    }
                    hostNicVo { IdentifiedVo.builder { name { "bond1" } } }
//                    ipAddressAssignments {
//                        listOf(
//                            IpAddressAssignmentVo.builder {
//                                assignmentMethod { "static" }
//                                ipVo {
//                                    IpVo.builder {
//                                        address {  }
//                                    }
//                                }
//                            }
//                        )
//                    }
                }
            )

        val result: Boolean =
            service.setUpNetworksFromHost(hostId, bonds, networkAttach)

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }


    companion object {
        private val log by LoggerDelegate()
    }
}