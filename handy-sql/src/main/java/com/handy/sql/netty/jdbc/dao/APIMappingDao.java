package com.handy.sql.netty.jdbc.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.http.api.consts.SystemDatabaseTableName;
import com.handy.sql.netty.http.api.entity.APIMappingEntity;
import com.handy.sql.netty.http.info.SQLAPIInfo;

public class APIMappingDao {

	public static int insert(SQLAPIInfo info, GeneratedKeyHolder keyHolder) {

		String sql = "insert into " + SystemDatabaseTableName.API_MAPPING
				+ " (`path`, `name`, `method`, `describe`, `create_time`, `status`, `response_code`, `response_reason`, `execute_sql`) values(:path, :name, :method, :describe, now(), :status, :response_code, :response_reason, :execute_sql)";

		APIMappingEntity apiEntity = new APIMappingEntity(info.getMapping().getPath(),
				info.getMapping().getMethod().name(), info.getName(),
				info.getDescribe() == null ? "" : info.getDescribe(), info.getResponseStatus().code(),
				info.getStatus().STATUS, info.getExecuteSQL(), info.getResponseStatus().reasonPhrase());
		return GlobalProvide.JDBC_TEMPLATE.updateExt(sql, apiEntity, keyHolder);
	}

	public static APIMappingEntity[] queryList() {

		String sql = "select `id`, `path`, `method`, `name`, `describe`, `response_code`, `response_reason`, `status`, `execute_sql` from "
				+ SystemDatabaseTableName.API_MAPPING;

		List<Map<String, Object>> selectedData = GlobalProvide.JDBC_TEMPLATE.queryForList(sql,
				new MapSqlParameterSource());
		return GlobalProvide.DB_OBJECT_MAPPER.convertValue(selectedData, APIMappingEntity[].class);
	}
}
