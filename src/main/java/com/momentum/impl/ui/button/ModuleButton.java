package com.momentum.impl.ui.button;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.impl.init.Modules;
import com.momentum.impl.ui.DrawableRect;
import com.momentum.impl.ui.Frame;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author linus
 * @since 01/16/2023
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ModuleButton extends DrawableRect {

    // position and states
    private boolean open;

    // module
    private final Module module;
    private final Frame frame;

    // config components
    private final List<OptionButton> optionButtons = new CopyOnWriteArrayList<>();

    /**
     * Initializes the module component
     *
     * @param module The module
     */
    public ModuleButton(float x, float y, Module module, Frame frame) {
        super(x, y, 86, 15);

        // assign module
        this.module = module;
        this.frame = frame;

        // all module configurations
        for (Option option : module.getOptions()) {

            // null check
            if (option != null) {

                // ignore enabled state
                if (option.getName().equalsIgnoreCase("Enabled")) {
                    continue;
                }

                // bind config
                if (option.getName().equalsIgnoreCase("Bind")) {

                    // add to list of config components
                    optionButtons.add(new BindButton(x, y, (Option<Integer>) option));
                }

                // boolean config
                else if (option.getVal() instanceof Boolean) {

                    // add to list of config components
                    optionButtons.add(new BooleanButton(x, y, option));
                }

                // number config
                else if (option.getVal() instanceof Number) {

                    // int config
                    if (option.getVal() instanceof Integer) {

                        // add to list of config components
                        optionButtons.add(new NumberButton(x, y, option));
                    }

                    // int config
                    else if (option.getVal() instanceof Float) {

                        // add to list of config components
                        optionButtons.add(new NumberButton(x, y, option));
                    }

                    // int config
                    else if (option.getVal() instanceof Double) {

                        // add to list of config components
                        optionButtons.add(new NumberButton(x, y, option));
                    }
                }

                // enum option
                else if (option.getVal() instanceof Enum<?>) {

                    // add to list of config components
                    optionButtons.add(new EnumButton(x, y, option));
                }
            }
        }
    }

    /**
     * Draw function with position updates
     *
     * @param ix     The x position
     * @param iy     The y position
     * @param mouseX The mouse's x position
     * @param mouseY The mouse's y position
     */
    public void draw(float ix, float iy, int mouseX, int mouseY) {

        // update positions
        x = ix;
        y = iy;

        // draw the component
        rect(module.isEnabled() ? Modules.COLOR_MODULE.getColorInt() : 0x55555555);
        mc.fontRenderer.drawStringWithShadow(module.getName(), ix + 2, iy + 4, -1);

        // run event on all components
        if (open) {

            // draw all config components
            float off = y + height + 0.5f;
            for (OptionButton<?> optionButton : optionButtons) {

                // run draw event
                optionButton.draw(ix + 0.5f, off, mouseX, mouseY);
                frame.offset(15);
                off += 15;
            }
        }
    }

    @Override
    protected void draw(int mouseX, int mouseY) {

        // call draw
        draw(x, y, mouseX, mouseY);
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {

        // check if the mouse is within the bounds of the component
        if (isWithin(mouseX, mouseY)) {

            // left click
            if (mouseButton == 0) {

                // toggle module
                module.toggle();
            }

            // right click
            if (mouseButton == 1) {

                // toggle open state
                open = !open;
            }
        }

        // run event on all components
        if (open) {
            for (OptionButton<?> optionButton : optionButtons) {

                // run draw event
                optionButton.click(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void type(char charTyped, int key) {

        // run event on all components
        if (open) {
            for (OptionButton<?> optionButton : optionButtons) {

                // run type event
                optionButton.type(charTyped, key);
            }
        }
    }

    /**
     * Gets all associated config buttons
     *
     * @return The associated config buttons
     */
    public List<OptionButton> getConfigButtons() {
        return optionButtons;
    }

    /**
     * Checks if the module button is currently open
     *
     * @return Whether the module button is currently open
     */
    public boolean isOpen() {
        return open;
    }
}
