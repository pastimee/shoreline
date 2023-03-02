package com.momentum.impl.ui.button;

import com.momentum.api.feature.Option;
import com.momentum.impl.init.Modules;
import com.momentum.impl.ui.ClickGuiScreen;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author linus
 * @since 02/01/2023
 */
public class NumberButton extends OptionButton<Number> {

    // number of decimal places
    private final int scale;

    /**
     * Config button
     *
     * @param x             The x-position
     * @param y             The y-position
     * @param option The associated config
     */
    protected NumberButton(float x, float y, Option<Number> option) {
        super(x, y, option);

        // value as a string
        String sval = String.valueOf(option.getVal());

        // rounding scale
        scale = sval.substring(sval.indexOf(".") + 1).length();
    }

    @Override
    protected void draw(float ix, float iy, int mouseX, int mouseY) {

        // update position
        x = ix;
        y = iy;

        // null check
        if (option.getMin() == null || option.getMax() == null) {
            return;
        }

        // check if the mouse is within the bounds of the component
        if (isWithin(mouseX, mouseY)) {

            // check if left mouse button is held
            if (ClickGuiScreen.MOUSE_LEFT_HOLD) {

                // percent of the slider that should be filled
                float fillv = (mouseX - ix) / width;

                // min and max values
                Number min = option.getMin();
                Number max = option.getMax();

                // integer val type
                if (option.getVal() instanceof Integer) {

                    // value of the button
                    float val = fillv * (max.intValue() - min.intValue());

                    // bound value
                    int bval = (int) MathHelper.clamp(val, min.intValue(), max.intValue());

                    // update config value
                    option.setVal(bval);
                }

                // float val type
                else if (option.getVal() instanceof Float) {

                    // value of the button
                    float val = fillv * (max.floatValue() - min.floatValue());

                    // bound value
                    float bval = MathHelper.clamp(val, min.floatValue(), max.floatValue());

                    // rounding using BigDecimal
                    BigDecimal bigDecimal = new BigDecimal(bval);

                    // round
                    bval = bigDecimal.setScale(scale, RoundingMode.HALF_UP).floatValue();

                    // update config value
                    option.setVal(bval);
                }

                // double val type
                else if (option.getVal() instanceof Double) {

                    // value of the button
                    double val = fillv * (max.doubleValue() - min.doubleValue());

                    // bound value
                    double bval = MathHelper.clamp(val, min.doubleValue(), max.doubleValue());

                    // rounding using BigDecimal
                    BigDecimal bigDecimal = new BigDecimal(bval);

                    // round
                    bval = bigDecimal.setScale(scale, RoundingMode.HALF_UP).doubleValue();

                    // update config value
                    option.setVal(bval);
                }

                // bounds
                float lower = ix + 1;
                float upper = ix + width - 1;

                // out of bounds
                if (mouseX < lower) {

                    // clamp to min
                    option.setVal(option.getMin());
                }

                else if (mouseX > upper) {

                    // clamp to max
                    option.setVal(option.getMax());
                }
            }
        }

        // slider fill
        float fill = (option.getVal().floatValue() - option.getMin().floatValue()) / (option.getMax().floatValue() - option.getMin().floatValue());

        // slider rect
        rect(ix, iy, (fill * width), height, Modules.COLOR_MODULE.getColorInt());
        mc.fontRenderer.drawStringWithShadow(option.getName() + TextFormatting.GRAY + " " + option.getVal(), ix + 2, iy + 4, -1);
    }
}
