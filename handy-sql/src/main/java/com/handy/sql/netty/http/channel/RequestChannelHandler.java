package com.handy.sql.netty.http.channel;

import com.handy.sql.netty.http.api.processor.RequestProcessor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RequestChannelHandler extends SimpleChannelInboundHandler<RequestProcessor> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestProcessor request) throws Exception {

	}
}
