package com.handy.sql.controller.domain.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.handy.sql.service.SQLService;

@RestController
@Validated
@RequestMapping("/api/sql")
public class SQLController {

	@Autowired
	private SQLService sqlService;
	
	@GetMapping()
	public void test() {
		System.out.println("test()");
	}
}
