package com.momentum.asm.mixins.vanilla.network;

import com.momentum.Momentum;
import com.momentum.impl.events.vanilla.network.DisconnectEvent;
import com.momentum.impl.events.vanilla.network.InboundPacketEvent;
import com.momentum.impl.events.vanilla.network.OutboundPacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    /**
     * Called when the network manager checks for disconnection
     */
    @Inject(method = "checkDisconnected", at = @At(value = "HEAD"))
    public void onCheckDisconnected(CallbackInfo ci) {

        // post the disconnect event
        DisconnectEvent disconnectEvent = new DisconnectEvent();
        Momentum.EVENT_BUS.dispatch(disconnectEvent);
    }

    /**
     * Dispatched when the client sends a {@link Packet} to the server
     */
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onSendPacket(Packet<?> packetIn, CallbackInfo ci) {

        // post the packet send event
        OutboundPacketEvent outgoingPacketEvent = new OutboundPacketEvent(packetIn);
        Momentum.EVENT_BUS.dispatch(outgoingPacketEvent);

        // cancel packet send if the event is canceled
        if (outgoingPacketEvent.isCanceled()) {
            ci.cancel();
        }
    }

    /**
     * Dispatched when the server sends a {@link Packet} to the client
     */
    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    public void onPacketReceive(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo ci) {

        // post the packet receive event
        InboundPacketEvent inboundPacketEvent = new InboundPacketEvent(p_channelRead0_2_);
        Momentum.EVENT_BUS.dispatch(inboundPacketEvent);

        // cancel packet receive if the event is canceled
        if (inboundPacketEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
