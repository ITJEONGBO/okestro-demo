package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.*
import com.itinfo.util.ovirt.findAllClusters
import com.itinfo.util.ovirt.findAllNetworks
import com.itinfo.util.ovirt.findAllStorageDomains
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.DataCenterBuilder
import org.ovirt.engine.sdk4.builders.VersionBuilder
import org.ovirt.engine.sdk4.types.*
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(DataCenterVo::class.java)

/**
 * [DataCenterVo]
 * 데이터센터
 *
 * @property id [String] 
 * @property name [String] 
 * @property comment [String] 코멘트
 * @property description [String] 설명
 * @property storageType [Boolean] 스토리지 유형(공유됨, 로컬)   api에 local로 표시됨
 * @property quotaMode [QuotaModeType] 쿼터모드(비활성화됨, 감사, 강제적용)
 * @property status [DataCenterStatus] 상태(contend, maintenance, not_operational, problematic, uninitialized, up)
 * @property version [String]
 *
 * <link>
 * @property clusterVos List<[IdentifiedVo]>
 * @property networkVos List<[IdentifiedVo]>
 * @property storageDomainVos List<[IdentifiedVo]>
 * 
 */
class DataCenterVo (
	val id: String = "",
	val name: String = "",
	val comment: String = "",
	val description: String = "",
	val storageType: Boolean = false,
	val quotaMode: QuotaModeType = QuotaModeType.DISABLED,
	val status: DataCenterStatus = DataCenterStatus.NOT_OPERATIONAL,
	val version: String = "",
	val clusterVos: List<IdentifiedVo> = listOf(),
	val networkVos: List<IdentifiedVo> = listOf(),
	val storageDomainVos: List<IdentifiedVo> = listOf()

): Serializable {
	override fun toString(): String =
		gson.toJson(this)
		
	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
		private var bComment: String = "";fun comment(block: () -> String?) { bComment = block() ?: "" }
		private var bDescription: String = "";fun description(block: () -> String?) { bDescription = block() ?: "" }
		private var bStorageType: Boolean = false;fun storageType(block: () -> Boolean?) { bStorageType = block() ?: false }
		private var bQuotaMode: QuotaModeType = QuotaModeType.DISABLED;fun quotaMode(block: () -> QuotaModeType?) { bQuotaMode = block() ?: QuotaModeType.DISABLED }
		private var bStatus: DataCenterStatus = DataCenterStatus.NOT_OPERATIONAL;fun status(block: () -> DataCenterStatus?) { bStatus = block() ?: DataCenterStatus.NOT_OPERATIONAL }
		private var bVersion: String = "";fun version(block: () -> String?) { bVersion = block() ?: "" }
		private var bClusterVos: List<IdentifiedVo> = listOf();fun clusterVos(block: () -> List<IdentifiedVo>?) { bClusterVos = block() ?: listOf() }
		private var bNetworkVos: List<IdentifiedVo> = listOf();fun networkVos(block: () -> List<IdentifiedVo>?) { bNetworkVos = block() ?: listOf() }
		private var bStorageDomainVos: List<IdentifiedVo> = listOf();fun storageDomainVos(block: () -> List<IdentifiedVo>?) { bStorageDomainVos = block() ?: listOf() }
//		private var bClusterVos: List<ClusterVo> = listOf();fun clusterVos(block: () -> List<ClusterVo>?) { bClusterVos = block() ?: listOf() }
//		private var bNetworkVos: List<NetworkVo> = listOf();fun networkVos(block: () -> List<NetworkVo>?) { bNetworkVos = block() ?: listOf() }
//		private var bStorageDomainVos: List<StorageDomainVo> = listOf();fun storageDomainVos(block: () -> List<StorageDomainVo>?) { bStorageDomainVos = block() ?: listOf() }

		fun build(): DataCenterVo = DataCenterVo(bId, bName, bComment, bDescription, bStorageType, bQuotaMode, bStatus, bVersion, bClusterVos, bNetworkVos, bStorageDomainVos)
	}

	companion object {
		inline fun builder(block: DataCenterVo.Builder.() -> Unit): DataCenterVo = DataCenterVo.Builder().apply(block).build()
	}
}

fun DataCenter.toDataCenterIdName(): DataCenterVo = DataCenterVo.builder {
	id { this@toDataCenterIdName.id() }
	name { this@toDataCenterIdName.name() }
}

fun List<DataCenter>.toDataCenterIdNames(): List<DataCenterVo> =
	this@toDataCenterIdNames.map { it.toDataCenterIdName() }

