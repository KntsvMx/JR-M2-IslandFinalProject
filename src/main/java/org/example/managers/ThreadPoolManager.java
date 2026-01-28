package org.example.managers;

import java.util.concurrent.*;

public class ThreadPoolManager {
    private static volatile ThreadPoolManager instance;
    private final ScheduledExecutorService scheduledPool;
    private final ExecutorService servicePool;



    public ThreadPoolManager() {
        this.servicePool = Executors.newFixedThreadPool(4);
        this.scheduledPool = Executors.newScheduledThreadPool(2);
    }

    public static ThreadPoolManager getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolManager.class) {
                if(instance == null) {
                    instance = new ThreadPoolManager();
                }
            }
        }
        return instance;
    }

    public Future<?> submit(Runnable task) {
        return servicePool.submit(task);
    }

    public void scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        scheduledPool.scheduleAtFixedRate(task, initialDelay, period, unit);
    }


    public void shutDown() {
        servicePool.shutdown();
        scheduledPool.shutdown();
    }

}
