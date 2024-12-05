package com.itinfo.itcloud.model.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.gson
import org.ovirt.engine.sdk4.types.IscsiDetails
import java.io.Serializable

class IscsiDetailVo(
    val address: String = "",
    val port: Int = 0,
    val portal: String = "",
    val target: String = "",
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bAddress: String = "";fun address(block: () -> String?) { bAddress = block() ?: "" }
        private var bPort: Int = 0;fun port(block: () -> Int?) { bPort = block() ?: 0 }
        private var bPortal: String = "";fun portal(block: () -> String?) { bPortal = block() ?: "" }
        private var bTarget: String = "";fun target(block: () -> String?) { bTarget = block() ?: "" }
        fun build(): IscsiDetailVo = IscsiDetailVo(bAddress, bPort, bPortal, bTarget)
    }

    companion object {
        private val log by LoggerDelegate()
        inline fun builder(block: Builder.() -> Unit): IscsiDetailVo = Builder().apply(block).build()
    }
}

fun IscsiDetails.toIscsiDetailVo(): IscsiDetailVo {
    return IscsiDetailVo.builder {
        address { this@toIscsiDetailVo.address() }
        port { this@toIscsiDetailVo.portAsInteger() }
        portal { this@toIscsiDetailVo.portal() }
        target { this@toIscsiDetailVo.target() }
    }
}
fun List<IscsiDetails>.toIscsiDetailVos(): List<IscsiDetailVo> =
    this@toIscsiDetailVos.map { it.toIscsiDetailVo() }