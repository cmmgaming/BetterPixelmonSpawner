package com.lypaka.betterpixelmonspawner.PokemonSpawningInfo;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Config.PokemonConfig;
import com.pixelmongenerations.core.enums.EnumSpecies;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InfoRegistry {

    public static void loadPokemonSpawnData() throws ObjectMappingException {

        for (String name : PokemonConfig.fileNames) {

            // I could just use the name variable here but I'm too afraid it would fuck something else up by changing it now
            // Using this to fix legendaries not getting filtered out of the natural spawner if that setting is enabled
            String pokemonName = name.replace(".conf", "");
            if (PokemonConfig.getConfigNode(name, "Biomes").isVirtual()) continue; // pokemon does not spawn naturally
            if (ConfigGetters.blacklistedSpawnPokemon.contains(name)) continue;
            List<String> biomes = new ArrayList<>();
            Map<String, Map<String, Map<String, Map<String, String>>>> spawnMap = PokemonConfig.getConfigNode(name, "Biomes").getValue(new TypeToken<Map<String, Map<String, Map<String, Map<String, String>>>>>() {});
            for (Map.Entry<String, Map<String, Map<String, Map<String, String>>>> entry : spawnMap.entrySet()) {

                String biome = entry.getKey();
                biomes.add(biome);
                BiomeList.addToBiomeList(name, biome);
                Map<String, Map<String, Map<String, String>>> m2 = entry.getValue();
                for (Map.Entry<String, Map<String, Map<String, String>>> m2Entry : m2.entrySet()) {

                    String time = m2Entry.getKey();
                    Map<String, Map<String, String>> m3 = m2Entry.getValue();
                    for (Map.Entry<String, Map<String, String>> m3Entry : m3.entrySet()) {

                        String weather = m3Entry.getKey();
                        Map<String, String> conditions = m3Entry.getValue();
                        String levelRange = conditions.get("level-range");
                        String spawnChance = conditions.get("spawn-chance");
                        String spawnLocation = conditions.get("spawn-location");
                        String groupSize = conditions.get("group-size");
                        String texture = null;
                        String heldItemID = null;
                        boolean hostile = false;
                        if (conditions.containsKey("texture")) {

                            texture = conditions.get("texture");

                        }
                        if (conditions.containsKey("held-item")) {

                            heldItemID = conditions.get("held-item");

                        }
                        if (conditions.containsKey("hostile")) {

                            hostile = Boolean.parseBoolean(conditions.get("hostile"));

                        }
                        if (ConfigGetters.removeLegendariesFromNormalSpawner) {

                            if (pokemonName.contains("-")) {

                                if (pokemonName.equalsIgnoreCase("ho-oh")) {

                                    pokemonName = "Ho-Oh";

                                } else if (pokemonName.equalsIgnoreCase("Hakamo-o")) {

                                    pokemonName = "Hakamo-o";

                                } else if (pokemonName.equalsIgnoreCase("Jangmo-o")) {

                                    pokemonName = "Jangmo-o";

                                } else if (pokemonName.equalsIgnoreCase("Kommo-o")) {

                                    pokemonName = "Kommo-o";

                                } else if (pokemonName.equalsIgnoreCase("Porygon-Z")) {

                                    pokemonName = "Porygon-Z";

                                } else {

                                    pokemonName = pokemonName.split("-")[0];

                                }

                            }
                            // Generations teammates, if any of you ever see this, I want you to know that this stupidity line here is ya'lls fault with your "make it all lowercase" bullshit.
                            EnumSpecies tempSpecies = EnumSpecies.getFromNameAnyCase(pokemonName);
                            try {

                                if (EnumSpecies.legendaries.contains(tempSpecies.getPokemonName()) || EnumSpecies.ultrabeasts.contains(tempSpecies.getPokemonName()) || ConfigGetters.specialLegendaries.contains(tempSpecies.getPokemonName())) {

                                    LegendarySpawnInfo lInfo = new LegendarySpawnInfo(name, biome, time, weather, levelRange, groupSize, spawnChance, spawnLocation, texture, heldItemID, hostile);
                                    lInfo.register();

                                } else if (tempSpecies.getPokemonName().equalsIgnoreCase("Meltan") && ConfigGetters.removeMeltan) {

                                    LegendarySpawnInfo lInfo = new LegendarySpawnInfo(name, biome, time, weather, levelRange, groupSize, spawnChance, spawnLocation, texture, heldItemID, hostile);
                                    lInfo.register();

                                } else if (tempSpecies.getPokemonName().equalsIgnoreCase("Eternatus") && ConfigGetters.removeEternatus) {

                                    LegendarySpawnInfo lInfo = new LegendarySpawnInfo(name, biome, time, weather, levelRange, groupSize, spawnChance, spawnLocation, texture, heldItemID, hostile);
                                    lInfo.register();

                                } else {

                                    List<PokemonSpawnInfo> psi = new ArrayList<>();
                                    if (BiomeList.biomePokemonMap.containsKey(biome)) {

                                        psi = BiomeList.biomePokemonMap.get(biome);

                                    }
                                    PokemonSpawnInfo pInfo = new PokemonSpawnInfo(name, biome, time, weather, levelRange, groupSize, spawnChance, spawnLocation, texture, heldItemID, hostile);
                                    pInfo.register();
                                    psi.add(pInfo);
                                    BiomeList.biomePokemonMap.put(biome, psi);

                                }

                            } catch (NullPointerException er) {

                                System.out.println("couldn't get species from " + pokemonName);

                            }


                        } else {

                            PokemonSpawnInfo pInfo = new PokemonSpawnInfo(name, biome, time, weather, levelRange, groupSize, spawnChance, spawnLocation, texture, heldItemID, hostile);
                            pInfo.register();

                        }

                    }

                }

            }

        }

    }

}
