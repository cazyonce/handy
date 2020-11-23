package com.handy.sql.netty.http.api.processor.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.api.consts.APIMappingConst;
import com.handy.sql.netty.http.api.consts.SystemDatabaseTableName;
import com.handy.sql.netty.http.api.entity.APIMappingHeaderEntity;
import com.handy.sql.netty.http.api.enums.APIStatus;
import com.handy.sql.netty.http.api.enums.HttHeaderType;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.api.processor.dynamic.adapter.HttpProcessAdapter;
import com.handy.sql.netty.http.info.APIInfo;
import com.handy.sql.netty.http.info.SQLAPIInfo;
import com.handy.sql.netty.jdbc.dao.APIMappingDao;
import com.handy.sql.netty.jdbc.transaction.TransactionManagerUtil;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

public class PostRegisterAPIProcessor extends AbstractHttpProcessor {

	@Override
	public APIInfo newAPIInfoInstance() {
		APIInfo info = new APIInfo(APIMappingConst.SYTEM_REGISTER_API_POST, HttpResponseStatus.OK,
				PostRegisterAPIProcessor.class);
		info.setName("注册SQL API ");
		info.setStatus(APIStatus.ENABLED);

		DefaultHttpHeaders requestHeaders = new DefaultHttpHeaders();
		requestHeaders.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		requestHeaders.add(HttpHeaderNames.ACCEPT, HttpHeaderValues.APPLICATION_JSON);
		info.setRequestHeaders(requestHeaders);

		DefaultHttpHeaders responseHeaders = new DefaultHttpHeaders();
		responseHeaders.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
		info.setResponseHeaders(responseHeaders);
		return info;
	}

	@Override
	public String processRequest(FullHttpRequest request) throws CustomException {

		SQLAPIInfo insertAPIInfo;
		try {
			insertAPIInfo = GlobalProvide.OBJECT_MAPPER.readValue(request.content().toString(CharsetUtil.UTF_8),
					SQLAPIInfo.class);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new CustomException("数据错误");
		}

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		try (TransactionManagerUtil ts = TransactionManagerUtil.open()) {
			int row;
			try {
				row = APIMappingDao.insert(insertAPIInfo, keyHolder);
			} catch (Exception e) {

				if (e instanceof CustomException) {
					throw e;
				}
				e.printStackTrace();
				// TODO: 记录错误日志
				throw new CustomException("add api error > " + e.getCause().getMessage());
			}

			if (row < 1) {
				// TODO: 记录错误日志
				throw new CustomException("add api mapping data failed, row: " + row);
			}

			final int apiMappingId = keyHolder.getKey().intValue();

			try {
				batchInsertHeaders(apiMappingId, apiInfo.getRequestHeaders(), HttHeaderType.REQUEST);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: 记录错误日志
				throw new CustomException("add http request headers error > " + e.getCause().getMessage());
			}

			try {
				batchInsertHeaders(apiMappingId, apiInfo.getResponseHeaders(), HttHeaderType.RESPONSE);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: 记录错误日志
				throw new CustomException("add http response headers error > " + e.getCause().getMessage());
			}

			insertAPIInfo.setExecuteProcessorClass(
					HttpProcessAdapter.get(HttpMethod.valueOf(insertAPIInfo.getMapping().getMethod().name())));
			GlobalProvide.PATH_MAPPING_MANAGER.register(insertAPIInfo);

			System.out.println("执行注册api完成");
			ts.commit();
			return null;
		} catch (Exception e) {
			if (e instanceof CustomException) {
				throw (CustomException) e;
			}
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 批量插入请求头数据
	 * 
	 * @param apiMappingId
	 * @param headers
	 * @param type
	 * @param template
	 * @throws CustomException
	 */
	private void batchInsertHeaders(int apiMappingId, HttpHeaders headers, HttHeaderType type) throws CustomException {

		if (headers == null || headers.isEmpty()) {
			return;
		}

		ArrayList<APIMappingHeaderEntity> parameterList = new ArrayList<APIMappingHeaderEntity>();
		Iterator<Entry<String, String>> iterator = headers.iteratorAsString();
		Entry<String, String> entry = iterator.next();
		String sql = "insert into " + SystemDatabaseTableName.API_MAPPING_HEADER
				+ " (`api_mapping_id`, `name`, `value`, `header_type`) values(:api_mapping_id, :name, :value, :header_type)";
		parameterList.add(new APIMappingHeaderEntity(apiMappingId, entry.getKey(), entry.getValue(), type.name()));

		while (iterator.hasNext()) {
			entry = iterator.next();
			parameterList.add(new APIMappingHeaderEntity(apiMappingId, entry.getKey(), entry.getValue(), type.name()));
		}

		GlobalProvide.JDBC_TEMPLATE.batchUpdateExt(sql, parameterList);

	}

}
