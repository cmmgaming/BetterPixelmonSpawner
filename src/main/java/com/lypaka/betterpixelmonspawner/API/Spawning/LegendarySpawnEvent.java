package com.lypaka.betterpixelmonspawner.API.Spawning;

import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.LegendarySpawnInfo;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a legendary is set to be spawned
 */
@Cancelable
public class LegendarySpawnEvent extends Event {

    private EntityPixelmon pokemon;
    private EntityPlayerMP player;
    private LegendarySpawnInfo info;

    public LegendarySpawnEvent (EntityPixelmon pokemon, EntityPlayerMP player, LegendarySpawnInfo info) {

        this.pokemon = pokemon;
        this.player = player;
        this.info = info;

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

    public LegendarySpawnInfo getInfo() {

        return this.info;

    }

}
