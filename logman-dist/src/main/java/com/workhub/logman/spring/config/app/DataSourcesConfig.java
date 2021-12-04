package com.workhub.logman.spring.config.app;

import com.workhub.logman.routing.RoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * DataSource Beans config
 *
 * @since 1.0.0 | 24.11.2021
 */
public class DataSourcesConfig {

    @Bean("logmanDs")
    RoutingDataSource logmanDs() {
        return new RoutingDataSource();
    }

    @Bean
    NamedParameterJdbcOperations namedParameterTemplate(@Qualifier("logmanDs")DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}
