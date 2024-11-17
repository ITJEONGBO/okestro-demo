package com.itinfo.itcloud.repository.history.dto

import com.itinfo.itcloud.GB
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.repository.history.entity.HostSamplesHistoryEntity
import com.itinfo.util.ovirt.findAllHosts
import com.itinfo.util.ovirt.findAllStatisticsFromHost
import com.itinfo.util.ovirt.findAllVms
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Host
import org.ovirt.engine.sdk4.types.HostStatus
import org.ovirt.engine.sdk4.types.Statistic
import org.ovirt.engine.sdk4.types.VmStatus
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.time.LocalDateTime

private val log = LoggerFactory.getLogger(HostUsageDto::class.java)

/**
 * [HostUsageDto]
 * 전체 사용량 용도
 * 
 * @property hostId [String] 
 * @property hostName [String] 
 * @property historyDatetime [LocalDateTime] 
 * @property totalCpuUsagePercent [Double] 
 * @property totalMemoryUsagePercent [Double]
 * @property totalCpuCore [Int]
 * @property usedCpuCore [Int]
 * @property commitCpuCore [Int]
 * @property totalMemoryGB [Double]
 * @property usedMemoryGB [Double] 
 * @property freeMemoryGB [Double] 
 */
class HostUsageDto(
    val hostId: String = "",
    val hostName: String = "",
    val historyDatetime: LocalDateTime? = null,
    val totalCpuUsagePercent: Double = 0.0,
    val totalMemoryUsagePercent: Double = 0.0,
    val totalCpuCore: Int = 0,
    val usedCpuCore: Int = 0,
    val commitCpuCore: Int = 0,
    val totalMemoryGB: Double = 0.0,
    val usedMemoryGB: Double = 0.0,
    val freeMemoryGB: Double = 0.0,
): Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder {
    	private var bHostId: String = "";fun hostId(block: () -> String?) { bHostId = block() ?: "" }
    	private var bHostName: String = "";fun hostName(block: () -> String?) { bHostName = block() ?: "" }
    	private var bHistoryDatetime: LocalDateTime? = null;fun historyDatetime(block: () -> LocalDateTime?) { bHistoryDatetime = block() }
    	private var bTotalCpuUsagePercent: Double = 0.0;fun totalCpuUsagePercent(block: () -> Double?) { bTotalCpuUsagePercent = block() ?: 0.0 }
    	private var bTotalMemoryUsagePercent: Double = 0.0;fun totalMemoryUsagePercent(block: () -> Double?) { bTotalMemoryUsagePercent = block() ?: 0.0 }
        private var bTotalCpuCore: Int = 0; fun totalCpuCore(block: () -> Int?) { bTotalCpuCore = block() ?: 0 }
        private var bUsedCpuCore: Int = 0; fun usedCpuCore(block: () -> Int?) { bUsedCpuCore = block() ?: 0 }
        private var bCommitCpuCore: Int = 0; fun commitCpuCore(block: () -> Int?) { bCommitCpuCore = block() ?: 0 }
    	private var bTotalMemoryGB: Double = 0.0;fun totalMemoryGB(block: () -> Double?) { bTotalMemoryGB = block() ?: 0.0 }
    	private var bUsedMemoryGB: Double = 0.0;fun usedMemoryGB(block: () -> Double?) { bUsedMemoryGB = block() ?: 0.0 }
    	private var bFreeMemoryGB: Double = 0.0;fun freeMemoryGB(block: () -> Double?) { bFreeMemoryGB = block() ?: 0.0 }
        fun build(): HostUsageDto = HostUsageDto(bHostId, bHostName, bHistoryDatetime, bTotalCpuUsagePercent, bTotalMemoryUsagePercent, bTotalCpuCore, bUsedCpuCore, bCommitCpuCore, bTotalMemoryGB, bUsedMemoryGB, bFreeMemoryGB)
    }

    companion object {
        inline fun builder(block: HostUsageDto.Builder.() -> Unit): HostUsageDto = HostUsageDto.Builder().apply(block).build()
    }
}

