package com.caspian.impl.event.chat;

import com.caspian.api.event.Cancelable;
import com.caspian.api.event.Event;
import net.minecraft.client.util.math.MatrixStack;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
@Cancelable
public class ChatRenderEvent extends Event
{
    //
    private final MatrixStack matrices;

    //
    private final float x, y;

    public ChatRenderEvent(MatrixStack matrices, float x, float y)
    {
        this.matrices = matrices;
        this.x = x;
        this.y = y;
    }

    public MatrixStack getMatrices()
    {
        return matrices;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }
}