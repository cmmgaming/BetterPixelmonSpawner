package com.lypaka.betterpixelmonspawner;

import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZoneRegistry;
import com.lypaka.betterpixelmonspawner.Commands.PixelmonSpawnerCommand;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Config.ConfigManager;
import com.lypaka.betterpixelmonspawner.Config.ConfigUpdater;
import com.lypaka.betterpixelmonspawner.Config.PokemonConfig;
import com.lypaka.betterpixelmonspawner.Holidays.HolidayHandler;
import com.lypaka.betterpixelmonspawner.Listeners.*;
import com.lypaka.betterpixelmonspawner.PokeClear.ClearTask;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.InfoRegistry;
import com.lypaka.betterpixelmonspawner.Spawners.*;
import com.lypaka.betterpixelmonspawner.Utils.HeldItemUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.BossPokemonUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod(
        modid = BetterPixelmonSpawner.MOD_ID,
        name = BetterPixelmonSpawner.MOD_NAME,
        version = BetterPixelmonSpawner.VERSION,
        acceptableRemoteVersions = "*",
        dependencies = "required-after:lypakautils"
)
public class BetterPixelmonSpawner {

    public static final String MOD_ID = "betterpixelmonspawner";
    public static final String MOD_NAME = "BetterPixelmonSpawner";
    public static final String VERSION = "1.6.0";
    public static Logger logger = LogManager.getLogger("Better Pixelmon Spawner");
    public static Path dir;
    public static List<String> alolans = new ArrayList<>();
    public static List<String> galarians = new ArrayList<>();
    public static List<String> hisuians = new ArrayList<>();
    public static Random random = new Random();
    public static LocalDate currentDay = LocalDate.now();
    public static MinecraftServer server;
    public static List<DeadZone> deadZones = new ArrayList<>();

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) throws ObjectMappingException {

        dir = event.getModConfigurationDirectory().toPath().resolve(MOD_ID);
        ConfigManager.setup(dir);
        ConfigUpdater.updateConfig();
        ConfigGetters.load();
        PokemonConfig.setup(dir.resolve("pokemon"));
        loadRegionalLists();

    }

    @Mod.EventHandler
    public void onServerStarting (FMLServerStartingEvent event) throws ObjectMappingException {

        event.registerServerCommand(new PixelmonSpawnerCommand());
        logger.info("Registering Pokemon spawns...");
        InfoRegistry.loadPokemonSpawnData();
        logger.info("Registering Boss Pokemon spawns...");
        if (Loader.isModLoaded("betterbosses")) {

            if (!com.lypaka.betterbosses.Config.ConfigGetters.disableDefaultBosses) {

                BossPokemonUtils.loadBossList();

            }

        } else {

            BossPokemonUtils.loadBossList();

        }

        // Loads holidays from config, not worth having config getters for it since it only loads on startup unless reload command is ran
        HolidayHandler.loadHolidays();

        // Loads in the held item registry for all Pokemon that have held item data
        HeldItemUtils.load();

    }

    @Mod.EventHandler
    public void onServerStarted (FMLServerStartedEvent event) throws ObjectMappingException {

        server = FMLCommonHandler.instance().getMinecraftServerInstance();
        logger.info("Starting spawners...");
        PokemonSpawner.startTimer();
        LegendarySpawner.startTimer();
        NPCSpawner.startTimer();
        MiscSpawner.startTimer();

        ClearTask.startClearTask();

        MinecraftForge.EVENT_BUS.register(new BattleListener());
        MinecraftForge.EVENT_BUS.register(new CaptureListener());
        MinecraftForge.EVENT_BUS.register(new DefeatListener());
        MinecraftForge.EVENT_BUS.register(new DespawnListener());
        MinecraftForge.EVENT_BUS.register(new JoinListener());
        MinecraftForge.EVENT_BUS.register(new PokemonSpawnListener());
        MinecraftForge.EVENT_BUS.register(new ShinySpawnListener());
        MinecraftForge.EVENT_BUS.register(new FishSpawner());
        MinecraftForge.EVENT_BUS.register(new RespawnListener());
        MinecraftForge.EVENT_BUS.register(new CommandListener());

        // Loads the areas
        DeadZoneRegistry.loadAreas();

    }

    @Mod.EventHandler
    public void onShutDown (FMLServerStoppingEvent event) {

        ConfigManager.getConfigNode(6, "Legendary-Opt-Out").setValue(ConfigGetters.legendaryOptOut);
        ConfigManager.getConfigNode(6, "Misc-Opt-Out").setValue(ConfigGetters.miscOptOut);
        ConfigManager.getConfigNode(6, "NPC-Opt-Out").setValue(ConfigGetters.npcOptOut);
        ConfigManager.getConfigNode(6, "Pokemon-Opt-Out").setValue(ConfigGetters.pokemonOptOut);
        ConfigManager.getConfigNode(6, "Spawner-Filter").setValue(ConfigGetters.locationMap);
        ConfigManager.save();

    }

    private static void loadRegionalLists() {

        alolans.add("Rattata");
        alolans.add("Raticate");
        alolans.add("Raichu");
        alolans.add("Sandshrew");
        alolans.add("Sandslash");
        alolans.add("Vulpix");
        alolans.add("Ninetales");
        alolans.add("Diglett");
        alolans.add("Dugtrio");
        alolans.add("Meowth");
        alolans.add("Persian");
        alolans.add("Geodude");
        alolans.add("Graveler");
        alolans.add("Golem");
        alolans.add("Grimer");
        alolans.add("Muk");
        alolans.add("Exeggutor");
        alolans.add("Marowak");

        galarians.add("Meowth");
        galarians.add("Ponyta");
        galarians.add("Rapidash");
        galarians.add("Slowpoke");
        galarians.add("Slowbro");
        galarians.add("Farfetchd");
        galarians.add("Weezing");
        galarians.add("MrMime");
        galarians.add("Articuno");
        galarians.add("Zapdos");
        galarians.add("Moltres");
        galarians.add("Slowking");
        galarians.add("Corsola");
        galarians.add("Zigzagoon");
        galarians.add("Linoone");
        galarians.add("Darumaka");
        galarians.add("Darmanitan");
        galarians.add("Yamask");
        galarians.add("Stunfisk");

        hisuians.add("Growlithe");
        hisuians.add("Arcanine");
        hisuians.add("Voltorb");
        hisuians.add("Electrode");
        hisuians.add("Typhlosion");
        hisuians.add("Qwilfish");
        hisuians.add("Sneasel");
        hisuians.add("Samurott");
        hisuians.add("Lilligant");
        hisuians.add("Zorua");
        hisuians.add("Zoroark");
        hisuians.add("Braviary");
        hisuians.add("Sliggoo");
        hisuians.add("Goodra");
        hisuians.add("Avalugg");
        hisuians.add("Decidueye");

    }

}
