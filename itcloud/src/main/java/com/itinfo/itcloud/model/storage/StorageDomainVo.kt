package com.itinfo.itcloud.model.storage

import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.fromDataCenterToIdentifiedVo
import com.itinfo.itcloud.model.fromDiskProfilesToIdentifiedVos
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.computing.toDataCenterVoInfo
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.types.*
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Arrays
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.math.BigInteger

private val log = LoggerFactory.getLogger(StorageDomainVo::class.java)

/**
 * [StorageDomainVo]
 * 도메인
 *
 * @property id [String]
 * @property name [String]
 * @property description [String]
 * @property comment [String]
 * @property status [StorageDomainStatus] 상태
 * @property domainType [StorageDomainType] 도메인 유형
 * @property domainTypeMaster [Boolean] 마스터 여부
 * @property hostedEngine [Boolean] 호스트 엔진 가상머신 데이터 포함
 * @property storageType [StorageType] 스토리지 유형
 * @property format [StorageFormat] 포맷
 * @property active [Boolean] 데이터 센터간 상태: 활성화
 * @property diskSize [BigInteger] 전체공간
 * @property availableSize [BigInteger] 여유공간
 * @property usedSize [BigInteger] 사용된 공간
 * @property commitedSize [BigInteger] 할당됨
 * @property overCommit [BigInteger] 오버 할당 비율 (availableSize)
 * @property image [Int] 이미지 디스크사이즈 //?
 * @property storagePath [String] 내보내기 path ip
 * @property storageAddress [String] 내보내기 경로
 * @property logicalUnits List<[String]> iscsi 사용시 필요 (id값만 들어감)
 * @property warning [Int] 디스크 공간 부족 경고  Warning Low Confirmed Space Indicator
 * @property spaceBlocker [Int] 디스크 공간 동작 차단 Critical Space Action Blocker
 * @property dataCenterVo [IdentifiedVo]
 * @property hostVo [IdentifiedVo]
 * @property diskImageVos List<[DiskImageVo]>
 * @property profileVos List<[IdentifiedVo]>
 * @property nfsVersion [String]  nfs 버전

 *
 */
