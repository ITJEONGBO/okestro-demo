package com.itinfo.itcloud.service

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.common.DashDataCenterVo
import com.itinfo.itcloud.model.common.toComputings
import com.itinfo.itcloud.model.common.toNetworks
import com.itinfo.itcloud.model.common.toStorageDomains
import com.itinfo.util.ovirt.findAllDataCenters
import org.ovirt.engine.sdk4.types.DataCenter
import org.springframework.stereotype.Service

interface CommonService {
    /**
     * [CommonService.toComputing]
     * 컴퓨팅 목록
     *
     * @return List<[DashDataCenterVo]>
     */
    @Throws(Error::class)
    fun toComputing(): List<DashDataCenterVo>
    /**
     * [CommonService.toNetwork]
     * 네트워크 목록
     *
     * @return List<[DashDataCenterVo]>
     */
    @Throws(Error::class)
    fun toNetwork(): List<DashDataCenterVo>
    /**
     * [CommonService.toStorageDomain]
     * 스토리지도메인 목록
     *
     * @return List<[DashDataCenterVo]>
     */
    @Throws(Error::class)
    fun toStorageDomain(): List<DashDataCenterVo>
}

@Service
class CommonServiceImpl (

): BaseService(), CommonService {

    @Throws(Error::class)
    override fun toComputing(): List<DashDataCenterVo> {
        log.info("toComputing ... ")
        val dataCenters: List<DataCenter> =
            conn.findAllDataCenters()
                .getOrDefault(listOf())
        return dataCenters.toComputings(conn)
    }

    override fun toNetwork(): List<DashDataCenterVo> {
        log.info("toNetwork ... ")
        val dataCenters: List<DataCenter> =
            conn.findAllDataCenters()
                .getOrDefault(listOf())
        return dataCenters.toNetworks(conn)
    }

    override fun toStorageDomain(): List<DashDataCenterVo> {
        log.info("toStorageDomain ... ")
        val dataCenters: List<DataCenter> =
            conn.findAllDataCenters()
                .getOrDefault(listOf())
        return dataCenters.toStorageDomains(conn)
    }

    companion object{
        private val log by LoggerDelegate()
    }

}