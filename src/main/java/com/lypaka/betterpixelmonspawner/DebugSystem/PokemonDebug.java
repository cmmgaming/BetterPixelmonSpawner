package com.lypaka.betterpixelmonspawner.DebugSystem;

import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.pixelmongenerations.core.event.RepelHandler;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PokemonDebug {

    public static List<UUID> trackedPlayers = new ArrayList<>();

    public static void printPokemonDebugInformation (EntityPlayerMP player) {

        if (!trackedPlayers.contains(player.getUniqueID())) return;
        boolean spawnHappens = true;
        BetterPixelmonSpawner.logger.info("DEBUG: Printing information for " + player.getName() + "'s Pokemon spawn attempt!");
        if (DeadZone.getAreaFromLocation(player) != null) {

            DeadZone deadZone = DeadZone.getAreaFromLocation(player);
            List<String> entities = deadZone.getEntities();
            if (entities.contains("pokemon")) {

                BetterPixelmonSpawner.logger.info("DEBUG: " + player.getName() + " is in a Dead Zone that does not allow Pokemon spawns!");
                if (spawnHappens) {

                    spawnHappens = false;

                }

            }

        }
        if (ConfigGetters.pokemonOptOut.contains(player.getUniqueID().toString())) {

            BetterPixelmonSpawner.logger.info("DEBUG: " + player.getName() + " is currently opted out of Pokemon spawns!");
            if (spawnHappens) {

                spawnHappens = false;

            }

        }
        if (RepelHandler.hasRepel(player.getUniqueID())) {

            BetterPixelmonSpawner.logger.info("DEBUG: " + player.getName() + " currently has Repel active!");
            if (spawnHappens) {

                spawnHappens = false;

            }

        }
        BetterPixelmonSpawner.logger.info("DEBUG: " + player.getName() + " currently has " + PokemonCounter.getCount(player.getUniqueID()) + " Pokemon spawned for them!");
        if (PokemonCounter.getCount(player.getUniqueID()) >= ConfigGetters.maxPokemon) {

            if (ConfigGetters.maxPokemon != 0) {

                BetterPixelmonSpawner.logger.info("DEBUG: " + player.getName() + " has reached the maximum amount of Pokemon they can have spawned on them!");
                if (spawnHappens) {

                    spawnHappens = false;

                }

            }

        }

        if (!spawnHappens) {

            BetterPixelmonSpawner.logger.info("DEBUG: " + player.getName() + "'s spawn attempt has been cancelled for one or more reasons listed above!");

        } else {

            BetterPixelmonSpawner.logger.info("DEBUG: " + player.getName() + "'s spawn attempt has not been cancelled by any restriction modules!");

        }

    }

}
