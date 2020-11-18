package com.handy.sql.netty.http.api.processor;

import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.info.APIInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
public abstract class AbstractHttpProcessor implements RequestProcessor, ResponseProcessor {

	/**
	 * 这个属性是单例的，每次处理新的请求时，使用是注册处理器时的对象，而不是副本
	 */
	@Getter
	@Setter
	protected APIInfo apiInfo;

	public String getPath() {
		return apiInfo.getMapping().getPath();
	}

	public HttpMethod getMethod() {
		return apiInfo.getMapping().getMethod();
	}

	public HttpResponse process(FullHttpRequest request) throws CustomException {
		HttpHeaders responseHeaders = apiInfo.getResponseHeaders().copy();

		// 如果没有设置content type 默认为 text/plain
		if (!responseHeaders.contains(HttpHeaderNames.CONTENT_TYPE)) {
			responseHeaders.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
		}
		if (apiInfo.isResponseContent()) {

			String data = processRequestReturnContent(request);
			ByteBuf content = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
			responseHeaders.add(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
			return new DefaultFullHttpResponse(request.protocolVersion(), apiInfo.getResponseStatus(), content,
					responseHeaders, new DefaultHttpHeaders(false));
		}

		processRequestNoResponseContent(request);
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(),
				apiInfo.getResponseStatus());
		responseHeaders.add(HttpHeaderNames.CONTENT_LENGTH, 0);
		response.headers().set(responseHeaders);
		return response;
	}

	@Override
	public void processResponse(Object body) throws CustomException {

	}

	/**
	 * 通过com.handy.sql.netty.http.api.mapping.APIProcessorMappingManager调用这个方法创建的对象以单例的方式使用，所有不考虑其它地方的使用，可以不需要实现单例模式
	 * 
	 * @return
	 */
	public abstract APIInfo newAPIInfoInstance();
}
