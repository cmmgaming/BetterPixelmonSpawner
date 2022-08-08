package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.util.helper.RandomHelper;

public class Intimidate {

    public static boolean applies (EntityPixelmon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Intimidate");

    }

    public static EntityPixelmon tryIntimidate (EntityPixelmon wildPokemon, EntityPixelmon playerPokemon) {

        int level = playerPokemon.getLvl().getLevel();
        int spawnLevel = wildPokemon.getLvl().getLevel();
        if (level > spawnLevel) {

            int difference = level - spawnLevel;
            if (difference >= 5) {

                if (RandomHelper.getRandomChance(50)) {

                    wildPokemon = null;

                }

            }

        }

        return wildPokemon;

    }

}
