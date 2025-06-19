package org.example.managers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LockManager {
    private final Map<Object, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public ReentrantLock getLock(Object key) {
        return lockMap.computeIfAbsent(key, k -> new ReentrantLock());
    }

    public void executeWithLock(Object key, Runnable runnable) {
        ReentrantLock lock = getLock(key);
        try {
            if(lock.tryLock(100, TimeUnit.MILLISECONDS)) {
                try {
                    runnable.run();
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
