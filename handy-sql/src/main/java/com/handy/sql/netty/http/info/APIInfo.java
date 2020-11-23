package com.handy.sql.netty.http.info;

import java.util.ArrayList;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.handy.sql.netty.http.api.enums.APIStatus;
import com.handy.sql.netty.http.api.mapping.APIMapping;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;
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

	protected APIStatus status;

	protected APIMapping mapping;

	/**
	 * 数组存储的path变量的顺序与实际的一致
	 */
	protected ArrayList<String> pathVariableNames;

	@JsonSerialize(using = HttpHeadersSerializer.class)
	@JsonDeserialize(converter = HttpHeadersConverter.class)
	protected HttpHeaders requestHeaders;

	@JsonDeserialize(converter = HttpHeadersConverter.class)
	@JsonSerialize(using = HttpHeadersSerializer.class)
	protected HttpHeaders responseHeaders;

	@JsonSerialize(using = HttpResponseStatusSerializer.class)
	@JsonDeserialize(converter = HttpResponseStatusConverter.class)
	protected HttpResponseStatus responseStatus;

//	@JsonIgnore
	protected Class<? extends AbstractHttpProcessor> executeProcessorClass;

	public APIInfo(APIMapping mapping, HttpResponseStatus responseStatus,
			Class<? extends AbstractHttpProcessor> executeProcessorClass) {
		this.mapping = mapping;
		this.responseStatus = responseStatus;
		this.executeProcessorClass = executeProcessorClass;
	}
}
