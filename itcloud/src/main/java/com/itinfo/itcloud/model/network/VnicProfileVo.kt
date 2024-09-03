package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.VnicProfileBuilder
import org.ovirt.engine.sdk4.types.*
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(VnicProfileVo::class.java)
/**
 * [VnicProfileVo]
 * 네트워크 - VnicProfile
 *
 * - vnicProfile 생성
 *  생성 방법: 네트워크 생성시 같이 생성(기본으로 네트워크명과 동일한 이름으로 생성) / 네트워크-vnicProfile에서 생성(잘안씀)
 *  프로젝트에서는 네트워크-vnicProfile 은 생략예정(목록 포함)
 *
 *  vnicProfile 생성시 통과가 T면 네트워크 필터 사라지고, 마이그레이션 선택여부가 나온다 (front)
 *
 * @property id [String]
 * @property name [String]
 * @property description [String]
 * @property passThrough [VnicPassThroughMode]
 * @property migration [Boolean]
 * @property portMirroring [Boolean]
 * @property networkFilterVo [NetworkFilterVo] 네트워크 필터 값을 입력해서 넣는방식인거 같음 (참고, NetworkFilter, NetworkFilterParameter)
 * @property dataCenterVo [DataCenterVo]
 * @property networkVo [NetworkVo]
 *
 */
class VnicProfileVo(
	val id: String = "",
	val name: String = "",
	val description: String = "",
	val passThrough: VnicPassThroughMode = VnicPassThroughMode.DISABLED,
	val migration: Boolean = false,
	val portMirroring: Boolean = false,
	val networkFilterVo: IdentifiedVo =  IdentifiedVo(),
	val dataCenterVo: IdentifiedVo = IdentifiedVo(),
	val networkVo: IdentifiedVo = IdentifiedVo(),
) : Serializable {
	override fun toString(): String =
		gson.toJson(this)

	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
		private var bDescription: String = "";fun description(block: () -> String?) { bDescription = block() ?: "" }
		private var bPassThrough: VnicPassThroughMode = VnicPassThroughMode.DISABLED;fun passThrough(block: () -> VnicPassThroughMode) { bPassThrough = block() ?: VnicPassThroughMode.DISABLED }
		private var bMigration: Boolean = false;fun migration(block: () -> Boolean?) { bMigration = block() ?: false }
		private var bPortMirroring: Boolean = false;fun portMirroring(block: () -> Boolean?) { bPortMirroring = block() ?: false }
		private var bNetworkFilterVo: IdentifiedVo = IdentifiedVo();fun networkFilterVo(block: () -> IdentifiedVo?) { bNetworkFilterVo = block() ?: IdentifiedVo() }
		private var bDataCenterVo: IdentifiedVo = IdentifiedVo();fun dataCenterVo(block: () -> IdentifiedVo?) { bDataCenterVo = block() ?: IdentifiedVo() }
		private var bNetworkVo: IdentifiedVo = IdentifiedVo();fun networkVo(block: () -> IdentifiedVo?) { bNetworkVo = block() ?: IdentifiedVo() }

		fun build(): VnicProfileVo = VnicProfileVo(bId, bName, bDescription, bPassThrough, bMigration, bPortMirroring, bNetworkFilterVo, bDataCenterVo, bNetworkVo)
	}

	companion object {
		inline fun builder(block: VnicProfileVo.Builder.() -> Unit): VnicProfileVo = VnicProfileVo.Builder().apply(block).build()
	}
}

fun VnicProfile.toVnicProfileIdName(): VnicProfileVo = VnicProfileVo.builder {
	id { this@toVnicProfileIdName.id() }
	name { this@toVnicProfileIdName.name() }
}

fun List<VnicProfile>.toVnicProfileIdNames(): List<VnicProfileVo> =
	this@toVnicProfileIdNames.map { it.toVnicProfileIdName() }


fun VnicProfile.toVnicProfileVo(conn: Connection): VnicProfileVo {
	val network: Network? =
		conn.findNetwork(this@toVnicProfileVo.network().id())
			.getOrNull()
	val dataCenter: DataCenter? =
		network?.dataCenter()?.let { conn.findDataCenter(it.id())
			.getOrNull() }
	val networkFilter: NetworkFilter? =
		if(this@toVnicProfileVo.networkFilterPresent())
			conn.findNetworkFilter(this@toVnicProfileVo.networkFilter().id()).getOrNull()
		else null

	return VnicProfileVo.builder {
		id { this@toVnicProfileVo.id() }
		name { this@toVnicProfileVo.name() }
		description { this@toVnicProfileVo.description() }
		passThrough { this@toVnicProfileVo.passThrough().mode() }
//		passThrough { this@toVnicProfileVo.passThrough().mode().findVnicPass() }
		portMirroring { this@toVnicProfileVo.portMirroring() }
		migration { if(this@toVnicProfileVo.migratablePresent()) this@toVnicProfileVo.migratable() else null }
		networkFilterVo { networkFilter?.fromNetworkFilterToIdentifiedVo() }
		dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
		networkVo { network?.fromNetworkToIdentifiedVo() }
	}
}

