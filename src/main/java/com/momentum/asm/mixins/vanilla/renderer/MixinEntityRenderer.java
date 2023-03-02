package com.momentum.asm.mixins.vanilla.renderer;

import com.momentum.Momentum;
import com.momentum.api.util.Wrapper;
import com.momentum.impl.events.vanilla.renderer.RenderBlindnessEvent;
import com.momentum.impl.events.vanilla.renderer.RenderHurtCameraEvent;
import com.momentum.impl.events.vanilla.renderer.RenderItemActivationEvent;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.FogMode;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GLContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer implements Wrapper {

    // the activation item
    @Shadow
    private ItemStack itemActivationItem;

    // cloud fog state
    @Shadow
    private boolean cloudFog;

    /**
     * Called when the hurt camera effect is rendered
     */
    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void onHurtCameraEffect(float ticks, CallbackInfo ci) {

        // post the hurt camera event
        RenderHurtCameraEvent renderHurtCameraEvent = new RenderHurtCameraEvent();
        Momentum.EVENT_BUS.dispatch(renderHurtCameraEvent);

        // remove hurt camera effect
        if (renderHurtCameraEvent.isCanceled()) {
            ci.cancel();
        }
    }

    /**
     * Called when an item activation animation is rendered
     */
    @Inject(method = "renderItemActivation", at = @At(value = "HEAD"), cancellable = true)
    private void onRenderItemActivation(CallbackInfo ci) {

        // post render item activation event
        RenderItemActivationEvent renderItemActivationEvent = new RenderItemActivationEvent(itemActivationItem);
        Momentum.EVENT_BUS.dispatch(renderItemActivationEvent);

        // if event is cancelled, cancel the anim
        if (renderItemActivationEvent.isCanceled()) {
            ci.cancel();
        }
    }

    /**
     * Called when the entity renderer sets up fog rendering
     * Overwrite method, using Inject for better compat
     */
    @Inject(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ActiveRenderInfo;getBlockStateAtEntityViewpoint(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;F)Lnet/minecraft/block/state/IBlockState;", shift = Shift.AFTER, ordinal = 0), cancellable = true)
    private void onSetupFog(int startCoords, float partialTicks, CallbackInfo ci) {

        // view entity
        Entity entity = mc.getRenderViewEntity();

        // farthest plane distance in blocks
        float farPlaneDistance = mc.gameSettings.renderDistanceChunks * 16;

        // block state at entity view point
        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(mc.world, entity, partialTicks);

        // density
        float density = ForgeHooksClient.getFogDensity(mc.entityRenderer, entity, iblockstate, partialTicks, 0.1f);
        if (density >= 0) {
            GlStateManager.setFogDensity(density);
        }

        // run fog
        else

        // blindness
        if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(MobEffects.BLINDNESS)) {

            // post the render blindness event
            RenderBlindnessEvent renderBlindnessEvent = new RenderBlindnessEvent();
            Momentum.EVENT_BUS.dispatch(renderBlindnessEvent);

            // if canceled, don't render blindness fog
            if (!renderBlindnessEvent.isCanceled()) {

                // fog factor
                float factor = 5.0f;

                // potion remaining duration
                int duration = ((EntityLivingBase) entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();

                // linear close in
                if (duration < 20) {

                    // factor lessens
                    factor = 5.0f + (farPlaneDistance - 5.0f) * (1.0f - (float) duration / 20.0f);
                }

                // set fog mode
                GlStateManager.setFog(FogMode.LINEAR);

                // set fog start and end
                if (startCoords == -1) {
                    GlStateManager.setFogStart(0.0f);
                    GlStateManager.setFogEnd(factor * 0.8f);
                }

                else {
                    GlStateManager.setFogStart(factor * 0.25f);
                    GlStateManager.setFogEnd(factor);
                }

                if (GLContext.getCapabilities().GL_NV_fog_distance) {
                    GlStateManager.glFogi(34138, 34139);
                }
            }
        }

        // cloud fog
        else if (cloudFog) {

            // set fog mode and density
            GlStateManager.setFog(FogMode.EXP);
            GlStateManager.setFogDensity(0.1f);
        }

        // water fog
        else if (iblockstate.getMaterial() == Material.WATER) {

            // set fog mode
            GlStateManager.setFog(FogMode.EXP);

            // check water breathing
            if (entity instanceof EntityLivingBase) {

                // can breathe in water
                if (((EntityLivingBase) entity).isPotionActive(MobEffects.WATER_BREATHING)) {

                    // no density
                    GlStateManager.setFogDensity(0.01f);
                }

                else {

                    // apply respiration modifier
                    float modifier = EnchantmentHelper.getRespirationModifier((EntityLivingBase) entity) * 0.03f;
                    GlStateManager.setFogDensity(0.1f - modifier);
                }
            }

            // normal fog density
            else {
                GlStateManager.setFogDensity(0.1f);
            }
        }

        // lava fog
        else if (iblockstate.getMaterial() == Material.LAVA) {

            // set mode and density
            GlStateManager.setFog(FogMode.EXP);
            GlStateManager.setFogDensity(2.0f); // 2 because lava fog visibility is less
        }

        // normal fog render
        else {

            // set mode
            GlStateManager.setFog(FogMode.LINEAR);

            // set fog start and end
            if (startCoords == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(farPlaneDistance);
            }

            else {
                GlStateManager.setFogStart(farPlaneDistance * 0.75f);
                GlStateManager.setFogEnd(farPlaneDistance);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GlStateManager.glFogi(34138, 34139);
            }

            // check if should show fog
            if (mc.world.provider.doesXZShowFog((int) entity.posX, (int) entity.posZ) || mc.ingameGUI.getBossOverlay().shouldCreateFog()) {

                // set fog start and end
                GlStateManager.setFogStart(farPlaneDistance * 0.05F);
                GlStateManager.setFogEnd(Math.min(farPlaneDistance, 192.0F) * 0.5F);
            }

            // call event
            ForgeHooksClient.onFogRender(mc.entityRenderer, entity, iblockstate, partialTicks, startCoords, farPlaneDistance);
        }

        // render fog
        GlStateManager.enableColorMaterial();
        GlStateManager.enableFog();
        GlStateManager.colorMaterial(1028, 4608);
    }
}
