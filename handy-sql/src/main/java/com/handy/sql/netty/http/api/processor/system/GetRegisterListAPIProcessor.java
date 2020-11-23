package com.handy.sql.netty.http.api.processor.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.api.consts.APIMappingConst;
import com.handy.sql.netty.http.api.enums.APIStatus;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.info.APIInfo;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;

public class GetRegisterListAPIProcessor extends AbstractHttpProcessor {

	@Override
	public String processRequest(FullHttpRequest request) throws CustomException {
		
		try {
			return GlobalProvide.OBJECT_MAPPER.writeValueAsString(GlobalProvide.PATH_MAPPING_MANAGER.getAPIInfoList());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CustomException("序列化数据错误");
		}
	}

	@Override
	public APIInfo newAPIInfoInstance() {
		APIInfo info = new APIInfo(APIMappingConst.SYTEM_REGISTER_API_GET, HttpResponseStatus.OK,
				GetRegisterListAPIProcessor.class);
		info.setName("查询API列表");
		info.setStatus(APIStatus.ENABLED);

		DefaultHttpHeaders requestHeaders = new DefaultHttpHeaders();
		requestHeaders.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		info.setRequestHeaders(requestHeaders);

		DefaultHttpHeaders responseHeaders = new DefaultHttpHeaders();
		responseHeaders.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		info.setResponseHeaders(responseHeaders);
		return info;
	}
}
