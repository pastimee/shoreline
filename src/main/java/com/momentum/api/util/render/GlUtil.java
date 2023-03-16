package com.momentum.api.util.render;

import com.momentum.api.util.Wrapper;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author linus
 * @since 01/16/2023
 */
public class GlUtil implements Wrapper {
    
    // 2d
    
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
        glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f);

        // assign vertices
        glVertex2f(x, y);
        glVertex2f(x, y + height);
        glVertex2f(x + width, y + height);
        glVertex2f(x + width, y);
        glColor4f(0.0f, 0.0f, 0.0f, 1.0f);

        // end
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glPopMatrix();
    }
    
    // 3d

    // renderers
    public static final Tessellator tessellator = Tessellator.getInstance();
    public static final BufferBuilder buffer = tessellator.getBuffer();
    public static final RenderManager renderer = mc.getRenderManager();

    /**
     * Draws a box with specified dimensions at the specified position
     *
     * @param bb The specified position
     * @param color The color of the box
     */
    public static void box(AxisAlignedBB bb, int color) {

        // offset to render pos
        bb = bb.offset(-renderer.viewerPosX, -renderer.viewerPosY, -renderer.viewerPosZ);

        // prepare render
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        // GlStateManager.color(0.0f, 1.0f, 0.0f, 0.75f);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.glLineWidth(6.0f);

        // begin the buffer
        buffer.begin(GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        // add box vertices
        Color c = new Color(color, true);
        addChainedFilledBoxVertices(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ,
                c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());

        // draw the box
        tessellator.draw();

        // release render
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    /**
     * Draws a bounding box
     *
     * @param bb The position
     * @param width The line width
     * @param color The box color
     */
    public static void boundingBox(AxisAlignedBB bb, float width, int color) {

        // offest to render pos
        bb = bb.offset(-renderer.viewerPosX, -renderer.viewerPosY, -renderer.viewerPosZ);

        // prepare render
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        GlStateManager.glLineWidth(width);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);

        // begin the buffer
        buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        // add box vertices
        Color c = new Color(color);
        addChainedBoundingBoxVertices(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ,
                c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());

        // draw the box
        tessellator.draw();

        // release render
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    /**
     * Add box vertices to the buffer
     *
     * @see net.minecraft.client.renderer.RenderGlobal
     * @param minX The min x of the box
     * @param minY The min y of the box
     * @param minZ The min z of the box
     * @param maxX The max x of the box
     * @param maxY The max y of the box
     * @param maxZ The max z of the box
     */
    private static void addChainedFilledBoxVertices(double minX, double minY, double minZ,
                                                   double maxX, double maxY, double maxZ,
                                                   float red, float green, float blue, float alpha) {

        // min, min, min, max, max, max -> max, max, max, min, min, min
        buffer.pos(minX, minY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, minY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, minY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, minY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, maxY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, maxY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, maxY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, minY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, maxY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, minY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, minY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, minY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, maxY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, maxY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, maxY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, minY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, maxY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, minY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, minY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, minY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, minY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, minY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, minY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, maxY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, maxY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, maxY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, maxY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, maxY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, maxY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, maxY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
    }
    
    /**
     * Add outlined box vertices to the buffer
     *
     * @see net.minecraft.client.renderer.RenderGlobal
     * @param minX The min x of the box
     * @param minY The min y of the box
     * @param minZ The min z of the box
     * @param maxX The max x of the box
     * @param maxY The max y of the box
     * @param maxZ The max z of the box
     */
    private static void addChainedBoundingBoxVertices(double minX, double minY, double minZ,
                                                     double maxX, double maxY, double maxZ,
                                                     float red, float green, float blue, float alpha) {

        // min, min, min, max, max, max -> max, max, max, min, min, min (alpha 0.0f for inner)
        buffer.pos(minX, minY, minZ)
                .color(red, green, blue, 0.0f)
                .endVertex();
        buffer.pos(minX, minY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, minY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, minY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, minY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, minY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, maxY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, maxY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, maxY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, maxY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, maxY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(minX, maxY, maxZ)
                .color(red, green, blue, 0.0f)
                .endVertex();
        buffer.pos(minX, minY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, maxY, maxZ)
                .color(red, green, blue, 0.0f)
                .endVertex();
        buffer.pos(maxX, minY, maxZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, maxY, minZ)
                .color(red, green, blue, 0.0f)
                .endVertex();
        buffer.pos(maxX, minY, minZ)
                .color(red, green, blue, alpha)
                .endVertex();
        buffer.pos(maxX, minY, minZ)
                .color(red, green, blue, 0.0f)
                .endVertex();
    }
}
