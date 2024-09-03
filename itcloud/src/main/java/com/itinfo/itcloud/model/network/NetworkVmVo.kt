package com.itinfo.itcloud.model.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.gson
import com.itinfo.util.ovirt.findCluster
import com.itinfo.util.ovirt.findClusterName
import com.itinfo.util.ovirt.findSpeed
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.*
import java.io.Serializable
import java.math.BigInteger

/**
 * [NetworkVmVo]
 * 
 * @property vmId [String]
 * @property vmName [String]
 * @property clusterId [String]
 * @property clusterName [String]
 * @property ipv4 [String]
 * @property ipv6 [String]
 * @property fqdn [String]
 * @property status [VmStatus]
 *
 * vnic
 * @property vnicStatus [Boolean]
 * @property vnicId [String]
 * @property vnicName [String]
 * @property vnicRx [BigInteger]
 * @property vnicTx [BigInteger]
 * @property rxTotalSpeed [BigInteger]
 * @property txTotalSpeed [BigInteger]
 * @property description [String]
 */
class NetworkVmVo(
	val vmId: String = "",
	val vmName: String = "",
	val clusterId: String = "",
	val clusterName: String = "",
	val ipv4: String = "",
	val ipv6: String = "",
	val fqdn: String = "",
	val status: VmStatus = VmStatus.UNKNOWN,

    // vnic
	val vnicStatus: Boolean = false,
	val vnicId: String = "",
	val vnicName: String = "",
	val vnicRx: BigInteger = BigInteger.ZERO,
	val vnicTx: BigInteger = BigInteger.ZERO,
	val rxTotalSpeed: BigInteger = BigInteger.ZERO,
	val txTotalSpeed: BigInteger = BigInteger.ZERO,
	val description: String = "",
) : Serializable {
	override fun toString(): String =
		gson.toJson(this)

	class Builder {
		private var bVmId: String = "";fun vmId(block: () -> String?) { bVmId = block() ?: "" }
		private var bVmName: String = "";fun vmName(block: () -> String?) { bVmName = block() ?: "" }
		private var bClusterId: String = "";fun clusterId(block: () -> String?) { bClusterId = block() ?: "" }
		private var bClusterName: String = "";fun clusterName(block: () -> String?) { bClusterName = block() ?: "" }
		private var bIpv4: String = "";fun ipv4(block: () -> String?) { bIpv4 = block() ?: "" }
		private var bIpv6: String = "";fun ipv6(block: () -> String?) { bIpv6 = block() ?: "" }
		private var bFqdn: String = "";fun fqdn(block: () -> String?) { bFqdn = block() ?: "" }
		private var bStatus: VmStatus = VmStatus.UNKNOWN;fun status(block: () -> VmStatus?) { bStatus = block() ?: VmStatus.UNKNOWN }
		private var bVnicStatus: Boolean = false;fun vnicStatus(block: () -> Boolean?) { bVnicStatus = block() ?: false }
		private var bVnicId: String = "";fun vnicId(block: () -> String?) { bVnicId = block() ?: "" }
		private var bVnicName: String = "";fun vnicName(block: () -> String?) { bVnicName = block() ?: "" }
		private var bVnicRx: BigInteger = BigInteger.ZERO;fun vnicRx(block: () -> BigInteger?) { bVnicRx = block() ?: BigInteger.ZERO }
		private var bVnicTx: BigInteger = BigInteger.ZERO;fun vnicTx(block: () -> BigInteger?) { bVnicTx = block() ?: BigInteger.ZERO }
		private var bRxTotalSpeed: BigInteger = BigInteger.ZERO;fun rxTotalSpeed(block: () -> BigInteger?) { bRxTotalSpeed = block() ?: BigInteger.ZERO }
		private var bTxTotalSpeed: BigInteger = BigInteger.ZERO;fun txTotalSpeed(block: () -> BigInteger?) { bTxTotalSpeed = block() ?: BigInteger.ZERO }
		private var bDescription: String = "";fun description(block: () -> String?) { bDescription = block() ?: "" }
		fun build(): NetworkVmVo = NetworkVmVo(bVmId, bVmName, bClusterId, bClusterName, bIpv4, bIpv6, bFqdn, bStatus, bVnicStatus, bVnicId, bVnicName, bVnicRx, bVnicTx, bRxTotalSpeed, bTxTotalSpeed, bDescription)
	}

	companion object {
		private val log by LoggerDelegate()
		inline fun builder(block: NetworkVmVo.Builder.() -> Unit): NetworkVmVo =
			NetworkVmVo.Builder().apply(block).build()
	}
}

fun Vm.toNetworkVmVo(conn: Connection, nic: Nic, statistics: List<Statistic>): NetworkVmVo {
	val cluster: Cluster? =
		conn.findCluster(this@toNetworkVmVo.cluster().id())
			.getOrNull()

	return NetworkVmVo.builder {
		vmId { this@toNetworkVmVo.id() }
		vmName { this@toNetworkVmVo.name() }
		status { if (this@toNetworkVmVo.statusPresent()) this@toNetworkVmVo.status() else VmStatus.UNKNOWN }
		fqdn { this@toNetworkVmVo.fqdn() }
		clusterName { cluster?.name() }
		description { this@toNetworkVmVo.description() }
		vnicStatus { nic.linked() }
		vnicId { nic.id() }
		vnicName { nic.name() }
		vnicRx { if (this@toNetworkVmVo.status() == VmStatus.UP) statistics.findSpeed("data.current.rx.bps") else null }
		vnicTx { if (this@toNetworkVmVo.status() == VmStatus.UP) statistics.findSpeed("data.current.tx.bps") else null }
		rxTotalSpeed { if (this@toNetworkVmVo.status() == VmStatus.UP) statistics.findSpeed("data.total.rx") else null }
		txTotalSpeed { if (this@toNetworkVmVo.status() == VmStatus.UP) statistics.findSpeed("data.total.tx") else null }
		ipv4 { if(this@toNetworkVmVo.reportedDevicesPresent()) this@toNetworkVmVo.reportedDevices().first().ips()[0].address() else null }
		ipv6 { if(this@toNetworkVmVo.reportedDevicesPresent()) this@toNetworkVmVo.reportedDevices().first().ips()[1].address() else null  }
	}
}