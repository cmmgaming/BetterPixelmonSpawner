package com.lypaka.betterpixelmonspawner.Utils.PokemonUtils;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.npcs.registry.DropItemRegistry;
import com.pixelmongenerations.common.entity.pixelmon.drops.BossInfo;
import com.pixelmongenerations.core.enums.EnumBossMode;
import com.pixelmongenerations.core.enums.EnumSpecies;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;

public class BossPokemonUtils {

    private static List<String> possibleBosses = new ArrayList<>();
    public static Map<UUID, Integer> amountMap = new HashMap<>();

    public static void loadBossList() {

        possibleBosses = new ArrayList<>();
        for (EnumSpecies species : EnumSpecies.values()) {

            if (EnumSpecies.legendaries.contains(species.getPokemonName()) || EnumSpecies.ultrabeasts.contains(species.getPokemonName())) {

                possibleBosses.add(species.getPokemonName());

            } else {

                List<String> defaultBosses = new ArrayList<>();
                ArrayList<BossInfo> bosses = DropItemRegistry.getBossPokemon();
                for (BossInfo b : bosses) {

                    if (b.pokemon == species) {

                        if (!defaultBosses.contains(species.getPokemonName())) {

                            defaultBosses.add(species.getPokemonName());

                        }
                        if (!possibleBosses.contains(species.getPokemonName())) {

                            possibleBosses.add(species.getPokemonName());

                        }

                    }

                }

                if (ConfigGetters.bossesCanBeNormal) {

                    if (!defaultBosses.contains(species.getPokemonName())) {

                        if (possibleBosses.contains(species.getPokemonName())) {

                            possibleBosses.add(species.getPokemonName());

                        }

                    }

                }

            }

        }

    }

    public static boolean spawnBoss() {

        if (ConfigGetters.bossSpawnChance == 0) return false;
        return RandomHelper.getRandomChance(ConfigGetters.bossSpawnChance);

    }

    public static boolean isPossibleBoss (String name) {

        return possibleBosses.contains(name);

    }

    public static EnumBossMode getBossMode() {

        EnumBossMode mode = EnumBossMode.Uncommon;
        double sum = ConfigGetters.bossSpawnMap.values().stream().mapToDouble(c -> c).sum();
        double rng = BetterPixelmonSpawner.random.nextDouble() * sum;
        for (Map.Entry<String, Double> entry : ConfigGetters.bossSpawnMap.entrySet()) {

            if (Double.compare(entry.getValue(), rng) <= 0) {

                mode = EnumBossMode.valueOf(entry.getKey());
                break;

            } else {

                rng -= entry.getValue();

            }

        }

        return mode;

    }

    public static boolean canSpawn (EntityPlayerMP player) {

        if (!amountMap.containsKey(player.getUniqueID())) return true;
        int amount = amountMap.get(player.getUniqueID());
        return amount < ConfigGetters.maxBosses;

    }

    public static void addCount (UUID uuid) {

        int amount = 0;
        if (amountMap.containsKey(uuid)) {

            amount = amountMap.get(uuid);

        }

        int updated = amount + 1;
        amountMap.put(uuid, updated);

    }

    public static void removeCount (UUID uuid) {

        if (amountMap.containsKey(uuid)) {

            int amount = amountMap.get(uuid);
            int updated = amount - 1;
            amountMap.put(uuid, updated);

        }

    }

}
