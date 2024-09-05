package com.itinfo.itcloud.service.common

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.service.BaseService
import com.itinfo.itcloud.socket.WSMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

interface ItWsNotifyService {
	fun notify(wsMessage: WSMessage)
}

@Service
class NotifyServiceImpl(

): BaseService(), ItWsNotifyService {
	@Autowired private lateinit var msgTemplate: SimpMessagingTemplate

	override fun notify(wsMessage: WSMessage) {
		sendMessage(gson.toJson(wsMessage))
	}

	private fun sendMessage(msg: String, destination: String = "/topic/public") {
		log.debug("sendMessage('$destination', '$msg') ...",)
		if (destination.contains("reload")) {
			msgTemplate.convertAndSend(destination, "")
			return
		}
		msgTemplate.convertAndSend(destination, msg)
	}

	companion object {
		private val log by LoggerDelegate()
	}
}