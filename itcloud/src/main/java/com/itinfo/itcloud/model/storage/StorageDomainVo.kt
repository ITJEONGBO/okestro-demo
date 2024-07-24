package com.itinfo.itcloud.model.storage

import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.computing.HostVo
import com.itinfo.itcloud.model.gson
import com.itinfo.itcloud.service.storage.StorageServiceImpl
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.types.*
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.math.BigInteger

private val log = LoggerFactory.getLogger(StorageDomainVo::class.java)

/**
 * [StorageDomainVo]
 * 도메인
 *
 * @property id [String] 도메인
 * @property name [String] 도메인
 * @property description [String] 도메인
 * @property comment [String] 도메인
 *
 * @property active [Boolean] 상태: 활성화
 * @property status [StorageDomainStatus]
 * @property domainType [StorageDomainType] 도메인 유형
 * @property domainTypeMaster [Boolean]
 * @property availableSize [BigInteger] 여유공간
 * @property usedSize [BigInteger] 사용된 공간
 * @property commitedSize [BigInteger] 
 * @property diskSize [BigInteger] 전체공간
 * @property overCommit [BigInteger] 오버 활당 비율 (availableSize
 * @property imageCnt [Int] 이미지 디스크사이즈 
 * @property nfsVersion [String]
 * @property warning [Int] 디스크공간부족 경고
 * @property storagePath [String] 내보내기 경로
 * @property storageAddress [String]
 * @property blockSize [Int] 디스크공간 동작차단
 * @property format [StorageFormat]
 * @property storageType [StorageType] 스토리지 유형 
 * @property backup [Boolean]
 * @property logicalUnitId [String]
 * @property dataCenterVo [DataCenterVo]
 * @property hostVo [HostVo]
 * @property diskImageVos List<[DiskImageVo]>
 * @property diskProfileVos List<[DiskProfileVo]>
 */
