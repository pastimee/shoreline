package com.momentum.asm.mixins.vanilla.gui;

import com.momentum.Momentum;
import com.momentum.api.util.Wrapper;
import com.momentum.impl.events.vanilla.gui.RenderChatBoxEvent;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiTextField.class)
public class MixinGuiTextField implements Wrapper {

    // gui fields
    @Shadow
    public int x;

    @Shadow
    public int y;

    @Shadow
    public int height;

    @Shadow
    private boolean enableBackgroundDrawing;

    /**
     * Called when the chat text box is rendered
     */
    @Inject(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiTextField;getEnableBackgroundDrawing()Z", shift = Shift.BEFORE))
    private void onDrawTextBox(CallbackInfo ci) {

        // post the render chat box event
        RenderChatBoxEvent renderChatBoxEvent = new RenderChatBoxEvent();
        Momentum.EVENT_BUS.dispatch(renderChatBoxEvent);

        // draw text
        if (renderChatBoxEvent.isCanceled()) {

            // render string
            mc.fontRenderer.drawStringWithShadow(renderChatBoxEvent.getText(), x,
                    enableBackgroundDrawing ? y + (height - 8) / 2.0f : y, 0xff808080);
        }
    }
}
