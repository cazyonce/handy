package com.handy.sql.netty.http.api.processor.dynamic.adapter;

import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.api.processor.dynamic.DeleteSQLDynamicProcessor;
import com.handy.sql.netty.http.api.processor.dynamic.GetSQLDynamicProcessor;
import io.netty.handler.codec.http.HttpMethod;

public class HttpProcessAdapter {

	public static Class<? extends AbstractHttpProcessor> get(HttpMethod method) throws CustomException {
		if (HttpMethod.GET == method) {
			return GetSQLDynamicProcessor.class;
		}
		if (HttpMethod.DELETE == method) {
			return DeleteSQLDynamicProcessor.class;
		}
		throw new CustomException("Http method对应的处理器未找到：" + method.name());
	}
}
