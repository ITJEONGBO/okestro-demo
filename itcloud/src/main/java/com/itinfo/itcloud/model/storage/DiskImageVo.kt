package com.itinfo.itcloud.model.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.repository.engine.entity.DiskVmElementEntity
import com.itinfo.itcloud.repository.engine.entity.toVmId
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.DiskBuilder
import org.ovirt.engine.sdk4.builders.DiskProfileBuilder
import org.ovirt.engine.sdk4.builders.StorageDomainBuilder
import org.ovirt.engine.sdk4.types.*
import java.io.Serializable
import java.math.BigInteger

/**
 * [DiskImageVo]
 * 스토리지도메인 - 디스크 이미지 생성
 * 가상머신 - 생성 - 인스턴스 이미지 - 생성
 *
 * @property id [String] 디스크 id
 * @property size [Int] 크기(Gib)
 * @property appendSize [Int] 확장크기
 * @property alias [String] 별칭(이름과 같음)
 * @property description [String] 설명
 * @property dataCenterVo [IdentifiedVo] <스토리지-디스크-생성>
 * @property storageDomainVo [IdentifiedVo] 스토리지 도메인
 * @property sparse [Boolean] 할당 정책 (씬 true, 사전할당 false)
 * @property diskProfileVo [IdentifiedVo] 디스크 프로파일 (스토리지-디스크프로파일)
 * @property wipeAfterDelete [Boolean] 삭제 후 초기화
 * @property sharable [Boolean] 공유가능 (공유가능 o 이라면 증분백업 안됨 FRONT에서 막기?)
 * @property backup [Boolean] 증분 백업 사용 (기본이 true)
 * @property virtualSize [BigInteger] 가상크기 (provisionedSize)
 * @property actualSize [BigInteger] 실제크기
 * @property status [DiskStatus] 디스크상태
 * @property contentType [DiskContentType]
 * @property storageType [DiskStorageType] 유형
 * @property createDate [String] 생성일자
 * @property connectVm [IdentifiedVo] 연결대상
 * @property diskProfileVos List<[IdentifiedVo]> 디스크 프로파일 목록
 */
