package com.momentum.impl.modules.client.hud;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;

/**
 * @author linus
 * @since 02/08/2023
 */
public class HudModule extends Module {

    // hud options
    public final Option<Boolean> watermarkOption =
            new Option<>("Watermark", "Displays watermark", true);
    public final Option<Boolean> directionOption =
            new Option<>("Direction", "Displays facing direction", true);
    public final Option<Boolean> armorOption =
            new Option<>("Armor", "Displays equipped armor", true);
    public final Option<Boolean> potionEffectsOption =
            new Option<>("PotionEffects", "Displays active potion effects", true);
    public final Option<PotionHud> potionHudOption =
            new Option<>("PotionHud", "Displays active potion effects", PotionHud.HIDE);
    public final Option<Boolean> serverBrandOption =
            new Option<>("ServerBrand", "Displays server brand", true);
    public final Option<Boolean> tpsOption =
            new Option<>("TPS", "Displays server ticks per second", true);
    public final Option<Boolean> fpsOption =
            new Option<>("FPS", "Displays game FPS", true);
    public final Option<Boolean> speedOption =
            new Option<>("Speed", "Displays player move speed", true);
    public final Option<Boolean> pingOption =
            new Option<>("Ping", "Displays player ping in ms", true);
    public final Option<Ordering> orderingOption =
            new Option<>("Ordering", "Arraylist ordering type", Ordering.LENGTH);
    public final Option<Boolean> coordinatesOption =
            new Option<>("Coordinates", "Displays overworld coordinates", true);
    public final Option<Boolean> netherCoordinatesOption =
            new Option<>("NetherCoordinates", "Displays nether coordinates", true);
    public final Option<Boolean> durabilityOption =
            new Option<>("Durability", "Displays held item's durability", true);
    public final Option<Boolean> arraylistOption =
            new Option<>("Arraylist", "Displays list of enabled modules", true);
    public final Option<Rendering> renderingOption =
            new Option<>("Rendering", "Arraylist rendering position", Rendering.UP);

    // listeners
    public final RenderTextOverlayListener renderTextOverlayListener
            = new RenderTextOverlayListener(this);
    public final RenderPotionOverlayListener renderPotionOverlayListener
            = new RenderPotionOverlayListener(this);

    public HudModule() {
        super("Hud", "Heads up display", ModuleCategory.CLIENT);

        // options
        associate(
                watermarkOption,
                directionOption,
                armorOption,
                potionEffectsOption,
                potionHudOption,
                serverBrandOption,
                tpsOption,
                fpsOption,
                speedOption,
                pingOption,
                orderingOption,
                coordinatesOption,
                netherCoordinatesOption,
                durabilityOption,
                arraylistOption,
                renderingOption,
                bind,
                drawn
        );

        // listeners
        associate(
                renderTextOverlayListener,
                renderPotionOverlayListener
        );

        // default hidden
        draw(false);
    }
}
