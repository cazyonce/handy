package com.handy.sql.netty.http.api;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseInfo {
	private Map<String, String> headers;
}
