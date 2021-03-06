package com.handy.sql.netty.http.channel;

import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;

public class HttpChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	final String[] filterURL = { "/favicon.ico" };

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

		QueryStringDecoder uriDecoder = new QueryStringDecoder(request.uri());
//		System.out.println("uriDecoder: " + uriDecoder);
//		System.out.println("rawQuery: " + uriDecoder.rawQuery());
//		System.out.println("rawPath: " + uriDecoder.rawPath());
//		System.out.println("parameters class: " + uriDecoder.parameters().getClass());
//		System.out.println("parameters: " + uriDecoder.parameters());
		System.out.println("uri: " + uriDecoder.uri());
		// 暂时对未知的url过滤
		if (filterPath(uriDecoder.path())) {
			return;
		}
		try {

			AbstractHttpProcessor processor = GlobalProvide.PATH_MAPPING_MANAGER.get(uriDecoder.path(),
					request.method());
			ctx.writeAndFlush(processor.process(request));
		} catch (Exception e) {
			e.printStackTrace();
			DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(),
					HttpResponseStatus.EXPECTATION_FAILED);
			HttpHeaders responseHeaders = new DefaultHttpHeaders();
			responseHeaders.add(HttpHeaderNames.CONTENT_LENGTH, 0);
			response.headers().set(responseHeaders);
			ctx.writeAndFlush(response);
		}
	}

	private boolean filterPath(String path) {
		for (String url : filterURL) {
			if (url.equals(path)) {
				return true;
			}
		}
		return false;
	}

}
