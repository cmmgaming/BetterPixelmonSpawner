package com.lypaka.betterpixelmonspawner.API.Spawning;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when spawning of other misc entities in Pixelmon (Zygarde Cells, Dynamax things, whatever)
 */
@Cancelable
public class MiscSpawnEvent extends Event {

    private final EntityLivingBase entity;
    private final EntityPlayerMP player;

    public MiscSpawnEvent (EntityLivingBase entity, EntityPlayerMP player) {

        this.entity = entity;
        this.player = player;

    }

    public EntityLivingBase getEntity() {

        return this.entity;

    }

    public EntityPlayerMP getPlayer() {

        return this.player;

    }

}
