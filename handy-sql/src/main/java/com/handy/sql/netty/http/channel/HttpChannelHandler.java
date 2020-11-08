package com.handy.sql.netty.http.channel;

import com.handy.sql.netty.http.api.mapping.PathMapping;
import com.handy.sql.netty.http.api.mapping.PathMappingManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

public class HttpChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	final String[] filterURL = { "/favicon.ico" };
	final String REGISTER_API_PATH = "register/api";

	private static final PathMapping pathMapping = new PathMapping();
	private static final PathMappingManager pathMappingUtil = new PathMappingManager();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

		QueryStringDecoder uriDecoder = new QueryStringDecoder(request.uri());

		// 暂时对未知的url过滤
		if (filterPath(uriDecoder.path())) {
			return;
		}
		PathMapping apiMapping = pathMappingUtil.get(pathMapping, uriDecoder.path());
		apiMapping.getProcessor().process(request);

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