class DiskImageVo(
	val id: String = "",
	val size: BigInteger = BigInteger.ZERO,
	val appendSize: BigInteger = BigInteger.ZERO,
	val alias: String = "",
	val description: String = "",
	val dataCenterVo: IdentifiedVo = IdentifiedVo(),
	val storageDomainVo: IdentifiedVo = IdentifiedVo(),
	val sparse: Boolean = false,
	val diskProfileVo: IdentifiedVo = IdentifiedVo(),
	val wipeAfterDelete: Boolean = false,
	val sharable: Boolean = false,
	val backup: Boolean = false,
	val format: DiskFormat = DiskFormat.RAW,
	val virtualSize: BigInteger = BigInteger.ZERO,
	val actualSize: BigInteger = BigInteger.ZERO,
	val status: DiskStatus = DiskStatus.LOCKED,
	val contentType: DiskContentType = DiskContentType.DATA, // unknown
	val storageType: DiskStorageType = DiskStorageType.IMAGE,
	val createDate: String = "",
	val connectVm: IdentifiedVo = IdentifiedVo(),
	val connectTemplate: IdentifiedVo = IdentifiedVo(),
	val diskProfileVos: List<IdentifiedVo> = listOf()
): Serializable {
	override fun toString(): String =
		gson.toJson(this)

	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bSize: BigInteger = BigInteger.ZERO;fun size(block: () -> BigInteger?) { bSize = block() ?: BigInteger.ZERO }
		private var bAppendSize: BigInteger = BigInteger.ZERO;fun appendSize(block: () -> BigInteger?) { bAppendSize = block() ?: BigInteger.ZERO }
		private var bAlias: String = "";fun alias(block: () -> String?) { bAlias = block() ?: "" }
		private var bDescription: String = "";fun description(block: () -> String?) { bDescription = block() ?: "" }
		private var bDataCenterVo: IdentifiedVo = IdentifiedVo();fun dataCenterVo(block: () -> IdentifiedVo?) { bDataCenterVo = block() ?: IdentifiedVo() }
		private var bStorageDomainVo: IdentifiedVo = IdentifiedVo();fun storageDomainVo(block: () -> IdentifiedVo?) { bStorageDomainVo = block() ?: IdentifiedVo() }
		private var bSparse: Boolean = false;fun sparse(block: () -> Boolean?) { bSparse = block() ?: false }
		private var bDiskProfileVo: IdentifiedVo = IdentifiedVo();fun diskProfileVo(block: () -> IdentifiedVo?) { bDiskProfileVo = block() ?: IdentifiedVo() }
		private var bWipeAfterDelete: Boolean = false;fun wipeAfterDelete(block: () -> Boolean?) { bWipeAfterDelete = block() ?: false }
		private var bSharable: Boolean = false;fun sharable(block: () -> Boolean?) { bSharable = block() ?: false }
		private var bBackup: Boolean = false;fun backup(block: () -> Boolean?) { bBackup = block() ?: false }
		private var bFormat: DiskFormat = DiskFormat.RAW;fun format(block: () -> DiskFormat?) { bFormat = block() ?: DiskFormat.RAW }
		private var bVirtualSize: BigInteger = BigInteger.ZERO;fun virtualSize(block: () -> BigInteger?) { bVirtualSize = block() ?: BigInteger.ZERO }
		private var bActualSize: BigInteger = BigInteger.ZERO;fun actualSize(block: () -> BigInteger?) { bActualSize = block() ?: BigInteger.ZERO }
		private var bStatus: DiskStatus = DiskStatus.LOCKED;fun status(block: () -> DiskStatus?) { bStatus = block() ?: DiskStatus.LOCKED }
		private var bContentType: DiskContentType = DiskContentType.DATA;fun contentType(block: () -> DiskContentType?) { bContentType = block() ?: DiskContentType.DATA }
		private var bStorageType: DiskStorageType = DiskStorageType.IMAGE;fun storageType(block: () -> DiskStorageType?) { bStorageType = block() ?: DiskStorageType.IMAGE }
		private var bCreateDate: String = "";fun createDate(block: () -> String?) { bCreateDate = block() ?: "" }
		private var bConnectVm: IdentifiedVo = IdentifiedVo();fun connectVm(block: () -> IdentifiedVo?) { bConnectVm = block() ?: IdentifiedVo() }
		private var bConnectTemplate: IdentifiedVo = IdentifiedVo();fun connectTemplate(block: () -> IdentifiedVo?) { bConnectTemplate = block() ?: IdentifiedVo() }
		private var bDiskProfileVos: List<IdentifiedVo> = listOf();fun diskProfileVos(block: () -> List<IdentifiedVo>?) { bDiskProfileVos = block() ?: listOf() }

        fun build(): DiskImageVo = DiskImageVo(bId, bSize, bAppendSize, bAlias, bDescription, bDataCenterVo, bStorageDomainVo, bSparse, bDiskProfileVo, bWipeAfterDelete, bSharable, bBackup, bFormat, bVirtualSize, bActualSize, bStatus, bContentType, bStorageType, bCreateDate, bConnectVm, bConnectTemplate, bDiskProfileVos)
	}
	companion object {
		private val log by LoggerDelegate()
		inline fun builder(block: Builder.() -> Unit): DiskImageVo = Builder().apply(block).build()
	}
}


fun Disk.toDiskIdName(): DiskImageVo = DiskImageVo.builder {
	id { this@toDiskIdName.id() }
	alias() { this@toDiskIdName.alias() }
}
fun List<Disk>.toDiskIdNames(): List<DiskImageVo> =
	this@toDiskIdNames.map { it.toDiskIdName() }

/**
 * 디스크 목록
 * 스토리지도메인 - 디스크 목록
 */
fun Disk.toDiskMenu(conn: Connection, id: String): DiskImageVo {
	val storageDomain: StorageDomain? =
		conn.findStorageDomain(this.storageDomains().first().id()).getOrNull()

	var vmIdentifiedVo: IdentifiedVo? = null
	var templateIdentifiedVo: IdentifiedVo? = null

	if (id.isNotEmpty()) {
		val vmCache = mutableMapOf<String, Vm?>()
		val templateCache = mutableMapOf<String, Template?>()

		val vm = vmCache.getOrPut(id) {
			try { conn.findVm(id).getOrNull() }
			catch (e: Exception) { null }
		}

		val template = templateCache.getOrPut(id) {
			if (vm == null) {
				try { conn.findTemplate(id).getOrNull() }
				catch (e: Exception) { null }
			} else { null }
		}

		vmIdentifiedVo = vm?.let {
			IdentifiedVo.builder {
				id { it.id() }
				name { it.name() }
			}
		}
		templateIdentifiedVo = template?.let {
			IdentifiedVo.builder {
				id { it.id() }
				name { it.name() }
			}
		}
	}

	return DiskImageVo.builder {
		id { this@toDiskMenu.id() }
		alias { this@toDiskMenu.alias() }
		sharable { this@toDiskMenu.shareable() }
		storageDomainVo { storageDomain?.fromStorageDomainToIdentifiedVo() }
		virtualSize { this@toDiskMenu.provisionedSize() }
		actualSize { this@toDiskMenu.actualSize() }
		status { this@toDiskMenu.status() }
		sparse { this@toDiskMenu.sparse() }
		storageType { this@toDiskMenu.storageType() }
		description { this@toDiskMenu.description() }
		connectVm { vmIdentifiedVo }
		connectTemplate { templateIdentifiedVo }

	}
}


