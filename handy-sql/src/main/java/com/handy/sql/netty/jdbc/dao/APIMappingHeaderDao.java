package com.handy.sql.netty.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.http.api.consts.SystemDatabaseTableName;
import com.handy.sql.netty.http.api.entity.APIHeaderEntity;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;

public class APIMappingHeaderDao {

	public static int[] batchInsert(ArrayList<APIHeaderEntity> parameterList) {

		String sql = "insert into " + SystemDatabaseTableName.API_HEADER
				+ " (`api_mapping_id`, `name`, `value`, `header_type`) values(:api_mapping_id, :name, :value, :header_type)";
		return GlobalProvide.JDBC_TEMPLATE.batchUpdateExt(sql, parameterList);
	}

	public static List<HttpHeaders> queryListToHttpHeaders(int apiMappingId, String headerType) {

		String sql = "select `name`, `value` from " + SystemDatabaseTableName.API_HEADER
				+ " where api_mapping_id=:api_mapping_id and header_type=:header_type";
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		parameter.addValue("api_mapping_id", apiMappingId);
		parameter.addValue("header_type", headerType);
		return GlobalProvide.JDBC_TEMPLATE.query(sql, parameter, new RowMapper<HttpHeaders>() {

			@Override
			public HttpHeaders mapRow(ResultSet rs, int rowNum) throws SQLException {
				HttpHeaders header = new DefaultHttpHeaders();
				header.add(rs.getString("name"), rs.getString("value"));
				return header;
			}
		});
	}
}
