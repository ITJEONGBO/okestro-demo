package com.itinfo.itcloud.model.common

import com.itinfo.itcloud.model.gson
import org.ovirt.engine.sdk4.types.Network
import java.io.Serializable

class DashNetworkVo (
    val id: String = "",
    val name: String = "",

): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }

        fun build(): DashNetworkVo = DashNetworkVo(bId, bName)
    }
    companion object {
        inline fun builder(block: DashNetworkVo.Builder.() -> Unit): DashNetworkVo = DashNetworkVo.Builder().apply(block).build()
    }
}

fun Network.toNetwork(): DashNetworkVo = DashNetworkVo.builder {
        id { this@toNetwork.id() }
        name { this@toNetwork.name() }
}
fun List<Network>.toNetworks(): List<DashNetworkVo> =
    this@toNetworks.map { it.toNetwork() }