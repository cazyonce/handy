package com.handy.sql.netty.http.info;

import io.netty.handler.codec.http.HttpHeaders;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseInfo{
	private HttpHeaders response;
	
}
