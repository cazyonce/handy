package com.handy.sql.netty.http.api.processor;

import com.handy.sql.netty.exception.CustomException;

public interface ResponseProcessor {
	
	void processResponse(Object body) throws CustomException;
}
