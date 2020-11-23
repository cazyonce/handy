package com.handy.sql.converter.tag;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.handy.sql.netty.http.api.enums.SQLKeyword;

public class WhereTagConverter extends AbstractTagConverter {

	@Override
	public SQLKeyword getSQLKeyword() {
		return SQLKeyword.WHERE;
	}

	@Override
	public int convert(boolean keywordExisted, char[] statement, int position, StringBuffer sqlBuffer,
			Map<String, List<String>> urlParameters, MapSqlParameterSource parameterSource) {
		StringBuffer temp = new StringBuffer();
		int newPosition = findOptions(statement, position, temp, urlParameters, parameterSource);
		if (temp.length() <= 0) {
			return newPosition;
		}
		if (keywordExisted) {
			sqlBuffer.append("and ");
		} else {
			sqlBuffer.append("where ");
		}
		sqlBuffer.append(temp);
		return newPosition;
	}

	public int findOptions(char[] statement, int position, StringBuffer sqlBuffer,
			Map<String, List<String>> urlParameters, MapSqlParameterSource parameterSource) {
		if (position < statement.length) {
			if (statement[position] == '[') { // 字段选项开始字符
				int optionStart = ++position; // 定义字段名开始位置
				StringBuffer tempSQL = new StringBuffer();
				while (position < statement.length) {
					char c = statement[position];
					if (c == ',') {
						if (optionStart != position) { // 到达一个参数点
							String fieldName = new String(Arrays.copyOfRange(statement, optionStart, position)).trim();
							StringBuffer fieldOptionSQL = generateOption(fieldName, urlParameters,
									tempSQL.length() == 0, parameterSource);
							if (fieldOptionSQL != null) {
								if (tempSQL.length() > 0) {
									tempSQL.append(" and");
								}
								tempSQL.append(fieldOptionSQL);
							}
							optionStart = position + 1; // 刷新下一个字段名的开始位置
						}
					} else if (c == ']') {
						String fieldName = new String(Arrays.copyOfRange(statement, optionStart, position)).trim();
						StringBuffer fieldOptionSQL = generateOption(fieldName, urlParameters, tempSQL.length() == 0,
								parameterSource);
						if (fieldOptionSQL != null) {
							if (tempSQL.length() > 0) {
								tempSQL.append(" and");
							}
							tempSQL.append(fieldOptionSQL);
						}
						sqlBuffer.append(tempSQL);
						return position;
					}
					position++;
				}
				return position;
			}

			return position;
		}
		
		StringBuffer tempSQL = new StringBuffer();
		// 该where标签不存在固定的可选字段条件，默认添加所有可选添加
		for (Entry<String, List<String>> entry : urlParameters.entrySet()) {
			StringBuffer fieldOptionSQL = generateOption(entry.getKey(), urlParameters,
					tempSQL.length() == 0, parameterSource);
			if (fieldOptionSQL != null) {
				if (tempSQL.length() > 0) {
					tempSQL.append(" and");
				}
				tempSQL.append(fieldOptionSQL);
			}
		}
		sqlBuffer.append(tempSQL);
		return position;
	}

	private StringBuffer generateOption(String fieldName, Map<String, List<String>> urlParameters, boolean firstField,
			MapSqlParameterSource parameterSource) {
		List<String> values = urlParameters.get(fieldName);
		int valueLength;
		if (values == null || (valueLength = values.size()) <= 0) {
			return null;
		}
		StringBuffer tempSQL = new StringBuffer();
		if (!firstField) {
			tempSQL.append(" ");
		}
		tempSQL.append("`").append(fieldName).append("`");
		if (valueLength == 1) {
			tempSQL.append("=:").append(fieldName);
			parameterSource.addValue(fieldName, values.get(0));
			return tempSQL;
		}

		Iterator<String> iterator = values.iterator();
		String fieldNameTemp = fieldName + "0";
		tempSQL.append(" in(:").append(fieldNameTemp);
		parameterSource.addValue(fieldNameTemp, iterator.next());
		int i = 1;
		while (iterator.hasNext()) {
			fieldNameTemp = fieldName + String.valueOf(i);
			parameterSource.addValue(fieldNameTemp, iterator.next());
			tempSQL.append(", :").append(fieldNameTemp);
		}
		tempSQL.append(")");
		return tempSQL;
	}
}
