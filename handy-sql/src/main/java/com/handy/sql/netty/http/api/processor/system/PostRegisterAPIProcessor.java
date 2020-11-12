package com.handy.sql.netty.http.api.processor.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.api.consts.APIMappingConst;
import com.handy.sql.netty.http.api.consts.SystemDatabaseTableName;
import com.handy.sql.netty.http.api.enums.HttHeaderType;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.info.APIInfo;
import com.handy.sql.netty.http.info.SQLAPIInfo;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

public class PostRegisterAPIProcessor extends AbstractHttpProcessor {

	private PostRegisterAPIProcessor(APIInfo apiInfo) {
		super(apiInfo);
	}

	public static AbstractHttpProcessor newInstance() {
		APIInfo info = new APIInfo(APIMappingConst.SYTEM_REGISTER_API_POST, HttpResponseStatus.OK);
		info.setName("注册SQL API ");
		info.setResponseContent(false);

		DefaultHttpHeaders requestHeaders = new DefaultHttpHeaders();
		requestHeaders.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		requestHeaders.add(HttpHeaderNames.ACCEPT, HttpHeaderValues.APPLICATION_JSON);
		info.setRequestHeaders(requestHeaders);

		DefaultHttpHeaders responseHeaders = new DefaultHttpHeaders();
		responseHeaders.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
		info.setResponseHeaders(responseHeaders);
		return new PostRegisterAPIProcessor(info);
	}

	@Override
	public void processRequestNoResponseContent(FullHttpRequest request) throws Exception {
		
		SQLAPIInfo info = GlobalProvide.OBJECT_MAPPER.readValue(request.content().toString(CharsetUtil.UTF_8),
				SQLAPIInfo.class);
		
		System.out.println(GlobalProvide.OBJECT_MAPPER.writeValueAsString(info));
		
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(
				GlobalProvide.JDBC_TEMPLATE.getDataSource());
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		
		try {
			int row = insertApiInfo(info, keyHolder, template);
			if (row < 1) {
				// TODO: 记录错误日志
				throw new CustomException("add api mapping data failed, row: " + row);
			}
		} catch (Exception e) {

			if (e instanceof CustomException) {
				throw e;
			}
			e.printStackTrace();
			// TODO: 记录错误日志
			throw new CustomException("add api error > " + e.getCause().getMessage());
		}
		
		final int apiMappingId = keyHolder.getKey().intValue();
		System.out.println(apiMappingId);

		try {
			batchInsertHeaders(apiMappingId, apiInfo.getRequestHeaders(), HttHeaderType.REQUEST, template);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: 记录错误日志
			throw new CustomException("add http request headers error > " + e.getCause().getMessage());
		}

		try {
			batchInsertHeaders(apiMappingId, apiInfo.getResponseHeaders(), HttHeaderType.RESPONSE, template);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: 记录错误日志
			throw new CustomException("add http response headers error > " + e.getCause().getMessage());
		}

//		template.update(new PreparedStatementCreator() {
//			
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				String sql = "insert into api_mapping value(path, name, describe, create_tim) values(?, ?, ?, now())";	
//				 PreparedStatement ps=con.prepareStatement(sql);
//				return null;
//			}
//		});
//		template.update("insert into api_mapping value(path, name, describe, create_tim) values(?, ?, ?, now())", args)
		System.out.println("执行注册api完成");
	}

	private int insertApiInfo(SQLAPIInfo info, GeneratedKeyHolder keyHolder, NamedParameterJdbcTemplate template) {

		String sql = "insert into " + SystemDatabaseTableName.API_MAPPING
				+ " (`path`, `name`, `method`, `describe`, `create_time`) values(:path, :name, :method, :describe, now())";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("path", info.getMapping().getPath());
		parameters.addValue("method", info.getMapping().getMethod().name());
		parameters.addValue("name", info.getName());
		parameters.addValue("describe", info.getDescribe() == null ? "" : info.getDescribe());
		return template.update(sql, parameters, keyHolder);
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
	private void batchInsertHeaders(int apiMappingId, HttpHeaders headers, HttHeaderType type,
			NamedParameterJdbcTemplate template) throws CustomException {

		if (headers == null || headers.isEmpty()) {
			return;
		}

		List<MapSqlParameterSource> parameterList = new ArrayList<MapSqlParameterSource>();
		Iterator<Entry<String, String>> iterator = headers.iteratorAsString();
		Entry<String, String> entry = iterator.next();
		String sql = "insert into " + SystemDatabaseTableName.API_MAPPING_HEADER
				+ " (`api_mapping_id`, `name`, `value`, `header_type`) values(:api_mapping_id, :name, :value, :header_type)";
		parameterList.add(batchInsertHeadersParametersPrepared(apiMappingId, entry, type));

		while (iterator.hasNext()) {
			entry = iterator.next();
			parameterList.add(batchInsertHeadersParametersPrepared(apiMappingId, entry, type));
		}

		template.batchUpdate(sql, parameterList.toArray(new MapSqlParameterSource[parameterList.size()]));

	}

	/**
	 * 预定义批量插入Http header sql参数
	 * 
	 * @param apiMappingId
	 * @param header
	 * @param type
	 * @return
	 */
	private MapSqlParameterSource batchInsertHeadersParametersPrepared(int apiMappingId, Entry<String, String> header,
			HttHeaderType type) {

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("api_mapping_id", apiMappingId);
		parameters.addValue("name", header.getKey());
		parameters.addValue("value", header.getKey());
		parameters.addValue("header_type", type.name());
		return parameters;
	}
}
