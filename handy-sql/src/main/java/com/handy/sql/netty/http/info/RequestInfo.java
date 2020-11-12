package com.handy.sql.netty.http.info;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class RequestInfo {
	
	private final String path;
	
	private final String method;
	
	private final Map<String, String> headers = new HashMap<String, String>();
	
	public void addHeader(String key, String value) {
		headers.put(key, value);
	}

	public RequestInfo(String path, String method) {
		this.path = path;
		this.method = method;
	}
}
