package com.momentum.impl.handlers;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.momentum.Momentum;
import com.momentum.api.command.Command;
import com.momentum.api.event.Listener;
import com.momentum.api.handler.Handler;
import com.momentum.api.util.Wrapper;
import com.momentum.asm.mixins.vanilla.accessors.IGuiChat;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.forge.event.ClientSendMessageEvent;
import com.momentum.impl.events.vanilla.entity.UpdateEvent;
import com.momentum.impl.events.vanilla.gui.RenderChatBoxEvent;
import com.momentum.impl.init.Commands;
import net.minecraft.client.gui.GuiChat;

import java.util.Arrays;

/**
 * Manages command functionality
 *
 * @author linus
 * @since 02/18/2023
 */
public class CommandHandler extends Handler implements Wrapper {

    // suggestion
    private StringBuilder suggestionBuilder = new StringBuilder();

    /**
     * Manages command functionality
     */
    public CommandHandler() {

        // command invoke impl
        associate(new Listener<ClientSendMessageEvent>() {

            @Override
            public void invoke(ClientSendMessageEvent event) {

                // player message
                String message = event.getMessage().trim();

                // event the user sends a command
                if (message.startsWith(Commands.PREFIX)) {

                    // prevent rendering
                    event.setCanceled(true);
                    mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());

                    // remove prefix
                    message = message.substring(1);

                    // passed arguments
                    String[] args = message.split(" ");

                    // given command
                    String command = args[0];

                    // remove command from args
                    args = Arrays.copyOfRange(args, 1, args.length);

                    // executable command
                    Command executable = null;

                    // search commands
                    for (Command c : Momentum.COMMAND_REGISTRY.getData()) {

                        // match
                        if (c.startsWith(command) != -1) {

                            // mark as suggestion
                            executable = c;
                        }
                    }

                    // execute command
                    if (executable != null) {

                        // execute
                        executable.invoke(args);
                    }

                    else {

                        // unrecognized command exception
                        Momentum.CHAT_MANAGER.send(ChatFormatting.RED + "Invalid command! Please enter a valid command.");
                    }
                }
            }
        });

        // suggestion impl
        associate(new Listener<UpdateEvent>() {

            @Override
            public void invoke(UpdateEvent event) {

                // null check
                if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
                    return;
                }

                // reset suggestion builder
                suggestionBuilder = new StringBuilder();

                // player is in chat
                if (mc.currentScreen instanceof GuiChat) {

                    // chat input
                    String input = ((IGuiChat) mc.currentScreen).getInputField().getText();

                    // argument inputs
                    String[] args = input.split(" ");

                    // event the user sends a command
                    if (input.startsWith(Commands.PREFIX)) {

                        // command
                        String command = args[0].substring(1);

                        // suggestion
                        Command suggestion = null;

                        // search commands
                        for (Command c : Momentum.COMMAND_REGISTRY.getData()) {

                            // match
                            if (c.startsWith(command) != -1) {

                                // mark as suggestion
                                suggestion = c;
                            }
                        }

                        // args without command
                        args = Arrays.copyOfRange(args, 1, args.length);

                        // found a suggestion
                        if (suggestion != null) {

                            // use cases
                            String[] cases = suggestion.getUseCase().split(" ");

                            // index of the suggestion
                            int index = suggestion.startsWith(command);

                            // must match
                            if (index == -2) {
                                return;
                            }

                            // args size
                            for (int i = 0; i < suggestion.getArgSize(); i++) {

                                // make sure no OOB exception
                                if (i < args.length) {

                                    // sync input
                                    cases[i] = args[i];
                                }
                            }

                            // prefix
                            suggestionBuilder.append(Commands.PREFIX);

                            // show suggestion
                            if (index == -1) {

                                // add name to suggestion builder
                                suggestionBuilder
                                        .append(suggestion.getName().toLowerCase())
                                        .append(" ");

                                // add cases
                                for (String c : cases) {

                                    // add use cases
                                    suggestionBuilder
                                            .append(c)
                                            .append(" ");
                                }
                            }

                            // alias
                            else {

                                // add aliases to suggestion
                                suggestionBuilder
                                        .append(suggestion.getAlias(index).toLowerCase())
                                        .append(" ");

                                // add cases
                                for (String c : cases) {

                                    // add use cases
                                    suggestionBuilder
                                            .append(c)
                                            .append(" ");
                                }
                            }
                        }
                    }
                }
            }
        });

        // suggestion render impl
        associate(new Listener<RenderChatBoxEvent>() {

            @Override
            public void invoke(RenderChatBoxEvent event) {

                // suggestion to text
                String text = suggestionBuilder.toString();

                // render suggestion in chat box
                if (!text.isEmpty()) {

                    // cancel
                    event.setCanceled(true);
                    event.setText(text);
                }
            }
        });
    }
}
