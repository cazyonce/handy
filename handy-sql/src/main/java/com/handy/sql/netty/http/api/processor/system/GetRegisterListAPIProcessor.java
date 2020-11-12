package com.handy.sql.netty.http.api.processor.system;

import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.http.api.consts.APIMappingConst;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.info.APIInfo;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;

public class GetRegisterListAPIProcessor extends AbstractHttpProcessor {

	private GetRegisterListAPIProcessor(APIInfo apiInfo) {
		super(apiInfo);
	}

	public static AbstractHttpProcessor newInstance() {
		APIInfo info = new APIInfo(APIMappingConst.SYTEM_REGISTER_API_GET, HttpResponseStatus.OK);
		info.setName("查询API列表");
		info.setResponseContent(true);

		DefaultHttpHeaders requestHeaders = new DefaultHttpHeaders();
		requestHeaders.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		info.setRequestHeaders(requestHeaders);

		DefaultHttpHeaders responseHeaders = new DefaultHttpHeaders();
		responseHeaders.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		info.setResponseHeaders(responseHeaders);
		return new GetRegisterListAPIProcessor(info);
	}

	@Override
	public String processRequestReturnContent(FullHttpRequest request) throws Exception {
		return GlobalProvide.OBJECT_MAPPER.writeValueAsString(GlobalProvide.PATH_MAPPING_MANAGER.getAPIInfoList());
	}
}
