package com.lypaka.betterpixelmonspawner.PokemonSpawningInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegendarySpawnInfo {

    private final String name;
    private final String biome;
    private final String time;
    private final String weather;
    private String levelRange;
    private String groupSize;
    private final double spawnChance;
    private final String spawnLocation;
    private String texture;
    private String heldItemID;
    private boolean hostile;
    public static Map<String, List<LegendarySpawnInfo>> infoMap = new HashMap<>();

    public LegendarySpawnInfo (String name, String biome, String time, String weather, String levelRange, String groupSize, String spawnChance, String spawnLocation, String texture, String heldItemID, boolean hostile) {

        this.name = name;
        this.biome = biome;
        this.time = time;
        this.weather = weather;
        this.levelRange = levelRange;
        this.groupSize = groupSize;
        this.spawnChance = Double.parseDouble(spawnChance);
        this.spawnLocation = spawnLocation;
        this.texture = texture;
        this.heldItemID = heldItemID;
        this.hostile = hostile;

    }

    public void register() {

        List<LegendarySpawnInfo> list = new ArrayList<>();
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

    public String getTime() {

        return this.time;

    }

    public String getWeather() {

        return this.weather;

    }

    public String getLevelRange() {

        return this.levelRange;

    }

    public void setLevelRange (String levelRange) {

        this.levelRange = levelRange;

    }

    public String getGroupSize() {

        return this.groupSize;

    }

    public void setGroupSize (String groupSize) {

        this.groupSize = groupSize;

    }

    public double getSpawnChance() {

        return this.spawnChance;

    }

    public String getSpawnLocation() {

        return this.spawnLocation;

    }

    public String getTexture() {

        return this.texture;

    }

    public void setTexture (String texture) {

        this.texture = texture;

    }

    public String getHeldItemID() {

        return this.heldItemID;

    }

    public void setHeldItemID (String id) {

        this.heldItemID = id;

    }

    public boolean isHostile() {

        return this.hostile;

    }

    public void setHostile (boolean hostile) {

        this.hostile = hostile;

    }

}
