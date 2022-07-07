package com.lypaka.betterpixelmonspawner.Utils.Counters;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import net.minecraft.entity.EntityLivingBase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiscCounter {

    public static Map<UUID, Integer> miscCountMap = new HashMap<>();

    public static int getCount (UUID uuid) {

        int count = 0;
        if (miscCountMap.containsKey(uuid)) {

            count = miscCountMap.get(uuid);

        }

        return count;

    }

    public static void increment (EntityLivingBase entity, UUID uuid) {

        if (ConfigGetters.maxMiscEntities <= 0) return;
        int count = getCount(uuid);
        int updated = count + 1;
        miscCountMap.put(uuid, updated);
        entity.addTag("SpawnedPlayerUUID:" + uuid.toString());

    }

    public static void decrement (UUID uuid) {

        if (ConfigGetters.maxMiscEntities <= 0) return;
        int count = getCount(uuid);
        int updated = Math.max(0, count - 1);
        miscCountMap.put(uuid, updated);

    }

}
