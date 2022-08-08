package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.LegendarySpawnInfo;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.lypaka.betterpixelmonspawner.Utils.FormIndexFromName;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumType;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;

public class LightningRod {

    public static boolean applies (EntityPixelmon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("LightningRod") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Lightning Rod");

    }

    public static LegendarySpawnInfo tryLightningRodOnLegendary (LegendarySpawnInfo originalSpawn, List<LegendarySpawnInfo> possibleSpawns, EntityPlayerMP player) {

        if (!RandomHelper.getRandomChance(50)) return originalSpawn;
        List<LegendarySpawnInfo> electricPokemon = new ArrayList<>();
        for (LegendarySpawnInfo info : possibleSpawns) {

            String pokemonName = info.getName();
            pokemonName = pokemonName.replace(".conf", "");
            EntityPixelmon pokemon;
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

            if (pokemon.baseStats.type1 == EnumType.Electric || pokemon.baseStats.type2 == EnumType.Electric) {

                electricPokemon.add(info);

            }

        }

        if (electricPokemon.size() == 0) {

            return originalSpawn;

        } else {

            Map<Double, UUID> spawnChanceMap = new HashMap<>();
            Map<UUID, LegendarySpawnInfo> pokemonSpawnInfoMap = new HashMap<>();
            List<Double> spawnChances = new ArrayList<>(electricPokemon.size());
            int spawnIndex = 0;
            double spawnChanceModifier = 1.0;
            for (LegendarySpawnInfo info : electricPokemon) {

                UUID randUUID = UUID.randomUUID();
                double spawnChance = info.getSpawnChance() * spawnChanceModifier;
                spawnChanceMap.put(spawnChance, randUUID);
                pokemonSpawnInfoMap.put(randUUID, info);
                spawnChances.add(spawnIndex, spawnChance);
                spawnIndex++;

            }
            Collections.sort(spawnChances);
            int spawnAmount = electricPokemon.size();
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

            return selectedSpawn;

        }

    }

    public static PokemonSpawnInfo tryLightningRodOnPokemon (PokemonSpawnInfo originalSpawn, List<PokemonSpawnInfo> possibleSpawns, EntityPlayerMP player) {

        if (!RandomHelper.getRandomChance(50)) return originalSpawn;
        List<PokemonSpawnInfo> electricPokemon = new ArrayList<>();
        for (PokemonSpawnInfo info : possibleSpawns) {

            String pokemonName = info.getName();
            pokemonName = pokemonName.replace(".conf", "");
            EntityPixelmon pokemon;
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

            if (pokemon.baseStats.type1 == EnumType.Electric || pokemon.baseStats.type2 == EnumType.Electric) {

                electricPokemon.add(info);

            }

        }

        if (electricPokemon.size() == 0) {

            return originalSpawn;

        } else {

            Map<Double, UUID> spawnChanceMap = new HashMap<>();
            Map<UUID, PokemonSpawnInfo> pokemonSpawnInfoMap = new HashMap<>();
            List<Double> spawnChances = new ArrayList<>(electricPokemon.size());
            int spawnIndex = 0;
            double spawnChanceModifier = 1.0;
            for (PokemonSpawnInfo info : electricPokemon) {

                UUID randUUID = UUID.randomUUID();
                double spawnChance = info.getSpawnChance() * spawnChanceModifier;
                spawnChanceMap.put(spawnChance, randUUID);
                pokemonSpawnInfoMap.put(randUUID, info);
                spawnChances.add(spawnIndex, spawnChance);
                spawnIndex++;

            }
            Collections.sort(spawnChances);
            int spawnAmount = electricPokemon.size();
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

            return selectedSpawn;

        }

    }

}
