package com.handy.sql.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
public class SQLService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Object querySQL(String sql) {
		return jdbcTemplate.query(sql, new RowMapper<HashMap<String, Object>>() {

			@Override
			public HashMap<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				HashMap<String, Object> map = new HashMap<String, Object>();

				return map;
			}
		});
	}
	
	public void execute(String sql) {
		jdbcTemplate.execute(sql);
	}

}
