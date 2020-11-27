package com.handy.sql.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.handy.sql.netty.GlobalProvide;
import com.handy.sql.netty.exception.CustomException;

public class TestSelectSQLConverter {

	public static void main(String[] args) {
		Map<String, List<String>> urlParameters = new HashMap<String, List<String>>();

		List<String> list1 = new ArrayList<String>();
		list1.add("1");
		list1.add("2");
		List<String> list2 = new ArrayList<String>();
		list2.add("3");
		urlParameters.put("aa", list1);
		urlParameters.put("22", list1);
		urlParameters.put("233", list2);
		String sql = "select * from api_mapping where id=:id <*where>[aa,22,233];";
		String sql2 = "select * from api_mapping <where>[aa,22,233];";
		String sql3 = "select * from api_mapping <where>[ aa , 22 ];";
		String sql4 = "select * from api_mapping <where>[ aa , 22, 44]";
		String sql5 = "select * from api_mapping where aa=:aa";
		String sql6 = "select * from api_mapping <where>";
		String sql7 = "select * from api_mapping where aa2=:aa2 <*where>";
		String[] sqls = { sql, sql2, sql3, sql4, sql5, sql6, sql7 };

		for (String string : sqls) {
			MapSqlParameterSource parameterSource = new MapSqlParameterSource();
			System.out.println("下下下下下下下下下下下下下下下下下下下下下下下下下下下下下");
			System.out.println("rwa: " + string);
			try {
				System.out.println("convert after: "
						+ GlobalProvide.SELECT_SQL_CONVERTER.convert(string, urlParameters, parameterSource));
				System.out.println(urlParameters);
				System.out.println(parameterSource);
			} catch (CustomException e) {
				e.printStackTrace();
			}
			System.out.println("上上上上上上上上上上上上上上上上上上上上上上上上上上上上上");
		}
	}
}
