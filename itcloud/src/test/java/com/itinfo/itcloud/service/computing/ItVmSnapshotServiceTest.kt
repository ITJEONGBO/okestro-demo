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
 * @since 2024.08.28
 */
@SpringBootTest
class ItVmSnapshotServiceTest {
    @Autowired private lateinit var service: ItVmSnapshotService

    private lateinit var hostVm: String // hostVm

    @BeforeEach
    fun setup() {
        hostVm = "c26e287c-bc48-4da7-9977-61203abf9e64" // HostedEngine
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
            service.findAllSnapshotsFromVm("4fd618ae-761c-4518-bf6c-f2245e439079")

        assertThat(result, `is`(not(nullValue())))

        result.forEach { println(it) }
        assertThat(result.size, `is`(1))
    }

    companion object {
        private val log by LoggerDelegate()
    }
}