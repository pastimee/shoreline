package com.momentum.impl.ui.button;

import com.momentum.api.feature.Option;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

/**
 * @author linus
 * @since 02/01/2023
 */
public class BindButton extends OptionButton<Integer> {

    // check whether we are listening for an input
    private boolean listen;

    /**
     * Bind button
     *
     * @param x      The x-position
     * @param y      The y-position
     * @param option The associated config
     */
    protected BindButton(float x, float y, Option<Integer> option) {
        super(x, y, option);
    }

    @Override
    protected void draw(float ix, float iy, int mouseX, int mouseY) {

        // update positions
        x = ix;
        y = iy;

        // config value
        String val = listen ? "..." : Keyboard.getKeyName(option.getVal());

        // draw the component
        rect(0x00000000);
        mc.fontRenderer.drawStringWithShadow(option.getName() + TextFormatting.GRAY + " " + val, ix + 2, iy + 4, -1);
    }

    @Override
    protected void click(int mouseX, int mouseY, int mouseButton) {

        // check if the mouse is within the bounds of the component
        if (isWithin(mouseX, mouseY)) {

            // left click
            if (mouseButton == 0) {

                // toggle listen state
                listen = !listen;
            }
        }
    }

    @Override
    public void type(char charTyped, int key) {

        // check if we are listening for key input
        if (listen) {

            // unbind
            if (key == Keyboard.KEY_ESCAPE || key == Keyboard.KEY_BACK) {

                // update the bind
                option.setVal(Keyboard.KEY_NONE);
            }

            // input bind
            else {

                // update the bind
                option.setVal(key);
            }

            // no longer listening
            listen = false;
        }
    }
}
