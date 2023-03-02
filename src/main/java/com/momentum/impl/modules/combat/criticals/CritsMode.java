package com.momentum.impl.modules.combat.criticals;

/**
 * Criticals mode
 *
 * @author linus
 * @since 02/20/2023
 */
public enum CritsMode {

    /**
     * Attempts changing hit to a critical via packets
     */
    PACKET(0.05f, 0.0f, 0.03f, 0.0f),

    /**
     * Attempts changing hit to a critical via packets for Updated NCP
     */
    PACKET_STRICT(0.11f, 0.1100013579f, 0.0000013579f);

    // packet offsets
    private final float[] offsets;

    /**
     * Initializes the crits mode with given offsets
     *
     * @param offsets The offsets associated with this mode
     */
    CritsMode(float... offsets) {
        this.offsets = offsets;
    }

    /**
     * Gets the packet y offsets for the mode
     *
     * @return The packet y offsets for the mode
     */
    public float[] getOffsets() {
        return offsets;
    }
}
