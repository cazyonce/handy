package com.handy.sql.netty.jackon.converter;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import com.fasterxml.jackson.databind.util.StdConverter;
import io.netty.handler.codec.http.HttpResponseStatus;

public class HttpResponseStatusConverter extends StdConverter<LinkedHashMap<String, Object>, HttpResponseStatus> {

	@Override
	public HttpResponseStatus convert(LinkedHashMap<String, Object> value) {
		Integer code = null;
		String reason = null;
		for (Entry<String, Object> entry : value.entrySet()) {
			String key = entry.getKey();
			if ("code".equals(key)) {
				code = (Integer) entry.getValue();
			} else if ("reason".equals(key)) {
				reason = (String) entry.getValue();
			}
		}
		return new HttpResponseStatus(code, reason);
	}

}
