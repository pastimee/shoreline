package com.momentum.impl.modules.movement.noslow;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import com.momentum.impl.init.Modules;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.client.settings.KeyConflictContext;

/**
 * @author linus
 * @since 02/13/2023
 */
public class NoSlowModule extends Module {

    // anticheat options
    public final Option<Boolean> strictOption =
            new Option<>("Strict", new String[] {"NCPStrict"}, "Bypass NCP-Updated servers", false);
    public final Option<Boolean> airStrictOption =
            new Option<>("AirStrict", "Bypass NCP-Updated servers while moving in air", false);

    // inventory move options
    public final Option<Boolean> inventoryMoveOption =
            new Option<>("InventoryMove", "Allows player to move in inventories", true);
    public final Option<Boolean> arrowMoveOption =
            new Option<>("ArrowMove", "Allows player to move camera with arrow keys in inventories", false);

    // slowdown options
    public final Option<Boolean> itemsOption =
            new Option<>("Items", "Removes item use slowdowns", true);
    public final Option<Boolean> websOption =
            new Option<>("Webs", "Removes web slowdown", true);
    public final Option<Float> webSpeedOption =
            new Option<>("WebSpeed", "Web move speed", 0.0f, 3.5f, 20.0f);
    public final Option<Boolean> soulSandOption =
            new Option<>("SoulSand", "Removes soul sand slowdown", true);
    public final Option<Boolean> slimeOption =
            new Option<>("Slime", "Removes slime slowdown", true);
    public final Option<Boolean> iceOption =
            new Option<>("Ice", "Removes ice slipperiness slowdown", true);

    // listeners
    OutboundPacketListener outboundPacketListener =
            new OutboundPacketListener(this);
    UpdateListener updateListener =
            new UpdateListener(this);
    InputUpdateListener inputUpdateListener =
            new InputUpdateListener(this);
    ItemUseListener itemUseListener =
            new ItemUseListener(this);
    SoulSandListener soulSandListener =
            new SoulSandListener(this);
    SlimeListener slimeListener =
            new SlimeListener(this);
    KeyDownListener keyDownListener =
            new KeyDownListener(this);

    // server side states
    public boolean serverSneaking;

    // list of keys
    public static final KeyBinding[] KEYS = new KeyBinding[] {
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindSprint,
            mc.gameSettings.keyBindSneak,
            mc.gameSettings.keyBindJump
    };

    public NoSlowModule() {
        super("NoSlow", new String[] {"NoSlowDown"}, "Removes item slowdowns", ModuleCategory.MOVEMENT);

        // options
        associate(
                strictOption,
                airStrictOption,
                inventoryMoveOption,
                arrowMoveOption,
                itemsOption,
                websOption,
                webSpeedOption,
                soulSandOption,
                slimeOption,
                iceOption,
                bind,
                drawn
        );

        // listeners
        associate(
                outboundPacketListener,
                updateListener,
                inputUpdateListener,
                itemUseListener,
                soulSandListener,
                slimeListener,
                keyDownListener
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();

        // set the slipperiness of ice to the normal block value
        if (iceOption.getVal()) {
            Blocks.ICE.setDefaultSlipperiness(0.6F);
            Blocks.PACKED_ICE.setDefaultSlipperiness(0.6F);
            Blocks.FROSTED_ICE.setDefaultSlipperiness(0.6F);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        // update our sneak state
        if (serverSneaking && airStrictOption.getVal()) {

            // send stop sneak to match our server state
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }

        // reset state
        serverSneaking = false;

        // reset our keys
        for (KeyBinding binding : KEYS) {

            // reset keybind conflict to in-game
            binding.setKeyConflictContext(KeyConflictContext.IN_GAME);
        }

        // reset ice slipperiness to default value
        if (iceOption.getVal()) {
            Blocks.ICE.setDefaultSlipperiness(0.98F);
            Blocks.FROSTED_ICE.setDefaultSlipperiness(0.98F);
            Blocks.PACKED_ICE.setDefaultSlipperiness(0.98F);
        }

        // reset timer
        Modules.TIMER_MODULE.provide(1f);
    }

    /**
     * Checks if the player is slowed
     *
     * @return Whether the player is slowed
     */
    public boolean isSlowed() {
        return !mc.player.isRiding() && !mc.player.isElytraFlying() && mc.player.isHandActive() && itemsOption.getVal();
    }

    /**
     * Checks if the player is in a screen
     *
     * @return Whether the player is in a screen
     */
    public boolean isInScreen() {
        return mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditSign || mc.currentScreen instanceof GuiRepair);
    }
}
