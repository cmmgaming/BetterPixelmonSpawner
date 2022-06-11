package com.lypaka.betterpixelmonspawner.Config;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 * Loads and stores all the configuration settings.
 * It loads from file on server start up. or when a player reloads the plugin.
 *
 * @uathor landonjw
 * @since 9/25/2019 - Version 1.0.0
 */
public class ConfigManager {
    private static String[] FILE_NAMES = new String[]{"generalSettings.conf", "holidays.conf", "pokemonSpawner.conf", "legendarySpawner.conf", "npcSpawner.conf", "miscSpawner.conf", "storage.conf", "areas.conf"};
    private static Path dir;
    private static Path[] config = new Path[FILE_NAMES.length];
    private static ArrayList<ConfigurationLoader<CommentedConfigurationNode>> configLoad = new ArrayList<ConfigurationLoader<CommentedConfigurationNode>>(FILE_NAMES.length);
    private static CommentedConfigurationNode[] configNode = new CommentedConfigurationNode[FILE_NAMES.length];
    private static HoconConfigurationLoader configurationLoader;

    public static void setup(Path folder) {
        dir = checkDir(folder);

        for (int i = 0; i < FILE_NAMES.length; i++) {
            config[i] = dir.resolve(FILE_NAMES[i]);
        }

        load();
    }

    public static Path checkDir(Path dir) {
        try {
            return Files.exists(dir) ? dir : Files.createDirectories(dir);
        } catch (IOException e) {
            throw new RuntimeException("Error creating dir! " + dir, e);
        }
    }

    public static void load() {
        try {
            for (int i = 0; i < FILE_NAMES.length; i++) {
                config[i] = dir.resolve(FILE_NAMES[i]);

                if (!config[i].toFile().exists()) {
                    try {
                        Files.copy(BetterPixelmonSpawner.class.getClassLoader().getResourceAsStream("assets/betterpixelmonspawner/" + FILE_NAMES[i]), config[i], StandardCopyOption.REPLACE_EXISTING);
                    } catch (DirectoryNotEmptyException er) {
                        // do nothing here
                    }
                }

                configurationLoader = HoconConfigurationLoader.builder()
                        .setPath(config[i])
                        .build();

                configLoad.add(i, configurationLoader);
                configNode[i] = configurationLoader.load();
            }
        } catch (IOException e) {
            BetterPixelmonSpawner.logger.error("BetterPixelmonSpawner configuration could not load.");
            e.printStackTrace();
        }
    }

    public static void save() {
        for (int i = 0; i < FILE_NAMES.length; i++) {
            try {
                configLoad.get(i).save(configNode[i]);
            } catch (IOException e) {
                BetterPixelmonSpawner.logger.error("BetterPixelmonSpawner could not save configuration.");
                e.printStackTrace();
            }
        }
    }

    public static CommentedConfigurationNode getConfigNode(int index, Object... node) {
        return configNode[index].getNode(node);
    }
}