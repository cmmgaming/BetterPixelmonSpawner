package com.lypaka.betterpixelmonspawner.Utils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.npcs.registry.DropItemRegistry;
import com.pixelmongenerations.common.entity.pixelmon.drops.AlphaInfo;
import com.pixelmongenerations.core.enums.EnumSpecies;
import com.pixelmongenerations.core.util.helper.RandomHelper;

import java.util.ArrayList;

public class AlphaPokemonUtils {

    public static boolean spawnAlpha() {

        if (ConfigGetters.alphaSpawnChance == 0) return false;
        return RandomHelper.getRandomChance(ConfigGetters.alphaSpawnChance);

    }

    public static boolean isDefaultAlpha (EnumSpecies species) {

        ArrayList<AlphaInfo> alphaSpawns = DropItemRegistry.getAlphaPokemon();
        for (AlphaInfo a : alphaSpawns) {

            if (a.pokemon == species) {

                return true;

            }

        }

        return false;

    }

}
