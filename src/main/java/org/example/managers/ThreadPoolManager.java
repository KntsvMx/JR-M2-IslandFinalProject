package org.example.managers;

import java.util.concurrent.*;

public class ThreadPoolManager {
    private final ThreadPoolExecutor computationExecutor;
    private final ScheduledExecutorService scheduledExecutor;

    public ThreadPoolManager() {
        computationExecutor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() / 2,
                Runtime.getRuntime().availableProcessors(),
                60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        scheduledExecutor = Executors.newScheduledThreadPool(2);
    }

    public void shutDown() {
        computationExecutor.shutdown();
        scheduledExecutor.shutdown();
    }

}
