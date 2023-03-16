package com.momentum.impl.modules.client.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.momentum.Momentum;
import com.momentum.api.event.FeatureListener;
import com.momentum.api.module.Module;
import com.momentum.api.util.render.Formatter;
import com.momentum.asm.mixins.vanilla.accessors.IMinecraft;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.asm.mixins.vanilla.accessors.ITimer;
import com.momentum.impl.events.forge.RenderTextOverlayEvent;
import com.momentum.impl.init.Handlers;
import com.momentum.impl.init.Modules;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @author linus
 * @since 02/08/2023
 */
public class RenderTextOverlayListener extends FeatureListener<HudModule, RenderTextOverlayEvent> {

    // corners
    private float topLeft;
    private float topRight;
    private float bottomLeft;
    private float bottomRight;

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected RenderTextOverlayListener(HudModule feature) {
        super(feature);
    }

    @Override
    public void invoke(RenderTextOverlayEvent event) {

        // null check
        if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
            return;
        }

        // resolutions
        ScaledResolution resolution = new ScaledResolution(mc);
        int resWidth = resolution.getScaledWidth();
        int resHeight = resolution.getScaledHeight();

        // corner positions
        topLeft = 2;
        topRight = 2;
        bottomLeft = resHeight - 11;
        bottomRight = resHeight - 11;

        // effect hud is displayed
        if (feature.potionHudOption.getVal() == PotionHud.MOVE) {

            // check if potion effects are active (i.e. hud is renderingOption)
            if (!mc.player.getActivePotionEffects().isEmpty()) {

                // move top right corner to account for effect hud
                offset(Corner.TOP_RIGHT, 27);
            }
        }

        // chat gui is displayed
        if (mc.currentScreen instanceof GuiChat) {

            // offset to account for chat box height
            offset(Corner.BOTTOM_LEFT, 14);
            offset(Corner.BOTTOM_RIGHT, 14);
        }

        // display client name and version
        if (feature.watermarkOption.getVal()) {

            // build display string
            StringBuilder watermarkString =
                    new StringBuilder()
                    .append(Momentum.CLIENT_NAME)
                    .append(TextFormatting.WHITE)
                    .append(" ")
                    .append(Momentum.MOD_VERSION);

            // draw to screen
            mc.fontRenderer.drawStringWithShadow(watermarkString.toString(), 2, topLeft, Modules.COLOR_MODULE.getColorInt());
            offset(Corner.TOP_LEFT);
        }

        // active modules
        if (feature.arraylistOption.getVal()) {

            // sorted list of modules
            Collection<Module> modules = Momentum.MODULE_REGISTRY.getData();

            // sort
            modules = modules.stream()
                    .filter(Module::isEnabled)
                    .sorted(Comparator.comparing(module -> {

                        // formatted module name
                        StringBuilder formatted = new StringBuilder(module.getName());

                        // module contains arraylist data
                        if (!module.getData().equalsIgnoreCase("")) {

                            // add module data
                            formatted.append(TextFormatting.GRAY)
                                    .append(" [")
                                    .append(TextFormatting.WHITE)
                                    .append(module.getData())
                                    .append(TextFormatting.GRAY)
                                    .append("]");
                        }

                        // name length
                        if (feature.orderingOption.getVal() == Ordering.LENGTH) {

                            // string width
                            return mc.fontRenderer.getStringWidth(formatted.toString()) * -1;
                        }

                        // alphabetical order
                        else {

                            // ASCII value
                            return (int) module.getName().charAt(0);
                        }
                    }))
                    .collect(Collectors.toList());

            // render arraylist
            for (Module module : modules) {

                // ignore modules that aren't drawn to the arraylist
                if (module.isDrawn()) {

                    // formatted module name
                    StringBuilder formatted = new StringBuilder(module.getName());

                    // module contains arraylist data
                    if (!module.getData().equalsIgnoreCase("")) {

                        // add module data
                        formatted.append(TextFormatting.GRAY)
                                .append(" [")
                                .append(TextFormatting.WHITE)
                                .append(module.getData())
                                .append(TextFormatting.GRAY)
                                .append("]");
                    }
                    // width of the element
                    float width = mc.fontRenderer.getStringWidth(formatted.toString());

                    // draw string
                    mc.fontRenderer.drawStringWithShadow(formatted.toString(), resWidth - 1 - width, feature.renderingOption.getVal() == Rendering.UP ? topRight : bottomRight, Modules.COLOR_MODULE.getColorInt());

                    // offset bottom right
                    if (feature.renderingOption.getVal() == Rendering.UP) {
                        offset(Corner.TOP_RIGHT);
                    }

                    // offset top right
                    else {
                        offset(Corner.BOTTOM_RIGHT);
                    }
                }
            }
        }

