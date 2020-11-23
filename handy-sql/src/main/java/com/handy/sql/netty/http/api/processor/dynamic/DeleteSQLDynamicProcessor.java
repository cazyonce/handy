package com.handy.sql.netty.http.api.processor.dynamic;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.exception.CustomException;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
import com.handy.sql.netty.http.info.APIInfo;
import com.handy.sql.netty.http.info.SQLAPIInfo;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

public class DeleteSQLDynamicProcessor extends AbstractHttpProcessor {

	@SuppressWarnings("unchecked")
	@Override
	public Integer processRequest(FullHttpRequest request) throws CustomException {
		SQLAPIInfo sqlAPIInfo = (SQLAPIInfo) apiInfo;
		QueryStringDecoder uriDecoder = new QueryStringDecoder(request.uri());
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		String sql = sqlAPIInfo.getExecuteSQL();
		if (sql == null || sql.isEmpty()) {
			return null;
		}
		sql = GlobalProvide.SELECT_SQL_CONVERTER.convert(sqlAPIInfo.getExecuteSQL(), uriDecoder.parameters(),
				parameterSource);
		return GlobalProvide.JDBC_TEMPLATE.update(sql, parameterSource);
	}

	@Override
	public APIInfo newAPIInfoInstance() {
		return null;
	}

}
