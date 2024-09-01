package com.itinfo.itcloud.model.common

import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.gson
import java.io.Serializable

class DashTemplateVo (
    val template: IdentifiedVo = IdentifiedVo()
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bTemplate: IdentifiedVo = IdentifiedVo(); fun template(block: () -> IdentifiedVo?) { bTemplate = block() ?: IdentifiedVo() }

        fun build(): DashTemplateVo = DashTemplateVo(bTemplate)
    }
    companion object {
        inline fun builder(block: DashTemplateVo.Builder.() -> Unit): DashTemplateVo = DashTemplateVo.Builder().apply(block).build()
    }
}