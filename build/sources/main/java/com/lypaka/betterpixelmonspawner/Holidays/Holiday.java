package com.lypaka.betterpixelmonspawner.Holidays;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;

import java.util.ArrayList;
import java.util.List;

public class Holiday {

    private final String name;
    private final int month;
    private final int dayStart;
    private final int dayEnd;
    private final List<String> pokemonNames;
    private final List<String> particleNames;
    private final List<String> specialTextures;
    public static List<Holiday> holidayList = new ArrayList<>();

    public Holiday (String name, int month, int dayStart, int dayEnd, List<String> pokemonNames, List<String> particleNames, List<String> specialTextures) {

        this.name = name;
        this.month = month;
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.pokemonNames = pokemonNames;
        this.particleNames = particleNames;
        this.specialTextures = specialTextures;

    }

    public void register() {

        holidayList.add(this);

    }

    public void activate() {

        HolidayHandler.activeHolidays.add(this);
        BetterPixelmonSpawner.logger.info("Activating holiday: " + this.name);

    }

    public void deactivate() {

        BetterPixelmonSpawner.logger.info("Deactivating holiday: " + this.name);

    }

    // Thanks Pixelmon
    public boolean isWithinDate (int month, int dayMin, int dayMax) {

        final int currentMonth = BetterPixelmonSpawner.currentDay.getMonthValue();
        final int currentDay = BetterPixelmonSpawner.currentDay.getDayOfMonth();
        boolean a = currentMonth == month;
        boolean b = currentDay >= dayMin;
        boolean c = currentDay <= dayMax;
        boolean d = b && c;
        boolean e = a && d;
        return e;

    }

    public String getName() {

        return this.name;

    }

    public int getMonth() {

        return this.month;

    }

    public int getDayStart() {

        return this.dayStart;

    }

    public int getDayEnd() {

        return this.dayEnd;

    }

    public List<String> getPokemon() {

        return this.pokemonNames;

    }

    public List<String> getParticles() {

        return this.particleNames;

    }

    public List<String> getSpecialTextures() {

        return this.specialTextures;

    }

}
