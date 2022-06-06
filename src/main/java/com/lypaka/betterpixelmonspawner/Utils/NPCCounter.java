package com.lypaka.betterpixelmonspawner.Utils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.npcs.EntityNPC;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPCCounter {

    public static Map<UUID, Integer> npcCountMap = new HashMap<>();

    public static int getCount (UUID uuid) {

        int count = 0;
        if (npcCountMap.containsKey(uuid)) {

            count = npcCountMap.get(uuid);

        }

        return count;

    }

    public static void increment (EntityNPC npc, UUID uuid) {

        if (ConfigGetters.maxNPCs <= 0) return;
        int count = getCount(uuid);
        int updated = count + 1;
        npcCountMap.put(uuid, updated);
        npc.addTag("SpawnedPlayerUUID:" + uuid.toString());

    }

    public static void decrement (UUID uuid) {

        if (ConfigGetters.maxNPCs <= 0) return;
        int count = getCount(uuid);
        int updated = Math.max(0, count - 1);
        npcCountMap.put(uuid, updated);

    }

}
