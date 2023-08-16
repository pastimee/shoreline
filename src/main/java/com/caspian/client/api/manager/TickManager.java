package com.caspian.client.api.manager;

import com.caspian.client.Caspian;
import com.caspian.client.api.handler.tick.TickHandler;
import com.caspian.client.api.handler.tick.TickSync;

/**
 *
 *
 * @author linus
 * @since 1.0
 *
 * @see TickHandler
 */
public class TickManager
{
    // The TPS tick handler.
    private final TickHandler handler = new TickHandler();;

    /**
     *
     *
     */
    public TickManager()
    {
        Caspian.EVENT_HANDLER.subscribe(handler);
    }

    /**
     *
     *
     * @return
     */
    public float getTpsAverage()
    {
        float avg = 0.0f;
        if (handler.hasTicks())
        {
            for (float t : handler.getTicks())
            {
                avg += t;
            }
            avg /= handler.size();
        }
        return avg;
    }

    /**
     *
     *
     * @return
     */
    public float getTpsCurrent()
    {
        if (handler.hasTicks())
        {
            return handler.peek();
        }
        return 20.0f;
    }

    /**
     *
     *
     * @return
     */
    public float getTpsMin()
    {
        float min = 20.0f;
        for (float t : handler.getTicks())
        {
            if (t < min)
            {
                min = t;
            }
        }
        return min;
    }

    /**
     *
     *
     * @param tps
     * @return
     */
    public float getTickSync(TickSync tps)
    {
        return switch (tps)
                {
                    case AVERAGE -> getTpsAverage();
                    case CURRENT -> getTpsCurrent();
                    case MINIMAL -> getTpsMin();
                    case NONE -> 20.0f;
                };
    }
}
