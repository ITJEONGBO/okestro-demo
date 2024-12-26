package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.gson
import org.ovirt.engine.sdk4.builders.OptionBuilder
import org.ovirt.engine.sdk4.types.Option
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(OptionVo::class.java)

class OptionVo (
    val name: String = "",
    val value: String = "",
    val type: String = "",
) : Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder {
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
        private var bValue: String = "";fun value(block: () -> String?) { bValue = block() ?: "" }
        private var bType: String = ""; fun type(block: () -> String?) { bType = block() ?: "" }

        fun build(): OptionVo = OptionVo(bName, bValue, bType)
    }

    companion object {
        inline fun builder(block: OptionVo.Builder.() -> Unit): OptionVo = OptionVo.Builder().apply(block).build()
    }
}

fun Option.toOptionVo(): OptionVo {
    return OptionVo.builder {
        name { this@toOptionVo.name() }
        value { this@toOptionVo.value() }
        type { this@toOptionVo.type()?.takeIf { this@toOptionVo.typePresent() } }
    }
}
fun List<Option>.toOptionVos(): List<OptionVo> =
    this@toOptionVos.map { it.toOptionVo() }

fun OptionVo.toOptionBuilder(): Option {
    return OptionBuilder()
        .name(this@toOptionBuilder.name)
        .value(this@toOptionBuilder.value)
        .type(this@toOptionBuilder.type)
        .build()
}
