package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.lypaka.betterpixelmonspawner.Utils.LegendaryListing;
import com.pixelmongenerations.api.events.spawning.DespawnEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.UUID;

public class DespawnListener {

    @SubscribeEvent
    public void onDespawn (DespawnEvent event) {

        EntityPixelmon pokemon = event.getPokemon();
        if (pokemon.battleController != null) return;
        for (String tag : pokemon.getTags()) {

            if (tag.contains("SpawnedPlayerUUID:")) {

                String[] split = tag.split(":");
                UUID uuid = UUID.fromString(split[1]);
                PokemonCounter.decrement(uuid);
                break;

            }

        }
        if (EnumSpecies.legendaries.contains(pokemon.getPokemonName()) || EnumSpecies.ultrabeasts.contains(pokemon.getPokemonName()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

            pokemon.getTags().forEach(tag -> {

                if (tag.equalsIgnoreCase("SpawnedLegendary")) {

                    try {

                        LegendaryListing.updatePokemonStatusDespawned(pokemon.getUniqueID());

                    } catch (ObjectMappingException e) {

                        e.printStackTrace();

                    }

                }

            });

        }

    }

}
