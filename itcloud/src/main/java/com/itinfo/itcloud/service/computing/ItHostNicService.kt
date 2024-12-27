package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.HostNicVo
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.network.toHostNicVos
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.types.HostNic
import org.springframework.stereotype.Service

interface ItHostNicService {
    /**
     * [ItHostNicService.findAllFromHost]
     * 호스트 네트워크 인터페이스 목록
     *
     * @param hostId [String] 호스트 Id
     * @return List<[HostNicVo]> 네트워크 인터페이스 목록
     */
    @Throws(Error::class)
    fun findAllFromHost(hostId: String): List<HostNicVo>
    /**
     * [ItHostNicService.findOneFromHost]
     * 호스트 네트워크 인터페이스 목록
     *
     * @param hostId [String] 호스트 Id
     * @param hostNicId [String] 호스트 nic Id
     * @return List<[HostNicVo]> 네트워크 인터페이스 목록
     */
    @Throws(Error::class)
    fun findOneFromHost(hostId: String, hostNicId: String): List<HostNicVo>
    /**
     * [ItHostNicService.setUpNetworksFromHost]
     * 호스트 네트워크 설정
     *
     * @param hostId [String] 호스트 Id
     * @param network [NetworkVo] 네트워크 (미정)
     * @return [Boolean] 아직미정
     */
    @Throws(Error::class)
    fun setUpNetworksFromHost(hostId: String, network: NetworkVo): Boolean


}

@Service
class ItHostNicServiceImpl(

): BaseService(), ItHostNicService {

    @Throws(Error::class)
    override fun findAllFromHost(hostId: String): List<HostNicVo> {
        log.info("findAllFromHost ... hostId: {}", hostId)
        val res: List<HostNic> = conn.findAllNicsFromHost(hostId)
            .getOrDefault(listOf())
        return res.toHostNicVos(conn)
    }

    @Throws(Error::class)
    override fun findOneFromHost(hostId: String, hostNicId: String): List<HostNicVo> {
        TODO("Not yet implemented")
    }

    @Throws(Error::class)
    override fun setUpNetworksFromHost(hostId: String, network: NetworkVo): Boolean {
        log.info("setUpNetworksFromHost ... hostId: {}", hostId)

        TODO("Not yet implemented")
    }


    companion object {
        private val log by LoggerDelegate()
    }
}