package com.handy.sql.config;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.handy.sql.controller.interceptor.AccessPermissionsInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private AccessPermissionsInterceptor accessPermissionsInterceptor;

	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 不需要拦截的url
		final String[] paths = {
//				"/common.c78cb6589e954912d0f7.js",
//				"/*.js",
//				"/6.281e3e0030739ffcea44.js",
//				"/7.93bdd079602452229d5c.js",
//				"/assets/**", // 静态资源
				"/tencent/callback/**", // 管理员登录
				"/api/admin/login/**", // 管理员登录
				"/webjars/**", // swagger2 api 文档
				"/swagger-resources/**", // swagger2 api 文档
				"/swagger-ui.html", // swagger2 api 文档
				"/csrf", // swagger2 api 文档
				"/error", // springboot 系统错误
				"/static/**", "/ws/*", "/proxy/*" // 代理服务器API
		};
		registry.addInterceptor(accessPermissionsInterceptor).addPathPatterns("/**").excludePathPatterns(paths);
	}

	// 解决controll 中文乱码
	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		return converter;
	}

	// 注入controll 参数验证器
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

	/**
	 * springboot 本身已经注入了ObjectMapper 但是为了修改序列化时间格式，这里进行设置时间序列化的格式并覆盖
	 * 
	 * @return
	 */
	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper om = new ObjectMapper();
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
		om.registerModule(javaTimeModule);
		om.setDefaultPropertyInclusion(Include.NON_NULL);
		return om;
	}

	@Bean  
    public ServerEndpointExporter serverEndpointExporter() {  
        return new ServerEndpointExporter();  
    }  
}