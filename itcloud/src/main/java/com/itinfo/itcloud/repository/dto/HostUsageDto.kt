package com.itinfo.itcloud.repository.dto

import com.itinfo.itcloud.GB
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.repository.entity.HostSamplesHistoryEntity
import com.itinfo.util.ovirt.findAllHosts
import com.itinfo.util.ovirt.findAllStatisticsFromHost
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Host
import org.ovirt.engine.sdk4.types.Statistic
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
    val hostCnt = this@toHostUsageDto.size
    val total: Double = this@toHostUsageDto.sumOf { it.memoryAsLong()?.toDouble() ?: 0.0 } / GB

    val used: Double = this@toHostUsageDto
        .flatMap { conn.findAllStatisticsFromHost(it.id()).getOrDefault(listOf()) }
        .filter { "memory.used" == it.name() }
        .sumOf { stat: Statistic -> stat.values().firstOrNull()?.datum()?.toDouble() ?: 0.0 } / GB

//    val totalCpuCore: Int =
//        conn.findAllHosts()
//            .getOrDefault(listOf())
//            .filter { host ->
//                host.to
//            }

    val free = total - used
    var totalCpu = 0.0
    var totalMemory = 0.0
    var time: LocalDateTime? = null
    for (usage in hostSamplesHistoryEntities.toHostUsageDtos()) {
        totalCpu += usage.totalCpuUsagePercent
        totalMemory += usage.totalMemoryUsagePercent
        time = usage.historyDatetime
    }

    log.debug("totalCpuMemory ... ${totalCpu}, ${this@toHostUsageDto.size}")
    return HostUsageDto.builder {
        historyDatetime { time }
        totalCpuUsagePercent { Math.round(totalCpu / hostCnt).toDouble() }
        totalMemoryUsagePercent { Math.round(totalMemory / hostCnt).toDouble() }
        totalMemoryGB { total }
        usedMemoryGB { used }
        freeMemoryGB { free }
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
