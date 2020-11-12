package com.handy.sql.netty.http.api.mapping;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.handy.sql.netty.jackon.converter.HttpMethodConverter;
import com.handy.sql.netty.jackon.serializer.HttpMethodSerializer;
import io.netty.handler.codec.http.HttpMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class APIMapping {

	@JsonSerialize(using = HttpMethodSerializer.class)
	@JsonDeserialize(converter = HttpMethodConverter.class)
	private HttpMethod method;

	private String path;

	public APIMapping(HttpMethod method, String path) {
		this.method = method;
		this.path = path;
	}

	public static APIMapping newAPIMapping(HttpMethod method, String path) {
		return new APIMapping(method, path);
	}

	public static APIMapping post(String path) {
		return new APIMapping(HttpMethod.POST, path);
	}

	public static APIMapping get(String path) {
		return new APIMapping(HttpMethod.GET, path);
	}

	public static APIMapping put(String path) {
		return new APIMapping(HttpMethod.PUT, path);
	}

}
