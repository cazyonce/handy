package com.handy.sql.netty.http.api.processor;

import io.netty.handler.codec.http.FullHttpRequest;

public interface RequestProcessor {

	public <T> T processRequest(FullHttpRequest request) throws Exception;
}
