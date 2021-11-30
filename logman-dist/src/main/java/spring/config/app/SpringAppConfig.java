package spring.config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jndi.JndiAccessor;

@Configuration
@Import({BeansConfig.class, DataSourcesConfig.class})
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