package com.itinfo.itcloud.controller.common

import com.itinfo.itcloud.socket.WSMessage
import io.swagger.annotations.Api
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
@Api(tags = ["Notify"])
class NotifyController {

	@MessageMapping("/notify")	/* 클라이언트 입장에서 메세지 보내는 곳 (e.g. stompClient.send('/app/notify', {}, JSON.stringify(chatMessage))) */
	@SendTo("/topic/public")	/* 클라이언트 입장에서 메세지 보이는 곳 (e.g. stompClient.subscribe('/topic/public', () => { ... }) */
	fun sendMessage(wsMessage: WSMessage): WSMessage = wsMessage

}