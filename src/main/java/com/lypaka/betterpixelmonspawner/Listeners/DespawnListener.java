package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.pixelmongenerations.api.events.spawning.DespawnEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class DespawnListener {

    @SubscribeEvent
    public void onDespawn (DespawnEvent event) {

        EntityPixelmon pokemon = event.getPokemon();
        for (String tag : pokemon.getTags()) {

            if (tag.contains("SpawnedPlayerUUID:")) {

                String[] split = tag.split(":");
                UUID uuid = UUID.fromString(split[1]);
                PokemonCounter.decrement(uuid);
                break;

            }

        }

    }

}
