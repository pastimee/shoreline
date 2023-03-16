package com.momentum.api.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author linus
 * @since 03/13/2023
 */
public interface IThreaded {

    // executors
    ExecutorService es =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
}
