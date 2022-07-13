package com.lypaka.betterpixelmonspawner.Utils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Config.ConfigManager;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayer;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LegendaryListing {

    public static String getLocation() {

        String w = LegendaryInfoGetters.getPokemon().world.getSaveHandler().getWorldDirectory().getName();
        return w + "," + LegendaryInfoGetters.getSpawnPos().getX() + "," +
                LegendaryInfoGetters.getSpawnPos().getY() + "," + LegendaryInfoGetters.getSpawnPos().getZ();

    }

    public static void updateListingConfig (EntityPixelmon pokemon) {

        int amount = ConfigGetters.listAmount;
        Map<String, Map<String, String>> spawnsMap = ConfigGetters.lastSpawnMap;
        LocalDateTime now = LocalDateTime.now();

        String time = now.toString();

        for (int i = 1; i <= amount; i++) {

            if (ConfigGetters.nextUpdatedSlot == 0) {

                ConfigGetters.nextUpdatedSlot = 1;
                Map<String, String> innerMap = new HashMap<>();
                innerMap.put("Name", pokemon.getPokemonName());
                innerMap.put("UUID", pokemon.getUniqueID().toString());
                innerMap.put("Status", "Alive");
                innerMap.put("Time", time);
                innerMap.put("Location", getLocation());
                spawnsMap.put("Pokemon-" + ConfigGetters.nextUpdatedSlot, innerMap);
                ConfigManager.getConfigNode(8, "Spawns").setValue(spawnsMap);
                ConfigManager.getConfigNode(8, "Settings", "Next-Updated-Slot").setValue(2);
                ConfigGetters.nextUpdatedSlot = 2;
                break;

            } else if (ConfigGetters.nextUpdatedSlot == i) {

                Map<String, String> innerMap = new HashMap<>();
                innerMap.put("Name", pokemon.getPokemonName());
                innerMap.put("UUID", pokemon.getUniqueID().toString());
                innerMap.put("Status", "Alive");
                innerMap.put("Time", time);
                innerMap.put("Location", getLocation());
                spawnsMap.put("Pokemon-" + ConfigGetters.nextUpdatedSlot, innerMap);
                ConfigManager.getConfigNode(8, "Spawns").setValue(spawnsMap);
                int nextSlot = ConfigGetters.nextUpdatedSlot + 1;
                ConfigManager.getConfigNode(8, "Settings", "Next-Updated-Slot").setValue(nextSlot);
                ConfigGetters.nextUpdatedSlot = nextSlot;
                break;

            } else if (ConfigGetters.nextUpdatedSlot == amount) {

                Map<String, String> innerMap = new HashMap<>();
                innerMap.put("Name", pokemon.getPokemonName());
                innerMap.put("UUID", pokemon.getUniqueID().toString());
                innerMap.put("Status", "Alive");
                innerMap.put("Time", time);
                innerMap.put("Location", getLocation());
                spawnsMap.put("Pokemon-" + ConfigGetters.nextUpdatedSlot, innerMap);
                ConfigManager.getConfigNode(8, "Spawns").setValue(spawnsMap);
                ConfigManager.getConfigNode(8, "Settings", "Next-Updated-Slot").setValue(1);
                ConfigGetters.nextUpdatedSlot = 1;
                break;

            }

        }

        ConfigManager.save();

    }

    public static void updatePokemonStatusCaptured (EntityPixelmon pokemon, EntityPlayer player) throws ObjectMappingException {

        int amount = ConfigGetters.listAmount;
        Map<String, Map<String, String>> spawnsMap = ConfigGetters.lastSpawnMap;
        Map<String, String> innerMap;
        LocalDateTime now;

        for (int i = 1; i <= amount; i++) {

            innerMap = spawnsMap.get("Pokemon-" + i);
            if (innerMap.get("UUID").equalsIgnoreCase(pokemon.getUniqueID().toString())) {

                now = LocalDateTime.now();

                innerMap.put("Time", now.toString());
                innerMap.put("Status", "Captured by: " + player.getName());
                ConfigManager.getConfigNode(8, "Spawns", "Pokemon-" + i).setValue(innerMap);
                break;

            }

        }

        ConfigManager.save();

    }

    public static void updatePokemonStatusKilled (EntityPixelmon pokemon, EntityPlayer player) throws ObjectMappingException {

        int amount = ConfigGetters.listAmount;
        Map<String, Map<String, String>> spawnsMap = ConfigGetters.lastSpawnMap;
        Map<String, String> innerMap;
        LocalDateTime now;

        for (int i = 1; i <= amount; i++) {

            innerMap = spawnsMap.get("Pokemon-" + i);
            if (innerMap.get("UUID").equalsIgnoreCase(pokemon.getUniqueID().toString())) {

                now = LocalDateTime.now();

                innerMap.put("Time", now.toString());
                innerMap.put("Status", "Killed by: " + player.getName());
                ConfigManager.getConfigNode(8, "Spawns", "Pokemon-" + i).setValue(innerMap);
                break;

            }

        }

        ConfigManager.save();

    }

    public static void updatePokemonStatusDespawned (UUID pokemon) throws ObjectMappingException {

        int amount = ConfigGetters.listAmount;
        Map<String, Map<String, String>> spawnsMap = ConfigGetters.lastSpawnMap;
        Map<String, String> innerMap;
        LocalDateTime now;

        for (int i = 1; i <= amount; i++) {

            innerMap = spawnsMap.get("Pokemon-" + i);
            if (innerMap.get("UUID").equalsIgnoreCase(pokemon.toString())) {

                now = LocalDateTime.now();

                innerMap.put("Time", now.toString());
                innerMap.put("Status", "Despawned");
                ConfigManager.getConfigNode(8, "Spawns", "Pokemon-" + i).setValue(innerMap);
                break;

            }

        }

    }

    public static String getPokemonName (int listing) {

        return ConfigGetters.lastSpawnMap.get("Pokemon-" + listing).get("Name");

    }

    public static String getTime (int listing) {

        String mainTime = ConfigGetters.lastSpawnMap.get("Pokemon-" + listing).get("Time");
        String returnTime;

        if (ConfigGetters.timeSetting == 1) {

            LocalDateTime t = LocalDateTime.parse(mainTime);
            String day = String.valueOf(t.getDayOfMonth());
            String hour = String.valueOf(t.getHour());
            String minute = String.valueOf(t.getMinute());
            String second = String.valueOf(t.getSecond());
            if (t.getDayOfMonth() < 10) {

                day = "0" + t.getDayOfMonth();

            }
            if (t.getHour() < 10) {

                hour = "0" + t.getHour();

            }
            if (t.getMinute() < 10) {

                minute = "0" + t.getMinute();

            }
            if (t.getSecond() < 10) {

                second = "0" + t.getSecond();

            }

            returnTime = t.getMonth().name() + " " + day + ", " + hour + ":" + minute + ":" + second;

        } else {

            returnTime = makeTimeReadable(mainTime);

        }

        return returnTime;

    }

    public static String getStatus (int listing) {

        return ConfigGetters.lastSpawnMap.get("Pokemon-" + listing).get("Status");

    }

    private static String makeTimeReadable (String node) {

        LocalDateTime nodeTime = LocalDateTime.parse(node);
        LocalDateTime time = LocalDateTime.now();
        Duration duration = Duration.between(nodeTime, time);
        return printSeconds(duration.getSeconds());

    }

    private static String printSeconds(long seconds) {

        StringBuilder timeString = new StringBuilder();
        if (timeString.length() != 0 || seconds >= 86400) timeString.append(seconds / 86400).append(" days, ");
        if (timeString.length() != 0 || seconds >= 3600) timeString.append(seconds % 86400 / 3600).append(" hours, ");
        if (timeString.length() != 0 || seconds >= 60) timeString.append(seconds % 3600 / 60).append(" minutes, ");
        timeString.append(seconds % 60).append(" seconds");
        return timeString.toString();

    }

}
