package com.lypaka.betterpixelmonspawner.Spawners;

import com.lypaka.betterpixelmonspawner.API.HostileEvent;
import com.lypaka.betterpixelmonspawner.API.Spawning.*;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.DebugSystem.PokemonDebug;
import com.lypaka.betterpixelmonspawner.Holidays.Holiday;
import com.lypaka.betterpixelmonspawner.Holidays.HolidayHandler;
import com.lypaka.betterpixelmonspawner.Listeners.JoinListener;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.lypaka.betterpixelmonspawner.Utils.*;
import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.lypaka.betterpixelmonspawner.ExternalAbilities.*;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.AlphaPokemonUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.BossPokemonUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.PokemonUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.TotemPokemonUtils;
import com.lypaka.lypakautils.WorldDimGetter;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.api.spawning.conditions.WorldTime;
import com.pixelmongenerations.common.block.tileEntities.TileEntityScarecrow;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.common.entity.pixelmon.EnumAggression;
import com.pixelmongenerations.core.config.PixelmonConfig;
import com.pixelmongenerations.core.event.RepelHandler;
import com.pixelmongenerations.core.storage.PixelmonStorage;
import com.pixelmongenerations.core.storage.PlayerStorage;
import com.pixelmongenerations.core.util.PixelmonMethods;
import com.pixelmongenerations.core.util.helper.BlockHelper;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

import java.util.*;

public class PokemonSpawner {

    private static Timer timer = null;