class StorageDomainVo(
	val id: String = "",
	val name: String = "",
	val description: String = "",
	val comment: String = "",
	val status: StorageDomainStatus = StorageDomainStatus.UNKNOWN,
	val domainType: String = "", /*StorageDomainType.IMAGE*/
	val domainTypeMaster: Boolean = false,
	val hostedEngine: Boolean = false,
	val storageType: String = "", /*StorageType.NFS*/
	val format: StorageFormat = StorageFormat.V5,
	val active: Boolean = false,
	val diskSize: BigInteger = BigInteger.ZERO,
	val availableSize: BigInteger = BigInteger.ZERO,
	val usedSize: BigInteger = BigInteger.ZERO,
	val commitedSize: BigInteger = BigInteger.ZERO,
	val overCommit: BigInteger = BigInteger.ZERO,
	val image: Int = 0,
	val storagePath: String = "",
	val storageAddress: String = "",
	val logicalUnits: List<String> = listOf(),
	val warning: Int = 0,
	val spaceBlocker: Int = 0,
	val dataCenterVo: IdentifiedVo = IdentifiedVo(),
	val hostVo: IdentifiedVo = IdentifiedVo(),
	val diskImageVos: List<DiskImageVo> = listOf(),
	val profileVos: List<IdentifiedVo> = listOf(),
	val nfsVersion: String = "",
): Serializable {
	override fun toString(): String =
		gson.toJson(this)
	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
		private var bDescription: String = "";fun description(block: () -> String?) { bDescription = block() ?: "" }
		private var bComment: String = "";fun comment(block: () -> String?) { bComment = block() ?: "" }
		private var bStatus: StorageDomainStatus = StorageDomainStatus.UNKNOWN;fun status(block: () -> StorageDomainStatus?) { bStatus = block() ?: StorageDomainStatus.UNKNOWN }
		private var bDomainType: String = "";fun domainType(block: () -> String?) { bDomainType = block() ?: "" }
		private var bDomainTypeMaster: Boolean = false;fun domainTypeMaster(block: () -> Boolean?) { bDomainTypeMaster = block() ?: false }
		private var bHostedEngine: Boolean = false;fun hostedEngine(block: () -> Boolean?) { bHostedEngine = block() ?: false }
		private var bStorageType: String = "";fun storageType(block: () -> String?) { bStorageType = block() ?: "" }
		private var bFormat: StorageFormat = StorageFormat.V1;fun format(block: () -> StorageFormat?) { bFormat = block() ?: StorageFormat.V1 }
		private var bActive: Boolean = false;fun active(block: () -> Boolean?) { bActive = block() ?: false }
		private var bDiskSize: BigInteger = BigInteger.ZERO;fun diskSize(block: () -> BigInteger?) { bDiskSize = block() ?: BigInteger.ZERO }
		private var bAvailableSize: BigInteger = BigInteger.ZERO;fun availableSize(block: () -> BigInteger?) { bAvailableSize = block() ?: BigInteger.ZERO }
		private var bUsedSize: BigInteger = BigInteger.ZERO;fun usedSize(block: () -> BigInteger?) { bUsedSize = block() ?: BigInteger.ZERO }
		private var bCommitedSize: BigInteger = BigInteger.ZERO;fun commitedSize(block: () -> BigInteger?) { bCommitedSize = block() ?: BigInteger.ZERO }
		private var bOverCommit: BigInteger = BigInteger.ZERO;fun overCommit(block: () -> BigInteger?) { bOverCommit = block() ?: BigInteger.ZERO }
		private var bImage: Int = 0;fun image(block: () -> Int?) { bImage = block() ?: 0 }
		private var bStoragePath: String = "";fun storagePath(block: () -> String?) { bStoragePath = block() ?: ""}
		private var bStorageAddress: String = "";fun storageAddress(block: () -> String?) { bStorageAddress = block() ?: ""}
		private var bLogicalUnits: List<String> = listOf();fun logicalUnits(block: () -> List<String>?) { bLogicalUnits = block() ?: listOf() }
		private var bWarning: Int = 0;fun warning(block: () -> Int?) { bWarning = block() ?: 0 }
		private var bSpaceBlocker: Int = 0;fun spaceBlocker(block: () -> Int?) { bSpaceBlocker = block() ?: 0 }
		private var bDataCenterVo: IdentifiedVo = IdentifiedVo();fun dataCenterVo(block: () -> IdentifiedVo?) { bDataCenterVo = block() ?: IdentifiedVo() }
		private var bHostVo: IdentifiedVo = IdentifiedVo();fun hostVo(block: () -> IdentifiedVo?) { bHostVo = block() ?: IdentifiedVo() }
		private var bDiskImageVos: List<DiskImageVo> = listOf();fun diskImageVos(block: () -> List<DiskImageVo>?) { bDiskImageVos = block() ?: listOf() }
		private var bProfileVos: List<IdentifiedVo> = listOf();fun profileVos(block: () -> List<IdentifiedVo>?) { bProfileVos = block() ?: listOf() }
		private var bNfsVersion: String = "";fun nfsVersion(block: () -> String?) { bNfsVersion = block() ?: ""}

		fun build(): StorageDomainVo = StorageDomainVo(bId, bName, bDescription, bComment, bStatus, bDomainType, bDomainTypeMaster, bHostedEngine, bStorageType, bFormat, bActive, bDiskSize, bAvailableSize, bUsedSize, bCommitedSize, bOverCommit, bImage, bStoragePath, bStorageAddress, bLogicalUnits, bWarning, bSpaceBlocker, bDataCenterVo, bHostVo, bDiskImageVos, bProfileVos, bNfsVersion,)
	}
	
	companion object {
		inline fun builder(block: StorageDomainVo.Builder.() -> Unit): StorageDomainVo = StorageDomainVo.Builder().apply(block).build()
	}
}

fun StorageDomain.toStorageDomainIdName(): StorageDomainVo = StorageDomainVo.builder {
	id { this@toStorageDomainIdName.id() }
	name { this@toStorageDomainIdName.name() }
}
fun List<StorageDomain>.toStorageDomainIdNames(): List<StorageDomainVo> =
	this@toStorageDomainIdNames.map { it.toStorageDomainIdName() }

//
fun StorageDomain.toStorageDomainDataCenter(conn: Connection): StorageDomainVo {
	val dataCenter: DataCenter? =
		if (this@toStorageDomainDataCenter.dataCentersPresent() && this@toStorageDomainDataCenter.dataCenters().isNotEmpty())
			conn.findDataCenter(this@toStorageDomainDataCenter.dataCenters().first().id(), "storagedomains").getOrNull()
		else null
	val s = dataCenter?.storageDomains()?.find { it.id() == this@toStorageDomainDataCenter.id() }

	return StorageDomainVo.builder {
		id { this@toStorageDomainDataCenter.id() }
		name { this@toStorageDomainDataCenter.name() }
		status { s?.status() }
		dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
	}
}


