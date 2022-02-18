package com.workhub.logman.spring.config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jndi.JndiAccessor;

@Import({BeansConfig.class, DataSourcesConfig.class, TaskExecutorConfig.class})
public class SpringAppConfig {

    @Bean
    public JndiDataSourceLookup lookup() {
        return new JndiDataSourceLookup();
    }

    @Bean
    public JndiAccessor jndiAccessor() {
        return new JndiAccessor();
    }

}
