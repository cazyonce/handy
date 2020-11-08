package com.handy.sql.netty.http.info;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseInfo {
	private Map<String, String> headers;
}
