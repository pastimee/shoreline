package com.momentum.impl.events.forge.event;

/**
 * @author linus
 * @since 02/13/2023
 */
public enum ItemUseStage {

    /**
     * Called at OnItemUseStart
     */
    START,

    /**
     * Called at OnItemUseTick
     */
    TICK,

    /**
     * Called at OnItemUseStop
     */
    STOP,

    /**
     * Called at OnItemUseFinish
     */
    FINISH
}
