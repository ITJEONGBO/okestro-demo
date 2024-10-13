package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.gson
import java.io.Serializable

class IpAddressAssignmentVo (
    val assignmentMethod: String = "",
    val ipVo: IpVo = IpVo()
) : Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
        private var bIpVo: IpVo = IpVo();fun ipVo(block: () -> IpVo?) { bIpVo = block() ?: IpVo() }

        fun build(): IpAddressAssignmentVo = IpAddressAssignmentVo(bName, bIpVo)
    }

    companion object {
        inline fun builder(block: IpAddressAssignmentVo.Builder.() -> Unit): IpAddressAssignmentVo = IpAddressAssignmentVo.Builder().apply(block).build()
    }
}
