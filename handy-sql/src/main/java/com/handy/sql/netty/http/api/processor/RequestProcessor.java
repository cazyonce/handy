package com.handy.sql.netty.http.api.processor;

import com.handy.sql.netty.exception.CustomException;

import io.netty.handler.codec.http.FullHttpRequest;

public interface RequestProcessor {

	public default <T> T processRequest(FullHttpRequest request) throws CustomException {
		return null;
	}
}
