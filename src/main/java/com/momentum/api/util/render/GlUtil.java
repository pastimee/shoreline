package com.momentum.api.util.render;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author linus
 * @since 01/16/2023
 */
public class GlUtil {


    /**
     * Draws a rectangle
     *
     * @param x      The x-position of the rectangle
     * @param y      The y-position of the rectangle
     * @param width  The width of the rectangle
     * @param height The height of the rectangle
     * @param color  The color of the rectangle
     */
    public static void rect(float x, float y, float width, float height, int color) {

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
}
