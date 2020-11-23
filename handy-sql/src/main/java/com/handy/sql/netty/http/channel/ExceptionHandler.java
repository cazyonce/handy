package com.handy.sql.netty.http.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ExceptionHandler extends SimpleChannelInboundHandler<Throwable> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Throwable msg) throws Exception {
		System.out.println(msg.getMessage());
		
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println(cause.getMessage());
	}
}
