package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.SnapshotVo
import com.itinfo.itcloud.model.storage.DiskAttachmentVo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItVmSnapshotServiceTest]
 * [ItVmSnapshotService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.09.26
 */
@SpringBootTest
class ItVmSnapshotServiceTest {
    @Autowired private lateinit var service: ItVmSnapshotService

    private lateinit var hostVm: String // hostVm
    private lateinit var apm: String // apm

    @BeforeEach
    fun setup() {
        hostVm = "c2ae1da5-ce4f-46df-b337-7c471bea1d8d" // HostedEngine
        apm = "fceb0fe4-2927-4340-a970-401fe55781e6"
    }

    /**
     * [should_findAllSnapshotsFromVm]
     * [ItVmSnapshotService.findAllSnapshotsFromVm]에 대한 단위테스트
     *
     * @see [ItVmSnapshotService.findAllSnapshotsFromVm]
     */
    @Test
    fun should_findAllSnapshotsFromVm(){
        log.debug("should_findAllSnapshotsFromVm")
        val result: List<SnapshotVo> =
            service.findAllSnapshotsFromVm(apm)

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
        assertThat(result.size, `is`(3))
    }


    /**
     * [should_findSnapshotFromVm]
     * [ItVmSnapshotService.findSnapshotFromVm]에 대한 단위테스트
     *
     * @see [ItVmSnapshotService.findSnapshotFromVm]
     */
    @Test
    fun should_findSnapshotFromVm(){
        log.debug("should_findSnapshotFromVm")
        val result: SnapshotVo? =
            service.findSnapshotFromVm(apm, snapshotId = "2bda4522-5d82-4772-a1bd-65633928bf35")

        assertThat(result, `is`(not(nullValue())))
        println(result)
    }




    companion object {
        private val log by LoggerDelegate()
    }
}