fun HostSamplesHistoryEntity.toHostUsageDto(): HostUsageDto {
    return HostUsageDto.builder {
        hostId { "${this@toHostUsageDto.hostId}" }
        historyDatetime { this@toHostUsageDto.historyDatetime }
        totalCpuUsagePercent { this@toHostUsageDto.cpuUsagePercent.toDouble() }
        totalMemoryUsagePercent { this@toHostUsageDto.memoryUsagePercent.toDouble() }
    }
}

fun List<HostSamplesHistoryEntity>.toHostUsageDtos(): List<HostUsageDto> =
    this@toHostUsageDtos.map { it.toHostUsageDto() }

fun List<Host>.toHostUsageDto(conn: Connection, hostSamplesHistoryEntities: List<HostSamplesHistoryEntity>): HostUsageDto {
    val hostAll: List<Host> = conn.findAllHosts().getOrDefault(listOf())
    val vmAll = conn.findAllVms().getOrDefault(listOf())
    val hostCnt = this@toHostUsageDto.size

    // 총 메모리 계산
    val totalMemoryGB: Double = hostAll.sumOf { it.memoryAsLong()?.toDouble() ?: 0.0 } / GB

    // 사용 중인 메모리 계산
    val usedMemoryGB = this@toHostUsageDto
        .asSequence() // 시퀀스를 사용해 지연 평가로 효율성 증가
        .flatMap { conn.findAllStatisticsFromHost(it.id()).getOrDefault(listOf()).asSequence() }
        .filter { it.name() == "memory.used" }
        .sumOf { it.values().firstOrNull()?.datum()?.toDouble() ?: 0.0 } / GB

    val totalCpuCore: Int =
        hostAll.sumOf {
            if(it.cpu().topology().coresPresent())
            it.cpu().topology().coresAsInteger() *
                    it.cpu().topology().socketsAsInteger() *
                    it.cpu().topology().threadsAsInteger()
            else 0
        }

    val commitCpuCore: Int =
        vmAll.sumOf {
            if(it.cpuPresent()) {it.cpu().topology().coresAsInteger() * it.cpu().topology().socketsAsInteger() * it.cpu().topology().threadsAsInteger()}
            else 0
        }

    val usedCpuCore: Int =
        vmAll.filter { it.status() == VmStatus.UP }
            .sumOf {
                if(it.cpuPresent()) { it.cpu().topology().coresAsInteger() * it.cpu().topology().socketsAsInteger() * it.cpu().topology().threadsAsInteger() }
                else 0
            }

    val freeMemoryGB = totalMemoryGB - usedMemoryGB

    val (totalCpuUsage, totalMemoryUsage, historyTime) =
        hostSamplesHistoryEntities.toHostUsageDtos()
            .fold(Triple(0.0, 0.0, null as LocalDateTime?)) { acc, usage ->
                Triple(acc.first + usage.totalCpuUsagePercent, acc.second + usage.totalMemoryUsagePercent, usage.historyDatetime)
    }

    return HostUsageDto.builder {
        historyDatetime { historyTime }
        totalCpuUsagePercent { Math.round(totalCpuUsage / hostCnt).toDouble() }
        totalMemoryUsagePercent { Math.round(totalMemoryUsage / hostCnt).toDouble() }
        totalMemoryGB { totalMemoryGB }
        usedMemoryGB { usedMemoryGB }
        freeMemoryGB { freeMemoryGB }
        totalCpuCore { totalCpuCore }
        commitCpuCore { commitCpuCore }
        usedCpuCore { usedCpuCore }
    }
}

fun List<HostSamplesHistoryEntity>.toTotalCpuMemoryUsages(conn: Connection, host: Host): List<HostUsageDto>  {
    val hostName = host.name()
    return this@toTotalCpuMemoryUsages.map {
        HostUsageDto.builder {
            hostName { hostName }
            historyDatetime { it.historyDatetime }
            totalCpuUsagePercent { it.cpuUsagePercent.toDouble() }
            totalMemoryUsagePercent { it.memoryUsagePercent.toDouble() }
        }
    }
}
