package spring.config.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan("com.workhub.logman.web.controller")
public class MvcConfig {
}
