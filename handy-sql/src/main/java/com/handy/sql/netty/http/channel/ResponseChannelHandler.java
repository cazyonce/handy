package com.handy.sql.netty.http.channel;

import com.handy.sql.netty.http.api.processor.ResponseProcessor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ResponseChannelHandler extends SimpleChannelInboundHandler<ResponseProcessor> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ResponseProcessor msg) throws Exception {

	}

}
