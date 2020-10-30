package com.handy.sql;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.test.context.junit4.SpringRunner;

import com.handy.sql.boot.HandyApplication;
import com.handy.sql.config.SpringUtil;
import com.handy.sql.service.SQLService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HandyApplication.class)
public class AmassApplicationTests {

	@Autowired
	private SQLService sqlService;
	
	private JdbcTemplate jdbcTemplate;
	@Test
	public void testAdd() {
		String sql = "create table test_aa (`runoob_author` VARCHAR(40) NOT NULL);";
		
		Object aa = jdbcTemplate.execute(new StatementCallback() {
			
			@Override
			public Object doInStatement(Statement stmt) throws SQLException, DataAccessException {
				return stmt.execute(sql);
			}
		});
		System.out.println(aa);
		sqlService.execute("create database `handy` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;");
	}
}