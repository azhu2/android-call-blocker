package com.alexazhu.callblocker.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncExecutorUtil {
    private static final AsyncExecutorUtil INSTANCE = new AsyncExecutorUtil();

    private final Executor executor;

    private AsyncExecutorUtil() {
        executor = Executors.newFixedThreadPool(3);
    }

    public Executor getExecutor() {
        return executor;
    }

    public static AsyncExecutorUtil getInstance() {
        return INSTANCE;
    }
}