fun StorageDomain.toDomainStatus(conn: Connection): StorageDomainVo {
	val dataCenter: DataCenter? =
		if(this@toDomainStatus.dataCenterPresent())
			conn.findDataCenter(this@toDomainStatus.dataCenter().id()).getOrNull()
		else null

	return StorageDomainVo.builder {
		status { this@toDomainStatus.status() }
	}
}
fun List<StorageDomain>.toDomainStatuss(conn: Connection): List<StorageDomainVo> =
	this@toDomainStatuss.map { it.toDomainStatus(conn) }



fun StorageDomain.toStorageDomainMenu(conn: Connection): StorageDomainVo {
	val dataCenter: DataCenter? =
		if(this@toStorageDomainMenu.dataCentersPresent())
			conn.findDataCenter(this@toStorageDomainMenu.dataCenters().first().id()).getOrNull()
		else null
	val s: StorageDomain? =
		dataCenter?.let { conn.findAttachedStorageDomainFromDataCenter(it.id(), this@toStorageDomainMenu.id()).getOrNull() }
	val hostedVm =
		conn.findAllVmsFromStorageDomain(this@toStorageDomainMenu.id()).getOrDefault(listOf())
			.any { it.name() == "HostedEngine" }

	return StorageDomainVo.builder {
		id { this@toStorageDomainMenu.id() }
		name { this@toStorageDomainMenu.name() }
		description { this@toStorageDomainMenu.description() }
		status { s?.status() }
		hostedEngine { hostedVm }
		comment { this@toStorageDomainMenu.comment() }
		domainType { this@toStorageDomainMenu.type().value() }
		domainTypeMaster {
			if (this@toStorageDomainMenu.masterPresent()) this@toStorageDomainMenu.master()
			else false
		}
		storageType {
			if (this@toStorageDomainMenu.storagePresent()) this@toStorageDomainMenu.storage().type().value()
			else null
		}
		format { this@toStorageDomainMenu.storageFormat() }
		usedSize { this@toStorageDomainMenu.used() }
		availableSize { this@toStorageDomainMenu.available() }
		diskSize { this@toStorageDomainMenu.available().add(this@toStorageDomainMenu.used()) }
		dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
	}
}
fun List<StorageDomain>.toStorageDomainsMenu(conn: Connection): List<StorageDomainVo> =
	this@toStorageDomainsMenu.map { it.toStorageDomainMenu(conn) }

fun StorageDomain.toActiveDomain(conn: Connection): StorageDomainVo {
	return StorageDomainVo.builder {
		id { this@toActiveDomain.id() }
		name { this@toActiveDomain.name() }
		usedSize { this@toActiveDomain.used() }
		availableSize { this@toActiveDomain.available() }
		diskSize { this@toActiveDomain.available().add(this@toActiveDomain.used()) }
	}
}
fun List<StorageDomain>.toActiveDomains(conn: Connection): List<StorageDomainVo> =
	this@toActiveDomains.map { it.toActiveDomain(conn)}


