package com.lypaka.betterpixelmonspawner.Utils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpawnerUtils {

    public static void add (EntityPlayerMP player, String module) {

        if (module.equalsIgnoreCase("all")) {

            List<String> pokemonList = ConfigGetters.pokemonOptOut;
            List<String> npcList = ConfigGetters.npcOptOut;
            List<String> legendaryList = ConfigGetters.legendaryOptOut;
            List<String> miscList = ConfigGetters.miscOptOut;
            pokemonList.add(player.getUniqueID().toString());
            npcList.add(player.getUniqueID().toString());
            legendaryList.add(player.getUniqueID().toString());
            miscList.add(player.getUniqueID().toString());
            player.sendMessage(FancyText.getFancyText("&aSuccessfully added you to every exempt list!"));

        } else {

            List<String> list;
            switch (module) {

                case "pokemon":
                    list = ConfigGetters.pokemonOptOut;
                    module = "Pokemon";
                    break;

                case "npc":
                    list = ConfigGetters.npcOptOut;
                    module = "NPC";
                    break;

                case "legendary":
                    list = ConfigGetters.legendaryOptOut;
                    module = "Legendary";
                    break;

                default:
                    list = ConfigGetters.miscOptOut;
                    module = "Misc";
                    break;

            }

            if (!list.contains(player.getUniqueID().toString())) {

                list.add(player.getUniqueID().toString());
                player.sendMessage(FancyText.getFancyText("&aSuccessfully added you to the " + module + " exempt list!"));
                player.sendMessage(FancyText.getFancyText("&eTo re-enable spawns for the " + module + " spawner, run &c\"/bpspawner opt in " + module.toLowerCase() + "\"&e."));

            } else {

                player.sendMessage(FancyText.getFancyText("&cYou are already on the exempt list for the " + module + " spawner!"));
                player.sendMessage(FancyText.getFancyText("&eDid you mean to re-enable the spawns? Run &c\"/bpspawner opt in " + module.toLowerCase() + "\"&e."));

            }

        }

    }

    public static void remove (EntityPlayerMP player, String module) {

        if (module.equalsIgnoreCase("all")) {

            List<String> pokemonList = ConfigGetters.pokemonOptOut;
            List<String> npcList = ConfigGetters.npcOptOut;
            List<String> legendaryList = ConfigGetters.legendaryOptOut;
            List<String> miscList = ConfigGetters.miscOptOut;
            pokemonList.removeIf(entry -> entry.equalsIgnoreCase(player.getUniqueID().toString()));
            npcList.removeIf(entry -> entry.equalsIgnoreCase(player.getUniqueID().toString()));
            legendaryList.removeIf(entry -> entry.equalsIgnoreCase(player.getUniqueID().toString()));
            miscList.removeIf(entry -> entry.equalsIgnoreCase(player.getUniqueID().toString()));
            player.sendMessage(FancyText.getFancyText("&aSuccessfully removed you from every exempt list!"));

        }

        List<String> list;
        switch (module) {

            case "pokemon":
                list = ConfigGetters.pokemonOptOut;
                module = "Pokemon";
                break;

            case "npc":
                list = ConfigGetters.npcOptOut;
                module = "NPC";
                break;

            case "legendary":
                list = ConfigGetters.legendaryOptOut;
                module = "Legendary";
                break;

            default:
                list = ConfigGetters.miscOptOut;
                module = "Misc";
                break;

        }

        if (!list.contains(player.getUniqueID().toString())) {

            player.sendMessage(FancyText.getFancyText("&cYou are not already on the exempt list for the " + module + " spawner!"));
            player.sendMessage(FancyText.getFancyText("&eDid you mean to re-enable the spawns? Run &c\"/bpspawner opt in " + module.toLowerCase() + "\"&e."));

        } else {

            list.removeIf(entry -> player.getUniqueID().toString().equalsIgnoreCase(entry.toString()));
            player.sendMessage(FancyText.getFancyText("&aSuccessfully removed you from the exempt list for the " + module + " spawner!"));
            player.sendMessage(FancyText.getFancyText("&eDid you mean to disable the spawns? Run &c\"/bpspawner opt out " + module.toLowerCase() + "\"&e."));

        }

    }

}
