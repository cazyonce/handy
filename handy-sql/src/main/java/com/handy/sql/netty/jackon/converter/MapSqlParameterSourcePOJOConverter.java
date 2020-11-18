package com.handy.sql.netty.jackon.converter;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.handy.sql.netty.http.api.pojo.MapSqlParameterSourcePOJO;

public class MapSqlParameterSourcePOJOConverter
		extends StdConverter<LinkedHashMap<String, Object>, MapSqlParameterSourcePOJO> {

	@Override
	public MapSqlParameterSourcePOJO convert(LinkedHashMap<String, Object> value) {

		MapSqlParameterSourcePOJO source = new MapSqlParameterSourcePOJO();

		for (Entry<String, Object> entry : value.entrySet()) {
			source.addValue(entry.getKey(), entry.getValue());
		}
		return source;
	}

}
