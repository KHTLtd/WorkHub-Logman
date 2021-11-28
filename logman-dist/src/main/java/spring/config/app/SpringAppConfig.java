package spring.config.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BeansConfig.class, DataSourcesConfig.class})
public class SpringAppConfig {
}
