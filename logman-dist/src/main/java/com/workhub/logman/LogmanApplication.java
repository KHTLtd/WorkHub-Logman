package com.workhub.logman;

import com.workhub.logman.spring.config.app.SpringAppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@Import(SpringAppConfig.class)
public class  LogmanApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(LogmanApplication.class, args);
		for (String name : context.getBeanDefinitionNames()) {
			System.out.println(name);
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(LogmanApplication.class);
	}
}


