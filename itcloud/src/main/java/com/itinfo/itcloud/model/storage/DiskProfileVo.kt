package com.itinfo.itcloud.model.storage

import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.IdentifiedVo
import org.ovirt.engine.sdk4.builders.DiskProfileBuilder
import org.ovirt.engine.sdk4.builders.StorageDomainBuilder
import org.ovirt.engine.sdk4.types.DiskProfile
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(DiskProfileVo::class.java)

/**
 * [DiskProfileVo]
 * 디스크 프로필
 *
 * @property id [String]
 * @property name [String]
 * @property description [String] 설명
 */
open class DiskProfileVo(
	val id: String = "",
	val name: String = "",
	val description: String = "",
	val storageDomain: IdentifiedVo = IdentifiedVo()
): Serializable {
	override fun toString(): String =
		gson.toJson(this)
	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
		private var bDescription: String = "";fun description(block: () -> String?) { bDescription = block() ?: "" }
		private var bStorageDomain: IdentifiedVo = IdentifiedVo();fun storageDomain(block: () -> IdentifiedVo?) { bStorageDomain = block() ?: IdentifiedVo() }

		fun build(): DiskProfileVo = DiskProfileVo(bId, bName, bDescription, bStorageDomain)
	}
	
	companion object {
		inline fun builder(block: DiskProfileVo.Builder.() -> Unit): DiskProfileVo = DiskProfileVo.Builder().apply(block).build()
	}
}

fun DiskProfile.toDiskProfileVo(): DiskProfileVo {
	return DiskProfileVo.builder {
		id { this@toDiskProfileVo.id() }
		name { if(this@toDiskProfileVo.namePresent()) this@toDiskProfileVo.name() else "" }
		description { this@toDiskProfileVo.description() }
	}
}

fun List<DiskProfile>.toDiskProfileVos(): List<DiskProfileVo> =
	this@toDiskProfileVos.map { it.toDiskProfileVo() }

/**
 * 디스크 프로파일 빌더
 */
fun DiskProfileVo.toDiskProfileBuilder(): DiskProfileBuilder {
	val diskProfileBuilder = DiskProfileBuilder()
	return diskProfileBuilder
		.name(this@toDiskProfileBuilder.name)
		.description(this@toDiskProfileBuilder.description)
		.storageDomain(StorageDomainBuilder().id(this@toDiskProfileBuilder.storageDomain.id).build())
}

/**
 * 디스크 프로파일 생성 빌더
 */
fun DiskProfileVo.toAddDiskProfileBuilder(): DiskProfile =
	this@toAddDiskProfileBuilder.toDiskProfileBuilder().build()

/**
 * 디스크 프로파일 편집 빌더
 */
fun DiskProfileVo.toEditDiskProfileBuilder(): DiskProfile =
	this@toEditDiskProfileBuilder.toDiskProfileBuilder().id(this@toEditDiskProfileBuilder.id).build()


