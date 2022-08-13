package com.lypaka.betterpixelmonspawner.Config;

import java.util.HashMap;
import java.util.Map;

public class ConfigUpdater {

    public static void updateConfig() {

        boolean needsSaving = false;

        /** Version 1.4.1 */
        if (ConfigManager.getConfigNode(2, "Blocks-Before-Level-Increase").isVirtual()) {

            needsSaving = true;
            ConfigManager.getConfigNode(2, "Blocks-Before-Level-Increase").setComment("If \"Scale-Levels-By-Distance\" = true, how many blocks away from spawn before a Pokemon's level is increased");
            ConfigManager.getConfigNode(2, "Blocks-Before-Level-Increase").setValue(30);

        }
        if (ConfigManager.getConfigNode(2, "Level-Modifier").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Level-Modifier").setComment("If \"Scale-Levels-By-Distance\" = true, by how much a Pokemon's level is increased based on \"Blocks-Before-Level-Increase\"");
            ConfigManager.getConfigNode(2, "Level-Modifier").setValue(1);

        }
        if (ConfigManager.getConfigNode(2, "Max-Scaled-Level").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Max-Scaled-Level").setComment("If \"Scale-Levels-By-Distance\" is enabled, the maximum level a Pokemon can be on");
            ConfigManager.getConfigNode(2, "Max-Scaled-Level").setValue(60);

        }
        if (ConfigManager.getConfigNode(2, "Scale-Levels-By-Distance").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Scale-Levels-By-Distance").setComment("If true, will scale wild Pokemon levels based on distance from world spawn");
            ConfigManager.getConfigNode(2, "Scale-Levels-By-Distance").setValue(false);

        }
        if (ConfigManager.getConfigNode(4, "Blocks-Before-Level-Increase").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(4, "Blocks-Before-Level-Increase").setComment("If \"Scale-Levels-By-Distance\" = true, how many blocks away from spawn before a NPC Trainer's level is increased");
            ConfigManager.getConfigNode(4, "Blocks-Before-Level-Increase").setValue(30);

        }
        if (ConfigManager.getConfigNode(4, "Level-Modifier").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(4, "Level-Modifier").setComment("If \"Scale-Levels-By-Distance\" = true, by how much a NPC Trainer's level is increased based on \"Blocks-Before-Level-Increase\"");
            ConfigManager.getConfigNode(4, "Level-Modifier").setValue(1);

        }
        if (ConfigManager.getConfigNode(4, "Max-Scaled-Level").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(4, "Max-Scaled-Level").setComment("If \"Scale-Levels-By-Distance\" is enabled, the maximum level a NPC Trainer can be on");
            ConfigManager.getConfigNode(4, "Max-Scaled-Level").setValue(60);

        }
        if (ConfigManager.getConfigNode(4, "Scale-Levels-By-Distance").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(4, "Scale-Levels-By-Distance").setComment("If true, will scale wild NPC Trainer levels based on distance from world spawn");
            ConfigManager.getConfigNode(4, "Scale-Levels-By-Distance").setValue(false);

        }

        /** Version 1.5.0 */
        if (!ConfigManager.getConfigNode(2, "Generate-Files").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Generate-Files").setValue(null);

        }

        /** Version 1.5.1 */
        if (ConfigManager.getConfigNode(5, "Broadcasts").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(5, "Broadcasts").setComment("Sets broadcast messages to go off when a misc entity is spawned");
            ConfigManager.getConfigNode(5, "Broadcasts", "pixelmon:zygarde_cell").setValue("&dA Zygarde Cell has spawned in a %biome% biome!");
            ConfigManager.getConfigNode(5, "Broadcasts", "pixelmon:dynamax_energy").setValue("&dA Dynamax Beam has spawned in a %biome% biome!");
            ConfigManager.getConfigNode(5, "Broadcasts", "pixelmon:wishing_star").setValue("&dA Wishing Star has spawned in a %biome% biome!");
            ConfigManager.getConfigNode(5, "Broadcasts", "pixelmon:space_time_distortion").setValue("&dA Space Time Distortion has spawned in a %biome% biome!");
            ConfigManager.getConfigNode(5, "Broadcasts", "pixelmon:mysterious_ring").setValue("&dA Mysterious Ring has spawned in a %biome% biome!");

        }

        /** Version 1.5.5 */
        if (ConfigManager.getConfigNode(3, "Spawn-Interval-Max").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(3, "Spawn-Interval").setValue(null);
            ConfigManager.getConfigNode(3, "Spawn-Interval-Max").setValue(3600);
            ConfigManager.getConfigNode(3, "Spawn-Interval-Min").setValue(1800);

        }

        if (ConfigManager.getConfigNode(6, "Spawner-Filter").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            Map<String, String> uuidMap = new HashMap<>();
            ConfigManager.getConfigNode(6, "Spawner-Filter").setValue(uuidMap);

        }

        /** Version 1.6.0 */
        if (ConfigManager.getConfigNode(2, "Held-Items").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Held-Items").setComment("If true, will enable the Held Item module which tries to put set held items on Pokemon from the heldItems.conf file");
            ConfigManager.getConfigNode(2, "Held-Items").setValue(true);

        }
        if (ConfigManager.getConfigNode(2, "External-Abilities").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "External-Abilities").setComment("If true, will enable the External Abilities module. For more information, see here: https://bulbapedia.bulbagarden.net/wiki/Category:Abilities_that_affect_appearance_of_wild_Pok%C3%A9mon");
            ConfigManager.getConfigNode(2, "External-Abilities").setValue(true);

        }
        if (ConfigManager.getConfigNode(2, "Ignore-Creative").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Ignore-Creative").setComment("If true, will ignore (not spawn on) players in creative mode");
            ConfigManager.getConfigNode(2, "Ignore-Creative").setValue(false);

        }
        if (ConfigManager.getConfigNode(2, "Ignore-Spectator").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Ignore-Spectator").setComment("If true, will ignore (not spawn on) players in spectator mode");
            ConfigManager.getConfigNode(2, "Ignore-Spectator").setValue(false);

        }

        if (ConfigManager.getConfigNode(3, "Ignore-Creative").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(3, "Ignore-Creative").setComment("If true, will ignore (not spawn on) players in creative mode");
            ConfigManager.getConfigNode(3, "Ignore-Creative").setValue(false);

        }
        if (ConfigManager.getConfigNode(3, "Ignore-Spectator").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(3, "Ignore-Spectator").setComment("If true, will ignore (not spawn on) players in spectator mode");
            ConfigManager.getConfigNode(3, "Ignore-Spectator").setValue(false);

        }

        if (ConfigManager.getConfigNode(4, "Ignore-Creative").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(4, "Ignore-Creative").setComment("If true, will ignore (not spawn on) players in creative mode");
            ConfigManager.getConfigNode(4, "Ignore-Creative").setValue(false);

        }
        if (ConfigManager.getConfigNode(4, "Ignore-Spectator").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(4, "Ignore-Spectator").setComment("If true, will ignore (not spawn on) players in spectator mode");
            ConfigManager.getConfigNode(4, "Ignore-Spectator").setValue(false);

        }

        if (ConfigManager.getConfigNode(5, "Ignore-Creative").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(5, "Ignore-Creative").setComment("If true, will ignore (not spawn on) players in creative mode");
            ConfigManager.getConfigNode(5, "Ignore-Creative").setValue(false);

        }
        if (ConfigManager.getConfigNode(5, "Ignore-Spectator").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(5, "Ignore-Spectator").setComment("If true, will ignore (not spawn on) players in spectator mode");
            ConfigManager.getConfigNode(5, "Ignore-Spectator").setValue(false);

        }

        if (ConfigManager.getConfigNode(2, "Enable-Group-Size").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Enable-Group-Size").setComment("If false, will disable the group size system, meaning only 1 Pokemon will spawn from a spawn attempt instead of like 2 or 3, ignoring the group-size setting in the Pokemon's .conf file");
            ConfigManager.getConfigNode(2, "Enable-Group-Size").setValue(true);

        }

        /** Version 1.6.1 */
        if (ConfigManager.getConfigNode(2, "Max-Alphas").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Max-Alphas").setComment("Sets the maximum allowed amount of Alpha Pokemon spawned per player");
            ConfigManager.getConfigNode(2, "Max-Alphas").setValue(3);

        }
        if (ConfigManager.getConfigNode(2, "Max-Bosses").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Max-Bosses").setComment("Sets the maximum allowed amount of Boss Pokemon spawned per player");
            ConfigManager.getConfigNode(2, "Max-Bosses").setValue(2);

        }
        if (ConfigManager.getConfigNode(2, "Max-Totems").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Max-Totems").setComment("Sets the maximum allowed amount of Totem Pokemon spawned per player");
            ConfigManager.getConfigNode(2, "Max-Totems").setValue(3);

        }
        if (ConfigManager.getConfigNode(5, "Spawn-Interval").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(5, "Spawn-Interval").setComment("Sets the interval in which the misc entity spawner runs at, in seconds");
            ConfigManager.getConfigNode(5, "Spawn-Interval").setValue(240);

        }

        /** Version 1.6.3 */
        if (ConfigManager.getConfigNode(2, "Spawn-Level-Randomization").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }

            ConfigManager.getConfigNode(2, "Spawn-Level-Randomization").setComment("If true, will apply a little randomization to the spawn level selection (only used if \"Scale-Levels-By-Distance\" = true)");
            ConfigManager.getConfigNode(2, "Spawn-Level-Randomization").setValue(true);
            ConfigManager.getConfigNode(2, "Spawn-Level-Randomization-Value-Max").setValue(1.15);
            ConfigManager.getConfigNode(2, "Spawn-Level-Randomization-Value-Min").setValue(0.75);

        }

        if (needsSaving) {

            ConfigManager.save();

        }

    }

}
