package com.handy.sql.netty.jackon.serializer;

import java.io.IOException;
import java.util.HashMap;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.netty.handler.codec.http.HttpResponseStatus;

public class HttpResponseStatusSerializer extends JsonSerializer<HttpResponseStatus>{

	@Override
	public void serialize(HttpResponseStatus value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", value.code());
		map.put("reason", value.reasonPhrase());
		gen.writeObject(map);
	}

}
