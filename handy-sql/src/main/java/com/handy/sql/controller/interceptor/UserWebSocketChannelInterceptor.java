package com.handy.sql.controller.interceptor;

import java.util.Map;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

public class UserWebSocketChannelInterceptor implements ChannelInterceptor {

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
//		System.out.println(channel);
//		System.out.println(message);
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
		// 1. 判断是否首次连接请求
		System.out.println(accessor.getCommand());
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			System.out.println("第一次连接");
		} else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
			System.out.println("连接关闭");
		}
		// 不是首次连接，已经成功登陆
		return message;
	}
}