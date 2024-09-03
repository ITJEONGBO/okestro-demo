package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.model.computing.ClusterVo
import com.itinfo.itcloud.gson
import java.io.Serializable

/**
 * [NetworkPropertyVo]
 * 네트워크 생성 - 클러스터 목록에서 연결, 필수 정하는
 *
 * attached- T => required- T/F
 * attached- F => required- F
 *
 * @property attached [Boolean] 연결
 * @property required [Boolean] 필수
 */
class NetworkPropertyVo (
    val attached: Boolean = false,
    val required: Boolean = false,
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bAttached: Boolean = false; fun attached(block: () -> Boolean?) { bAttached = block() ?: false}
        private var bRequired: Boolean = false; fun required(block: () -> Boolean?) { bRequired = block() ?: false}

        fun build(): NetworkPropertyVo = NetworkPropertyVo(bAttached, bRequired)
    }

    companion object {
        inline fun builder(block: NetworkPropertyVo.Builder.() -> Unit): NetworkPropertyVo = NetworkPropertyVo.Builder().apply(block).build()
    }
}