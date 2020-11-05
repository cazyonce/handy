package com.handy.sql.netty;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

public class Server {

	private Logger logger = LogManager.getLogger(Server.class);
	private static final NioEventLoopGroup serverBossGroup = new NioEventLoopGroup();
	private static final NioEventLoopGroup serverWorkerGroup = new NioEventLoopGroup();
	private final ServerBootstrap bootstrap = new ServerBootstrap();
	public int port = 8089;

	public static void main(String[] args) throws Exception {
//		Server server = new Server();
//		server.start();
	}

	public void start() {

		bootstrap.group(serverBossGroup, serverWorkerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					public void initChannel(SocketChannel ch) throws Exception {
//						 ch.pipeline().addLast(new HttpServerCodec());
						ch.pipeline().addLast(new HttpRequestDecoder());
						ch.pipeline().addLast(new HttpObjectAggregator(2048));
						ch.pipeline().addLast(new HttpResponseEncoder());
						ch.pipeline().addLast(initSimpleChannelInboundHandler());
					}
				});
		try {
			bootstrap.bind(port).sync().channel().closeFuture().sync();
			;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private SimpleChannelInboundHandler<FullHttpRequest> initSimpleChannelInboundHandler() {
		return new SimpleChannelInboundHandler<FullHttpRequest>() {

			protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
				QueryStringDecoder uriDecoder = new QueryStringDecoder(msg.getUri());
				System.out.println(uriDecoder.path());
				System.out.println(uriDecoder.uri());
				System.out.println(uriDecoder.parameters());
				System.out.println(uriDecoder);
//				new HttpServerCodec().
//				new HttpObjectAggregator(maxContentLength)
				System.out.println("333333333333333333333333333333333");
				System.out.println(msg);
				System.out.println("222222222222222222222222222222");
				System.out.println(msg.content().toString(CharsetUtil.UTF_8));
//				logger.debug("channel read message type is {} ", msg.getType());
				// 回复信息给浏览器
				ByteBuf content = Unpooled.copiedBuffer("hello，我是服务器", CharsetUtil.UTF_8);
				// 构造http响应
				DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
						HttpResponseStatus.OK, content);

				response.headers().set("Content-Type", "text/plain;charset=UTF-8");
				response.headers().set("Content-Length", content.readableBytes());

				ctx.writeAndFlush(response);
				System.out.println("22222222222222222");
			}

			private void handleAuthMessage(ChannelHandlerContext ctx, HttpMessage proxyMessage) {

			}

			/**
			 * 当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时
			 *
			 * @param ctx
			 * @param cause
			 */
			@Override
			public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws IOException {
				cause.printStackTrace();
				ctx.close();// 抛出异常，断开与客户端的连接
			}

//			/**
//			 * 客户端与服务端第一次建立连接时 执行
//			 *
//			 * @param ctx
//			 * @throws Exception
//			 */
//			@Override
//			public void channelActive(ChannelHandlerContext ctx) throws Exception, IOException {
//				super.channelActive(ctx);
//				ctx.channel().read();
//				InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//				String clientIp = insocket.getAddress().getHostAddress();
//				// TODO: 最终这个是客户端key需要去查询当前请求的ip对应的客户端key
//				String clientKey = "123";
//				saveToChannelManager(clientKey, ctx.channel());
//				logger.debug("连接成功 IP: {}", clientIp);
//			}

			/**
			 * 客户端与服务端 断连时 执行
			 *
			 * @param ctx
			 * @throws Exception
			 */
			@Override
			public void channelInactive(ChannelHandlerContext ctx) throws Exception, IOException {
				Channel channel = ctx.channel();
				logger.debug("连接断开 channel: {}", channel);
				super.channelInactive(ctx);
			}

		};
	}

}