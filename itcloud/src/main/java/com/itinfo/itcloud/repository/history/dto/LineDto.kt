package com.itinfo.itcloud.repository.history.dto

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.computing.findNetworkListPercent
import com.itinfo.itcloud.repository.history.entity.VmSamplesHistoryEntity
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.findAllStatisticsFromVm
import com.itinfo.util.ovirt.findVm
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Vm
import java.io.Serializable
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class LineDto (
    val name: String = "",
    val dataList: List<Int> = listOf(),
    var time: List<LocalDateTime> = listOf(),
): Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder {
        private var bName: String = ""; fun name(block: () -> String?) {bName = block() ?: ""}
        private var bDataList: List<Int> = listOf(); fun dataList(block: () -> List<Int>?) {bDataList = block() ?: listOf()}
        private var bTime: List<LocalDateTime> = listOf();fun time(block: () -> List<LocalDateTime>?) { bTime = block() ?: listOf() }

        fun build(): LineDto = LineDto(bName, bDataList, bTime)
    }

    companion object {
        private val log by LoggerDelegate()
        inline fun builder(block: LineDto.Builder.() -> Unit): LineDto = LineDto.Builder().apply(block).build()
    }
}

fun List<VmSamplesHistoryEntity>.toVmCpuLineDtos(conn: Connection): List<LineDto> {
    val vmDataMap = mutableMapOf<String, MutableList<Int>>()
    val vmTimeMap = mutableMapOf<String, MutableList<LocalDateTime>>()

    this@toVmCpuLineDtos.forEach {
        vmDataMap.computeIfAbsent(it.vmId.toString()) { mutableListOf() }.add(it.cpuUsagePercent)
        vmTimeMap.computeIfAbsent(it.vmId.toString()) { mutableListOf() }.add(it.historyDatetime)
    }

    return vmDataMap.map { (vmId, dataList) ->
        val vm: Vm =
            conn.findVm(vmId).getOrNull()
                ?: throw ErrorPattern.VM_NOT_FOUND.toException()
        LineDto.builder {
            name { vm.name() }
            dataList { dataList }
            time { vmTimeMap[vmId] ?: listOf() }
        }
    }
}

fun List<VmSamplesHistoryEntity>.toVmMemoryLineDtos(conn: Connection): List<LineDto> {
    val vmDataMap = mutableMapOf<String, MutableList<Int>>()
    val vmTimeMap = mutableMapOf<String, MutableList<LocalDateTime>>()

    this@toVmMemoryLineDtos.forEach {
        vmDataMap.computeIfAbsent(it.vmId.toString()) { mutableListOf() }.add(it.memoryUsagePercent)
        vmTimeMap.computeIfAbsent(it.vmId.toString()) { mutableListOf() }.add(it.historyDatetime)
    }

    return vmDataMap.map { (vmId, dataList) ->
        val vm: Vm = conn.findVm(vmId).getOrNull()
                ?: throw ErrorPattern.VM_NOT_FOUND.toException()
        LineDto.builder {
            name { vm.name() }
            dataList { dataList }
            time { vmTimeMap[vmId] ?: listOf() }
        }
    }
}

fun Vm.toVmNetworkLineDto(conn: Connection): LineDto {
    val statistics = conn.findAllStatisticsFromVm(this.id())
    return LineDto.builder {
        name { this@toVmNetworkLineDto.name() }
        dataList { statistics.findNetworkListPercent() }
    }
}
fun List<Vm>.toVmNetworkLineDtos(conn: Connection): List<LineDto> =
    this@toVmNetworkLineDtos.map { it.toVmNetworkLineDto(conn) }