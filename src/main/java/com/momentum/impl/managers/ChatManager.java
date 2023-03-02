package com.momentum.impl.managers;

import com.momentum.Momentum;
import com.momentum.api.util.Wrapper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Manages client chat output
 *
 * @author linus
 * @since 02/19/2023
 */
public class ChatManager implements Wrapper {

    /**
     * Sends a client message
     *
     * @param message The message to send
     */
    public void send(String message) {
        mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(TextFormatting.DARK_PURPLE + "[" + Momentum.CLIENT_NAME + "] " + TextFormatting.RESET + message), ThreadLocalRandom.current().nextInt(32767));
    }
}
