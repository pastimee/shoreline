package net.shoreline.client.impl.module.combat;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.RotationModule;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.shoreline.client.util.world.EntityUtil;

import java.util.List;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class AutoPotModule extends RotationModule
{
    //
    Config<Float> healthConfig = new NumberConfig<>("Health", "The minimum " +
            "health to start using potions", 1.0f, 10.0f, 19.0f);
    Config<Float> delayConfig = new NumberConfig<>("Delay", "The delay " +
            "between using potions", 0.01f, 0.70f, 1.0f);
    Config<Boolean> handFixConfig = new BooleanConfig("1.9Fix", "Fixes the " +
            "hand when using potions in versions 1.9 or below", true);
    Config<Integer> slotConfig = new NumberConfig<>("Slot", "The slot of the " +
            "potion in the hotbar to swap with", 0, 1, 8);
    Config<Boolean> onGroundConfig = new BooleanConfig("OnGround", "Attempts " +
            "to fix the onGround state before using potions", false);
    //
    private double groundX;
    private double groundY;
    private double groundZ;
    private int groundCancelTicks;
    private boolean potion;
    //
    private final Timer potionTimer = new CacheTimer();

    /**
     *
     */
    public AutoPotModule()
    {
        super("AutoPot", "Automatically throws beneficial potions",
                ModuleCategory.COMBAT);
    }

    /**
     *
     */
    @Override
    public void onEnable()
    {
        groundCancelTicks = 0;
    }

    /**
     *
     * @param event
     */
    @EventListener
    public void onPlayerUpdate(PlayerUpdateEvent event)
    {
        if (!mc.player.isOnGround() && onGroundConfig.getValue())
        {
            return;
        }
        if (event.getStage() == EventStage.PRE)
        {
            float health = EntityUtil.getHealth(mc.player);
            if (health <= healthConfig.getValue()
                    && potionTimer.passed(delayConfig.getValue() * 1000.0f))
            {
                int instantHealth = getInstantHealthPotion();
                if (instantHealth != -1 && mc.player.verticalCollision
                        && !Modules.SPEED.isEnabled() && !onGroundConfig.getValue())
                {
                    Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(),
                            mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), -90.0f, true));
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
                            instantHealth, slotConfig.getValue(),
                            SlotActionType.SWAP, mc.player);
                    potionTimer.reset();
                    Managers.NETWORK.sendPacket(new UpdateSelectedSlotC2SPacket(slotConfig.getValue()));
                    if (handFixConfig.getValue())
                    {
                        Managers.NETWORK.sendSequencedPacket(id ->
                                new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
                    }
                    else
                    {
                        Managers.NETWORK.sendSequencedPacket(id ->
                                new PlayerInteractItemC2SPacket(Hand.OFF_HAND, id));
                    }
                    Managers.NETWORK.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
                    Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(),
                            mc.player.getY() + 0.42, mc.player.getZ(), true));
                    Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(),
                            mc.player.getY() + 0.75, mc.player.getZ(), true));
                    Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(),
                            mc.player.getY() + 1.0, mc.player.getZ(), true));
                    Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(),
                            mc.player.getY() + 1.16, mc.player.getZ(), true));
                    Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(),
                            mc.player.getY() + 1.24, mc.player.getZ(), true));
                    groundX = mc.player.getX();
                    groundY = mc.player.getY() + 1.24;
                    groundZ = mc.player.getZ();
                    groundCancelTicks = 5;
                }
                else
                {
                    setRotation(mc.player.getYaw(), 90.0f);
                    potion = true;
                    // potionTimer.reset();
                }
                if (groundCancelTicks >= 0)
                {
                    event.cancel();
                }
                if (groundCancelTicks == 0)
                {
                    Managers.MOVEMENT.setMotionXZ(0.0, 0.0);
                    mc.player.updatePosition(groundX, groundY, groundZ);
                    Managers.MOVEMENT.setMotionY(-0.08);
                }
                groundCancelTicks--;
            }
        }
        else if (event.getStage() == EventStage.POST && potion)
        {
            int instantHealth = getInstantHealthPotion();
            if (instantHealth == -1)
            {
                return;
            }
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
                    instantHealth, slotConfig.getValue(),
                    SlotActionType.SWAP, mc.player);
            potionTimer.reset();
            Managers.NETWORK.sendPacket(new UpdateSelectedSlotC2SPacket(slotConfig.getValue()));
            if (handFixConfig.getValue())
            {
                Managers.NETWORK.sendSequencedPacket(id ->
                        new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
            }
            else
            {
                Managers.NETWORK.sendSequencedPacket(id ->
                        new PlayerInteractItemC2SPacket(Hand.OFF_HAND, id));
            }
            Managers.NETWORK.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
            potion = false;
        }
    }

    /**
     *
     * @return
     */
    private int getInstantHealthPotion()
    {
        int potionSlot = -1;
        for (int i = 0; i < 45; i++)
        {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty())
            {
                continue;
            }
            if (stack.getItem() instanceof PotionItem
                    && PotionUtil.getPotionEffects(stack) != null)
            {
                List<StatusEffectInstance> effects =
                        PotionUtil.getPotionEffects(stack);
                for (StatusEffectInstance effect : effects)
                {
                    if (effect.getEffectType() == StatusEffects.INSTANT_HEALTH)
                    {
                        potionSlot = i;
                        break;
                    }
                }
            }
        }
        return potionSlot;
    }
}
