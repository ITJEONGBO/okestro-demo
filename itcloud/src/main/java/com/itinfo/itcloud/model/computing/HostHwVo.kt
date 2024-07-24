package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.model.gson
import org.ovirt.engine.sdk4.types.Host
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(HostHwVo::class.java)


/**
 * [HostHwVo]
 * 호스트 하드웨어
 *
 * @property manufacturer [String] 생산자
 * @property family [String] 제품군
 * @property productName [String] 제품 이름
 * @property serialNum [String] 일련번호
 * @property uuid [String]
 * @property hwVersion [String] 버전
 * @property cpuName [String] cpu 모델
 * @property cpuType [String] cpu 유형
 * @property cpuTopologyCore [Int] cpu-topology-core
 * @property cpuTopologySocket [Int] cpu-topology-socket
 * @property cpuTopologyThread [Int] cpu-topology-thread
 * @property cpuTopologyAll [Int]  cpu core*socket*thread 논리 cpu 코어수
 * @property cpuOnline List<[Int]>  온라인 논리 CPU 수
 * // val hostCpuUnits: List<HostCpuUnit> = conn.findAllCpuUnitFromHost(this@toHostVo.id())
 */
class HostHwVo(
    val manufacturer: String = "",
    val family: String = "",
    val productName: String = "",
    val serialNum: String = "",
    val uuid: String = "",
    val hwVersion: String = "",
    val cpuName: String = "",
    val cpuType: String = "",
    val cpuTopologyCore: Int = 0,
    val cpuTopologySocket: Int = 0,
    val cpuTopologyThread: Int = 0,
    val cpuTopologyAll: Int = 0,
    val cpuOnline: List<Int> = listOf(),
): Serializable{
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bManufacturer: String = ""; fun manufacturer(block: () -> String?) { bManufacturer = block() ?: "" }
        private var bFamily: String = ""; fun family(block: () -> String?) { bFamily = block() ?: "" }
        private var bProductName: String = ""; fun productName(block: () -> String?) { bProductName = block() ?: "" }
        private var bSerialNum: String = ""; fun serialNum(block: () -> String?) { bSerialNum = block() ?: "" }
        private var bUuid: String = ""; fun uuid(block: () -> String?) { bUuid = block() ?: "" }
        private var bHwVersion: String = ""; fun hwVersion(block: () -> String?) { bHwVersion = block() ?: "" }
        private var bCpuName: String = ""; fun cpuName(block: () -> String?) { bCpuName = block() ?: "" }
        private var bCpuType: String = ""; fun cpuType(block: () -> String?) { bCpuType = block() ?: "" }
        private var bCpuTopologyCore: Int = 0; fun cpuTopologyCore(block: () -> Int?) { bCpuTopologyCore = block() ?: 0 }
        private var bCpuTopologySocket: Int = 0; fun cpuTopologySocket(block: () -> Int?) { bCpuTopologySocket = block() ?: 0 }
        private var bCpuTopologyThread: Int = 0; fun cpuTopologyThread(block: () -> Int?) { bCpuTopologyThread = block() ?: 0 }
        private var bCpuTopologyAll: Int = 0; fun cpuTopologyAll(block: () -> Int?) { bCpuTopologyAll = block() ?: 0 }
        private var bCpuOnline: List<Int> = listOf(); fun cpuOnline(block: () -> List<Int>?) { bCpuOnline = block() ?: listOf() }

        fun build(): HostHwVo = HostHwVo(bManufacturer, bFamily, bProductName, bSerialNum, bUuid, bHwVersion, bCpuName, bCpuType, bCpuTopologyCore, bCpuTopologySocket, bCpuTopologyThread, bCpuTopologyAll, bCpuOnline )
    }

    companion object {
        inline fun builder(block: HostHwVo.Builder.() -> Unit): HostHwVo = HostHwVo.Builder().apply(block).build()
    }
}

/**
 * 호스트 하드웨어 정보 받기
 * @param host  호스트 객체
 * @return 하드웨어 정보
 */
fun Host.toHostHwVo(): HostHwVo {
   log.debug("Host.toHostHwVo ... ")
   return HostHwVo.builder {
       family { if (this@toHostHwVo.hardwareInformation().familyPresent()) this@toHostHwVo.hardwareInformation().family() else "" }
       manufacturer { if(this@toHostHwVo.hardwareInformation().manufacturerPresent()) this@toHostHwVo.hardwareInformation().manufacturer() else "" }
       productName { if (this@toHostHwVo.hardwareInformation().productNamePresent()) this@toHostHwVo.hardwareInformation().productName() else "" }
       hwVersion { if (this@toHostHwVo.hardwareInformation().versionPresent()) this@toHostHwVo.hardwareInformation().version() else "" }
       cpuName { if (this@toHostHwVo.cpu().namePresent()) this@toHostHwVo.cpu().name() else "" }
       cpuType { if (this@toHostHwVo.cpu().typePresent()) this@toHostHwVo.cpu().type() else "" }
       uuid { if (this@toHostHwVo.hardwareInformation().uuidPresent()) this@toHostHwVo.hardwareInformation().uuid() else "" }
       serialNum { if (this@toHostHwVo.hardwareInformation().serialNumberPresent()) this@toHostHwVo.hardwareInformation().serialNumber() else "" }
       cpuTopologySocket { if (this@toHostHwVo.cpu().topologyPresent()) this@toHostHwVo.cpu().topology().socketsAsInteger() else 0 }
       cpuTopologyThread { if (this@toHostHwVo.cpu().topologyPresent()) this@toHostHwVo.cpu().topology().threadsAsInteger() else 0 }
       cpuTopologyCore { if (this@toHostHwVo.cpu().topologyPresent()) this@toHostHwVo.cpu().topology().coresAsInteger() else 0 }
   }
}