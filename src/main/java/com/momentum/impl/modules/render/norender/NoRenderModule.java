package com.momentum.impl.modules.render.norender;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;

/**
 * @author linus
 * @since 02/13/2023
 */
public class NoRenderModule extends Module {

    // dropped item options
    public final Option<NoItems> noItemsOption =
            new Option<>("NoItems", "Removes dropped item rendering", NoItems.OFF);

    // overlay options
    public final Option<Boolean> fireOption =
            new Option<>("Fire", "Removes the fire overlay", true);
    public final Option<Boolean> hurtCameraOption =
            new Option<>("HurtCamera", new String[] {"NoHurtCam"}, "Removes the hurt camera effect", true);
    public final Option<Boolean> bossOverlayOption =
            new Option<>("BossOverlay", "Removes the boss bar overlay", true);
    public final Option<Boolean> pumpkinOverlayOption =
            new Option<>("PumpkinOverlay", "Removes the pumpkin overlay", true);
    public final Option<Boolean> blockOverlayOption =
            new Option<>("BlockOverlay", "Removes the block overlay", true);
    public final Option<Boolean> waterOverlayOption =
            new Option<>("WaterOverlay", "Removes the water overlay", true);

    // render options
    public final Option<Boolean> blindnessOption =
            new Option<>("Blindness", "Removes blindness effects", true);
    public final Option<Boolean> totemAnimationOption =
            new Option<>("TotemAnimation", "Removes the totem pop animation", false);
    public final Option<Boolean> skylightOption =
            new Option<>("Skylight", "Removes sky light rendering", true);
    public final Option<Boolean> signTextOption =
            new Option<>("SignText", "Removes sign text rendering", false);
    public final Option<Boolean> mapOption =
            new Option<>("Map", "Removes map rendering", false);
    public final Option<Boolean> beaconBeamOption =
            new Option<>("BeaconBeam", "Removes beacon beam rendering", false);
    public final Option<Boolean> witherSkullOption =
            new Option<>("WitherSkull", "Removes flying wither skulls rendering", false);
    public final Option<Boolean> barrierOption =
            new Option<>("Barrier", "Removes barrier rendering", true);
    public final Option<Boolean> armorOption =
            new Option<>("Armor", "Removes armor rendering", false);
    public final Option<Boolean> explosionsOption =
            new Option<>("Explosions", "Removes the explosion particle effect", true);

    // listeners
    public final TickListener tickListener =
            new TickListener(this);
    public final RenderEntityItemListener renderEntityItemListener =
            new RenderEntityItemListener(this);
    public final RenderHudOverlayListener renderHudOverlayListener =
            new RenderHudOverlayListener(this);
    public final RenderPumpkinOverlayListener renderPumpkinOverlayListener =
            new RenderPumpkinOverlayListener(this);
    public final RenderBossOverlayListener renderBossOverlayListener =
            new RenderBossOverlayListener(this);
    public final RenderHurtCameraListener renderHurtCameraListener =
            new RenderHurtCameraListener(this);
    public final RenderBlindnessListener renderBlindnessListener =
            new RenderBlindnessListener(this);
    public final RenderItemActivationListener renderItemActivationListener =
            new RenderItemActivationListener(this);
    public final RenderSkyLightListener renderSkyLightListener =
            new RenderSkyLightListener(this);
    public final RenderSignTextListener renderSignTextListener =
            new RenderSignTextListener(this);
    public final RenderMapListener renderMapListener =
            new RenderMapListener(this);
    public final RenderWitherSkullListener renderWitherSkullListener =
            new RenderWitherSkullListener(this);
    public final RenderBeaconBeamListener renderBeaconBeamListener =
            new RenderBeaconBeamListener(this);
    public final RenderArmorModelListener renderArmorModelListener =
            new RenderArmorModelListener(this);
    public final RenderBarrierListener renderBarrierListener =
            new RenderBarrierListener(this);
    public final RenderExplosionListener renderExplosionListener =
            new RenderExplosionListener(this);

    public NoRenderModule() {
        super("NoRender", "Prevents certain elements from rendering", ModuleCategory.RENDER);

        // options
        associate(
                noItemsOption,
                fireOption,
                hurtCameraOption,
                bossOverlayOption,
                pumpkinOverlayOption,
                blockOverlayOption,
                waterOverlayOption,
                blindnessOption,
                totemAnimationOption,
                skylightOption,
                signTextOption,
                mapOption,
                beaconBeamOption,
                witherSkullOption,
                barrierOption,
                armorOption,
                explosionsOption,
                bind,
                drawn
        );

        // listeners
        associate(
                tickListener,
                renderEntityItemListener,
                renderHudOverlayListener,
                renderPumpkinOverlayListener,
                renderBossOverlayListener,
                renderHurtCameraListener,
                renderBlindnessListener,
                renderItemActivationListener,
                renderSkyLightListener,
                renderSignTextListener,
                renderMapListener,
                renderWitherSkullListener,
                renderBeaconBeamListener,
                renderArmorModelListener,
                renderBarrierListener,
                renderExplosionListener
        );
    }
}
