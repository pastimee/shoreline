package com.momentum.api.module;

/**
 * @author linus
 * @since 01/16/2023
 */
public enum ModuleCategory {

    /**
     * Modules used for combat (Ex: KillAura, AutoCrystal, Surround, etc.)
     */
    COMBAT,

    /**
     * Modules that exploit certain anticheats to allow for "non-vanilla" behavior (Ex: AntiHunger, PacketFlight, Reach, etc.)
     */
    EXPLOITS,

    /**
     * Modules that don't fit in the other categories
     */
    MISCELLANEOUS,

    /**
     * Modules that allow the player to move in unnatural ways (Ex: Flight, Speed, ReverseStep, etc.)
     */
    MOVEMENT,

    /**
     * Modules that are visual modifications (Ex: ESP, Nametags, HoleESP, etc.)
     */
    RENDER,

    /**
     * Modules that are modifications to world (Ex: Wallhack, SpeedMine, FastUse, etc.)
     */
    WORLD,

    /**
     * Modules associated with client processes
     */
    CLIENT,

    /**
     * Modules hidden from the ClickGUI
     */
    HIDDEN
}
