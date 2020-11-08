package com.handy.sql.netty.http.api.processor.system;

import com.handy.sql.netty.Server;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.info.APIInfo;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;

public class RegisterAPIProcessor extends AbstractHttpProcessor {

	@Override
	public String processRequest(FullHttpRequest request) throws Exception {
		String body = request.content().toString(CharsetUtil.UTF_8);
		APIInfo api = Server.objectMapper.readValue(body, APIInfo.class);
		
		return  "执行完成";
	}

//	@Override
//	public Object processRequest(FullHttpRequest request) throws Exception {
//		return request;
//		// TODO Auto-generated method stub
//		
//	}

}
