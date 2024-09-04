package com.itinfo.itcloud.socket

import com.itinfo.itcloud.gson
import com.itinfo.util.ovirt.Term
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.time.LocalDateTime

private val log = LoggerFactory.getLogger(WSMessage::class.java)

class WSMessage(
	val title: String = "",
	val content: String = "",
	val tag: WSMessageTag? = WSMessageTag.UNKNOWN,
	val date: LocalDateTime? = LocalDateTime.now(),
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

	class Builder {
		private var bTitle: String = "";fun title(block: () -> String?) { bTitle = block() ?: "" }
		private var bContent: String = "";fun content(block: () -> String?) { bContent = block() ?: "" }
		private var bTag: WSMessageTag? = WSMessageTag.UNKNOWN;fun tag(block: () -> WSMessageTag?) { bTag = block() ?: WSMessageTag.UNKNOWN }
		private var bLocalDateTime: LocalDateTime? = LocalDateTime.now();fun localDateTime(block: () -> LocalDateTime?) { bLocalDateTime = block() ?: LocalDateTime.now() }
		fun build(): WSMessage = WSMessage(bTitle, bContent, bTag, bLocalDateTime)
    }

    companion object {
        inline fun builder(block: WSMessage.Builder.() -> Unit): WSMessage = WSMessage.Builder().apply(block).build()

    }
}


fun Term.simpleNotify(isSuccess: Boolean = true, title: String = "", content: String): WSMessage = when(this) {
	else -> WSMessage.builder {
		title { title + if (isSuccess) " 완료" else " 실패" }
		content { "$content 처리가 " + if (isSuccess) "완료되었습니다." else "실패하였습니다."   }
		tag { if (isSuccess) WSMessageTag.SUCCESS else WSMessageTag.ERROR }
	}
}

enum class WSMessageTag {
	SUCCESS,
	ERROR,
	WARNING,
	UNKNOWN
}