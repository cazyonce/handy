package com.handy.sql.netty.jackon.converter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import com.fasterxml.jackson.databind.util.StdConverter;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;

public class HttpHeadersConverter extends StdConverter<ArrayList<LinkedHashMap<String, Object>>, HttpHeaders> {

	@Override
	public HttpHeaders convert(ArrayList<LinkedHashMap<String, Object>> value) {
		HttpHeaders headers = new DefaultHttpHeaders();
		for (LinkedHashMap<String, Object> entry : value) {

			for (Entry<String, Object> entry2 : entry.entrySet()) {
				headers.add(entry2.getKey(), entry2.getValue());
			}
		}
		return headers;
	}

}
