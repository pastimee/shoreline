package com.momentum.impl.handlers.hole;

import com.momentum.Momentum;
import com.momentum.api.event.Listener;
import com.momentum.api.handler.Handler;
import com.momentum.api.thread.IThreaded;
import com.momentum.api.util.block.BlockScanner;
import com.momentum.api.util.block.ResistantBlocks;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.vanilla.TickEvent;
import com.momentum.impl.events.vanilla.network.InboundPacketEvent;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange.BlockUpdateData;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Handles holes
 *
 * @author linus
 * @since 03/13/2023
 */
public class HoleHandler extends Handler
        implements IThreaded {

    // holes
    private final List<Hole> holes =
            new CopyOnWriteArrayList<>();

    // call service
    private int calls;
    private final ExecutorCompletionService<Hole> cs =
            new ExecutorCompletionService<>(es);

    /**
     * Handles holes
     */
    public HoleHandler() {

        // hole updater impl
        Momentum.EVENT_BUS.subscribe(new Listener<TickEvent>() {

            @Override
            public void invoke(TickEvent event) {

                // null check
                if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
                    return;
                }

                // catches InterruptedException and ExecutionException
                try {

                    // run for all calls
                    int r = 0;
                    for (int i = 0; i < calls; i++) {

                        // result
                        Future<Hole> result = cs.take();

                        // check result found
                        if (result != null
                                && result.get() != null) {

                            // add hole
                            Hole hole = result.get();

                            // check hole
                            if (hole.getPos() != null
                                    && hole.getHoleType() != null) {

                                holes.add(hole);
                            }

                            // invalid hole
                            else {

                                // remove
                                holes.remove(hole);
                            }

                            // call produced results
                            r++;
                        }
                    }
                    
                    // update list
                    calls -= r;
                }

                // no exception handling
                catch (InterruptedException | ExecutionException e) {

                    // print error
                    e.printStackTrace();
                }
            }
        });

        // hole finder impl
        Momentum.EVENT_BUS.subscribe(new Listener<InboundPacketEvent>() {

            @Override
            public void invoke(InboundPacketEvent event) {

                // packet for chunk data
                if (event.getPacket() instanceof SPacketChunkData) {

                    // packet from event
                    SPacketChunkData packet = (SPacketChunkData) event.getPacket();

                    // chunk x
                    for (int x = packet.getChunkX(); x < 16; x++) {

                        // world height
                        for (int y = 0; y < mc.world.getActualHeight(); y++) {

                            // chunk z
                            for (int z = packet.getChunkZ(); z < 16; z++) {

                                // chunk pos
                                BlockPos pos = new BlockPos(x, y, z);

                                // check air
                                if (BlockScanner.canStandOn(pos)) {

                                    // callable impl
                                    HoleCallable callable =
                                            new HoleCallable(pos);

                                    // submit to executor
                                    cs.submit(callable);
                                    calls++;
                                }
                            }
                        }
                    }
                }

                // packet for block changes
                else if (event.getPacket() instanceof SPacketBlockChange) {

                    // packet from event
                    SPacketBlockChange packet = (SPacketBlockChange) event.getPacket();

                    // block pos change
                    BlockPos pos = packet.getBlockPosition();

                    // callable impl
                    HoleCallable callable =
                            new HoleCallable(pos);

                    // submit to executor
                    cs.submit(callable);
                    calls++;

                    // check all faces
                    for (EnumFacing face : EnumFacing.VALUES) {

                        // facing pos
                        BlockPos facing = pos.offset(face);

                        // callable impl
                        HoleCallable fcallable =
                                new HoleCallable(facing);

                        // submit to executor
                        cs.submit(fcallable);
                        calls++;
                    }
                }

                // packet for multi block changes
                else if (event.getPacket() instanceof SPacketMultiBlockChange) {

                    // packet from event
                    SPacketMultiBlockChange packet = (SPacketMultiBlockChange) event.getPacket();

                    // changes
                    for (BlockUpdateData update : packet.getChangedBlocks()) {

                        // block pos change
                        BlockPos pos = update.getPos();

                        // callable impl
                        HoleCallable callable =
                                new HoleCallable(pos);

                        // submit to executor
                        cs.submit(callable);
                        calls++;

                        // check all faces
                        for (EnumFacing face : EnumFacing.VALUES) {

                            // facing pos
                            BlockPos facing = pos.offset(face);

                            // callable impl
                            HoleCallable fcallable =
                                    new HoleCallable(facing);

                            // submit to executor
                            cs.submit(fcallable);
                            calls++;
                        }
                    }
                }
            }
        });
    }

    /**
     * Gets the found holes
     *
     * @return The found holes
     */
    public Collection<Hole> getHoles() {
        return holes;
    }

    // callable impl
    static class HoleCallable
            implements Callable<Hole> {

        // Standard Holes, player can stand in them to prevent
        // large amounts of explosion damage
        public static final Vec3i[] SINGLE = {
                new Vec3i(1, 0, 0),
                new Vec3i(-1, 0, 0),
                new Vec3i(0, 0, 1),
                new Vec3i(0, 0, -1)
        };

        // Double X Holes, player can stand in the middle of the blocks
        // to prevent placements on these blocks
        public static final Vec3i[] DOUBLE_X = {

                //
                new Vec3i(2, 0, 0),
                new Vec3i(-1, 0, 0),
                new Vec3i(0, 0, 1),
                new Vec3i(1, 0, 1),
                new Vec3i(0, 0, -1),
                new Vec3i(1, 0, -1)
        };

        // Double Z Holes, player can stand in the middle of the blocks
        // to prevent placements on these blocks
        public static final Vec3i[] DOUBLE_Z = {

                new Vec3i(1, 0, 0),
                new Vec3i(1, 0, 1),
                new Vec3i(-1, 0, 0),
                new Vec3i(-1, 0, 1),
                new Vec3i(0, 0, 2),
                new Vec3i(0, 0, -1)
        };

        // Quad Holes, player can stand in the middle of four blocks
        // to prevent placements on these blocks
        public static final Vec3i[] QUAD = {
                new Vec3i(2, 0, 0),
                new Vec3i(2, 0, 1),
                new Vec3i(1, 0, 2),
                new Vec3i(0, 0, 2),
                new Vec3i(-1, 0, 0),
                new Vec3i(-1, 0, 1),
                new Vec3i(0, 0, -1),
                new Vec3i(1, 0, -1),
        };

        // pos to run
        private final BlockPos pos;

        /**
         * Initializes callable
         *
         * @param pos The pos to run
         */
        public HoleCallable(BlockPos pos) {
            this.pos = pos;
        }

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         */
        @Override
        public Hole call() {

            // center
            BlockPos center = pos;

            // side resistance
            int resistant = 0;
            int unbreakable = 0;

            // check center
            if (BlockScanner.canStandOn(center)) {

                // check all offsets
                for (Vec3i side : SINGLE) {

                    // offset pos by side
                    BlockPos off = pos.add(side);

                    // resistance check
                    if (ResistantBlocks.isBlastResistant(off)) {

                        // update number of resistant sides
                        resistant++;
                    }

                    // unbreakable check
                    else if (ResistantBlocks.isUnbreakable(off)) {

                        // update number of unbreakable sides
                        unbreakable++;
                    }
                }

                // check sum
                int sum = resistant + unbreakable;
                if (resistant == SINGLE.length
                        || unbreakable == SINGLE.length
                        || sum == SINGLE.length) {

                    // found a new hole
                    return new Hole(pos, sum == SINGLE.length ?
                            HoleType.MIXED : resistant == SINGLE.length ? HoleType.RESISTANT : HoleType.UNBREAKABLE);
                }
            }

            // reset
            resistant = 0;
            unbreakable = 0;

            // center
            center = pos.add(1, 0, 0);

            // check center
            if (BlockScanner.canStandOn(center)) {

                // check all offsets
                for (Vec3i side : DOUBLE_X) {

                    // offset pos by side
                    BlockPos off = pos.add(side);

                    // resistance check
                    if (ResistantBlocks.isBlastResistant(off)) {

                        // update number of resistant sides
                        resistant++;
                    }

                    // unbreakable check
                    else if (ResistantBlocks.isUnbreakable(off)) {

                        // update number of unbreakable sides
                        unbreakable++;
                    }
                }

                // check sum
                int sum = resistant + unbreakable;
                if (resistant == DOUBLE_X.length
                        || unbreakable == DOUBLE_X.length
                        || sum == DOUBLE_X.length) {

                    // found a new hole
                    return new Hole(pos, sum == DOUBLE_X.length ?
                            HoleType.DOUBLE_MIXED_X : resistant == DOUBLE_X.length ? HoleType.DOUBLE_RESISTANT_X : HoleType.DOUBLE_UNBREAKABLE_X);
                }
            }

            // reset
            resistant = 0;
            unbreakable = 0;

            // center
            center = pos.add(0, 0, 1);

            // check center
            if (BlockScanner.canStandOn(center)) {

                // check all offsets
                for (Vec3i side : DOUBLE_Z) {

                    // offset pos by side
                    BlockPos off = pos.add(side);

                    // resistance check
                    if (ResistantBlocks.isBlastResistant(off)) {

                        // update number of resistant sides
                        resistant++;
                    }

                    // unbreakable check
                    else if (ResistantBlocks.isUnbreakable(off)) {

                        // update number of unbreakable sides
                        unbreakable++;
                    }
                }

                // check sum
                int sum = resistant + unbreakable;
                if (resistant == DOUBLE_Z.length
                        || unbreakable == DOUBLE_Z.length
                        || sum == DOUBLE_Z.length) {

                    // found a new hole
                    return new Hole(pos, sum == DOUBLE_Z.length ?
                            HoleType.DOUBLE_MIXED_Z : resistant == DOUBLE_Z.length ? HoleType.DOUBLE_RESISTANT_Z : HoleType.DOUBLE_UNBREAKABLE_Z);
                }
            }

            // not a hole
            return new Hole(null, null);
        }
    }
}
