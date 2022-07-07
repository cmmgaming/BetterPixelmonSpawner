package com.lypaka.betterpixelmonspawner.Utils.PokemonUtils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.core.util.helper.RandomHelper;

public class TotemPokemonUtils {

    public static boolean spawnTotem() {

        if (ConfigGetters.totemSpawnChance == 0) return false;
        return RandomHelper.getRandomChance(ConfigGetters.totemSpawnChance);

    }

}
