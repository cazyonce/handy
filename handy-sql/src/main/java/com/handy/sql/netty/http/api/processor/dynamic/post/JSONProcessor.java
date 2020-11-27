package com.handy.sql.netty.http.api.processor.dynamic.post;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.api.processor.dynamic.adapter.post.InterfacePostProcessAdapter;
import com.handy.sql.netty.http.info.APIInfo;
import com.handy.sql.netty.http.info.SQLAPIInfo;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.util.CharsetUtil;

public class JSONProcessor extends AbstractHttpProcessor implements InterfacePostProcessAdapter {

	@SuppressWarnings("unchecked")
	@Override
	public Object processRequest(FullHttpRequest request) throws CustomException {
		SQLAPIInfo sqlAPIInfo = (SQLAPIInfo) apiInfo;
		String sql = sqlAPIInfo.getExecuteSQL();
		if (sql == null || sql.isEmpty()) {
			return null;
		}
		Object data;
		try {
			data = GlobalProvide.OBJECT_MAPPER.readValue(request.content().toString(CharsetUtil.UTF_8), Object.class);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
			throw new CustomException("解析数据错误");
		}
		if (data instanceof LinkedHashMap) {
			return GlobalProvide.JDBC_TEMPLATE.updateExt(sql, data);
		} else if (data instanceof ArrayList) {
			return GlobalProvide.JDBC_TEMPLATE.batchUpdateExt(sql, (ArrayList<Object>) data);

		}
		return null;
	}

	@Override
	public APIInfo newAPIInfoInstance() {
		return null;
	}

	@Override
	public String contentType() {
		return HttpHeaderValues.APPLICATION_JSON.toString();
	}

}
