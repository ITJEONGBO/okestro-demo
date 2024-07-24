package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.gson
import com.itinfo.util.ovirt.findAllDataCenters
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.DataCenter
import org.ovirt.engine.sdk4.types.OpenStackNetworkProvider
import java.io.Serializable

/**
 * [OpenStackNetworkVo]
 * 외부 네트워크 공급자
 *
 * 가져올 네트워크
 * @property id [String]
 * @property name [String]
 *
 * 네트워크가 속해있는 dc
// * @property dataCenterVos List<[DataCenterVo]>
 */
class OpenStackNetworkVo (
    val id: String = "",
    val name: String = "",
//    val dataCenterVos: List<DataCenterVo> = listOf(),
) : Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
//        private var bDataCenterVos: List<DataCenterVo> = listOf();fun dataCenterVos(block: () -> List<DataCenterVo>?) { bDataCenterVos = block() ?: listOf() }

        fun build(): OpenStackNetworkVo = OpenStackNetworkVo(bId, bName,/*bDataCenterVos*/)
    }

    companion object {
        inline fun builder(block: OpenStackNetworkVo.Builder.() -> Unit): OpenStackNetworkVo =
            OpenStackNetworkVo.Builder().apply(block).build()
    }
}

fun OpenStackNetworkProvider.toOpenStackNetworkVoIdName(): OpenStackNetworkVo = OpenStackNetworkVo.builder {
    id { this@toOpenStackNetworkVoIdName.id() }
    name { this@toOpenStackNetworkVoIdName.name() }
}

fun OpenStackNetworkProvider.toOpenStackNetworkVo(conn: Connection): OpenStackNetworkVo {
    val dataCenters: List<DataCenter> = conn.findAllDataCenters()
        .getOrDefault(listOf())

    return OpenStackNetworkVo.builder {
        id { this@toOpenStackNetworkVo.id() }
        name { this@toOpenStackNetworkVo.name() }
//        dataCenterVos { dataCenters.toDataCenterIdNames() }
    }
}
