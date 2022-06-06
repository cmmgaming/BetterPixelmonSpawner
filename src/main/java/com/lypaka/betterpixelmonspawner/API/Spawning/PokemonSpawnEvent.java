package com.lypaka.betterpixelmonspawner.API.Spawning;

import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a regular Pokemon (not Boss, Totem, Alpha, shiny, etc) is set to be spawned
 */
@Cancelable
public class PokemonSpawnEvent extends Event {

    private EntityPixelmon pokemon;
    private final EntityPlayerMP player;
    private PokemonSpawnInfo info;
    private int groupSize;

    public PokemonSpawnEvent (EntityPixelmon pokemon, EntityPlayerMP player, PokemonSpawnInfo info, int groupSize) {

        this.pokemon = pokemon;
        this.player = player;
        this.info = info;
        this.groupSize = groupSize;

    }

    public EntityPixelmon getPokemon() {

        return this.pokemon;

    }

    public void setPokemon (EntityPixelmon pokemon) {

        this.pokemon = pokemon;

    }

    public EntityPlayerMP getPlayer() {

        return this.player;

    }

    public PokemonSpawnInfo getInfo() {

        return this.info;

    }

    public int getGroupSize() {

        return this.groupSize;

    }

}
