package com.lypaka.betterpixelmonspawner.Areas;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigManager;
import com.lypaka.lypakautils.WorldMap;
import net.minecraft.world.World;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AreaRegistry {

    public static void loadAreas() throws ObjectMappingException {

        BetterPixelmonSpawner.areas = new ArrayList<>(); // clears for reload command
        Map<String, Map<String, String>> areas = ConfigManager.getConfigNode(7, "Areas").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        for (Map.Entry<String, Map<String, String>> entry : areas.entrySet()) {

            String name = entry.getKey(); // not needed, just there
            Map<String, String> actualArea = entry.getValue();
            List<String> entities = new ArrayList<>();
            if (!ConfigManager.getConfigNode(7, "Areas", name, "Entities").isVirtual()) {

                entities = ConfigManager.getConfigNode(7, "Areas", name, "Entities").getList(TypeToken.of(String.class));

            }
            int maxX = Integer.parseInt(actualArea.get("Max-X"));
            int maxY = Integer.parseInt(actualArea.get("Max-Y"));
            int maxZ = Integer.parseInt(actualArea.get("Max-Z"));
            int minX = Integer.parseInt(actualArea.get("Min-X"));
            int minY = Integer.parseInt(actualArea.get("Min-Y"));
            int minZ = Integer.parseInt(actualArea.get("Min-Z"));
            String worldName = actualArea.get("World");
            World w = null;
            for (Map.Entry<String, World> wEntry : WorldMap.worldMap.entrySet()) {

                if (wEntry.getKey().equalsIgnoreCase(worldName)) {

                    w = wEntry.getValue();
                    break;

                }

            }

            if (w != null) {

                Area area = new Area(entities, maxX, maxY, maxZ, minX, minY, minZ, w);
                area.create();
                BetterPixelmonSpawner.logger.info("Successfully registered area: " + name + "!");

            } else {

                BetterPixelmonSpawner.logger.error("Could not find world: " + worldName + " for area: " + name + "!");

            }

        }

    }

}
