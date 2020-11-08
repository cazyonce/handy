package com.handy.sql.netty.http.info;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestInfo {
	
	private String path;
	
	private String method;
	
	private Map<String, String> headers;
}
