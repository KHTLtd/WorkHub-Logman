package workhub.commons.concurrency.workmanagers;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class JettyWorkManager implements ExecutorService {
    private final ExecutorService workers = Executors.newFixedThreadPool(5);

    public JettyWorkManager() {
    }

    @Override
    public void shutdown() {
        this.workers.shutdown();
    }

    @NotNull
    @Override
    public List<Runnable> shutdownNow() {
        return this.workers.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return this.workers.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return this.workers.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return this.workers.awaitTermination(timeout, unit);
    }

    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        return this.workers.submit(task);
    }

    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Runnable task, T result) {
        return this.workers.submit(task, result);
    }

    @NotNull
    @Override
    public Future<?> submit(@NotNull Runnable task) {
        return this.workers.submit(task);
    }

    @NotNull
    @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.workers.invokeAll(tasks);
    }

    @NotNull
    @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return this.workers.invokeAll(tasks, timeout, unit);
    }

    @NotNull
    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return this.workers.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.workers.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void execute(@NotNull Runnable command) {
        this.workers.execute(command);
    }
}
