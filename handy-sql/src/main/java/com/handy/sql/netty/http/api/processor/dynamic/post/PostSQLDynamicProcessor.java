package com.handy.sql.netty.http.api.processor.dynamic.post;

import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.channel.APIChannelPipeline;
import com.handy.sql.netty.http.channel.handler.post.JSONHandler;
import com.handy.sql.netty.http.info.APIInfo;
import com.handy.sql.netty.http.info.SQLAPIInfo;
import io.netty.handler.codec.http.FullHttpRequest;

public class PostSQLDynamicProcessor extends AbstractHttpProcessor {

	private final static APIChannelPipeline<FullHttpRequest> API_CHANNEL_PIPELINE = new APIChannelPipeline<FullHttpRequest>();
	
	static {
		API_CHANNEL_PIPELINE.addLast(new JSONHandler());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object processRequest(FullHttpRequest request) throws CustomException {
//		HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
		SQLAPIInfo sqlAPIInfo = (SQLAPIInfo) apiInfo;
		String sql = sqlAPIInfo.getExecuteSQL();
		if (sql == null || sql.isEmpty()) {
			return null;
		}
		return API_CHANNEL_PIPELINE.process(request);
	}

	@Override
	public APIInfo newAPIInfoInstance() {
		return null;
	}

}