fun Disk.toDiskInfo(conn: Connection): DiskImageVo {
	val diskProfile: DiskProfile? =
		if(this@toDiskInfo.diskProfilePresent()) conn.findDiskProfile(this@toDiskInfo.diskProfile().id()).getOrNull()
		else null
	val storageDomain: StorageDomain? = conn.findStorageDomain(this.storageDomains().first().id())
			.getOrNull()
	val dataCenter: DataCenter? = storageDomain?.dataCenters()?.first()?.let {
		conn.findDataCenter(it.id())
			.getOrNull()
	}

	return DiskImageVo.builder {
		id { this@toDiskInfo.id() }
		alias { this@toDiskInfo.alias() }
		description { this@toDiskInfo.description() }
		sparse { this@toDiskInfo.sparse() } // 할당정책
		dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
		storageDomainVo { storageDomain?.fromStorageDomainToIdentifiedVo() }
		diskProfileVo { diskProfile?.fromDiskProfileToIdentifiedVo() }
		virtualSize { this@toDiskInfo.provisionedSize() }
		actualSize { this@toDiskInfo.actualSize() }
		wipeAfterDelete { this@toDiskInfo.wipeAfterDelete() }
		sharable { this@toDiskInfo.shareable() }
		backup { this@toDiskInfo.backup() == DiskBackup.INCREMENTAL }
	}
}
fun List<Disk>.toDisksInfo(conn: Connection): List<DiskImageVo> =
	this@toDisksInfo.map { it.toDiskInfo(conn) }


fun Disk.toDiskImageVo(conn: Connection): DiskImageVo {
	val storageDomain: StorageDomain? =
		conn.findStorageDomain(this@toDiskImageVo.storageDomains().first().id())
			.getOrNull()

	val dataCenter: DataCenter? =
		if(storageDomain?.dataCentersPresent() == true) conn.findDataCenter(storageDomain.dataCenters().first().id()).getOrNull()
		else null

	val diskProfile: DiskProfile? =
		if(this@toDiskImageVo.diskProfilePresent()) conn.findDiskProfile(this@toDiskImageVo.diskProfile().id()).getOrNull()
		else null

	return DiskImageVo.builder {
		id { this@toDiskImageVo.id() }
		size { this@toDiskImageVo.provisionedSize() } // 1024^3
		alias { this@toDiskImageVo.alias() }
		description { this@toDiskImageVo.description() }
		dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
		storageDomainVo { storageDomain?.fromStorageDomainToIdentifiedVo() }
		sparse { this@toDiskImageVo.sparse() }
		diskProfileVo { diskProfile?.fromDiskProfileToIdentifiedVo() }
		wipeAfterDelete { this@toDiskImageVo.wipeAfterDelete() }
		sharable { this@toDiskImageVo.shareable() }
		backup { this@toDiskImageVo.backup() == DiskBackup.INCREMENTAL }
		virtualSize { this@toDiskImageVo.provisionedSize() }
		actualSize { this@toDiskImageVo.actualSize() }
		status { this@toDiskImageVo.status() }
		contentType { this@toDiskImageVo.contentType() }
		storageType { this@toDiskImageVo.storageType() }
//		createDate { this@toDiskImageVo. } // TODO
//		connectVm { toConnectVm(conn, diskVmElementEntity) } } }  // TODO
	}
}
fun List<Disk>.toDiskImageVos(conn: Connection): List<DiskImageVo> =
	this@toDiskImageVos.map { it.toDiskImageVo(conn) }



