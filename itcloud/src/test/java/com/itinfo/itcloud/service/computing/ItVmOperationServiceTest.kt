package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.VmExportVo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ItVmOperationServiceTest {
    @Autowired private lateinit var service: ItVmOperationService

    private lateinit var dataCenterId: String
    private lateinit var clusterId: String // Default
    private lateinit var networkId: String // ovirtmgmt(dc: Default)
    private lateinit var host02: String // host02.ititinfo.local
    private lateinit var host01: String // host01
    private lateinit var hostVm: String // hostVm
    private lateinit var vm01_1: String // hostVm

    @BeforeEach
    fun setup() {
        dataCenterId = "6cde7270-6459-11ef-8be2-00163e5d0646"
        clusterId = "6ce0356a-6459-11ef-a03a-00163e5d0646"
        networkId = "00000000-0000-0000-0000-000000000009"
        host01 = "722096d3-4cb2-43b0-bf41-dd69c3a70779"
        host02 = "789b78c4-3fcf-4f19-9b69-d382aa66c12f"
        hostVm = "c26e287c-bc48-4da7-9977-61203abf9e64"
        vm01_1 = "0a27211c-04da-490c-9a05-804f439033e1"
    }


    /**
     * [should_start_Vm]
     * [ItVmOperationService.start]에 대한 단위테스트
     *
     * @see ItVmOperationService.start
     */
    @Test
    fun should_start_Vm() {
        log.debug("should_start_Vm ... ")
        val result: Boolean =
            service.start(vm01_1)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }

    /**
     * [should_pause_Vm]
     * [ItVmOperationService.pause]에 대한 단위테스트
     *
     * @see ItVmOperationService.pause
     */
    @Test
    fun should_pause_Vm() {
        log.debug("should_pause_Vm ... ")
        val result: Boolean =
            service.pause(vm01_1)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }

    /**
     * [should_powerOff_Vm]
     * [ItVmOperationService.powerOff]에 대한 단위테스트
     *
     * @see ItVmOperationService.powerOff
     */
    @Test
    fun should_powerOff_Vm() {
        log.debug("should_powerOff_Vm ... ")
        val result: Boolean =
            service.powerOff(vm01_1)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }

    /**
     * [should_shutdown_Vm]
     * [ItVmOperationService.shutdown]에 대한 단위테스트
     *
     * @see ItVmOperationService.shutdown
     */
    @Test
    fun should_shutdown_Vm() {
        log.debug("should_shutdown_Vm ... ")
        val result: Boolean =
            service.shutdown(vm01_1)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }

    /**
     * [should_reboot_Vm]
     * [ItVmOperationService.reboot]에 대한 단위테스트
     *
     * @see ItVmOperationService.reboot
     */
    @Test
    fun should_reboot_Vm() {
        log.debug("should_reboot_Vm ... ")
        val result: Boolean =
            service.reboot(vm01_1)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }

    /**
     * [should_reset_Vm]
     * [ItVmOperationService.reset]에 대한 단위테스트
     *
     * @see ItVmOperationService.reset
     */
    @Test
    fun should_reset_Vm() {
        log.debug("should_reset_Vm ... ")
        val result: Boolean =
            service.reset(vm01_1)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }



    /**
     * [should_migrateHostList]
     * [ItVmOperationService.migrateHostList]에 대한 단위테스트
     *
     * @see ItVmOperationService.migrateHostList
     */
    @Test
    fun should_migrateHostList() {
        log.debug("should_migrateHostList ... ")
        val result: List<IdentifiedVo> =
            service.migrateHostList(hostVm)

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
    }


    /**
     * [should_migrate_Vm]
     * [ItVmOperationService.migrate]에 대한 단위테스트
     *
     * @see ItVmOperationService.migrate
     */
    @Test
    fun should_migrate_Vm() {
        log.debug("should_migrate_Vm ... ")
        val result: Boolean =
            service.migrate(vm01_1, host02)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }


    /**
     * [should_exportOvaVm]
     * [ItVmOperationService.exportOvaVm]에 대한 단위테스트
     *
     * @see ItVmOperationService.exportOvaVm
     */
    // TODO 테스트를 하려면 설치되는 host에 cmd로 접속하여 지정 디렉토리에 파일이 있는지 확인해야함
    //  => 파일확인은 불가능
    @Test
    fun should_exportOvaVm() {
        log.debug("should_exportOvaVm ... ")
        val exportVo: VmExportVo = VmExportVo.builder {
            vmVo { IdentifiedVo.builder { id { vm01_1 } } }
            hostVo { IdentifiedVo.builder { name { "host02.ititinfo.local" } } }
            directory { "/root" }
            fileName { "exporth2" }
        }
        val result: Boolean =
            service.exportOvaVm(exportVo)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }


    companion object {
        private val log by LoggerDelegate()
    }
}