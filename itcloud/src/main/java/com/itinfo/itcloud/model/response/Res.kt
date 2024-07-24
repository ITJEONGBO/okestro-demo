package com.itinfo.itcloud.model.response

import com.itinfo.common.LoggerDelegate
import java.io.Serializable

data class Res<T>(
    val head: Head = Head(),
    val body: Body<T>? = null,
) : Serializable {
	class Builder<T> {
        private var bHead: Head = Head();fun head(block: () -> Head?) { bHead = block() ?: Head() }
        private var bBody: Body<T>? = null;fun body(block: () -> Body<T>?) { bBody = block() }
        fun build(): Res<T> = Res(bHead, bBody)
	}
    companion object {
		private val log by LoggerDelegate()
		inline fun <reified T> builder(block: Res.Builder<T>.() -> Unit): Res<T> = Res.Builder<T>().apply(block).build()
		inline fun <reified T> success(crossinline block: () -> T): Res<T> = builder {
			head { Head.success() }
			Body.success { block() }
		}
		inline fun <reified T> fail(code: Int = 400, message: String? = ""): Res<T> = builder {
			head { Head.fail(code ,message) }
		}
		inline fun <reified T> fail(e: Throwable?): Res<T> = builder {
			head { Head.fail(400, e?.localizedMessage) }
		}
		inline fun <reified T> safely(crossinline block: () -> T): Res<T> = try {
			success { block() }
		} catch (e: Error) {
			fail(e)
		}

		/*
		fun fail(e: Throwable?): Res<Boolean> =
			builder<Boolean> {
				head {
					Head.builder {
						code { 404 }
						message { e?.localizedMessage }
					}
				}
				body {
					Body.builder {
						content { false }
					}
				}
			}

        fun fail(code: Int = 400, message: String = ""): Res<Boolean> =
			builder<Boolean> {
				head {
					Head.builder {
						code { 404 }
						message { message }
					}
				}
				body {
					Body.builder {
						content { false }
					}
				}
			}
		*/

        fun duplicateResponse(name: String): Res<Boolean> {
            val head = Head(404,"중복된 이름입니다.")
            val body = Body(false)
            log.error("중복된 이름 : {}", name)
            return Res(head, body)
        }

        fun createResponse(): Res<Boolean> {
            val head = Head(201, "생성 성공")
            val body = Body(true)
            return Res(head, body)
        }

        fun successResponse(): Res<Boolean> {
            val head = Head.success()
            val body = Body(true)
            return Res(head, body)
        }
    }
}