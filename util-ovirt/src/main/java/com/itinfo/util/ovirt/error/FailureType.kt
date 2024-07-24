package com.itinfo.util.ovirt.error

import com.itinfo.util.ovirt.log
import java.util.concurrent.ConcurrentHashMap

enum class FailureType(
	val code: Int,
	val message: String,
) {
	BAD_REQUEST(400, "요청 불량"),
	NOT_FOUND(404, "찾을 수 없는"),
	ID_NOT_FOUND(404, "ID 없음"),
	DUPLICATE(409, "이름 중복"),
	UNPROCESSABLE_CONTENT(422, "다룰 수 없는 컨텐츠"),
	UNKNOWN(499, "알 수 없는 오류");
	companion object {
		private val findMap: MutableMap<Int, FailureType> = ConcurrentHashMap<Int, FailureType>()
		init {
			values().forEach { findMap[it.code] = it }
		}
		@JvmStatic fun findByCode(code: Int): FailureType = findMap[code] ?: UNKNOWN
	}
}

fun FailureType.toError(target: String): Error {
	log.error("toError ... target: {}", this@toError.message)
	return when(this@toError) {
		FailureType.NOT_FOUND -> Error("${this@toError.message} $target")
		FailureType.DUPLICATE, FailureType.UNPROCESSABLE_CONTENT -> Error("$target ${this@toError.message}")
		else -> Error(this@toError.message)
	}
}

inline fun <reified T> Error.toFailure(): Result<T> {
	return Result.failure(this@toFailure)
}

inline fun <reified T> FailureType.toResult(target: String) = this@toResult.toError(target).toFailure<T>()