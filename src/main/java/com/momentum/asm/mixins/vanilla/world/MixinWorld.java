package com.momentum.asm.mixins.vanilla.world;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.world.EntityRemoveEvent;
import com.momentum.impl.events.vanilla.world.EntitySpawnEvent;
import com.momentum.impl.events.vanilla.world.RenderSkyColorEvent;
import com.momentum.impl.events.vanilla.world.RenderSkyLightEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(World.class)
public class MixinWorld {

    /**
     * Called when sky light is rendered
     */
    @Inject(method = "checkLightFor", at = @At(value = "HEAD"), cancellable = true)
    private void onCheckLightFor(EnumSkyBlock lightType, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {

        // sky light
        if (lightType == EnumSkyBlock.SKY) {

            // post the render sky light event
            RenderSkyLightEvent renderSkyLightEvent = new RenderSkyLightEvent();
            Momentum.EVENT_BUS.dispatch(renderSkyLightEvent);

            // cancel sky light rendering
            if (renderSkyLightEvent.isCanceled()) {
                cir.cancel();
                cir.setReturnValue(true);
            }
        }
    }

    /**
     * Called when an entity is spawned in the world
     */
    @Inject(method = "spawnEntity", at = @At(value = "HEAD"), cancellable = true)
    private void onSpawnEntity(Entity entityIn, CallbackInfoReturnable<Boolean> cir) {

        // post the entity spawn event
        EntitySpawnEvent entitySpawnEvent = new EntitySpawnEvent(entityIn);
        Momentum.EVENT_BUS.dispatch(entitySpawnEvent);

        // cancel entity spawn
        if (entitySpawnEvent.isCanceled()) {

            // prevent entity from spawning
            cir.cancel();
            cir.setReturnValue(false);
        }
    }

    /**
     * Called when an entity is removed from the world
     */
    @Inject(method = "removeEntity", at = @At(value = "HEAD"))
    private void onRemoveEntity(Entity entityIn, CallbackInfo ci) {

        // post the entity remove event
        EntityRemoveEvent entityRemoveEvent = new EntityRemoveEvent(entityIn);
        Momentum.EVENT_BUS.dispatch(entityRemoveEvent);
    }

    /**
     * Called when the sky color is rendered
     */
    @Inject(method = "getSkyColor", at = @At(value = "HEAD"), cancellable = true)
    private void onGetSkyColor(Entity entityIn, float partialTicks, CallbackInfoReturnable<Vec3d> cir) {

        // post the render sky color event
        RenderSkyColorEvent renderSkyColorEvent = new RenderSkyColorEvent();
        Momentum.EVENT_BUS.dispatch(renderSkyColorEvent);

        // override color
        if (renderSkyColorEvent.isCanceled()) {

            // replace color
            Color color = new Color(renderSkyColorEvent.getColor());;
            cir.cancel();
            cir.setReturnValue(new Vec3d(color.getRed(), color.getGreen(), color.getBlue()));
        }
    }
}
