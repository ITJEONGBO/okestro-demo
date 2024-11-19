package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.auth.RutilProperties
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Error
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.UnknownHostException

interface ItHostOperationService {
    /**
     * [ItHostOperationService.deactivate]
     * 호스트 관리 - 유지보수
     *
     * @param hostId [String] 호스트 아이디
     * @return [Boolean]
     */
    @Throws(Error::class)
    fun deactivate(hostId: String): Boolean
    /**
     * [ItHostOperationService.activate]
     * 호스트 관리 - 활성
     *
     * @param hostId [String] 호스트 아이디
     * @return [Boolean]
     */
    @Throws(Error::class)
    fun activate(hostId: String): Boolean
    /**
     * [ItHostOperationService.restart]
     * 호스트 ssh 관리 - 재시작
     *
     * @param hostId [String] 호스트 아이디
     * @return [Boolean]
     */
    @Throws(
        UnknownHostException::class,
        Error::class
    )
    fun restart(hostId: String): Boolean
    /**
     * [ItHostOperationService.enrollCertificate]
     * 설치 - 인증서 등록
     *
     * @param hostId [String] 호스트 아이디
     * @return [Boolean]
     */
    @Throws(Error::class)
    fun enrollCertificate(hostId: String): Boolean
    /**
     * [ItHostOperationService.globalHaActivate]
     * 글로벌 HA 유지관리를 활성화
     *
     * @param hostId [String] 호스트 아이디
     * @return [Boolean]
     */
    @Throws(Error::class)
    fun globalHaActivate(hostId: String): Boolean
    /**
     * [ItHostOperationService.globalHaDeactivate]
     * 글로벌 HA 유지관리를 비활성화
     *
     * @param hostId [String] 호스트 아이디
     * @return [Boolean]
     */
    @Throws(Error::class)
    fun globalHaDeactivate(hostId: String): Boolean

    /**
     * [ItHostOperationService.refresh]
     * 호스트 관리 - 새로고침
     *
     * @param hostId [String] 호스트 아이디
     * @return [Boolean]
     */
    @Deprecated("사용안함")
    @Throws(Error::class)
    fun refresh(hostId: String): Boolean
}

@Service
class HostOperationServiceImpl(

): BaseService(), ItHostOperationService {

    @Autowired private lateinit var rutil: RutilProperties

    @Throws(Error::class)
    override fun deactivate(hostId: String): Boolean {
        log.info("deactivate ... hostId: {}", hostId)
        val res: Result<Boolean> =
            conn.deactivateHost(hostId)
        return res.isSuccess
    }

    @Throws(Error::class)
    override fun activate(hostId: String): Boolean {
        log.info("activate ... hostId: {}", hostId)
        val res: Result<Boolean> =
            conn.activateHost(hostId)
        return res.isSuccess
    }

    @Throws(UnknownHostException::class, Error::class)
    override fun restart(hostId: String): Boolean {
        log.info("reStart ... hostId: {}", hostId)
        // TODO host root 로그인이 아닌 새호스트 계정을 생성하고 이를 application.properties에 저장해서 보여주는 방식
        val name = rutil.id
        val password = rutil.password
        log.info("Host ID: {}, Password: {}", name, password)
        val res: Result<Boolean> =
            conn.restartHost(hostId, name, password)
        return res.isSuccess
    }

    // TODO 인증서 등록에 대한 조건이 뭔지 모르겠음 (활성화 조건을 모름)
    @Throws(Error::class)
    override fun enrollCertificate(hostId: String): Boolean {
        log.info("enrollCertificate ... hostId: {}", hostId)
        val res: Result<Boolean> =
            conn.enrollCertificate(hostId)
        return res.isSuccess
    }

    @Throws(Error::class)
    override fun globalHaActivate(hostId: String): Boolean {
        TODO("Not yet implemented")
    }

    @Throws(Error::class)
    override fun globalHaDeactivate(hostId: String): Boolean {
        TODO("Not yet implemented")
    }


    @Deprecated("사용안함")
    @Throws(Error::class)
    override fun refresh(hostId: String): Boolean {
        log.info("refresh ... hostId: {}", hostId)
        val res: Result<Boolean> =
            conn.refreshHost(hostId)
        return res.isSuccess
    }

    companion object {
        private val log by LoggerDelegate()
    }
}