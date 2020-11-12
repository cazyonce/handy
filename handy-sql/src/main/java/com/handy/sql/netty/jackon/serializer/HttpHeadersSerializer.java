package com.handy.sql.netty.jackon.serializer;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.netty.handler.codec.http.HttpHeaders;

public class HttpHeadersSerializer extends JsonSerializer<HttpHeaders>{

	@Override
	public void serialize(HttpHeaders value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeObject(value.entries());
	}

}
