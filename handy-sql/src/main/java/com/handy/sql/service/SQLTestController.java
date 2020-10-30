package com.handy.sql.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/api/sql_test")
public class SQLTestController {

	@ResponseBody
	@GetMapping()
	public String test() {
		return "sql_test()";
	}
}