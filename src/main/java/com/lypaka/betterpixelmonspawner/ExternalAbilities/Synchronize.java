package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.network.EnumUpdateType;

public class Synchronize {

    public static boolean applies (EntityPixelmon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Synchronize");

    }

    public static void applySynchronize (EntityPixelmon wildPokemon, EntityPixelmon playersPokemon) {

        wildPokemon.setNature(playersPokemon.getNature());
        wildPokemon.update(EnumUpdateType.Nature);

    }

}