        // display active potion effects
        if (feature.potionEffectsOption.getVal()) {

            // active potions
            for (PotionEffect p : mc.player.getActivePotionEffects()) {

                // potion name
                String name = I18n.format(p.getEffectName());

                // potion formatted
                StringBuilder potionString =
                        new StringBuilder(name);

                // potion effect amplifier
                int amplifier = p.getAmplifier() + 1;

                // potion formatted
                potionString.append(" ")
                        .append(amplifier > 1 ? amplifier + " " : "")
                        .append(ChatFormatting.WHITE)
                        .append(Potion.getPotionDurationString(p, 1f));

                // width of the element
                float width = mc.fontRenderer.getStringWidth(potionString.toString());

                // draw string
                mc.fontRenderer.drawStringWithShadow(potionString.toString(), resWidth - 1 - width, feature.renderingOption.getVal() == Rendering.UP ? bottomRight : topRight, p.getPotion().getLiquidColor());

                // offset bottom right
                if (feature.renderingOption.getVal() == Rendering.UP) {
                    offset(Corner.BOTTOM_RIGHT);
                }

                // offset top right
                else {
                    offset(Corner.TOP_RIGHT);
                }
            }
        }

        // display server's "brand"
        if (feature.serverBrandOption.getVal()) {

            // formatted string
            StringBuilder brandString =
                    new StringBuilder(mc.player.getServerBrand());

            // width of the element
            float width = mc.fontRenderer.getStringWidth(brandString.toString());

            // draw string
            mc.fontRenderer.drawStringWithShadow(brandString.toString(), resWidth - 1 - width, feature.renderingOption.getVal() == Rendering.UP ? bottomRight : topRight, Modules.COLOR_MODULE.getColorInt());

            // offset bottom right
            if (feature.renderingOption.getVal() == Rendering.UP) {
                offset(Corner.BOTTOM_RIGHT);
            }

            // offset top right
            else {
                offset(Corner.TOP_RIGHT);
            }
        }

        // display player movement speed
        if (feature.speedOption.getVal()) {

            // position diffs
            double xdiff = mc.player.posX - mc.player.prevPosX;
            double zdiff = mc.player.posZ - mc.player.prevPosZ;

            // distance travelled
            double dist = MathHelper.sqrt(xdiff * xdiff + zdiff * zdiff);

            // timer tick length
            float tickLength = 50f / ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();

            // speed in kmh
            String speed = String.valueOf((dist / 1000) / (0.05f / 3600) * (tickLength));

            // formatted string
            StringBuilder speedString =
                    new StringBuilder()
                            .append("Speed ")
                            .append(TextFormatting.WHITE)
                            .append(speed, 0, speed.indexOf(".") + 2)
                            .append("k/mh");

            // width of the element
            float width = mc.fontRenderer.getStringWidth(speedString.toString());

            // draw string
            mc.fontRenderer.drawStringWithShadow(speedString.toString(), resWidth - 1 - width, feature.renderingOption.getVal() == Rendering.UP ? bottomRight : topRight, Modules.COLOR_MODULE.getColorInt());

            // offset bottom right
            if (feature.renderingOption.getVal() == Rendering.UP) {
                offset(Corner.BOTTOM_RIGHT);
            }

            // offset top right
            else {
                offset(Corner.TOP_RIGHT);
            }
        }

