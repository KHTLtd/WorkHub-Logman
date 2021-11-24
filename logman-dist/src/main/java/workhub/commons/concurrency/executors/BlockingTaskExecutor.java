package workhub.commons.concurrency.executors;

import io.micrometer.core.lang.NonNullApi;
import lombok.SneakyThrows;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.DefaultManagedTaskExecutor;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * TaskExecutor with a blocking strategy.
 * Waits until the task is complete or timed out.
 *
 * @author alexkirillov
 * @since 1.0.0 | 24.11.2021
 */
@NonNullApi
@SuppressWarnings("BusyWait")
public class BlockingTaskExecutor extends DefaultManagedTaskExecutor {

    protected long timeoutMil;
    private static final long SLEEP_TIME = 1;

    @SneakyThrows
    @Override
    public Future<?> submit(Runnable task) {
        setTimeout();
        long startTime = System.currentTimeMillis();
        while (isOperationTimedOut(startTime)) {
            try {
                return super.submit(task);
            } catch (TaskRejectedException taskRejectedException) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException interruptedException) {
                    throw taskRejectedException;
                }
            }
        }
        throw new TaskRejectedException(
                "Executor [" + this + "] did not accept task [" + task + "]. Free thread waiting timed out."
        );
    }

    @SneakyThrows
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        setTimeout();
        long startTime = System.currentTimeMillis();
        while (isOperationTimedOut(startTime)) {
            try {
                return super.submit(task);
            } catch (TaskRejectedException taskRejectedException) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException interruptedException) {
                    throw taskRejectedException;
                }
            }
        }
        throw new TaskRejectedException(
                "Executor [" + this + "] did not accept task [" + task + "]. Free thread waiting timed out."
        );
    }

    private void setTimeout() {
        try {
            timeoutMil = Long.parseLong(System.getProperty("task.executor.blocking.timeout", "300000"));
        } catch (Exception e) {
            timeoutMil = 300000;
        }
    }

    private boolean isOperationTimedOut(long startTime) {
        return System.currentTimeMillis() - startTime < timeoutMil;
    }
}
