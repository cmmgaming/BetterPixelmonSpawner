package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JoinListener {

    public static Map<UUID, EntityPlayerMP> playerMap = new HashMap<>();

    @SubscribeEvent
    public void onJoin (PlayerEvent.PlayerLoggedInEvent event) {

        EntityPlayerMP player = (EntityPlayerMP) event.player;
        playerMap.put(player.getUniqueID(), player);
        if (ConfigGetters.pokemonOptOut.contains(player.getUniqueID().toString())) {

            player.sendMessage(FancyText.getFancyText("&eJust a reminder, you're currently opted out of Pokemon spawns!"));

        }
        if (ConfigGetters.miscOptOut.contains(player.getUniqueID().toString())) {

            player.sendMessage(FancyText.getFancyText("&eJust a reminder, you're currently opted out of misc entity spawns!"));

        }
        if (ConfigGetters.npcOptOut.contains(player.getUniqueID().toString())) {

            player.sendMessage(FancyText.getFancyText("&eJust a reminder, you're currently opted out of NPC spawns!"));

        }
        if (ConfigGetters.legendaryOptOut.contains(player.getUniqueID().toString())) {

            player.sendMessage(FancyText.getFancyText("&eJust a reminder, you're currently opted out of legendary spawns!"));

        }

    }

    @SubscribeEvent
    public void onLeave (PlayerEvent.PlayerLoggedOutEvent event) {

        playerMap.entrySet().removeIf(entry -> entry.getKey().toString().equalsIgnoreCase(event.player.getUniqueID().toString()));

    }

}
