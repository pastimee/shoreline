package com.momentum.asm.mixins.vanilla.settings;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.settings.KeyDownEvent;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public class MixinKeyBinding {

    // keybinding pressed state
    @Shadow
    private boolean pressed;

    /**
     * Called when pressed is checked
     */
    @Inject(method = "isKeyDown", at = @At("HEAD"), cancellable = true)
    public void onIsKeyDown(CallbackInfoReturnable<Boolean> cir) {

        // post key down event
        KeyDownEvent keyDownEvent = new KeyDownEvent();
        Momentum.EVENT_BUS.dispatch(keyDownEvent);

        // prevent key context
        if (keyDownEvent.isCanceled()) {
            cir.cancel();
            cir.setReturnValue(pressed);
        }
    }
}
