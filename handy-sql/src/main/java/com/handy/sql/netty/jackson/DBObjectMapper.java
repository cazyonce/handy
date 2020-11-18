package com.handy.sql.netty.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

public class DBObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = 1L;

	public static DBObjectMapper newInstance() {
		DBObjectMapper mapper = new DBObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		return mapper;
	}

	private DBObjectMapper() {
		super();
	}

	private DBObjectMapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext dc) {
		super(jf, sp, dc);
	}

	private DBObjectMapper(JsonFactory jf) {
		super(jf);
	}

	private DBObjectMapper(ObjectMapper src) {
		super(src);
	}

}
