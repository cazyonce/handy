package com.handy.sql.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Controller
@Component
public class AccessPermissionsInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		String path = request.getRequestURI();
		System.out.println(path + "---" + request.getMethod() + "---" + request.getRemoteAddr() + "---"
				+ request.getSession().getId());

		if (!(handler instanceof HandlerMethod)) {
			System.out.println("!(handler instanceof HandlerMethod)");
			return false;
		}


		return true;
	}
}