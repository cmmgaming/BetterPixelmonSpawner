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

}
