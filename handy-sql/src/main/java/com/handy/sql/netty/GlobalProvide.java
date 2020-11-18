package com.handy.sql.netty;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handy.sql.netty.http.api.mapping.APIProcessorMappingManager;
import com.handy.sql.netty.jackson.DBObjectMapper;
import com.handy.sql.netty.jdbc.core.namedparam.MyNamedParameterJdbcTemplate;

public final class GlobalProvide {

	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static final DBObjectMapper DB_OBJECT_MAPPER = DBObjectMapper.newInstance();

	public static final MyNamedParameterJdbcTemplate JDBC_TEMPLATE;

	public final static APIProcessorMappingManager PATH_MAPPING_MANAGER = new APIProcessorMappingManager();

	static {
		DriverManagerDataSource dataSource = new DriverManagerDataSource(
				"jdbc:mysql://localhost:3306/handy?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true",
				"root", "root");
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		JDBC_TEMPLATE = new MyNamedParameterJdbcTemplate(dataSource);
	}
}
