package com.handy.sql.netty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.handy.sql.converter.SQLConverter;
import com.handy.sql.netty.http.api.mapping.APIProcessorMappingManager;
import com.handy.sql.netty.http.api.processor.dynamic.adapter.post.PostProcessAdapter;
import com.handy.sql.netty.http.api.processor.dynamic.post.JSONProcessor;
import com.handy.sql.netty.jackson.DBObjectMapper;
import com.handy.sql.netty.jdbc.core.namedparam.MyNamedParameterJdbcTemplate;

public final class GlobalProvide {

	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static final DBObjectMapper DB_OBJECT_MAPPER = DBObjectMapper.newInstance();

	public static final MyNamedParameterJdbcTemplate JDBC_TEMPLATE;

	public final static APIProcessorMappingManager PATH_MAPPING_MANAGER = new APIProcessorMappingManager();

	public final static PlatformTransactionManager TRANSACTION_MANAGER;

	public final static SQLConverter SELECT_SQL_CONVERTER = new SQLConverter();

	public final static PostProcessAdapter POST_PROCESS_ADAPTER = new PostProcessAdapter();
	
	static {
		DriverManagerDataSource dataSource = new DriverManagerDataSource(
				"jdbc:mysql://localhost:3306/handy?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true",
				"root", "root");
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		TRANSACTION_MANAGER = new DataSourceTransactionManager(dataSource);
		JDBC_TEMPLATE = new MyNamedParameterJdbcTemplate(dataSource);

		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		javaTimeModule.addSerializer(LocalDate.class,
				new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
		javaTimeModule.addDeserializer(LocalDateTime.class,
				new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		javaTimeModule.addDeserializer(LocalDate.class,
				new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		javaTimeModule.addDeserializer(LocalTime.class,
				new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
		OBJECT_MAPPER.registerModule(javaTimeModule);
		OBJECT_MAPPER.setDefaultPropertyInclusion(Include.NON_NULL);

		DB_OBJECT_MAPPER.registerModule(javaTimeModule);
		DB_OBJECT_MAPPER.setDefaultPropertyInclusion(Include.NON_NULL);
		
		POST_PROCESS_ADAPTER.add(new JSONProcessor());

	}
}
