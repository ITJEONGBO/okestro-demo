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
import java.util.Arrays

/**
 * [ItVmDiskServiceTest]
 * [ItVmDiskService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.09.26
 */
@SpringBootTest
class ItVmDiskServiceTest {
    @Autowired private lateinit var service: ItVmDiskService

    private lateinit var hostVm: String // hostVm
    private lateinit var apm: String // apm
    private lateinit var diskAttachmentId: String // apm
    private lateinit var domainId: String // nfs
    private lateinit var storage: String // hosted-storage
    private lateinit var diskProfileId: String // apm

    @BeforeEach
    fun setup() {
        hostVm = "c2ae1da5-ce4f-46df-b337-7c471bea1d8d" // HostedEngine
        apm = "fceb0fe4-2927-4340-a970-401fe55781e6"
        diskAttachmentId = "ebe58983-3c96-473a-9553-98bee3606f0e"
        domainId = "06faa572-f1ac-4874-adcc-9d26bb74a54d"
        storage = "213b1a0a-b0c0-4d10-95a4-7aafed4f76b9"
        diskProfileId = "3b68642f-425a-4d0d-aa2f-0fef3a1a20d5"
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
            service.findAllDisksFromVm(apm)

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
        assertThat(result.size, `is`(2))
    }

    /**
     * [should_findDiskFromVm]
     * [ItVmDiskService.findDiskFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.findDiskFromVm]
     */
    @Test
    fun should_findDiskFromVm(){
        log.debug("should_findDiskFromVm")
        val result: DiskAttachmentVo? =
            service.findDiskFromVm(apm, diskAttachmentId)

        assertThat(result, `is`(not(nullValue())))
        println(result)
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
        val diskAttachVo: DiskAttachmentVo = DiskAttachmentVo.builder {
            bootable { false }
            active { true }
            interface_ { DiskInterface.VIRTIO_SCSI }
            readOnly { false }
            diskImageVo {
                DiskImageVo.builder {
                    size { 1 }
                    alias { "random3_disk" }
                    description { "" }
                    storageDomainVo {
                        IdentifiedVo.builder { id { domainId } }
                    }
                    sparse { true } // 할당정책: 씬
                    diskProfileVo {
                        IdentifiedVo.builder { id { diskProfileId } }
                    }
                    wipeAfterDelete { false }
                    sharable { false }
                    backup { true } // 증분백업 기본값 t
                }
            }
        }

        val result: DiskAttachmentVo? =
            service.addDiskFromVm(apm, diskAttachVo)

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }

    /**
     * [should_attachMultiDiskFromVm]
     * [ItVmDiskService.attachMultiDiskFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.attachMultiDiskFromVm]
     */
    @Test
    fun should_attachMultiDiskFromVm() {
        log.debug("should_attachMultiDiskFromVm")
        val diskAttachVos: List<DiskAttachmentVo> =
            Arrays.asList(
                DiskAttachmentVo.builder {
                    bootable { false }
                    readOnly { false }
                    active { true }
                    diskImageVo {
                        DiskImageVo.builder {
                            id { "bd2f3120-e605-4bfb-8faa-2407c0349399" }
                        }
                    }
                },
                DiskAttachmentVo.builder {
                    bootable { false }
                    readOnly { false }
                    active { true }
                    diskImageVo {
                        DiskImageVo.builder {
                            id { "8b2637c9-d219-4c69-a5ee-828397a12f3a" }
                        }
                    }
                }
            )

        val result: Boolean =
            service.attachMultiDiskFromVm(apm, diskAttachVos)
        assertThat(result, `is`(not(nullValue())))
        println(result)
    }

    /**
     * [should_activeDisksFromVm]
     * [ItVmDiskService.activeDisksFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.activeDisksFromVm]
     */
    @Test
    fun should_activeDisksFromVm(){
        log.debug("should_activeDisksFromVm")
        val ids: List<String> =  Arrays.asList(
            "ebe58983-3c96-473a-9553-98bee3606f0e",
            "33b2d1c3-bf01-46d6-9bc2-368180e3955c",
            "05bd71e4-1de7-494a-865d-35aebb7b5d3b"
        )

        val result: Boolean? =
            service.activeDisksFromVm(apm, ids)

        assertThat(result, `is`(not(nullValue())))
        print(result)
    }

    /**
     * [should_deactivateDisksFromVm]
     * [ItVmDiskService.deactivateDisksFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.deactivateDisksFromVm]
     */
    @Test
    fun should_deactivateDisksFromVm(){
        log.debug("should_deactivateDisksFromVm")
        val ids: List<String> =
            Arrays.asList(
                "ebe58983-3c96-473a-9553-98bee3606f0e",
                "33b2d1c3-bf01-46d6-9bc2-368180e3955c",
                "05bd71e4-1de7-494a-865d-35aebb7b5d3b"
            )

        val result: Boolean? =
            service.deactivateDisksFromVm(apm, ids)

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
            service.findAllStorageDomains(apm,"64a0c5a4-feb2-4571-afba-89bfb4c41e9a")

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
        assertThat(result.size, `is`(1))
    }

    /**
     * [should_moveDisk]
     * [ItVmDiskService.moveDiskFromVm]에 대한 단위테스트
     *
     * @see [ItVmDiskService.moveDiskFromVm]
     */
    @Test
    fun should_moveDisk() {
        log.debug("should_moveDiskFromVm")
        val diskAttachVo: DiskAttachmentVo = DiskAttachmentVo.builder {
            diskImageVo {
                DiskImageVo.builder {
                    id { "8b2637c9-d219-4c69-a5ee-828397a12f3a" }
                    storageDomainVo {
                        IdentifiedVo.builder {
                            id { storage }
                        }
                    }
                }
            }
        }
        val result: Boolean =
            service.moveDiskFromVm(apm, diskAttachVo)

        assertThat(result, `is`(not(nullValue())))
        assertThat(result, `is`(true))
    }


    companion object {
        private val log by LoggerDelegate()
    }
}