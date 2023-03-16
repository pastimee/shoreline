package com.momentum.impl.modules.movement.noslow;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.IEntity;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.entity.UpdateEvent;
import com.momentum.impl.init.Modules;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.input.Keyboard;

/**
 * @author linus
 * @since 02/13/2023
 */
public class UpdateListener extends FeatureListener<NoSlowModule, UpdateEvent> {
    protected UpdateListener(NoSlowModule feature) {
        super(feature);
    }

    @Override
    public void invoke(UpdateEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // server sneaking
        if (feature.serverSneaking) {

            // needs update
            if (feature.airStrictOption.getVal() && !mc.player.isHandActive()) {

                // update server state
                feature.serverSneaking = false;
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player,
                        Action.STOP_SNEAKING));
            }
        }

        // if we are slowed, then send corresponding packets
        if (feature.isSlowed()) {

            // NCP bypass
            // if (feature.strictOption.getVal()) {
            //    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(BlockPos.ORIGIN, EnumFacing.UP,
            //            EnumHand.MAIN_HAND, 0, 0, 0));
            // }
        }

        // player is in web
        if (((IEntity) mc.player).isInWeb()) {

            // webs and going down
            if (mc.gameSettings.keyBindSneak.isPressed() && feature.websOption.getVal()) {

                // fall
                if (!mc.player.onGround) {

                    // update timer
                    Modules.TIMER_MODULE.provide(feature.webSpeedOption.getVal());
                }

                // reset
                else {

                    // reset timer
                    Modules.TIMER_MODULE.provide(1.0f);
                }
            }
        }

        // allows you to move normally while in GUI screens
        if (feature.isInScreen() && feature.inventoryMoveOption.getVal()) {

            // update keybind state and conflict context
            for (KeyBinding binding : NoSlowModule.KEYS) {

                // set binding key state
                KeyBinding.setKeyBindState(binding.getKeyCode(), Keyboard.isKeyDown(binding.getKeyCode()));

                // set key conflict context to defaults (i.e. in-game context)
                binding.setKeyConflictContext(ConflictContext.DEFAULT_CONTEXT);
            }

            // update rotation based on arrow key movement
            if (feature.arrowMoveOption.getVal()) {

                // rotation values
                float yaw = mc.player.rotationYaw;
                float pitch = mc.player.rotationPitch;

                // up arrow key
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {

                    // look up
                    pitch -= 3;
                }

                // down arrow key
                else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {

                    // look down
                    pitch += 3;
                }

                // right arrow key
                else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {

                    // look right
                    yaw += 3;
                }

                // left arrow key
                else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {

                    // look left
                    yaw -= 3;
                }

                // update player rotation
                mc.player.rotationYaw = yaw;
                mc.player.rotationPitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
            }
        }

        // reset key context if no inventory move
        else {

            // reset keys
            for (KeyBinding binding : NoSlowModule.KEYS) {

                // reset key conflict context
                binding.setKeyConflictContext(KeyConflictContext.IN_GAME);
            }
        }
    }
}
