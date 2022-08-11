package com.lypaka.betterpixelmonspawner.Utils.Counters;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.PokeClear.ClearTask;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.AlphaPokemonUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.BossPokemonUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.TotemPokemonUtils;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;

import java.util.*;

public class PokemonCounter {

    public static Map<UUID, Integer> pokemonCountMap = new HashMap<>();
    public static Map<UUID, List<EntityPixelmon>> pokemonMap = new HashMap<>();

    public static int getCount (UUID uuid) {

        int count = 0;
        if (pokemonCountMap.containsKey(uuid)) {

            count = pokemonCountMap.get(uuid);

        }

        return count;

    }

    public static void increment (EntityPixelmon pokemon, UUID uuid) {

        int count = getCount(uuid);
        int updated = count + 1;
        pokemonCountMap.put(uuid, updated);
        pokemon.addTag("SpawnedPlayerUUID:" + uuid.toString());

    }

    public static void decrement (UUID uuid) {

        int count = getCount(uuid);
        int updated = Math.max(0, count - 1);
        pokemonCountMap.put(uuid, updated);

    }

    public static void addPokemon (EntityPixelmon pokemon, UUID uuid) {

        List<EntityPixelmon> pokeList = new ArrayList<>();
        if (pokemonMap.containsKey(uuid)) {

            pokeList = pokemonMap.get(uuid);

        }
        pokeList.add(pokemon);
        if (pokemon.isAlpha()) {

            AlphaPokemonUtils.addCount(uuid);

        } else if (pokemon.isTotem()) {

            TotemPokemonUtils.addCount(uuid);

        } else if (pokemon.getTags().contains("PixelmonDefaultBoss") || pokemon.getTags().contains("BossPokemon:Tier-")) {

            BossPokemonUtils.addCount(uuid);

        }
        pokemonMap.put(uuid, pokeList);

    }

    public static void removePokemon (EntityPixelmon pokemon, UUID uuid) {

        List<EntityPixelmon> pokeList = new ArrayList<>();
        if (pokemonMap.containsKey(uuid)) {

            pokeList = pokemonMap.get(uuid);

        } else {

            BetterPixelmonSpawner.logger.error("Tried to remove a Pokemon from a player it was not spawned for!");
            BetterPixelmonSpawner.logger.error("Report this message to Lypaka!");
            return;

        }
        pokeList.removeIf(entry -> {

            if (entry.getUniqueID().toString().equalsIgnoreCase(pokemon.getUniqueID().toString())) {

                decrement(uuid);
                return true;

            }

            return false;

        });
        pokemonMap.put(uuid, pokeList);
        if (pokemon.isAlpha()) {

            AlphaPokemonUtils.removeCount(uuid);

        } else if (pokemon.isTotem()) {

            TotemPokemonUtils.removeCount(uuid);

        } else if (pokemon.getTags().contains("PixelmonDefaultBoss") || pokemon.getTags().contains("BossPokemon:Tier-")) {

            BossPokemonUtils.removeCount(uuid);

        }

    }

    public static void checkForDespawnPokemon (UUID uuid) {

        if (!pokemonMap.containsKey(uuid)) return;
        List<EntityPixelmon> pokemonList = pokemonMap.get(uuid);
        pokemonList.removeIf(pokemon -> {

            if (pokemon.isDead) {

                decrement(uuid);
                ClearTask.count++;
                return true;

            } else if (pokemon.battleController == null) {

                if (!ClearTask.isBlacklisted(pokemon)) {

                    for (String tag : pokemon.getTags()) {

                        if (tag.contains("SpawnedPlayerUUID:")) {

                            decrement(uuid);
                            pokemon.setDead();
                            ClearTask.count++;
                            return true;

                        }

                    }

                    if (pokemon.isAlpha()) {

                        AlphaPokemonUtils.removeCount(uuid);
                        pokemon.setDead();
                        ClearTask.count++;

                    } else if (pokemon.isTotem()) {

                        TotemPokemonUtils.removeCount(uuid);
                        pokemon.setDead();
                        ClearTask.count++;

                    } else if (pokemon.getTags().contains("PixelmonDefaultBoss") || pokemon.getTags().contains("BossPokemon:Tier-")) {

                        BossPokemonUtils.removeCount(uuid);
                        pokemon.setDead();
                        ClearTask.count++;

                    }

                }

            }

            return false;

        });
        pokemonMap.put(uuid, pokemonList);

    }

}