fun List<VnicProfile>.toVnicProfileVos(conn: Connection): List<VnicProfileVo> =
	this@toVnicProfileVos.map { it.toVnicProfileVo(conn) }


/**
 * vnicProfile 빌더
 *  여쭤보기 네트워크 생성할때 만드는것으로 끝나는지 따로 vnicProfile에서 만드는지
 * 만약 네트워크에서 만들고 끝난다고 한다면 vnicprofile 빌더는 따로 필요없음
 */
fun VnicProfileVo.toVnicProfileBuilder(conn: Connection): VnicProfileBuilder {
	return VnicProfileBuilder()
//		.network()
//		.name()
//		.description()
//		.migratable()
}









fun Nic.toVnicProfileVoFromNic(conn: Connection): VnicProfileVo {
	log.debug("toVnicProfileVo ... ")
	val vnicProfile: VnicProfile? =
		conn.findVnicProfile(this@toVnicProfileVoFromNic.vnicProfile().id())
			.getOrNull()
	val network: Network? =
		vnicProfile?.network()?.let {
			conn.findNetwork(it.id()).getOrNull()
		}

	return VnicProfileVo.builder {
		id { vnicProfile?.id() }
		name { vnicProfile?.name() }
		networkVo { network?.fromNetworkToIdentifiedVo() }
	}
}

/**
 * 편집 - vnic
 * @param conn
 * @return
 */
fun List<Nic>.toVnicProfileVosFromNic(conn: Connection): List<VnicProfileVo> =
	this.map { it.toVnicProfileVoFromNic(conn) }







/*
fun Network.toVnicCreateVo(conn: Connection): VnicProfileVo {
	val dataCenter: DataCenter? =
		conn.findDataCenter(this@toVnicCreateVo.dataCenter().id())
			.getOrNull()
	return VnicProfileVo.builder {
		dataCenterVo { dataCenter?.toDataCenterIdName() }
		networkId { this@toVnicCreateVo.id() }
		networkName { this@toVnicCreateVo.name() }
//		nfVo(NetworkFilterVo.builder().build())
		// 프론트에서 네트워크 기본값 지정
		migration { true } // TODO 기본값?
	}
}

fun VnicProfile.toVnicCreateVo(conn: Connection, vcId: String): VnicProfileVo {
	val	network: Network =
		conn.findNetwork(this@toVnicCreateVo.network().id())
			.getOrNull() ?: return VnicCreateVo.builder {
			networkId { this@toVnicCreateVo.network().id() }
			name { this@toVnicCreateVo.name() }
			description { this@toVnicCreateVo.description() }
			passThrough { this@toVnicCreateVo.passThrough().mode() }
			migration { !this@toVnicCreateVo.migratablePresent() || this@toVnicCreateVo.migratable() }
			portMirror { this@toVnicCreateVo.portMirroring() }
		}
	val dc: DataCenter = network.dataCenter()
	val networkFilter: NetworkFilter =
		conn.findNetworkFilter(this@toVnicCreateVo.networkFilter().id())
			.getOrNull() ?: return VnicProfileVo.builder {
			dcId { dc.id() }
			dcName { dc.name() }
			networkId { this@toVnicCreateVo.network().id() }
			networkName { network.name() }
			id { vcId }
			name { this@toVnicCreateVo.name() }
			description { this@toVnicCreateVo.description() }
			passThrough { this@toVnicCreateVo.passThrough().mode() }
			migration { !this@toVnicCreateVo.migratablePresent() || this@toVnicCreateVo.migratable() }
			portMirror { this@toVnicCreateVo.portMirroring() }
		}

	return VnicProfileVo.builder {
		dcId { dc.id() }
		dcName { dc.name() }
		networkId { this@toVnicCreateVo.network().id() }
		networkName { network.name() }
		id { vcId }
		name { this@toVnicCreateVo.name() }
		description { this@toVnicCreateVo.description() }
		passThrough { this@toVnicCreateVo.passThrough().mode() }
		migration { !this@toVnicCreateVo.migratablePresent() || this@toVnicCreateVo.migratable() }
		portMirror { this@toVnicCreateVo.portMirroring() }
		networkFilterName { networkFilter.name() }
	}
}
*/
