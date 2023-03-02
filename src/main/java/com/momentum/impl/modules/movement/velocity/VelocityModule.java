package com.momentum.impl.modules.movement.velocity;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linus
 * @since 01/09/2023
 */
public class VelocityModule extends Module {

    // velocity multipliers
    public final Option<Integer> horizontalOption =
            new Option<>("Horizontal", new String[] {"H"}, "Horizontal velocity multiplier", 0, 0, 100);
    public final Option<Integer> verticalOption =
            new Option<>("Vertical", new String[] {"V"}, "Vertical velocity multiplier", 0, 0, 100);

    // no-push options
    public final Option<Boolean> entitiesOption =
            new Option<>("Entities", new String[] {"NoPushEntities"}, "Prevents being pushed by entities", true);
    public final Option<Boolean> blocksOption =
            new Option<>("Blocks", new String[] {"NoPushBlocks"}, "Prevents being pushed out of blocks", true);
    public final Option<Boolean> liquidsOption =
            new Option<>("Liquids", new String[] {"NoPushLiquids"}, "Prevents being pushed by liquids", true);
    public final Option<Boolean> fishHookOption =
            new Option<>("Fishhooks", new String[] {"Bobbers"}, "Prevents being pulled by fishhooks", true);

    // listeners
    public final EntityCollisionListener entityCollisionListener
            = new EntityCollisionListener(this);
    public final InboundPacketListener inboundPacketListener
            = new InboundPacketListener(this);
    public final PushedByWaterListener pushedByWaterListener
            = new PushedByWaterListener(this);
    public final PushOutOfBlocksListener pushOutOfBlocksListener
            = new PushOutOfBlocksListener(this);

    public VelocityModule() {
        super("Velocity", new String[] {"AntiKnockback", "AntiKB"}, "Modifies player velocity", ModuleCategory.MOVEMENT);

        // options
        associate(
                horizontalOption,
                verticalOption,
                entitiesOption,
                blocksOption,
                liquidsOption,
                fishHookOption,
                bind,
                drawn
        );

        // listeners
        associate(
                entityCollisionListener,
                inboundPacketListener,
                pushedByWaterListener,
                pushOutOfBlocksListener
        );
    }

    @Override
    public String getData() {

        // module data
        return "H" + horizontalOption.getVal().floatValue() + "%" + TextFormatting.GRAY + "|" + TextFormatting.WHITE + "V" + verticalOption.getVal().floatValue() + "%";
    }
}
