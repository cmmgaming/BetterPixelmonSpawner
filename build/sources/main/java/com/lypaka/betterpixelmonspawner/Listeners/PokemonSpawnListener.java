package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.API.MarkEvent;
import com.lypaka.betterpixelmonspawner.API.Spawning.PokemonSpawnEvent;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumMark;
import com.pixelmongenerations.core.network.EnumUpdateType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class PokemonSpawnListener {

    @SubscribeEvent
    public void onPokemonSpawn (PokemonSpawnEvent event) throws ObjectMappingException {

        EntityPlayerMP player = event.getPlayer();
        EntityPixelmon pokemon = event.getPokemon();
        PokemonSpawnInfo info = event.getInfo();
        double ivPercent = pokemon.stats.IVs.getPercent();
        if (ivPercent >= 75) {

            if (!ConfigGetters.highIVSoundID.equalsIgnoreCase("")) {

                // I hate this
                FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(FMLCommonHandler.instance().getMinecraftServerInstance(), "playsound " + ConfigGetters.highIVSoundID
                        + " master " + player.getName()
                        + " " + player.getPosition().getX()
                        + " " + player.getPosition().getY()
                        + " " + player.getPosition().getZ()
                );

            }

        }

        if (ConfigGetters.marksEnabled) {

            EnumMark mark = EnumMark.rollMark(player, pokemon);
            MarkEvent markEvent = new MarkEvent(pokemon, player, info, mark);
            MinecraftForge.EVENT_BUS.post(markEvent);
            if (!markEvent.isCanceled()) {

                pokemon.setMark(markEvent.getMark());
                pokemon.update(EnumUpdateType.Mark);

            }

        }

    }

}
