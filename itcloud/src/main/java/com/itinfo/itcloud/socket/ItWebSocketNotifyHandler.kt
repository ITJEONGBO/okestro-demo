package com.itinfo.itcloud.socket

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.service.common.ItWsNotifyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class ItWebSocketNotifyHandler : TextWebSocketHandler() {
	@Autowired private lateinit var wsNotify: ItWsNotifyService

	override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
		log.info("handleTextMessage ... message: {}", message)
		val payload = message.payload
		TODO("NOT IMPLEMENTED")
		// val chatMessage: ChatMessage = Util.Chat.resolvePayload(payload)
		// chatService.handleAction(chatMessage.getRoomId(), session, chatMessage)
	}

	companion object {
		private val log by LoggerDelegate()
	}
}