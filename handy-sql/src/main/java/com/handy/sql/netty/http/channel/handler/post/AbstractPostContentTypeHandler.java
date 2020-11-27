package com.handy.sql.netty.http.channel.handler.post;

import com.handy.sql.netty.http.channel.AbstractAPIChannel;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;

public abstract class AbstractPostContentTypeHandler extends AbstractAPIChannel<FullHttpRequest> {

	@Override
	public boolean read(FullHttpRequest request) {
		return read0(request.headers().get(HttpHeaderNames.CONTENT_TYPE));
	}
	
	protected abstract boolean read0(String mimeType);

	

}
