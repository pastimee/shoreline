package com.momentum.impl.commands;

import com.momentum.Momentum;
import com.momentum.api.command.Command;
import com.momentum.api.feature.Option;
import com.momentum.api.module.Module;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.Collection;

/**
 * @author linus
 * @since 02/20/2023
 */
public class SetCommand extends Command {

    // associated module
    private final Module module;

    /**
     * Setting command
     *
     * @param module The associated module
     */
    public SetCommand(Module module) {
        super(module.getName(), module.getAliases(), "Sets the option values for " + module.getName());

        // assign module
        this.module = module;
    }

    /**
     * Invokes the command
     *
     * @param args The command arguments
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void invoke(String[] args) {

        // make sure arg size matches
        if (args.length == getArgSize()) {

            // search through all options
            Option opt = null;
            for (Option option : module.getOptions()) {

                // found option
                if (option.equalsIgnoreCase(args[0])) {

                    // mark option and break
                    opt = option;
                    break;
                }
            }

            // found option specified by args
            if (opt != null) {

                // catches Exceptions when setting values
                try {

                    // boolean option
                    if (opt.getVal() instanceof Boolean) {

                        // val specified by user
                        boolean val = Boolean.parseBoolean(args[1]);

                        // update option val
                        ((Option<Boolean>) opt).setVal(val);
                    }

                    // number option
                    else if (opt.getVal() instanceof Number) {

                        // int option
                        if (opt.getVal() instanceof Integer) {

                            // val specified by user
                            int val = Integer.parseInt(args[1]);

                            // update option val
                            ((Option<Integer>) opt).setVal(val);
                        }

                        // float option
                        else if (opt.getVal() instanceof Float) {

                            // val specified by user
                            float val = Float.parseFloat(args[1]);

                            // update option val
                            ((Option<Float>) opt).setVal(val);
                        }

                        // double option
                        else if (opt.getVal() instanceof Double) {

                            // val specified by user
                            double val = Double.parseDouble(args[1]);

                            // update option val
                            ((Option<Double>) opt).setVal(val);
                        }
                    }

                    // enum option
                    else if (opt.getVal() instanceof Enum<?>) {

                        // val specified by user
                        Enum<?> val = Enum.valueOf(((Option<Enum>) opt).getVal().getClass(), args[2].toUpperCase());

                        // update option val
                        ((Option<Enum>) opt).setVal(val);
                    }

                    // color option
                    else if (opt.getVal() instanceof Color) {

                        // list of rgb values compiled into a color obj
                        Color val = new Color(Integer.parseInt(args[2]));

                        // update option val
                        ((Option<Color>) opt).setVal(val);
                    }

                    // list option
                    else if (opt.getVal() instanceof Collection) {

                        // item entry
                        if (args[2].startsWith("item_")) {

                            // item value
                            Item val = Item.getByNameOrId(args[2].substring(5));

                            // add to item list
                            ((Option<Collection>) opt).getVal().add(val);
                        }

                        // block entry
                        else if (args[2].startsWith("block_")) {

                            // block value
                            Block val = Block.getBlockFromName(args[2].substring(6));

                            // add to block list
                            ((Option<Collection>) opt).getVal().add(val);
                        }

                        // format exception
                        else {

                            // notify
                            Momentum.CHAT_MANAGER.send(TextFormatting.RED + "Invalid format! Include type before list value (such as item_minecraft:egg or block_minecraft:dirt)");
                        }
                    }
                }

                // exceptions when handling setting
                catch (Exception e) {

                    // notify
                    Momentum.CHAT_MANAGER.send(TextFormatting.RED + "Invalid value! Could not parse " + args[1]);
                    e.printStackTrace();
                }
            }

            // invalid args
            else {

                // notify
                Momentum.CHAT_MANAGER.send(TextFormatting.RED + "Invalid option! Could not find " + args[0]);
            }
        }

        // incorrect args
        else {

            // notify
            Momentum.CHAT_MANAGER.send(TextFormatting.RED + "Incorrect arguments! Please follow the format " + getUseCase());
        }
    }

    /**
     * Gets a correct use case
     *
     * @return The correct use case
     */
    @Override
    public String getUseCase() {
        return "<option> <value>";
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