fun StorageDomain.toStorageDomainVo(conn: Connection): StorageDomainVo {
	val diskProfiles: List<DiskProfile> =
		conn.findAllDiskProfilesFromStorageDomain(this@toStorageDomainVo.id())
			.getOrDefault(listOf())
	val disks: List<Disk> =
		conn.findAllDisks()
			.getOrDefault(listOf())
			.filter { it.storageDomainsPresent() && it.storageDomains().first().id() == this@toStorageDomainVo.id()
	}
	val dataCenter: DataCenter? =
		if(this@toStorageDomainVo.dataCentersPresent()) conn.findDataCenter(this@toStorageDomainVo.dataCenters().first().id()).getOrNull()
		else null

	return StorageDomainVo.builder {
		id { this@toStorageDomainVo.id() }
		name { this@toStorageDomainVo.name() }
		description { this@toStorageDomainVo.description() }
//		status { status }
		comment { this@toStorageDomainVo.comment() }
		domainType { this@toStorageDomainVo.type().value() }
		domainTypeMaster { if(this@toStorageDomainVo.masterPresent()) this@toStorageDomainVo.master() else false}
		storageType { if(this@toStorageDomainVo.storagePresent()) this@toStorageDomainVo.storage().type().value() else null }
		format { this@toStorageDomainVo.storageFormat() }
		usedSize { this@toStorageDomainVo.used() }
		availableSize { this@toStorageDomainVo.available() }
		commitedSize { this@toStorageDomainVo.committed() }
		profileVos { diskProfiles.fromDiskProfilesToIdentifiedVos()  }
		diskSize {
//			 TODO: 이거 처리 어떻게 해야하는지 확립필요
			if (this@toStorageDomainVo.availablePresent()) this@toStorageDomainVo.available().add(this@toStorageDomainVo.used())
			else BigInteger.ZERO
		}
		diskImageVos { disks.toDiskImageVos(conn) }
		dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
		warning { this@toStorageDomainVo.warningLowSpaceIndicatorAsInteger() }
		spaceBlocker { this@toStorageDomainVo.criticalSpaceActionBlockerAsInteger() }
		storageAddress { this@toStorageDomainVo.storage().address() + this@toStorageDomainVo.storage().path() } // 경로
		nfsVersion { this@toStorageDomainVo.storage().nfsVersion().value() }

	}
}
fun List<StorageDomain>.toStorageDomainVos(conn: Connection): List<StorageDomainVo> =
	this@toStorageDomainVos.map { it.toStorageDomainVo(conn) }


fun StorageDomain.toStorageDomainSize(): StorageDomainVo {
	return StorageDomainVo.builder {
		id { this@toStorageDomainSize.id() }
		name { this@toStorageDomainSize.name() }
		usedSize { this@toStorageDomainSize.used() }
		availableSize { this@toStorageDomainSize.available() }
	}
}
fun List<StorageDomain>.toStorageDomainSizes(): List<StorageDomainVo> =
	this@toStorageDomainSizes.map { it.toStorageDomainSize() }



// region: 빌더
/**
 * 스토리지 도메인 생성 빌더
 * 기본
 */
fun StorageDomainVo.toAddStorageDomainBuilder(): StorageDomain {
	return StorageDomainBuilder()
		.name(this@toAddStorageDomainBuilder.name)
		.type(StorageDomainType.fromValue(this@toAddStorageDomainBuilder.domainType))
		.description(this@toAddStorageDomainBuilder.description)
		.warningLowSpaceIndicator(this@toAddStorageDomainBuilder.warning)
		.criticalSpaceActionBlocker(this@toAddStorageDomainBuilder.spaceBlocker)  //디스크 공간 동작 차단
		.dataCenters(*arrayOf(DataCenterBuilder().id(this@toAddStorageDomainBuilder.dataCenterVo.id).build()))
		.host(HostBuilder().name(this@toAddStorageDomainBuilder.hostVo.name).build())
		.storage(
			if(StorageType.fromValue(this@toAddStorageDomainBuilder.storageType) == StorageType.NFS) {
				this@toAddStorageDomainBuilder.toAddNFSBuilder()
			} else { // ISCSI, Fibre Channel
				this@toAddStorageDomainBuilder.toAddEtcBuilder()
			}
		).build()
}

/**
 * NFS
 */
fun StorageDomainVo.toAddNFSBuilder(): HostStorage {
	return HostStorageBuilder()
			.address(this@toAddNFSBuilder.storageAddress)
//			.nfsVersion(NfsVersion.AUTO)
			.path(this@toAddNFSBuilder.storagePath)
			.type(StorageType.fromValue(this@toAddNFSBuilder.storageType))
	.build()
}

/**
 * ISCSI, Fibre Channel
 */
fun StorageDomainVo.toAddEtcBuilder(): HostStorage {
	return HostStorageBuilder()
		.type(StorageType.fromValue(this@toAddEtcBuilder.storageType))
			.logicalUnits(this@toAddEtcBuilder.logicalUnits.map {
				LogicalUnitBuilder().id(it).build()
			})
	.build()
}

/**
 * 스토리지 도메인 편집 빌더
 * 기본
 */
fun StorageDomainVo.toEditStorageDomainBuilder(): StorageDomain {
	return StorageDomainBuilder()
		.id(this@toEditStorageDomainBuilder.id)
		.name(this@toEditStorageDomainBuilder.name)
		.description(this@toEditStorageDomainBuilder.description)
		.warningLowSpaceIndicator(this@toEditStorageDomainBuilder.warning)
		.criticalSpaceActionBlocker(this@toEditStorageDomainBuilder.spaceBlocker)
		.build()
}

// endregion