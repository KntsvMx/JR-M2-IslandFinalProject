package org.example.global;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class GlobalVariables {
    public static final ReentrantLock lock = new ReentrantLock();
    public static final ThreadLocalRandom random = ThreadLocalRandom.current();
}
