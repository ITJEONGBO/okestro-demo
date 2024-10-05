package com.itinfo.itcloud.model.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.repository.engine.entity.DiskVmElementEntity
import com.itinfo.itcloud.repository.engine.entity.toVmId
import com.itinfo.util.ovirt.findDataCenter
import com.itinfo.util.ovirt.findDiskProfile
import com.itinfo.util.ovirt.findStorageDomain
import com.itinfo.util.ovirt.findVm
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
 * @property virtualSize [BigInteger] 가상크기
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
	val size: Int = 0,
	val appendSize: Int = 0,
	val alias: String = "",
	val description: String = "",
	val dataCenterVo: IdentifiedVo = IdentifiedVo(),
	val storageDomainVo: IdentifiedVo = IdentifiedVo(),
	val sparse: Boolean = false,
	val diskProfileVo: IdentifiedVo = IdentifiedVo(),
	val wipeAfterDelete: Boolean = false,
	val sharable: Boolean = false,
	val backup: Boolean = false,
	val virtualSize: BigInteger = BigInteger.ZERO,
	val actualSize: BigInteger = BigInteger.ZERO,
	val status: DiskStatus = DiskStatus.LOCKED,
	val contentType: DiskContentType = DiskContentType.DATA, // unknown
	val storageType: DiskStorageType = DiskStorageType.IMAGE,
	val createDate: String = "",
	val connectVm: IdentifiedVo = IdentifiedVo(),
	val diskProfileVos: List<IdentifiedVo> = listOf()
): Serializable {
	override fun toString(): String =
		gson.toJson(this)

	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bSize: Int = 0;fun size(block: () -> Int?) { bSize = block() ?: 0 }
		private var bAppendSize: Int = 0;fun appendSize(block: () -> Int?) { bAppendSize = block() ?: 0 }
		private var bAlias: String = "";fun alias(block: () -> String?) { bAlias = block() ?: "" }
		private var bDescription: String = "";fun description(block: () -> String?) { bDescription = block() ?: "" }
		private var bDataCenterVo: IdentifiedVo = IdentifiedVo();fun dataCenterVo(block: () -> IdentifiedVo?) { bDataCenterVo = block() ?: IdentifiedVo() }
		private var bStorageDomainVo: IdentifiedVo = IdentifiedVo();fun storageDomainVo(block: () -> IdentifiedVo?) { bStorageDomainVo = block() ?: IdentifiedVo() }
		private var bSparse: Boolean = false;fun sparse(block: () -> Boolean?) { bSparse = block() ?: false }
		private var bDiskProfileVo: IdentifiedVo = IdentifiedVo();fun diskProfileVo(block: () -> IdentifiedVo?) { bDiskProfileVo = block() ?: IdentifiedVo() }
		private var bWipeAfterDelete: Boolean = false;fun wipeAfterDelete(block: () -> Boolean?) { bWipeAfterDelete = block() ?: false }
		private var bSharable: Boolean = false;fun sharable(block: () -> Boolean?) { bSharable = block() ?: false }
		private var bBackup: Boolean = false;fun backup(block: () -> Boolean?) { bBackup = block() ?: false }
		private var bVirtualSize: BigInteger = BigInteger.ZERO;fun virtualSize(block: () -> BigInteger?) { bVirtualSize = block() ?: BigInteger.ZERO }
		private var bActualSize: BigInteger = BigInteger.ZERO;fun actualSize(block: () -> BigInteger?) { bActualSize = block() ?: BigInteger.ZERO }
		private var bStatus: DiskStatus = DiskStatus.LOCKED;fun status(block: () -> DiskStatus?) { bStatus = block() ?: DiskStatus.LOCKED }
		private var bContentType: DiskContentType = DiskContentType.DATA;fun contentType(block: () -> DiskContentType?) { bContentType = block() ?: DiskContentType.DATA }
		private var bStorageType: DiskStorageType = DiskStorageType.IMAGE;fun storageType(block: () -> DiskStorageType?) { bStorageType = block() ?: DiskStorageType.IMAGE }
		private var bCreateDate: String = "";fun createDate(block: () -> String?) { bCreateDate = block() ?: "" }
		private var bConnectVm: IdentifiedVo = IdentifiedVo();fun connectVm(block: () -> IdentifiedVo?) { bConnectVm = block() ?: IdentifiedVo() }
		private var bDiskProfileVos: List<IdentifiedVo> = listOf();fun diskProfileVos(block: () -> List<IdentifiedVo>?) { bDiskProfileVos = block() ?: listOf() }

        fun build(): DiskImageVo = DiskImageVo(bId, bSize, bAppendSize, bAlias, bDescription, bDataCenterVo, bStorageDomainVo, bSparse, bDiskProfileVo, bWipeAfterDelete, bSharable, bBackup, bVirtualSize, bActualSize, bStatus, bContentType, bStorageType, bCreateDate, bConnectVm, bDiskProfileVos)
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
fun Disk.toDiskMenu(conn: Connection, vmId: String?): DiskImageVo {
	val storageDomain: StorageDomain? =
		conn.findStorageDomain(this.storageDomains().first().id())
			.getOrNull()
	val vm: Vm? =
		vmId?.let { conn.findVm(it).getOrNull() }
	val i: IdentifiedVo = IdentifiedVo.builder {
		id { vm?.id() }
		name { vm?.name() }
	}

	return DiskImageVo.builder {
		id { this@toDiskMenu.id() }
		alias { this@toDiskMenu.alias() }
		sharable { this@toDiskMenu.shareable() }
		storageDomainVo { storageDomain?.fromStorageDomainToIdentifiedVo() }
		virtualSize { this@toDiskMenu.totalSize() }
		actualSize { this@toDiskMenu.actualSize() }
		status { this@toDiskMenu.status() }
		sparse { this@toDiskMenu.sparse() }
		storageType { this@toDiskMenu.storageType() }
		description { this@toDiskMenu.description() }
		connectVm { i }
//		createDate {  } // 생성일자
	}
}

fun Disk.toDiskInfo(conn: Connection): DiskImageVo {
	val diskProfile: DiskProfile? =
		if(this@toDiskInfo.diskProfilePresent()) conn.findDiskProfile(this@toDiskInfo.diskProfile().id()).getOrNull()
		else null

	return DiskImageVo.builder {
		id { this@toDiskInfo.id() }
		alias { this@toDiskInfo.alias() }
		description { this@toDiskInfo.description() }
		diskProfileVo { diskProfile?.fromDiskProfileToIdentifiedVo() }
		virtualSize { this@toDiskInfo.totalSize() }
		actualSize { this@toDiskInfo.actualSize() }
	}
}

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
		size { (this@toDiskImageVo.provisionedSizeAsLong() / 1073741824).toInt() } // 1024^3
		alias { this@toDiskImageVo.alias() }
		description { this@toDiskImageVo.description() }
		dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
		storageDomainVo { storageDomain?.fromStorageDomainToIdentifiedVo() }
		sparse { this@toDiskImageVo.sparse() }
		diskProfileVo { diskProfile?.fromDiskProfileToIdentifiedVo() }
		wipeAfterDelete { this@toDiskImageVo.wipeAfterDelete() }
		sharable { this@toDiskImageVo.shareable() }
		backup { this@toDiskImageVo.backup() == DiskBackup.INCREMENTAL }
		virtualSize { this@toDiskImageVo.totalSize() }
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
		size { (this@toDiskVo.provisionedSizeAsLong() / 1073741824).toInt() } // 1024^3
		alias { this@toDiskVo.alias() }
		description { this@toDiskVo.description() }
		dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
		storageDomainVo { storageDomain?.fromStorageDomainToIdentifiedVo() }
		sparse { this@toDiskVo.sparse() }
		diskProfileVo { diskProfile?.fromDiskProfileToIdentifiedVo() }
		wipeAfterDelete { this@toDiskVo.wipeAfterDelete() }
		sharable { this@toDiskVo.shareable() }
		backup { this@toDiskVo.backup() == DiskBackup.INCREMENTAL }
		virtualSize { this@toDiskVo.totalSize() }
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
		.provisionedSize(BigInteger.valueOf(this@toAddDiskBuilder.size.toLong() * 1024 * 1024 * 1024))
		.build()

fun DiskImageVo.toEditDiskBuilder(): Disk =
	this@toEditDiskBuilder.toDiskBuilder()
		.id(this@toEditDiskBuilder.id)
		.provisionedSize((this@toEditDiskBuilder.size + this@toEditDiskBuilder.appendSize).toLong() * 1024 * 1024 *1024 )
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
fun DiskImageVo.toUploadDiskBuilder(fileSize: Long): Disk =
	DiskBuilder()
		.contentType(DiskContentType.ISO)
		.provisionedSize(fileSize)
		.alias(this@toUploadDiskBuilder.alias)
		.description(this@toUploadDiskBuilder.description)
		.storageDomains(*arrayOf(StorageDomainBuilder().id(this@toUploadDiskBuilder.storageDomainVo.id)))
		.diskProfile(DiskProfileBuilder().id(this@toUploadDiskBuilder.diskProfileVo.id))
		.shareable(this@toUploadDiskBuilder.sharable)
		.wipeAfterDelete(this@toUploadDiskBuilder.wipeAfterDelete)
		.backup(DiskBackup.NONE) // 증분백업 되지 않음
		.format(DiskFormat.RAW) // 이미지 업로드는 raw 형식만 가능 +front 처리?
		.build()
