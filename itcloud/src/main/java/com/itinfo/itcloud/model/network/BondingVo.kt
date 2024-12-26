package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.fromHostNicToIdentifiedVo
import com.itinfo.itcloud.model.fromHostNicsToIdentifiedVos
import com.itinfo.util.ovirt.findNicFromHost
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Bonding
import org.ovirt.engine.sdk4.types.Host
import org.ovirt.engine.sdk4.types.HostNic
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(BondingVo::class.java)

class BondingVo (
    val activeSlave: IdentifiedVo = IdentifiedVo(),  // hostNicvo
    val optionVos: List<OptionVo> = listOf(),
    val slaves: List<IdentifiedVo> = listOf(),  // hostNic
): Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder{
        private var bActiveSlave: IdentifiedVo = IdentifiedVo(); fun activeSlave(block: () -> IdentifiedVo?) { bActiveSlave = block() ?: IdentifiedVo() }
        private var bOptionVos: List<OptionVo> = listOf(); fun optionVos(block: () -> List<OptionVo>?) { bOptionVos = block() ?: listOf() }
        private var bSlaves: List<IdentifiedVo> = listOf(); fun slaves(block: () -> List<IdentifiedVo>?) { bSlaves = block() ?: listOf() }

        fun build(): BondingVo = BondingVo(bActiveSlave, bOptionVos, bSlaves)
    }

    companion object{
        inline fun builder(block: BondingVo.Builder.() -> Unit): BondingVo =  BondingVo.Builder().apply(block).build()
    }
}

fun Bonding.toBondingVo(conn: Connection, hostId: String): BondingVo {
    // Extract slave HostNics using the provided connection
    val slaves = if (this@toBondingVo.slavesPresent()) {
        this@toBondingVo.slaves().mapNotNull { hostNic ->
            // Extract the id from each slave
            val slaveId = hostNic.id()
            val nic = conn.findNicFromHost(hostId, slaveId).getOrNull()
            nic?.fromHostNicToIdentifiedVo()
        }
    } else listOf()

    return BondingVo.builder {
        activeSlave {
            if (this@toBondingVo.activeSlavePresent()) {
                val activeSlaveId = this@toBondingVo.activeSlave().id()
                val nic = conn.findNicFromHost(hostId, activeSlaveId).getOrNull()
                nic?.fromHostNicToIdentifiedVo()
            } else null
        }
        optionVos {
            if (this@toBondingVo.optionsPresent()) this@toBondingVo.options().toOptionVos() else listOf()
        }
        slaves { slaves }
    }
}
