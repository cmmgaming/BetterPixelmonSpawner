package com.lypaka.betterpixelmonspawner.PokemonSpawningInfo;

import java.util.ArrayList;
import java.util.List;

public class PokemonInfo {

    private String name;
    private List<String> biomes;
    public static List<PokemonInfo> pokemonInfo = new ArrayList<>();

    public PokemonInfo (String name, List<String> biomes) {

        this.name = name;
        this.biomes = biomes;

    }

    public void add() {

        pokemonInfo.add(this);

    }

    public String getName() {

        return this.name;

    }

    public List<String> getBiomes() {

        return this.biomes;

    }

}
