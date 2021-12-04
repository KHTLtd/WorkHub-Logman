package com.workhub.logman.spring.config.app;

import com.workhub.logman.dao.ILogDataDao;
import com.workhub.logman.dao.impl.LogDataDaoImpl;
import com.workhub.logman.spring.config.kafka.KafkaConfig;
import com.workhub.logman.persistence.service.IPersistenceService;
import com.workhub.logman.persistence.service.impl.PersistenceServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * Service Beans config
 *
 * @since 1.0.0 | 24.11.2021
 */
@Import({KafkaConfig.class})
public class BeansConfig {

    @Bean
    public IPersistenceService persistenceService(
            @Qualifier("persistenceTaskExecutor") AsyncTaskExecutor executor,
            ILogDataDao dao
    ) {
        return new PersistenceServiceImpl(executor, dao);
    }

    @Bean
    public ILogDataDao logDataDao() {
        return new LogDataDaoImpl();
    }
}
