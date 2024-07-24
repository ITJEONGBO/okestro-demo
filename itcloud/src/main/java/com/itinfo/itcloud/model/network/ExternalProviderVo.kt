package com.itinfo.itcloud.model.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.gson
import com.itinfo.util.ovirt.findAllExternalNetworkProviders
import com.itinfo.util.ovirt.findAllOpenStackNetworkProviders
import com.itinfo.util.ovirt.findExternalProvider
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.ExternalProvider
import java.io.Serializable

class ExternalProviderVo (
    val id: String = "",
    val name: String = ""
//    val networkVos: List<NetworkVo>
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
        fun build(): ExternalProviderVo = ExternalProviderVo(bId, bName)
    }

    companion object {
        private val log by LoggerDelegate()
        inline fun builder(block: ExternalProviderVo.Builder.() -> Unit): ExternalProviderVo =
            ExternalProviderVo.Builder().apply(block).build()
    }
}

fun ExternalProvider.toExternalProviderVo(conn: Connection): ExternalProviderVo {
    // 굳이 조회를해서 ????? 이렇게????
    /*
    val externalProvider: ExternalProvider? =
        conn.findAllOpenStackNetworkProviders()
            .getOrDefault(listOf())
            .firstOrNull()
    */
    return ExternalProviderVo.builder {
        id { this@toExternalProviderVo.id() }
        name { this@toExternalProviderVo.name() }
    }
}