fun Disk.toDiskVo(conn: Connection, vmId: String): DiskImageVo {
	val storageDomain: StorageDomain? =
		conn.findStorageDomain(this@toDiskVo.storageDomains().first().id())
			.getOrNull()

	val dataCenter: DataCenter? =
		if(storageDomain?.dataCentersPresent() == true) conn.findDataCenter(storageDomain.dataCenters().first().id()).getOrNull()
		else null

	val diskProfile: DiskProfile? =
		if(this@toDiskVo.diskProfilePresent()) conn.findDiskProfile(this@toDiskVo.diskProfile().id()).getOrNull()
		else null

	return DiskImageVo.builder {
		id { this@toDiskVo.id() }
		size { this@toDiskVo.provisionedSize() } // 1024^3
		alias { this@toDiskVo.alias() }
		description { this@toDiskVo.description() }
		dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
		storageDomainVo { storageDomain?.fromStorageDomainToIdentifiedVo() }
		sparse { this@toDiskVo.sparse() }
		diskProfileVo { diskProfile?.fromDiskProfileToIdentifiedVo() }
		wipeAfterDelete { this@toDiskVo.wipeAfterDelete() }
		sharable { this@toDiskVo.shareable() }
		backup { this@toDiskVo.backup() == DiskBackup.INCREMENTAL }
		virtualSize { this@toDiskVo.provisionedSize() }
		actualSize { this@toDiskVo.actualSize() }
		status { this@toDiskVo.status() }
		contentType { this@toDiskVo.contentType() }
		storageType { this@toDiskVo.storageType() }
//		createDate { this@toDiskImageVo. } // TODO
	}
}



/**
 * 스토리지 - 디스크 생성
 * 가상머신 - 가상머신 생성 - 디스크 생성
 */
fun DiskImageVo.toDiskBuilder(): DiskBuilder {
	return DiskBuilder()
		.alias(this@toDiskBuilder.alias)
		.description(this@toDiskBuilder.description)
		.wipeAfterDelete(this@toDiskBuilder.wipeAfterDelete)
		.shareable(this@toDiskBuilder.sharable)
		.backup(if (this@toDiskBuilder.backup) DiskBackup.INCREMENTAL else DiskBackup.NONE)
		.format(if (this@toDiskBuilder.backup) DiskFormat.COW else DiskFormat.RAW)
		.sparse(this@toDiskBuilder.sparse)
		.diskProfile(DiskProfileBuilder().id(this@toDiskBuilder.diskProfileVo.id))
}

fun DiskImageVo.toAddDiskBuilder(): Disk =
	this@toAddDiskBuilder.toDiskBuilder()
		.storageDomains(*arrayOf(StorageDomainBuilder().id(this@toAddDiskBuilder.storageDomainVo.id).build()))
		.provisionedSize(this@toAddDiskBuilder.size)
		.build()

fun DiskImageVo.toEditDiskBuilder(): Disk =
	this@toEditDiskBuilder.toDiskBuilder()
		.id(this@toEditDiskBuilder.id)
		.provisionedSize(this@toEditDiskBuilder.size.add(this@toEditDiskBuilder.appendSize))
		.build()

/**
 * 디스크 업로드
 * ISO 이미지 업로드용
 * (화면표시) 파일 선택시 파일에 있는 포맷, 컨텐츠(파일 확장자로 칭하는건지), 크기 출력
 * 	파일 크기가 자동으로 디스크 옵션에 추가, 파일 명칭이 파일의 이름으로 지정됨 (+설명)
 * 	디스크 이미지 업로드
 *  required: provisioned_size, alias, description, wipe_after_delete, shareable, backup, disk_profile.
 *	
 */
fun DiskImageVo.toUploadDiskBuilder(conn: Connection, fileSize: Long): Disk {
	val storageDomain: StorageDomain = conn.findStorageDomain(this@toUploadDiskBuilder.storageDomainVo.id)
		.getOrNull() ?: throw ErrorPattern.STORAGE_DOMAIN_NOT_FOUND.toException()
	// storage가 nfs 면 씬, iscsi면 사전할당
	val allocation: Boolean = storageDomain.storage().type() == StorageType.NFS

	return DiskBuilder()
		.contentType(DiskContentType.ISO)
		.provisionedSize(fileSize)
		.sparse(allocation)
		.alias(this@toUploadDiskBuilder.alias)
		.description(this@toUploadDiskBuilder.description)
		.storageDomains(*arrayOf(StorageDomainBuilder().id(this@toUploadDiskBuilder.storageDomainVo.id)))
		.diskProfile(DiskProfileBuilder().id(this@toUploadDiskBuilder.diskProfileVo.id))
		.shareable(this@toUploadDiskBuilder.sharable)
		.wipeAfterDelete(this@toUploadDiskBuilder.wipeAfterDelete)
		.backup(DiskBackup.NONE) // 증분백업 되지 않음
		.format(DiskFormat.RAW) // 이미지 업로드는 raw 형식만 가능 +front 처리?
		.build()
}