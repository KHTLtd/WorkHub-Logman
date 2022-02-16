package com.workhub.logman.spring.config.web;

import com.workhub.logman.web.controller.LogController;
import org.springframework.context.annotation.Bean;

public class ControllerConfig {

    @Bean
    LogController logController() {
        return new LogController();
    }

}
