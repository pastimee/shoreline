package com.momentum.impl.modules.combat.criticals;

import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import com.momentum.api.module.ModuleCategory;
import com.momentum.api.util.render.Formatter;
import com.momentum.api.util.time.Timer;

/**
 * @author linus
 * @since 02/20/2023
 */
public class CriticalsModule extends Module {

    // crits options
    public final Option<CritsMode> modeOption =
            new Option<>("Mode", "Mode for packets", CritsMode.PACKET);

    // listeners
    public final OutboundPacketListener outboundPacketListener =
            new OutboundPacketListener(this);

    public CriticalsModule() {
        super("Criticals", new String[] {"Crits"}, "Guarantees all attacks are critical hits", ModuleCategory.COMBAT);

        // options
        associate(
                modeOption,
                bind,
                drawn
        );

        // listeners
        associate(
                outboundPacketListener
        );
    }

    @Override
    public String getData() {

        // data is mode
        return Formatter.formatEnum(modeOption.getVal());
    }
}
