package com.handy.sql.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.handy.sql.controller.interceptor.UserWebSocketChannelInterceptor;
import com.handy.sql.controller.interceptor.WebSocketHandshakeInterceptor;

/**
 * 配置WebSocket
 */
@Configuration
//注解开启使用STOMP协议来传输基于代理(message broker)的消息,这时控制器支持使用@MessageMapping,就像使用@RequestMapping一样
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	
	// 注册STOMP协议的节点(endpoint),并映射指定的url
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 注册一个STOMP的endpoint,并指定使用SockJS协议
		registry.addEndpoint("/ws/system").addInterceptors(new WebSocketHandshakeInterceptor()).setAllowedOrigins("*");
		// .withSockJS() // 切换支持http或https方式，默认使用ws或wss访问
	}

	// 配置消息代理(Message Broker)
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 点对点应配置一个/user消息代理，广播式应配置一个/topic消息代理,群发（mass），单独聊天（alone）
//		registry.enableSimpleBroker("/topic");
		// 点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
		registry.setUserDestinationPrefix("/user");

//		 registry.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
//		WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
		registration.interceptors(new UserWebSocketChannelInterceptor());
	}
}