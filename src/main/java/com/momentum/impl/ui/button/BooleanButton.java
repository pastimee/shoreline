package com.momentum.impl.ui.button;

import com.momentum.api.feature.Option;
import com.momentum.impl.init.Modules;

/**
 * @author linus
 * @since 01/28/2023
 */
public class BooleanButton extends OptionButton<Boolean> {

    /**
     * Boolean button
     *
     * @param x      The x-position
     * @param y      The y-position
     * @param option The associated config
     */
    protected BooleanButton(float x, float y, Option<Boolean> option) {
        super(x, y, option);
    }

    @Override
    protected void draw(float ix, float iy, int mouseX, int mouseY) {

        // update positions
        x = ix;
        y = iy;

        // config value
        boolean val = option.getVal();

        // draw the component
        rect(val ? Modules.COLOR_MODULE.getColorInt() : 0x00000000);
        mc.fontRenderer.drawStringWithShadow(option.getName(), ix + 2, iy + 4, -1);
    }

    @Override
    protected void click(int mouseX, int mouseY, int mouseButton) {

        // check if the mouse is within the bounds of the component
        if (isWithin(mouseX, mouseY)) {

            // left click
            if (mouseButton == 0) {

                // config value
                boolean val = option.getVal();

                // toggle module
                option.setVal(!val);
            }
        }
    }
}
