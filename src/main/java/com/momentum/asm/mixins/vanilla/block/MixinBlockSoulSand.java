package com.momentum.asm.mixins.vanilla.block;

import com.momentum.Momentum;
import com.momentum.api.util.Wrapper;
import com.momentum.impl.events.vanilla.block.SoulSandEvent;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSoulSand.class)
public class MixinBlockSoulSand implements Wrapper {

    /**
     * Called when player collides with soul sand
     */
    @Inject(method = "onEntityCollidedWithBlock", at = @At("HEAD"), cancellable = true)
    private void onOnEntityCollidedWithBlock(World world, BlockPos blockPos, IBlockState iBlockState, Entity entity, CallbackInfo ci) {

        // check if player is colliding
        if (entity == mc.player) {

            // post soul sand event
            SoulSandEvent soulSandEvent = new SoulSandEvent();
            Momentum.EVENT_BUS.dispatch(soulSandEvent);

            // prevent player from being slowed down
            if (soulSandEvent.isCanceled()) {
                ci.cancel();
            }
        }
    }
}
