package spring.config.app;

import com.workhub.logman.routing.RoutingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * DataSource Beans config
 *
 * @since 1.0.0 | 24.11.2021
 */
@Configuration
public class DataSourcesConfig {

    @Bean("logmanDs")
    DataSource logmanDs() {
        return new RoutingDataSource();
    }

}
