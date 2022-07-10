package com.lypaka.betterpixelmonspawner.Holidays;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterpixelmonspawner.API.Spawning.PokemonSpawnEvent;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Config.ConfigManager;
import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.time.LocalDate;
import java.util.*;

public class HolidayHandler {

    public static List<Holiday> activeHolidays = new ArrayList<>();
    private static Timer timer = null;

    public static void startHourTracker() {

        if (timer != null) {

            timer.cancel();

        }

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                BetterPixelmonSpawner.currentDay = LocalDate.now();
                BetterPixelmonSpawner.logger.info("Checking for holiday update...");
                if (HolidayHandler.activeHolidays != null) {

                    HolidayHandler.activeHolidays.removeIf(holiday -> {

                        if (!holiday.isWithinDate(holiday.getMonth(), holiday.getDayStart(), holiday.getDayEnd())) {

                            holiday.deactivate();
                            return true;

                        }

                        return false;

                    });

                }
                for (Holiday holiday : Holiday.holidayList) {

                    if (holiday.isWithinDate(holiday.getMonth(), holiday.getDayStart(), holiday.getDayEnd())) {

                        if (!HolidayHandler.activeHolidays.isEmpty()) {

                            for (Holiday h : HolidayHandler.activeHolidays) {

                                if (!h.getName().equalsIgnoreCase(holiday.getName())) {

                                    holiday.activate();

                                }

                            }

                        } else {

                            holiday.activate();

                        }

                    }

                }

            }

        },0, 3600000);

    }

    public static void loadHolidays() throws ObjectMappingException {

        BetterPixelmonSpawner.logger.info("Registering holidays...");
        Map<String, Map<String, Integer>> holidayMap = ConfigManager.getConfigNode(1, "Holidays").getValue(new TypeToken<Map<String, Map<String, Integer>>>() {});
        for (Map.Entry<String, Map<String, Integer>> entry : holidayMap.entrySet()) {

            String name = entry.getKey();
            Map<String, Integer> values = entry.getValue();
            int month = values.get("Month");
            int dayStart = values.get("Day-Start");
            int dayEnd = values.get("Day-End");
            List<String> particles = new ArrayList<>();
            if (!ConfigManager.getConfigNode(1, "Holidays", name, "Special-Textures").isVirtual()) {

                particles = ConfigManager.getConfigNode(1, "Holidays", name, "Particles").getList(TypeToken.of(String.class));

            }
            List<String> pokemon = ConfigManager.getConfigNode(1, "Holidays", name, "Pokemon").getList(TypeToken.of(String.class));
            List<String> textures = new ArrayList<>();
            if (!ConfigManager.getConfigNode(1, "Holidays", name, "Special-Textures").isVirtual()) {

                textures = ConfigManager.getConfigNode(1, "Holidays", name, "Special-Textures").getList(TypeToken.of(String.class));

            }
            Holiday holiday = new Holiday(name, month, dayStart, dayEnd, pokemon, particles, textures);
            holiday.register();

        }

        startHourTracker();

    }

    @SubscribeEvent
    public void onHolidaySpawn (PokemonSpawnEvent event) {

        if (activeHolidays == null) return;
        if (activeHolidays.isEmpty()) return;
        EntityPixelmon pokemon = event.getPokemon();
        Map<String, List<String>> particleMap = new HashMap<>();
        Map<String, List<String>> textureMap = new HashMap<>();
        for (Holiday h : activeHolidays) {

            List<String> particles = new ArrayList<>();
            List<String> textures = new ArrayList<>();
            if (h.getPokemon().contains(pokemon.getPokemonName()) || h.getPokemon().contains("shiny") && pokemon.isShiny()) {

                if (particleMap.containsKey(h.getName())) {

                    particles = particleMap.get(h.getName());

                }

                if (textureMap.containsKey(h.getName())) {

                    textures = textureMap.get(h.getName());

                }

                particles.addAll(h.getParticles());
                particleMap.put(h.getName(), particles);
                textures.addAll(h.getSpecialTextures());
                textureMap.put(h.getName(), textures);

            }

        }

        String holiday;
        List<String> holidays = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : particleMap.entrySet()) {

            holidays.add(entry.getKey());

        }
        if (holidays.size() > 0) {

            // I know this is probably dumb, but every other time I've tried getting a random index from a list with a size of 1, it throws IndexOutOfBounds so this prevents any chance of that ever happening
            if (holidays.size() == 1) {

                holiday = holidays.get(0);

            } else {

                holiday = holidays.get(BetterPixelmonSpawner.random.nextInt(holidays.size()));

            }

            boolean doMessage = false;
            if (particleMap.containsKey(holiday)) {

                List<String> possibleParticles = particleMap.get(holiday);
                if (possibleParticles.size() > 0) {

                    if (RandomHelper.getRandomChance(ConfigGetters.particleChance)) {

                        String randomParticle;
                        if (possibleParticles.size() == 1) {

                            randomParticle = possibleParticles.get(0);

                        } else {

                            randomParticle = possibleParticles.get(BetterPixelmonSpawner.random.nextInt(possibleParticles.size()));

                        }
                        if (randomParticle.contains(":")) {

                            String[] split = randomParticle.split(":");
                            String species = split[0];
                            randomParticle = split[1];
                            if (pokemon.getPokemonName().equalsIgnoreCase(species)) {

                                pokemon.setParticleId(randomParticle);
                                doMessage = true;

                            }

                        } else {

                            pokemon.setParticleId(randomParticle);
                            doMessage = true;

                        }

                    }

                }

            }
            if (textureMap.containsKey(holiday)) {

                List<String> possibleTextures = textureMap.get(holiday);
                if (possibleTextures.size() > 0) {

                    if (RandomHelper.getRandomChance(ConfigGetters.textureChance)) {

                        String randomTexture;
                        if (possibleTextures.size() == 1) {

                            randomTexture = possibleTextures.get(0);

                        } else {

                            randomTexture = possibleTextures.get(BetterPixelmonSpawner.random.nextInt(possibleTextures.size()));

                        }

                        pokemon.setCustomSpecialTexture(randomTexture);
                        doMessage = true;

                    }

                }

            }
            if (doMessage && !ConfigGetters.holidaySpawnMessage.equalsIgnoreCase("")) {

                pokemon.world.getMinecraftServer().getPlayerList().sendMessage(FancyText.getFancyText(ConfigGetters.holidaySpawnMessage
                        .replace("%holiday%", holiday)
                        .replace("%pokemon%", pokemon.getPokemonName())
                ));

            }

        }

    }

}
