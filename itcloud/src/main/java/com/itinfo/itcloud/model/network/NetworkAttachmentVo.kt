package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.IdentifiedVo
import java.io.Serializable

/**
 * [NetworkAttachmentVo]
 *
 * @property id [String]
 * @property inSync [Boolean]
 * @property ipAddressAssignments List[IpAddressAssignmentVo]
 * @property hostVo [IdentifiedVo]
 * @property hostNicVo [IdentifiedVo]
 * @property networkVo [IdentifiedVo]
 */
class NetworkAttachmentVo (
    val id: String = "",
    val inSync : Boolean = false,
    val ipAddressAssignments: List<IpAddressAssignmentVo>,
    val hostVo: IdentifiedVo = IdentifiedVo(),
    val hostNicVo: IdentifiedVo = IdentifiedVo(),
    val networkVo: IdentifiedVo = IdentifiedVo(),
    // reported_configurations
) : Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = ""; fun id(block: () -> String?) { bId = block() ?: "" }
        private var bInSync: Boolean = false; fun inSync(block: () -> Boolean?) { bInSync = block() ?: false }
        private var bIpAddressAssignments: List<IpAddressAssignmentVo> = listOf(); fun ipAddressAssignments(block: () -> List<IpAddressAssignmentVo>?) { bIpAddressAssignments = block() ?: listOf() }
        private var bHostVo: IdentifiedVo = IdentifiedVo(); fun hostVo(block: () -> IdentifiedVo?) { bHostVo = block() ?: IdentifiedVo() }
        private var bHostNicVo: IdentifiedVo = IdentifiedVo(); fun hostNicVo(block: () -> IdentifiedVo?) { bHostNicVo = block() ?: IdentifiedVo() }
        private var bNetworkVo: IdentifiedVo = IdentifiedVo(); fun networkVo(block: () -> IdentifiedVo?) { bNetworkVo = block() ?: IdentifiedVo() }

        fun build(): NetworkAttachmentVo = NetworkAttachmentVo(bId, bInSync, bIpAddressAssignments, bHostVo, bHostNicVo, bNetworkVo)
    }

    companion object {
        inline fun builder(block: NetworkAttachmentVo.Builder.() -> Unit): NetworkAttachmentVo = NetworkAttachmentVo.Builder().apply(block).build()
    }
}