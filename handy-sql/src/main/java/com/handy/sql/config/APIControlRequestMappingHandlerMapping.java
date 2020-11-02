package com.handy.sql.config;

import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import java.lang.reflect.Method;

public class APIControlRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
	}

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
		System.out.println("VersionControlRequestMappingHandlerMapping===============");
		return info;
	}

	public void registerMapping(Method method, Object controllerClass) {
		registerMapping(getMappingForMethod(method, controllerClass.getClass()), controllerClass, method);
	}
}