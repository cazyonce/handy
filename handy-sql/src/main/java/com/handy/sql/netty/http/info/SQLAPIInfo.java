package com.handy.sql.netty.http.info;

import com.handy.sql.netty.http.api.mapping.APIMapping;
import com.handy.sql.netty.http.api.processor.AbstractHttpProcessor;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class SQLAPIInfo extends APIInfo {

	private String executeSQL;

	public SQLAPIInfo(APIMapping mapping, HttpResponseStatus responseStatus,
			Class<? extends AbstractHttpProcessor> executeProcessorClass) {
		super(mapping, responseStatus, executeProcessorClass);
	}
	
}
