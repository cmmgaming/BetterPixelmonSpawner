package com.lypaka.betterpixelmonspawner.Utils;

import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.time.LocalDateTime;

public class LegendaryInfoGetters {

    private static EntityPixelmon pokemon;
    private static String legendaryName;
    private static EntityPlayer spawnedPlayer;
    private static BlockPos spawnPos;
    public static String time;

    public static EntityPixelmon getPokemon() {

        return pokemon;

    }

    public static void setPokemon (EntityPixelmon poke) {

        pokemon = poke;

    }

    public static String getLegendaryName() {

        return legendaryName;

    }

    public static void setLegendaryName (String name) {

        legendaryName = name;

    }

    public static EntityPlayer getSpawnedPlayer() {

        return spawnedPlayer;

    }

    public static void setSpawnedPlayer (EntityPlayer player) {

        spawnedPlayer = player;

    }

    public static BlockPos getSpawnPos() {

        return spawnPos;

    }

    public static void setSpawnPos (BlockPos pos) {

        spawnPos = pos;

    }

    public static String getSpawnLocationString() {

        String name = spawnedPlayer.world.getWorldInfo().getWorldName();
        return "World: " + name + " | X: " + spawnPos.getX() + " | Y: " + spawnPos.getY() + " | Z: " + spawnPos.getZ();

    }

    public static String getTime() {

        return time;

    }

    public static void setTime (LocalDateTime now) {

        time = now.getMonth().name() + " " + now.getDayOfMonth() + ", " + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();

    }

    public static String getBiomeName (String biomeID) {

        String[] split = biomeID.split(":");
        String biomeNameMessy = split[1];
        String biomeNamePretty = "";
        if (biomeNameMessy.contains("_")) {

            String[] biomeSplit = biomeNameMessy.split("_");
            for (String s : biomeSplit) {

                biomeNamePretty = biomeNamePretty + " " + s.substring(0, 1).toUpperCase() + s.substring(1);

            }

        } else {

            biomeNamePretty = biomeNameMessy.substring(0, 1).toUpperCase() + biomeNameMessy.substring(1);

        }

        return biomeNamePretty;

    }

}
