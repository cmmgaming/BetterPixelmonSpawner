package com.lypaka.betterpixelmonspawner.Utils.PokemonUtils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TotemPokemonUtils {

    public static Map<UUID, Integer> amountMap = new HashMap<>();

    public static boolean spawnTotem() {

        if (ConfigGetters.totemSpawnChance == 0) return false;
        return RandomHelper.getRandomChance(ConfigGetters.totemSpawnChance);

    }

    public static boolean canSpawn (EntityPlayerMP player) {

        if (!amountMap.containsKey(player.getUniqueID())) return true;
        int amount = amountMap.get(player.getUniqueID());
        return amount < ConfigGetters.maxTotems;

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
