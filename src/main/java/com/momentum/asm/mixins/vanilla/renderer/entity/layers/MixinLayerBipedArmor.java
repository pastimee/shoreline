package com.momentum.asm.mixins.vanilla.renderer.entity.layers;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.renderer.entity.layers.RenderArmorModelEvent;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerBipedArmor.class)
public class MixinLayerBipedArmor {

    /**
     * Called when the armor model visibility is set
     */
    @Inject(method = "setModelSlotVisible", at = @At(value = "HEAD"), cancellable = true)
    private void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slotIn, CallbackInfo ci) {

        // post the armor model event
        RenderArmorModelEvent renderArmorModelEvent = new RenderArmorModelEvent();
        Momentum.EVENT_BUS.dispatch(renderArmorModelEvent);

        // prevent armor rendering
        if (renderArmorModelEvent.isCanceled()) {

            // set visibility to false
            ci.cancel();
            model.bipedHead.showModel = false;
            model.bipedHeadwear.showModel = false;
            model.bipedBody.showModel = false;
            model.bipedLeftArm.showModel = false;
            model.bipedRightArm.showModel = false;
            model.bipedLeftLeg.showModel = false;
            model.bipedRightLeg.showModel = false;
        }
    }
}
