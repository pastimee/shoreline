package com.momentum.impl.handlers;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.momentum.Momentum;
import com.momentum.api.command.Command;
import com.momentum.api.event.Listener;
import com.momentum.api.handler.Handler;
import com.momentum.asm.mixins.vanilla.accessors.IGuiChat;
import com.momentum.asm.mixins.vanilla.accessors.INetHandlerPlayClient;
import com.momentum.impl.events.forge.event.ClientSendMessageEvent;
import com.momentum.impl.events.vanilla.entity.UpdateEvent;
import com.momentum.impl.events.vanilla.gui.RenderChatBoxEvent;
import com.momentum.impl.init.Commands;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;

import java.util.Arrays;

/**
 * Manages command functionality
 *
 * @author linus
 * @since 02/18/2023
 */
public class CommandHandler extends Handler {

    // suggestion
    private StringBuilder suggestionBuilder = new StringBuilder();

    /**
     * Manages command functionality
     */
    public CommandHandler() {

        // command invoke impl
        Momentum.EVENT_BUS.subscribe(new Listener<ClientSendMessageEvent>() {

            @Override
            public void invoke(ClientSendMessageEvent event) {

                // player message
                String message = event.getMessage().trim();

                // event the user sends a command
                if (message.startsWith(Commands.PREFIX)) {

                    // prevent rendering
                    event.setCanceled(true);

                    // chat gui
                    // GuiNewChat chat = mc.ingameGUI.getChatGUI();

                    // make sure chat gui exists
                    // if (chat != null) {

                        // add to chat
                        // chat.addToSentMessages(event.getMessage());

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
                            if (c.startsWith(command) != -2) {

                                // mark as suggestion
                                executable = c;
                                break;
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
                    //}
                }
            }
        });

        // suggestion impl
        Momentum.EVENT_BUS.subscribe(new Listener<UpdateEvent>() {

            @Override
            public void invoke(UpdateEvent event) {

                // null check
                if (mc.player == null || mc.world == null || !((INetHandlerPlayClient) mc.player.connection).isDoneLoadingTerrain()) {
                    return;
                }

                // reset suggestion builder
                StringBuilder suggestion = new StringBuilder();

                // player is in gui
                if (mc.currentScreen != null) {

                    // player is in chat
                    if (mc.currentScreen instanceof GuiChat) {

                        // chat input
                        GuiTextField input = ((IGuiChat) mc.currentScreen).getInputField();

                        // make sure input exists
                        if (input != null) {

                            // argument inputs
                            String[] args = input.getText().split(" ");

                            // event the user sends a command
                            if (input.getText().startsWith(Commands.PREFIX)) {

                                // command
                                String cmd = args[0].substring(1);

                                // make sure the command is not broken
                                if (!cmd.endsWith(" ")) {

                                    // suggestion
                                    Command command = null;

                                    // search commands
                                    for (Command c : Momentum.COMMAND_REGISTRY.getData()) {

                                        // index
                                        int i = c.startsWith(cmd);

                                        // match
                                        if (i != -2) {

                                            // check bounds
                                            String name = i == -1 ? c.getName() : c.getAlias(i);
                                            if (cmd.length() <= name.length()) {

                                                // mark as suggestion
                                                command = c;
                                                break;
                                            }
                                        }
                                    }

                                    // args without command
                                    args = Arrays.copyOfRange(args, 1, args.length);

                                    // found a suggestion
                                    if (command != null) {

                                        // use cases
                                        String[] cases = command.getUseCase().split(" ");

                                        // index of the suggestion
                                        int index = command.startsWith(cmd);

                                        // args size
                                        for (int i = 0; i < command.getArgSize(); i++) {

                                            // make sure no OOB exception
                                            if (i < args.length) {

                                                // sync input
                                                cases[i] = args[i];
                                            }
                                        }

                                        // prefix
                                        suggestion.append(Commands.PREFIX);

                                        // show suggestion
                                        if (index == -1) {

                                            // add name to suggestion builder
                                            suggestion
                                                    .append(command.getName().toLowerCase())
                                                    .append(" ");

                                            // add cases
                                            for (String c : cases) {

                                                // add use cases
                                                suggestion
                                                        .append(c)
                                                        .append(" ");
                                            }
                                        }

                                        // alias
                                        else {

                                            // add aliases to suggestion
                                            suggestion
                                                    .append(command.getAlias(index).toLowerCase())
                                                    .append(" ");

                                            // add cases
                                            for (String c : cases) {

                                                // add use cases
                                                suggestion
                                                        .append(c)
                                                        .append(" ");
                                            }
                                        }

                                        // update suggestion builder
                                        suggestionBuilder = suggestion;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        // suggestion render impl
        Momentum.EVENT_BUS.subscribe(new Listener<RenderChatBoxEvent>() {

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
