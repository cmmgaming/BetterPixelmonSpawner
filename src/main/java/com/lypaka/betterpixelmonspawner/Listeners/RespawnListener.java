package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.Utils.Counters.MiscCounter;
import com.lypaka.betterpixelmonspawner.Utils.Counters.NPCCounter;
import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.UUID;

public class RespawnListener {

    @SubscribeEvent
    public void onRespawn (PlayerEvent.PlayerRespawnEvent event) {

        EntityPlayerMP player = (EntityPlayerMP) event.player;
        putTheBitchBackIfMissing(player);

    }

    private static void putTheBitchBackIfMissing (EntityPlayerMP player) {

        UUID uuid = player.getUniqueID();
        JoinListener.playerMap.entrySet().removeIf(entry -> entry.getKey().toString().equalsIgnoreCase(uuid.toString()));
        JoinListener.playerMap.put(uuid, player);
        if (!MiscCounter.miscCountMap.containsKey(uuid)) {

            MiscCounter.miscCountMap.put(uuid, 0);

        }
        if (!NPCCounter.npcCountMap.containsKey(uuid)) {

            NPCCounter.npcCountMap.put(uuid, 0);

        }
        if (!PokemonCounter.pokemonCountMap.containsKey(uuid)) {

            PokemonCounter.pokemonCountMap.put(uuid, 0);

        }

    }

}
