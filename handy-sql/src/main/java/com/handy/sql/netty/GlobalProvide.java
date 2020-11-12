package com.handy.sql.netty;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handy.sql.netty.http.api.mapping.APIProcessorMappingManager;

public final class GlobalProvide {

	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	public static final JdbcTemplate JDBC_TEMPLATE;
	
	public final static APIProcessorMappingManager PATH_MAPPING_MANAGER = new APIProcessorMappingManager();
	
	static {
		DriverManagerDataSource dataSource = new DriverManagerDataSource(
				"jdbc:mysql://localhost:3306/handy?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true",
				"root", "root");
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		JDBC_TEMPLATE = new JdbcTemplate(dataSource);
	}
}
