package com.handy.sql.netty.http.api.processor;

import io.netty.handler.codec.http.FullHttpRequest;

public interface RequestProcessor {

	public default String processRequestReturnContent(FullHttpRequest request) throws Exception{
		return "";
	}

	public default void processRequestNoResponseContent(FullHttpRequest request) throws Exception {

	}
}
