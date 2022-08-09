package com.lypaka.betterpixelmonspawner.Spawners;

import com.lypaka.betterpixelmonspawner.API.HostileEvent;
import com.lypaka.betterpixelmonspawner.API.Spawning.LegendarySpawnEvent;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Listeners.JoinListener;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.LegendarySpawnInfo;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.lypaka.betterpixelmonspawner.Utils.*;
import com.lypaka.betterpixelmonspawner.ExternalAbilities.*;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.LegendaryUtils;
import com.lypaka.lypakautils.WorldDimGetter;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.api.spawning.conditions.WorldTime;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.storage.PixelmonStorage;
import com.pixelmongenerations.core.storage.PlayerStorage;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.time.LocalDateTime;
import java.util.*;

public class LegendarySpawner {

    private static Timer legendaryTimer = null;
    public static LocalDateTime nextSpawnAttempt = null;
    private static long interval;
    private static int rand;

    public static void startTimer() {

        if (legendaryTimer != null) {

            legendaryTimer.cancel();

        }

        if (!ConfigGetters.legendarySpawnerEnabled) {

            return;

        } else {

            if (!ConfigGetters.removeLegendariesFromNormalSpawner) {

                BetterPixelmonSpawner.logger.info("Detected to not remove legendaries from normal spawner, so the legendary spawner is not needed. Disabling...");
                return;

            }

        }

        rand = RandomHelper.getRandomNumberBetween(ConfigGetters.legendarySpawnIntervalMin, ConfigGetters.legendarySpawnIntervalMax);
        interval = rand * 1000L;
        legendaryTimer = new Timer();
        legendaryTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                rand = RandomHelper.getRandomNumberBetween(ConfigGetters.legendarySpawnIntervalMin, ConfigGetters.legendarySpawnIntervalMax);
                interval = rand * 1000L;
                nextSpawnAttempt = LocalDateTime.now().plusSeconds(rand);
                List<EntityPlayerMP> onlinePlayers = new ArrayList<>();
                for (Map.Entry<UUID, EntityPlayerMP> entry : JoinListener.playerMap.entrySet()) {

                    if (!ConfigGetters.legendaryOptOut.contains(entry.getValue().getUniqueID().toString())) {

                        if (entry.getValue().isCreative() && ConfigGetters.ignoreCreativeLegendary) continue;
                        if (entry.getValue().isSpectator() && ConfigGetters.ignoreSpectatorLegendary) continue;
                        if (DeadZone.getAreaFromLocation(entry.getValue()) != null) {

                            DeadZone deadZone = DeadZone.getAreaFromLocation(entry.getValue());
                            List<String> entities = deadZone.getEntities();
                            if (!entities.contains("legendaries")) {

                                onlinePlayers.add(entry.getValue());

                            }

                        } else {

                            onlinePlayers.add(entry.getValue());

                        }

                    }

                }

                if (onlinePlayers.size() == 0) return;
                EntityPlayerMP player;
                if (onlinePlayers.size() == 1) {

                    player = onlinePlayers.get(0);

                } else {

                    player = onlinePlayers.get(BetterPixelmonSpawner.random.nextInt(onlinePlayers.size()));

                }
                String worldName = player.world.getWorldInfo().getWorldName();
                if (ConfigGetters.worldBlacklist.contains(worldName)) return;
                String biomeID = player.world.getBiome(player.getPosition()).getRegistryName().toString();
                if (!BiomeList.biomesToPokemon.containsKey(biomeID)) return;
                if (!BiomeList.biomeLegendaryMap.containsKey(biomeID)) return;

                List<LegendarySpawnInfo> possibleSpawns = new ArrayList<>();
                List<String> usedNames = new ArrayList<>();
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                    String location;
                    PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(player.getUniqueID()).get();
                    EntityPixelmon firstPartyPokemon = null;
                    for (int i = 0; i < 6; i++) {

                        EntityPixelmon p = party.getPokemon(party.getIDFromPosition(i), player.world);
                        if (p != null) {

                            firstPartyPokemon = p;
                            break;

                        }

                    }
                    if (!ConfigGetters.locationMap.containsKey(player.getUniqueID().toString())) {

                        if (player.getRidingEntity() != null) {

                            Entity mount = player.getRidingEntity();
                            if (mount.isInWater()) {

                                location = "water";

                            } else if (mount.onGround) {

                                if (mount.getPosition().getY() <= 63) {

                                    location = "underground";

                                } else {

                                    location = "land";

                                }

                            } else {

                                location = "air";

                            }

                        } else {

                            if (player.isInWater()) {

                                location = "water";

                            } else if (player.onGround) {

                                if (player.getPosition().getY() <= 63) {

                                    location = "underground";

                                } else {

                                    location = "land";

                                }

                            } else {

                                location = "air";

                            }

                        }

                    } else {

                        location = ConfigGetters.locationMap.get(player.getUniqueID().toString());

                    }
                    EntityPixelmon finalFirstPartyPokemon = firstPartyPokemon;
                    if (!ConfigGetters.legendarySpawnFilterEnabled) {

                        for (String name : BiomeList.biomesToPokemon.get(biomeID)) {

                            for (Map.Entry<String, List<LegendarySpawnInfo>> entry : LegendarySpawnInfo.infoMap.entrySet()) {

                                if (entry.getKey().equalsIgnoreCase(name)) {

                                    List<LegendarySpawnInfo> infos = entry.getValue();
                                    for (LegendarySpawnInfo info : infos) {

                                        if (!usedNames.contains(info.getName())) {

                                            usedNames.add(info.getName());
                                            possibleSpawns.add(info);

                                        }

                                    }

                                }

                            }

                        }

                    } else {

                        int ticks = (int) (player.world.getWorldTime() % 24000L);
                        ArrayList<WorldTime> currentTimes = WorldTime.getCurrent(ticks);
                        String weather;
                        if (player.world.isRaining()) {

                            weather = "rain";

                        } else if (player.world.isThundering()) {

                            weather = "storm";

                        } else {

                            weather = "clear";

                        }

                        for (String name : BiomeList.biomesToPokemon.get(biomeID)) {

                            for (Map.Entry<String, List<LegendarySpawnInfo>> entry : LegendarySpawnInfo.infoMap.entrySet()) {

                                if (entry.getKey().equalsIgnoreCase(name)) {

                                    List<LegendarySpawnInfo> infos = entry.getValue();
                                    for (LegendarySpawnInfo info : infos) {

                                        if (currentTimes.contains(WorldTime.valueOf(info.getTime().toUpperCase()))) {

                                            if (info.getWeather().equalsIgnoreCase(weather)) {

                                                if (info.getSpawnLocation().contains(location)) {

                                                    if (!usedNames.contains(info.getName())) {

                                                        if (RandomHelper.getRandomChance(info.getSpawnChance())) {

                                                            possibleSpawns.add(info);
                                                            usedNames.add(info.getName());

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                    if (possibleSpawns.size() == 0) {

                        BetterPixelmonSpawner.logger.info("A legendary was not spawned!");
                        return;

                    }

                    Map<Double, UUID> spawnChanceMap = new HashMap<>();
                    Map<UUID, LegendarySpawnInfo> pokemonSpawnInfoMap = new HashMap<>();
                    List<Double> spawnChances = new ArrayList<>(possibleSpawns.size());
                    int spawnIndex = 0;
                    double spawnChanceModifier = 1.0;
                    if (finalFirstPartyPokemon != null) {

                        if (ArenaTrap.applies(finalFirstPartyPokemon) || Illuminate.applies(finalFirstPartyPokemon) || NoGuard.applies(finalFirstPartyPokemon)) {

                            spawnChanceModifier = 2.0;

                        } else if (Infiltrator.applies(finalFirstPartyPokemon) || QuickFeet.applies(finalFirstPartyPokemon) || Stench.applies(finalFirstPartyPokemon) || WhiteSmoke.applies(finalFirstPartyPokemon)) {

                            spawnChanceModifier = 0.5;

                        }

                    }
                    for (LegendarySpawnInfo info : possibleSpawns) {

                        UUID randUUID = UUID.randomUUID();
                        double spawnChance = info.getSpawnChance() * spawnChanceModifier;
                        spawnChanceMap.put(spawnChance, randUUID);
                        pokemonSpawnInfoMap.put(randUUID, info);
                        spawnChances.add(spawnIndex, spawnChance);
                        spawnIndex++;

                    }
                    Collections.sort(spawnChances);
                    int spawnAmount = possibleSpawns.size();
                    LegendarySpawnInfo selectedSpawn = null;
                    List<LegendarySpawnInfo> names = new ArrayList<>();
                    for (int i = 0; i < spawnAmount; i++) {

                        double spawnChance = spawnChances.get(i);
                        UUID uuid = spawnChanceMap.get(spawnChance);
                        LegendarySpawnInfo info = pokemonSpawnInfoMap.get(uuid);
                        if (RandomHelper.getRandomChance(spawnChance)) {

                            if (!names.contains(info)) {

                                names.add(info);

                            }

                        }

                    }
                    if (names.size() > 1) {

                        selectedSpawn = RandomHelper.getRandomElementFromList(names);

                    } else if (names.size() == 1) {

                        selectedSpawn = names.get(0);

                    }
                    if (FlashFire.applies(finalFirstPartyPokemon)) {

                        selectedSpawn = FlashFire.tryFlashFireOnLegendary(selectedSpawn, possibleSpawns, player);

                    } else if (Harvest.applies(finalFirstPartyPokemon)) {

                        selectedSpawn = Harvest.tryHarvestOnLegendary(selectedSpawn, possibleSpawns, player);

                    } else if (LightningRod.applies(finalFirstPartyPokemon) || Static.applies(finalFirstPartyPokemon)) {

                        selectedSpawn = LightningRod.tryLightningRodOnLegendary(selectedSpawn, possibleSpawns, player);

                    } else if (MagnetPull.applies(finalFirstPartyPokemon)) {

                        selectedSpawn = MagnetPull.tryMagnetPullOnLegendary(selectedSpawn, possibleSpawns, player);

                    } else if (StormDrain.applies(finalFirstPartyPokemon)) {

                        selectedSpawn = StormDrain.tryStormDrainOnLegendary(selectedSpawn, possibleSpawns, player);

                    }
                    if (selectedSpawn == null) return;
                    String[] levelRange = selectedSpawn.getLevelRange().split("-");
                    int max = Integer.parseInt(levelRange[0]);
                    int min = Integer.parseInt(levelRange[1]);
                    String pokemonName = selectedSpawn.getName();
                    EntityPixelmon pokemon;
                    pokemonName = pokemonName.replace(".conf", "");
                    if (pokemonName.contains("-")) {

                        if (pokemonName.equalsIgnoreCase("porygon-z")) {

                            pokemonName = "porygon-z";
                            pokemon = PokemonSpec.from(pokemonName).create(player.world);

                        } else {

                            String[] split = pokemonName.split("-");
                            pokemonName = split[0];
                            String form = "";
                            for (int f = 1; f < split.length; f++) {

                                form = form + "-" + split[f];

                            }

                            pokemon = PokemonSpec.from(pokemonName).create(player.world);
                            int pokemonForm = FormIndexFromName.getFormNumberFromFormName(pokemonName, form);
                            pokemon.setForm(pokemonForm, true);

                        }

                    } else {

                        pokemon = PokemonSpec.from(pokemonName).create(player.world);

                    }
                    int level = RandomHelper.getRandomNumberBetween(min, max);
                    if (Hustle.applies(finalFirstPartyPokemon) || Pressure.applies(finalFirstPartyPokemon) || VitalSpirit.applies(finalFirstPartyPokemon)) {

                        level = Hustle.tryApplyHustle(level, selectedSpawn);

                    }
                    pokemon.getLvl().setLevel(level);
                    if (Intimidate.applies(finalFirstPartyPokemon) || KeenEye.applies(finalFirstPartyPokemon)) {

                        pokemon = Intimidate.tryIntimidate(pokemon, finalFirstPartyPokemon);
                        if (pokemon == null) return;

                    }
                    pokemon.updateStats();

                    if (selectedSpawn.getHeldItemID() != null) {

                        pokemon.heldItem = new ItemStack(Item.getByNameOrId(selectedSpawn.getHeldItemID()));

                    } else {

                        if (ConfigGetters.heldItemsEnabled) {

                            HeldItemUtils.tryApplyHeldItem(pokemonName, pokemon, finalFirstPartyPokemon);

                        }

                    }

                    if (selectedSpawn.getTexture() != null) {

                        pokemon.setCustomSpecialTexture(selectedSpawn.getTexture());

                    }

                    int xzRadius = ConfigGetters.legendarySpawnLocationXZ;
                    int yRadius = ConfigGetters.legendarySpawnLocationY;
                    int spawnX;
                    int spawnY;
                    int spawnZ;

                    if (RandomHelper.getRandomChance(50)) {

                        spawnX = player.getPosition().getX() + xzRadius;

                    } else {

                        spawnX = player.getPosition().getX() - xzRadius;

                    }
                    if (RandomHelper.getRandomChance(50)) {

                        spawnZ = player.getPosition().getZ() + xzRadius;

                    } else {

                        spawnZ = player.getPosition().getZ() - xzRadius;

                    }

                    BlockPos safeSpawn;
                    if (location.equalsIgnoreCase("air")) {

                        spawnY = player.getPosition().getY() + yRadius;
                        safeSpawn = new BlockPos(spawnX, spawnY, spawnZ);

                    } else {

                        spawnY = player.getPosition().getY();
                        BlockPos baseSpawn = new BlockPos(spawnX, spawnY, spawnZ);
                        if (!location.equalsIgnoreCase("underground")) {

                            if (WorldDimGetter.getDimID(player.world, player) == -1) {

                                safeSpawn = baseSpawn;

                            } else {

                                safeSpawn = new BlockPos(spawnX, player.world.getTopSolidOrLiquidBlock(baseSpawn).getY(), spawnZ);

                            }

                        } else {

                            safeSpawn = baseSpawn; // not the safest thing to do as Pokemon could spawn in blocks underground, but there's not a lot of space down there anyway

                        }

                    }
                    if (CuteCharm.applies(finalFirstPartyPokemon)) {

                        CuteCharm.tryApplyCuteCharmEffect(pokemon, finalFirstPartyPokemon);

                    }
                    if (Synchronize.applies(finalFirstPartyPokemon)) {

                        Synchronize.applySynchronize(pokemon, finalFirstPartyPokemon);

                    }
                    pokemon.setLocationAndAngles(safeSpawn.getX() + BetterPixelmonSpawner.random.nextDouble(), safeSpawn.getY(), safeSpawn.getZ() + BetterPixelmonSpawner.random.nextDouble(),0, 0);
                    boolean hostile = false;
                    if (selectedSpawn.isHostile()) {

                        hostile = true;
                        HostileEvent hostileEvent = new HostileEvent(pokemon, player, selectedSpawn);
                        MinecraftForge.EVENT_BUS.post(hostileEvent);
                        if (hostileEvent.isCanceled()) {

                            hostile = false;

                        }

                    }
                    pokemon.setHostile(hostile);

                    LegendarySpawnEvent legendarySpawnEvent = new LegendarySpawnEvent(pokemon, player, selectedSpawn);
                    MinecraftForge.EVENT_BUS.post(legendarySpawnEvent);
                    if (!legendarySpawnEvent.isCanceled()) {

                        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                            player.world.spawnEntity(legendarySpawnEvent.getPokemon());
                            LegendaryUtils.handleGlowing(legendarySpawnEvent.getPokemon());
                            LegendaryUtils.handleGracePeriod(legendarySpawnEvent.getPokemon(), legendarySpawnEvent.getPlayer());
                            LegendaryInfoGetters.setLegendaryName(legendarySpawnEvent.getPokemon().getPokemonName());
                            LegendaryInfoGetters.setPokemon(legendarySpawnEvent.getPokemon());
                            LegendaryInfoGetters.setSpawnedPlayer(player);
                            LegendaryInfoGetters.setSpawnPos(legendarySpawnEvent.getPokemon().getPosition());
                            LegendaryInfoGetters.setTime(LocalDateTime.now());
                            LegendaryListing.updateListingConfig(legendarySpawnEvent.getPokemon());
                            legendarySpawnEvent.getPokemon().addTag("SpawnedLegendary"); // used for the last legend list, so the event listeners know what is a BPS spawned legendary
                            if (!ConfigGetters.legendarySpawnMessage.equalsIgnoreCase("")) {

                                BetterPixelmonSpawner.server.getPlayerList().sendMessage(FancyText.getFancyText(ConfigGetters.legendarySpawnMessage
                                        .replace("%biome%", getPrettyBiomeName(legendarySpawnEvent.getPokemon().world.getBiome(legendarySpawnEvent.getPokemon().getPosition()).getRegistryName().toString()))
                                        .replace("%pokemon%", legendarySpawnEvent.getPokemon().getPokemonName())
                                        .replace("%player%", legendarySpawnEvent.getPlayer().getName())
                                ));

                            }

                        });

                    }

                });

            }

        }, interval, interval);

    }

    public static String getPrettyBiomeName (String biomeID) {

        String[] split = biomeID.split(":");
        String name = "";
        if (split[1].contains("_")) {

            String[] fullName = split[1].split("_");
            for (String s : fullName) {

                String word = s.substring(0, 1).toUpperCase() + s.substring(1);
                name = name + word + " ";

            }

            name = name.substring(0, name.length() - 1); // removes that last space

        } else {

            name = split[1].substring(0, 1).toUpperCase() + split[1].substring(1);

        }

        return name;

    }

}
