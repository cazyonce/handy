package com.handy.sql.netty.http.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class API {

	private String name;

	private RequestInfo request;

	private ResponseInfo response;

	private String executeSQL;
}
