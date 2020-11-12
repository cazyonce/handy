package com.handy.sql.netty.jackon.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import io.netty.handler.codec.http.HttpMethod;

public class HttpMethodConverter extends StdConverter<String, HttpMethod> {

	@Override
	public HttpMethod convert(String value) {
		return new HttpMethod(value.toUpperCase());
	}

}
