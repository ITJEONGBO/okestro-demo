package com.itinfo.itcloud.controller

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.socket.messaging.WebSocketStompClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotifyControllerTest {
	@Autowired
	lateinit var restTemplate: TestRestTemplate

	private val url = "ws://localhost:8080/ws"

	@Test
	fun `test send message through WebSocket`() {
		val stompClient = WebSocketStompClient(StandardWebSocketClient())
		stompClient.messageConverter = MappingJackson2MessageConverter()

		val stompHeaders = WebSocketHttpHeaders()
		val stompSession = stompClient.connect(url, stompHeaders, object : StompSessionHandlerAdapter() {}).get(1, TimeUnit.SECONDS)

		val messageQueue: BlockingQueue<WSMessage> = LinkedBlockingDeque()

		stompSession.subscribe("/topic/public", object : StompFrameHandler {
			override fun getPayloadType(stompHeaders: StompHeaders): Type = WSMessage::class.java

			override fun handleFrame(stompHeaders: StompHeaders, payload: Any?) {
				messageQueue.offer(payload as WSMessage)
			}
		})

		val wsMessage = WSMessage("Test message")
		stompSession.send("/app/notify", wsMessage)

		val receivedMessage = messageQueue.poll(1, TimeUnit.SECONDS)

		assertNotNull(receivedMessage)
		assertEquals(wsMessage, receivedMessage)
	}
}