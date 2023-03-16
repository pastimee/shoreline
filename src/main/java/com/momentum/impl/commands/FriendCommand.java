package com.momentum.impl.commands;

import com.momentum.Momentum;
import com.momentum.api.command.Command;
import com.momentum.impl.managers.Relation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

/**
 * @author linus
 * @since 03/13/2023
 */
public class FriendCommand extends Command {

    public FriendCommand() {
        super("Friend", "Friends a player");
    }

    /**
     * Invokes the command
     *
     * @param args The command arguments
     */
    @Override
    public void invoke(String[] args) {

        // check arg size
        if (args.length == getArgSize()) {

            // find given player
            EntityPlayer player = null;
            for (EntityPlayer p : mc.world.playerEntities) {

                // name matches
                if (p.getName().equalsIgnoreCase(args[1])) {

                    // name match
                    player = p;
                    break;
                }
            }

            // found player
            if (player != null) {

                // add arg
                if (args[0].equalsIgnoreCase("add")) {

                    // add friend relation to player
                    Momentum.RELATION_MANGER
                            .add(player, Relation.FRIEND);
                }

                // remove arg
                else if (args[0].equalsIgnoreCase("remove")
                        || args[0].equalsIgnoreCase("delete")
                        || args[0].equalsIgnoreCase("del")) {

                    // remove friend relation from player
                    Momentum.RELATION_MANGER
                            .remove(player, Relation.FRIEND);
                }

                // invalid args
                else {

                    // notify
                    Momentum.CHAT_MANAGER.send(
                            TextFormatting.RED + "Invalid option! Could not find " + args[0]);
                }
            }

            // invalid args
            else {

                // notify
                Momentum.CHAT_MANAGER.send(
                        TextFormatting.RED + "Could not find player with name " + args[0]);
            }
        }

        // incorrect args
        else {

            // notify
            Momentum.CHAT_MANAGER.send(
                    TextFormatting.RED + "Incorrect arguments! Please follow the format " + getUseCase());
        }
    }

    /**
     * Gets a correct use case
     *
     * @return The correct use case
     */
    @Override
    public String getUseCase() {
        return "<add/remove> <name>";
    }

    /**
     * Gets the maximum argument size
     *
     * @return The maximum argument size
     */
    @Override
    public int getArgSize() {
        return 2;
    }
}
