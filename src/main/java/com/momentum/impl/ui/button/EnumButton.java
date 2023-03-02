package com.momentum.impl.ui.button;

import com.momentum.api.feature.Option;
import com.momentum.api.util.render.Formatter;
import com.momentum.impl.init.Modules;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;

/**
 * @author linus
 * @since 02/06/2023
 */
public class EnumButton extends OptionButton<Enum<?>> {

    // enum index
    private int index;

    /**
     * Config button
     *
     * @param x      The x-position
     * @param y      The y-position
     * @param option The associated config
     */
    protected EnumButton(float x, float y, Option<Enum<?>> option) {
        super(x, y, option);
    }

    @Override
    protected void draw(float ix, float iy, int mouseX, int mouseY) {

        // update positions
        x = ix;
        y = iy;

        // config value
        String val = Formatter.formatEnum(option.getVal());

        // draw the component
        rect(Modules.COLOR_MODULE.getColorInt());
        mc.fontRenderer.drawStringWithShadow(option.getName() + TextFormatting.GRAY + " " + val, ix + 2, iy + 4, -1);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void click(int mouseX, int mouseY, int mouseButton) {

        // check if the mouse is within the bounds of the component
        if (isWithin(mouseX, mouseY)) {

            // option value
            Enum<?> val = option.getVal();

            // search all values
            String[] values = Arrays.stream(val.getClass().getEnumConstants())
                    .map(Enum::name)
                    .toArray(String[]::new);

            // left click
            if (mouseButton == 0) {

                // update index
                index = index + 1 > values.length - 1 ? 0 : index + 1;

                // use value index
                option.setVal(Enum.valueOf(val.getClass(), values[index]));
            }

            // right click
            else if (mouseButton == 1) {

                // update index
                index = index - 1 < 0 ? values.length - 1 : index - 1;

                // use value index
                option.setVal(Enum.valueOf(val.getClass(), values[index]));
            }
        }
    }
}
