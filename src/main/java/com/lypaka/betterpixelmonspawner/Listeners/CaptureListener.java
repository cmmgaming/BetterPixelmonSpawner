package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import com.lypaka.betterpixelmonspawner.Utils.LegendaryListing;
import com.pixelmongenerations.api.events.CaptureEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.UUID;

public class CaptureListener {

    @SubscribeEvent
    public void onCatchAttempt (CaptureEvent.StartCaptureEvent event) {

        EntityPixelmon pokemon = event.getPokemon();
        EntityPlayerMP player = event.getPlayer();

        if (EnumSpecies.legendaries.contains(pokemon.getPokemonName()) || EnumSpecies.ultrabeasts.contains(pokemon.getPokemonName()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

            for (String tag : pokemon.getTags()) {

                if (tag.contains("LegendaryGracePeriod:")) {

                    String[] split = tag.split(":");
                    String uuid = split[1];
                    if (!uuid.equalsIgnoreCase(player.getUniqueID().toString())) {

                        String name = JoinListener.playerMap.get(UUID.fromString(uuid)).getName();
                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFancyText("&eGrace Period activated! Only " + name + " can battle this Pokemon!"));
                        break;

                    }

                }

            }

        }

    }

    @SubscribeEvent
    public void onCatch (CaptureEvent.SuccessfulCaptureEvent event) {

        EntityPixelmon pokemon = event.getPokemon();
        if (pokemon.isGlowing()) {

            pokemon.setGlowing(false);

        }
        if (EnumSpecies.legendaries.contains(pokemon.getPokemonName()) || EnumSpecies.ultrabeasts.contains(pokemon.getPokemonName()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

            pokemon.getTags().forEach(tag -> {

                if (tag.equalsIgnoreCase("SpawnedLegendary")) {

                    try {

                        LegendaryListing.updatePokemonStatusCaptured(pokemon, event.getPlayer());

                    } catch (ObjectMappingException e) {

                        e.printStackTrace();

                    }

                }

            });

        }

    }

}
