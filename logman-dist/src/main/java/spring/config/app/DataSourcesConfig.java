package spring.config.app;

import com.workhub.logman.routing.RoutingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DataSource Beans config
 *
 * @since 1.0.0 | 24.11.2021
 */
@Configuration
public class DataSourcesConfig {

    @Bean("logmanDs")
    RoutingDataSource logmanDs() {
        return new RoutingDataSource();
    }

}