// TODO ERROR
fun DataCenter.toDataCenterVo(
	conn: Connection?,
	findNetworks: Boolean = true,
	findStorageDomains: Boolean = true,
	findClusters: Boolean = true
): DataCenterVo {
	log.debug("DataCenter.toDataCenterVo ... findNetworks: {}, findStorageDomains: {}, findClusters: {}", findNetworks, findStorageDomains, findClusters)
	val networks: List<Network> = (conn?.findAllNetworks()?.getOrNull() ?: listOf()).filter {
		it.dataCenter().id() == this@toDataCenterVo.id()
	}
	val storageDomains: List<StorageDomain> =
		conn?.findAllStorageDomains()
			?.getOrDefault(listOf())
			?.filter { it.dataCentersPresent() && it.dataCenters().first().id() == this@toDataCenterVo.id() } ?: listOf()
	val clusters: List<Cluster> = (conn?.findAllClusters()?.getOrDefault(listOf()))?.filter {
		it.dataCenterPresent() && it.dataCenter().id() == this@toDataCenterVo.id()
	} ?: listOf()

	val storageDomainVos: List<IdentifiedVo> = if (conn == null || !findStorageDomains) listOf() else storageDomains.fromStorageDomainsToIdentifiedVos()
	val networkVos: List<IdentifiedVo> = if (conn == null || !findNetworks) listOf() else networks.fromNetworksToIdentifiedVos()
	val clusterVos: List<IdentifiedVo> = if (conn == null || !findClusters) listOf() else clusters.fromClustersToIdentifiedVos()

//	val storageDomainVos: List<StorageDomainVo> = if (conn == null || !findStorageDomains) listOf() else storageDomains.toStorageDomainVos(conn)
//	val networkVos: List<NetworkVo> = if (conn == null || !findNetworks) listOf() else networks.toNetworkVos(conn)
//	val clusterVos: List<ClusterVo> = if (conn == null || !findClusters) listOf() else clusters.toClusterVos(conn)

	return DataCenterVo.builder {
		id { this@toDataCenterVo.id() }
		name { this@toDataCenterVo.name() }
		storageType { this@toDataCenterVo.local() }
		clusterVos { clusterVos }
		networkVos { networkVos }
		storageDomainVos { storageDomainVos }
	}
}

fun List<DataCenter>.toDataCenterVos(
	conn: Connection?,
	findClusters: Boolean = true,
	findNetworks: Boolean = true,
	findStorageDomains: Boolean = true
): List<DataCenterVo> =
	this@toDataCenterVos.map { it.toDataCenterVo(conn, findClusters, findNetworks, findStorageDomains) }


/**
 * 데이터센터 목록 & 편집창
 */
fun DataCenter.toDataCenterVoInfo(): DataCenterVo {
	return DataCenterVo.builder {
		id { this@toDataCenterVoInfo.id() }
		name { this@toDataCenterVoInfo.name() }
		comment { this@toDataCenterVoInfo.comment() }
		description { this@toDataCenterVoInfo.description() }
		storageType { this@toDataCenterVoInfo.local() }
		status { this@toDataCenterVoInfo.status() }
		quotaMode { this@toDataCenterVoInfo.quotaMode() }
//		version { this@toDataCenterVoInfo.version().fullVersion() }
		version { this@toDataCenterVoInfo.version().major().toString() + "." + this@toDataCenterVoInfo.version().minor() }
	}
}

/**
 * 데이터센터 목록
 */
fun List<DataCenter>.toDataCenterVoInfos(): List<DataCenterVo> =
	this@toDataCenterVoInfos.map { it.toDataCenterVoInfo() }

/**
 * 데이터센터 빌더
 */
fun DataCenterVo.toDataCenterBuilder(): DataCenterBuilder {
	val ver = this@toDataCenterBuilder.version.split(".")
	if (ver.size < 2) throw IllegalArgumentException("잘못된 버전정보 입력")

	return DataCenterBuilder()
		.name(this@toDataCenterBuilder.name) // 이름
		.description(this@toDataCenterBuilder.description) // 설명
		.local(this@toDataCenterBuilder.storageType) // 스토리지 유형
		.version(VersionBuilder().major(ver[0].toInt()).minor(ver[1].toInt()).build())
		.quotaMode(this@toDataCenterBuilder.quotaMode)
		.comment(this@toDataCenterBuilder.comment)
}

/**
 * 데이터센터 생성 빌더
 */
fun DataCenterVo.toAddDataCenterBuilder(): DataCenter =
	this@toAddDataCenterBuilder.toDataCenterBuilder().build()

/**
 * 데이터센터 편집 빌더
 */
fun DataCenterVo.toEditDataCenterBuilder(): DataCenter =
	this@toEditDataCenterBuilder.toDataCenterBuilder().id(this@toEditDataCenterBuilder.id).build()
