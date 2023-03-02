package com.momentum.asm.mixins.vanilla;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.KeyInputEvent;
import com.momentum.impl.events.vanilla.TickEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    /**
     * Dispatched when the game runs the tick keyboard loop
     */
    @Inject(method = "runTickKeyboard", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;fireKeyInput()V", shift = Shift.BEFORE))
    private void onRunTickKeyboard(CallbackInfo ci) {

        // post the key input event
        KeyInputEvent keyInputEvent = new KeyInputEvent();
        Momentum.EVENT_BUS.dispatch(keyInputEvent);
    }

    /**
     * Dispatched when the game runs the tick loop
     */
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", shift = Shift.BEFORE))
    private void onRunTick(CallbackInfo ci) {

        // post the tick event
        TickEvent tickEvent = new TickEvent();
        Momentum.EVENT_BUS.dispatch(tickEvent);
    }
}
