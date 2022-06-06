package com.lypaka.betterpixelmonspawner.API;

import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumMark;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a Pokemon gets a Mark assigned to it
 */
@Cancelable
public class MarkEvent extends Event {

    private final EntityPixelmon pokemon;
    private final EntityPlayerMP player;
    private final PokemonSpawnInfo info;
    private EnumMark mark;

    public MarkEvent (EntityPixelmon pokemon, EntityPlayerMP player, PokemonSpawnInfo info, EnumMark mark) {

        this.pokemon = pokemon;
        this.player = player;
        this.info = info;
        this.mark = mark;

    }

    public EntityPixelmon getPokemon() {

        return this.pokemon;

    }

    public EntityPlayerMP getPlayer() {

        return this.player;

    }

    public PokemonSpawnInfo getInfo() {

        return this.info;

    }

    public EnumMark getMark() {

        return this.mark;

    }

    public void setMark (EnumMark mark) {

        this.mark = mark;

    }

}
