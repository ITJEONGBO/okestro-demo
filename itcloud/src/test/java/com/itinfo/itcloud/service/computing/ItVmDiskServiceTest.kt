package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.storage.DiskAttachmentVo
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.model.storage.toDiskAttachmentVo
import com.itinfo.itcloud.model.storage.toDiskImageVo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ovirt.engine.sdk4.builders.DiskAttachmentBuilder
import org.ovirt.engine.sdk4.builders.DiskBuilder
import org.ovirt.engine.sdk4.builders.StorageDomainBuilder
import org.ovirt.engine.sdk4.types.DiskAttachment
import org.ovirt.engine.sdk4.types.DiskBackup
import org.ovirt.engine.sdk4.types.DiskInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItVmDiskServiceTest]
 * [ItVmDiskService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.08.28
 */
@SpringBootTest
class ItVmDiskServiceTest {
    @Autowired private lateinit var service: ItVmDiskService

    private lateinit var hostVm: String // hostVm
    private lateinit var vm01_1: String // hostVm
    private lateinit var nicId: String

    @BeforeEach
    fun setup() {
        hostVm = "c26e287c-bc48-4da7-9977-61203abf9e64" // HostedEngine
        vm01_1 = "0a27211c-04da-490c-9a05-804f439033e1" // vm01-1
        nicId = "9f8ba468-35ea-4102-baa6-44951557eac9" // vnet0
    }

    /**
     * [should_addDisk]
     * [ItVmDiskService.addDiskFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.addDiskFromVm]
     */
    @Test
    fun should_addDisk(){
        log.debug("should_addDisk")
        val diskattaches: MutableList<DiskAttachmentVo> = mutableListOf()

        val diskAttachVo: DiskAttachmentVo =
            DiskAttachmentVo.builder {
                diskImageVo {
                    DiskImageVo.builder {
                        size { 2 }
                        alias { "vm01-1_disk01" }
                        description { "test" }
                        interface_ { DiskInterface.VIRTIO_SCSI }
                        storageDomainVo { IdentifiedVo.builder { id { "dc38dcb4-c3f9-4568-af0b-0d6a225d25e5" } } }
                        backup { false }
                        active { true }
                        diskProfileVo {IdentifiedVo.builder { id { "df3d6b80-5326-4855-96a4-455147016fc7" } }}
                    }
                }
                bootable { false }
                readOnly { false }
            }

        diskattaches.add(diskAttachVo)

        val vmVo: VmVo = VmVo.builder {
            id { vm01_1 }
            diskAttachmentVos { diskattaches }
        }

        val result: List<DiskAttachmentVo> =
            service.adddiskattach(vmVo)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result.size, `is`(1))

        result.forEach { println(it) }
    }

    /**
     * [should_findAllDisksFromVm]
     * [ItVmDiskService.findAllDisksFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.findAllDisksFromVm]
     */
    @Test
    fun should_findAllDisksFromVm(){
        log.debug("should_findAllDisksFromVm")
        val result: List<DiskAttachmentVo> =
            service.findAllDisksFromVm(vm01_1)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result.size, `is`(1))

        result.forEach { println(it) }
    }

    /**
     * [should_findOneDiskFromVm]
     * [ItVmDiskService.findOneDiskFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.findOneDiskFromVm]
     */
    @Test
    fun should_findOneDiskFromVm(){
        log.debug("should_findOneDiskFromVm")
        val diskAttachmentId = "06276214-bfcf-4943-8c6e-a51a68bc6453"
        val result: DiskAttachmentVo =
            service.findOneDiskFromVm(vm01_1, diskAttachmentId)

        assertThat(result, `is`(not(nullValue())))
        log.debug("result: {}", result)
    }

    companion object {
        private val log by LoggerDelegate()
    }
}