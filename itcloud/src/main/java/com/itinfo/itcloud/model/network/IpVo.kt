package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.gson
import org.ovirt.engine.sdk4.builders.IpBuilder
import org.ovirt.engine.sdk4.types.Ip
import org.ovirt.engine.sdk4.types.IpVersion
import java.io.Serializable

class IpVo (
    val address: String = "",
    val gateway: String = "",
    val version: IpVersion = IpVersion.V4,
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bAddress: String = "";fun address(block: () -> String?) { bAddress = block() ?: "" }
        private var bGateway: String = "";fun gateway(block: () -> String?) { bGateway = block() ?: "" }
        private var bVersion: IpVersion = IpVersion.V4;fun version(block: () -> IpVersion?) { bVersion = block() ?: IpVersion.V4 }

        fun build(): IpVo = IpVo(bAddress, bGateway, bVersion)
    }

    companion object {
        inline fun builder(block: IpVo.Builder.() -> Unit): IpVo = IpVo.Builder().apply(block).build()
    }
}

fun IpVo.toIpBuilder(): Ip =
    IpBuilder()
        .address(this@toIpBuilder.address)
        .gateway(this@toIpBuilder.gateway)
        .version(this@toIpBuilder.version)
        .build()