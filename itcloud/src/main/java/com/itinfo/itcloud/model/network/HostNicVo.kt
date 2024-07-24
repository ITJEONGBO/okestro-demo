package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.model.computing.HostVo
import com.itinfo.itcloud.model.computing.toHostIdName
import com.itinfo.itcloud.model.gson
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.*
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.math.BigInteger

private val log = LoggerFactory.getLogger(HostNicVo::class.java)

/**
 * [HostNicVo]
 * Host
 * @property id [String]
 * @property name [String]
 * @property bridged [Boolean]
 * @property ipv4 [String] ip.address
 * @property ipv6 [String]
 * @property macAddress [String]
 * @property mtu [Int]
 * @property status [NicStatus]
 * @property hostVo [HostVo]
 * @property networkVo [NetworkVo]
 *
 * nic.statistics
 * @property speed [BigInteger] mbps
 * @property rxSpeed [BigInteger] mbps
 * @property txSpeed [BigInteger] mbps
 * @property rxTotalSpeed [BigInteger] byte
 * @property txTotalSpeed [BigInteger] byte
 * @property rxTotalError [BigInteger] byte
 * @property txTotalError [BigInteger] byte
 *
 */
class HostNicVo(
	val id: String = "",
	val name: String = "",
	val bridged: Boolean = false,
	val ipv4: String = "",
	val ipv6: String = "",
	val macAddress: String = "",
	val mtu: Int = 0,
	val status: NicStatus = NicStatus.DOWN,
	val hostVo: HostVo = HostVo(),
	val networkVo: NetworkVo = NetworkVo(),
	val speed: BigInteger = BigInteger.ZERO,
	val rxSpeed: BigInteger = BigInteger.ZERO,
	val txSpeed: BigInteger = BigInteger.ZERO,
	val rxTotalSpeed: BigInteger = BigInteger.ZERO,
	val txTotalSpeed: BigInteger = BigInteger.ZERO,
	val rxTotalError: BigInteger = BigInteger.ZERO,
	val txTotalError: BigInteger = BigInteger.ZERO,
) : Serializable {
	override fun toString(): String =
		gson.toJson(this)

	class Builder {
		private var bId: String = ""; fun id(block: () -> String?) { bId = block() ?: "" } 
		private var bName: String = ""; fun name(block: () -> String?) { bName = block() ?: "" } 
		private var bBridged: Boolean = false; fun bridged(block: () -> Boolean?) { bBridged = block() ?: false }
		private var bIpv4: String = ""; fun ipv4(block: () -> String?) { bIpv4 = block() ?: "" } 
		private var bIpv6: String = ""; fun ipv6(block: () -> String?) { bIpv6 = block() ?: "" } 
		private var bMacAddress: String = ""; fun macAddress(block: () -> String?) { bMacAddress = block() ?: "" } 
		private var bMtu: Int = 0; fun mtu(block: () -> Int?) { bMtu = block() ?: 0 } 
		private var bStatus: NicStatus = NicStatus.DOWN; fun status(block: () -> NicStatus?) { bStatus = block() ?: NicStatus.DOWN } 
		private var bHostVo: HostVo = HostVo(); fun hostVo(block: () -> HostVo?) { bHostVo = block() ?: HostVo() }
		private var bNetworkVo: NetworkVo = NetworkVo(); fun networkVo(block: () -> NetworkVo?) { bNetworkVo = block() ?: NetworkVo() } 
		private var bSpeed: BigInteger = BigInteger.ZERO; fun speed(block: () -> BigInteger?) { bSpeed = block() ?: BigInteger.ZERO } 
		private var bRxSpeed: BigInteger = BigInteger.ZERO; fun rxSpeed(block: () -> BigInteger?) { bRxSpeed = block() ?: BigInteger.ZERO }
		private var bTxSpeed: BigInteger = BigInteger.ZERO; fun txSpeed(block: () -> BigInteger?) { bTxSpeed = block() ?: BigInteger.ZERO }
		private var bRxTotalSpeed: BigInteger = BigInteger.ZERO; fun rxTotalSpeed(block: () -> BigInteger?) { bRxTotalSpeed = block() ?: BigInteger.ZERO } 
		private var bTxTotalSpeed: BigInteger = BigInteger.ZERO; fun txTotalSpeed(block: () -> BigInteger?) { bTxTotalSpeed = block() ?: BigInteger.ZERO } 
		private var bRxTotalError: BigInteger = BigInteger.ZERO; fun rxTotalError(block: () -> BigInteger?) { bRxTotalError = block() ?: BigInteger.ZERO } 
		private var bTxTotalError: BigInteger = BigInteger.ZERO; fun txTotalError(block: () -> BigInteger?) { bTxTotalError = block() ?: BigInteger.ZERO } 

		fun build(): HostNicVo = HostNicVo(bId, bName, bBridged, bIpv4, bIpv6, bMacAddress, bMtu, bStatus, bHostVo, bNetworkVo, bSpeed, bRxSpeed, bTxSpeed, bRxTotalSpeed, bTxTotalSpeed, bRxTotalError, bTxTotalError)
	}

	companion object {
		inline fun builder(block: HostNicVo.Builder.() -> Unit): HostNicVo = HostNicVo.Builder().apply(block).build()
	}
}

fun HostNic.toHostNicIdName(): HostNicVo = HostNicVo.builder {
	id { this@toHostNicIdName.id() }
	name { this@toHostNicIdName.name() }
}

fun List<HostNic>.toHostNicIdNames(): List<HostNicVo> =
	this@toHostNicIdNames.map { it.toHostNicIdName() }


fun HostNic.toHostNicVo(conn: Connection): HostNicVo {
	log.debug("Host.toHostNicVo ... ")
	val hostVo: HostVo? =
		conn.findHost(this@toHostNicVo.host().id()).getOrNull()?.toHostIdName()
	val networkVo: NetworkVo? =
		conn.findNetwork(this@toHostNicVo.network().id()).getOrNull()?.toNetworkIdName()
	val statistics: List<Statistic>? =
		hostVo?.let { conn.findAllStatisticsFromHostNic(it.id, this@toHostNicVo.id()).getOrDefault(listOf()) }

	return HostNicVo.builder {
		id { this@toHostNicVo.id() }
		name { this@toHostNicVo.name() }
		bridged { this@toHostNicVo.bridged() }
		ipv4 { this@toHostNicVo.ip().address() }
		ipv6 { if(this@toHostNicVo.ipv6().addressPresent()) this@toHostNicVo.ipv6().address() else null }
		macAddress { this@toHostNicVo.mac().address() }
		mtu { this@toHostNicVo.mtuAsInteger() }
		status { this@toHostNicVo.status() }
		hostVo { hostVo }
		networkVo { networkVo }
		speed { this@toHostNicVo.speed() }
		rxSpeed { statistics?.findSpeed("data.current.rx.bps") }
		txSpeed { statistics?.findSpeed("data.current.tx.bps") }
		rxTotalSpeed { statistics?.findSpeed("data.total.rx") }
		txTotalSpeed { statistics?.findSpeed("data.total.tx") }
		rxTotalError { statistics?.findSpeed("errors.total.rx") }
		txTotalError { statistics?.findSpeed("errors.total.tx") }
	}
}

fun List<HostNic>.toHostNicVos(conn: Connection): List<HostNicVo>{
	return this@toHostNicVos.map { it.toHostNicVo(conn) }
}
