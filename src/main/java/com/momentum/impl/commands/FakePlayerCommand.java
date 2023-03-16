package com.momentum.impl.commands;

import com.mojang.authlib.GameProfile;
import com.momentum.Momentum;
import com.momentum.api.command.Command;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.text.TextFormatting;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author linus
 * @since 03/13/2023
 */
public class FakePlayerCommand extends Command {

    private final Queue<Integer> fakes =
            new LinkedList<>();

    // last pushed
    private boolean last;

    public FakePlayerCommand() {
        super("FakePlayer", "Adds a fake player to the world");
    }

    /**
     * Invokes the command
     *
     * @param args The command arguments
     */
    @Override
    public void invoke(String[] args) {

        // add singular
        if (args.length == 0) {

            // last pushed
            if (last) {

                // remove last fake player from world
                pop();
                last = false;
            }

            // last popped
            else {

                // spawn a fake player in the world
                push(false);
                last = true;
            }
        }

        // push/pop
        else if (args.length == getArgSize()) {

            // push fakeplayer
            if (args[0].equalsIgnoreCase("push")
                    || args[0].equalsIgnoreCase("add")) {

                // spawn fakeplayer
                push(true);
            }

            // pop fakeplayer
            else if (args[0].equalsIgnoreCase("pop")
                    || args[0].equalsIgnoreCase("remove")) {

                // despawn fakeplayer
                pop();
            }
        }

        // incorrect args
        else {

            // notify
            Momentum.CHAT_MANAGER.send(TextFormatting.RED + "Incorrect arguments! Please follow the format " + getUseCase());
        }
    }

    /**
     * Spawns a fake player
     */
    private void push(boolean num) {

        // create a fake player
        EntityOtherPlayerMP fake =
                new EntityOtherPlayerMP(mc.world,
                        new GameProfile(new UUID(0, 0), "FakePlayer" + (num ? fakes.size() : "")));

        // copy features from player
        fake.setHealth(mc.player.getHealth());
        fake.setAbsorptionAmount(mc.player.getAbsorptionAmount());
        fake.copyLocationAndAnglesFrom(mc.player);
        fake.rotationYawHead = mc.player.rotationYaw;
        fake.inventory.copyInventory(mc.player.inventory);
        fake.inventoryContainer = mc.player.inventoryContainer;

        // fake id
        int id = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);

        // don't add same id twice
        if (!fakes.contains(id)) {

            // add the fake player to world
            fakes.add(id);
            mc.world.addEntityToWorld(id, fake);
        }
    }

    /**
     * Removes a fake player
     */
    private void pop() {

        // last id
        Integer fake = fakes.poll();
        if (fake != null) {

           // remove the fake player
           mc.world.removeEntityFromWorld(fake);
       }
    }

    /**
     * Gets a correct use case
     *
     * @return The correct use case
     */
    @Override
    public String getUseCase() {
        return "<optional:push/pop>";
    }

    /**
     * Gets the maximum argument size
     *
     * @return The maximum argument size
     */
    @Override
    public int getArgSize() {
        return 1;
    }
}
