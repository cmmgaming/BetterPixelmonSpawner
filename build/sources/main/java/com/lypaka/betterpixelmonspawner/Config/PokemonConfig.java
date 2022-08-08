package com.lypaka.betterpixelmonspawner.Config;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;

/**
 * Loads and stores all the configuration settings.
 * It loads from file on server start up. or when a player reloads the plugin.
 *
 * @author BurstingFire
 * @since 04/11/2022 - Version 1.0.0
 */
public class PokemonConfig {

    private static Path dir;
    private static final Map<String, ConfigurationLoader<CommentedConfigurationNode>> configLoaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final Map<String, CommentedConfigurationNode> configNodes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    public static List<String> fileNames;

    public static void setup (Path folder) {

        dir = checkDir(folder);
        fileNames = new ArrayList<>();
        load();

    }

    public static Path checkDir (Path dir) {

        try {

            return Files.exists(dir) ? dir : Files.createDirectories(dir);

        } catch (IOException e) {

            throw new RuntimeException("Error creating dir! " + dir, e);

        }

    }

    public static void load() {

        try {

            if (dir.toFile().listFiles().length == 0) {

                String location = "assets/betterpixelmonspawner/pokemon";

                // directory is empty, load defaults from assets
                FileSystem fileSystem = FileSystems.newFileSystem(BetterPixelmonSpawner.class.getClassLoader().getResource(location).toURI(), new HashMap<>());
                Files.walk(fileSystem.getPath(location)).map(Path::getFileName).map(Path::toString).filter(fileName -> fileName.endsWith(".conf")).forEach(fileName -> {

                    Path generatedFile = dir.resolve(fileName);
                    fileNames.add(fileName);
                    try {

                        Files.copy(BetterPixelmonSpawner.class.getClassLoader().getResourceAsStream(location + "/" + fileName), dir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

                    } catch (IOException e) {

                        BetterPixelmonSpawner.logger.error("BetterPixelmonSpawner could not copy file! " + fileName);
                        e.printStackTrace();

                    }
                    HoconConfigurationLoader configurationLoader = HoconConfigurationLoader.builder()
                            .setPath(generatedFile)
                            .build();
                    configLoaders.put(fileName, configurationLoader);
                    try {

                        configNodes.put(fileName, configurationLoader.load());

                    } catch (IOException e) {

                        e.printStackTrace();

                    }

                });

                fileSystem.close();

            } else {

                for (File file : dir.toFile().listFiles()) {

                    try {

                        Path generatedFile = dir.resolve(file.getName());
                        fileNames.add(file.getName());
                        HoconConfigurationLoader configurationLoader = HoconConfigurationLoader.builder()
                                .setPath(generatedFile)
                                .build();
                        configLoaders.put(file.getName(), configurationLoader);
                        configNodes.put(file.getName(), configurationLoader.load());

                    } catch (IOException e) {

                        BetterPixelmonSpawner.logger.error("BetterPixelmonSpawner could not load configuration! " + file.getName());
                        e.printStackTrace();

                    }

                }

            }

        } catch (URISyntaxException | IOException e) {

            BetterPixelmonSpawner.logger.error("BetterPixelmonSpawner could not access assets!");
            e.printStackTrace();

        }

    }

    public static void save() {

        for (Map.Entry<String, ConfigurationLoader<CommentedConfigurationNode>> entry : configLoaders.entrySet()) {

            try {

                entry.getValue().save(configNodes.get(entry.getKey()));

            } catch (IOException e) {

                BetterPixelmonSpawner.logger.error("BetterPixelmonSpawner could not save configuration! " + entry.getKey() + ".conf");
                e.printStackTrace();

            }

        }

    }

    public static CommentedConfigurationNode getConfigNode (String name, Object... node) {

        return configNodes.getOrDefault(name, HoconConfigurationLoader.builder().build().createEmptyNode()).getNode(node);

    }

}