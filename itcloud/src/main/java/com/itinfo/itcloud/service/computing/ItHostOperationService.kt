package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.types.Cluster
import org.ovirt.engine.sdk4.types.Host
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
     * [ItHostOperationService.refresh]
     * 호스트 관리 - 새로고침
     *
     * @param hostId [String] 호스트 아이디
     * @return [Boolean]
     */
    @Throws(Error::class)
    fun refresh(hostId: String): Boolean
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
}

@Service
class HostOperationServiceImpl: BaseService(), ItHostOperationService {

    @Throws(Error::class)
    override fun deactivate(hostId: String): Boolean {
        log.info("deactivate ... hostId: {}", hostId)
        val res: Result<Boolean> =
            conn.deactivateHost(hostId)
        return res.isSuccess
    }

    @Throws(Error::class)
    override fun activate(hostId: String): Boolean {
        log.info("activate ... ")
        val res: Result<Boolean> =
            conn.activateHost(hostId)
        return res.isSuccess
    }

    @Throws(Error::class)
    override fun refresh(hostId: String): Boolean {
        log.info("refreshHost ... ")
        val res: Result<Boolean> =
            conn.refreshHost(hostId)
        return res.isSuccess
    }

    @Throws(UnknownHostException::class, Error::class)
    override fun restart(hostId: String): Boolean {
        log.info("reStartHost ... ")
    // TODO: Host 이름, PW 입력문제
        val userName = ""
        val hostPw: String = "adminRoot!@#"

        val res: Result<Boolean> =
            conn.restartHost(hostId, hostPw)
        return res.isSuccess
    }

    companion object {
        private val log by LoggerDelegate()   }
}