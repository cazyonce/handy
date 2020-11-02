package com.handy.sql.api.request.mapping;

import org.springframework.web.bind.annotation.RequestMethod;

public class RequestMapping {

	String name;
	
	String[] value;
	
	String[] path;
	
	RequestMethod[] method;
	
	String[] params;
	
	String[] headers;
	
	String[] consume;
	
	String[] produces;
}
