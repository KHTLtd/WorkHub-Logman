package spring.config.app;

import com.workhub.logman.dao.ILogDataDao;
import com.workhub.logman.dao.impl.LogDataDaoImpl;
import com.workhub.logman.persistence.service.IPersistenceService;
import com.workhub.logman.persistence.service.impl.PersistenceServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Service Beans config
 *
 * @since 1.0.0 | 24.11.2021
 */
@Configuration
public class BeansConfig {

    @Bean
    public IPersistenceService persistenceService() {
        return new PersistenceServiceImpl();
    }

    @Bean
    public ILogDataDao logDataDao() {
        return new LogDataDaoImpl();
    }
}
