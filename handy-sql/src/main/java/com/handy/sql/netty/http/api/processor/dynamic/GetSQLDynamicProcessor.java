package com.handy.sql.netty.http.api.processor.dynamic;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.JdbcUtils;
import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.info.APIInfo;
import com.handy.sql.netty.http.info.SQLAPIInfo;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

public class GetSQLDynamicProcessor extends AbstractHttpProcessor {

	@SuppressWarnings("unchecked")
	@Override
	public List<LinkedHashMap<String, Object>> processRequest(FullHttpRequest request) throws CustomException {
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
		QueryStringDecoder uriDecoder = new QueryStringDecoder(request.uri());
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		String sql = sqlAPIInfo.getExecuteSQL();
		if (sql == null || sql.isEmpty()) {
			return null;
		}
		sql = GlobalProvide.SELECT_SQL_CONVERTER.convert(sqlAPIInfo.getExecuteSQL(), uriDecoder.parameters(),
				parameterSource);
		return GlobalProvide.JDBC_TEMPLATE.query(sql, parameterSource, new RowMapper<LinkedHashMap<String, Object>>() {

			@Override
			public LinkedHashMap<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResultSetMetaData metaData = rs.getMetaData();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>(metaData.getColumnCount());
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					String fieldName = JdbcUtils.convertUnderscoreNameToPropertyName(metaData.getColumnName(i));
					if (Timestamp.class.getTypeName().equals(metaData.getColumnClassName(i))) {
						ConversionService service = DefaultConversionService.getSharedInstance();
						map.put(fieldName, service.convert(rs.getTimestamp(i), LocalDateTime.class));
					} else {
						map.put(fieldName, rs.getObject(i));
					}
				}
				return map;
			}
		});
	}

	@Override
	public APIInfo newAPIInfoInstance() {
		return null;
	}

}
