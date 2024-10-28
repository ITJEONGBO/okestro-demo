package com.itinfo.itcloud.repository.history.dto

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.computing.findCpuPercent
import com.itinfo.itcloud.model.computing.findMemoryPercent
import com.itinfo.itcloud.model.computing.findNetworkPercent
import com.itinfo.itcloud.ovirtDf
import com.itinfo.itcloud.repository.history.entity.HostSamplesHistoryEntity
import com.itinfo.itcloud.repository.history.entity.StorageDomainSamplesHistoryEntity
import com.itinfo.itcloud.repository.history.entity.VmSamplesHistoryEntity
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.findHost
import com.itinfo.util.ovirt.findStorageDomain
import com.itinfo.util.ovirt.findVm
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Host
import org.ovirt.engine.sdk4.types.Statistic
import org.ovirt.engine.sdk4.types.StorageDomain
import org.ovirt.engine.sdk4.types.Vm
import java.io.Serializable
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.Date

/**
 * [UsageDto]
 *
 * @property id [String]
 * @property name [String]
 * @property cpuPercent [Int]
 * @property memoryPercent [Int]
 * @property networkPercent [Int]
 * @property time [String]
 */
class UsageDto (
    val id: String = "",
    val name: String = "",
    val cpuPercent: Int = 0,
    val memoryPercent: Int = 0,
    var networkPercent: Int = 0,
    var time: LocalDateTime? = null,
//    var time: String = ""
): Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder {
        private var bId: String = ""; fun id(block: () -> String?) {bId = block() ?: ""}
        private var bName: String = ""; fun name(block: () -> String?) {bName = block() ?: ""}
        private var bCpuPercent: Int = 0; fun cpuPercent(block: () -> Int?) {bCpuPercent = block() ?: 0}
        private var bMemoryPercent: Int = 0; fun memoryPercent(block: () -> Int?) {bMemoryPercent = block() ?: 0}
        private var bNetworkPercent: Int = 0; fun networkPercent(block: () -> Int?) {bNetworkPercent = block() ?: 0}
        private var bTime: LocalDateTime? = null;fun time(block: () -> LocalDateTime?) { bTime = block() }
//        private var bTime: String = ""; fun time(block: () -> String?) {bTime = block() ?: ""}

        fun build(): UsageDto = UsageDto( bId, bName, bCpuPercent, bMemoryPercent, bNetworkPercent, bTime)
    }

    companion object {
        private val log by LoggerDelegate()
        inline fun builder(block: UsageDto.Builder.() -> Unit): UsageDto = UsageDto.Builder().apply(block).build()
    }
}

fun VmSamplesHistoryEntity.toVmCpuUsageDto(conn: Connection): UsageDto {
    val vm: Vm =
        conn.findVm(this@toVmCpuUsageDto.vmId.toString()).getOrNull()
            ?: throw ErrorPattern.VM_NOT_FOUND.toException()

    return UsageDto.builder {
        name { vm.name() }
        cpuPercent { this@toVmCpuUsageDto.cpuUsagePercent }
    }
}
fun List<VmSamplesHistoryEntity>.toVmCpuUsageDtos(conn: Connection): List<UsageDto> =
    this@toVmCpuUsageDtos.map { it.toVmCpuUsageDto(conn) }


fun VmSamplesHistoryEntity.toVmMemUsageDto(conn: Connection): UsageDto {
    val vm: Vm =
        conn.findVm(this@toVmMemUsageDto.vmId.toString()).getOrNull()
            ?: throw ErrorPattern.VM_NOT_FOUND.toException()
    return UsageDto.builder {
        name { vm.name() }
        memoryPercent { this@toVmMemUsageDto.memoryUsagePercent }
    }
}
fun List<VmSamplesHistoryEntity>.toVmMemUsageDtos(conn: Connection): List<UsageDto> =
    this@toVmMemUsageDtos.map { it.toVmMemUsageDto(conn) }


