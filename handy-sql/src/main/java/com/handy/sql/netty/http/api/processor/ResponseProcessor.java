package com.handy.sql.netty.http.api.processor;

public interface ResponseProcessor {
	
	void processResponse(Object body) throws Exception;
}
