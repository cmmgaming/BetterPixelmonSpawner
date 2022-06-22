package com.lypaka.betterpixelmonspawner.Spawners;

import com.lypaka.betterpixelmonspawner.API.HostileEvent;
import com.lypaka.betterpixelmonspawner.API.Spawning.LegendarySpawnEvent;
import com.lypaka.betterpixelmonspawner.Areas.Area;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Listeners.JoinListener;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.LegendarySpawnInfo;
import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import com.lypaka.betterpixelmonspawner.Utils.FormIndexFromName;
import com.lypaka.betterpixelmonspawner.Utils.LegendaryUtils;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.api.spawning.conditions.WorldTime;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;

public class LegendarySpawner {

    private static Timer legendaryTimer = null;

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

        long interval = ConfigGetters.legendarySpawnInterval * 1000L;
        legendaryTimer = new Timer();
        legendaryTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                List<EntityPlayerMP> onlinePlayers = new ArrayList<>();
                for (Map.Entry<UUID, EntityPlayerMP> entry : JoinListener.playerMap.entrySet()) {

                    if (!ConfigGetters.legendaryOptOut.contains(entry.getValue().getUniqueID().toString())) {

                        if (!Area.isInArea(entry.getValue())) {

                            onlinePlayers.add(entry.getValue());

                        } else {

                            if (Area.getAreaFromLocation(entry.getValue()) != null) {

                                Area area = Area.getAreaFromLocation(entry.getValue());
                                List<String> entities = area.getEntities();
                                if (!entities.contains("legendaries")) {

                                    onlinePlayers.add(entry.getValue());

                                }

                            } else {

                                onlinePlayers.add(entry.getValue());

                            }

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

                List<LegendarySpawnInfo> possibleSpawns = new ArrayList<>();
                List<String> usedNames = new ArrayList<>();
                String location;
                if (player.isInWater()) {

                    location = "water";

                } else if (player.onGround) {

                    if (player.getPosition().getY() <= 63) {

                        location = "underground";

                    } else {

                        location = "land";

                    }

                }  else {

                    location = "air";

                }
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

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

                                                if (info.getSpawnLocation().equalsIgnoreCase(location)) {

                                                    if (!usedNames.contains(info.getName())) {

                                                        if (RandomHelper.getRandomChance(info.getSpawnChance() * 25)) {

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

                    LegendarySpawnInfo selectedSpawn;
                    if (possibleSpawns.size() == 1) {

                        selectedSpawn = possibleSpawns.get(0);

                    } else {

                        selectedSpawn = possibleSpawns.get(BetterPixelmonSpawner.random.nextInt(possibleSpawns.size()));

                    }

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
                    pokemon.getLvl().setLevel(level);
                    pokemon.updateStats();

                    if (selectedSpawn.getHeldItemID() != null) {

                        pokemon.heldItem = new ItemStack(Item.getByNameOrId(selectedSpawn.getHeldItemID()));

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
                        safeSpawn = new BlockPos(spawnX, player.world.getTopSolidOrLiquidBlock(baseSpawn).getY(), spawnZ);

                    }
                    pokemon.setLocationAndAngles(safeSpawn.getX() + 0.5, safeSpawn.getY(), safeSpawn.getZ() + 0.5,0, 0);
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
                            if (!ConfigGetters.legendarySpawnMessage.equalsIgnoreCase("")) {

                                BetterPixelmonSpawner.server.getPlayerList().sendMessage(FancyText.getFancyText(ConfigGetters.legendarySpawnMessage
                                        .replace("%biome%", getPrettyBiomeName(biomeID))
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

    private static String getPrettyBiomeName (String biomeID) {

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
