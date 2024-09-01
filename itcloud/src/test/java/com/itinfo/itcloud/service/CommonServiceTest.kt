package com.itinfo.itcloud.service

import org.junit.jupiter.api.Test
import com.itinfo.itcloud.model.common.DashDataCenterVo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CommonServiceTest {
    @Autowired private lateinit var service: CommonService

    @Test
    fun should_computing(){
        val result: List<DashDataCenterVo> =
            service.toComputing()

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
    }

    @Test
    fun should_network(){
        val result: List<DashDataCenterVo> =
            service.toNetwork()

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
    }

    @Test
    fun should_storageDomain(){
        val result: List<DashDataCenterVo> =
            service.toStorageDomain()

        assertThat(result, `is`(not(nullValue())))
        result.forEach { println(it) }
    }

}