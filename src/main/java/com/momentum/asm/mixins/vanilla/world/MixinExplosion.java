package com.momentum.asm.mixins.vanilla.world;

import com.momentum.Momentum;
import com.momentum.api.util.Wrapper;
import com.momentum.api.util.block.ResistantBlocks;
import com.momentum.impl.events.vanilla.world.RenderExplosionEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(Explosion.class)
public class MixinExplosion implements Wrapper {

    // Explosion instance vars
    @Shadow
    @Final
    private boolean damagesTerrain;

    @Shadow
    @Final
    private List<BlockPos> affectedBlockPositions;

    @Shadow
    @Final
    private float size;

    @Shadow
    @Final
    private boolean causesFire;

    @Shadow
    @Final
    private Random random;

    /**
     * Called at the second part of the explosion (sound, particles, drop spawn)
     * Overwrite method, using Inject for better compat
     */
    @Inject(method = "doExplosionB", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/EntityPlayer;DDDLnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FF)V", shift = Shift.AFTER), cancellable = true)
    private void onDoExplosionB(boolean spawnParticles, CallbackInfo ci) {

        // post the explosion event
        RenderExplosionEvent renderExplosionEvent = new RenderExplosionEvent();
        Momentum.EVENT_BUS.dispatch(renderExplosionEvent);

        // prevent particles
        if (renderExplosionEvent.isCanceled()) {

            // cancel
            ci.cancel();

            // damage terrain
            if (damagesTerrain) {

                // explosion affected blocks
                for (BlockPos blockpos : affectedBlockPositions) {

                    // block info
                    IBlockState iblockstate = mc.world.getBlockState(blockpos);
                    Block block = iblockstate.getBlock();

                    // check block material
                    if (iblockstate.getMaterial() != Material.AIR) {

                        // this method always returns true?
                        if (!ResistantBlocks.isBlastResistant(blockpos)) {

                            // drop block
                            block.dropBlockAsItemWithChance(mc.world, blockpos, mc.world.getBlockState(blockpos), 1.0f / size, 0);
                        }

                        // callback
                        mc.world.setBlockToAir(blockpos);
                        // block.onBlockExploded(mc.world, blockpos, this);
                    }
                }
            }

            // cause fire
            if (causesFire) {

                // explosion affected blocks
                for (BlockPos blockpos1 : affectedBlockPositions) {

                    // can set on fire
                    if (mc.world.getBlockState(blockpos1).getMaterial() == Material.AIR && mc.world.getBlockState(blockpos1.down()).isFullBlock() && random.nextInt(3) == 0) {

                        // set on fire
                        mc.world.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
                    }
                }
            }
        }
    }
}
