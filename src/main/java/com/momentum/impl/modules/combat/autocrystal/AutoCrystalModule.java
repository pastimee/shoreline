package com.momentum.impl.modules.combat.autocrystal;

import com.momentum.api.feature.IService;
import com.momentum.api.feature.Option;
import com.momentum.api.module.BlockPlacerModule;
import com.momentum.api.module.ModuleCategory;
import com.momentum.api.thread.Consumer;
import com.momentum.api.thread.IThreaded;
import com.momentum.api.thread.Producer;
import com.momentum.api.thread.ProducerConsumerQueue;
import com.momentum.api.util.entity.EntityUtil;
import com.momentum.api.util.rotation.Rotation;
import com.momentum.api.util.rotation.RotationUtil;
import com.momentum.api.util.time.Timer;
import com.momentum.api.util.world.RaytraceUtil;
import com.momentum.asm.mixins.vanilla.accessors.ICPacketUseEntity;
import com.momentum.asm.mixins.vanilla.accessors.IEntityLivingBase;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * @author linus
 * @since 03/13/2023
 */
@SuppressWarnings("rawtypes")
public class AutoCrystalModule extends BlockPlacerModule
        implements IService<EntityEnderCrystal>, IThreaded {

    // AntiCheat options
    // NCP
    public final Option<Boolean> multiTaskOption =
            new Option<>("MultiTask", "Attacks when hand is active", true);
    public final Option<Boolean> whileMiningOption =
            new Option<>("WhileMining", "Attacks when mining", true);
    public final Option<Boolean> mineIgnoreOption =
            new Option<>("MineIgnore", "Ignores mining block when calculating placements", false);
    public final Option<Boolean> rotateOption =
            new Option<>("Rotate", "Rotates before attacking", true);
    public final Option<Timing> timingOption =
            new Option<>("Timing", "Attack timing mode", Timing.SEQUENTIAL);
    public final Option<YawStepMode> yawStepOption =
            new Option<>("YawStep", "Apply yaw step to rotation", YawStepMode.OFF);
    public final Option<Integer> yawStepThresholdOption =
            new Option<>("YawStepThreshold", "Maximum yaw step", 0, 25, 180);
    public final Option<Integer> yawStepTicksOption =
            new Option<>("YawStepTicks", "Yaw step ticks", 0, 2, 5);
    public final Option<Float> randomSpeedOption =
            new Option<>("RandomSpeed", "Random speed for attacks", 0.0f, 0.0f, 10.0f);

    // attack options
    public final Option<Boolean> blockDestructionOption =
            new Option<>("BlockDestruction", "Takes blast resistance into account in calculations", true);
    public final Option<Boolean> awaitOption =
            new Option<>("Await", "Attack on packet", true);
    public final Option<Swap> antiWeaknessOption =
            new Option<>("AntiWeakness", "Swaps to tool before attacking", Swap.NORMAL);
    public final Option<Boolean> safetyOption =
            new Option<>("Safety", "Check player safety when attacking", true);
    public final Option<Boolean> inhibitOption =
            new Option<>("Inhibit", "Prevents unnecessary attacks", true);
    public final Option<Float> safetyBalanceOption =
            new Option<>("SafetyBalance", "Safety target vs self damage balance", 0.0f, 1.0f, 5.0f);
    public final Option<Float> attackSpeedOption =
            new Option<>("AttackSpeed", new String[] {"CPS"}, "Speed for attacks", 0.0f, 20.0f, 20.0f);
    public final Option<Float> attackDelayOption =
            new Option<>("AttackDelay", "Delay for attacks", 0.0f, 0.0f, 5.0f);
    public final Option<Integer> attackFactorOption =
            new Option<>("AttackFactor", "???", 0, 0, 5);
    public final Option<Integer> ticksExistedOption =
            new Option<>("TicksExisted", "Ticks existed speed", 0, 0, 5);
    public final Option<Float> attackRangeOption =
            new Option<>("AttackRange", "Range for attacks", 0.0f, 4.5f, 5.0f);
    public final Option<Float> strictAttackRangeOption =
            new Option<>("StrictAttackRange", "Strict range for attacks", 0.0f, 4.5f, 5.0f);
    public final Option<Float> attackWallRangeOption =
            new Option<>("AttackWallRange", "Range for wall attacks", 0.0f, 4.5f, 5.0f);

    // place options
    public final Option<Boolean> playersOption =
            new Option<>("Players","Target players", true);
    public final Option<Boolean> monstersOption =
            new Option<>("Monsters", new String[] {"Hostiles"}, "Target monsters", true);
    public final Option<Boolean> neutralsOption =
            new Option<>("Neutrals", "Target neutrals", true);
    public final Option<Boolean> animalsOption =
            new Option<>("Animals", new String[] {"Passives"}, "Target animals", true);
    public final Option<Boolean> placeOption =
            new Option<>("Place", "Place crystals", true);
    public final Option<Sequential> sequentialOption =
            new Option<>("Sequential", "???", Sequential.NONE);
    public final Option<Boolean> boostOption =
            new Option<>("Boost", "???", true);
    public final Option<Boolean> tickSyncOption =
            new Option<>("TickSync", "?????", true);
    public final Option<RaytraceMode> raytraceOption =
            new Option<>("Raytrace", "Raytrace calculations for wall placements", RaytraceMode.TRACE);
    public final Option<Boolean> strictDirectionOption =
            new Option<>("StrictDirection", "Place on the correct face", true);
    public final Option<Boolean> armorBreakerOption =
            new Option<>("ArmorBreak", "Breaks armor durability", true);
    public final Option<Placements> placementsOption =
            new Option<>("Placements", "Crystal placement mode", Placements.NATIVE);
    public final Option<Swap> swapOption =
            new Option<>("Swap", "Crystal swap mode", Swap.NORMAL);
    public final Option<Float> alternativeSpeedOption =
            new Option<>("AlternativeSpeed", "Speed for alternative swap attacks", 0.0f, 18.0f, 20.0f);
    public final Option<Float> placeSpeedOption =
            new Option<>("PlaceSpeed", "Speed for placements", 0.0f, 20.0f, 20.0f);
    public final Option<Float> swapDelayOption =
            new Option<>("SwapDelay", "Swap delay", 0.0f, 0.0f, 10.0f);
    public final Option<Float> swapPenaltyOption =
            new Option<>("SwapPenalty", "???", 0.0f, 10.0f, 10.0f);
    public final Option<Float> placeRangeOption =
            new Option<>("PlaceRange", "Range for placements", 0.0f, 4.5f, 5.0f);
    public final Option<Float> strictPlaceRangeOption =
            new Option<>("StrictPlaceRange", "Strict range for placements", 0.0f, 4.5f, 5.0f);
    public final Option<Boolean> strictPlaceRangeEyeOption =
            new Option<>("StrictPlaceRangeEye", "Strict range to eye", true);
    public final Option<Float> placeWallRangeOption =
            new Option<>("PlaceWallRange", "Range for wall placements", 0.0f, 4.5f, 5.0f);
    public final Option<Float> enemyRangeOption =
            new Option<>("EnemyRange", "Range for enemy consideration", 0.0f, 8.0f, 15.0f);
    public final Option<Integer> extrapolationOption =
            new Option<>("Extrapolation", "Extrapolate movement", 0, 0, 10);
    public final Option<Float> minDamageOption =
            new Option<>("MinDamage", "Minimum damage", 0.1f, 6.0f, 10.0f);
    public final Option<Float> armorScaleOption =
            new Option<>("ArmorScale", "Armor breaker scale", 0.0f, 0.1f, 1.0f);
    public final Option<Float> maxLocalDamageOption =
            new Option<>("MaxLocalDamage", "Maximum local damage", 0.1f, 6.0f, 10.0f);
    public final Option<Float> lethalMultiplierOption =
            new Option<>("LethalMultiplier", "Multiplier to override minimum damage", 1.0f, 2.0f, 5.0f);

    // listeners
    public final TickListener tickListener =
            new TickListener(this);
    public final UpdateWalkingPlayerListener updateWalkingPlayerListener =
            new UpdateWalkingPlayerListener(this);
    public final InboundPacketListener inboundPacketListener =
            new InboundPacketListener(this);

    // packet tracker
    protected final Set<CPacketUseEntity> attacks =
            new ConcurrentSet<>();
    protected final Set<CPacketPlayerTryUseItemOnBlock> clicks =
            new ConcurrentSet<>();

    // shared objects
    protected final Queue<EntityEnderCrystal> crystals =
            new ProducerConsumerQueue<>();
    protected final Queue<BlockPos> placements =
            new ProducerConsumerQueue<>();

    // producers
    protected final Producer crystalP =
            new AttackProducer(this, crystals);
    protected final Producer placementP =
            new PlacementProducer(this, placements);

    // consumers
    protected final Consumer crystalC =
            new AttackConsumer(this, crystals);
    protected final Consumer placementC =
           new PlacementConsumer(this, placements);

    // timers
    protected final Timer crystalLast =
            new Timer();
    protected final Timer placementLast =
            new Timer();

    public AutoCrystalModule() {
        super("AutoCrystal", new String[] {"CrystalAura", "CA"}, "Automatically explodes crystals", ModuleCategory.COMBAT);

        // options
        associate(
                multiTaskOption,
                whileMiningOption,
                mineIgnoreOption,
                rotateOption,
                timingOption,
                yawStepOption,
                yawStepThresholdOption,
                yawStepTicksOption,
                randomSpeedOption,
                blockDestructionOption,
                awaitOption,
                antiWeaknessOption,
                safetyOption,
                inhibitOption,
                safetyBalanceOption,
                attackSpeedOption,
                attackDelayOption,
                attackFactorOption,
                ticksExistedOption,
                attackRangeOption,
                strictAttackRangeOption,
                attackWallRangeOption,
                playersOption,
                monstersOption,
                neutralsOption,
                animalsOption,
                placeOption,
                sequentialOption,
                boostOption,
                tickSyncOption,
                raytraceOption,
                strictDirectionOption,
                armorBreakerOption,
                placementsOption,
                swapOption,
                alternativeSpeedOption,
                placeSpeedOption,
                swapDelayOption,
                swapPenaltyOption,
                placeRangeOption,
                strictPlaceRangeOption,
                strictPlaceRangeEyeOption,
                placeWallRangeOption,
                enemyRangeOption,
                extrapolationOption,
                minDamageOption,
                armorScaleOption,
                maxLocalDamageOption,
                lethalMultiplierOption,
                bind,
                drawn
        );

        // listeners
        associate(
                tickListener,
                updateWalkingPlayerListener,
                inboundPacketListener
        );
    }

    /**
     * Provides the service
     *
     * @param in The input
     */
    @Override
    public void provide(EntityEnderCrystal in) {

        // add to queue
        // crystals.offer(in);
    }

    /**
     * Attacks the ender crystal
     *
     * @param in The ender crystal
     * @return Whether the attack was successful
     */
    protected boolean attack(EntityEnderCrystal in) {

        // make sure the target actually exists
        if (in == null || in.isDead) {

            // attack was not successful
            return false;
        }

        // attack
        int id = in.getEntityId();
        return attack(id);
    }

    /**
     * Attacks the entity id
     * Useful for attack on packets
     *
     * @param in The entity id
     * @return Whether the attack was successful
     */
    protected boolean attack(int in) {

        // offhand crystal
        ItemStack offhand = mc.player.getHeldItemOffhand();
        boolean off = offhand.getItem() instanceof ItemEndCrystal;

        // attack packet
        CPacketUseEntity packet = new CPacketUseEntity();
        ((ICPacketUseEntity) packet).setEntityId(in);
        ((ICPacketUseEntity) packet).setAction(Action.ATTACK);

        // attack
        mc.player.connection.sendPacket(packet);
        attacks.add(packet);

        // held item stack
        ItemStack stack = mc.player.getHeldItem(off ?
                EnumHand.OFF_HAND : EnumHand.MAIN_HAND);

        // check stack
        if (!stack.isEmpty()) {

            // swing item
            if (!stack.getItem().onEntitySwing(mc.player, stack)) {

                // check swing progress
                if (!mc.player.isSwingInProgress
                        || mc.player.swingProgressInt >= ((IEntityLivingBase) mc.player).hookGetArmSwingAnimationEnd() / 2
                        || mc.player.swingProgressInt < 0) {

                    // apply swing progress
                    mc.player.swingProgressInt = -1;
                    mc.player.isSwingInProgress = true;
                    mc.player.swingingHand = off ?
                            EnumHand.OFF_HAND : EnumHand.MAIN_HAND;

                    // send animation packet
                    if (mc.player.world instanceof WorldServer) {
                        ((WorldServer) mc.player.world).getEntityTracker().sendToTracking(mc.player,
                                new SPacketAnimation(mc.player, 0));
                    }
                }
            }
        }

        // swing
        mc.player.connection.sendPacket(new CPacketAnimation(off ?
                EnumHand.OFF_HAND: EnumHand.MAIN_HAND));

        // attack was not successful
        return false;
    }

    /**
     * Places a crystal
     *
     * @return Whether the placement was successful
     */
    @Override
    public boolean place(BlockPos in) {

        // pos exists check
        if (in == null) {

            // placement was not successful
            return false;
        }

        // items
        ItemStack mainhand = mc.player.getHeldItemMainhand();
        ItemStack offhand = mc.player.getHeldItemOffhand();
        boolean off = offhand.getItem() instanceof ItemEndCrystal;

        // check held item
        if (mainhand.getItem() instanceof ItemEndCrystal
                || off) {

            // placement direction
            EnumFacing face = EnumFacing.UP;

            // NCP-Update facings, requires some more calc
            if (strictDirectionOption.getVal()) {

                // out of sight
                int py = in.getY();
                if (py > mc.player.posY + mc.player.getEyeHeight()) {

                    // min
                    double min = 100.0;

                    // length
                    for (float x = 0.0f; x < 1.0f; x += 0.05f) {

                        // height
                        for (float y = 0.0f; y < 1.0f; y += 0.05f) {

                            // width
                            for (float z = 0.0f; z < 1.0f; z += 0.05f) {

                                // trace vector
                                Vec3d vec = new Vec3d(in)
                                        .addVector(x, y, z);

                                // distance to vec
                                double dist = mc.player.getDistance(vec.x, vec.y, vec.z);
                                if (dist < min) {

                                    // race between vectors
                                    RayTraceResult result = mc.world.rayTraceBlocks(
                                            mc.player.getPositionEyes(1), vec, false, true, false);

                                    // check result
                                    if (result != null
                                            && result.typeOfHit == Type.BLOCK) {

                                        // update direction
                                        min = dist;
                                        face = result.sideHit;
                                    }
                                }
                            }
                        }
                    }
                }

                // NCP facings
                else {

                    // center vector
                    Vec3d vec = new Vec3d(in)
                            .addVector(0.5f, 0.5f, 0.5f);

                    // race between vectors
                    RayTraceResult result = mc.world.rayTraceBlocks(
                            mc.player.getPositionEyes(1), vec, false, true, false);

                    // check result
                    if (result != null
                            && result.typeOfHit == Type.BLOCK) {

                        // update direction
                        face = result.sideHit;
                    }

                    // world height
                    int height = mc.world.getActualHeight();
                    if ((py + 1) == height) {

                        // bypass place check by facing downward
                        face = EnumFacing.DOWN;
                    }
                }

                // vectors
                Vec3d vec3d = mc.player.getPositionEyes(1.0f);
                Rotation r = RotationUtil.diff(vec3d, new Vec3d(in)
                        .addVector(0.5f, 0.5f, 0.5f));
                Vec3d vec3d1 = RotationUtil.getLook(r);
                float range = Math.max(placeRangeOption.getVal(), strictPlaceRangeOption.getVal());
                Vec3d vec3d2 = vec3d
                        .addVector(vec3d1.x * range, vec3d1.y * range, vec3d1.z * range);

                // trace result
                RayTraceResult result = mc.world.rayTraceBlocks(
                        vec3d, vec3d2, false, false, true);

                // check result
                if (result != null
                        && result.hitVec != null) {

                    // place vec
                    Vec3d vec = result.hitVec;
                    float f = (float) (vec.x - in.getX());
                    float f1 = (float) (vec.y - in.getY());
                    float f2 = (float) (vec.z - in.getZ());

                    // place
                    CPacketPlayerTryUseItemOnBlock packet =
                            new CPacketPlayerTryUseItemOnBlock(in, face, off ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, f, f1, f2);
                    mc.player.connection.sendPacket(packet);
                    clicks.add(packet);

                    // held item stack
                    ItemStack stack = mc.player.getHeldItem(off ?
                            EnumHand.OFF_HAND : EnumHand.MAIN_HAND);

                    // check stack
                    if (!stack.isEmpty()) {

                        // swing item
                        if (!stack.getItem().onEntitySwing(mc.player, stack)) {

                            // check swing progress
                            if (!mc.player.isSwingInProgress
                                    || mc.player.swingProgressInt >= ((IEntityLivingBase) mc.player).hookGetArmSwingAnimationEnd() / 2
                                    || mc.player.swingProgressInt < 0) {

                                // apply swing progress
                                mc.player.swingProgressInt = -1;
                                mc.player.isSwingInProgress = true;
                                mc.player.swingingHand = off ?
                                        EnumHand.OFF_HAND : EnumHand.MAIN_HAND;

                                // send animation packet
                                if (mc.player.world instanceof WorldServer) {
                                    ((WorldServer) mc.player.world).getEntityTracker().sendToTracking(mc.player,
                                            new SPacketAnimation(mc.player, 0));
                                }
                            }
                        }
                    }

                    // swing
                    mc.player.connection.sendPacket(new CPacketAnimation(off ?
                            EnumHand.OFF_HAND: EnumHand.MAIN_HAND));
                }
            }
        }

        // placement was not successful
        return false;
    }

    /**
     * Called when a Block is right-clicked with an {@link net.minecraft.item.ItemEndCrystal}
     *
     * @param worldIn The world
     * @param pos The block
     * @return The result of the action
     */
    protected EnumActionResult onUseItemEnderCrystal(World worldIn, BlockPos pos) {

        // block state
        IBlockState iblockstate = worldIn.getBlockState(pos);

        // check blocks
        if (iblockstate.getBlock() != Blocks.OBSIDIAN
                && iblockstate.getBlock() != Blocks.BEDROCK) {

            // crystal can only be placed on obsidian or bedrock
            return EnumActionResult.FAIL;
        }

        // passed block check
        else {

            // air block
            BlockPos blockpos = pos.up(); // +1
            IBlockState block = worldIn.getBlockState(blockpos);

            // check air block
            if (!worldIn.isAirBlock(blockpos) &&
                    !block.getBlock().isReplaceable(worldIn, blockpos)) {

                // air block
                BlockPos blockpos1 = blockpos.up(); // +2
                IBlockState block1 = worldIn.getBlockState(blockpos1);

                // check air block
                if (placementsOption.getVal() == Placements.PROTOCOL_HACK
                        || (!worldIn.isAirBlock(blockpos1)
                        && !block1.getBlock().isReplaceable(worldIn, blockpos1))) {

                    // crystal needs two block space
                    return EnumActionResult.FAIL;
                }
            }

            // passed air block checks
            else {

                // entities in placement
                double d0 = blockpos.getX();
                double d1 = blockpos.getY();
                double d2 = blockpos.getZ();
                List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(
                        null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0, d1 + 2.0, d2 + 1.0));

                // entity list is not empty
                if (!list.isEmpty()) {

                    // crystal blocked by entity
                    return EnumActionResult.FAIL;
                }

                // passed entity blocking check
                else {

                    // success
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        // unreachable
        return EnumActionResult.FAIL;
    }

    /**
     * Checks if a given block is visible to the player
     *
     * @param in The block
     * @return Whether a given block is visible to the player
     */
    protected boolean isVisible(BlockPos in) {

        // Trace to expected pos
        // NCP won't flag for placing at normal ranges
        return RaytraceUtil.canSee(mc.player, in,
                2.70000004768372f);
    }

    /**
     * Checks if a given entity is visible to the player
     *
     * @param in The entity
     * @return Whether a given entity is visible to the player
     */
    protected boolean isVisible(Entity in) {

        // scan raytrace
        if (raytraceOption.getVal() == RaytraceMode.SCAN) {

            // raytrace
            Vec3d trace = RaytraceUtil.canSeeScan(mc.player, in);

            // check bounding box
            return trace != null;
        }

        else {

            // offset
            float off = strictPlaceRangeEyeOption.getVal() ? in.getEyeHeight() : 0.0f;

            // raytrace offset
            return RaytraceUtil.canSee(mc.player, in, off);
        }
    }

    /**
     * Checks entity type
     *
     * @param in The entity
     * @return Whether the entity is a valid entity type
     */
    protected boolean isValidEntity(Entity in) {

        // entity check
        return in instanceof EntityPlayer && playersOption.getVal()
                || EntityUtil.isHostileMob(in) && monstersOption.getVal()
                || EntityUtil.isNeutralMob(in) && neutralsOption.getVal()
                || EntityUtil.isPassiveMob(in) && animalsOption.getVal();
    }
}
