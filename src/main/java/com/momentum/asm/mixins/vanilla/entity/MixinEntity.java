package com.momentum.asm.mixins.vanilla.entity;

import com.momentum.Momentum;
import com.momentum.api.util.Wrapper;
import com.momentum.impl.events.vanilla.entity.StepEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity implements Wrapper {

    // entity bounding box
    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();

    // entity step height
    @Shadow
    public float stepHeight;

    /**
     * Called after the move function sets the player bounding box
     *
     * @author auto
     */
    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", shift = Shift.BEFORE, ordinal = 0))
    private void onMove(MoverType type, double x, double y, double z, CallbackInfo ci) {

        // check if this entity is the player
        if (((Entity) (Object) this).equals(mc.player)) {

            // post the step event
            StepEvent stepEvent = new StepEvent(getEntityBoundingBox(), stepHeight);
            Momentum.EVENT_BUS.dispatch(stepEvent);

            // if step event is canceled
            if (stepEvent.isCanceled()) {

                // update step height
                stepHeight = stepEvent.getHeight();
            }
        }
    }
}