        // display server response time
        if (feature.pingOption.getVal()) {

            // server response time
            int responseTime = 0;

            // check if the player connection exists
            if (mc.getConnection() != null) {

                // player -> server response time
                NetworkPlayerInfo networkInfo = mc.getConnection().getPlayerInfo(mc.player.getUniqueID());

                // network info is available
                if (networkInfo != null) {

                    // update server response time
                    responseTime = networkInfo.getResponseTime();
                }
            }

            // formatted string
            StringBuilder pingString =
                    new StringBuilder()
                            .append("Ping ")
                            .append(TextFormatting.WHITE)
                            .append(responseTime)
                            .append("ms");

            // width of the element
            float width = mc.fontRenderer.getStringWidth(pingString.toString());

            // draw string
            mc.fontRenderer.drawStringWithShadow(pingString.toString(), resWidth - 1 - width, feature.renderingOption.getVal() == Rendering.UP ? bottomRight : topRight, Modules.COLOR_MODULE.getColorInt());

            // offset bottom right
            if (feature.renderingOption.getVal() == Rendering.UP) {
                offset(Corner.BOTTOM_RIGHT);
            }

            // offset top right
            else {
                offset(Corner.TOP_RIGHT);
            }
        }

        // display the server TPS
        if (feature.tpsOption.getVal()) {

            // tps values
            String tps = String.valueOf(Handlers.TICK_HANDLER.getTps());
            String last = String.valueOf(Handlers.TICK_HANDLER.getLast());

            // index of decimal places
            int tpsDecimal = tps.indexOf(".");
            int lastDecimal = tps.indexOf(".");

            // build display string
            StringBuilder tpsString =
                    new StringBuilder("TPS ")
                            .append(TextFormatting.WHITE)
                            .append(tps, 0, tpsDecimal + (tps.charAt(tpsDecimal + 1) == '0' ? 2 : 3))
                            .append(" ")
                            .append(TextFormatting.GRAY)
                            .append("[")
                            .append(TextFormatting.WHITE)
                            .append(last, 0, lastDecimal + (last.charAt(lastDecimal + 1) == '0' ? 2 : 3))
                            .append(TextFormatting.GRAY)
                            .append("]");

            // width of the element
            float width = mc.fontRenderer.getStringWidth(tpsString.toString());

            // draw to HUD
            mc.fontRenderer.drawStringWithShadow(tpsString.toString(), resWidth - 1 - width, feature.renderingOption.getVal() == Rendering.UP ? bottomRight : topRight, Modules.COLOR_MODULE.getColorInt());

            // offset bottom right
            if (feature.renderingOption.getVal() == Rendering.UP) {
                offset(Corner.BOTTOM_RIGHT);
            }

            // offset top right
            else {
                offset(Corner.TOP_RIGHT);
            }
        }

        // displays game FPS
        if (feature.fpsOption.getVal()) {

            // formatted string
            StringBuilder fpsString =
                    new StringBuilder()
                            .append("FPS ")
                            .append(TextFormatting.WHITE)
                            .append(Minecraft.getDebugFPS());

            // width of the element
            float width = mc.fontRenderer.getStringWidth(fpsString.toString());

            // draw string
            mc.fontRenderer.drawStringWithShadow(fpsString.toString(), resWidth - 1 - width, feature.renderingOption.getVal() == Rendering.UP ? bottomRight : topRight, Modules.COLOR_MODULE.getColorInt());

            // offset bottom right
            if (feature.renderingOption.getVal() == Rendering.UP) {
                offset(Corner.BOTTOM_RIGHT);
            }

            // offset top right
            else {
                offset(Corner.TOP_RIGHT);
            }
        }

        // display player coordinates in the world
        if (feature.coordinatesOption.getVal()) {

            // overworld pos
            String x = String.valueOf(mc.player.posX);
            String y = String.valueOf(mc.player.posY);
            String z = String.valueOf(mc.player.posZ);

            // format coordinates
            StringBuilder coordinateString =
                    new StringBuilder()
                            .append("XYZ ")
                            .append(TextFormatting.WHITE)
                            .append(x, 0, x.indexOf(".") + 2) // overworld
                            .append(", ")
                            .append(y, 0, y.indexOf(".") + 2)
                            .append(", ")
                            .append(z, 0, z.indexOf(".") + 2)
                            .append(TextFormatting.RESET);

            // display players coordinates in the nether/overworld
            if (feature.netherCoordinatesOption.getVal()) {

                // checks if the player is in the nether
                boolean nether = mc.world.getBiome(mc.player.getPosition()).getBiomeName().equalsIgnoreCase("Hell");

                // nether pos
                String nx = String.valueOf(nether ? mc.player.posX * 8 : mc.player.posX / 8);
                String nz = String.valueOf(nether ? mc.player.posZ * 8 : mc.player.posZ / 8);

                // format nether coordinates
                coordinateString
                        .append(" [")
                        .append(TextFormatting.WHITE)
                        .append(nx, 0, nx.indexOf(".") + 2) // nether
                        .append(", ")
                        .append(nz, 0, nx.indexOf(".") + 2)
                        .append(TextFormatting.RESET)
                        .append("]");
            }

            // draw string
            mc.fontRenderer.drawStringWithShadow(coordinateString.toString(), 2, bottomLeft, Modules.COLOR_MODULE.getColorInt());
            offset(Corner.BOTTOM_LEFT);
        }

