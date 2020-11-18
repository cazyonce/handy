package com.handy.sql.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

import com.handy.sql.netty.http.channel.HttpChannelHandler;
import com.handy.sql.netty.http.session.HttpSession;

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
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.util.CharsetUtil;

public class Server {

	private static final NioEventLoopGroup serverBossGroup = new NioEventLoopGroup();
	private static final NioEventLoopGroup serverWorkerGroup = new NioEventLoopGroup();
	private final ServerBootstrap bootstrap = new ServerBootstrap();
	public int port = 8089;
	
	public static void main(String[] args) throws Exception {
		GlobalProvide.PATH_MAPPING_MANAGER.init();
		Server server = new Server();
		server.start();
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
						ch.pipeline().addLast(new HttpChannelHandler());
//						ch.pipeline().addLast(initSimpleChannelInboundHandler());
					}
				});
		try {
			bootstrap.bind(port).sync().channel().closeFuture().sync();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private SimpleChannelInboundHandler<FullHttpRequest> initSimpleChannelInboundHandler() {
		return new SimpleChannelInboundHandler<FullHttpRequest>() {
			final String[] filterURL = { "/favicon.ico" };
			protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
				process(ctx, request);

//				System.out.println(msg.content().toString(CharsetUtil.UTF_8));
//				logger.debug("channel read message type is {} ", msg.getType());

				// 回复信息给浏览器
				ByteBuf content = Unpooled.copiedBuffer("hello，我是服务器", CharsetUtil.UTF_8);
				// 构造http响应
				DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
						HttpResponseStatus.OK, content);

				response.headers().set("Content-Type", "text/plain;charset=UTF-8");
				response.headers().set("Content-Length", content.readableBytes());

				String cookieValue = ServerCookieEncoder.STRICT.encode(HttpSession.getHttpSession(request).getCookie());
				response.headers().set(HttpHeaderNames.SET_COOKIE, cookieValue);
				ctx.writeAndFlush(response);
			}

			public void process(ChannelHandlerContext ctx, FullHttpRequest request) {
				QueryStringDecoder uriDecoder = new QueryStringDecoder(request.uri());
				String path = uriDecoder.path();

				for (String url : filterURL) {
					if (url.equals(path)) {
						return;
					}
				}
//				process(uriDecoder, request);

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

			/**
			 * 客户端与服务端第一次建立连接时 执行
			 *
			 * @param ctx
			 * @throws Exception
			 */
			@Override
			public void channelActive(ChannelHandlerContext ctx) throws Exception, IOException {
				super.channelActive(ctx);
//				ctx.channel().read();
				InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
				String clientIp = insocket.getAddress().getHostAddress();
//				// TODO: 最终这个是客户端key需要去查询当前请求的ip对应的客户端key
//				String clientKey = "123";
//				saveToChannelManager(clientKey, ctx.channel());
				System.out.println("连接成功 date: " + LocalDateTime.now() + ", IP: " + clientIp);
			}

			/**
			 * 客户端与服务端 断连时 执行
			 *
			 * @param ctx
			 * @throws Exception
			 */
			@Override
			public void channelInactive(ChannelHandlerContext ctx) throws Exception, IOException {
				Channel channel = ctx.channel();
				System.out.println("连接断开 date: " + LocalDateTime.now() + ", channel: " + channel);
				super.channelInactive(ctx);
			}

		};
	}

}