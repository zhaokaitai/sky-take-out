package com.sky.task;

import com.sky.websocket.WebSocketServer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author zkt
 */
@Component
public class WebSocketTask {
	@Resource
	private WebSocketServer webSocketServer;
	
	/**
	 * 通过WebSocket每隔5秒向客户端发送消息
	 */
	// @Scheduled(cron = "0/5 * * * * ?")
	public void sendMessageToClient() {
		webSocketServer.sendToAllClient("这是来自服务端的消息：" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
	}
}
