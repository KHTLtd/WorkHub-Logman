package com.workhub.logman.spring.config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.DefaultManagedTaskExecutor;
import com.workhub.commons.concurrency.executors.BlockingTaskExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.DefaultManagedTaskExecutor;
import com.workhub.commons.concurrency.executors.BlockingTaskExecutor;

/**
 * TaskExecutor Beans configuration
 *
 * @since 1.0.0 | 24.11.2021
 */
public class TaskExecutorConfig {
    private final String JNDI_EXECUTOR_PERSISTENCE = "jboss/ee/concurrency/executor/persistenceTaskExecutor";
    private final String JNDI_EXECUTOR_PARTITIONS_DELETION = "jboss/ee/concurrency/executor/partitionsTaskExecutor";

    @Bean("persistenceTaskExecutor")
    public DefaultManagedTaskExecutor persistenceTaskExecutor() {
        DefaultManagedTaskExecutor defaultManagedTaskExecutor = new DefaultManagedTaskExecutor();
        defaultManagedTaskExecutor.setResourceRef(true);
        defaultManagedTaskExecutor.setJndiName(JNDI_EXECUTOR_PERSISTENCE);
        return defaultManagedTaskExecutor;
    }

    @Bean("partitionsTaskExecutor")
    public DefaultManagedTaskExecutor partitionsTaskExecutor() {
        DefaultManagedTaskExecutor defaultManagedTaskExecutor = new BlockingTaskExecutor();
        defaultManagedTaskExecutor.setResourceRef(true);
        defaultManagedTaskExecutor.setJndiName(JNDI_EXECUTOR_PARTITIONS_DELETION);
        return defaultManagedTaskExecutor;
    }

}
