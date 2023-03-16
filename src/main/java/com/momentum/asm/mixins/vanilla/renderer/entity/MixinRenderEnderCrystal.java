package com.momentum.asm.mixins.vanilla.renderer.entity;

import com.momentum.Momentum;
import com.momentum.api.event.EventStage;
import com.momentum.api.util.Wrapper;
import com.momentum.impl.events.vanilla.renderer.entity.RenderEnderCrystalEvent;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderEnderCrystal.class)
public class MixinRenderEnderCrystal implements Wrapper {

    // with base
    @Final
    @Shadow
    private ModelBase modelEnderCrystal;

    // without base
    @Final
    @Shadow
    private ModelBase modelEnderCrystalNoBase;

    /**
     * Called when an ender crystal is rendered
     */
    @Inject(method = "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V", at = @At(value = "HEAD"), cancellable = true)
    private void onDoRender(EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {

        // render values
        float f = entity.innerRotation + partialTicks;
        float f1 = MathHelper.sin(f * 0.2f) / 2.0f + 0.5f;
        f1 = f1 * f1 + f1;

        // post the render ender crystal event
        // pre
        RenderEnderCrystalEvent renderEnderCrystalEvent = new RenderEnderCrystalEvent(
                entity.shouldShowBottom() ? modelEnderCrystal : modelEnderCrystalNoBase, entity, 0.0f, f * 3.0f, f1 * 0.2f, 0.0f, 0.0f, 0.0625f);
        renderEnderCrystalEvent.setStage(EventStage.PRE);
        Momentum.EVENT_BUS.dispatch(renderEnderCrystalEvent);

        // custom model rendering
        if (renderEnderCrystalEvent.isCanceled()) {

            // override
            ci.cancel();

            // start render
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            mc.getRenderManager().renderEngine
                    .bindTexture(new ResourceLocation("textures/entity/endercrystal/endercrystal.png"));

            // post
            renderEnderCrystalEvent.setStage(EventStage.POST);
            Momentum.EVENT_BUS.dispatch(renderEnderCrystalEvent);

            // end render
            GlStateManager.popMatrix();

            // render beam
            BlockPos blockpos = entity.getBeamTarget();

            // beam exists
            if (blockpos != null) {

                // bind texture
                mc.getRenderManager().renderEngine
                        .bindTexture(new ResourceLocation("textures/entity/endercrystal/endercrystal.png"));

                // beam render
                float f2 = blockpos.getX() + 0.5f;
                float f3 = blockpos.getY() + 0.5f;
                float f4 = blockpos.getZ() + 0.5f;
                double d0 = f2 - entity.posX;
                double d1 = f3 - entity.posY;
                double d2 = f4 - entity.posZ;
                RenderDragon.renderCrystalBeams(
                        x + d0, y - 0.3d + (double) (f1 * 0.4f) + d1, z + d2, partialTicks, f2, f3, f4, entity.innerRotation, entity.posX, entity.posY, entity.posZ);
            }

            // super.doRender
            renderName(entity, x, y, z);
        }
    }

    /**
     * Renders the crystal entity name
     *
     * @param entity The entity name
     * @param x The x pos
     * @param y The y pos
     * @param z The z pos
     */
    protected void renderName(EntityEnderCrystal entity, double x, double y, double z) {

        // check if can render name
        if (canRenderName(entity)) {
            this.renderLivingLabel(entity, entity.getDisplayName().getFormattedText(), x, y, z, 64);
        }
    }

    /**
     * Checks if a crystal entity needs render name
     *
     * @param entity The entity
     */
    protected boolean canRenderName(EntityEnderCrystal entity) {
        return entity.getAlwaysRenderNameTagForRender()
                && entity.hasCustomName();
    }

    /**
     * Renders a label on a crystal entity
     *
     * @param entityIn The entity
     * @param str The label
     * @param x The x pos
     * @param y The y pos
     * @param z The z pos
     * @param maxDistance The maximum render distance
     */
    protected void renderLivingLabel(EntityEnderCrystal entityIn, String str, double x, double y, double z, int maxDistance) {

        // check render null
        if (mc.getRenderViewEntity() != null) {

            // dist to label
            double d0 = entityIn.getDistanceSq(mc.getRenderViewEntity());

            // check max dist
            if (d0 <= maxDistance * maxDistance) {

                // render label
                boolean flag = entityIn.isSneaking();
                float f = mc.getRenderManager().playerViewY;
                float f1 = mc.getRenderManager().playerViewX;
                boolean flag1 = mc.getRenderManager().options.thirdPersonView == 2;
                float f2 = entityIn.height + 0.5f - (flag ? 0.25f : 0.0f);
                int i = "deadmau5".equals(str) ? -10 : 0;
                EntityRenderer.drawNameplate(mc.fontRenderer, str, (float) x, (float) y + f2, (float) z, i, f, f1, flag1, flag);
            }
        }
    }
}
