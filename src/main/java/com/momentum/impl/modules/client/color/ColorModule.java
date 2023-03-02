package com.momentum.impl.modules.client.color;

import com.momentum.api.feature.Option;
import com.momentum.api.module.ConcurrentModule;
import com.momentum.api.module.ModuleCategory;

import java.awt.*;

/**
 * @author linus
 * @since 01/22/2023
 */
public class ColorModule extends ConcurrentModule {

    // option
    public final Option<Color> colorOption =
            new Option<>("Color", "Client color", new Color(252, 3, 82));

    public ColorModule() {
        super("Color", new String[] {"ClientColor", "GlobalColor"}, "The client's color", ModuleCategory.CLIENT);

        // options
        associate(
                colorOption,
                drawn
        );

        // default hidden
        draw(false);
    }

    /**
     * Gets the client color
     *
     * @return The client color
     */
    public Color getColor() {
        return colorOption.getVal();
    }

    /**
     * Integrate alpha into the client color
     *
     * @param alpha The alpha value
     * @return The client color
     */
    public Color getColor(int alpha) {

        // alpha out of bounds
        if (alpha < 0 || alpha > 255) {
            throw new IndexOutOfBoundsException();
        }

        // client color
        Color c = getColor();

        // break down into rgb components
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        // create new color to integrate alpha
        return new Color(r, g, b, alpha);
    }


    /**
     * Gets the RGB int for the client color
     *
     * @return The RGB int for the client color
     */
    public int getColorInt() {
        return getColor().getRGB();
    }

    /**
     * Gets the RGB int for the client color
     *
     * @param alpha The alpha value
     * @return The RGB int for the client color
     */
    public int getColorInt(int alpha) {
        return getColor(alpha).getRGB();
    }
}
