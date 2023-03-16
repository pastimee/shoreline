package com.momentum.asm.mixins.vanilla.accessors;

import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Gives access to the {@link GuiEditSign} private fields
 */
@Mixin(GuiEditSign.class)
public interface IGuiEditSign {

    @Accessor("tileSign")
    TileEntitySign getTileSign();
}
