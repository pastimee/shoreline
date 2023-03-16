package com.momentum.impl.ui;

import com.momentum.Momentum;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import com.momentum.api.util.render.Formatter;
import com.momentum.impl.init.Modules;
import com.momentum.impl.ui.button.OptionButton;
import com.momentum.impl.ui.button.ModuleButton;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author linus
 * @since 01/16/2023
 */
public class Frame extends DrawableRect {

    // position and states
    private float px, py, fheight;
    private float off; // global module offset
    private boolean open;
    private boolean drag;

    // category
    private final String category;

    // module components
    List<ModuleButton> moduleButtons = new CopyOnWriteArrayList<>();

    /**
     * Initializes the category component
     *
     * @param category The category
     */
    public Frame(float x, float y, ModuleCategory category) {
        super(x, y, 88, 14);

        // formatted name of the category
        this.category = Formatter.formatEnum(category);

        // sort all modules
        for (Module d : Momentum.MODULE_REGISTRY.getData()) {

            // check if the module's category matches
            if (d.getCategory() == category) {

                // add to module components
                moduleButtons.add(new ModuleButton(x, y, d, this));
            }
        }

        // all category frames are open by default
        open = true;
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        // check if the mouse is within the bounds of the component
        if (isWithin(mouseX, mouseY)) {

            // check if left mouse button is held
            if (ClickGuiScreen.MOUSE_LEFT_HOLD) {

                // drag component
                drag = true;
            }
        }

        // drag state
        if (drag) {

            // update position
            x += ClickGuiScreen.MOUSE_X - px;
            y += ClickGuiScreen.MOUSE_Y - py;
        }

        // draw the component
        rect(Modules.COLOR_MODULE.getColorInt());
        mc.fontRenderer.drawStringWithShadow(category, x + 3, y + 3, -1);

        // check frame open state
        if (open) {

            // total frame height
            fheight = 3;
            for (ModuleButton moduleButton : moduleButtons) {

                // account for button height
                fheight += 15.5f;

                // account for open config buttons
                if (moduleButton.isOpen()) {

                    // account for gap
                    fheight += 0.5f;

                    // all config buttons in the module button
                    for (OptionButton optionButton : moduleButton.getConfigButtons()) {

                        // config button height may vary
                        fheight += optionButton.getHeight();
                    }
                }
            }

            // background
            rect(x, y + height, 88, fheight, 0x77000000);

            // draw all module components
            off = y + height + 1;
            for (ModuleButton moduleButton : moduleButtons) {

                // draw module component
                moduleButton.draw(x + 1f, off + 1, mouseX, mouseY);
                off += 15.5f;
            }
        }

        // update previous position
        px = ClickGuiScreen.MOUSE_X;
        py = ClickGuiScreen.MOUSE_Y;
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {

        // check if the mouse is within the bounds of the component
        if (isWithin(mouseX, mouseY)) {

            // right click
            if (mouseButton == 1) {

                // toggle open state
                open = !open;
            }
        }

        // click all module components
        if (open) {
            for (ModuleButton moduleButton : moduleButtons) {

                // run on click event
                moduleButton.click(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void type(char charTyped, int key) {

        // type all module components
        if (open) {
            for (ModuleButton moduleButton : moduleButtons) {

                // run on click event
                moduleButton.type(charTyped, key);
            }
        }
    }

    /**
     * Gets the total height of the frame
     *
     * @return The total height
     */
    public float getTotalHeight() {

        // total height
        return fheight;
    }

    /**
     * Releases the drag state
     */
    public void release() {

        // drag state becomes false
        drag = false;
    }

    /**
     * Update global offset
     *
     * @param in The offset
     */
    public void offset(float in) {
        off += in;
    }

    /**
     * Opens the frame
     *
     * @param in The new open state
     */
    public void open(boolean in) {
        open = in;
    }

    /**
     * Checks if the frame is open
     *
     * @return Whether the frame is open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Gets the category name
     *
     * @return The category name
     */
    public String getCategory() {
        return category;
    }
}
