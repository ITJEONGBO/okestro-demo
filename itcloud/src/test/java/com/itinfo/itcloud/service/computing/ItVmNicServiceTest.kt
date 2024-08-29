package com.itinfo.itcloud.service.computing

import com.itinfo.itcloud.ItCloudApplication.Companion.log
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.network.NicVo
import com.itinfo.itcloud.service.computing.ItVmServiceTest.Companion
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ovirt.engine.sdk4.types.NicInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItVmNicServiceTest]
 * [ItVmNicService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.08.28
 */
@SpringBootTest
class ItVmNicServiceTest {
    @Autowired private lateinit var service: ItVmNicService

    private lateinit var hostVm: String // hostVm
    private lateinit var vmId: String // hostVm
    private lateinit var nicId: String

    @BeforeEach
    fun setup() {
        hostVm = "c26e287c-bc48-4da7-9977-61203abf9e64" // HostedEngine
        vmId = "0a27211c-04da-490c-9a05-804f439033e1" // vm01-1
        nicId = "9f8ba468-35ea-4102-baa6-44951557eac9" // vnet0
    }

    /**
     * [should_findAllNicsFromVm]
     * [ItVmNicService.findAllNicsFromVm]에 대한 단위테스트
     *
     * @see [ItVmNicService.findAllNicsFromVm]
     */
    @Test
    fun should_findAllNicsFromVm() {
        log.debug("should_findAllNicsFromVm ...")
        val result: List<NicVo> =
            service.findAllNicsFromVm(vmId)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result.size, `is`(1))

        result.forEach { println(it) }
    }

    /**
     * [should_findOneNicFromVm]
     * [ItVmNicService.findOneNicFromVm]에 대한 단위테스트
     *
     * @see [ItVmNicService.findOneNicFromVm]
     */
    @Test
    fun should_findOneNicFromVm() {
        log.debug("should_findOneNicFromVm ...")
        val result: NicVo? =
            service.findOneNicFromVm(vmId, nicId)

        assertThat(result, `is`(not(nullValue())))

        println(result)
    }

    /**
     * [should_addNicFromVm]
     * [ItVmNicService.addNicFromVm]에 대한 단위테스트
     *
     * @see ItVmNicService.addNicFromVm
     */
    @Test
    fun should_addNicFromVm() {
        log.debug("should_addNicFromVm ... ")
        val addVmNic: NicVo = NicVo.builder {
            name { "nic7" }
            vnicProfileVo { IdentifiedVo.builder { id { "0000000a-000a-000a-000a-000000000398" } } }
            interface_ { NicInterface.VIRTIO  }
            linked { true }
            plugged { true }
            macAddress { "00:14:4a:23:67:55" }
        }

        val addResult: NicVo? =
            service.addNicFromVm(vmId, addVmNic)

        assertThat(addResult, `is`(not(nullValue())))
        assertThat(addResult?.id, `is`(not(nullValue())))
    }


}