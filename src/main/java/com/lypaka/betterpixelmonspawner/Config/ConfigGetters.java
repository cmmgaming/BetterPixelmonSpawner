package com.lypaka.betterpixelmonspawner.Config;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigGetters {

    public static boolean pokeClearEnabled;
    public static int pokeClearInterval;
    public static String pokeClearMessage;
    public static List<String> blacklistedClearPokemon;
    public static int clearWarningInterval;
    public static String clearWarningMessage;
    public static List<String> worldBlacklist;

    public static double alphaSpawnChance;
    public static int blocksBeforePokemonIncrease;
    public static boolean bossesCanBeNormal;
    public static double bossSpawnChance;
    public static Map<String, Double> bossSpawnMap;
    public static boolean enableGroupSize;
    public static boolean externalAbilitiesEnabled;
    public static double gmaxChance;
    public static boolean heldItemsEnabled;
    public static boolean ignoreCreativePokemon;
    public static boolean ignoreSpectatorPokemon;
    public static int pokemonLevelModifier;
    public static boolean marksEnabled;
    public static int maxPokemonScaleLevel;
    public static int maxPokemon;
    public static String highIVSoundID;
    public static double aggressiveChance;
    public static List<String> blacklistedSpawnPokemon;
    public static boolean removeEternatus;
    public static boolean removeLegendariesFromNormalSpawner;
    public static boolean removeMeltan;
    public static boolean scalePokemonLevelsByDistance;
    public static boolean spawnerEnabled;
    public static int spawnInterval;
    public static int xzRadius;
    public static int yRadius;
    public static double totemSpawnChance;
    public static boolean validateSpawns;

    public static boolean legendarySpawnerEnabled;
    public static boolean ignoreCreativeLegendary;
    public static boolean ignoreSpectatorLegendary;
    public static double legendarySpawnChance;
    public static boolean legendarySpawnFilterEnabled;
    public static int legendarySpawnLocationXZ;
    public static int legendarySpawnLocationY;
    public static int legendaryGracePeriod;
    public static int legendarySpawnIntervalMax;
    public static int legendarySpawnIntervalMin;
    public static String legendarySpawnMessage;
    public static boolean legendarySpawnsGlowing;
    public static List<String> specialLegendaries;

    public static int blocksBeforeNPCIncrease;
    public static int npcDespawnTimer;
    public static boolean npcSpawnerEnabled;
    public static boolean ignoreCreativeNPC;
    public static boolean ignoreSpectatorNPC;
    public static int npcLevelModifier;
    public static int maxNPCs;
    public static int maxNPCScaleLevel;
    public static boolean scaleNPCLevelsByDistance;
    public static int npcSpawnInterval;
    public static int npcSpawnLocationXZ;
    public static Map<String, Double> npcSpawnMap;
    public static boolean unsafeSpawnLocations;

    public static Map<String, String> miscBroadcastMap;
    public static int miscDespawnTimer;
    public static boolean miscSpawnerEnabled;
    public static boolean ignoreCreativeMisc;
    public static boolean ignoreSpectatorMisc;
    public static int maxMiscEntities;
    public static int miscEntityInterval;
    public static int miscRadiusXZ;
    public static Map<String, Double> miscEntitySpawnMap;
    public static boolean unsafeMiscSpawnLocations;

    public static double particleChance;
    public static String holidaySpawnMessage;
    public static double textureChance;

    public static List<String> legendaryOptOut;
    public static List<String> miscOptOut;
    public static List<String> npcOptOut;
    public static List<String> pokemonOptOut;
    public static Map<String, String> locationMap;

    public static int listGUIRows;
    public static String listGUITitle;
    public static Map<String, Map<String, String>> listGUISlotMap;
    public static int listAmount;
    public static int nextUpdatedSlot;
    public static int timeSetting;
    public static Map<String, Map<String, String>> lastSpawnMap;

    public static void load() throws ObjectMappingException {

        pokeClearEnabled = ConfigManager.getConfigNode(0, "PokeClear", "Enabled").getBoolean();
        pokeClearInterval = ConfigManager.getConfigNode(0, "PokeClear", "Interval").getInt();
        pokeClearMessage = ConfigManager.getConfigNode(0, "PokeClear", "Message").getString();
        blacklistedClearPokemon = ConfigManager.getConfigNode(0, "PokeClear", "Pokemon-Blacklist").getList(TypeToken.of(String.class));
        clearWarningInterval = ConfigManager.getConfigNode(0, "PokeClear", "Warning-Interval").getInt();
        clearWarningMessage = ConfigManager.getConfigNode(0, "PokeClear", "Warning-Message").getString();
        worldBlacklist = ConfigManager.getConfigNode(0, "World-Blacklist").getList(TypeToken.of(String.class));

        particleChance = ConfigManager.getConfigNode(1, "General-Settings", "Particle-Chance").getDouble();
        holidaySpawnMessage = ConfigManager.getConfigNode(1, "General-Settings", "Spawn-Message").getString();
        textureChance = ConfigManager.getConfigNode(1, "General-Settings", "Texture-Chance").getDouble();

        alphaSpawnChance = ConfigManager.getConfigNode(2, "Alpha-Spawn-Chance").getDouble();
        blocksBeforePokemonIncrease = ConfigManager.getConfigNode(2, "Blocks-Before-Level-Increase").getInt();
        bossesCanBeNormal = ConfigManager.getConfigNode(2, "Bosses-Can-Be-Normals").getBoolean();
        bossSpawnChance = ConfigManager.getConfigNode(2, "Boss-Spawn-Chance").getDouble();
        bossSpawnMap = ConfigManager.getConfigNode(2, "Boss-Spawn-Map").getValue(new TypeToken<Map<String, Double>>() {});
        enableGroupSize = ConfigManager.getConfigNode(2, "Enable-Group-Size").getBoolean();
        externalAbilitiesEnabled = ConfigManager.getConfigNode(2, "External-Abilities").getBoolean();
        gmaxChance = ConfigManager.getConfigNode(2, "GMax-Factor-Chance").getDouble();
        heldItemsEnabled = ConfigManager.getConfigNode(2, "Held-Items").getBoolean();
        ignoreCreativePokemon = ConfigManager.getConfigNode(2, "Ignore-Creative").getBoolean();
        ignoreSpectatorPokemon = ConfigManager.getConfigNode(2, "Ignore-Spectator").getBoolean();
        pokemonLevelModifier = ConfigManager.getConfigNode(2, "Level-Modifier").getInt();
        marksEnabled = ConfigManager.getConfigNode(2, "Marks-Enabled").getBoolean();
        maxPokemonScaleLevel = ConfigManager.getConfigNode(2, "Max-Scaled-Level").getInt();
        maxPokemon = ConfigManager.getConfigNode(2, "Max-Pokemon-Per-Player").getInt();
        highIVSoundID = ConfigManager.getConfigNode(2, "Play-High-IV-Sound").getString();
        aggressiveChance = ConfigManager.getConfigNode(2, "Pokemon-Aggressive").getDouble();
        blacklistedSpawnPokemon = ConfigManager.getConfigNode(2, "Pokemon-Blacklist").getList(TypeToken.of(String.class));
        removeEternatus = ConfigManager.getConfigNode(2, "Remove-Eternatus-From-Normal-Spawner").getBoolean();
        removeLegendariesFromNormalSpawner = ConfigManager.getConfigNode(2, "Remove-Legendaries-From-Normal-Spawner").getBoolean();
        removeMeltan = ConfigManager.getConfigNode(2, "Remove-Meltan-From-Normal-Spawner").getBoolean();
        scalePokemonLevelsByDistance = ConfigManager.getConfigNode(2, "Scale-Levels-By-Distance").getBoolean();
        spawnerEnabled = ConfigManager.getConfigNode(2, "Spawner-Enabled").getBoolean();
        spawnInterval = ConfigManager.getConfigNode(2, "Spawn-Interval").getInt();
        xzRadius = ConfigManager.getConfigNode(2, "Spawn-Location-XZ").getInt();
        yRadius = ConfigManager.getConfigNode(2, "Spawn-Location-Y").getInt();
        totemSpawnChance = ConfigManager.getConfigNode(2, "Totem-Spawn-Chance").getDouble();
        validateSpawns = ConfigManager.getConfigNode(2, "Validate-Spawns").getBoolean();

        legendarySpawnerEnabled = ConfigManager.getConfigNode(3, "Enabled").getBoolean();
        ignoreCreativeLegendary = ConfigManager.getConfigNode(3, "Ignore-Creative").getBoolean();
        ignoreSpectatorLegendary = ConfigManager.getConfigNode(3, "Ignore-Spectator").getBoolean();
        legendarySpawnChance = ConfigManager.getConfigNode(3, "Spawn-Chance").getDouble();
        legendarySpawnFilterEnabled = ConfigManager.getConfigNode(3, "Spawn-Filter-Enabled").getBoolean();
        legendaryGracePeriod = ConfigManager.getConfigNode(3, "Spawn-Grace-Period").getInt();
        legendarySpawnIntervalMax = ConfigManager.getConfigNode(3, "Spawn-Interval-Max").getInt();
        legendarySpawnIntervalMin = ConfigManager.getConfigNode(3, "Spawn-Interval-Min").getInt();
        legendarySpawnLocationXZ = ConfigManager.getConfigNode(3, "Spawn-Location-XZ").getInt();
        legendarySpawnLocationY = ConfigManager.getConfigNode(3, "Spawn-Location-Y").getInt();
        legendarySpawnMessage = ConfigManager.getConfigNode(3, "Spawn-Message").getString();
        legendarySpawnsGlowing = ConfigManager.getConfigNode(3, "Spawn-Pokemon-Glowing").getBoolean();
        specialLegendaries = ConfigManager.getConfigNode(3, "Special-Spawns").getList(TypeToken.of(String.class));

        blocksBeforeNPCIncrease = ConfigManager.getConfigNode(4, "Blocks-Before-Level-Increase").getInt();
        npcDespawnTimer = ConfigManager.getConfigNode(4, "Despawn-Timer").getInt();
        npcSpawnerEnabled = ConfigManager.getConfigNode(4, "Enabled").getBoolean();
        ignoreCreativeNPC = ConfigManager.getConfigNode(4, "Ignore-Creative").getBoolean();
        ignoreSpectatorNPC = ConfigManager.getConfigNode(4, "Ignore-Spectator").getBoolean();
        npcLevelModifier = ConfigManager.getConfigNode(4, "Level-Modifier").getInt();
        maxNPCs = ConfigManager.getConfigNode(4, "Max-NPCs").getInt();
        maxNPCScaleLevel = ConfigManager.getConfigNode(4, "Max-Scaled-Level").getInt();
        scaleNPCLevelsByDistance = ConfigManager.getConfigNode(4, "Scale-Levels-By-Distance").getBoolean();
        npcSpawnInterval = ConfigManager.getConfigNode(4, "Spawn-Interval").getInt();
        npcSpawnLocationXZ = ConfigManager.getConfigNode(4, "Spawn-Location-XZ").getInt();
        npcSpawnMap = ConfigManager.getConfigNode(4, "Spawn-Map").getValue(new TypeToken<Map<String, Double>>() {});
        unsafeSpawnLocations = ConfigManager.getConfigNode(4, "Unsafe-Spawn-Locations").getBoolean();

        miscBroadcastMap = ConfigManager.getConfigNode(5, "Broadcasts").getValue(new TypeToken<Map<String, String>>() {});
        miscDespawnTimer = ConfigManager.getConfigNode(5, "Despawn-Timer").getInt();
        miscSpawnerEnabled = ConfigManager.getConfigNode(5, "Enabled").getBoolean();
        ignoreCreativeMisc = ConfigManager.getConfigNode(5, "Ignore-Creative").getBoolean();
        ignoreSpectatorMisc = ConfigManager.getConfigNode(5, "Ignore-Spectator").getBoolean();
        maxMiscEntities = ConfigManager.getConfigNode(5, "Max-Entities").getInt();
        miscEntityInterval = ConfigManager.getConfigNode(5, "Spawn-Interval").getInt();
        miscRadiusXZ = ConfigManager.getConfigNode(5, "Spawn-Location-XZ").getInt();
        miscEntitySpawnMap = ConfigManager.getConfigNode(5, "Spawn-Map").getValue(new TypeToken<Map<String, Double>>() {});
        unsafeMiscSpawnLocations = ConfigManager.getConfigNode(5, "Unsafe-Spawn-Locations").getBoolean();

        legendaryOptOut = new ArrayList<>(ConfigManager.getConfigNode(6, "Legendary-Opt-Out").getList(TypeToken.of(String.class)));
        miscOptOut = new ArrayList<>(ConfigManager.getConfigNode(6, "Misc-Opt-Out").getList(TypeToken.of(String.class)));
        npcOptOut = new ArrayList<>(ConfigManager.getConfigNode(6, "NPC-Opt-Out").getList(TypeToken.of(String.class)));
        pokemonOptOut = new ArrayList<>(ConfigManager.getConfigNode(6, "Pokemon-Opt-Out").getList(TypeToken.of(String.class)));
        locationMap = ConfigManager.getConfigNode(6, "Spawner-Filter").getValue(new TypeToken<Map<String, String>>() {});

        listGUIRows = ConfigManager.getConfigNode(8, "GUI-Settings", "Rows").getInt();
        listGUITitle = ConfigManager.getConfigNode(8, "GUI-Settings", "Title").getString();
        listGUISlotMap = ConfigManager.getConfigNode(8, "GUI-Slots").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        listAmount = ConfigManager.getConfigNode(8, "List-Settings", "Amount").getInt();
        nextUpdatedSlot = ConfigManager.getConfigNode(8, "List-Settings", "Next-Updated-Slot").getInt();
        timeSetting = ConfigManager.getConfigNode(8, "List-Settings", "Time-Setting").getInt();
        lastSpawnMap = ConfigManager.getConfigNode(8, "Spawns").getValue(new TypeToken<Map<String, Map<String, String>>>() {});

    }

}
