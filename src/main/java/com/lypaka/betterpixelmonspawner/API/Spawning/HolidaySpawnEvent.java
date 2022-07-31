package com.lypaka.betterpixelmonspawner.API.Spawning;

import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class HolidaySpawnEvent extends Event {

    private final String holiday;
    private EntityPixelmon pokemon;
    private final EntityPlayerMP player;

    public HolidaySpawnEvent (String holiday, EntityPixelmon pokemon, EntityPlayerMP player) {

        this.holiday = holiday;
        this.pokemon = pokemon;
        this.player = player;

    }

    public String getHoliday() {

        return this.holiday;

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

}
