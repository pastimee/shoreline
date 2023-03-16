package com.momentum.api.thread;

import com.momentum.api.feature.Feature;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;

/**
 * @author linus
 * @since 03/13/2023
 */
public abstract class Producer<F extends Feature, E>
        implements Callable<E>, IThreaded {

    // queue
    protected final F feature;
    protected final Queue<E> sharedObject;

    // completion service
    private int calls;
    private final ExecutorCompletionService<E> cs =
            new ExecutorCompletionService<>(es);

    /**
     * Initializes the queue
     *
     * @param feature The feature
     * @param sharedObject The shared queue object
     */
    public Producer(F feature, Queue<E> sharedObject) {
        this.feature = feature;
        this.sharedObject = sharedObject;
    }

    // produce
    @Override
    public abstract E call();

    /**
     * Submit to executor
     */
    public void produce() {

        // submit to com
        cs.submit(this);
        calls++;
    }
}
