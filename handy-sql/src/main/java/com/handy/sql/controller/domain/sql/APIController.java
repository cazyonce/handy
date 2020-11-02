package com.handy.sql.controller.domain.sql;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.handy.sql.service.APIService;

@RestController
@Validated
@RequestMapping("/api")
public class APIController {

	@Autowired
	private APIService apiService;

	@PostMapping()
	public void test(@RequestBody Map<String, String> map) throws Exception {
		apiService.addAPI(map);
	}
}
