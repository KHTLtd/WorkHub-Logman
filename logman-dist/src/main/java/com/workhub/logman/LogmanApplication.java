package com.workhub.logman;

//import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;


//@EnableAdminServer
@ComponentScan("com.workhub.logman.spring.config")
@SpringBootApplication
public class LogmanApplication  {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(LogmanApplication.class, args);
		for (String name : context.getBeanDefinitionNames()) {
			System.out.println(name);
		}
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(LogmanApplication.class);
//	}
}


