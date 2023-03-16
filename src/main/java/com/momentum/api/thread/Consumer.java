package com.momentum.api.thread;

import com.momentum.api.feature.Feature;
import net.minecraft.entity.item.EntityEnderCrystal;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

/**
 * @author linus
 * @since 03/13/2023
 */
public abstract class Consumer<F extends Feature, E>
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
    public Consumer(F feature, Queue<E> sharedObject) {
        this.feature = feature;
        this.sharedObject = sharedObject;
    }

    // consume
    @Override
    public abstract E call();

    /**
     * Submit to executor
     */
    public void consume() {

        // submit to com
        cs.submit(this);
        calls++;
    }
}
