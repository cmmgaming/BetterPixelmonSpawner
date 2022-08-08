package com.lypaka.betterpixelmonspawner.PokemonSpawningInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FishingSpawnInfo {

    private final String name;
    private final String biome;
    private final String location;
    private final String rod;
    private final String time;
    private final String weather;
    private final String levelRange;
    private final double spawnChance;
    private final String heldItem;
    public static Map<String, List<FishingSpawnInfo>> infoMap = new HashMap<>();

    public FishingSpawnInfo (String name, String biome, String location, String rod, String time, String weather, String levelRange, double spawnChance, String heldItem) {

        this.name = name;
        this.biome = biome;
        this.location = location;
        this.rod = rod;
        this.time = time;
        this.weather = weather;
        this.levelRange = levelRange;
        this.spawnChance = spawnChance;
        this.heldItem = heldItem;

    }

    public void register() {

        List<FishingSpawnInfo> list = new ArrayList<>();
        if (infoMap.containsKey(this.name)) {

            list = infoMap.get(this.name);

        }

        list.add(this);
        infoMap.put(this.name, list);

    }

    public String getName() {

        return this.name;

    }

    public String getBiome() {

        return this.biome;

    }

    public String getLocation() {

        return this.location;

    }

    public String getRod() {

        return this.rod;

    }

    public String getTime() {

        return this.time;

    }

    public String getWeather() {

        return this.weather;

    }

    public String getLevelRange() {

        return this.levelRange;

    }

    public double getSpawnChance() {

        return this.spawnChance;

    }

    public String getHeldItemID() {

        return this.heldItem;

    }

}
