package com.momentum.api.util.block;

import com.momentum.api.util.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

/**
 * Block resistance type data
 *
 * @author linus
 * @since 02/18/2023
 */
public class ResistantBlocks implements Wrapper {

    // All blocks that are resistant to explosions
    private static final List<Block> BLAST_RESISTANT = Arrays.asList(
            Blocks.OBSIDIAN,
            Blocks.ANVIL,
            Blocks.ENCHANTING_TABLE,
            Blocks.ENDER_CHEST,
            Blocks.BEACON
    );

    // All blocks that are unbreakable with tools in survival mode
    private static final List<Block> UNBREAKABLE = Arrays.asList(
            Blocks.BEDROCK,
            Blocks.COMMAND_BLOCK,
            Blocks.CHAIN_COMMAND_BLOCK,
            Blocks.END_PORTAL_FRAME,
            Blocks.BARRIER,
            Blocks.PORTAL
    );

    /**
     * Finds the if a given position is breakable
     *
     * @param pos The position to check
     * @return Whether or not the given position is breakable
     */
    public static boolean isBreakable(BlockPos pos) {
        return !UNBREAKABLE.contains(mc.world.getBlockState(pos).getBlock());
    }

    /**
     * Finds the if a given block is breakable
     *
     * @param block The block to check
     * @return Whether or not the given block is breakable
     */
    public static boolean isBreakable(Block block) {
        return !UNBREAKABLE.contains(block);
    }

    /**
     * Finds the if a given position is unbreakable
     *
     * @param pos The position to check
     * @return Whether or not the given position is unbreakable
     */
    public static boolean isUnbreakable(BlockPos pos) {
        return UNBREAKABLE.contains(mc.world.getBlockState(pos).getBlock());
    }

    /**
     * Finds the if a given block is unbreakable
     *
     * @param block The block to check
     * @return Whether or not the given block is unbreakable
     */
    public static boolean isUnbreakable(Block block) {
        return UNBREAKABLE.contains(block);
    }

    /**
     * Finds the if a given position is blast resistant
     *
     * @param pos The position to check
     * @return Whether or not the given position is blast resistant
     */
    public static boolean isBlastResistant(BlockPos pos) {

        // block at pos
        Block block = mc.world.getBlockState(pos).getBlock();

        // check if data contains
        return BLAST_RESISTANT.contains(block) || UNBREAKABLE.contains(block);
    }

    /**
     * Finds the if a given position is air
     *
     * @param pos The position to check
     * @return Whether or not the given position is air
     */
    public static boolean isAir(BlockPos pos) {

        // check position is air
        return mc.world.isAirBlock(pos);
    }

    /**
     * Finds the if a given position is replaceable
     *
     * @param pos The position to check
     * @return Whether or not the given position is replaceable
     */
    public static boolean isReplaceable(BlockPos pos) {

        // check position is air
        return mc.world.getBlockState(pos).getMaterial().isReplaceable();
    }
}
