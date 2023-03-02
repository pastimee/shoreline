package com.momentum.impl.modules.render.nametags;

import com.momentum.api.event.FeatureListener;
import com.momentum.api.util.render.GlUtil;
import com.momentum.api.util.render.InterpolationUtil;
import com.momentum.asm.mixins.vanilla.accessors.IRenderManager;
import com.momentum.impl.events.forge.RenderWorldEvent;
import com.momentum.impl.init.Handlers;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author linus
 * @since 03/01/2023
 */
public class RenderWorldListener extends FeatureListener<NametagsModule, RenderWorldEvent> {

    // item offsets
    private int off;

    // enchant offsets
    private int esize;
    private float eoff;

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderWorldListener(NametagsModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderWorldEvent event) {

        // make sure the render engine exists
        if (mc.renderEngine != null && mc.getRenderManager().options != null) {

            // make sure the view entity exists
            if (mc.getRenderViewEntity() != null) {

                // interpolate the player's position
                Vec3d interpolate = InterpolationUtil.getInterpolatedPosition(mc.getRenderViewEntity(), mc.getRenderPartialTicks());

                // get our render offsets.
                double x = ((IRenderManager) mc.getRenderManager()).getRenderX();
                double y = ((IRenderManager) mc.getRenderManager()).getRenderY();
                double z = ((IRenderManager) mc.getRenderManager()).getRenderZ();

                // render for all players in world
                for (EntityPlayer p : mc.world.playerEntities) {

                    // player info
                    String info = getInfo(p);

                    // make sure player info exists
                    if (info != null) {

                        // interpolate the player's position. if we were to use static positions
                        // the nametags above the player would jitter and would not look good.
                        Vec3d pinterpolate = InterpolationUtil.getInterpolatedPosition(p, mc.getRenderPartialTicks());

                        // width of the background
                        int width = mc.fontRenderer.getStringWidth(info);
                        float hwidth = width / 2.0f;

                        // distance from local player
                        double dx = (interpolate.x - x) - (pinterpolate.x - x);
                        double dy = (interpolate.y - y) - (pinterpolate.y - y);
                        double dz = (interpolate.z - z) - (pinterpolate.z - z);
                        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

                        // figure out the scaling from the player, scaling only starts when the
                        // local player is 8 blocks away from the nametag
                        double sdist = Math.max(dist - 8, 0);
                        double scaling = 0.0245 + (feature.scalingOption.getVal() * sdist);

                        // offset the background and text by player view
                        GlStateManager.pushMatrix();
                        RenderHelper.enableStandardItemLighting();
                        GlStateManager.enablePolygonOffset();
                        GlStateManager.doPolygonOffset(1, -1500000);
                        GlStateManager.disableLighting();
                        GlStateManager.translate(pinterpolate.x - x, ((pinterpolate.y + p.height) + (p.isSneaking() ? 0.22 : 0.25)) - y, pinterpolate.z - z);
                        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
                        GlStateManager.rotate(mc.getRenderManager().playerViewX, (mc.gameSettings.thirdPersonView == 2) ? -1 : 1, 0, 0);
                        GlStateManager.scale(-scaling, -scaling, scaling);
                        GlStateManager.disableDepth();
                        GlStateManager.enableBlend();
                        GlStateManager.enableBlend();

                        // background
                        if (feature.borderedOption.getVal()) {

                            // draw rect behind nametag
                            // GlStateManager.enableBlend();
                            GlUtil.rect(-hwidth - 1, -mc.fontRenderer.FONT_HEIGHT - 2, width, mc.fontRenderer.FONT_HEIGHT + 2, 0x0000005b);
                            // GlStateManager.disableBlend();
                        }

                        // nametag color
                        int color = getColor(p);

                        // draw the info
                        mc.fontRenderer.drawStringWithShadow(info, -hwidth + 1, -mc.fontRenderer.FONT_HEIGHT, color);

                        // item rendering
                        // float by = -mc.fontRenderer.FONT_HEIGHT - (5 + );

                        // display items
                        List<ItemStack> items = new CopyOnWriteArrayList<>();

                        // check if offhand is empty
                        // far left
                        if (!p.getHeldItemOffhand().isEmpty()) {

                            // add player's offhand item to display items
                            items.add(p.getHeldItemOffhand());
                        }

                        // in between
                        for (ItemStack a : p.getArmorInventoryList()) {

                            // check if armor slot is empty
                            if (!a.isEmpty()) {

                                // add all armor pieces to display items
                                items.add(a);
                            }
                        }

                        // check if mainhand is empty
                        // far right
                        if (!p.getHeldItemMainhand().isEmpty()) {

                            // add player's mainhand item to display items
                            items.add(p.getHeldItemMainhand());
                        }

                        // reverse armor list, we want the order to be
                        // mainhand, helmet -> boots, offhand
                        Collections.reverse(items);

                        // enchantment name size
                        esize = 0;

                        // find size of enchantments
                        if (feature.enchantmentsOption.getVal()) {

                            // check each display item's enchantments
                            for (ItemStack i : items) {

                                // enchantment size
                                int size = EnchantmentHelper.getEnchantments(i).size();

                                // check old enchantment size
                                if (size > esize) {

                                    // number of enchants on the item
                                    esize = size;
                                }
                            }
                        }

                        // clamp size
                        esize = Math.max(esize, 4);

                        // check if items is empty
                        if (!feature.armorOption.getVal() || items.isEmpty()) {

                            // no items, render lower
                            esize = 0;
                        }
                        
                        // armor rendering
                        if (feature.armorOption.getVal()) {

                            // render display items
                            off = -8 * items.size();
                            for (ItemStack i : items) {

                                // begin render
                                GlStateManager.pushMatrix();
                                GlStateManager.depthMask(true);
                                GlStateManager.clear(256);

                                // enable item lighting
                                RenderHelper.enableStandardItemLighting();

                                // set item z
                                mc.getRenderItem().zLevel = -150;
                                GlStateManager.disableAlpha();
                                GlStateManager.enableDepth();
                                GlStateManager.disableCull();

                                // render item
                                mc.getRenderItem().renderItemAndEffectIntoGUI(i, off, (int) (-mc.fontRenderer.FONT_HEIGHT + (esize * -4.75f) + 1.0f));
                                mc.getRenderItem().renderItemOverlays(mc.fontRenderer, i, off, (int) (-mc.fontRenderer.FONT_HEIGHT + (esize * -4.75f) + 1.0f));

                                // reset item z
                                mc.getRenderItem().zLevel = 0;

                                // reset lighting
                                RenderHelper.disableStandardItemLighting();
                                GlStateManager.enableCull();
                                GlStateManager.enableAlpha();
                                GlStateManager.disableDepth();
                                GlStateManager.enableDepth();
                                GlStateManager.popMatrix();

                                // display durability of item
                                if (feature.durabilityOption.getVal()) {

                                    // only tools and armor have durability
                                    if (i.getItem().isDamageable()) {

                                        // durability (percent) of the item
                                        int durability = (int) ((i.getMaxDamage() - i.getItemDamage()) / ((float) i.getMaxDamage()) * 100.0f);

                                        // should be above the point
                                        eoff = -mc.fontRenderer.FONT_HEIGHT;

                                        // scale to 1/2
                                        GlStateManager.pushMatrix();
                                        GlStateManager.scale(0.5, 0.5, 0.5);

                                        // rescale position
                                        float sx = (off + 1.0f) * 2;
                                        float sy = (-mc.fontRenderer.FONT_HEIGHT + (esize * -4.75f)) * 2;

                                        // draw durability
                                        mc.fontRenderer.drawStringWithShadow(durability + "%", sx, sy + eoff - (esize == 0 ? 3 : 0), i.getItem().getRGBDurabilityForDisplay(i));

                                        // update offset
                                        eoff += mc.fontRenderer.FONT_HEIGHT - 0.5f;

                                        // reset scale
                                        GlStateManager.scale(2, 2, 2);
                                        GlStateManager.popMatrix();
                                    }
                                }

                                // display enchantments
                                if (feature.enchantmentsOption.getVal()) {

                                    // scale by 1/2
                                    GlStateManager.pushMatrix();
                                    GlStateManager.scale(0.5, 0.5, 0.5);

                                    // mark as god apple
                                    if (i.getItem() instanceof ItemAppleGold) {

                                        // god apple
                                        if (i.hasEffect()) {

                                            // display info
                                            // rescale position
                                            float sx = (off - 1.0f) * 2;
                                            float sy = (-mc.fontRenderer.FONT_HEIGHT + (esize * -4.75f) + 2.0f) * 2;

                                            // draw enchants
                                            mc.fontRenderer.drawStringWithShadow("God", sx, sy + eoff, 0xC34D41);

                                            // update offset
                                            eoff += mc.fontRenderer.FONT_HEIGHT - 0.5f;
                                        }
                                    }

                                    // check if the item is enchanted
                                    else if (i.isItemEnchanted()) {

                                        // enchantment map
                                        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(i);
                                        
                                        // render all enchants
                                        for (Enchantment e : enchants.keySet()) {

                                            // formatted enchantment name
                                            StringBuilder enchant = new StringBuilder();

                                            // enchant name -> translated format
                                            int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(e, i);
                                            
                                            // enchantment translated name
                                            String name = e.getTranslatedName(enchantmentLevel);

                                            // curse of vanishing effect
                                            if (name.contains("Vanish")) {

                                                // format name
                                                enchant.append("Van");
                                            }

                                            // curse of binding effect
                                            else if (name.contains("Bind")) {

                                                // format name
                                                enchant.append("Bind");
                                            }
                                            
                                            // non-curse effect
                                            else {

                                                // cut off last letters
                                                int sub = (enchantmentLevel > 1) ? 2 : 3;
                                                if (name.length() > sub) {
                                                    
                                                    // formatted name
                                                    String fname = name.substring(0, sub);

                                                    // enchant name + lvl
                                                    enchant.append(fname);

                                                    // lvl must be greater than 1 to render
                                                    if (enchantmentLevel > 1) {

                                                        // 32ks
                                                        if (enchantmentLevel > 99) {

                                                            // cutoff
                                                            enchant.append("99+");
                                                        }

                                                        // normal lvl
                                                        else {

                                                            // add enchant level
                                                            enchant.append(enchantmentLevel);
                                                        }
                                                    }
                                                }
                                            }

                                            // rescale position
                                            float sx = (off + 1.0f) * 2;
                                            float sy = (-mc.fontRenderer.FONT_HEIGHT + (esize * -4.75f)) * 2;

                                            // draw enchants
                                            mc.fontRenderer.drawStringWithShadow(enchant.toString(), sx, sy + eoff, -1);

                                            // update offset
                                            eoff += mc.fontRenderer.FONT_HEIGHT - 0.5f;
                                        }
                                    }

                                    // reset scale
                                    GlStateManager.scale(2, 2, 2);
                                    GlStateManager.popMatrix();
                                }

                                // display held item name
                                if (feature.itemNameOption.getVal()) {

                                    // name of the item held in the mainhand
                                    String name = p.getHeldItemMainhand().getDisplayName();

                                    // scale by 1/2
                                    GlStateManager.pushMatrix();
                                    GlStateManager.scale(0.5, 0.5, 0.5);

                                    // width of the item name
                                    float iwidth = mc.fontRenderer.getStringWidth(name) * 0.5f;
                                    float ihwidth = iwidth / 2.0f;

                                    // scaled size
                                    int scaledSize = esize + 1;

                                    // no armor
                                    if (esize == 0) {
                                        scaledSize = 1;
                                    }

                                    // give space for dura
                                    if (feature.durabilityOption.getVal()) {
                                        scaledSize += 1;
                                    }

                                    // rescale position
                                    float sx = (-ihwidth + 1) * 2;
                                    float sy = (-mc.fontRenderer.FONT_HEIGHT + (scaledSize * -4.75f) - (esize == 0 ? 3.0f : 0.0f)) * 2;

                                    // draw item name
                                    mc.fontRenderer.drawStringWithShadow(name, sx, sy, -1);
                                    
                                    // reset scale
                                    GlStateManager.scale(2, 2, 2);
                                    GlStateManager.popMatrix();
                                }

                                // offset
                                off += 16;
                            }
                        }

                        // reset the background and text by player view
                        GlStateManager.enableDepth();
                        GlStateManager.disableBlend();
                        GlStateManager.disablePolygonOffset();
                        GlStateManager.doPolygonOffset(1, 1500000);
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
    }

    /**
     * Gets all player info
     *
     * @param in The player
     * @return Player info
     */
    private String getInfo(EntityPlayer in) {

        if (in != null) {

            // the player's info
            StringBuilder info = new StringBuilder();

            // add the player's name
            info.append(in.getName())
                    .append(" ");

            // add the player's entity ID
            if (feature.entityIdOption.getVal()) {

                // entity ID
                info.append("ID: ")
                        .append(in.getEntityId())
                        .append(" ");
            }

            // add the player's gamemode
            if (feature.gamemodeOption.getVal()) {

                // first letter of gamemode
                if (in.isCreative()) {
                    info.append("[C] ");
                }

                // collision, use I
                else if (in.isSpectator()) {
                    info.append("[I] ");
                }

                // adventure and survival are both listed as S
                else {
                    info.append("[S] ");
                }
            }

            // add the player's ping
            if (feature.pingOption.getVal()) {

                // check if connection exist
                if (mc.getConnection() != null) {

                    // connection info
                    NetworkPlayerInfo connection = mc.getConnection().getPlayerInfo(in.getUniqueID());

                    // latency in ms
                    info.append(connection.getResponseTime())
                            .append("ms ");
                }
            }

            // add the player's health
            if (feature.healthOption.getVal()) {

                // player health with absorption
                double health = Math.ceil(in.getHealth() + in.getAbsorptionAmount());

                // health color
                TextFormatting hcolor;

                // green
                if (health > 18) {
                    hcolor = TextFormatting.GREEN;
                }

                // dark green
                else if (health > 16) {
                    hcolor = TextFormatting.DARK_GREEN;
                }

                // yellow
                else if (health > 12) {
                    hcolor = TextFormatting.YELLOW;
                }

                // orange
                else if (health > 8) {
                    hcolor = TextFormatting.GOLD;
                }

                // red
                else if (health > 4) {
                    hcolor = TextFormatting.RED;
                }

                // dark red
                else {
                    hcolor = TextFormatting.DARK_RED;
                }

                // health (with indicator color)
                info.append(hcolor)
                        .append((int) health)
                        .append(" ");
            }

            // add the player's totem pops
            if (feature.totemPopsOption.getVal()) {

                // player's totem pops
                int pops = Handlers.POP_HANDLER.getPops(in);

                // render check
                // only render if player has popped at least once
                if (pops > 0) {

                    // pop color
                    // green
                    TextFormatting pcolor = TextFormatting.GREEN;

                    // dark green
                    if (pops > 1) {
                        pcolor = TextFormatting.DARK_GREEN;
                    }

                    // yellow
                    if (pops > 2) {
                        pcolor = TextFormatting.YELLOW;
                    }

                    // orange
                    if (pops > 3) {
                        pcolor = TextFormatting.GOLD;
                    }

                    // red
                    if (pops > 4) {
                        pcolor = TextFormatting.RED;
                    }

                    // dark red
                    if (pops > 5) {
                        pcolor = TextFormatting.DARK_RED;
                    }

                    // pops (with indicator color)
                    info.append(pcolor)
                            .append("-")
                            .append(pops)
                            .append(" ");
                }
            }

            // info
            return info.toString();
        }

        // no info
        return null;
    }

    /**
     * Gets the player's nametag color
     *
     * @param in The player
     * @return Player's nametag color
     */
    @SuppressWarnings("ConstantConditions")
    private int getColor(EntityPlayer in) {

        // red
        if (in.isInvisible()) {
            return 0xffff2500;
        }

        // fake player, reddish pink
        if (mc.getConnection() != null
                && mc.getConnection().getPlayerInfo(in.getUniqueID()) == null) {
            return 0xffef0147;
        }

        // gold
        if (in.isSneaking()) {
            return 0xffff9900;
        }

        // white
        return 0xffffffff;
    }
}