class StorageDomainVo(
	val id: String = "",
	val name: String = "",
	val description: String = "",
	val comment: String = "",
	val active: Boolean = false,
	val status: StorageDomainStatus = StorageDomainStatus.UNKNOWN,
	val domainType: StorageDomainType = StorageDomainType.IMAGE,
	val domainTypeMaster: Boolean = false,
	val availableSize: BigInteger = BigInteger.ZERO,
	val usedSize: BigInteger = BigInteger.ZERO,
	val commitedSize: BigInteger = BigInteger.ZERO,
	val diskSize: BigInteger = BigInteger.ZERO,
	val overCommit: BigInteger = BigInteger.ZERO,
	val imageCnt: Int = 0,
	val nfsVersion: String = "",
	val warning: Int = 0,
	val storagePath: String = "",
	val storageAddress: String = "",
	val blockSize: Int = 0,
	val format: StorageFormat = StorageFormat.V1,
	val storageType: StorageType = StorageType.NFS,
	val backup: Boolean = false,
	val logicalUnitId: String = "",
	val dataCenterVo: DataCenterVo = DataCenterVo(),
	val hostVo: HostVo = HostVo(),
	val diskImageVos: List<DiskImageVo> = listOf(),
	val diskProfileVos: List<DiskProfileVo> = listOf(),
): Serializable {
	override fun toString(): String =
		gson.toJson(this)
	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
		private var bDescription: String = "";fun description(block: () -> String?) { bDescription = block() ?: "" }
		private var bComment: String = "";fun comment(block: () -> String?) { bComment = block() ?: "" }
		private var bActive: Boolean = false;fun active(block: () -> Boolean?) { bActive = block() ?: false }
		private var bStatus: StorageDomainStatus = StorageDomainStatus.UNKNOWN;fun status(block: () -> StorageDomainStatus?) { bStatus = block() ?: StorageDomainStatus.UNKNOWN }
		private var bDomainType: StorageDomainType = StorageDomainType.IMAGE;fun domainType(block: () -> StorageDomainType?) { bDomainType = block() ?: StorageDomainType.IMAGE }
		private var bDomainTypeMaster: Boolean = false;fun domainTypeMaster(block: () -> Boolean?) { bDomainTypeMaster = block() ?: false }
		private var bAvailableSize: BigInteger = BigInteger.ZERO;fun availableSize(block: () -> BigInteger?) { bAvailableSize = block() ?: BigInteger.ZERO }
		private var bUsedSize: BigInteger = BigInteger.ZERO;fun usedSize(block: () -> BigInteger?) { bUsedSize = block() ?: BigInteger.ZERO }
		private var bCommitedSize: BigInteger = BigInteger.ZERO;fun commitedSize(block: () -> BigInteger?) { bCommitedSize = block() ?: BigInteger.ZERO }
		private var bDiskSize: BigInteger = BigInteger.ZERO;fun diskSize(block: () -> BigInteger?) { bDiskSize = block() ?: BigInteger.ZERO }
		private var bOverCommit: BigInteger = BigInteger.ZERO;fun overCommit(block: () -> BigInteger?) { bOverCommit = block() ?: BigInteger.ZERO }
		private var bImageCnt: Int = 0;fun imageCnt(block: () -> Int?) { bImageCnt = block() ?: 0 }
		private var bNfsVersion: String = "";fun nfsVersion(block: () -> String?) { bNfsVersion = block() ?: "" }
		private var bWarning: Int = 0;fun warning(block: () -> Int?) { bWarning = block() ?: 0 }
		private var bStoragePath: String = "";fun storagePath(block: () -> String?) { bStoragePath = block() ?: ""}
		private var bStorageAddress: String = "";fun storageAddress(block: () -> String?) { bStorageAddress = block() ?: ""}
		private var bBlockSize: Int = 0;fun blockSize(block: () -> Int?) { bBlockSize = block() ?: 0 }
		private var bFormat: StorageFormat = StorageFormat.V1;fun format(block: () -> StorageFormat?) { bFormat = block() ?: StorageFormat.V1 }
		private var bStorageType: StorageType = StorageType.NFS;fun storageType(block: () -> StorageType?) { bStorageType = block() ?: StorageType.NFS }
		private var bBackup: Boolean = false;fun backup(block: () -> Boolean?) { bBackup = block() ?: false }
		private var bLogicalUnitId: String = "";fun logicalUnitId(block: () -> String?) { bLogicalUnitId = block() ?: "" }
		private var bDataCenterVo: DataCenterVo = DataCenterVo();fun dataCenterVo(block: () -> DataCenterVo?) { bDataCenterVo = block() ?: DataCenterVo() }
		private var bHostVo: HostVo = HostVo();fun hostVo(block: () -> HostVo?) { bHostVo = block() ?: HostVo() }
		private var bDiskImageVos: List<DiskImageVo> = listOf();fun diskImageVos(block: () -> List<DiskImageVo>?) { bDiskImageVos = block() ?: listOf() }
		private var bProfileVos: List<DiskProfileVo> = listOf();fun profileVos(block: () -> List<DiskProfileVo>?) { bProfileVos = block() ?: listOf() }

		fun build(): StorageDomainVo = StorageDomainVo(bId, bName, bDescription, bComment, bActive, bStatus, bDomainType, bDomainTypeMaster, bAvailableSize, bUsedSize, bCommitedSize, bDiskSize, bOverCommit, bImageCnt, bNfsVersion, bWarning, bStoragePath, bStorageAddress, bBlockSize, bFormat, bStorageType, bBackup, bLogicalUnitId, bDataCenterVo, bHostVo, bDiskImageVos, bProfileVos)
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


fun StorageDomain.toStorageDomainVo(conn: Connection, isActive: Boolean = false): StorageDomainVo {
	log.debug("StorageDomain.toStorageDomainVo ... ")
	val diskProfiles: List<DiskProfile> =
		conn.findAllDiskProfilesFromStorageDomain(this@toStorageDomainVo.id()).getOrDefault(listOf())
	val disks: List<Disk> = conn.findAllDisks().getOrDefault(listOf()).filter {
		it.storageDomainsPresent() && it.storageDomains().first().id() == this@toStorageDomainVo.id()
	}
	return StorageDomainVo.builder {
		id { this@toStorageDomainVo.id() }
		name { this@toStorageDomainVo.name() }
		active { isActive }
		description { this@toStorageDomainVo.description() }
		status { this@toStorageDomainVo.status() }
		comment { this@toStorageDomainVo.comment() }
		domainType { this@toStorageDomainVo.type() }
		domainTypeMaster { this@toStorageDomainVo.master() }
		storageType { this@toStorageDomainVo.storage().type() }
		format { this@toStorageDomainVo.storageFormat() }
		usedSize { this@toStorageDomainVo.used() }
		availableSize { this@toStorageDomainVo.available() }
		profileVos { diskProfiles.toDiskProfileVos()  }
		diskSize { this@toStorageDomainVo.available().add(this@toStorageDomainVo.used()) }
		diskSize {
			// TODO: 이거 처리 어떻게 해야하는지 확립필요
			if (this@toStorageDomainVo.usedPresent()) this@toStorageDomainVo.used().add(this@toStorageDomainVo.available())
			else this@toStorageDomainVo.available().add(this@toStorageDomainVo.available())
		}
		diskImageVos { disks.toDiskImageVos(conn) }
	}
}

fun List<StorageDomain>.toStorageDomainVos(conn: Connection): List<StorageDomainVo> =
	this@toStorageDomainVos.map { it.toStorageDomainVo(conn) }

// TODO: 스토리지 도메인 묶는 작업 못함
fun DiskAttachment.toStorageDomainVo(conn: Connection): StorageDomainVo? {
	val diskAttFromTemplate: DiskAttachment? =
		conn.findDiskAttachmentFromTemplate(this@toStorageDomainVo.id(), this@toStorageDomainVo.template().id()).getOrNull()
	val disk: Disk? = diskAttFromTemplate?.disk()?.id()?.let { conn.findDisk(it).getOrNull() }
	val storageDomain: StorageDomain? = disk?.storageDomains()?.firstOrNull()?.id()
		?.let { conn.findStorageDomain(it).getOrNull() }

	return storageDomain?.toStorageDomainVo(conn)
}

fun List<DiskAttachment>.toDiskAttachMentStorageDomainVos(conn: Connection): List<StorageDomainVo> =
	this@toDiskAttachMentStorageDomainVos.mapNotNull { it.toStorageDomainVo(conn) }

//TODO
fun StorageDomainVo.toStorageDomainBuilder(conn: Connection): StorageDomainBuilder {
	val storageDomainBuilder = StorageDomainBuilder()
	storageDomainBuilder
		.name(this@toStorageDomainBuilder.name)
		.type(this@toStorageDomainBuilder.domainType) // domaintype
		.host(HostBuilder().name(this@toStorageDomainBuilder.hostVo.name).build())
//			.discardAfterDelete()
//			.supportsDiscard()
//			.backup()
//			.wipeAfterDelete()

	if (this@toStorageDomainBuilder.storageType == StorageType.ISCSI) { // 스토리지 유형이 iscsi 일 경우
		return storageDomainBuilder
			.dataCenter(DataCenterBuilder().id(this@toStorageDomainBuilder.dataCenterVo.id).build())
			.storage(
				HostStorageBuilder().type(StorageType.ISCSI)
					.logicalUnits(LogicalUnitBuilder().id(this@toStorageDomainBuilder.logicalUnitId).build()).build()
			)
	} else { // 그외 다른 유형
		// 경로  예: myserver.mydomain.com:/my/local/path
		// paths[0] = address, paths[1] = path
		val paths = this@toStorageDomainBuilder.storagePath.split(":".toRegex(), limit = 2).toTypedArray()
		return storageDomainBuilder
			.storage(
				HostStorageBuilder().type(this@toStorageDomainBuilder.storageType).address(paths[0].trim { it <= ' ' })
					.path(paths[1].trim { it <= ' ' }).build()
			)
	}
}
