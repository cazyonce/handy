package com.handy.sql.netty.http.api.processor.dynamic;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.info.APIInfo;
import com.handy.sql.netty.http.info.SQLAPIInfo;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;

public class GetSQLDynamicProcessor extends AbstractHttpProcessor {
	
	
	@Override
	public String processRequestReturnContent(FullHttpRequest request) throws CustomException {
		SQLAPIInfo sqlAPIInfo = (SQLAPIInfo) apiInfo;
//
//		HttpHeaders requestHttpHeaders = request.headers();
//
//		String contentType = requestHttpHeaders.get(HttpHeaderNames.CONTENT_TYPE);
//		if (contentType == null) {
//			throw new CustomException("header not found: " + HttpHeaderNames.CONTENT_TYPE);
//		}
////		 HttpHeaderValues.APPLICATION_JSON.toString()
//		if (HttpHeaderValues.APPLICATION_JSON.toString().equalsIgnoreCase(contentType)) {
//
//		}
		
		List<Map<String, Object>> data = GlobalProvide.JDBC_TEMPLATE.queryForList(sqlAPIInfo.getExecuteSQL(), new MapSqlParameterSource());
		try {
			return GlobalProvide.DB_OBJECT_MAPPER.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CustomException("序列化数据错误");
		}
	}

	@Override
	public APIInfo newAPIInfoInstance() {
		return null;
	}

}
