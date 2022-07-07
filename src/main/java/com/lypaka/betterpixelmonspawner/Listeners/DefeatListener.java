package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.pixelmongenerations.api.events.BeatWildPixelmonEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class DefeatListener {

    @SubscribeEvent
    public void onDefeat (BeatWildPixelmonEvent event) {

        EntityPixelmon pokemon = event.getWildPokemon().controlledPokemon.get(0).pokemon;
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