        // display player's axis direction
        if (feature.directionOption.getVal()) {

            // facing direction
            EnumFacing direction = mc.player.getHorizontalFacing();

            // axis direction (i.e. pos or neg)
            AxisDirection axisDirection = direction.getAxisDirection();

            // formatted string
            StringBuilder directionString =
                    new StringBuilder()
                            .append(Formatter.capitalise(direction.getName()))
                            .append(" [")
                            .append(TextFormatting.WHITE)
                            .append(Formatter.formatEnum(direction.getAxis()))
                            .append(axisDirection.equals(AxisDirection.POSITIVE) ? "+" : "-")
                            .append(TextFormatting.RESET)
                            .append("]");

            // draw string
            mc.fontRenderer.drawStringWithShadow(directionString.toString(), 2, bottomLeft, Modules.COLOR_MODULE.getColorInt());
            offset(Corner.BOTTOM_LEFT);
        }

        // display player's equipped armor
        if (feature.armorOption.getVal()) {

            // armor render offset
            // display each armor piece
            int off = 0;
            for (ItemStack armor : mc.player.inventory.armorInventory) {

                // check if armor exists
                if (!armor.isEmpty()) {

                    // y offset
                    int y = 0;

                    // if we're in the water, then the armor should render above the bubbles
                    if (mc.player.isInsideOfMaterial(Material.WATER)) {
                        y = 10;
                    }

                    // if we're in creative, we have no hunger bar, so we should render lower
                    if (mc.player.capabilities.isCreativeMode) {
                        y = -15;
                    }

                    // start
                    GlStateManager.pushMatrix();
                    RenderHelper.enableGUIStandardItemLighting();

                    // position
                    int x = (resWidth / 2);
                    int xoff = (9 - off) * 16;

                    // render item
                    mc.getRenderItem().zLevel = 200;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(armor,  x + xoff - 78, resHeight - y - 55);
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, armor, x + xoff - 78, resHeight - y - 55, "");
                    mc.getRenderItem().zLevel = 0;

                    // reset
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.popMatrix();
                }

                // update armor offset
                off++;
            }
        }
    }

    /**
     * Offsets a given corner
     *
     * @param corner The corner to offset
     */
    public void offset(Corner corner) {

        // default element height
        offset(corner, 10);
    }

    /**
     * Offsets a given corner by the given value
     *
     * @param corner The corner to offset
     * @param val The value
     */
    public void offset(Corner corner, int val) {

        // top left
        if (corner == Corner.TOP_LEFT) {

            // add value to corner y position because elements are added top to bottom
            topLeft += val;
        }

        // top right
        else if (corner == Corner.TOP_RIGHT) {

            // add value to corner y position because elements are added top to bottom
            topRight += val;
        }

        // bottom left
        else if (corner == Corner.BOTTOM_LEFT) {

            // subtract value to corner y position because elements are added bottom to top
            bottomLeft -= val;
        }

        // bottom right
        else if (corner == Corner.BOTTOM_RIGHT) {

            // subtract value to corner y position because elements are added bottom to top
            bottomRight -= val;
        }
    }

    // corners
    public enum Corner {

        /**
         * Top left corner
         */
        TOP_LEFT,

        /**
         * Top right corner
         */
        TOP_RIGHT,

        /**
         * Bottom left corner
         */
        BOTTOM_LEFT,

        /**
         * Bottom right corner
         */
        BOTTOM_RIGHT,
    }
}
