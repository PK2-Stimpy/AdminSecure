package me.pk2.adminsecure.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorCentral {
    public static final Executor webhookExecutor = Executors.newFixedThreadPool(4); /* 4 Threads Pool */
}