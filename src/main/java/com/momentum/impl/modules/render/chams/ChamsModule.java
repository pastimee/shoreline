package com.momentum.impl.modules.render.chams;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import com.momentum.api.util.entity.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

/**
 * @author linus
 * @since 03/11/2023
 */
public class ChamsModule extends Module {

    // render options
    public final Option<ChamsMode> modeOption =
            new Option<>("Mode", "Render mode", ChamsMode.FILL);
    public final Option<Boolean> selfOption =
            new Option<>("Self", "Render local player", false);
    public final Option<Boolean> playersOption =
            new Option<>("Players","Render players", true);
    public final Option<Boolean> monstersOption =
            new Option<>("Monsters", "Render monsters", true);
    public final Option<Boolean> animalsOption =
            new Option<>("Animals", "Render animals", true);
    public final Option<Boolean> othersOption =
            new Option<>("Others", "Render others", true);
    public final Option<Boolean> textureOption =
            new Option<>("Texture","Render model texture", false);
    public final Option<Boolean> xqzOption =
            new Option<>("XQZ","Render through walls color", false);
    public final Option<Boolean> shineOption =
            new Option<>("Shine","Render enchantment shine", false);
    public final Option<Float> lineWidthOption =
            new Option<>("LineWidth","Render line width", 1.0f, 1.0f, 3.0f);


    // color options
    public final Option<Color> colorOption =
            new Option<>("Color", "Render color", new Color(252, 3, 82, 70));
    public final Option<Color> xqzColorOption =
            new Option<>("XQZColor", "XQZ render color", new Color(252, 3, 82, 70));

    // listeners
    public final RenderModelListener renderModelListener =
            new RenderModelListener(this);
    public final RenderEnderCrystalListener renderEnderCrystalListener =
            new RenderEnderCrystalListener(this);

    public ChamsModule() {
        super("Chams", "Renders entities through walls", ModuleCategory.RENDER);

        // options
        associate(
                modeOption,
                selfOption,
                playersOption,
                monstersOption,
                animalsOption,
                othersOption,
                textureOption,
                xqzOption,
                shineOption,
                lineWidthOption,
                bind,
                drawn
        );

        // listeners
        associate(
                renderModelListener,
                renderEnderCrystalListener
        );
    }

    /**
     * Checks entity type
     *
     * @param in The entity
     * @return Whether the entity is a valid entity type
     */
    protected boolean isValidEntity(Entity in) {

        // entity check
        return in == mc.player && selfOption.getVal()
                || (in instanceof EntityPlayer && in != mc.player) && playersOption.getVal()
                || EntityUtil.isHostileMob(in) && monstersOption.getVal()
                || EntityUtil.isPassiveMob(in) && animalsOption.getVal();
    }
}
