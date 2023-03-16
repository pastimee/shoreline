package com.momentum.asm.mixins.vanilla.entity;

import com.momentum.Momentum;
import com.momentum.api.event.EventStage;
import com.momentum.impl.events.vanilla.entity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.MoverType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    // mc
    @Shadow
    protected Minecraft mc;

    @Shadow
    private boolean prevOnGround;

    @Shadow
    private float lastReportedYaw;

    @Shadow
    private float lastReportedPitch;

    @Shadow
    private int positionUpdateTicks;

    @Shadow
    private double lastReportedPosX;

    @Shadow
    private double lastReportedPosY;

    @Shadow
    private double lastReportedPosZ;

    @Shadow
    private boolean autoJumpEnabled;

    // server state: sprint
    @Shadow
    private boolean serverSprintState;

    // server state: sneak
    @Shadow
    private boolean serverSneakState;

    // server connection
    @Shadow
    @Final
    public NetHandlerPlayClient connection;

    // curr view entity
    @Shadow
    protected abstract boolean isCurrentViewEntity();

    // updateAutoJump method
    @Shadow
    protected abstract void updateAutoJump(float p_189810_1_, float p_189810_2_);

    // updates movement
    @Shadow
    public abstract void onUpdate();

    // updates packets
    @Shadow
    protected abstract void onUpdateWalkingPlayer();
    
    @Shadow 
    public abstract boolean isSneaking();

    // locks the update function
    private boolean lock;

    /**
     * Dummy constructor
     */
    public MixinEntityPlayerSP() {
        
        // copy player values
        super(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getGameProfile());
    }

    /**
     * Called when an entity is pushed out of a block
     **/
    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void onPushOutOfBlocks(double p_pushOutOfBlocks_1_, double p_pushOutOfBlocks_2_, double p_pushOutOfBlocks_3_, CallbackInfoReturnable<Boolean> cir) {

        // post the push out of blocks event
        PushOutOfBlocksEvent pushOutOfBlocksEvent = new PushOutOfBlocksEvent();
        Momentum.EVENT_BUS.dispatch(pushOutOfBlocksEvent);

        // cancel packet send if the event is canceled
        if (pushOutOfBlocksEvent.isCanceled()) {
            cir.cancel();
            cir.setReturnValue(false);
        }
    }

    /**
     * Called when movement packets are sent
     */
    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    private void onOnUpdateWalkingPlayer(CallbackInfo ci) {
        
        // post the update walking player event
        UpdateWalkingPlayerEvent updateWalkingPlayerEvent =
                new UpdateWalkingPlayerEvent(posX, posY, posZ, rotationYaw, rotationPitch, onGround);
        updateWalkingPlayerEvent.setStage(EventStage.PRE);
        Momentum.EVENT_BUS.dispatch(updateWalkingPlayerEvent);
        
        // client will send custom values
        if (updateWalkingPlayerEvent.isCanceled()) {

            // override vanilla packet data
            ci.cancel();

            // sprint flag
            boolean flag = isSprinting();

            // server and client sprint state are different
            if (flag != serverSprintState) {

                // send sprint packet
                if (flag) {
                    connection.sendPacket(new CPacketEntityAction(
                            this, Action.START_SPRINTING));
                }
                
                else {
                    connection.sendPacket(new CPacketEntityAction(
                            this, Action.STOP_SPRINTING));
                }

                // update server sprint state
                serverSprintState = flag;
            }

            // sneak flag
            boolean flag1 = isSneaking();

            // server and client sneak state are different
            if (flag1 != serverSneakState) {

                // send sneak packet
                if (flag1) {
                    connection.sendPacket(new CPacketEntityAction(
                            this, Action.START_SNEAKING));
                }
                
                else {
                    connection.sendPacket(new CPacketEntityAction(
                            this, Action.STOP_SNEAKING));
                }

                // update server sneak state
                serverSneakState = flag1;
            }

            // only update if we are the current viewer entity
            if (isCurrentViewEntity()) {

                // calc position diffs
                // AxisAlignedBB axisalignedbb = getEntityBoundingBox();
                double d0 = updateWalkingPlayerEvent.getX() - lastReportedPosX;
                double d1 = updateWalkingPlayerEvent.getY() - lastReportedPosY;
                double d2 = updateWalkingPlayerEvent.getZ() - lastReportedPosZ;
                double d3 = updateWalkingPlayerEvent.getYaw() - lastReportedYaw;
                double d4 = updateWalkingPlayerEvent.getPitch() - lastReportedPitch;

                // update position every 20 ticks
                ++positionUpdateTicks;

                // position/rotation update flags
                boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || positionUpdateTicks >= 20;
                boolean flag3 = d3 != 0.0D || d4 != 0.0D;

                // riding
                if (isRiding()) {
                    connection.sendPacket(new CPacketPlayer.PositionRotation(
                            motionX, -999.0D, motionZ, updateWalkingPlayerEvent.getYaw(), updateWalkingPlayerEvent.getPitch(), updateWalkingPlayerEvent.getOnGround()));

                    // don't update position twice
                    flag2 = false;
                }

                // position and rotation update
                else if (flag2 && flag3) {
                    connection.sendPacket(new CPacketPlayer.PositionRotation(
                            updateWalkingPlayerEvent.getX(), updateWalkingPlayerEvent.getY(), updateWalkingPlayerEvent.getZ(), updateWalkingPlayerEvent.getYaw(), updateWalkingPlayerEvent.getPitch(), updateWalkingPlayerEvent.getOnGround()));
                }

                // position update
                else if (flag2) {
                    connection.sendPacket(new CPacketPlayer.Position(
                            updateWalkingPlayerEvent.getX(), updateWalkingPlayerEvent.getY(), updateWalkingPlayerEvent.getZ(), updateWalkingPlayerEvent.getOnGround()));
                }

                // rotation update
                else if (flag3) {
                    connection.sendPacket(new CPacketPlayer.Rotation(
                            updateWalkingPlayerEvent.getYaw(), updateWalkingPlayerEvent.getPitch(), updateWalkingPlayerEvent.getOnGround()));
                }

                // onGround update only
                else if (prevOnGround != onGround) {
                    connection.sendPacket(new CPacketPlayer(updateWalkingPlayerEvent.getOnGround()));
                }

                // update last reported position
                // reset position update ticks
                if (flag2) {
                    lastReportedPosX = updateWalkingPlayerEvent.getX();
                    lastReportedPosY = updateWalkingPlayerEvent.getY();
                    lastReportedPosZ = updateWalkingPlayerEvent.getZ();
                    positionUpdateTicks = 0;
                }

                // update last reported rotations
                if (flag3) {
                    lastReportedYaw = updateWalkingPlayerEvent.getYaw();
                    lastReportedPitch = updateWalkingPlayerEvent.getPitch();
                }

                // mark previous onGround
                prevOnGround = updateWalkingPlayerEvent.getOnGround();
                autoJumpEnabled = mc.gameSettings.autoJump;
            }
        }

        // post update
        updateWalkingPlayerEvent.setStage(EventStage.POST);
        Momentum.EVENT_BUS.dispatch(updateWalkingPlayerEvent);
    }

    /**
     * Called when the sprint state updates on the entity living update
     */
    @Inject(method = "setSprinting", at = @At(value = "HEAD"), cancellable = true)
    private void onSetSprinting(boolean val, CallbackInfo ci) {

        // post living update event
        SprintingEvent sprintingEvent = new SprintingEvent();
        Momentum.EVENT_BUS.dispatch(sprintingEvent);

        // prevent val being false
        if (sprintingEvent.isCanceled() && !val) {
            
            // cancel
            ci.cancel();
        }
    }

    /**
     * Called when the player is updated
     */
    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;onUpdate()V", shift = Shift.BEFORE))
    private void onOnUpdate(CallbackInfo ci) {

        // post the update event
        UpdateEvent updateEvent = new UpdateEvent();
        Momentum.EVENT_BUS.dispatch(updateEvent);
    }

    /**
     * Called when player moves
     */
    @Inject(method = "move", at = @At(value = "HEAD"), cancellable = true)
    private void onMove(MoverType type, double x, double y, double z, CallbackInfo ci) {

        // post move event
        MoveEvent moveEvent = new MoveEvent(x, y, z);
        Momentum.EVENT_BUS.dispatch(moveEvent);

        // use custom movement
        if (moveEvent.isCanceled()) {

            // cancel move
            ci.cancel();
            
            // previous xz pos
            double px = posX;
            double pz = posZ;

            // move
            super.move(type, moveEvent.getX(), moveEvent.getY(), moveEvent.getZ());

            // update auto jump
            updateAutoJump((float) (posX - px), (float) (posZ - pz));
        }
    }

    /**
     * Called after the onUpdate method
     */
    @Inject(method = "onUpdate", at = @At(value = "TAIL"), cancellable = true)
    private void onOnUpdateTail(CallbackInfo ci) {

        // check if inject is locked
        if (!lock) {

            // post the update playerSP event
            UpdatePlayerSpEvent updatePlayerSpEvent = new UpdatePlayerSpEvent();
            Momentum.EVENT_BUS.dispatch(updatePlayerSpEvent);

            // event is canceled
            if (updatePlayerSpEvent.isCanceled()) {

                // prevent player from updating
                ci.cancel();

                // run dummy onUpdateWalkingPlayer
                for (int i = 0; i < updatePlayerSpEvent.getIterations(); i++) {

                    // lock
                    lock = true;

                    // run onUpdate, this updates motion
                    // run onUpdateWalkingPlayer, this updates packets
                    onUpdate();
                    onUpdateWalkingPlayer();

                    // unlock
                    lock = false;
                }
            }
        }
    }
}
