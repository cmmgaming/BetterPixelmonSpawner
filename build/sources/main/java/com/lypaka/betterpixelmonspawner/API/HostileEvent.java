package com.lypaka.betterpixelmonspawner.API;

import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.LegendarySpawnInfo;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a Pokemon is set to spawn hostile
 */
@Cancelable
public class HostileEvent extends Event {

    private EntityPixelmon pokemon;
    private EntityPlayerMP player;
    private PokemonSpawnInfo pokemonInfo;
    private LegendarySpawnInfo legendaryInfo;

    public HostileEvent (EntityPixelmon pokemon, EntityPlayerMP player, PokemonSpawnInfo pokemonInfo) {

        this.pokemon = pokemon;
        this.player = player;
        this.pokemonInfo = pokemonInfo;
        this.legendaryInfo = null;

    }

    public HostileEvent (EntityPixelmon pokemon, EntityPlayerMP player, LegendarySpawnInfo legendaryInfo) {

        this.pokemon = pokemon;
        this.player = player;
        this.pokemonInfo = null;
        this.legendaryInfo = legendaryInfo;

    }

    public EntityPixelmon getPokemon() {

        return this.pokemon;

    }

    public EntityPlayerMP getPlayer() {

        return this.player;

    }

    public PokemonSpawnInfo getPokemonInfo() {

        return this.pokemonInfo;

    }

    public LegendarySpawnInfo getLegendaryInfo() {

        return this.legendaryInfo;

    }

}
