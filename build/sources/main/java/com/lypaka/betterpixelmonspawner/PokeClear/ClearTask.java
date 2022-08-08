package com.lypaka.betterpixelmonspawner.PokeClear;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Listeners.JoinListener;
import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.lypaka.lypakautils.WorldMap;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumSpecies;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;

public class ClearTask {

    private static Timer warningTimer = null;
    private static Timer clearTimer = null;
    public static int count = 0;

    public static void startClearTask() {

        if (warningTimer != null) {

            warningTimer.cancel();

        }

        if (clearTimer != null) {

            clearTimer.cancel();

        }

        // checks this here for if on reload, clear task is toggled off. Clears the tasks should they aren't running for no reason basically
        if (!ConfigGetters.pokeClearEnabled) return;

        warningTimer = new Timer();
        clearTimer = new Timer();
        long msgInterval = ConfigGetters.clearWarningInterval * 1000L;
        long clearInterval = ConfigGetters.pokeClearInterval * 1000L;
        long interval = clearInterval - msgInterval;
        long actualFuckingInterval = clearInterval + interval;
        warningTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                BetterPixelmonSpawner.server.getPlayerList().sendMessage(FancyText.getFancyText(ConfigGetters.clearWarningMessage));
                clearTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {

                        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                            JoinListener.pokemonMap.entrySet().removeIf(entry -> {

                                PokemonCounter.checkForDespawnPokemon(entry.getKey());
                                if (!JoinListener.playerMap.containsKey(entry.getKey())) {

                                    return true;

                                }

                                return false;

                            });

                            String msg = ConfigGetters.pokeClearMessage.replace("%number%", String.valueOf(count));
                            if (count == 1 && msg.contains("have")) {

                                msg = msg.replace("have", "has");

                            }

                            BetterPixelmonSpawner.server.getPlayerList().sendMessage(FancyText.getFancyText(msg
                                    .replace("%number%", String.valueOf(count))
                            ));

                            count = 0;

                        });

                    }

                }, interval);

            }

        }, 0, actualFuckingInterval);

    }

    public static boolean isBlacklisted (EntityPixelmon pokemon) {

        boolean blacklisted = false;
        if (pokemon.hasOwner()) {

            return true;

        }
        if (pokemon.battleController != null) {

            return true;

        }
        for (String entry : ConfigGetters.blacklistedClearPokemon) {

            if (entry.equalsIgnoreCase("legendaries") && EnumSpecies.legendaries.contains(pokemon.getPokemonName()) ||
                entry.equalsIgnoreCase("legendaries") && EnumSpecies.ultrabeasts.contains(pokemon.getPokemonName()) ||
                entry.equalsIgnoreCase("legendaries") && pokemon.getTags().contains("SpecialLegendarySpawn")) {

                blacklisted = true;

            } else if (entry.equalsIgnoreCase("alphas") && pokemon.isAlpha()) {

                blacklisted = true;

            } else if (entry.equalsIgnoreCase("totems") && pokemon.isTotem()) {

                blacklisted = true;

            } else if (entry.equalsIgnoreCase("bosses")) {

                if (pokemon.isMega) {

                    blacklisted = true;

                } else {

                    for (String tag : pokemon.getTags()) {

                        if (tag.equalsIgnoreCase("PixelmonDefaultBoss") || tag.contains("BossPokemon:Tier-")) {

                            blacklisted = true;
                            break;

                        }

                    }

                }

            } else if (entry.equalsIgnoreCase("shinies") && pokemon.isShiny()) {

                blacklisted = true;


            } else if (entry.equalsIgnoreCase("textures") && !pokemon.getCustomTexture().equalsIgnoreCase("")) {

                blacklisted = true;

            } else if (entry.equalsIgnoreCase("outbreakpokemon") && pokemon.getTags().contains("OutbreakPokemon")) {

                blacklisted = true;

            }

        }

        return blacklisted;

    }

}
