package com.lypaka.betterpixelmonspawner.Utils.PokemonUtils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.npcs.registry.DropItemRegistry;
import com.pixelmongenerations.common.entity.pixelmon.drops.AlphaInfo;
import com.pixelmongenerations.core.enums.EnumSpecies;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AlphaPokemonUtils {

    public static Map<UUID, Integer> amountMap = new HashMap<>();

    public static boolean spawnAlpha() {

        if (ConfigGetters.alphaSpawnChance == 0) return false;
        return RandomHelper.getRandomChance(ConfigGetters.alphaSpawnChance);

    }

    public static boolean isDefaultAlpha (EnumSpecies species, int form) {

        ArrayList<AlphaInfo> alphaSpawns = DropItemRegistry.getAlphaPokemon();
        for (AlphaInfo a : alphaSpawns) {

            int pokeForm;
            if (a.form == null) {

                pokeForm = 0;

            } else {

                pokeForm = a.form;

            }
            boolean formMatches = false;
            if (form == -1 && pokeForm == 0) {

                formMatches = true;

            } else if (form == pokeForm) {

                formMatches = true;

            }
            if (a.pokemon == species && formMatches) {

                return true;

            }

        }

        return false;

    }

    public static boolean canSpawn (EntityPlayerMP player) {

        if (!amountMap.containsKey(player.getUniqueID())) return true;
        int amount = amountMap.get(player.getUniqueID());
        return amount < ConfigGetters.maxAlphas;

    }

    public static void addCount (UUID uuid) {

        int amount = 0;
        if (amountMap.containsKey(uuid)) {

            amount = amountMap.get(uuid);

        }

        int updated = amount + 1;
        amountMap.put(uuid, updated);

    }

    public static void removeCount (UUID uuid) {

        if (amountMap.containsKey(uuid)) {

            int amount = amountMap.get(uuid);
            int updated = amount - 1;
            amountMap.put(uuid, updated);

        }

    }

}
