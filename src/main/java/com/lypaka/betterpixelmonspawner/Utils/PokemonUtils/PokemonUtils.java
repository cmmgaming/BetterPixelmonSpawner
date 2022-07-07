package com.lypaka.betterpixelmonspawner.Utils.PokemonUtils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumSpecies;

public class PokemonUtils {

    public static EntityPixelmon validatePokemon (EntityPixelmon pokemon, int spawnLevel) {

        if (!ConfigGetters.validateSpawns) return pokemon;
        int minimumLevel = pokemon.baseStats.spawnLevel;
        if (spawnLevel < minimumLevel) {

            // Pokemon cannot naturally exist at this level, check for previous evolution forms
            if (pokemon.baseStats.preEvolutions.length > 0) {

                EnumSpecies preEvo = pokemon.baseStats.preEvolutions[pokemon.baseStats.preEvolutions.length - 1];
                EntityPixelmon newPokemon = PokemonSpec.from(preEvo.getPokemonName()).create(pokemon.world);
                newPokemon.setForm(pokemon.getForm());
                return newPokemon;

            } else {

                // Pokemon is a single-stage Pokemon and cannot be de-evolved, return the Pokemon
                return pokemon;

            }

        }

        return pokemon;

    }

}
