package com.handy.sql.netty.http.info;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.handy.sql.netty.http.api.mapping.APIMapping;
import com.handy.sql.netty.jackon.converter.HttpHeadersConverter;
import com.handy.sql.netty.jackon.converter.HttpResponseStatusConverter;
import com.handy.sql.netty.jackon.serializer.HttpHeadersSerializer;
import com.handy.sql.netty.jackon.serializer.HttpResponseStatusSerializer;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class APIInfo {

	protected String name;
	
	protected String describe;

	protected APIMapping mapping;

	@JsonSerialize(using = HttpHeadersSerializer.class)
	@JsonDeserialize(converter = HttpHeadersConverter.class)
	protected HttpHeaders requestHeaders;

	@JsonDeserialize(converter = HttpHeadersConverter.class)
	@JsonSerialize(using = HttpHeadersSerializer.class)
	protected HttpHeaders responseHeaders;

	/** 是否需要返回内容 **/
	protected boolean responseContent;

	@JsonSerialize(using = HttpResponseStatusSerializer.class)
	@JsonDeserialize(converter = HttpResponseStatusConverter.class)
	protected HttpResponseStatus responseStatus;

	public APIInfo(APIMapping mapping, HttpResponseStatus responseStatus) {
		this.mapping = mapping;
		this.responseStatus = responseStatus;
	}
}
