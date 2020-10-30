package com.handy.sql.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handy.sql.service.SQLTestController;

@ServerEndpoint(value = "/ws/end_point", configurator = GetHttpSessionConfigurator.class)
@Component
public class WebSocketServer {
	/** concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。 */
//    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
	/** 与某个客户端的连接会话，需要通过它来给客户端发送数据 */
	private Session session;
	private HttpSession httpSession;
	private String sessionId;
	private JdbcTemplate jdbcTemplate;

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		this.session = session;
		sessionId = httpSession.getId();
		jdbcTemplate = SpringUtil.getBean(JdbcTemplate.class);
		RequestMappingHandlerMapping requestMappingHandlerMapping = SpringUtil.getBean(RequestMappingHandlerMapping.class);
		Method getMappingForMethod =ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "getMappingForMethod",Method.class,Class.class);
		//设置私有属性为可见
		getMappingForMethod.setAccessible(true);
		//获取类中的方法
		Method[] method_arr = SQLTestController.class.getMethods();
		for (Method method : method_arr) {
		        //判断方法上是否有注解RequestMapping
			if (method.getAnnotation(GetMapping.class) != null) {
				System.out.println("99999999999");
			        //获取到类的RequestMappingInfo 
				RequestMappingInfo mappingInfo;
				try {
					mappingInfo = (RequestMappingInfo) getMappingForMethod.invoke(requestMappingHandlerMapping, method,SQLTestController.class);
					System.out.println(mappingInfo);
					requestMappingHandlerMapping.registerMapping(mappingInfo, SQLTestController.class.newInstance(),method);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
//		requestMappingHandlerMapping.registerMapping(new RequestMappingInfo, controllerClass.newInstance(),method);
		sendMessage("连接成功");
	}
public static void main(String[] args) {
	Class ss = Method.class.getClass();
//	ss.getMethod(name, parameterTypes)
//	try {
//	Constructor c=ss.getConstructor(String.class,int.class);
		for (Constructor string : ss.getConstructors()) {
			System.out.println(string.getName());
		}
//		Method method = ss.getMethod("Method");
//		System.out.println(method.getName());
//	} catch (NoSuchMethodException | SecurityException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	ss.getMethod("Method", null, null, null, null, null,0,0,null,
//			new byte[] {},new byte[] {},new byte[] {});
}
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
//        webSocketMap.remove(sessionId);
		System.out.println("连接关闭");
	}

	static String[] EXECUTE_WITH_SQL = { "create", "alter", "drop", "insert", "delete" };

	static String[] QYERY_FOR_MAP_SQL = { "desc", "show", "select" };

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String sql, Session session) {
		System.out.println("用户消息:" + sessionId + ",报文:" + sql);
		
//    	String sql = "create table test_aa (`runoob_author` VARCHAR(40) NOT NULL);";
		if (sql == null || "".equals(sql = sql.trim().toLowerCase())) {
			sendMessage("sql is empty!");
			return;
		}
		try {

			for (String string : EXECUTE_WITH_SQL) {

				if (sql.startsWith(string)) {
					jdbcTemplate.execute(sql);
					sendMessage("run sql success!");
					return;
				}
			}
			for (String string : QYERY_FOR_MAP_SQL) {
				if (sql.startsWith(string)) {
					ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
					sendMessage(objectMapper.writeValueAsString(jdbcTemplate.queryForList(sql)));
					return;
				}
			}
		} catch (Exception e) {

			sendMessage("run sql '" + sql + "' error: " + e.getMessage());
		}

	}

	/**
	 *
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("用户错误:" + sessionId + ",原因:" + error.getMessage());
		error.printStackTrace();
	}

	/**
	 * 实现服务器主动推送
	 */
	public void sendMessage(String message) {
		try {

			this.session.getBasicRemote().sendText(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送自定义消息
	 */
//    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
//        System.out.println("发送消息到:"+userId+"，报文:"+message);
//        if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
//            webSocketMap.get(userId).sendMessage(message);
//        }else{
//            System.out.println("用户"+userId+",不在线！");
//        }
//    }
	public static void controlCenter(Class controllerClass,ApplicationContext  Context,Integer type) throws IllegalAccessException, Exception{
		//获取RequestMappingHandlerMapping 
		RequestMappingHandlerMapping requestMappingHandlerMapping=(RequestMappingHandlerMapping) Context.getBean("requestMappingHandlerMapping");
		Method getMappingForMethod =ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "getMappingForMethod",Method.class,Class.class);
		//设置私有属性为可见
		getMappingForMethod.setAccessible(true);
		//获取类中的方法
		Method[] method_arr = controllerClass.getMethods();
		for (Method method : method_arr) {
		        //判断方法上是否有注解RequestMapping
			if (method.getAnnotation(RequestMapping.class) != null) {
			        //获取到类的RequestMappingInfo 
				RequestMappingInfo mappingInfo = (RequestMappingInfo) getMappingForMethod.invoke(requestMappingHandlerMapping, method,controllerClass);
				if(type == 1){
				        //注册
					registerMapping(requestMappingHandlerMapping, mappingInfo, controllerClass, method);
				}else if(type == 2){
				        //取消注册
					unRegisterMapping(requestMappingHandlerMapping, mappingInfo);
					registerMapping(requestMappingHandlerMapping, mappingInfo, controllerClass, method);
				}else if(type == 3){
					unRegisterMapping(requestMappingHandlerMapping, mappingInfo);
				}
				
			}
		}
	}
	
	/**
	 * 
	* registerMapping(注册mapping到spring容器中)    
	* @param   requestMappingHandlerMapping    
	* @Exception 异常对象    
	* @since  CodingExample　Ver(编码范例查看) 1.1
	* @author jiaxiaoxian
	 */
	public static void registerMapping(RequestMappingHandlerMapping requestMappingHandlerMapping,RequestMappingInfo mappingInfo, Class controllerClass, Method method) throws Exception, IllegalAccessException{
		requestMappingHandlerMapping.registerMapping(mappingInfo, controllerClass.newInstance(),method);
	}
	
	/**
	 * 
	* unRegisterMapping(spring容器中删除mapping)    
	* @param   requestMappingHandlerMapping    
	* @Exception 异常对象    
	* @since  CodingExample　Ver(编码范例查看) 1.1
	* @author jiaxiaoxian
	 */
	public static void unRegisterMapping(RequestMappingHandlerMapping requestMappingHandlerMapping,RequestMappingInfo mappingInfo) throws Exception, IllegalAccessException{
		requestMappingHandlerMapping.unregisterMapping(mappingInfo);
	}
}