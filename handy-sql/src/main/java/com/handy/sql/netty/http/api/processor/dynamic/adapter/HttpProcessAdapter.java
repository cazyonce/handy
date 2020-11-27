package com.handy.sql.netty.http.api.processor.dynamic.adapter;

import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.api.processor.dynamic.DeleteSQLDynamicProcessor;
import com.handy.sql.netty.http.api.processor.dynamic.GetSQLDynamicProcessor;
import com.handy.sql.netty.http.api.processor.dynamic.adapter.post.InterfacePostProcessAdapter;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

public class HttpProcessAdapter {

	@SuppressWarnings("unchecked")
	public static Class<? extends AbstractHttpProcessor> adaptive(HttpMethod method, HttpHeaders requestHeaders)
			throws CustomException {
		if (HttpMethod.GET == method) {
			return GetSQLDynamicProcessor.class;
		}
		if (HttpMethod.DELETE == method) {
			return DeleteSQLDynamicProcessor.class;
		}
		if (HttpMethod.POST == method || HttpMethod.PUT == method) {
			String mimeType;
			if (requestHeaders == null || requestHeaders.isEmpty()
					|| (mimeType = requestHeaders.get(HttpHeaderNames.CONTENT_TYPE)) == null) {
				throw new CustomException("Http method对应的处理器未找到：" + method.name());
			}
			Class<? extends InterfacePostProcessAdapter> adapterClass = GlobalProvide.POST_PROCESS_ADAPTER
					.adaptive(mimeType);
			if (adapterClass.getSuperclass() == AbstractHttpProcessor.class) {
				return (Class<? extends AbstractHttpProcessor>) adapterClass;
			}
			throw new CustomException("class: " + adapterClass.getTypeName() + " 不是class: "
					+ AbstractHttpProcessor.class.getTypeName() + " 的实现类");
		}
		throw new CustomException("Http method对应的处理器未找到：" + method.name());
	}
}
