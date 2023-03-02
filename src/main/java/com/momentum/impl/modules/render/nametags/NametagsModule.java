package com.momentum.impl.modules.render.nametags;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;

/**
 * @author linus
 * @since 03/01/2023
 */
public class NametagsModule extends Module {

    // nametag options
    public final Option<Boolean> armorOption =
            new Option<>("Armor", "Displays the player's armor", true);
    public final Option<Boolean> enchantmentsOption =
            new Option<>("Enchantments", new String[] {"Enchants", "Enchant"}, "Displays the player's armor enchantments", true);
    public final Option<Boolean> durabilityOption =
            new Option<>("Durability", new String[] {"Dura"}, "Displays the player's armor durability", true);
    public final Option<Boolean> itemNameOption =
            new Option<>("ItemName", "Displays the player's mainhand item's name", true);
    public final Option<Boolean> healthOption =
            new Option<>("Health", "Displays the player's health", true);
    public final Option<Boolean> totemPopsOption =
            new Option<>("TotemPops", new String[] {"Pops"}, "Displays the number of totems that the player popped", true);
    public final Option<Float> scalingOption =
            new Option<>("Scaling", new String[] {"Scale"}, "Nametag scale", 0.001f, 0.003f, 0.01f);
    public final Option<Boolean> invisiblesOption =
            new Option<>("Invisibles", "Apply nametags to invisible players", true);
    public final Option<Boolean> entityIdOption =
            new Option<>("EntityID", "Displays the player's world id", false);
    public final Option<Boolean> gamemodeOption =
            new Option<>("Gamemode", "Displays the player's gamemode", false);
    public final Option<Boolean> pingOption =
            new Option<>("Ping", "Displays the player's server latency", true);
    public final Option<Boolean> borderedOption =
            new Option<>("Bordered", "Nametag background", true);

    // listeners
    public final RenderNametagListener renderNametagListener =
            new RenderNametagListener(this);
    public final RenderWorldListener renderWorldListener =
            new RenderWorldListener(this);

    public NametagsModule() {
        super("Nametags", "Improves player nametags", ModuleCategory.RENDER);

        // options
        associate(
                armorOption,
                enchantmentsOption,
                durabilityOption,
                itemNameOption,
                healthOption,
                totemPopsOption,
                scalingOption,
                invisiblesOption,
                entityIdOption,
                gamemodeOption,
                pingOption,
                borderedOption,
                bind,
                drawn
        );

        // listeners
        associate(
                renderNametagListener,
                renderWorldListener
        );
    }
}
