package com.momentum.api.module;

import net.minecraft.util.math.BlockPos;

/**
 * @author linus
 * @since 03/13/2023
 */
public class BlockPlacerModule extends Module
        implements IBlockPlacer {

    /**
     * Module with aliases
     *
     * @param name        The name of the module
     * @param aliases     The aliases of the module
     * @param description The description of the module
     * @param category    The category that the module will appear under in the UI
     */
    public BlockPlacerModule(String name, String[] aliases, String description, ModuleCategory category) {
        super(name, aliases, description, category);
    }

    /**
     * Default module
     *
     * @param name        The name of the module
     * @param description The description of the module
     * @param category    The category that the module will appear under in the UI
     */
    public BlockPlacerModule(String name, String description, ModuleCategory category) {
        this(name, new String[] {}, description, category);
    }

    /**
     * Place block
     *
     * @param in The position
     */
    @Override
    public boolean place(BlockPos in) {

        // TODO: IMPL
        return true;
    }
}