fun VmSamplesHistoryEntity.toVmUsageDto(conn: Connection): UsageDto {
    val vm: Vm =
        conn.findVm(this@toVmUsageDto.vmId.toString()).getOrNull()
            ?: throw ErrorPattern.VM_NOT_FOUND.toException()

    return UsageDto.builder {
        id { vm.id() }
        name { vm.name() }
        cpuPercent { this@toVmUsageDto.cpuUsagePercent }
        memoryPercent { this@toVmUsageDto.memoryUsagePercent }
        time { this@toVmUsageDto.historyDatetime }
//        time { ovirtDf.format(date) }
    }
}
fun List<VmSamplesHistoryEntity>.toVmUsageDtos(conn: Connection): List<UsageDto> =
    this@toVmUsageDtos.map { it.toVmUsageDto(conn) }


fun StorageDomainSamplesHistoryEntity.toStorageChart(conn: Connection): UsageDto {
    val storageDomain: StorageDomain =
        conn.findStorageDomain(this@toStorageChart.storageDomainId.toString())
            .getOrNull() ?: throw ErrorPattern.STORAGE_DOMAIN_NOT_FOUND.toException()
    val totalGB = (this@toStorageChart.availableDiskSizeGb + this@toStorageChart.usedDiskSizeGb).toDouble()
    return UsageDto.builder {
        id { storageDomain.id() }
        name { storageDomain.name() }
        memoryPercent { ((this@toStorageChart.usedDiskSizeGb / totalGB) * 100).toInt() }
    }
}
fun List<StorageDomainSamplesHistoryEntity>.toStorageCharts(conn: Connection) =
    this@toStorageCharts.map { it.toStorageChart(conn) }


fun HostSamplesHistoryEntity.toHostCpuChart(conn: Connection): UsageDto {
    val host: Host =
        conn.findHost(this@toHostCpuChart.hostId.toString())
            .getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toException()
    return UsageDto.builder {
        name { host.name() }
        cpuPercent { this@toHostCpuChart.cpuUsagePercent }
    }
}
fun List<HostSamplesHistoryEntity>.toHostCpuCharts(conn: Connection): List<UsageDto> =
    this@toHostCpuCharts.map { it.toHostCpuChart(conn) }

fun HostSamplesHistoryEntity.toHostMemChart(conn: Connection): UsageDto {
    val host: Host =
        conn.findHost(this@toHostMemChart.hostId.toString())
            .getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toException()
    return UsageDto.builder {
        name { host.name() }
        memoryPercent { this@toHostMemChart.memoryUsagePercent }
    }
}
fun List<HostSamplesHistoryEntity>.toHostMemCharts(conn: Connection): List<UsageDto> =
    this@toHostMemCharts.map { it.toHostMemChart(conn) }



/**
 * vm 사용량
 */
fun List<Statistic>.toVmUsage(): UsageDto{
    return UsageDto.builder {
        cpuPercent { this@toVmUsage.findCpuPercent() }
        memoryPercent { this@toVmUsage.findMemoryPercent() }
        networkPercent { this@toVmUsage.findNetworkPercent() }
    }
}

/**
 * vm cpuPercent 반환
 */
fun List<Statistic>.findCpuPercent(): Int? {
    val cpuUsageStatistic = this@findCpuPercent.firstOrNull { it.name() == "cpu.usage.history" }
    return cpuUsageStatistic?.values()?.firstOrNull()?.datum()?.toInt()
}
/**
 * vm MemoryPercent 반환
 */
fun List<Statistic>.findMemoryPercent(): Int? {
    val cpuUsageStatistic = this@findMemoryPercent.firstOrNull { it.name() == "memory.usage.history" }
    return cpuUsageStatistic?.values()?.firstOrNull()?.datum()?.toInt()
}
/**
 * vm NetworkPercent
 */
fun List<Statistic>.findNetworkPercent(): Int? {
    val cpuUsageStatistic = this@findNetworkPercent.firstOrNull { it.name() == "network.usage.history" }
    return cpuUsageStatistic?.values()?.firstOrNull()?.datum()?.toInt()
}
