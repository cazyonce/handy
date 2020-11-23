package com.handy.sql.netty.http.api.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.info.APIInfo;
import com.handy.sql.netty.http.response.content.ResponseContent;
import com.handy.sql.netty.http.response.content.ResponseContent.ResponseCode;
import com.handy.sql.netty.http.response.content.ResponseContentData;

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
import io.netty.handler.codec.http.HttpResponseStatus;
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
		try {

			HttpHeaders responseHeaders = apiInfo.getResponseHeaders().copy();

			// 如果没有设置content type 默认为 text/plain
			if (!responseHeaders.contains(HttpHeaderNames.CONTENT_TYPE)) {
				responseHeaders.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
			}

			Object rawData = processRequest(request);
			String data;
			if (rawData != null) {
				System.out.println(rawData);
				data = GlobalProvide.OBJECT_MAPPER
						.writeValueAsString(new ResponseContentData<Object>(ResponseCode.SUCCESS, rawData));
			} else {
				data = GlobalProvide.OBJECT_MAPPER.writeValueAsString(new ResponseContent(ResponseCode.SUCCESS));
			}

			ByteBuf content = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
			responseHeaders.add(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
			return new DefaultFullHttpResponse(request.protocolVersion(), apiInfo.getResponseStatus(), content,
					responseHeaders, new DefaultHttpHeaders(false));
//			DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(),
//					apiInfo.getResponseStatus());
//			responseHeaders.add(HttpHeaderNames.CONTENT_LENGTH, 0);
//			response.headers().set(responseHeaders);
//			return response;

		} catch (Exception e) {
			// TODO: 记录日志
			HttpHeaders responseHeaders = new DefaultHttpHeaders();
			if (e instanceof CustomException) {
				String data;
				try {
					data = GlobalProvide.OBJECT_MAPPER
							.writeValueAsString(new ResponseContent(ResponseCode.FAIL.code, e.getMessage()));
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
					DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(),
							HttpResponseStatus.EXPECTATION_FAILED);
					responseHeaders.add(HttpHeaderNames.CONTENT_LENGTH, 0);
					response.headers().set(responseHeaders);
					return response;
				}

				ByteBuf content = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
				responseHeaders.add(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
				return new DefaultFullHttpResponse(request.protocolVersion(), apiInfo.getResponseStatus(), content,
						responseHeaders, new DefaultHttpHeaders(false));

			}
			e.printStackTrace();
			DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(),
					HttpResponseStatus.EXPECTATION_FAILED);
			responseHeaders.add(HttpHeaderNames.CONTENT_LENGTH, 0);
			response.headers().set(responseHeaders);
			return response;
		}
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
