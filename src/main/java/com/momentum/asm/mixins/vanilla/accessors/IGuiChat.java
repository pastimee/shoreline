package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Gives access to the {@link GuiChat} private fields
 */
@Mixin(GuiChat.class)
public interface IGuiChat {

    @Accessor("inputField")
    GuiTextField getInputField();
}
