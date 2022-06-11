package com.lypaka.betterpixelmonspawner.API.Spawning;

import com.pixelmongenerations.common.entity.npcs.EntityNPC;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a NPC is set to be spawned on a player
 */
@Cancelable
public class NPCSpawnEvent extends Event {

    private EntityNPC npc;
    private final EntityPlayerMP player;

    public NPCSpawnEvent (EntityNPC npc, EntityPlayerMP player) {

        this.npc = npc;
        this.player = player;

    }

    public EntityNPC getNPC() {

        return this.npc;

    }

    public void setNPC (EntityNPC npc) {

        this.npc = npc;

    }

    public EntityPlayerMP getPlayer() {

        return this.player;

    }

}
