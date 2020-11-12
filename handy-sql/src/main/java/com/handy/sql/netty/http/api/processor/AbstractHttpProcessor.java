package com.handy.sql.netty.http.api.processor;

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

public abstract class AbstractHttpProcessor implements RequestProcessor, ResponseProcessor {

	@Getter
	protected final APIInfo apiInfo;

	public AbstractHttpProcessor(APIInfo apiInfo) {
		this.apiInfo = apiInfo;
	}

	public String getPath() {
		return apiInfo.getMapping().getPath();
	}
	
	public HttpMethod getMethod() {
		return apiInfo.getMapping().getMethod();
	}

	public HttpResponse process(FullHttpRequest request) throws Exception {
		HttpHeaders responseHeaders = apiInfo.getResponseHeaders();
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
	public void processResponse(Object body) throws Exception {

	}
}
