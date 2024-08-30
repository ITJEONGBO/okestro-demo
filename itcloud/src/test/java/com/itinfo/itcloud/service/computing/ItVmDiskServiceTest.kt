package com.itinfo.itcloud.service.computing

import com.itinfo.itcloud.ItCloudApplication.Companion.log
import com.itinfo.itcloud.model.storage.DiskAttachmentVo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    private lateinit var vmId: String // hostVm
    private lateinit var nicId: String

    @BeforeEach
    fun setup() {
        hostVm = "c26e287c-bc48-4da7-9977-61203abf9e64" // HostedEngine
        vmId = "0a27211c-04da-490c-9a05-804f439033e1" // vm01-1
        nicId = "9f8ba468-35ea-4102-baa6-44951557eac9" // vnet0
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
            service.findAllDisksFromVm(vmId)

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
            service.findOneDiskFromVm(vmId, diskAttachmentId)

        assertThat(result, `is`(not(nullValue())))
        print(result)
    }


}