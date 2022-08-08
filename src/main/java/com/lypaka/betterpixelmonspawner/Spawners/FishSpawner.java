package com.lypaka.betterpixelmonspawner.Spawners;

import com.lypaka.betterpixelmonspawner.API.Spawning.FishSpawnEvent;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.FishingSpawnInfo;
import com.lypaka.betterpixelmonspawner.ExternalAbilities.*;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.lypaka.betterpixelmonspawner.Utils.FormIndexFromName;
import com.lypaka.betterpixelmonspawner.Utils.HeldItemUtils;
import com.pixelmongenerations.api.events.FishingEvent;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.api.spawning.conditions.WorldTime;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.common.entity.projectiles.EntityHook;
import com.pixelmongenerations.core.config.BetterSpawnerConfig;
import com.pixelmongenerations.core.config.PixelmonConfig;
import com.pixelmongenerations.core.storage.PixelmonStorage;
import com.pixelmongenerations.core.storage.PlayerStorage;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class FishSpawner {

    @SubscribeEvent
    public void onCast (FishingEvent.FishingCastEvent event) {

        EntityPlayerMP player = event.getPlayer();
        PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(player.getUniqueID()).get();
        EntityPixelmon firstPartyPokemon = null;
        for (int i = 0; i < 6; i++) {

            EntityPixelmon p = party.getPokemon(party.getIDFromPosition(i), player.world);
            if (p != null) {

                firstPartyPokemon = p;
                break;

            }

        }
        if (firstPartyPokemon != null) {

            if (StickyHold.applies(firstPartyPokemon) || SuctionCups.applies(firstPartyPokemon)) {

                event.setChanceOfNothing(Math.max(0, event.getChanceOfNothing() / 1.75f));

            }

        }

    }

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

            Map<Double, UUID> spawnChanceMap = new HashMap<>();
            Map<UUID, FishingSpawnInfo> pokemonSpawnInfoMap = new HashMap<>();
            List<Double> spawnChances = new ArrayList<>(possibleSpawns.size());
            int spawnIndex = 0;
            double spawnChanceModifier = 1.0;
            if (firstPartyPokemon != null) {

                if (ArenaTrap.applies(firstPartyPokemon) || Illuminate.applies(firstPartyPokemon) || NoGuard.applies(firstPartyPokemon)) {

                    spawnChanceModifier = 2.0;

                } else if (Infiltrator.applies(firstPartyPokemon) || QuickFeet.applies(firstPartyPokemon) || Stench.applies(firstPartyPokemon) || WhiteSmoke.applies(firstPartyPokemon)) {

                    spawnChanceModifier = 0.5;

                }

            }
            for (FishingSpawnInfo info : possibleSpawns) {

                UUID randUUID = UUID.randomUUID();
                double spawnChance = info.getSpawnChance() * spawnChanceModifier;
                spawnChanceMap.put(spawnChance, randUUID);
                pokemonSpawnInfoMap.put(randUUID, info);
                spawnChances.add(spawnIndex, spawnChance);
                spawnIndex++;

            }
            Collections.sort(spawnChances);
            int spawnAmount = possibleSpawns.size();
            FishingSpawnInfo selectedSpawn = null;
            List<FishingSpawnInfo> names = new ArrayList<>();
            for (int i = 0; i < spawnAmount; i++) {

                double spawnChance = spawnChances.get(i);
                UUID uuid = spawnChanceMap.get(spawnChance);
                FishingSpawnInfo info = pokemonSpawnInfoMap.get(uuid);
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
            if (Hustle.applies(firstPartyPokemon) || Pressure.applies(firstPartyPokemon) || VitalSpirit.applies(firstPartyPokemon)) {

                level = Hustle.tryApplyHustle(level, selectedSpawn);

            }
            pokemon.getLvl().setLevel(level);
            if (Intimidate.applies(firstPartyPokemon) || KeenEye.applies(firstPartyPokemon)) {

                pokemon = Intimidate.tryIntimidate(pokemon, firstPartyPokemon);
                if (pokemon == null) return;

            }
            if (selectedSpawn.getHeldItemID() != null) {

                pokemon.heldItem = new ItemStack(Item.getByNameOrId(selectedSpawn.getHeldItemID()));

            } else {

                if (ConfigGetters.heldItemsEnabled) {

                    HeldItemUtils.tryApplyHeldItem(pokemonName, pokemon, firstPartyPokemon);

                }

            }

            if (CuteCharm.applies(firstPartyPokemon)) {

                CuteCharm.tryApplyCuteCharmEffect(pokemon, firstPartyPokemon);

            }
            if (Synchronize.applies(firstPartyPokemon)) {

                Synchronize.applySynchronize(pokemon, firstPartyPokemon);

            }
            boolean lava = BetterSpawnerConfig.getLavaBlocks().contains(blockID);
            FishSpawnEvent fishSpawnEvent = new FishSpawnEvent(pokemon, player, selectedSpawn, lava);
            MinecraftForge.EVENT_BUS.post(fishSpawnEvent);
            if (!fishSpawnEvent.isCanceled()) {

                event.setEntity(fishSpawnEvent.getPokemon());

            }

        }

    }

}
