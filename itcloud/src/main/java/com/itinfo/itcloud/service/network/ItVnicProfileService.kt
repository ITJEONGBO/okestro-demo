package com.itinfo.itcloud.service.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.network.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.types.VnicProfile
import org.springframework.stereotype.Service

interface ItVnicProfileService{
    /**
     * [ItVnicProfileService.findAll]
     * 네트워크 - vNIC Profile 목록
     *
     * @return List<[VnicProfileVo]>
     */
    @Throws(Error::class)
    fun findAll(): List<VnicProfileVo>
    /**
     * [ItVnicProfileService.findAllFromNetwork]
     * 네트워크 - vNIC Profile 목록
     *
     * @param networkId [String] 네트워크 Id
     * @return List<[VnicProfileVo]>
     */
    @Throws(Error::class)
    fun findAllFromNetwork(networkId: String): List<VnicProfileVo>
    /**
     * [ItVnicProfileService.findOne]
     * 네트워크 - vNIC Profile
     *
     * @param vnicProfileId [String] vnicProfile Id
     * @return [VnicProfileVo]
     */
    @Throws(Error::class)
    fun findOne(vnicProfileId: String): VnicProfileVo?
    /**
     * [ItVnicProfileService.add]
     * 네트워크 - vNIC Profile 생성
     *
     * @param vnicProfileVo [VnicProfileVo]
     * @return [VnicProfileVo]?
     */
    @Throws(Error::class)
    fun add(vnicProfileVo: VnicProfileVo): VnicProfileVo?
    /**
     * [ItVnicProfileService.update]
     * 네트워크 - vNIC Profile 편집
     *
     * @param vnicProfileVo [VnicProfileVo]
     * @return [VnicProfileVo]?
     */
    @Throws(Error::class)
    fun update(vnicProfileVo: VnicProfileVo): VnicProfileVo?
    /**
     * [ItVnicProfileService.remove]
     * 네트워크 - vNIC Profile 삭제
     *
     * @param vnicProfileId [String] vnicProfile Id
     * @return [Boolean]
     */
    @Throws(Error::class)
    fun remove(vnicProfileId: String): Boolean

}
@Service
class VnicProfileServiceImpl(

): BaseService(), ItVnicProfileService {

    @Throws(Error::class)
    override fun findAll(): List<VnicProfileVo> {
        log.info("findAll ... ")
        val res: List<VnicProfile> =
            conn.findAllVnicProfiles().getOrDefault(listOf())
        return res.toVnicProfileVos(conn)
    }

    @Throws(Error::class)
    override fun findAllFromNetwork(networkId: String): List<VnicProfileVo> {
        log.info("findAllFromNetwork ... networkId: {}", networkId)
        val res: List<VnicProfile> =
            conn.findAllVnicProfilesFromNetwork(networkId).getOrDefault(listOf())
        return res.toVnicProfileVos(conn)
    }

    @Throws(Error::class)
    override fun findOne(vnicProfileId: String): VnicProfileVo? {
        log.info("findOne ... vcId: {}", vnicProfileId)
        val res: VnicProfile? =
            conn.findVnicProfile(vnicProfileId).getOrNull()
        return res?.toVnicProfileVo(conn)
    }

    @Throws(Error::class)
    override fun add(vnicProfileVo: VnicProfileVo): VnicProfileVo? {
        log.info("add ... ")
        val res: VnicProfile? =
            conn.addVnicProfileFromNetwork(
                vnicProfileVo.networkVo.id,
                vnicProfileVo.toAddVnicProfileBuilder()
            ).getOrNull()
        return res?.toVnicProfileVo(conn)
    }

    @Throws(Error::class)
    override fun update(vnicProfileVo: VnicProfileVo): VnicProfileVo? {
        log.info("update ... ")
        val res: VnicProfile? =
            conn.updateVnicProfile(vnicProfileVo.toEditVnicProfileBuilder()).getOrNull()
        return res?.toVnicProfileVo(conn)
    }

    @Throws(Error::class)
    override fun remove(vnicProfileId: String): Boolean {
        log.info("remove ... ")
        val res: Result<Boolean> =
            conn.removeVnicProfile(vnicProfileId)
        return res.isSuccess
    }


    companion object {
        private val log by LoggerDelegate()
    }
}