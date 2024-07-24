package com.itinfo.itcloud.model.storage

import com.itinfo.itcloud.model.gson
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
	val description: String = ""
): Serializable {
	override fun toString(): String =
		gson.toJson(this)
	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
		private var bDescription: String = "";fun description(block: () -> String?) { bDescription = block() ?: "" }

		fun build(): DiskProfileVo = DiskProfileVo(bId, bName, bDescription)
	}
	
	companion object {
		inline fun builder(block: DiskProfileVo.Builder.() -> Unit): DiskProfileVo = DiskProfileVo.Builder().apply(block).build()
	}
}

fun DiskProfile.toDiskProfileVo(): DiskProfileVo {
	log.debug("DiskProfile.toDiskProfileVo ... ")
	return DiskProfileVo.builder {
		id { this@toDiskProfileVo.id() }
		name { if(this@toDiskProfileVo.namePresent()) this@toDiskProfileVo.name() else "" }
		description { this@toDiskProfileVo.description() }
	}
}

fun List<DiskProfile>.toDiskProfileVos(): List<DiskProfileVo> =
	this@toDiskProfileVos.map { it.toDiskProfileVo() }