package com.momentum.asm.mixins.forge;

import com.momentum.Momentum;
import com.momentum.impl.events.forge.LeftClickBlockEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeHooks.class)
public class MixinForgeHooks {

    /**
     * Called when a player left clicks while targeting a block
     *
     * @see net.minecraft.block.Block
     */
    @Inject(method = "onLeftClickBlock", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void onOnLeftClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face, Vec3d hitVec, CallbackInfoReturnable<PlayerInteractEvent.LeftClickBlock> cir) {

        // post the left click block event
        LeftClickBlockEvent leftClickBlockEvent = new LeftClickBlockEvent(pos, face);
        Momentum.EVENT_BUS.dispatch(leftClickBlockEvent);

        // cancel forge event
        if (leftClickBlockEvent.isCanceled()) {
            cir.cancel();
            cir.setReturnValue(null);
        }
    }
}
