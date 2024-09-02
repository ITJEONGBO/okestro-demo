package com.itinfo.itcloud.model.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.computing.toDataCenterIdName
import com.itinfo.util.ovirt.findDataCenter
import com.itinfo.util.ovirt.findDiskProfile
import com.itinfo.util.ovirt.findStorageDomain
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
 *
 * @property virtualSize [BigInteger] 가상크기
 * @property actualSize [BigInteger] 실제크기
 * @property status [DiskStatus] 디스크상태
 * @property contentType [DiskContentType]
 * @property storageType [DiskStorageType] 유형
 * @property createDate [String] 생성일자
 * @property connectVm [IdentifiedVo] 연결대상
 *
 * @property diskProfileVos List<[IdentifiedVo]> 디스크 프로파일 목록
 */
open class DiskImageVo(
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

	val virtualSize: Int = 0,
	val actualSize: Int = 0,
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
		private var bVirtualSize: Int = 0;fun virtualSize(block: () -> Int?) { bVirtualSize = block() ?: 0 }
		private var bActualSize: Int = 0;fun actualSize(block: () -> Int?) { bActualSize = block() ?: 0 }
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


fun Disk.toDiskImageVo(conn: Connection): DiskImageVo {
	val storageDomain: StorageDomain? =
		conn.findStorageDomain(this@toDiskImageVo.storageDomains().first().id())
			.getOrNull()

	val dataCenter: DataCenter? =
		storageDomain?.dataCenters()?.first()?.let {
			conn.findDataCenter(it.id()).getOrNull()
		}
	val diskProfile: DiskProfile? =
		conn.findDiskProfile(this@toDiskImageVo.diskProfile().id())
			.getOrNull()

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
//		virtualSize
		actualSize { this@toDiskImageVo.actualSize().toInt() }
		status { this@toDiskImageVo.status() }
		contentType { this@toDiskImageVo.contentType() }
		storageType { this@toDiskImageVo.storageType() }
//		createDate { this@toDiskImageVo. } // TODO
//		connectVm { this@toDiskImageVo. }  // TODO
	}
}

fun List<Disk>.toDiskImageVos(conn: Connection): List<DiskImageVo> =
	this@toDiskImageVos.map { it.toDiskImageVo(conn) }

/**
 * 스토리지 - 디스크 생성
 * 가상머신 - 가상머신 생성 - 디스크 생성
 */
fun DiskImageVo.toDiskBuilder(): DiskBuilder {
	return DiskBuilder()
		.alias(this@toDiskBuilder.alias)
		.description(this@toDiskBuilder.description)
		.provisionedSize(BigInteger.valueOf((this@toDiskBuilder.size + this@toDiskBuilder.appendSize).toLong()).multiply(BigInteger.valueOf(1024).pow(3)) ) // 값 받은 것을 byte로 변환하여 준다
		.wipeAfterDelete(this@toDiskBuilder.wipeAfterDelete)
		.shareable(this@toDiskBuilder.sharable)
		.backup(if (this@toDiskBuilder.backup) DiskBackup.INCREMENTAL else DiskBackup.NONE)
		.format(if (this@toDiskBuilder.backup) DiskFormat.COW else DiskFormat.RAW)
		.storageDomain(StorageDomainBuilder().id(this@toDiskBuilder.storageDomainVo.id).build())
		.sparse(this@toDiskBuilder.sparse)
		.diskProfile(DiskProfileBuilder().id(this@toDiskBuilder.diskProfileVo.id)) // 없어도 상관없음
}

fun DiskImageVo.toAddDiskBuilder(): Disk =
	this@toAddDiskBuilder.toDiskBuilder().build()

fun DiskImageVo.toEditDiskBuilder(): Disk =
	this@toEditDiskBuilder.toDiskBuilder().id(this@toEditDiskBuilder.id).build()


