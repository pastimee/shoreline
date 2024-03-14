package net.shoreline.client.impl.module.combat;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.NumberDisplay;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.manager.world.tick.TickSync;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.RotationModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;
import net.shoreline.client.util.player.RotationUtil;
import net.shoreline.client.util.string.EnumFormatter;
import net.shoreline.client.util.world.EntityUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class AuraModule extends RotationModule
{
    Config<Boolean> swingConfig = new BooleanConfig("Swing", "Swings the " +
            "hand after attacking", true);
    // RANGES
    Config<TargetMode> modeConfig = new EnumConfig<>("Mode", "The mode for " +
            "targeting entities to attack", TargetMode.SWITCH,
            TargetMode.values());
    Config<Priority> priorityConfig = new EnumConfig<>("Priority", "The " +
            "heuristic to prioritize when searching for targets",
            Priority.HEALTH, Priority.values());
    Config<Float> rangeConfig = new NumberConfig<>("Range", "Range to attack " +
            "entities", 1.0f, 4.5f, 5.0f);
    Config<Float> wallRangeConfig = new NumberConfig<>("WallRange", "Range to" +
            " attack entities through walls", 1.0f, 4.5f, 5.0f);
    Config<Float> fovConfig = new NumberConfig<>("FOV", "Field of view to " +
            "attack entities", 1.0f, 180.0f, 180.0f);
    Config<Boolean> latencyPositionConfig = new BooleanConfig(
            "LatencyPosition", "Targets the latency positions of enemies", false);
    Config<Integer> maxLatencyConfig = new NumberConfig<>("MaxLatency",
            "Maximum latency factor when calculating positions", 50, 250,
            1000, () -> latencyPositionConfig.getValue());
    //
    Config<Boolean> attackDelayConfig = new BooleanConfig("AttackDelay",
            "Delays attacks according to minecraft hit delays for maximum " +
                    "damage per attack", false);
    Config<Float> attackSpeedConfig = new NumberConfig<>("AttackSpeed",
            "Delay for attacks (Only functions if AttackDelay is off)", 1.0f,
            20.0f, 20.0f, () -> !attackDelayConfig.getValue());
    Config<Float> randomSpeedConfig = new NumberConfig<>("RandomSpeed",
            "Randomized delay for attacks (Only functions if AttackDelay is " +
                    "off)", 0.0f, 0.0f, 10.0f,
            () -> !attackDelayConfig.getValue());
    Config<Integer> packetsConfig = new NumberConfig<>("Packets", "Maximum " +
            "attack packets to send in a single tick", 0, 1, 20);
    Config<Float> swapDelayConfig = new NumberConfig<>("SwapPenalty", "Delay " +
            "for attacking after swapping items which prevents NCP flags", 0.0f,
            0.0f, 10.0f);
    Config<TickSync> tpsSyncConfig = new EnumConfig<>("TPS-Sync", "Syncs the " +
            "attacks with the server TPS", TickSync.NONE, TickSync.values());
    Config<Boolean> autoSwapConfig = new BooleanConfig("AutoSwap",
            "Automatically swaps to a weapon before attacking", true);
    Config<Boolean> swordCheckConfig = new BooleanConfig("Sword-Check",
            "Checks if a weapon is in the hand before attacking", true);
    // ROTATE
    Config<Vector> hitVectorConfig = new EnumConfig<>("HitVector", "The " +
            "vector to aim for when attacking entities", Vector.FEET,
            Vector.values());
    Config<Boolean> rotateConfig = new BooleanConfig("Rotate", "Rotate" +
            "before attacking", false);
    Config<Boolean> strictRotateConfig = new BooleanConfig("RotateStrict",
            "Rotates yaw over multiple ticks to prevent certain rotation  " +
                    "flags in NCP", false, () -> rotateConfig.getValue());
    Config<Integer> rotateLimitConfig = new NumberConfig<>(
            "RotateLimit", "Maximum yaw rotation in degrees for one tick",
            1, 180, 180, NumberDisplay.DEGREES,
            () -> rotateConfig.getValue() && strictRotateConfig.getValue());
    Config<Integer> yawTicksConfig = new NumberConfig<>("YawTicks",
            "Minimum ticks to rotate yaw", 1, 1, 5,
            () -> rotateConfig.getValue() && strictRotateConfig.getValue());
    Config<Integer> rotateTimeoutConfig = new NumberConfig<>(
            "RotateTimeout", "Minimum ticks to hold the rotation yaw after " +
            "reaching the rotation", 0, 0, 5, () -> rotateConfig.getValue());
    Config<Integer> ticksExistedConfig = new NumberConfig<>("TicksExisted",
            "The minimum age of the entity to be considered for attack", 0, 50, 200);
    Config<Boolean> armorCheckConfig = new BooleanConfig("ArmorCheck",
            "Checks if target has armor before attacking", false);
    Config<Boolean> autoBlockConfig = new BooleanConfig("AutoBlock",
            "Automatically blocks after attack", false);
    Config<Boolean> stopSprintConfig = new BooleanConfig("StopSprint",
            "Stops sprinting before attacking to maintain vanilla behavior", false);
    Config<Boolean> stopShieldConfig = new BooleanConfig("StopShield",
            "Automatically handles shielding before attacking", false);
    //
    Config<Boolean> playersConfig = new BooleanConfig("Players",
            "Target players", true);
    Config<Boolean> monstersConfig = new BooleanConfig("Monsters",
            "Target monsters", false);
    Config<Boolean> neutralsConfig = new BooleanConfig("Neutrals",
            "Target neutrals", false);
    Config<Boolean> animalsConfig = new BooleanConfig("Animals",
            "Target animals", false);
    Config<Boolean> invisiblesConfig = new BooleanConfig("Invisibles",
            "Target invisible entities", true);
    Config<Boolean> renderConfig = new BooleanConfig("Render",
            "Renders an indicator over the target", true);
    //
    private Entity entityTarget;
    private final Timer attackTimer = new CacheTimer();
    private final Timer critTimer = new CacheTimer();
    private final Timer autoSwapTimer = new CacheTimer();
    private final Timer switchTimer = new CacheTimer();
    private long randomDelay = -1;
    //
    private int rotating;

    /**
     *
     */
    public AuraModule()
    {
        super("Aura", "Attacks nearby entities", ModuleCategory.COMBAT);
    }

    /**
     *
     * @return
     */
    @Override
    public String getModuleData()
    {
        return EnumFormatter.formatEnum(modeConfig.getValue());
    }

    /**
     *
     */
    @Override
    public void onDisable()
    {
        entityTarget = null;
        rotating = 0;
    }

    /**
     *
     * @param event
     */
    @EventListener
    public void onDisconnect(DisconnectEvent event)
    {
        disable();
    }

    /**
     *
     * @param event
     */
    @EventListener
    public void onPlayerUpdate(PlayerUpdateEvent event)
    {
        if (event.getStage() != EventStage.PRE)
        {
            return;
        }
        if (Modules.AUTO_CRYSTAL.isAttacking()
                || Modules.AUTO_CRYSTAL.isPlacing()
                || rotating > 0)
        {
            return;
        }
        final Vec3d eyepos = Managers.POSITION.getEyePos();
        switch (modeConfig.getValue())
        {
            case SWITCH -> entityTarget = getAttackTarget(eyepos);
            case SINGLE ->
            {
                if (entityTarget == null || !entityTarget.isAlive()
                        || !isInAttackRange(eyepos, entityTarget))
                {
                    entityTarget = getAttackTarget(eyepos);
                }
            }
        }
        if (entityTarget == null || !switchTimer.passed(swapDelayConfig.getValue() * 25.0f))
        {
            return;
        }
        if (rotateConfig.getValue())
        {
            float[] rotation = RotationUtil.getRotationsTo(mc.player.getEyePos(),
                    getAttackRotateVec(entityTarget));
            setRotation(rotation[0], rotation[1]);
        }
        if (isRotationBlocked())
        {
            return;
        }
        if (attackDelayConfig.getValue())
        {
            float ticks = 20.0f - Managers.TICK.getTickSync(tpsSyncConfig.getValue());
            float progress = mc.player.getAttackCooldownProgress(ticks);
            if (progress >= 1.0f && attackTarget(entityTarget))
            {
                mc.player.resetLastAttackedTicks();
            }
        }
        else
        {
            if (randomDelay < 0)
            {
                randomDelay = (long) RANDOM.nextFloat(
                        (randomSpeedConfig.getValue() * 10.0f) + 1.0f);
            }
            float delay = (attackSpeedConfig.getValue() * 50.0f) + randomDelay;
            if (attackTimer.passed(1000.0f - delay) && attackTarget(entityTarget))
            {
                randomDelay = -1;
                attackTimer.reset();
            }
        }
    }

    /**
     *
     * @param event
     */
    @EventListener
    public void onPacketOutbound(PacketEvent.Outbound event)
    {
        if (mc.player == null)
        {
            return;
        }
        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket)
        {
            switchTimer.reset();
            if (!event.isClientPacket())
            {
                autoSwapTimer.reset();
            }
        }
    }

    /**
     *
     * @param event
     */
    @EventListener
    public void onRenderWorld(RenderWorldEvent event)
    {
        if (entityTarget != null && renderConfig.getValue() && isHoldingSword())
        {
            RenderManager.renderBox(event.getMatrices(),
                    entityTarget.getBoundingBox(), Modules.COLORS.getRGB(60));
            RenderManager.renderBoundingBox(event.getMatrices(),
                    entityTarget.getBoundingBox(), 1.5f, Modules.COLORS.getRGB(145));
        }
    }

    /**
     *
     * @param entity
     * @return
     */
    private boolean attackTarget(Entity entity)
    {
        if (mc.player.isUsingItem() && mc.player.getActiveHand() == Hand.MAIN_HAND
                || mc.options.attackKey.isPressed())
        {
            autoSwapTimer.reset();
            return false;
        }
        // END PRE
        boolean sword = mc.player.getMainHandStack().getItem() instanceof SwordItem;
        if (autoSwapConfig.getValue() && autoSwapTimer.passed(500) && !sword)
        {
            int slot = getSwordSlot();
            if (slot != -1)
            {
                mc.player.getInventory().selectedSlot = slot;
                Managers.NETWORK.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
            }
        }
        if (!isHoldingSword())
        {
            return false;
        }
        preAttackTarget();
        // preMotionAttackTarget();
        Managers.NETWORK.sendPacket(PlayerInteractEntityC2SPacket.attack(entity,
                Managers.POSITION.isSneaking()));
        if (swingConfig.getValue())
        {
            mc.player.swingHand(Hand.MAIN_HAND);
        }
        else
        {
            Managers.NETWORK.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
        }
        postAttackTarget(entity);
        return true;
    }

    /**
     *
     * @return
     */
    private int getSwordSlot()
    {
        float sharp = 0.0f;
        int slot = -1;
        // Maximize item attack damage
        for (int i = 0; i < 9; i++)
        {
            final ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof SwordItem swordItem)
            {
                float sharpness = EnchantmentHelper.getLevel(
                        Enchantments.SHARPNESS, stack) * 0.5f + 0.5f;
                float dmg = swordItem.getAttackDamage() + sharpness;
                if (dmg > sharp)
                {
                    sharp = dmg;
                    slot = i;
                }
            }
        }
        return slot;
    }

    private boolean shielding;
    private boolean sneaking;
    private boolean sprinting;

    private void preAttackTarget()
    {
        final ItemStack offhand = mc.player.getOffHandStack();
        // Shield state
        shielding = false;
        if (stopShieldConfig.getValue())
        {
            shielding = offhand.getItem() == Items.SHIELD
                    && mc.player.isBlocking();
            if (shielding)
            {
                Managers.NETWORK.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM,
                        Managers.POSITION.getBlockPos(), Direction.getFacing(mc.player.getX(),
                        mc.player.getY(), mc.player.getZ())));
            }
        }
        sneaking = false;
        sprinting = false;
        if (stopSprintConfig.getValue())
        {
            sneaking = Managers.POSITION.isSneaking();
            if (sneaking)
            {
                Managers.NETWORK.sendPacket(new ClientCommandC2SPacket(mc.player,
                        ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
            }
            sprinting = Managers.POSITION.isSprinting();
            if (sprinting)
            {
                Managers.NETWORK.sendPacket(new ClientCommandC2SPacket(mc.player,
                        ClientCommandC2SPacket.Mode.STOP_SPRINTING));
            }
        }
    }

    // RELEASE
    private void postAttackTarget(Entity entity)
    {
        if (shielding)
        {
            Managers.NETWORK.sendSequencedPacket(s ->
                    new PlayerInteractItemC2SPacket(Hand.OFF_HAND, s));
        }
        if (sneaking)
        {
            Managers.NETWORK.sendPacket(new ClientCommandC2SPacket(mc.player,
                    ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
        }
        if (sprinting)
        {
            Managers.NETWORK.sendPacket(new ClientCommandC2SPacket(mc.player,
                    ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
        if (Modules.CRITICALS.isEnabled() && critTimer.passed(500))
        {
            if (!mc.player.isOnGround()
                    || mc.player.isRiding()
                    || mc.player.isSubmergedInWater()
                    || mc.player.isInLava()
                    || mc.player.isHoldingOntoLadder()
                    || mc.player.hasStatusEffect(StatusEffects.BLINDNESS)
                    || mc.player.input.jumping)
            {
                return;
            }
            Modules.CRITICALS.preAttackPacket();
            critTimer.reset();
            mc.player.addCritParticles(entity);
        }
    }

    /**
     *
     * @param pos
     * @return
     */
    private Entity getAttackTarget(Vec3d pos)
    {
        double min = Double.MAX_VALUE;
        Entity attackTarget = null;
        for (Entity entity : mc.world.getEntities())
        {
            if (entity == null || entity == mc.player
                    || !entity.isAlive() || !isEnemy(entity)
                    || Managers.SOCIAL.isFriend(entity.getUuid())
                    || entity instanceof EndCrystalEntity
                    || entity instanceof ItemEntity
                    || entity instanceof ArrowEntity
                    || entity instanceof ExperienceBottleEntity
                    || entity instanceof PlayerEntity player && player.isCreative())
            {
                continue;
            }
            if (armorCheckConfig.getValue()
                    && entity instanceof LivingEntity
                    && !entity.getArmorItems().iterator().hasNext())
            {
                continue;
            }
            double dist = pos.distanceTo(entity.getPos());
            if (isInAttackRange(dist, pos, entity))
            {
                if (entity.age < ticksExistedConfig.getValue())
                {
                    continue;
                }
                switch (priorityConfig.getValue())
                {
                    case DISTANCE ->
                    {
                        if (dist < min)
                        {
                            min = dist;
                            attackTarget = entity;
                        }
                    }
                    case HEALTH ->
                    {
                        if (entity instanceof LivingEntity e)
                        {
                            float health = EntityUtil.getHealth(e);
                            if (health < min)
                            {
                                min = health;
                                attackTarget = entity;
                            }
                        }
                    }
                    case ARMOR ->
                    {
                        if (entity instanceof LivingEntity e)
                        {
                            float armor = getArmorDurability(e);
                            if (armor < min)
                            {
                                min = armor;
                                attackTarget = entity;
                            }
                        }
                    }
                }
            }
        }
        return attackTarget;
    }

    /**
     *
     * @param e
     * @return
     */
    private float getArmorDurability(LivingEntity e)
    {
        float edmg = 0.0f;
        float emax = 0.0f;
        for (ItemStack armor : e.getArmorItems())
        {
            if (armor != null && !armor.isEmpty())
            {
                edmg += armor.getDamage();
                emax += armor.getMaxDamage();
            }
        }
        return 100.0f - edmg / emax;
    }

    /**
     *
     * @param pos
     * @param entity
     * @return
     */
    public boolean isInAttackRange(Vec3d pos, Entity entity)
    {
        double dist = pos.distanceTo(entity.getPos());
        return isInAttackRange(dist, pos, entity);
    }

    /**
     *
     * @param dist
     * @param pos
     * @param entity
     * @return
     */
    public boolean isInAttackRange(double dist, Vec3d pos, Entity entity)
    {
        if (dist > rangeConfig.getValue())
        {
            return false;
        }
        BlockHitResult result = mc.world.raycast(new RaycastContext(
                pos, entity.getPos(),
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE, mc.player));
        if (result != null && dist > wallRangeConfig.getValue())
        {
            return false;
        }
        if (fovConfig.getValue() != 180.0f)
        {
            float[] rots = RotationUtil.getRotationsTo(pos, entity.getPos());
            float diff = MathHelper.wrapDegrees(mc.player.getYaw()) - rots[0];
            float magnitude = Math.abs(diff);
            return magnitude <= fovConfig.getValue();
        }
        return true;
    }

    /**
     *
     * @return
     */
    public boolean isHoldingSword()
    {
        return !swordCheckConfig.getValue() || mc.player.getMainHandStack().getItem() instanceof SwordItem;
    }

    private Vec3d getAttackRotateVec(Entity entity)
    {
        Vec3d feetPos = entity.getPos();
        return switch (hitVectorConfig.getValue())
        {
            case FEET -> feetPos;
            case TORSO -> feetPos.add(0.0, entity.getHeight() / 2.0f, 0.0);
            case EYES -> feetPos.add(0.0, entity.getStandingEyeHeight(), 0.0);
        };
    }

    /**
     * Returns the number of ticks the rotation will last after setting the
     * player rotations to the dest rotations. Yaws are only calculated in
     * this method, the yaw will not be updated until the main
     * loop runs.
     *
     * @param dest The rotation
     * @return
     */
    private float[] getLimitRotation(float dest)
    {
        int tick;
        float[] yawLimits;
        if (strictRotateConfig.getValue())
        {
            float diff = dest - Managers.ROTATION.getWrappedYaw(); // yaw diff
            float magnitude = Math.abs(diff);
            if (magnitude > 180.0f)
            {
                diff += diff > 0.0f ? -360.0f : 360.0f;
            }
            final int dir = diff > 0.0f ? 1 : -1;
            tick = yawTicksConfig.getValue();
            // partition yaw
            float deltaYaw = magnitude / tick;
            if (deltaYaw > rotateLimitConfig.getValue())
            {
                tick = MathHelper.ceil(magnitude / rotateLimitConfig.getValue());
                deltaYaw = magnitude / tick;
            }
            deltaYaw *= dir;
            int yawCount = tick;
            tick += rotateTimeoutConfig.getValue();
            yawLimits = new float[tick];
            int off = tick - 1;
            float yawTotal = 0.0f;
            for (int i = 0; i < tick; ++i)
            {
                if (i > yawCount)
                {
                    yawLimits[off - i] = 0.0f;
                    continue;
                }
                yawTotal += deltaYaw;
                yawLimits[off - i] = yawTotal;
            }
        }
        else
        {
            tick = rotateTimeoutConfig.getValue() + 1;
            yawLimits = new float[tick];
            int off = tick - 1;
            yawLimits[off] = dest;
            for (int i = 1; i < tick; ++i)
            {
                yawLimits[off - i] = 0.0f;
            }
        }
        return yawLimits;
    }

    /**
     * Returns <tt>true</tt> if the {@link Entity} is a valid enemy to attack.
     *
     * @param e The potential enemy entity
     * @return <tt>true</tt> if the entity is an enemy
     *
     * @see EntityUtil
     */
    private boolean isEnemy(Entity e)
    {
        return (!e.isInvisible() || invisiblesConfig.getValue())
                && e instanceof PlayerEntity && playersConfig.getValue()
                || EntityUtil.isMonster(e) && monstersConfig.getValue()
                || EntityUtil.isNeutral(e) && neutralsConfig.getValue()
                || EntityUtil.isPassive(e) && animalsConfig.getValue();
    }

    public enum TargetMode
    {
        SWITCH,
        SINGLE
    }

    public enum Vector
    {
        EYES,
        TORSO,
        FEET
    }

    public enum Priority
    {
        HEALTH,
        DISTANCE,
        ARMOR
    }
}
