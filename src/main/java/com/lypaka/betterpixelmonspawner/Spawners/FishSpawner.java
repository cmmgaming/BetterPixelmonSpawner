package com.lypaka.betterpixelmonspawner.Spawners;

import com.lypaka.betterpixelmonspawner.API.Spawning.FishSpawnEvent;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.FishingSpawnInfo;
import com.lypaka.betterpixelmonspawner.Utils.FormIndexFromName;
import com.pixelmongenerations.api.events.FishingEvent;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.api.spawning.conditions.WorldTime;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.common.entity.projectiles.EntityHook;
import com.pixelmongenerations.core.config.BetterSpawnerConfig;
import com.pixelmongenerations.core.config.PixelmonConfig;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class FishSpawner {

    @SubscribeEvent
    public void onFishCatch (FishingEvent.FishingReelEvent event) {

        if (event.isPokemon()) {

            EntityPlayerMP player = event.getPlayer();
            EntityHook hook = event.getFishHook();
            String biome = hook.world.getBiome(hook.getPosition()).getRegistryName().toString();
            Entity ent = event.getEntity().get();
            String blockID = ent.world.getBlockState(ent.getPosition()).getBlock().getRegistryName().toString();
            String rod = event.getRodType().name();
            rod = "pixelmon:" + rod.replace("Rod", "").toLowerCase() + "_rod";
            if (!BiomeList.biomeFishMap.containsKey(biome)) return;
            if (DeadZone.getAreaFromLocation(player) != null) {

                DeadZone deadZone = DeadZone.getAreaFromLocation(player);
                List<String> entities = deadZone.getEntities();
                if (entities.contains("fishing")) {

                    return;

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
            int ticks = (int) (player.world.getWorldTime() % 24000L);
            ArrayList<WorldTime> currentTimes = WorldTime.getCurrent(ticks);

            // Look for any FishingSpawnInfo objects that have this time, weather, location, biome and rod type

            List<FishingSpawnInfo> possibleSpawns = new ArrayList<>();
            List<String> usedNames = new ArrayList<>();
            for (FishingSpawnInfo info : BiomeList.biomeFishMap.get(biome)) {

                if (currentTimes.contains(WorldTime.valueOf(info.getTime().toUpperCase()))) {

                    if (info.getWeather().equalsIgnoreCase(weather)) {

                        if (info.getRod().equalsIgnoreCase(rod)) {

                            if (info.getLocation().contains(blockID)) {

                                if (RandomHelper.getRandomChance(info.getSpawnChance())) {

                                    if (!usedNames.contains(info.getName())) {

                                        possibleSpawns.add(info);
                                        usedNames.add(info.getName());

                                    }

                                }

                            }

                        }

                    }

                }

            }

            FishingSpawnInfo selectedSpawn = RandomHelper.getRandomElementFromList(possibleSpawns);
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
            int level = 3;
            int x = player.getPosition().getX();
            int y = player.getPosition().getY();
            int z = player.getPosition().getZ();
            if (PixelmonConfig.spawnLevelsByDistance &&
                    (int)((double)level + Math.floor(Math.sqrt(player.world.getSpawnPoint().distanceSq(x, y, z)) / (double)PixelmonConfig.distancePerLevel + Math.random() * 3.0))
                            > PixelmonConfig.maxLevelByDistance) {

                level = PixelmonConfig.maxLevelByDistance;

            } else {

                level = RandomHelper.getRandomNumberBetween(min, max);

            }
            pokemon.getLvl().setLevel(level);

            boolean lava = BetterSpawnerConfig.getLavaBlocks().contains(blockID);
            FishSpawnEvent fishSpawnEvent = new FishSpawnEvent(pokemon, player, selectedSpawn, lava);
            MinecraftForge.EVENT_BUS.post(fishSpawnEvent);
            if (!fishSpawnEvent.isCanceled()) {

                event.setEntity(fishSpawnEvent.getPokemon());

            }

        }

    }

}
