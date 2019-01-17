package com.twinkle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.twinkle.user.repository.mapper")
public class TwinkleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwinkleApplication.class, args);
	}

}

