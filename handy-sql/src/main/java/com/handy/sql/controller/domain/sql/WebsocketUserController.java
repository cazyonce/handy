package com.handy.sql.controller.domain.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import com.handy.sql.consts.WebsocketUserRouterConsts;

@RestController
public class WebsocketUserController {

	@Autowired
	private SimpMessagingTemplate smt;

//	@Scheduled(fixedDelay = 3000)
	// sendto系列注解必须要在controller中和MassageMapp一起使用才有效
//	@MessageMapping("/send")
//	@SendTo("/topic/sub")
//	public String subscription() throws Exception {
////		System.out.println("========================");
////		System.out.println(session.getId());
////		smt.convertAndSend("/topic/sub", "开始推送消息了：" + "22211111112222222"+ LocalDateTime.now());
////		System.out.println("--------------------------");
//		return "2222222222222222222";
//	}

	// 单独聊天
	@MessageMapping("/aloneRequest")
	public void alone(String str, StompHeaderAccessor headerAccessor) {
//		 System.out.println(headerAccessor.getSubscriptionId());
//		System.out.println(((HttpSession)headerAccessor.getSessionAttributes().get("currentHttpSeesion")).getId());
//		session = ((HttpSession)headerAccessor.getSessionAttributes().get("currentHttpSeesion"));
//		System.out.println(getAccount());
//		System.out.println(headerAccessor.getSessionId());
//		System.out.println(headerAccessor.getUser());
//		System.out.println(str);
//		smt.convertAndSendToUser(headerAccessor.getUser().getName(), "/topic/sub",  new ResponseData<String>().success("登录通知测试"));
		smt.convertAndSendToUser(headerAccessor.getUser().getName(), WebsocketUserRouterConsts.SINGIN_NOTIFY, "登录通知测试");
//		register(headerAccessor).notifySubscribers(WebsocketUserRouterConsts.SINGIN_NOTIFY, new ResponseData<String>().success("登录通知测试"));
	}
}