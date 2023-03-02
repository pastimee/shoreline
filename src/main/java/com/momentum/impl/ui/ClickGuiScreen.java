package com.momentum.impl.ui;

import com.momentum.api.module.ModuleCategory;
import com.momentum.impl.init.Modules;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author linus
 * @since 01/16/2023
 */
public class ClickGuiScreen extends GuiScreen {

    // mouse position
    public static int MOUSE_X;
    public static int MOUSE_Y;

    // mouse states
    public static boolean MOUSE_RIGHT_CLICK;
    public static boolean MOUSE_RIGHT_HOLD;
    public static boolean MOUSE_LEFT_CLICK;
    public static boolean MOUSE_LEFT_HOLD;

    // category frames
    private final List<Frame> frames = new CopyOnWriteArrayList<>();

    /**
     * Initializes the ClickGui screen
     */
    public ClickGuiScreen() {

        // add all categories
        int x = 2;
        for (ModuleCategory category : ModuleCategory.values()) {

            // check if the category is valid
            if (!category.equals(ModuleCategory.HIDDEN)) {

                // add to category frames
                frames.add(new Frame(x, 10, category));
                x += 90;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        // run events on all category frames
        for (Frame c : frames) {

            // run event
            c.draw(mouseX, mouseY);
        }

        // update mouse state
        MOUSE_LEFT_CLICK = false;
        MOUSE_RIGHT_CLICK = false;

        // update mouse position
        MOUSE_X = mouseX;
        MOUSE_Y = mouseY;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // left click
        if (mouseButton == 0) {

            // update mouse states
            MOUSE_LEFT_CLICK = true;
            MOUSE_LEFT_HOLD = true;
        }

        // right click
        else if (mouseButton == 1) {

            // update mouse states
            MOUSE_RIGHT_CLICK = true;
            MOUSE_RIGHT_HOLD = true;
        }

        // run events on all category frames
        for (Frame c : frames) {

            // run event
            c.click(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        // no longer holding mouse
        if (state == 0) {

            // update mouse states
            MOUSE_LEFT_HOLD = false;
            MOUSE_RIGHT_HOLD = false;

            // run events on all category frames
            for (Frame c : frames) {

                // release state
                c.release();
            }
        }
    }

    @Override
    protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
        super.keyTyped(p_keyTyped_1_, p_keyTyped_2_);

        // run events on all category frames
        for (Frame c : frames) {

            // release state
            c.type(p_keyTyped_1_, p_keyTyped_2_);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        // turn off module
        Modules.CLICKGUI_MODULE.disable();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Gets the frames
     *
     * @return The frames
     */
    public Collection<Frame> getFrames() {
        return frames;
    }
}
