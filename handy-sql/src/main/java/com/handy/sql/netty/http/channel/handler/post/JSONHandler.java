package com.handy.sql.netty.http.channel.handler.post;

import com.handy.sql.netty.exception.CustomException;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderValues;

public class JSONHandler extends AbstractPostContentTypeHandler {

	@Override
	public Object process(FullHttpRequest t) throws CustomException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean read0(String mimeType) {
		return mimeType != null && mimeType.startsWith(HttpHeaderValues.APPLICATION_JSON.toString());
	}

}
