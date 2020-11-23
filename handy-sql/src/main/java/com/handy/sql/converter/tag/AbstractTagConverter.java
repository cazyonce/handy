package com.handy.sql.converter.tag;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.handy.sql.netty.http.api.enums.SQLKeyword;

public abstract class AbstractTagConverter {

	public abstract SQLKeyword getSQLKeyword();

	public abstract int convert(boolean keywordExisted, char[] statement, int position, StringBuffer sqlBuffer,
			Map<String, List<String>> urlParameters, MapSqlParameterSource parameterSource);

}
