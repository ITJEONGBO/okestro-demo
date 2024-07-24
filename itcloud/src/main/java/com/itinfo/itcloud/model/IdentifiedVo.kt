package com.itinfo.itcloud.model

import org.ovirt.engine.sdk4.types.*
import org.slf4j.LoggerFactory
import java.io.Serializable
import javax.xml.crypto.Data

private val log = LoggerFactory.getLogger(IdentifiedVo::class.java)

/**
 * [IdentifiedVo]
 * 단순 id, name을 출력하기 위해 있음
 *
 * @property id [String]
 * @property name [String]
 */
open class IdentifiedVo(
	val id: String = "",
	val name: String = ""
): Serializable {
	override fun toString(): String =
		gson.toJson(this)
	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
		fun build(): IdentifiedVo = IdentifiedVo(bId, bName)
	}

	companion object {
		inline fun builder(block: IdentifiedVo.Builder.() -> Unit): IdentifiedVo = IdentifiedVo.Builder().apply(block).build()
	}
}

fun DataCenter.fromDataCenterToIdentifiedVo(): IdentifiedVo = IdentifiedVo.builder {
	id { id() }
	name { if (namePresent()) name() else "" }
}
fun List<DataCenter>.fromDataCentersToIdentifiedVos(): List<IdentifiedVo> =
	this@fromDataCentersToIdentifiedVos.map { it.fromDataCenterToIdentifiedVo() }

// 선호도 그룹, 레이블 때문에 잇음
fun Host.fromHostToIdentifiedVo(): IdentifiedVo = IdentifiedVo.builder {
	id { id() }
	name { if (namePresent()) name() else "" }
}
fun List<Host>.fromHostsToIdentifiedVos(): List<IdentifiedVo> =
	this@fromHostsToIdentifiedVos.map { it.fromHostToIdentifiedVo() }

fun AffinityGroup.fromAffinityGroupToIdentifiedVo(): IdentifiedVo = IdentifiedVo.builder {
	id { id() }
	name { if (namePresent()) name() else "" }
}
fun List<AffinityGroup>.fromAffinityGroupsToIdentifiedVos(): List<IdentifiedVo> =
	this@fromAffinityGroupsToIdentifiedVos.map { it.fromAffinityGroupToIdentifiedVo() }

fun AffinityLabel.fromAffinityLabelToIdentifiedVo(): IdentifiedVo = IdentifiedVo.builder {
	id { id() }
	name { if (namePresent()) name() else "" }
}
fun List<AffinityLabel>.fromAffinityLabelsToIdentifiedVos(): List<IdentifiedVo> =
	this@fromAffinityLabelsToIdentifiedVos.map { it.fromAffinityLabelToIdentifiedVo() }

fun Vm.fromVmToIdentifiedVo(): IdentifiedVo = IdentifiedVo.builder {
	id { id() }
	name { if (namePresent()) name() else "" }
}
fun List<Vm>.fromVmsToIdentifiedVos(): List<IdentifiedVo> =
	this@fromVmsToIdentifiedVos.map { it.fromVmToIdentifiedVo() }




fun Application.fromApplicationToIdentifiedVo(): IdentifiedVo = IdentifiedVo.builder {
	id { id() }
	name { if (namePresent()) name() else "" }
}

fun List<Application>.fromApplicationsToIdentifiedVos(): List<IdentifiedVo> =
	this@fromApplicationsToIdentifiedVos.map { it.fromApplicationToIdentifiedVo() }
