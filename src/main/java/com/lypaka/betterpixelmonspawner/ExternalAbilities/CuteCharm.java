package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.common.entity.pixelmon.stats.Gender;
import com.pixelmongenerations.core.util.helper.RandomHelper;

public class CuteCharm {

    public static boolean applies (EntityPixelmon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("CuteCharm") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Cute Charm");

    }

    public static void tryApplyCuteCharmEffect (EntityPixelmon wildPokemon, EntityPixelmon playersPokemon) {

        Gender playerPokemonGender = playersPokemon.getGender();
        if (playerPokemonGender == Gender.None) return;
        if (wildPokemon.getGender() == Gender.None) return;

        Gender opposite;
        if (playerPokemonGender == Gender.Male) {

            opposite = Gender.Female;

        } else {

            opposite = Gender.Male;

        }

        if (wildPokemon.baseStats.malePercent < 100 && wildPokemon.baseStats.malePercent > 0) {

            // pokemon can be both male and female
            if (RandomHelper.getRandomChance(66.67)) {

                wildPokemon.setGender(opposite);

            }

        }

    }

}
