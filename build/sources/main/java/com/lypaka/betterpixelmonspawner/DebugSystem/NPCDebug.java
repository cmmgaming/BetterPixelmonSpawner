package com.lypaka.betterpixelmonspawner.DebugSystem;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Utils.Counters.NPCCounter;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPCDebug {

    public static List<UUID> trackedPlayers = new ArrayList<>();

    public static void printPlayerDebugInformation (EntityPlayerMP player) {

        if (!trackedPlayers.contains(player.getUniqueID())) return;
        if (ConfigGetters.npcOptOut.contains(player.getUniqueID().toString())) {

            BetterPixelmonSpawner.logger.info("NPC DEBUG: Player " + player.getName() + " is opted out of NPC spawns!");

        }
        if (NPCCounter.getCount(player.getUniqueID()) > ConfigGetters.maxNPCs) {

            if (ConfigGetters.maxNPCs > 0) {

                BetterPixelmonSpawner.logger.info("NPC DEBUG: Player " + player.getName() + " has exceeded the maximum allowed NPC spawns, skipping them!");

            }

        }
        if (player.isCreative() && ConfigGetters.ignoreCreativeNPC) {

            BetterPixelmonSpawner.logger.info("NPC DEBUG: Player " + player.getName() + " is in creative mode and the NPC spawner is set to ignore them!");

        }
        if (player.isSpectator() && ConfigGetters.ignoreSpectatorNPC) {

            BetterPixelmonSpawner.logger.info("NPC DEBUG: Player " + player.getName() + " is in spectator mode and the NPC spawner is set to ignore them!");

        }
        String worldName = player.world.getWorldInfo().getWorldName();
        if (ConfigGetters.worldBlacklist.contains(worldName)) {

            BetterPixelmonSpawner.logger.info("NPC DEBUG: Player " + player.getName() + "'s world is blacklisted from spawning!");

        }

    }

}