    public static void startTimer() {

        if (timer != null) {

            timer.cancel();

        }

        if (!ConfigGetters.spawnerEnabled) return;

        long interval = ConfigGetters.spawnInterval * 1000L;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                for (Map.Entry<UUID, EntityPlayerMP> playerEntry : JoinListener.playerMap.entrySet()) {

                    List<String> usedNames = new ArrayList<>();
                    EntityPlayerMP player = playerEntry.getValue();
                    PokemonDebug.printPokemonDebugInformation(player);
                    if (DeadZone.getAreaFromLocation(player) != null) {

                        DeadZone deadZone = DeadZone.getAreaFromLocation(player);
                        List<String> entities = deadZone.getEntities();
                        if (entities.contains("pokemon")) continue;

                    }
                    if (ConfigGetters.pokemonOptOut.contains(player.getUniqueID().toString())) continue;
                    if (RepelHandler.hasRepel(player.getUniqueID())) continue;
                    if (PokemonCounter.getCount(player.getUniqueID()) >= ConfigGetters.maxPokemon) {

                        if (ConfigGetters.maxPokemon != 0) continue;

                    }
                    if (BlockHelper.findClosestTileEntity(TileEntityScarecrow.class, player, PixelmonConfig.scarecrowRadius, s -> true) != null) {

                        continue;

                    }
                    if (player.isCreative() && ConfigGetters.ignoreCreativePokemon) continue;
                    if (player.isSpectator() && ConfigGetters.ignoreSpectatorPokemon) continue;
                    String worldName = player.world.getWorldInfo().getWorldName();
                    if (ConfigGetters.worldBlacklist.contains(worldName)) continue;
                    String biomeID = player.world.getBiome(player.getPosition()).getRegistryName().toString();
                    int ticks = (int) (player.world.getWorldTime() % 24000L);
                    ArrayList<WorldTime> currentTimes = WorldTime.getCurrent(ticks);
                    PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(player.getUniqueID()).get();
                    EntityPixelmon firstPartyPokemon = null;
                    for (int i = 0; i < 6; i++) {

                        EntityPixelmon p = party.getPokemon(party.getIDFromPosition(i), player.world);
                        if (p != null) {

                            firstPartyPokemon = p;
                            break;

                        }

                    }
                    String weather;
                    if (player.world.isRaining()) {

                        weather = "rain";

                    } else if (player.world.isThundering()) {

                        weather = "storm";

                    } else {

                        weather = "clear";

                    }
                    String location;
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
                    FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                        // Check if biome list contains biome ID
                        if (BiomeList.biomesToPokemon.containsKey(biomeID)) {

                            // Get all SpawnInfo objects for each Pokemon name in this biome
                            List<PokemonSpawnInfo> possibleSpawns = new ArrayList<>();
                            for (String pokemonNames : BiomeList.biomesToPokemon.get(biomeID)) {

                                for (Map.Entry<String, List<PokemonSpawnInfo>> entry : PokemonSpawnInfo.infoMap.entrySet()) {

                                    if (entry.getKey().equalsIgnoreCase(pokemonNames)) {

                                        List<PokemonSpawnInfo> infos = entry.getValue();
                                        for (PokemonSpawnInfo info : infos) {

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

                            if (possibleSpawns.size() == 0) return;

                            Map<Double, UUID> spawnChanceMap = new HashMap<>();
                            Map<UUID, PokemonSpawnInfo> pokemonSpawnInfoMap = new HashMap<>();
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
                            for (PokemonSpawnInfo info : possibleSpawns) {

                                UUID randUUID = UUID.randomUUID();
                                double spawnChance = info.getSpawnChance() * spawnChanceModifier;
                                spawnChanceMap.put(spawnChance, randUUID);
                                pokemonSpawnInfoMap.put(randUUID, info);
                                spawnChances.add(spawnIndex, spawnChance);
                                spawnIndex++;

                            }
                            Collections.sort(spawnChances);
                            int spawnAmount = possibleSpawns.size();
                            PokemonSpawnInfo selectedSpawn = null;
                            List<PokemonSpawnInfo> names = new ArrayList<>();
                            for (int i = 0; i < spawnAmount; i++) {

                                double spawnChance = spawnChances.get(i);
                                UUID uuid = spawnChanceMap.get(spawnChance);
                                PokemonSpawnInfo info = pokemonSpawnInfoMap.get(uuid);
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

                                selectedSpawn = FlashFire.tryFlashFireOnPokemon(selectedSpawn, possibleSpawns, player);

                            } else if (Harvest.applies(finalFirstPartyPokemon)) {

                                selectedSpawn = Harvest.tryHarvestOnPokemon(selectedSpawn, possibleSpawns, player);

                            } else if (LightningRod.applies(finalFirstPartyPokemon) || Static.applies(finalFirstPartyPokemon)) {

                                selectedSpawn = LightningRod.tryLightningRodOnPokemon(selectedSpawn, possibleSpawns, player);

                            } else if (MagnetPull.applies(finalFirstPartyPokemon)) {

                                selectedSpawn = MagnetPull.tryMagnetPullOnPokemon(selectedSpawn, possibleSpawns, player);

                            } else if (StormDrain.applies(finalFirstPartyPokemon)) {

                                selectedSpawn = StormDrain.tryStormDrainOnPokemon(selectedSpawn, possibleSpawns, player);

                            }
                            if (selectedSpawn == null) return;
                            String[] levelRange = selectedSpawn.getLevelRange().split("-");
                            int max = Integer.parseInt(levelRange[0]);
                            int min = Integer.parseInt(levelRange[1]);
                            String pokemonName = selectedSpawn.getName();
                            EntityPixelmon pokemon;
                            int maxGroupSize = Integer.parseInt(selectedSpawn.getGroupSize().split("-")[0]);
                            int minGroupSize = Integer.parseInt(selectedSpawn.getGroupSize().split("-")[1]);
                            int groupSize = ConfigGetters.enableGroupSize ? RandomHelper.getRandomNumberBetween(minGroupSize, maxGroupSize) : 1;
                            pokemonName = pokemonName.replace(".conf", "");
                            boolean checkBoss = true;
                            boolean pokeModified = false;
                            for (int i = 0; i < groupSize; i++) {

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

                                int xzRadius = ConfigGetters.xzRadius;
                                int yRadius = ConfigGetters.yRadius;
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
                                boolean gmax = false;
                                if (ConfigGetters.gmaxChance > 0.0) {

                                    if (RandomHelper.getRandomChance(ConfigGetters.gmaxChance)) {

                                        gmax = true;

                                    }

                                }
                                if (gmax && !pokemon.getSpecies().hasGmaxForm()) gmax = false;
                                boolean finalGmax = gmax;
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
                                Block block = player.world.getBlockState(safeSpawn).getBlock();
                                if (block != Blocks.AIR && block != Blocks.TALLGRASS && block != Blocks.GRASS) {

                                    return;

                                }
                                int x = safeSpawn.getX();
                                int y = safeSpawn.getY();
                                int z = safeSpawn.getZ();
                                if (CuteCharm.applies(finalFirstPartyPokemon)) {

                                    CuteCharm.tryApplyCuteCharmEffect(pokemon, finalFirstPartyPokemon);

                                }
                                if (Synchronize.applies(finalFirstPartyPokemon)) {

                                    Synchronize.applySynchronize(pokemon, finalFirstPartyPokemon);

                                }
                                pokemon.setLocationAndAngles(x + BetterPixelmonSpawner.random.nextDouble(), y, z + BetterPixelmonSpawner.random.nextDouble(),0, 0);
                                int level = 3;
                                if (ConfigGetters.scalePokemonLevelsByDistance) {

                                    if ((int)((double)level + Math.floor(Math.sqrt(player.world.getSpawnPoint().distanceSq(x, y, z)) / (double)ConfigGetters.blocksBeforePokemonIncrease + Math.random() * 3.0)) > ConfigGetters.maxPokemonScaleLevel) {

                                        level = ConfigGetters.maxPokemonScaleLevel;

                                    } else {

                                        int distance = (int) Math.floor(Math.sqrt(player.world.getSpawnPoint().distanceSq(x, y, z)));
                                        if (distance > ConfigGetters.blocksBeforePokemonIncrease) {

                                            int mod = (distance / ConfigGetters.blocksBeforePokemonIncrease) * ConfigGetters.pokemonLevelModifier;
                                            level = mod + level;

                                        }

                                    }

                                } else {

                                    level = RandomHelper.getRandomNumberBetween(min, max);

                                }
                                if (Hustle.applies(finalFirstPartyPokemon) || Pressure.applies(finalFirstPartyPokemon) || VitalSpirit.applies(finalFirstPartyPokemon)) {

                                    level = Hustle.tryApplyHustle(level, selectedSpawn);

                                }
                                pokemon.getLvl().setLevel(level);
                                if (Intimidate.applies(finalFirstPartyPokemon) || KeenEye.applies(finalFirstPartyPokemon)) {

                                    pokemon = Intimidate.tryIntimidate(pokemon, finalFirstPartyPokemon);
                                    if (pokemon == null) return;

                                }
                                pokemon = PokemonUtils.validatePokemon(pokemon, level);
                                pokemon.setLocationAndAngles(safeSpawn.getX() + BetterPixelmonSpawner.random.nextDouble(), safeSpawn.getY(), safeSpawn.getZ() + BetterPixelmonSpawner.random.nextDouble(),0, 0);
                                pokemon.updateStats();
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
                                if (ConfigGetters.aggressiveChance > 0) {

                                    if (RandomHelper.getRandomChance(ConfigGetters.aggressiveChance)) {

                                        pokemon.setAggression(EnumAggression.aggressive);

                                    }

                                }
                                if (Loader.isModLoaded("betterbosses")) {

                                    if (com.lypaka.betterbosses.Config.ConfigGetters.disableDefaultBosses) {

                                        checkBoss = false;

                                    }

                                }
                                float shinyChance = PixelmonConfig.shinyRate;
                                if (PixelmonConfig.enableCatchCombos) {

                                    if (PixelmonConfig.enableCatchComboShinyLock && PixelmonMethods.isCatchComboSpecies(player, pokemon.getSpecies())) {

                                        shinyChance = PixelmonMethods.getCatchComboChance(player);

                                    } else if (!PixelmonConfig.enableCatchComboShinyLock) {

                                        shinyChance = PixelmonMethods.getCatchComboChance(player);

                                    }

                                } else if (PixelmonMethods.isWearingShinyCharm(player)) {

                                    shinyChance = PixelmonConfig.shinyCharmRate;

                                }
                                boolean shiny = RandomHelper.getRandomNumberBetween(1, shinyChance) == 1;
                                if (shiny) {

                                    pokemon.setShiny(true);

                                }
                                if (!pokeModified) {

                                    if (HolidayHandler.activeHolidays != null && !HolidayHandler.activeHolidays.isEmpty()) {

                                        Map<String, List<String>> particleMap = new HashMap<>();
                                        Map<String, List<String>> textureMap = new HashMap<>();
                                        for (Holiday h : HolidayHandler.activeHolidays) {

                                            List<String> particles = new ArrayList<>();
                                            List<String> textures = new ArrayList<>();
                                            if (h.getPokemon().contains(pokemon.getPokemonName()) || h.getPokemon().contains("shiny") && pokemon.isShiny()) {

                                                if (particleMap.containsKey(h.getName())) {

                                                    particles = particleMap.get(h.getName());

                                                }

                                                if (textureMap.containsKey(h.getName())) {

                                                    textures = textureMap.get(h.getName());

                                                }

                                                particles.addAll(h.getParticles());
                                                particleMap.put(h.getName(), particles);
                                                textures.addAll(h.getSpecialTextures());
                                                textureMap.put(h.getName(), textures);

                                            }

                                        }

                                        String holiday;
                                        List<String> holidays = new ArrayList<>();
                                        for (Map.Entry<String, List<String>> entry : particleMap.entrySet()) {

                                            holidays.add(entry.getKey());

                                        }

                                        if (holidays.size() > 0) {

                                            // I know this is probably dumb, but every other time I've tried getting a random index from a list with a size of 1, it throws IndexOutOfBounds so this prevents any chance of that ever happening
                                            if (holidays.size() == 1) {

                                                holiday = holidays.get(0);

                                            } else {

                                                holiday = holidays.get(BetterPixelmonSpawner.random.nextInt(holidays.size()));

                                            }

                                            boolean doMessage = false;
                                            if (particleMap.containsKey(holiday)) {

                                                List<String> possibleParticles = particleMap.get(holiday);
                                                if (possibleParticles.size() > 0) {

                                                    if (RandomHelper.getRandomChance(ConfigGetters.particleChance)) {

                                                        String randomParticle;
                                                        if (possibleParticles.size() == 1) {

                                                            randomParticle = possibleParticles.get(0);

                                                        } else {

                                                            randomParticle = possibleParticles.get(BetterPixelmonSpawner.random.nextInt(possibleParticles.size()));

                                                        }
                                                        if (randomParticle.contains(":")) {

                                                            String[] split = randomParticle.split(":");
                                                            String species = split[0];
                                                            randomParticle = split[1];
                                                            if (pokemon.getPokemonName().equalsIgnoreCase(species)) {

                                                                pokemon.setParticleId(randomParticle);
                                                                doMessage = true;

                                                            }

                                                        } else {

                                                            pokemon.setParticleId(randomParticle);
                                                            doMessage = true;

                                                        }

                                                    }

                                                }

                                            }
                                            if (textureMap.containsKey(holiday)) {

                                                List<String> possibleTextures = textureMap.get(holiday);
                                                if (possibleTextures.size() > 0) {

                                                    if (RandomHelper.getRandomChance(ConfigGetters.textureChance)) {

                                                        String randomTexture;
                                                        if (possibleTextures.size() == 1) {

                                                            randomTexture = possibleTextures.get(0);

                                                        } else {

                                                            randomTexture = possibleTextures.get(BetterPixelmonSpawner.random.nextInt(possibleTextures.size()));

                                                        }

                                                        pokemon.setCustomSpecialTexture(randomTexture);
                                                        doMessage = true;

                                                    }

                                                }

                                            }
                                            HolidaySpawnEvent holidaySpawnEvent = new HolidaySpawnEvent(holiday, pokemon, player);
                                            MinecraftForge.EVENT_BUS.post(holidaySpawnEvent);
                                            if (!holidaySpawnEvent.isCanceled()) {

                                                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                                                    holidaySpawnEvent.getPlayer().world.spawnEntity(holidaySpawnEvent.getPokemon());
                                                    PokemonCounter.increment(holidaySpawnEvent.getPokemon(), player.getUniqueID());
                                                    PokemonCounter.addPokemon(holidaySpawnEvent.getPokemon(), player.getUniqueID());

                                                });
                                                if (doMessage && !ConfigGetters.holidaySpawnMessage.equalsIgnoreCase("")) {

                                                    pokemon.world.getMinecraftServer().getPlayerList().sendMessage(FancyText.getFancyText(ConfigGetters.holidaySpawnMessage
                                                            .replace("%holiday%", holiday)
                                                            .replace("%pokemon%", pokemon.getPokemonName())
                                                    ));

                                                }
                                                pokeModified = true;

                                            }

                                        }

                                    }

                                }
                                if (!pokeModified) {

                                    if (checkBoss) {

                                        if (BossPokemonUtils.isPossibleBoss(pokemon.getPokemonName())) {

                                            if (BossPokemonUtils.spawnBoss()) {

                                                if (RandomHelper.getRandomChance(ConfigGetters.bossSpawnChance)) {

                                                    BossSpawnEvent bossSpawnEvent = new BossSpawnEvent(pokemon, player, selectedSpawn);
                                                    MinecraftForge.EVENT_BUS.post(bossSpawnEvent);
                                                    if (!bossSpawnEvent.isCanceled()) {

                                                        pokemon.setBoss(BossPokemonUtils.getBossMode());
                                                        pokeModified = true;
                                                        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                                                            player.world.spawnEntity(bossSpawnEvent.getPokemon());
                                                            PokemonCounter.increment(bossSpawnEvent.getPokemon(), player.getUniqueID());
                                                            // Sets this tag for the PokeClear to be able to know what a Boss is, in the event of a "normal" Boss
                                                            bossSpawnEvent.getPokemon().addTag("PixelmonDefaultBoss");
                                                            PokemonCounter.addPokemon(bossSpawnEvent.getPokemon(), player.getUniqueID());

                                                        });
                                                        continue;

                                                    }

                                                }


                                            }

                                        }

                                    }

                                }
                                if (!pokeModified) {

                                    if (TotemPokemonUtils.spawnTotem()) {

                                        if (RandomHelper.getRandomChance(ConfigGetters.totemSpawnChance)) {

                                            TotemSpawnEvent totemSpawnEvent = new TotemSpawnEvent(pokemon, player, selectedSpawn);
                                            MinecraftForge.EVENT_BUS.post(totemSpawnEvent);
                                            if (!totemSpawnEvent.isCanceled()) {

                                                pokemon.setTotem(true);
                                                pokeModified = true;
                                                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                                                    player.world.spawnEntity(totemSpawnEvent.getPokemon());
                                                    PokemonCounter.increment(totemSpawnEvent.getPokemon(), player.getUniqueID());
                                                    PokemonCounter.addPokemon(totemSpawnEvent.getPokemon(), player.getUniqueID());

                                                });
                                                continue;

                                            }

                                        }

                                    }

                                }
                                if (!pokeModified) {

                                    if (AlphaPokemonUtils.spawnAlpha()) {

                                        if (AlphaPokemonUtils.isDefaultAlpha(pokemon.getSpecies())) {

                                            pokemon.setAlpha(true, true);
                                            pokeModified = true;
                                            AlphaSpawnEvent alphaSpawnEvent = new AlphaSpawnEvent(pokemon, player, selectedSpawn);
                                            MinecraftForge.EVENT_BUS.post(alphaSpawnEvent);
                                            if (!alphaSpawnEvent.isCanceled()) {

                                                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                                                    player.world.spawnEntity(alphaSpawnEvent.getPokemon());
                                                    PokemonCounter.increment(alphaSpawnEvent.getPokemon(), player.getUniqueID());
                                                    PokemonCounter.addPokemon(alphaSpawnEvent.getPokemon(), player.getUniqueID());

                                                });
                                                continue;

                                            }

                                        }

                                    }

                                }
                                if (shiny) {

                                    ShinySpawnEvent shinySpawnEvent = new ShinySpawnEvent(pokemon, player, selectedSpawn);
                                    MinecraftForge.EVENT_BUS.post(shinySpawnEvent);
                                    if (!shinySpawnEvent.isCanceled()) {

                                        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                                            shinySpawnEvent.getPokemon().setGmaxFactor(finalGmax);
                                            shinySpawnEvent.getPokemon().setShiny(true);
                                            player.world.spawnEntity(shinySpawnEvent.getPokemon());
                                            PokemonCounter.increment(shinySpawnEvent.getPokemon(), player.getUniqueID());
                                            PokemonCounter.addPokemon(shinySpawnEvent.getPokemon(), player.getUniqueID());

                                        });

                                    }

                                } else {

                                    PokemonSpawnEvent pokemonSpawnEvent = new PokemonSpawnEvent(pokemon, player, selectedSpawn, groupSize);
                                    MinecraftForge.EVENT_BUS.post(pokemonSpawnEvent);
                                    if (!pokemonSpawnEvent.isCanceled()) {

                                        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                                            pokemonSpawnEvent.getPokemon().setGmaxFactor(finalGmax);
                                            player.world.spawnEntity(pokemonSpawnEvent.getPokemon());
                                            PokemonCounter.increment(pokemonSpawnEvent.getPokemon(), player.getUniqueID());
                                            PokemonCounter.addPokemon(pokemonSpawnEvent.getPokemon(), player.getUniqueID());

                                        });

                                    }

                                }

                            }

                        }

                    });

                }

            }

        }, 0, interval);

    }

}
