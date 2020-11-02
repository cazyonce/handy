package com.handy.sql.config;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		sendMessage("连接成功");
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
	public void onMessage(String message, Session session) {
		System.out.println("用户消息:" + sessionId + ",报文:" + message);

//    	String sql = "create table test_aa (`runoob_author` VARCHAR(40) NOT NULL);";
		if (message == null || "".equals(message = message.trim().toLowerCase())) {
			sendMessage("sql is empty!");
			return;
		}
		final String sql = message;
		String sqlLog = "insert into system_sql_log(sql_str, status, remark, create_time) values(?, ?, ?, now())";
		try {
			processMessage(sql);
			try {
				jdbcTemplate.update(sqlLog, new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement preparedStatement) throws SQLException {
						preparedStatement.setString(1, sql);
						preparedStatement.setInt(2, 0);
						preparedStatement.setString(3, "run success");
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			jdbcTemplate.update(sqlLog, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1, sql);
					preparedStatement.setInt(2, 1);
					preparedStatement.setString(3, e.getMessage());
				}
			});
			sendMessage("sql error: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void processMessage(String sql) throws Exception {

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
		throw new Exception("The unknown SQL");
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
}