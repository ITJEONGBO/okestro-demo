package com.itinfo.itcloud.model.response

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.gson
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable

/**
 * [Body]
 *
 * @property content [T] 응답 데이터
 */
data class Body<T> (
	@ApiModelProperty(example = "응답 데이터")
    val content: T? = null
): Serializable{
    override fun toString(): String =
        gson.toJson(this)

    class Builder<T> {
        private var bContent: T? = null;fun content(block: () -> T?) { bContent = block() }
        fun build(): Body<T> = Body(bContent)
    }
    
    companion object {
        private val log by LoggerDelegate()
        inline fun <reified T> builder(block: Body.Builder<T>.() -> Unit): Body<T> = Body.Builder<T>().apply(block).build()
        inline fun <reified T> success(crossinline block: () -> T): Body<T> = builder<T> {
            content { block() }
        }
        inline fun <reified T> fail(): Body<T> = builder {
            content { null }
        }
    }
}

