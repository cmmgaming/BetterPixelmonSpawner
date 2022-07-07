package com.lypaka.betterpixelmonspawner.Utils.Counters;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PokemonCounter {

    public static Map<UUID, Integer> pokemonCountMap = new HashMap<>();

    public static int getCount (UUID uuid) {

        int count = 0;
        if (pokemonCountMap.containsKey(uuid)) {

            count = pokemonCountMap.get(uuid);

        }

        return count;

    }

    public static void increment (EntityPixelmon pokemon, UUID uuid) {

        if (ConfigGetters.maxPokemon <= 0) return;
        int count = getCount(uuid);
        int updated = count + 1;
        pokemonCountMap.put(uuid, updated);
        pokemon.addTag("SpawnedPlayerUUID:" + uuid.toString());

    }

    public static void decrement (UUID uuid) {

        if (ConfigGetters.maxPokemon <= 0) return;
        int count = getCount(uuid);
        int updated = Math.max(0, count - 1);
        pokemonCountMap.put(uuid, updated);

    }

}
