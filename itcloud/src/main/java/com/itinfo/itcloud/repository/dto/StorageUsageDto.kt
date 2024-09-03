package com.itinfo.itcloud.repository.dto

import com.itinfo.itcloud.GB
import com.itinfo.itcloud.gson
import com.itinfo.util.ovirt.findAllStorageDomains
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.StorageDomain
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.time.LocalDateTime

private val log = LoggerFactory.getLogger(StorageUsageDto::class.java)

/**
 * [StorageUsageDto]
 * 
 * @property storageDomainId [String]
 * @property storageDomainName [String]
 * @property historyDatetime [LocalDateTime]
 * @property usedPercent [Double] 사용하고 있는 사용량 %
 * @property totalGB [Double]
 * @property usedGB [Double]
 * @property freeGB [Double]
 */
class StorageUsageDto(
	// 전체 사용량 용도
	val storageDomainId: String = "",
	val storageDomainName: String = "",
	val historyDatetime: LocalDateTime? = null,
	val usedPercent: Double = 0.0,
	val totalGB: Double = 0.0,
	val usedGB: Double = 0.0,
	val freeGB: Double = 0.0,
): Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder {
		private var bStorageDomainId: String = "";fun storageDomainId(block: () -> String?) { bStorageDomainId = block() ?: "" }
		private var bStorageDomainName: String = "";fun storageDomainName(block: () -> String?) { bStorageDomainName = block() ?: "" }
		private var bHistoryDatetime: LocalDateTime? = null;fun historyDatetime(block: () -> LocalDateTime?) { bHistoryDatetime = block() }
		private var bUsedPercent: Double = 0.0;fun usedPercent(block: () -> Double?) { bUsedPercent = block() ?: 0.0 }
		private var bTotalGB: Double = 0.0;fun totalGB(block: () -> Double?) { bTotalGB = block() ?: 0.0 }
		private var bUsedGB: Double = 0.0;fun usedGB(block: () -> Double?) { bUsedGB = block() ?: 0.0 }
		private var bFreeGB: Double = 0.0;fun freeGB(block: () -> Double?) { bFreeGB = block() ?: 0.0 }
        fun build(): StorageUsageDto = StorageUsageDto(bStorageDomainId, bStorageDomainName, bHistoryDatetime, bUsedPercent, bTotalGB, bUsedGB, bFreeGB)
    }

    companion object {
        inline fun builder(block: StorageUsageDto.Builder.() -> Unit): StorageUsageDto = StorageUsageDto.Builder().apply(block).build()
    }
}

fun List<StorageDomain>.toStorageUsageDto(conn: Connection): StorageUsageDto {
	val storageDomains: List<StorageDomain> =
		conn.findAllStorageDomains()
			.getOrDefault(listOf())
			.filter { it.availablePresent() }
	val free: Double = storageDomains.filter { it.availablePresent() }.sumOf { it.availableAsLong()?.toDouble() ?: 0.0 } / GB
	val used: Double = storageDomains.filter { it.usedPresent() }.sumOf { it.usedAsLong()?.toDouble() ?: 0.0 } / GB

	return StorageUsageDto.builder {
		totalGB { free + used }
		freeGB { free }
		usedGB { used }
		usedPercent { used / (free + used) * 100 }
	}
}