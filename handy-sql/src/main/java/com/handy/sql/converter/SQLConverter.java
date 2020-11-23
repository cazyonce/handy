package com.handy.sql.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import com.handy.sql.converter.tag.AbstractTagConverter;
import com.handy.sql.converter.tag.WhereTagConverter;
import com.handy.sql.netty.exception.CustomException;

public class SQLConverter {

	private static final Map<String, AbstractTagConverter> TAG_CONVERTER = new HashMap<String, AbstractTagConverter>();

	static {
		WhereTagConverter whereTag = new WhereTagConverter();
		TAG_CONVERTER.put(whereTag.getSQLKeyword().name(), whereTag);
	}

	public String convert(String sql, Map<String, List<String>> urlParameters, MapSqlParameterSource parameterSource)
			throws CustomException {
		StringBuffer sqlBuffer = new StringBuffer();
		char[] statement = sql.toCharArray();
		int i = 0;
		int eqIndex = 0;
		int lastAppenIndex = i;
		while (i < statement.length) {
			char c = statement[i];
			if (eqIndex == 0 && c == '=') { // 记录等于符号，用于判断where标记之前是否存在where关键字
				eqIndex = i;
			} else if (c == '<') { // tag start
				int tagStart = i;
				
				i++;
				boolean bool = false;
				if (i < statement.length) {
					if (bool= statement[i] == '*') {
						i++;
					}
				}
				int start = i;
				while (i < statement.length) {
					c = statement[i];
					if (c == '>') {
						String keyword = sql.substring(start, i);
						AbstractTagConverter processor = TAG_CONVERTER.get(keyword.toUpperCase());
						if (processor == null) {
							throw new CustomException("未知的sql 标记：" + keyword);
						}
						sqlBuffer.append(statement, lastAppenIndex, tagStart - lastAppenIndex);
						i++;
						i = processor.convert(bool, statement, i, sqlBuffer, urlParameters,
								parameterSource);
						eqIndex = 0; // 重置为零
						lastAppenIndex = i + 1;
						break;
					}
					i++;
				}
				continue;
			}
			i++;
		}

		if (sqlBuffer.length() == 0) {
			return sql;
		}
		if (lastAppenIndex < i) {
			sqlBuffer.append(statement, lastAppenIndex, i - lastAppenIndex);
		}
		return sqlBuffer.toString();
	}
}
