package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.gson
import org.ovirt.engine.sdk4.builders.OptionBuilder
import org.ovirt.engine.sdk4.types.Option
import java.io.Serializable

class OptionVo (
    val name: String = "",
    val value: String = "",
) : Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
        private var bValue: String = "";fun value(block: () -> String?) { bValue = block() ?: "" }

        fun build(): OptionVo = OptionVo(bName, bValue)
    }

    companion object {
        inline fun builder(block: OptionVo.Builder.() -> Unit): OptionVo = OptionVo.Builder().apply(block).build()
    }
}

fun OptionVo.toOptionBuilder(): Option {
    return OptionBuilder()
        .name(this@toOptionBuilder.name)
        .value(this@toOptionBuilder.value)
        .build()
}