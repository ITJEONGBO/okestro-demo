package com.itinfo.service.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebsocketService {
	@Autowired private SimpMessagingTemplate template;
	public void sendMessage(String dest, String message) {
		template.convertAndSend(dest, message);
	}
}

