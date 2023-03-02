package com.momentum.asm.mixins.vanilla.block;

import com.momentum.Momentum;
import com.momentum.api.util.Wrapper;
import com.momentum.impl.events.vanilla.block.SlimeEvent;
import net.minecraft.block.BlockSlime;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSlime.class)
public class MixinBlockSlime implements Wrapper {

    /**
     * Called when entity walks on slime
     */
    @Inject(method = "onEntityWalk", at = @At("HEAD"), cancellable = true)
    private void onOnEntityWalk(World world, BlockPos blockPos, Entity entity, CallbackInfo ci) {

        if (entity == mc.player) {

            // post slime event
            SlimeEvent slimeEvent = new SlimeEvent();
            Momentum.EVENT_BUS.dispatch(slimeEvent);

            // prevent player from being slowed down
            if (slimeEvent.isCanceled()) {
                ci.cancel();
            }
        }
    }
}
