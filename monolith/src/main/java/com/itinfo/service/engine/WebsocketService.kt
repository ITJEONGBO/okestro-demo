package com.itinfo.service.engine

import com.itinfo.common.LoggerDelegate
import com.itinfo.service.impl.BaseService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class WebsocketService : BaseService() {

	@Autowired private lateinit var template: SimpMessagingTemplate

	fun sendMessage(dest: String, message: String?) {
		log.debug("sendMessage('$dest', '$message') ...",)
		template.convertAndSend(dest, message)
	}

	companion object {
		private val log by LoggerDelegate()
	}
}
