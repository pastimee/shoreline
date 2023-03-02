package com.momentum.api.util.block;

import com.momentum.api.util.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.Arrays;
import java.util.List;

/**
 * @author linus
 * @since 02/19/2023
 */
public class InteractBlocks implements Wrapper {

    // list of blocks which need to be shift clicked to be placed on
    private static final List<Block> SNEAK_BLOCKS = Arrays.asList(
            Blocks.ENDER_CHEST,
            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.CRAFTING_TABLE,
            Blocks.ANVIL,
            Blocks.BREWING_STAND,
            Blocks.HOPPER,
            Blocks.DROPPER,
            Blocks.DISPENSER,
            Blocks.TRAPDOOR,
            Blocks.ENCHANTING_TABLE,
            Blocks.WHITE_SHULKER_BOX,
            Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX,
            Blocks.LIGHT_BLUE_SHULKER_BOX,
            Blocks.YELLOW_SHULKER_BOX,
            Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX,
            Blocks.SILVER_SHULKER_BOX,
            Blocks.CYAN_SHULKER_BOX,
            Blocks.PURPLE_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX,
            Blocks.BROWN_SHULKER_BOX,
            Blocks.GREEN_SHULKER_BOX,
            Blocks.RED_SHULKER_BOX,
            Blocks.BLACK_SHULKER_BOX,
            // Blocks.COMMAND_BLOCK,
            // Blocks.CHAIN_COMMAND_BLOCK
            Blocks.OAK_DOOR,
            Blocks.DARK_OAK_DOOR,
            Blocks.SPRUCE_DOOR,
            Blocks.ACACIA_DOOR,
            Blocks.BIRCH_DOOR,
            Blocks.JUNGLE_DOOR
    );

    // blocks that are interact-able
    private static final List<Block> BUTTON_BLOCKS = Arrays.asList(
            Blocks.STONE_BUTTON,
            Blocks.WOODEN_BUTTON,
            Blocks.LEVER
    );

    /**
     * Checks if the shift blocks list contains a given block
     *
     * @param in The block to check
     * @return Whether the shift blocks list contains a given block
     */
    public static boolean canInteract(Block in) {
        return (mc.player.isSneaking() && SNEAK_BLOCKS.contains(in)) || BUTTON_BLOCKS.contains(in);
    }
}
