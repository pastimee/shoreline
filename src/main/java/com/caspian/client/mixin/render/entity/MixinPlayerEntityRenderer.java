package com.caspian.client.mixin.render.entity;

import com.caspian.client.Caspian;
import com.caspian.client.impl.event.render.RenderPlayerEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 *
 *
 * @author linus
 * @since 1.0
 *
 * @see PlayerEntityRenderer
 */
@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer
{
    //
    private float yaw, prevYaw, bodyYaw, prevBodyYaw, headYaw, prevHeadYaw;
    private float pitch, prevPitch;

    /**
     *
     *
     * @param abstractClientPlayerEntity
     * @param f
     * @param g
     * @param matrixStack
     * @param vertexConsumerProvider
     * @param i
     * @param ci
     */
    @Inject(method = "render(Lnet/minecraft/client/network/" +
            "AbstractClientPlayerEntity;FFLnet/minecraft/client/util/" +
            "math/MatrixStack;Lnet/minecraft/client/render " +
            "/VertexConsumerProvider;I)V", at = @At(value = "HEAD"),
            cancellable = true)
    private void onRenderHead(AbstractClientPlayerEntity abstractClientPlayerEntity,
                              float f, float g, MatrixStack matrixStack,
                              VertexConsumerProvider vertexConsumerProvider,
                              int i, CallbackInfo ci)
    {
        final RenderPlayerEvent renderPlayerEvent =
                new RenderPlayerEvent(abstractClientPlayerEntity);
        Caspian.EVENT_HANDLER.dispatch(renderPlayerEvent);
        yaw = abstractClientPlayerEntity.getYaw();
        prevYaw = abstractClientPlayerEntity.prevYaw;
        bodyYaw = abstractClientPlayerEntity.bodyYaw;
        prevBodyYaw = abstractClientPlayerEntity.prevBodyYaw;
        headYaw = abstractClientPlayerEntity.headYaw;
        prevHeadYaw = abstractClientPlayerEntity.prevHeadYaw;
        pitch = abstractClientPlayerEntity.getPitch();
        prevPitch = abstractClientPlayerEntity.prevPitch;
        if (renderPlayerEvent.isCanceled())
        {
            abstractClientPlayerEntity.setYaw(renderPlayerEvent.getYaw());
            abstractClientPlayerEntity.prevYaw = renderPlayerEvent.getYaw();
            abstractClientPlayerEntity.setBodyYaw(renderPlayerEvent.getYaw());
            abstractClientPlayerEntity.prevBodyYaw = renderPlayerEvent.getYaw();
            abstractClientPlayerEntity.setHeadYaw(renderPlayerEvent.getYaw());
            abstractClientPlayerEntity.prevHeadYaw = renderPlayerEvent.getYaw();
            abstractClientPlayerEntity.setPitch(renderPlayerEvent.getPitch());
            abstractClientPlayerEntity.prevPitch = renderPlayerEvent.getPitch();
        }
    }

    /**
     *
     *
     * @param abstractClientPlayerEntity
     * @param f
     * @param g
     * @param matrixStack
     * @param vertexConsumerProvider
     * @param i
     * @param ci
     */
    @Inject(method = "render(Lnet/minecraft/client/network/" +
            "AbstractClientPlayerEntity;FFLnet/minecraft/client/util/" +
            "math/MatrixStack;Lnet/minecraft/client/render " +
            "/VertexConsumerProvider;I)V", at = @At(value = "TAIL"),
            cancellable = true)
    private void onRenderTail(AbstractClientPlayerEntity abstractClientPlayerEntity,
                              float f, float g, MatrixStack matrixStack,
                              VertexConsumerProvider vertexConsumerProvider,
                              int i, CallbackInfo ci)
    {
        abstractClientPlayerEntity.setYaw(yaw);
        abstractClientPlayerEntity.prevYaw = prevYaw;
        abstractClientPlayerEntity.setBodyYaw(bodyYaw);
        abstractClientPlayerEntity.prevBodyYaw = prevBodyYaw;
        abstractClientPlayerEntity.setHeadYaw(headYaw);
        abstractClientPlayerEntity.prevHeadYaw = prevHeadYaw;
        abstractClientPlayerEntity.setPitch(pitch);
        abstractClientPlayerEntity.prevPitch = prevPitch;
    }
}
