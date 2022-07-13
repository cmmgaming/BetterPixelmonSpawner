package com.lypaka.betterpixelmonspawner.Listeners;

import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandListener {

    @SubscribeEvent
    public void onCommand (CommandEvent event) {

        if (event.getCommand().getName().equalsIgnoreCase("lastlegend") || event.getCommand().getName().equalsIgnoreCase("ll")) {

            event.setCanceled(true);
            event.getSender().getServer().getCommandManager().executeCommand(event.getSender(), "bps ll");

        } else if (event.getCommand().getName().contains("checkspawns")) {

            event.setCanceled(true);
            event.getSender().getServer().getCommandManager().executeCommand(event.getSender(), "bps list");

        } else if (event.getCommand().getName().contains("pokekill")) {

            event.setCanceled(true);
            event.getSender().getServer().getCommandManager().executeCommand(event.getSender(), "bps clear");

        }

    }

}
