package com.handy.sql.boot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.handy.sql.consts.Consts;

@ComponentScan({
	Consts.PACKAGE_CONTROLLER_INTERCEPTOR,
	Consts.PACKAGE_CONTROLLER_DOMAIN_SQL,
	Consts.PACKAGE_CONFIG,
	Consts.PACKAGE_SERVICE,
	})
@EnableAutoConfiguration
@EnableScheduling
@Configuration
@SpringBootApplication
public class HandyApplication {

	public static void main(String[] args) {
		SpringApplication.run(HandyApplication.class, args);
	}
}