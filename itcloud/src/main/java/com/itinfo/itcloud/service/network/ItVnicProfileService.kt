package com.itinfo.itcloud.service.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.network.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.types.VnicProfile
import org.springframework.stereotype.Service

interface ItVnicProfileService{
    /**
     * [ItVnicProfileService.findAllVnicProfilesFromNetwork]
     * 네트워크 - vNIC Profile 목록
     *
     * @param networkId [String] 네트워크 Id
     * @return List<[VnicProfileVo]>
     */
    @Throws(Error::class)
    fun findAllVnicProfilesFromNetwork(networkId: String): List<VnicProfileVo>
    /**
     * [ItVnicProfileService.findVnicProfile]
     * 네트워크 - vNIC Profile
     *
     * @param vnicProfileId [String] vnicProfile Id
     * @return [VnicProfileVo]
     */
    @Throws(Error::class)
    fun findVnicProfile(vnicProfileId: String): VnicProfileVo?
    /**
     * [ItVnicProfileService.addVnicProfile]
     * 네트워크 - vNIC Profile 생성
     *
     * @param vnicProfileVo [VnicProfileVo]
     * @return [VnicProfileVo]?
     */
    @Throws(Error::class)
    fun addVnicProfile(vnicProfileVo: VnicProfileVo): VnicProfileVo?
    /**
     * [ItVnicProfileService.updateVnicProfile]
     * 네트워크 - vNIC Profile 편집
     *
     * @param vnicProfileVo [VnicProfileVo]
     * @return [VnicProfileVo]?
     */
    @Throws(Error::class)
    fun updateVnicProfile(vnicProfileVo: VnicProfileVo): VnicProfileVo?
    /**
     * [ItVnicProfileService.removeVnicProfile]
     * 네트워크 - vNIC Profile 삭제
     *
     * @param vnicProfileId [String] vnicProfile Id
     * @return [Boolean]
     */
    @Throws(Error::class)
    fun removeVnicProfile(vnicProfileId: String): Boolean

}
@Service
class VnicProfileServiceImpl(

): BaseService(), ItVnicProfileService {

    @Throws(Error::class)
    override fun findAllVnicProfilesFromNetwork(networkId: String): List<VnicProfileVo> {
        log.info("findAllVnicProfilesFromNetwork ... networkId: {}", networkId)
        val res: List<VnicProfile> =
            conn.findAllVnicProfilesFromNetwork(networkId)
                .getOrDefault(listOf())
        return res.toVnicProfileVos(conn)
    }

    @Throws(Error::class)
    override fun findVnicProfile(vnicProfileId: String): VnicProfileVo? {
        log.info("findVnicProfile ... vcId: {}", vnicProfileId)
        val res: VnicProfile? =
            conn.findVnicProfile(vnicProfileId)
                .getOrNull()
        return res?.toVnicProfileVo(conn)
    }

    @Throws(Error::class)
    override fun addVnicProfile(vnicProfileVo: VnicProfileVo): VnicProfileVo? {
        log.info("addVnicProfile ... ")
        val res: VnicProfile? =
            conn.addVnicProfileFromNetwork(
                vnicProfileVo.networkVo.id,
                vnicProfileVo.toAddVnicProfileBuilder()
            ).getOrNull()
        return res?.toVnicProfileVo(conn)
    }

    @Throws(Error::class)
    override fun updateVnicProfile(vnicProfileVo: VnicProfileVo): VnicProfileVo? {
        log.info("updateVnicProfile ... ")
        val res: VnicProfile? =
            conn.updateVnicProfile(vnicProfileVo.toEditVnicProfileBuilder())
                .getOrNull()
        return res?.toVnicProfileVo(conn)
    }

    @Throws(Error::class)
    override fun removeVnicProfile(vnicProfileId: String): Boolean {
        log.info("removeVnicProfile ... ")
        val res: Result<Boolean> =
            conn.removeVnicProfile(vnicProfileId)
        return res.isSuccess
    }


    companion object {
        private val log by LoggerDelegate()
    }
}