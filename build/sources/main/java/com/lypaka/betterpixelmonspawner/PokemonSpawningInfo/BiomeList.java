package com.lypaka.betterpixelmonspawner.PokemonSpawningInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores a map of String to a list of Strings containing biome ID and all Pokemon that can spawn in that biome
 */
public class BiomeList {

    public static Map<String, List<String>> biomesToPokemon = new HashMap<>();
    public static Map<String, List<PokemonSpawnInfo>> biomePokemonMap = new HashMap<>();

    public static void addToBiomeList (String pokemonName, String biomeID) {

        List<String> names = new ArrayList<>();
        if (biomesToPokemon.containsKey(biomeID)) {

            names = biomesToPokemon.get(biomeID);

        }
        if (!names.contains(pokemonName)) {

            names.add(pokemonName);
            biomesToPokemon.put(biomeID, names);

        }

    }

}
