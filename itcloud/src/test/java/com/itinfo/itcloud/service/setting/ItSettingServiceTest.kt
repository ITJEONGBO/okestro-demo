package com.itinfo.itcloud.service.setting

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.setting.UsersVo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * [ItSettingServiceTest]
 * [ItSettingService]에 대한 단위테스트
 *
 * @author chanhi2000
 * @author deh22
 * @since 2024.10.15
 */
@SpringBootTest
class ItSettingServiceTest {
    @Autowired private lateinit var service: ItSettingService

    /**
     * [should_findAllUser]
     * [ItSettingService.findAllUser]에 대한 단위테스트
     *
     * @see ItSettingService.findAllUser
     */
    @Test
    fun should_findAllUser() {
        log.debug("should_findAllUser ... ")
        val result: List<UsersVo> =
            service.findAllUser()

        assertThat(result, `is`(not(nullValue())))
        assertThat(result.size, `is`(1))
    }



    companion object {
        private val log by LoggerDelegate()
    }

}
