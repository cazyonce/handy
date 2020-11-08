package com.handy.sql.netty.http.api.processor;

import com.handy.sql.netty.http.info.APIInfo;
import io.netty.handler.codec.http.FullHttpRequest;

public abstract class AbstractHttpProcessor implements RequestProcessor, ResponseProcessor {

	protected APIInfo apiInfo;

	public void process(FullHttpRequest request) throws Exception {
		processRequest(request);
	}

	@Override
	public void processResponse(Object body) throws Exception {
		// TODO Auto-generated method stub

	}
}
