package com.handy.sql.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@RequestMapping("/api/sql_test")
@RestController
public class SQLTestController {

	@ResponseBody
	@GetMapping("/")
	public String test() {
		return "sql_test1()";
	}
	
	@ResponseBody
	@GetMapping("/qq")
	public String test2() {
		return "sql_test2()";
	}
	
	@ResponseBody
	@GetMapping("/qq/")
	public String test3() {
		return "sql_test3()";
	}
	
	@ResponseBody
	@GetMapping("/qq/11")
	public String test4() {
		return "sql_test4()";
	}
	
//	@ResponseBody
//	@GetMapping("/qq/11/")
//	public String test5() {
//		return "sql_test5()";
//	}
	
	@ResponseBody
	@GetMapping("/qq/11/{qq}")
	public String test6() {
		return "sql_test6()";
	}
}