package com.handy.sql.netty.http.api.processor.system.api.mapping;

import com.handy.sql.netty.http.api.consts.APIMappingConst;
import com.handy.sql.netty.http.api.consts.SystemDatabaseTableName;
import com.handy.sql.netty.http.api.enums.APIStatus;
import com.handy.sql.netty.http.api.processor.dynamic.GetSQLDynamicProcessor;
import com.handy.sql.netty.http.info.APIInfo;
import com.handy.sql.netty.http.info.SQLAPIInfo;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;

public class GetListAPIProcessor extends GetSQLDynamicProcessor {

	@Override
	public APIInfo newAPIInfoInstance() {
		SQLAPIInfo info = new SQLAPIInfo(APIMappingConst.SYTEM_API_MAPPING_GET, HttpResponseStatus.OK,
				GetListAPIProcessor.class);
		info.setName("Select API Mapping list");
		info.setStatus(APIStatus.ENABLED);

		DefaultHttpHeaders requestHeaders = new DefaultHttpHeaders();
		requestHeaders.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		info.setRequestHeaders(requestHeaders);

		DefaultHttpHeaders responseHeaders = new DefaultHttpHeaders();
		responseHeaders.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		info.setResponseHeaders(responseHeaders);
		info.setExecuteSQL("select * from " + SystemDatabaseTableName.API_MAPPING);
		return info;
	}
}
