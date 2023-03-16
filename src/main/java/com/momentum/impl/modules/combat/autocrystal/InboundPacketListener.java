package com.momentum.impl.modules.combat.autocrystal;

import com.momentum.api.event.FeatureListener;
import com.momentum.asm.mixins.vanilla.accessors.ICPacketUseEntity;
import com.momentum.impl.events.vanilla.network.InboundPacketEvent;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

/**
 * @author linus
 * @since 03/14/2023
 */
public class InboundPacketListener extends FeatureListener<AutoCrystalModule, InboundPacketEvent> {

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected InboundPacketListener(AutoCrystalModule feature) {
        super(feature);
    }

    @Override
    public void invoke(InboundPacketEvent event) {

        // packet for sound effects
        if (event.getPacket() instanceof SPacketSoundEffect) {

            // packet from event
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();

            // explosion sound effect
            if (packet.getCategory() == SoundCategory.BLOCKS
                    && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {


            }
        }

        // packet for destroyed entities
        if (event.getPacket() instanceof SPacketDestroyEntities) {

            // packet from event
            SPacketDestroyEntities packet = (SPacketDestroyEntities) event.getPacket();

            // remove ids
            for (int id : packet.getEntityIDs()) {

                // attacks contains
                if (feature.attacks
                        .stream()
                        .anyMatch(p -> ((ICPacketUseEntity) p).getEntityId() == id)) {



                    // remove all
                    feature.clicks.removeIf(p -> ((ICPacketUseEntity) p).getEntityId() == id);

                    // produce new
                    feature.crystalP.produce();
                }
            }
        }

        // packet for spawning objects into world
        else if (event.getPacket() instanceof SPacketSpawnObject) {

            // packet from event
            SPacketSpawnObject packet = (SPacketSpawnObject) event.getPacket();

            // crystal spawn
            if (packet.getType() == 51) {

                // spawn pos
                BlockPos spawn = new BlockPos(
                        packet.getX() - 0.5f, packet.getY(), packet.getZ() - 0.5f);

                // clicks contains
                if (feature.clicks
                        .stream()
                        .anyMatch(p -> p.getPos() == spawn.down())) {

                    // attack on packet
                    if (feature.awaitOption.getVal()) {

                        // attack
                        int id = packet.getEntityID();
                        if (feature.attack(id)) {

                            // reset attack time
                            feature.crystalLast.reset();
                        }
                    }

                    // remove all
                    feature.clicks.removeIf(p -> p.getPos() == spawn.down());

                    // produce new
                    feature.placementP.produce();
                }
            }
        }
    }
}
