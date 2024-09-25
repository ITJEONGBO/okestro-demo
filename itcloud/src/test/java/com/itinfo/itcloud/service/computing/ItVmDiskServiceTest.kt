package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.storage.DiskAttachmentVo
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.model.storage.StorageDomainVo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
        hostVm = "c2ae1da5-ce4f-46df-b337-7c471bea1d8d" // HostedEngine
        nicId = "9f8ba468-35ea-4102-baa6-44951557eac9" // vnet0
    }

    /**
     * [should_addDisks]
     * [ItVmDiskService.addDisksFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.addDisksFromVm]
     */
    @Test
    fun should_addDisks(){
        log.debug("should_addDisks")
        val diskattaches: MutableList<DiskAttachmentVo> = mutableListOf()

        val diskAttachVo1: DiskAttachmentVo =
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
        val diskAttachVo2: DiskAttachmentVo =
            DiskAttachmentVo.builder {
                diskImageVo {
                    DiskImageVo.builder {
                        id { "4f0816d6-3a05-4235-b0b5-9c03f6f35dcc" }
                    }
                }
                bootable { false }
                readOnly { false }
            }
        val diskAttachVo3: DiskAttachmentVo =
            DiskAttachmentVo.builder {
                diskImageVo {
                    DiskImageVo.builder {
                        id { "09dc8bf0-7563-414d-a100-faa863ce2949" }
                    }
                }
                bootable { false }
                readOnly { false }
            }
        val diskAttachVo4: DiskAttachmentVo =
            DiskAttachmentVo.builder {
                diskImageVo {
                    DiskImageVo.builder {
                        size { 2 }
                        alias { "vm01-1_disk02" }
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

        diskattaches.add(diskAttachVo1)
        diskattaches.add(diskAttachVo2)
        diskattaches.add(diskAttachVo3)
        diskattaches.add(diskAttachVo4)

        val vmVo: VmVo = VmVo.builder {
            id { vm01_1 }
            diskAttachmentVos { diskattaches }
        }

//        val result: List<DiskAttachmentVo> =
//            service.addDisksFromVm(vmVo)

//        assertThat(result, `is`(not(nullValue())))
//        assertThat(result.size, `is`(4))
//
//        result.forEach { println(it) }
    }

    /**
     * [should_addDisk]
     * [ItVmDiskService.addDiskFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.addDiskFromVm]
     */
    @Test
    fun should_addDisk() {
        log.debug("should_addDisk")
        val diskAttachVo: DiskAttachmentVo =
            DiskAttachmentVo.builder {
                diskImageVo {
                    DiskImageVo.builder {
                        size { 2 }
                        alias { "vm01-1_disk03" }
                        description { "testone" }
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
        val vmVo: VmVo = VmVo.builder {
            id { vm01_1 }
            diskAttachmentVo { diskAttachVo }
        }

//        val result: DiskAttachmentVo? =
//            service.addDiskFromVm(vmVo)
//
//        assertThat(result, `is`(not(nullValue())))
//
//        println(result)
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
            service.findAllDisksFromVm("4fd618ae-761c-4518-bf6c-f2245e439079")

        assertThat(result, `is`(not(nullValue())))

        result.forEach { println(it) }
//        assertThat(result.size, `is`(1))
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
        val result: DiskAttachmentVo? =
            service.findDiskFromVm(vm01_1, diskAttachmentId)

        assertThat(result, `is`(not(nullValue())))
        log.debug("result: {}", result)
    }

    /**
     * [should_activeDiskFromVm]
     * [ItVmDiskService.activeDiskFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.activeDiskFromVm]
     */
    @Test
    fun should_activeDiskFromVm(){
        log.debug("should_activeDiskFromVm")
        val diskAttachment = DiskAttachmentVo.builder {
            id { "174a9404-bcbc-4ad7-a18b-5c157e7ebc91" }
            vmVo { IdentifiedVo.builder { id { "4fd618ae-761c-4518-bf6c-f2245e439079" } } }
        }

        val result: DiskAttachmentVo? =
            service.activeDiskFromVm(diskAttachment)

        assertThat(result, `is`(not(nullValue())))
        print(result)
    }

    /**
     * [should_activeDiskFromVm]
     * [ItVmDiskService.activeDiskFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.activeDiskFromVm]
     */
    @Test
    fun should_deactivateDiskFromVm(){
        log.debug("should_deactivateDiskFromVm")
        val diskAttachment = DiskAttachmentVo.builder {
            id { "174a9404-bcbc-4ad7-a18b-5c157e7ebc91" }
            vmVo { IdentifiedVo.builder { id { "4fd618ae-761c-4518-bf6c-f2245e439079" } } }
        }

        val result: DiskAttachmentVo? =
            service.deactivateDiskFromVm(diskAttachment)

        assertThat(result, `is`(not(nullValue())))
        print(result)
    }



    /**
     * [should_findAllDisksFromVm]
     * [ItVmDiskService.findAllDisksFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.findAllDisksFromVm]
     */
    @Test
    fun should_findAllDomains(){
        log.debug("should_findAllDomains")
        val result: List<StorageDomainVo> =
            service.findAllDomains("")

        assertThat(result, `is`(not(nullValue())))
        assertThat(result.size, `is`(1))

        result.forEach { println(it) }
    }



    companion object {
        private val log by LoggerDelegate()
    }
}