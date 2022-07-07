package com.lypaka.betterpixelmonspawner.API.Spawning;

import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.FishingSpawnInfo;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a Pokemon is summoned via fishing
 */
@Cancelable
public class FishSpawnEvent extends Event {

    private EntityPixelmon pokemon;
    private final EntityPlayerMP player;
    private final FishingSpawnInfo info;
    private final boolean isLava;

    public FishSpawnEvent (EntityPixelmon pokemon, EntityPlayerMP player, FishingSpawnInfo info, boolean isLava) {

        this.pokemon = pokemon;
        this.player = player;
        this.info = info;
        this.isLava = isLava;

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

    public FishingSpawnInfo getInfo() {

        return this.info;

    }

    public boolean isLava() {

        return this.isLava;

    }

}
