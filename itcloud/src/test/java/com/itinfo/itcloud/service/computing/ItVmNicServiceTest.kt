package com.itinfo.itcloud.service.computing

import com.itinfo.itcloud.ItCloudApplication.Companion.log
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.network.NicVo
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
//        assertThat(result?.name, `is`(""))

        println(result)
    }

    /**
     * [should_addFromVm]
     * [ItVmNicService.addFromVm]에 대한 단위테스트
     *
     * @see ItVmNicService.addFromVm
     */
    @Test
    fun should_addFromVm() {
        log.debug("should_addFromVm ... ")
        val addVmNic: NicVo = NicVo.builder {
            name { "nic3" }
            vnicProfileVo { IdentifiedVo.builder { id { "0000000a-000a-000a-000a-000000000398" } } }
            interface_ { NicInterface.VIRTIO  }
            linked { true }
            plugged { true }
//            macAddress { "00:14:4a:23:67:55" } // 기본은 없음
        }

        val addResult: NicVo? =
            service.addFromVm(vmId, addVmNic)

        assertThat(addResult?.id, `is`(not(nullValue())))
        assertThat(addResult?.vnicProfileVo?.id, `is`(addVmNic.vnicProfileVo.id))
        assertThat(addResult?.interface_, `is`(addVmNic.interface_))
        assertThat(addResult?.linked, `is`(addVmNic.linked))
        assertThat(addResult?.plugged, `is`(addVmNic.plugged))
//        assertThat(addResult?.macAddress, `is`(addVmNic.macAddress))
    }

    /**
     * [should_updateFromVm]
     * [ItVmNicService.updateFromVm]에 대한 단위테스트
     *
     * @see ItVmNicService.updateFromVm
     */
    @Test
    fun should_updateFromVm() {
        log.debug("should_updateFromVm ... ")
        val nicId = "80978aab-2a91-489a-9b5b-95009b760026"
        val updateVmNic: NicVo = NicVo.builder {
            id { nicId }
            name { "nic7" }
            vnicProfileVo { IdentifiedVo.builder { id { "86106902-bf8b-4637-95d7-8cf5aca28fc5" } } }
            interface_ { NicInterface.VIRTIO  }
            linked { false }
            plugged { false }
        }

        val updateResult: NicVo? =
            service.updateFromVm(vmId, updateVmNic)

        assertThat(updateResult?.id, `is`(not(nullValue())))
        assertThat(updateResult?.vnicProfileVo?.id, `is`(updateVmNic.vnicProfileVo.id))
        assertThat(updateResult?.interface_, `is`(updateVmNic.interface_))
        assertThat(updateResult?.linked, `is`(updateVmNic.linked))
        assertThat(updateResult?.plugged, `is`(updateVmNic.plugged))
    }

    /**
     * [should_removeFromVm]
     * [ItVmNicService.removeFromVm]에 대한 단위테스트
     *
     * @see ItVmNicService.removeFromVm
     */
    @Test
    fun should_removeFromVm() {
        log.debug("should_removeFromVm ... ")
        val nicId = "80978aab-2a91-489a-9b5b-95009b760026"
        val removeResult: Boolean =
            service.removeFromVm(vmId, nicId)

        assertThat(removeResult, `is`(true))
    }

}