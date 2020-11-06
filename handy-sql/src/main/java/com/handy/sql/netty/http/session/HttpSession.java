package com.handy.sql.netty.http.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import lombok.Getter;

public class HttpSession {

	private static final ConcurrentHashMap<String, HttpSession> SESSION_MAP = new ConcurrentHashMap<String, HttpSession>();
	private static final String CLIENT_COOKIE_NAME = "JSESSIONID";

	@Getter
	private Cookie cookie;

	private Map<String, Object> attributes = new HashMap<>();

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public void addAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	public String getId() {
		return cookie.value();
	}

	private HttpSession() {
	}

	private static String createSessionId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	private static Cookie findCookie(FullHttpRequest request) {
		String cookieValue = request.headers().get(HttpHeaderNames.COOKIE);
		if (cookieValue == null) {
			return null;
		}
		Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieValue);
		for (Cookie cookie : cookies) {
			if (CLIENT_COOKIE_NAME.equals(cookie.name())) {
				return cookie;
			}
		}
		return null;
	}

	private static HttpSession createDufaultHttpSession(String sessionId) {
		HttpSession session = new HttpSession();
		// TODO: 这个要创建Cookie需要通过cookie配置来进行创建，暂时就创建一个默认的
		session.cookie = new DefaultCookie(CLIENT_COOKIE_NAME, sessionId);
		session.cookie.setPath("/");
		return session;
	}

	/**
	 * 这个方法一次连接只能调用一次，防止在请求没有携带Cookie信息时，多次调用当前方法存储多个Session对象
	 * 
	 * @param request
	 * @return
	 */
	public static HttpSession getHttpSession(FullHttpRequest request) {
		Cookie cookie = findCookie(request);

		// 请求头中未携带Cookie表示这是一次新的请求
		if (cookie == null) {
			HttpSession session = createDufaultHttpSession(createSessionId());
			SESSION_MAP.put(session.getId(), session);
			return session;
		}

		HttpSession session = SESSION_MAP.get(cookie.value());
		if (session == null) {
			SESSION_MAP.put(cookie.value(), session = createDufaultHttpSession(cookie.value()));
		}
		return session;
	}
}