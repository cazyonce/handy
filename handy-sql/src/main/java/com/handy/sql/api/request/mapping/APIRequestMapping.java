package com.handy.sql.api.request.mapping;

import javax.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIRequestMapping {

	@NotBlank
	private String name;
	
	String[] path;
	
	RequestMethod[] method;
	
	String[] params;
	
	/** 限定允许或拒绝请求头 **/
	String[] headers;
	
	String[] consume;
	
	String[] produces;
	
}
