package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.gson
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.HostDevice
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(HostDeviceVo::class.java)

/**
 * [HostDeviceVo]
 *
 * @property name [String]
 * @property capability [String]
 * @property vendorId [String]
 * @property vendorName [String]
 * @property productId [String]
 * @property productName [String]
 * @property driver [String]
 */
class HostDeviceVo (
    val name: String = "",
    val capability: String = "",
    val vendorId: String = "",
    val vendorName: String = "",
    val productId: String = "",
    val productName: String = "",
    val driver: String = "",
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bName: String = ""; fun name(block: () -> String?) { bName = block() ?: ""}
        private var bCapability: String = ""; fun capability(block: () -> String?) { bCapability = block() ?: ""}
        private var bVendorId: String = ""; fun vendorId(block: () -> String?) { bVendorId = block() ?: ""}
        private var bVendorName: String = ""; fun vendorName(block: () -> String?) { bVendorName = block() ?: ""}
        private var bProductId: String = ""; fun productId(block: () -> String?) { bProductId = block() ?: ""}
        private var bProductName: String = ""; fun productName(block: () -> String?) { bProductName = block() ?: ""}
        private var bDriver: String = ""; fun driver(block: () -> String?) { bDriver = block() ?: ""}

        fun build(): HostDeviceVo = HostDeviceVo( bName, bCapability, bVendorId, bVendorName, bProductId, bProductName, bDriver)
    }

    companion object {
        inline fun builder(block: HostDeviceVo.Builder.() -> Unit): HostDeviceVo = HostDeviceVo.Builder().apply(block).build()
    }
}

fun HostDevice.toHostDeviceVo(conn: Connection): HostDeviceVo {
    return HostDeviceVo.builder {
        name { this@toHostDeviceVo.name() }
        capability { this@toHostDeviceVo.capability() }
        vendorName { if(this@toHostDeviceVo.vendorPresent()) "${this@toHostDeviceVo.vendor().name()} (${this@toHostDeviceVo.vendor().id()})" else ""}
        productName { if(this@toHostDeviceVo.productPresent())  "${this@toHostDeviceVo.product().name()} (${this@toHostDeviceVo.product().id()})" else "" }
        driver { this@toHostDeviceVo.driver() }
    }
}

fun List<HostDevice>.toHostDeviceVos(conn: Connection): List<HostDeviceVo> =
    this@toHostDeviceVos.map { it.toHostDeviceVo(conn) }