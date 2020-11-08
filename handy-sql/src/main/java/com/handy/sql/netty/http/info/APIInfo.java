package com.handy.sql.netty.http.info;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIInfo {

	private String name;

	private RequestInfo request;

	private ResponseInfo response;

}
