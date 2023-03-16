package com.momentum.impl.modules.render.chams;

import com.momentum.api.event.EventStage;
import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.renderer.entity.RenderEnderCrystalEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author linus
 * @since 03/13/2023
 */
public class RenderEnderCrystalListener extends FeatureListener<ChamsModule, RenderEnderCrystalEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderEnderCrystalListener(ChamsModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderEnderCrystalEvent event) {

        // apply chams to ender crystals
        if (feature.othersOption.getVal()) {

            // pre-render start
            if (event.getStage() == EventStage.PRE) {

                // custom model rendering
                event.setCanceled(true);
            }

            // post render start
            if (event.getStage() == EventStage.POST) {

                // LAYER 0
                // render entity texture
                if (feature.textureOption.getVal()) {

                    // apply transparent model
                    if (feature.modeOption.getVal() != ChamsMode.WIREFRAME) {

                        // GlStateManager.TRANSPARENT_MODEL apply
                        glPushMatrix();
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.15f);
                        GlStateManager.depthMask(false);
                        GlStateManager.enableBlend();
                        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                        GlStateManager.alphaFunc(GL_GREATER, 0.003921569f);
                    }

                    // render original model
                    event.getModelBase().render(
                            event.getEntityEnderCrystal(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScaleFactor());

                    // apply transparent model
                    if (feature.modeOption.getVal() != ChamsMode.WIREFRAME) {

                        // GlStateManager.TRANSPARENT_MODEL clean
                        GlStateManager.disableBlend();
                        GlStateManager.alphaFunc(GL_GREATER, 0.1f);
                        GlStateManager.depthMask(true);
                        glPopMatrix();
                    }
                }

                // start render
                glPushMatrix();
                glPushAttrib(GL_ALL_ATTRIB_BITS);

                // remove depth
                // model is now visible through walls
                glDisable(GL_DEPTH_TEST);

                // remove model texture
                glDisable(GL_TEXTURE_2D);
                glEnable(GL_BLEND);
                glDisable(GL_LIGHTING);

                // specify polygon mode, this changes how polygons are rendered in the model
                glPolygonMode(GL_FRONT_AND_BACK,
                        feature.modeOption.getVal() == ChamsMode.WIREFRAME ? GL_LINE : GL_FILL);

                // smooth lines
                glEnable(GL_LINE_SMOOTH);
                glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
                glLineWidth(feature.lineWidthOption.getVal());

                // color
                Color c = feature.colorOption.getVal();
                glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f,
                        feature.modeOption.getVal() == ChamsMode.WIREFRAME ? 1.0f : c.getAlpha() / 255.0f);

                // LAYER 1
                // render custom model
                event.getModelBase().render(
                        event.getEntityEnderCrystal(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScaleFactor());

                // render two models on one layer
                if (feature.modeOption.getVal() == ChamsMode.BOTH) {

                    // apply line polygon mode
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

                    // color
                    glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 1.0f);

                    // render custom model
                    event.getModelBase().render(
                            event.getEntityEnderCrystal(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScaleFactor());

                    // reset polygon mode
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                }

                // LAYER 2
                // render custom model with depth
                if (feature.xqzOption.getVal()) {

                    // apply depth
                    glEnable(GL_DEPTH_TEST);

                    // color
                    Color xqzC = feature.xqzColorOption.getVal();
                    glColor4f(xqzC.getRed() / 255.0f, xqzC.getGreen() / 255.0f, xqzC.getBlue() / 255.0f, xqzC.getAlpha() / 255.0f);

                    // render custom model
                    event.getModelBase().render(
                            event.getEntityEnderCrystal(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScaleFactor());

                    // render two models on one layer
                    if (feature.modeOption.getVal() == ChamsMode.BOTH) {

                        // apply line polygon mode
                        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

                        // color
                        glColor4f(xqzC.getRed() / 255.0f, xqzC.getGreen() / 255.0f, xqzC.getBlue() / 255.0f, 1.0f);

                        // render custom model
                        event.getModelBase().render(
                                event.getEntityEnderCrystal(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScaleFactor());

                        // reset polygon mode
                        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                    }
                }

                // LAYER 3
                // render custom model with enchantment shine
                if (feature.shineOption.getVal()) {

                    // need to re-enable textures
                    glEnable(GL_TEXTURE_2D);
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                    // glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

                    // apply blend function
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);

                    // render shine
                    // bind the enchantment glint texture
                    mc.getRenderManager().renderEngine
                            .bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));

                    // begin the texture matrix
                    GlStateManager.matrixMode(GL_TEXTURE);
                    GlStateManager.loadIdentity();

                    // apply scaling and rotations to the texture
                    GlStateManager.scale(0.33333334f, 0.33333334f, 0.33333334f);
                    GlStateManager.rotate(30.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.translate(0.0f, (event.getEntityEnderCrystal().ticksExisted + mc.getRenderPartialTicks()) * 0.004f * 4.0f, 0.0f);
                    GlStateManager.matrixMode(GL_MODELVIEW);
                    glTranslatef(0.0f, 0.0f, 0.0f);

                    // render the model
                    event.getModelBase().render(
                            event.getEntityEnderCrystal(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScaleFactor());

                    // load the matrix
                    GlStateManager.matrixMode(GL_TEXTURE);
                    GlStateManager.loadIdentity();
                    GlStateManager.matrixMode(GL_MODELVIEW);

                    // clean
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    glDisable(GL_TEXTURE_2D);
                }

                // stop render
                glEnable(GL_LIGHTING);
                glDisable(GL_BLEND);
                glEnable(GL_TEXTURE_2D);
                glPopAttrib();
                glPopMatrix();
            }
        }
    }
}
