//package com.itinfo.service.engine;
//
//import com.itinfo.service.impl.BaseService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class WebsocketService extends BaseService {
//	@Autowired private SimpMessagingTemplate template;
//	public void sendMessage(String dest, String message) {
//		log.debug("sendMessage('{}', '{}') ...", dest, message);
//		template.convertAndSend(dest, message);
//	}
//}
//
