package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.model.storage.*
import com.itinfo.util.ovirt.*
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
 * @property clusterVos List<[IdentifiedVo]>
 * @property networkVos List<[IdentifiedVo]>
 * @property storageDomainVos List<[StorageDomainVo]>
 * @property clusterCnt [Int]
 * @property hostCnt [Int]
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
	val storageDomainVos: List<StorageDomainVo> = listOf(),
	val clusterCnt: Int = 0,
	val hostCnt: Int = 0,
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
		private var bStorageDomainVos: List<StorageDomainVo> = listOf();fun storageDomainVos(block: () -> List<StorageDomainVo>?) { bStorageDomainVos = block() ?: listOf() }
		private var bClusterCnt: Int = 0; fun clusterCnt(block: () -> Int?) { bClusterCnt = block() ?: 0 }
		private var bHostCnt: Int = 0; fun hostCnt(block: () -> Int?) { bHostCnt = block() ?: 0 }

		fun build(): DataCenterVo = DataCenterVo(bId, bName, bComment, bDescription, bStorageType, bQuotaMode, bStatus, bVersion, bClusterVos, bNetworkVos, bStorageDomainVos, bClusterCnt, bHostCnt)
	}

	companion object {
		inline fun builder(block: DataCenterVo.Builder.() -> Unit): DataCenterVo = DataCenterVo.Builder().apply(block).build()
	}
}

/**
 * 단순 데이터센터 이름, 아이디 출력
 */
fun DataCenter.toDataCenterIdName(): DataCenterVo = DataCenterVo.builder {
	id { this@toDataCenterIdName.id() }
	name { this@toDataCenterIdName.name() }
}
fun List<DataCenter>.toDataCenterIdNames(): List<DataCenterVo> =
	this@toDataCenterIdNames.map { it.toDataCenterIdName() }

/**
 * 데이터센터 목록
 */
fun DataCenter.toDataCenterMenu(conn: Connection): DataCenterVo {
	val clusterSize = conn.findAllClustersFromDataCenter(this@toDataCenterMenu.id()).getOrDefault(listOf()).size
	val hostSize = conn.findAllHostsFromDataCenter(this@toDataCenterMenu.id()).getOrDefault(listOf()).size

	return DataCenterVo.builder {
		id { this@toDataCenterMenu.id() }
		name { this@toDataCenterMenu.name() }
		comment { this@toDataCenterMenu.comment() }
		description { this@toDataCenterMenu.description() }
		status { this@toDataCenterMenu.status() }
		version { this@toDataCenterMenu.version().major().toString() + "." + this@toDataCenterMenu.version().minor() }
		storageType { this@toDataCenterMenu.local() }
		clusterCnt { clusterSize }
		hostCnt { hostSize }
	}
}
fun List<DataCenter>.toDataCentersMenu(conn: Connection): List<DataCenterVo> =
	this@toDataCentersMenu.map { it.toDataCenterMenu(conn) }


/**
 * 데이터센터 정보(편집창)
 */
fun DataCenter.toDataCenterVoInfo(): DataCenterVo = DataCenterVo.builder {
	id { this@toDataCenterVoInfo.id() }
	name { this@toDataCenterVoInfo.name() }
	comment { this@toDataCenterVoInfo.comment() }
	description { this@toDataCenterVoInfo.description() }
	storageType { this@toDataCenterVoInfo.local() }
	quotaMode { this@toDataCenterVoInfo.quotaMode() }
	version { this@toDataCenterVoInfo.version().major().toString() + "." + this@toDataCenterVoInfo.version().minor() }
	status { this@toDataCenterVoInfo.status() }
}


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
		conn?.findAllStorageDomains()?.getOrDefault(listOf())
			?.filter { it.dataCentersPresent() && it.dataCenters().first().id() == this@toDataCenterVo.id() } ?: listOf()
	val clusters: List<Cluster> = (conn?.findAllClusters()?.getOrDefault(listOf()))?.filter {
		it.dataCenterPresent() && it.dataCenter().id() == this@toDataCenterVo.id()
	} ?: listOf()

	val storageDomainVoList: List<StorageDomainVo> = if (conn == null || !findStorageDomains) listOf() else storageDomains.toStorageDomainIdNames()
//	val storageDomainVos: List<IdentifiedVo> = if (conn == null || !findStorageDomains) listOf() else storageDomains.fromStorageDomainsToIdentifiedVos()
	val networkVos: List<IdentifiedVo> = if (conn == null || !findNetworks) listOf() else networks.fromNetworksToIdentifiedVos()
	val clusterVos: List<IdentifiedVo> = if (conn == null || !findClusters) listOf() else clusters.fromClustersToIdentifiedVos()

	return DataCenterVo.builder {
		id { this@toDataCenterVo.id() }
		name { this@toDataCenterVo.name() }
		storageType { this@toDataCenterVo.local() }
		clusterVos { clusterVos }
		networkVos { networkVos }
		storageDomainVos { storageDomainVoList }
	}
}
fun List<DataCenter>.toDataCenterVos(
	conn: Connection?,
	findClusters: Boolean = true,
	findNetworks: Boolean = true,
	findStorageDomains: Boolean = true
): List<DataCenterVo> =
	this@toDataCenterVos.map { it.toDataCenterVo(conn, findClusters, findNetworks, findStorageDomains) }


// region: builder

/**
 * 데이터센터 빌더
 */
fun DataCenterVo.toDataCenterBuilder(): DataCenterBuilder =
	DataCenterBuilder()
		.name(this@toDataCenterBuilder.name) // 이름
		.description(this@toDataCenterBuilder.description) // 설명
		.local(this@toDataCenterBuilder.storageType) // 스토리지 유형
		.version(VersionBuilder().major(4).minor(7))
		.quotaMode(this@toDataCenterBuilder.quotaMode)
		.comment(this@toDataCenterBuilder.comment)

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

// endregion
