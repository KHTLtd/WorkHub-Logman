package spring.config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.DefaultManagedTaskExecutor;
import workhub.commons.concurrency.executors.BlockingTaskExecutor;

/**
 * TaskExecutor Beans configuration
 *
 * @since 1.0.0 | 24.11.2021
 */
@Configuration
public class TaskExecutorConfig {

    private final String JNDI_EXECUTOR_PARTITIONS_DELETION = "jboss/ee/concurrency/executor/partitionsTaskExecutor";

    @Bean
    public DefaultManagedTaskExecutor partitionsTaskExecutor() {
        // TODO: change to Blocking strategy executor. cuz this one wont work, prbbly
        DefaultManagedTaskExecutor defaultManagedTaskExecutor = new BlockingTaskExecutor();
        defaultManagedTaskExecutor.setResourceRef(true);
        defaultManagedTaskExecutor.setJndiName(JNDI_EXECUTOR_PARTITIONS_DELETION);
        return defaultManagedTaskExecutor;
    }

}
