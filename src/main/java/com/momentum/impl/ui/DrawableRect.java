package com.momentum.impl.ui;

import com.momentum.api.util.Wrapper;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author linus
 * @since 01/16/2023
 */
public abstract class DrawableRect implements Wrapper {

    // position
    protected float x, y, width, height;

    /**
     * Dimensions of a rect
     *
     * @param x      The x-position
     * @param y      The y-position
     * @param width  The width
     * @param height The height
     */
    public DrawableRect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Draws the component
     *
     * @param mouseX The x-position of the mouse
     * @param mouseY The y-position of the mouse
     */
    protected abstract void draw(int mouseX, int mouseY);

    /**
     * Clicked the component
     *
     * @param mouseX      The x-position of the mouse
     * @param mouseY      The y-position of the mouse
     * @param mouseButton The pressed button
     */
    protected abstract void click(int mouseX, int mouseY, int mouseButton);

    /**
     * Typed on the keyboard
     *
     * @param charTyped The character typed
     * @param key       The key pressed
     */
    protected abstract void type(char charTyped, int key);

    /**
     * Updates the rectangle's position
     *
     * @param x The rectangle's x-position
     * @param y The rectangle's y-position
     */
    public void position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Draws a rectangle
     *
     * @param color The color of the rectangle
     */
    public void rect(int color) {
        rect(x, y, width, height, color);
    }

    /**
     * Draws a rectangle
     *
     * @param x      The x-position of the rectangle
     * @param y      The y-position of the rectangle
     * @param width  The width of the rectangle
     * @param height The height of the rectangle
     * @param color  The color of the rectangle
     */
    public void rect(float x, float y, float width, float height, int color) {

        // color as a object
        Color c = new Color(color, true);

        // gl args
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        glBegin(GL_QUADS);
        glColor4f((float) c.getRed() / 255, (float) c.getGreen() / 255, (float) c.getBlue() / 255, (float) c.getAlpha() / 255);

        // assign vertices
        glVertex2f(x, y);
        glVertex2f(x, y + height);
        glVertex2f(x + width, y + height);
        glVertex2f(x + width, y);
        glColor4f(0, 0, 0, 1);

        // end
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glPopMatrix();
    }

    /**
     * Checks if a given value is between the width and the height
     *
     * @param xval The x-position of the value
     * @param yval The y-position of the value
     * @return Whether the given value is between the width and the height
     */
    public boolean isWithin(float xval, float yval) {
        return isWithin(xval, yval, x, y, width, height);
    }

    /**
     * Checks if a given value is between the min and the mix
     *
     * @param xval The x-position of the value
     * @param yval The y-position of the value
     * @param xmin The x-position of the minimum bound
     * @param xmax The x-position of the maximum bound
     * @param ymin The y-position of the minimum bound
     * @param ymax The y-position of the maximum bound
     * @return Whether the given value is between the min and the mix
     */
    public boolean isWithin(float xval, float yval, float xmin, float ymin, float xmax, float ymax) {
        return xval >= xmin && xval <= xmin + xmax && yval >= ymin && yval <= ymin + ymax;
    }

    /**
     * Gets the x position
     *
     * @return The x position
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y position
     *
     * @return The y position
     */
    public float getY() {
        return y;
    }
}
