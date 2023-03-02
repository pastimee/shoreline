package com.momentum.api.util.time;

import java.util.concurrent.TimeUnit;

/**
 * @author linus
 * @since 02/27/2023
 */
public class Timer {

    // time
    private long time;

    /**
     * Millisecond timer
     */
    public Timer() {

        // set curr system time
        time = System.currentTimeMillis();
    }

    /**
     * Checks if the timer has passed a given time
     *
     * @param in The time to check
     * @return Whether the timer has passed a given time
     */
    public boolean passed(long in) {

        // time since last reset
        long since = System.currentTimeMillis() - time;

        // time passed
        return since >= in;
    }

    /**
     * Checks if the timer has passed a given time
     *
     * @param in The time to check
     * @param unit The time unit to use
     * @return Whether the timer has passed a given time
     */
    public boolean passed(long in, TimeUnit unit) {

        // time since last reset
        long since = System.currentTimeMillis() - time;

        // time passed
        return since >= unit.toMillis(in);
    }

    /**
     * Resets the time
     */
    public void reset() {

        // set curr system time
        time = System.currentTimeMillis();
    }
}
